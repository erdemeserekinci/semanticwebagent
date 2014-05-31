package com.galaksiya.semanticweb;

import com.hp.hpl.jena.graph.Node
import com.hp.hpl.jena.graph.Triple
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.ResourceFactory
import com.hp.hpl.jena.sparql.algebra.op.OpBGP
import com.hp.hpl.jena.sparql.core.BasicPattern
import com.hp.hpl.jena.sparql.expr.E_NotExists
import com.hp.hpl.jena.sparql.syntax.Element
import com.hp.hpl.jena.sparql.syntax.ElementFilter
import com.hp.hpl.jena.sparql.syntax.ElementGroup
import com.hp.hpl.jena.vocabulary.RDF

class Condition{
	char i = "a";
	List<Object> triples = new ArrayList<Object>()
	List<Object> notExistingTriples = new ArrayList<Object>()
	LinkedHashMap satisfiers;
	QueryType qtype = QueryType.SELECT
	boolean isSatisfied = false;

	Condition that(Resource property){
		Triple lastTriple = triples.last()
		Node varNode = Node.createVariable("" + i ++)
		Triple triple = new Triple(lastTriple.getSubject(),
				property.asNode(), varNode)
		triples.add(triple)
		return this
	}

	Condition knows(Resource resource){
		Node varNode = Node.createVariable(""+ i++)
		Triple triple = new Triple(varNode,
				Node.createURI(RDF.type.getURI()), resource.asNode());
		this.triples.add(triple)
		this
	}
	Condition knows(Resource resource,Collection variableConnection){
		this.satisfiers = variableConnection
		String variableName =  ""+i++
		this.satisfiers.put(variableName,variableConnection)
		Node varNode = Node.createVariable(variableName)
		Triple triple = new Triple(varNode,
				Node.createURI(RDF.type.getURI()), resource.asNode());
		this.triples.add(triple)
		this
	}

	Condition unknows(Resource resource){
		qtype = QueryType.ASK
		Node varNode = Node.createVariable(""+ i++)
		Triple triple = new Triple(varNode,
				Node.createURI(RDF.type.getURI()), resource.asNode());
		triples.add(triple)
		notExistingTriples.add(triple)
		this
	}

	Condition whose(Resource rsc){
		that rsc
	}

	Condition and(Resource rsc){
		that rsc
	}

	Condition is(String value){
		Triple lastTriple = this.triples.last()
		this.triples.remove(lastTriple)
		Triple triple = new Triple(lastTriple.getSubject(), lastTriple.getPredicate(), ResourceFactory.createPlainLiteral(value.toString()).asNode());
		triples.add(triple);
		return this
	}

	Condition is(Condition condition){
		Triple lastTriple = this.triples.last()
		this.triples.remove(lastTriple)
		//create element filter

		Node subject = lastTriple.getSubject()
		Node predicate = lastTriple.getPredicate()
		Node object= Node.createVariable(condition.toString())
		Triple newTriple = new Triple(subject,predicate ,object)

		triples.add(newTriple)
		notExistingTriples.add(newTriple)
		return this
	}

	public String toSparql(){
		String queryType = ""
		if(qtype == QueryType.SELECT){
			queryType = "SELECT * WHERE"
		} else if(qtype == QueryType.ASK){
			queryType = "ASK WHERE"
		}
		return queryType  + this.toString()
	}

	public String toString(){
		ElementGroup elements = new ElementGroup()
		this.triples.each {
			if(!(it in notExistingTriples)){
				elements.addTriplePattern(it)
			}
		}
		this.notExistingTriples.each {
				BasicPattern basicPattern = new BasicPattern()
				basicPattern.add(it)
				OpBGP op = new OpBGP(basicPattern)
				E_NotExists e_NotExists = new E_NotExists(op)
				ElementFilter elementFilter = new ElementFilter(e_NotExists)
				elements.addElement(elementFilter)
		}
		elements.toString()
	}
}