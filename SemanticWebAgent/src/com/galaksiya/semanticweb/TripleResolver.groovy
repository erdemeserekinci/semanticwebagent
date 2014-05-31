package com.galaksiya.semanticweb;

import groovy.util.logging.Log
import groovy.util.logging.Log4j;

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.Literal
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.Resource


@Log4j
class TripleResolver {
	Resource subject
	String schema
	OntModel model

	/**
	 * When a method is not recognized, assume it is a triple for a new triple. Create a simple
	 * triple that contains the method name and the parameter which is the body.
	 */
	def methodMissing(String methodName, args) {
		Literal literal = this.model.createLiteral(args[0].toString())
		Property property = this.model.createProperty(this.schema+"#"+methodName)
		log.trace "Creating triple $subject $property $literal"
		model.add(subject, property, literal)
	}
}