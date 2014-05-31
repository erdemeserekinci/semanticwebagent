package com.galaksiya.semanticweb.agent.recipe.task;

import com.galaksiya.semanticweb.agent.GTask
import com.galaksiya.semanticweb.agent.Ontology
import com.galaksiya.semanticweb.agent.recipe.MutfakOnluguFetcher


@Ontology(uri = "http://www.mutfakonlugu.com/resource/",
schema = "http://www.galaksiya/ontologies/recipe.n3",
location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/recipe.n3")
class FetchRecipeGroupTask extends GTask{

	MutfakOnluguFetcher fetcher;
	private String recipeGroupUri;

	public FetchRecipeGroupTask(String recipeGroupUri) {
		super(FetchRecipeGroupTask.class.getName());
		this.fetcher = new MutfakOnluguFetcher()
		this.recipeGroupUri = recipeGroupUri
	}

	@Override
	public void execute() throws Exception {
		//fetch recipe
		try{
			def recipeGroups =  this.fetcher.fetchRecipeGroups(this.recipeGroupUri)
			recipeGroups.each {
				String name = it.key
				String uri = it.value
				uri = "http://"+ uri.replace(" ", "%20")
				this.model.createRecipeGroup uri, {
					hasName name 
				}
			}
			this.logger.trace "Recipe Groups fetched -> Size: ${recipeGroups.size()}"
		}catch(Exception ex){
			ex.printStackTrace()
		}
	}
}