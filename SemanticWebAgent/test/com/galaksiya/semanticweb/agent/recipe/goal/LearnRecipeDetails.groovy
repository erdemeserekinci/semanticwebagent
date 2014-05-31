package com.galaksiya.semanticweb.agent.recipe.goal

import com.galaksiya.muse.agent.task.Task
import com.galaksiya.semanticweb.Condition
import com.galaksiya.semanticweb.PostCondition
import com.galaksiya.semanticweb.Precondition
import com.galaksiya.semanticweb.agent.Goal
import com.galaksiya.semanticweb.agent.Ontology
import com.galaksiya.semanticweb.agent.recipe.task.FetchRecipeDetailsTask

@Ontology(
	uri = "http://www.mutfakonlugu.com/resource/", 
	schema = "http://www.galaksiya/ontologies/recipe.n3", 
	location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/recipe.n3")
class LearnRecipeDetails extends Goal {

	def a = []
		
	@Precondition
	public Condition recipeDetailsUnkown = 	knows Recipe,a that hasCookingtype is NOT_THING
	
	@PostCondition
	public Condition recipeDetailsKnown = 	unknows RecipeGroup that hasRecipe is NOT_THING

	@Override
	public Task[] getTasks() {
		def tasks = []
		a.each {
			tasks << new FetchRecipeDetailsTask(it.toString())
		}
		tasks
	}
}