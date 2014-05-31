package com.galaksiya.semanticweb.util

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.ModelFactory

class OWL2N3Converter {
	
	static convert(String owlPath, String n3Path){
		OntModel ontModel = ModelFactory.createOntologyModel()
		
		FileReader reader = new FileReader(owlPath)
		ontModel.read reader, "RDF/XML"
		
		FileWriter writer = new FileWriter (new File(n3Path))
		ontModel.write(writer,"N-TRIPLE")
		
	}
	
	static main(args){
		OWL2N3Converter.convert("./ontologies/d2rq.owl", "./ontologies/d2rq.n3")
	}
}
