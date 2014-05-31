package com.galaksiya.semanticweb.agent.recipe;

import org.codehaus.groovy.runtime.GStringImpl;
import org.htmlparser.Parser
import org.htmlparser.filters.TagNameFilter
import org.htmlparser.util.SimpleNodeIterator



class MutfakOnluguFetcherTest extends GroovyTestCase{
	
	private static String URI_ISPANAK_CORBASI = "http://www.mutfakonlugu.com/tarif-Ispanak-Corbasi-592.html"
	private static String RECEIPE_GROUP = "http://www.mutfakonlugu.com/tarifler.php?tur=1"
	private static String FISH_GROUP = "http://www.mutfakonlugu.com/tarifler.php?tur=1&yID=30&ya_id=70&yadi=Bal%C4%B1k%20ve%20Deniz%20Mahs%C3%BClleri&view=list"
	public void testReceipeFetcher(){
		MutfakOnluguFetcher fetcher = new MutfakOnluguFetcher()
		def ispanakRecipe = fetcher.fetchSingleRecipe(URI_ISPANAK_CORBASI)		
		assert "Ispanak Çorbası" == ispanakRecipe.name
		assert "4 Kişilik" == ispanakRecipe.details."Kaç Kişilik?"
		assert "Tencere" == ispanakRecipe.details."Pişirme Şekli"
		assert "20 dk" == ispanakRecipe.details."Hazırlama Süresi"
		assert "30 dk" == ispanakRecipe.details."Pişirme Süresi"
		assert "0 dk" == ispanakRecipe.details."Dinlenme Süresi"
		assert "50 dk" == ispanakRecipe.details."Toplam Süre"
	}
	
	public void testGroupFetcher(){
		MutfakOnluguFetcher fetcher = new MutfakOnluguFetcher()
		def allGroups = fetcher.fetchRecipeGroups(RECEIPE_GROUP)
		println "**** Groups ****"
		allGroups.each{
			println "Key:${it.key} Value: ${it.value}"
		}
	}
	
	public void testListFetcher(){
		MutfakOnluguFetcher fetcher = new MutfakOnluguFetcher()
		def allRecipes = fetcher.fetchRecipeList(FISH_GROUP)
		println "**** Recipes ****"
		allRecipes.each{
			println "Key:${it.key} Value: ${it.value}"
		}
	}
}
