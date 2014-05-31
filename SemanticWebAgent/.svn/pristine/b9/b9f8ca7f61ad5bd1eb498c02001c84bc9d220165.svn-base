package com.galaksiya.semanticweb;

import java.util.Iterator;

import org.junit.Test;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.reasoner.ValidityReport.Report;

public class OWLRestrictionTest {
	// uri defs
	private static String URI_BASE = "http://www.galaksiya.com/ontologies/galaxia.owl";
	private static String URI_INDV = "http://www.galaksiya.com/ontologies/indv.owl";
	private static String URI_ROLE = "Role";
	private static String URI_GOAL = "Goal";
	private static String URI_TASK = "Task";
	private static String URI_HAS_GOAL = "hasGoal";

	// concepts
	private OntClass clssRole;
	private OntClass clssGoal;
	private OntClass clssTask;

	private ObjectProperty prpHasGoal;

	@Test
	public void test() {
		OntModel schema = ModelFactory
				.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
		// create concepts
		clssRole = schema.createClass(URI_BASE + "#" + URI_ROLE);
		clssGoal = schema.createClass(URI_BASE + "#" + URI_GOAL);
		clssTask = schema.createClass(URI_BASE + "#" + URI_TASK);

		clssGoal.addDisjointWith(clssTask);
		clssTask.addDisjointWith(clssGoal);

		// create properties
		prpHasGoal = schema.createObjectProperty(URI_BASE + "#" + URI_HAS_GOAL);
		// create restrictions
		// Restriction restriction =
		// schema.createSomeValuesFromRestriction(null, prpHasGoal, clssGoal);
		Restriction restriction1 = schema.createSomeValuesFromRestriction(null,
				prpHasGoal, clssGoal);
		Restriction restriction2 = schema.createMaxCardinalityRestriction(null, prpHasGoal, 1);
		
		clssRole.addSuperClass(restriction1);
		clssRole.addSuperClass(restriction2);

		// create individuals
		Individual indvRole1 = schema.createIndividual(URI_INDV
				+ "#FetcherRole1", clssRole);
		Individual indvGoal1 = schema.createIndividual(
				URI_INDV + "#FetchGoal1", clssGoal);
		Individual indvTask1 = schema.createIndividual(
				URI_INDV + "#FetchTask1", clssTask);

		//indvRole1.addProperty(prpHasGoal, indvGoal1);
		indvRole1.addProperty(prpHasGoal, indvTask1);

		// validation
		//schema.write(System.out, "N3");

		ValidityReport validityReport = schema.validate();
		if (validityReport != null) {
			System.out.println("Model consistency: " + validityReport.isValid());
			if(!validityReport.isValid()){
				Iterator<Report> reports = validityReport.getReports();
				int i = 1;
				while(reports.hasNext()){
					System.out.println("\t"+ i++ + " " +reports.next().toString());
				}
			}

		} else {
			System.out.println("No consistency report");
		}
	}
}