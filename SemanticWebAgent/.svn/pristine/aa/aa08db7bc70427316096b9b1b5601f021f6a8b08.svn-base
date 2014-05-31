package com.galaksiya.semanticweb.agent

import groovy.util.logging.Log;
import groovy.util.logging.Log4j;

import com.galaksiya.semanticweb.TripleResolver
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.vocabulary.RDF

@Log4j
class SemanticBridgeInjector {
	String uri
	String schema

	def inject = {MetaClass modelClass->
			log.debug("Injecting dynamic methods 2 ShortMemory")
			modelClass.methodMissing = mMissing
			modelClass.propertyMissing = pMissing
	}
	
	/**
	 * When a method is not recognized, assume it is a triple for a new triple. Create a simple
	 * triple that contains the method name and the parameter which is the body.
	 */
	def mMissing = {String methodName, args ->
		if(methodName.contains("create")){
			log.trace "Semantic operation detected: method: $methodName args: $args"
			//prepare parameters
			String uriSubject;
			def closure;
			if(args.length>1){
				uriSubject = args[0]
				closure = args[1]
			}else{
				uriSubject = uri + System.currentTimeMillis()
				closure = args[0]
			}
			//prepare uri's
			String uriObject = schema + "#" + methodName.substring(methodName.indexOf("create")+6)

			//initialize lists and type triple.
			Resource subject = createResource(uriSubject)
			Resource object = createResource(uriObject)
			log.debug "Creating triple $subject ${RDF.type} $object"
			add(subject, RDF.type, object)

			// if there is any argument then assume that it is a closure, next try to execute
			if(closure != null){
				TripleResolver tripleList = new TripleResolver(subject: subject, schema:schema, model: delegate)
				//change closure delegation
				closure.resolveStrategy = Closure.DELEGATE_FIRST
				closure.delegate = tripleList
				//execute closure
				closure()
			}
			subject
		}
	}
	
	
	def pMissing = { name -> delegate.name }
}
