package com.galaksiya.semanticweb.agent.recipe.task;

import com.galaksiya.semanticweb.agent.GTask;
import com.galaksiya.semanticweb.agent.Ontology;


@Ontology(
	uri = "http://www.mutfakonlugu.com/resource/", 
	schema = "http://www.galaksiya/ontologies/recipe.n3", 
	location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/recipe.n3")
class ViewRecipeKnowledgeTask extends GTask{

	public ViewRecipeKnowledgeTask() {
		super(getClass().getSimpleName());
	}

	@Override
	public void execute() throws Exception {
		//fetch recipe
		try{
			this.model.write System.out
			println this.model.listStatements().size()
		}catch(Exception ex){
			ex.printStackTrace()
		}
	}
}