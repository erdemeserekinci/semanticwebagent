package com.galaksiya.semanticweb.agent;

import com.galaksiya.semanticweb.Condition
import com.galaksiya.semanticweb.Nothing
import com.galaksiya.semanticweb.agent.recipe.goal.LearnRecipeGroupsGoal
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.ResourceFactory

class ConditionTest extends GroovyTestCase {
	Condition self = new Condition();

	Condition knows(Resource resource){
		return self.knows(resource)
	}

	public void testSimpleCondition(){
		String uri = "http://www.galaksiya.com"
		Resource Person = ResourceFactory.createResource( "${uri}#Person")
		Resource Male =  ResourceFactory.createResource( "${uri}#Male")
		Resource Female = ResourceFactory.createResource( "${uri}#Female")
		Property hasName = ResourceFactory.createProperty("${uri}#hasName")
		Property hasChild = ResourceFactory.createProperty("${uri}#hasChild")
		Property hasParent = ResourceFactory.createProperty("${uri}#hasParent")

		knows Person that hasChild whose hasName is "Ahmet"
		//.that(hasChild).whose(hasName).is("Ahmet")
		println self.toString()
	}

	public void testNotExistsCondition(){
		String uri = "http://www.galaksiya.com"
		Resource Person = ResourceFactory.createResource( "${uri}#Person")
		Resource Male =  ResourceFactory.createResource( "${uri}#Male")
		Resource Female = ResourceFactory.createResource( "${uri}#Female")
		Property hasName = ResourceFactory.createProperty("${uri}#hasName")
		Property hasChild = ResourceFactory.createProperty("${uri}#hasChild")
		Property hasParent = ResourceFactory.createProperty("${uri}#hasParent")
		Nothing nothing = new Nothing();

		knows Person that hasName is nothing
		//.that(hasChild).whose(hasName).is("Ahmet")
		println self.toString()
	}

	static main(args){
		LearnRecipeGroupsGoal goal = new LearnRecipeGroupsGoal()

		Condition condition = goal.invokePrecondition();
		println condition

		condition = goal.invokePostcondition();
		println condition
	}
}