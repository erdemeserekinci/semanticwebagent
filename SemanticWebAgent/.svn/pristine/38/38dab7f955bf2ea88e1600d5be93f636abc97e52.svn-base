package com.galaksiya.semanticweb.agent.recipe.task;

import com.galaksiya.semanticweb.agent.GTask
import com.galaksiya.semanticweb.agent.Ontology
import com.galaksiya.semanticweb.agent.recipe.MutfakOnluguFetcher
import com.hp.hpl.jena.rdf.model.Property
import com.hp.hpl.jena.rdf.model.Resource
import com.hp.hpl.jena.rdf.model.Statement


@Ontology(uri = "http://www.mutfakonlugu.com/resource/", 
	schema = "http://www.galaksiya/ontologies/recipe.n3", 
	location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/recipe.n3")
class FetchRecipesOfGroupTask extends GTask{

	MutfakOnluguFetcher fetcher;
	private String recipeGroupUri;

	public FetchRecipesOfGroupTask(String recipeGroup) {
		super(FetchRecipesOfGroupTask.class.getName());
		this.fetcher = new MutfakOnluguFetcher()
		this.recipeGroupUri = recipeGroup
	}

	@Override
	public void execute() throws Exception {
		//fetch recipe
		try{
			def allRecipes = fetcher.fetchRecipeList(recipeGroupUri)
			//TODO object property yönetmenin bir yolu bulanacak!
			Resource rscGroup = this.model.getResource(recipeGroupUri)
			Property hasNameProp = this.model.createProperty("http://www.galaksiya/ontologies/recipe.n3"+"#hasRecipe")			
			allRecipes.each{
				String recipeName = it.key
				String recipeUri = "http://" + it.value
				recipeUri = recipeUri.replace(" ", "%20")
				Resource rscRecipe = this.model.createRecipe recipeUri, {
					hasName recipeName
				} 				
				Statement st = this.model.createStatement(rscGroup,hasNameProp,rscRecipe)
				this.model.add(st)
			}
			this.logger.trace "Recipes fetched -> ${allRecipes.size()}"
		}catch(Exception ex){
			ex.printStackTrace()
		}
	}
}