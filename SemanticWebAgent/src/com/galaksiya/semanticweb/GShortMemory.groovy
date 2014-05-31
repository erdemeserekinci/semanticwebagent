package com.galaksiya.semanticweb;

import groovy.util.logging.Log

import com.galaksiya.muse.agent.memory.ShortMemory
import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.vocabulary.RDF

@Log
class GShortMemory extends ShortMemory{
	static int id = 0;

	String uri;
	String schema;


	/**
	 * Replace this model with short term memory
	 */
	public GShortMemory(){
		super()
	}
}