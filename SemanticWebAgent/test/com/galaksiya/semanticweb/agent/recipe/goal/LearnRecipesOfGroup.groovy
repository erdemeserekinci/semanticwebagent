package com.galaksiya.semanticweb.agent.recipe.goal

import com.galaksiya.muse.agent.task.Task
import com.galaksiya.semanticweb.Condition
import com.galaksiya.semanticweb.PostCondition
import com.galaksiya.semanticweb.Precondition
import com.galaksiya.semanticweb.agent.Goal
import com.galaksiya.semanticweb.agent.Ontology
import com.galaksiya.semanticweb.agent.recipe.task.FetchRecipesOfGroupTask

@Ontology(
	uri = "http://www.mutfakonlugu.com/resource/", 
	schema = "http://www.galaksiya/ontologies/recipe.n3", 
	location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/recipe.n3")
class LearnRecipesOfGroup extends Goal {

	def a = []
		
	@Precondition
	public Condition recipesOfAGroupUnkown = knows RecipeGroup,a that hasRecipe is NOT_THING
	
	@PostCondition
	public Condition recipeDetailsKnown = unknows RecipeGroup that hasRecipe is NOT_THING

	@Override
	public Task[] getTasks() {
		def tasks = []
		for (it in a) {
			tasks << new FetchRecipesOfGroupTask(it.toString())
		}
		tasks
	}
}