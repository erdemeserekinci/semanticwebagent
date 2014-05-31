package com.galaksiya.semanticweb.agent.recipe.task;

import com.galaksiya.semanticweb.agent.GTask
import com.galaksiya.semanticweb.agent.Ontology
import com.galaksiya.semanticweb.agent.recipe.MutfakOnluguFetcher



@Ontology(uri = "http://www.mutfakonlugu.com/resource/",
schema = "http://www.galaksiya/ontologies/recipe.n3",
location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/recipe.n3")
class FetchRecipeDetailsTask extends GTask{

	MutfakOnluguFetcher fetcher;
	private String recipeUri;

	public FetchRecipeDetailsTask(String recipeUri) {
		super(FetchRecipeDetailsTask.class.getSimpleName());
		this.fetcher = new MutfakOnluguFetcher()
		this.recipeUri = recipeUri
	}

	@Override
	public void execute() throws Exception {
		//fetch recipe
		try{
			def recipe = this.fetcher.fetchSingleRecipe(this.recipeUri);
			this.model.createRecipe recipeUri, {
				hasName recipe.name
				hasSize recipe.details["Kaç Kişilik?"]
				hasPreparetiontime recipe.details["Hazırlama Süresi"]
				hasCookingtype recipe.details["Pişirme Şekli"]
				hasCookingduration recipe.details["Pişirme Süresi"]
				hasRestduration recipe.details["Dinlenmes Süresi"]
				hasTotalduration recipe.details["Toplam Süre"]
			}
			this.logger.trace "Recipe Details fetched -> ${recipe.name}"
		}catch(Exception ex){
			ex.printStackTrace()
		}
	}
}