package com.galaksiya.semanticweb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.ValidityReport.Report;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * This test class is implemented in order to recognize reasoning details.
 * 
 * @author Erdem Eser Ekinci
 * @company Galaksiya Bilişim Teknolojileri
 * 
 */
public class OntologyValidation {
	private static final String URI_SCHEMA = "http://www.galaksiya.com/ontologies/galaxia.owl";
	private static final String URI_INDV = "http://www.galaksiya.com/ontologies/recsys.owl";

	private static final String PATH_SCHEMA = "galaxia.owl";

	private OntModel ontModelSchema;

	// concept and property names
	private static final String GOAL = "Goal";
	private static final String ROLE = "Role";
	private static final String TASK = "Task";
	
	//restriction uris
	private static final String RSTRC_ONE_GOAL = "OnlyOneGoal";
	private static final String RSTRC_ALL_FROM = "AllValuesGoal";
	
	// concept 
	private OntClass clssGoal;
	private OntClass clssRole;
	private OntClass clssTask;
	// properties
	private ObjectProperty propHasGoal;

	@Before
	public void prepareSchema() throws IOException {
		ontModelSchema = ModelFactory
				.createOntologyModel();
		// create concepts
		clssGoal = ontModelSchema.createClass(URI_SCHEMA + "#" + GOAL);
		clssRole = ontModelSchema.createClass(URI_SCHEMA + "#" + ROLE);
		clssTask = ontModelSchema.createClass(URI_SCHEMA + "#" + TASK);

		// clssGoal.addProperty(RDFS.subClassOf, clssRole);

		// create properties
		propHasGoal = ontModelSchema.createObjectProperty(URI_SCHEMA + "#"
				+ "hasGoal");
		propHasGoal.addProperty(RDFS.domain, clssRole);
		propHasGoal.addProperty(RDFS.range, clssGoal);
		// goal is subclass of role property
		// add restrict;
		// an instance of role has at most one goal
		Restriction rstrcMaxOneGoal = ontModelSchema
				.createMaxCardinalityRestriction(null, propHasGoal, 1);
		Restriction rstrcAllValuesFrom = ontModelSchema
				.createAllValuesFromRestriction(null, propHasGoal, clssGoal);

		// derive role class from the restrictions
		clssRole.addSuperClass(rstrcAllValuesFrom);
		clssRole.addSuperClass(rstrcMaxOneGoal);

		ontModelSchema.write(new FileWriter(PATH_SCHEMA));
	}

	@Test
	public void testValidation() {
		// prepare model
		OntModel ontModelIndv = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
		
		String absolutePath = "file://"
				+ new File(PATH_SCHEMA).getAbsolutePath();
		System.out.println(absolutePath);
		ontModelIndv.getDocumentManager().addAltEntry(URI_SCHEMA + "#",
				absolutePath);
		ontModelIndv.setNsPrefix("galaxia", URI_SCHEMA + "#");

		ontModelIndv.add(ontModelSchema);
		// initialize individual
		Individual indvRoleFetcher = ontModelIndv.createIndividual(URI_INDV
				+ "#" + "Fetcher", clssRole);
		Individual indvGoalFetch1 = ontModelIndv.createIndividual(URI_INDV
				+ "#" + "fetchGoal1", clssGoal);
		Individual indvGoalFetch2 = ontModelIndv.createIndividual(URI_INDV
				+ "#" + "fetchGoal2", clssGoal);

		Individual indvTaskFetch1 = ontModelIndv.createIndividual(URI_INDV
				+ "#" + "fetchTask1", clssTask);

		indvRoleFetcher.addProperty(propHasGoal, indvGoalFetch1);
		indvRoleFetcher.addProperty(propHasGoal, indvGoalFetch2);
		indvRoleFetcher.addProperty(propHasGoal, indvTaskFetch1);

		// assert
		System.out.println("****************Schema**********************");
		ontModelSchema.write(System.out, "N3");
		System.out.println("****************Individual**********************");
		ontModelIndv.write(System.out, "N3");

		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(ontModelSchema);
		InfModel infModel = ModelFactory.createInfModel(reasoner, ontModelIndv);

		System.out.println("Validation Reports");
		ValidityReport validity = ontModelIndv.validate();

		if (validity.isValid()) {
			System.out.println("OK");
		} else {
			System.out.println("Conflicts");
			for (Iterator<Report> report = validity.getReports(); report
					.hasNext();) {
				System.out.println(" - " + report.next());
			}
		}
		
		validity = infModel.validate();
		
		if (validity.isValid()) {
			System.out.println("OK");
		} else {
			System.out.println("Conflicts");
			for (Iterator<Report> report = validity.getReports(); report
					.hasNext();) {
				System.out.println(" - " + report.next());
			}
		}
	}

	@Test
	public void testInference() {
		// prepare model
		OntModel ontModelIndv = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);

		String absolutePath = "file://"
				+ new File(PATH_SCHEMA).getAbsolutePath();
		System.out.println(absolutePath);
		ontModelIndv.getDocumentManager().addAltEntry(URI_SCHEMA + "#",
				absolutePath);
		ontModelIndv.setNsPrefix("galaxia", URI_SCHEMA + "#");
		// init individual
		Individual indvRoleFetcher = ontModelIndv.createIndividual(URI_INDV
				+ "#" + "Fetcher", clssRole);
		Individual indvGoalFetch1 = ontModelIndv.createIndividual(URI_INDV
				+ "#" + "fetch1", clssGoal);
		Individual indvGoalFetch2 = ontModelIndv.createIndividual(URI_INDV
				+ "#" + "fetch2", clssGoal);

		indvRoleFetcher.addProperty(propHasGoal, indvGoalFetch1);
		indvRoleFetcher.addProperty(propHasGoal, indvGoalFetch2);

		// assert

		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(ontModelSchema);
		InfModel infmodel = ModelFactory.createInfModel(reasoner, ontModelIndv);

		infmodel.write(System.out, "N3");
	}

	public static void main(String[] args) {
		OntModel m_OWL_MEM = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM);
		OntModel m_MICRO_RULE = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

		String NS = "http://example.org/test#";
		OntClass c0 = m_MICRO_RULE.createClass(NS + "C0");
		OntClass c1 = m_MICRO_RULE.createClass(NS + "C1");

		// :i rdf:type :c0, :c1.
		Individual i = m_MICRO_RULE.createIndividual(NS + "i", c0);
		i.addRDFType(c1);

		// disjoint classes
		m_OWL_MEM.add(c0, OWL.disjointWith, c1);

		checkValid(m_OWL_MEM, "OWL MEM, before");
		checkValid(m_MICRO_RULE, "OWL McR, before");

		m_MICRO_RULE.addSubModel(m_OWL_MEM);

		checkValid(m_OWL_MEM, "OWL MEM, after");
		checkValid(m_MICRO_RULE, "OWL McR, after");
	}

	private static void checkValid(OntModel m, String msg) {
		ValidityReport validity = m.validate();
		String v = (validity == null) ? "null" : Boolean.toString(validity
				.isValid());
		System.out.println("Model " + msg + " isValid() => " + v);
	}
}