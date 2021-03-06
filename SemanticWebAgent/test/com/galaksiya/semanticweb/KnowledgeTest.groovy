package com.galaksiya.semanticweb;

import org.junit.Ignore

import com.galaksiya.semanticweb.agent.Ontology
import com.galaksiya.semanticweb.agent.SemanticBridgeInjector
import com.hp.hpl.jena.ontology.Individual
import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.StmtIterator
import com.hp.hpl.jena.util.iterator.ExtendedIterator

@Ontology(uri = "http://www.testtask.com", schema = "http://www.galaksiya/ontologies/organization.n3" ,location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/organization.n3")
class KnowledgeTest extends GroovyTestCase {
	String baseUri = "http://www.testtask.com"
	String schemaUri = "http://www.galaksiya/ontologies/organization.n3"

	GShortMemory knowledge
	OntModel schemaModel

	Resource rscPerson
	Property prpWebblog

	public void setUp() throws Exception {
		//initiate model
		SemanticBridgeInjector injector = new SemanticBridgeInjector(uri:baseUri, schema:schemaUri)
		injector.inject(GShortMemory.metaClass)
		knowledge = new GShortMemory(uri:baseUri,
				schema:schemaUri)
		//initiate schema
		schemaModel = ModelFactory.createOntologyModel()
		FileReader fileReader = new FileReader(new File("./ontologies/organization.n3"))
		schemaModel.read(fileReader,null, "N-TRIPLE")
		// load concepts those will be used in tests from schema
		rscPerson = schemaModel.getResource("http://www.galaksiya/ontologies/organization.n3#Person")
	}

	public void testIndividualCreation(){
		//execute
		knowledge.createPerson {}
		//Assert triple count
		this.assertEquals("Triple size after org:Person individual creation", 1,knowledge.listStatements().size())
		//Assert individual count
		ExtendedIterator it = knowledge.listIndividuals(rscPerson)
		this.assertEquals("Individual size after org:Person individual creation", 1,it.size())
	}

	public void testIndividualCreationWithSpecificURI(){
		String givenUri = "http://www.testtask.com#givenUri"
		//execute
		knowledge.createPerson givenUri, {}
		//Assert triple count
		this.assertEquals("Triple size after org:Person individual creation", 1,knowledge.listStatements().size())
		//Assert individual count
		ExtendedIterator it = knowledge.listIndividuals(rscPerson)
		this.assertEquals("Individual size after org:Person individual creation", 1,it.size())
		//assert given uri
		it = knowledge.listIndividuals(rscPerson)
		this.assertEquals("Uri of created individual ", givenUri ,it.next().toString())
	}

	public void testIndividualCreationWithDatatypeProperty(){
		//execute
		knowledge.createPerson "http://www.testtask.com#givenURI", { webblog "http://galaksiya.com/erdemeserekinci" }

		//assert
		ExtendedIterator<Individual> it = knowledge.listIndividuals(rscPerson)
		Individual indv = it.next()

		//Assert size of properties
		StmtIterator listOfProperties = indv.listProperties()
		this.assertEquals("Property size after org:Person individual creation", 2,listOfProperties.size());
		this.assertEquals("org:Webblog property value of org:Person individual creation", "http://galaksiya.com/erdemeserekinci",indv.getPropertyValue(prpWebblog).toString());
	}

	public void testIndividualCreationWithObjectProperty(){
		//execute
		Resource rscPerson = knowledge.createPerson { }
		Resource rscOrganization = knowledge.createOrganization {hasMember rscPerson}
		//assert
		ExtendedIterator<Individual> it = knowledge.listIndividuals(rscPerson)
		Individual indv = it.next()

		//Assert size of properties
		StmtIterator listOfProperties = indv.listProperties()
		this.assertEquals("Property size after org:Person individual creation", 2,listOfProperties.size());
		this.assertEquals("org:Webblog property value of org:Person individual creation", "http://galaksiya.com/erdemeserekinci",indv.getPropertyValue(prpWebblog).toString());
	}
}