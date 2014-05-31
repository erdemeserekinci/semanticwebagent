package com.galaksiya.semanticweb.agent.recipe.goal


import com.galaksiya.muse.agent.task.Task
import com.galaksiya.semanticweb.Condition
import com.galaksiya.semanticweb.PostCondition
import com.galaksiya.semanticweb.Precondition
import com.galaksiya.semanticweb.agent.Goal
import com.galaksiya.semanticweb.agent.Ontology
import com.galaksiya.semanticweb.agent.recipe.task.FetchRecipeGroupTask

@Ontology(
	uri = "http://www.mutfakonlugu.com/resource/", 
	schema = "http://www.galaksiya/ontologies/recipe.n3", 
	location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/recipe.n3")
class LearnRecipeGroupsGoal extends Goal {

	@Precondition
	Condition pre = unknows RecipeGroup 
	
	@PostCondition
	Condition post = knows RecipeGroup 

	@Override
	public Task[] getTasks() {
		def tasks = []
		tasks << new FetchRecipeGroupTask("http://www.mutfakonlugu.com/tarifler.php?tur=1")
		tasks
	}
}