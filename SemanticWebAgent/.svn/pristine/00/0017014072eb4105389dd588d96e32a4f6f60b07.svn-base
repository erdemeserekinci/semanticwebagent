package com.galaksiya.semanticweb.agent.recipe

import groovy.util.logging.Log4j;

import org.apache.log4j.Level;
import org.htmlparser.Parser
import org.htmlparser.Node
import org.htmlparser.filters.AndFilter
import org.htmlparser.filters.HasAttributeFilter
import org.htmlparser.filters.HasParentFilter
import org.htmlparser.filters.HasSiblingFilter;
import org.htmlparser.filters.TagNameFilter
import org.htmlparser.util.SimpleNodeIterator
import org.htmlparser.util.NodeList

@Log4j
class MutfakOnluguFetcher {
	
	static def HOST = "www.mutfakonlugu.com"		
	
	def fetchSingleRecipe = {String uri ->
		log.info "Fetching page of $uri"
		def recipe = new Expando()
		Parser parser = new Parser(uri)
		recipe.name = fetchName(parser);
		parser.reset()
		recipe.details = fetchRecipeDetail(parser);
		parser.reset()
		recipe.ingredients = fetchIngredients(parser);
		parser.reset()
		fetchImage(parser,recipe)
		return recipe
	}
	
	def fetchRecipeGroups = { String grupUri ->
		log.trace "Fetching recipe groups"
		Map result = [:]
		//match sibling
		TagNameFilter h3TagFilter = new TagNameFilter("h3") 	
		HasSiblingFilter siblingFilter = new HasSiblingFilter(h3TagFilter)		
		// refine tag name filter	
		TagNameFilter liTagFilter = new TagNameFilter("li")
		AndFilter liAndh3Filter = new AndFilter(liTagFilter,siblingFilter)
		//Parse nodes
		Parser parser = new Parser(grupUri)
		NodeList nodeList = parser.parse(liAndh3Filter)
		SimpleNodeIterator iterator = nodeList.elements()
		while(iterator.hasMoreNodes()){
			def node = iterator.nextNode()
			node = node.getChildren().elementAt(0);
			String grup = node.getChildren().elementAt(0).getText().trim() 
			String uri =  HOST +"/"+ node.getAttribute("href")
			uri.replace(" ", "%20")
			result.put(grup, uri) 
		}
		log.debug "Recipe groups fetched"
		result
	}
		
	def fetchRecipeList = {String listUri ->
		log.trace "Fetching recipe groups $listUri"
		Map result = [:]
		//State parent
		TagNameFilter tdFilter = new TagNameFilter("td")
		HasAttributeFilter valignFilter = new HasAttributeFilter("valign")
		AndFilter parentPropertiesFilter = new AndFilter(tdFilter,valignFilter)
		HasParentFilter hasParentFilter = new HasParentFilter(parentPropertiesFilter)
		//find target tah
		TagNameFilter ahrefFilter = new TagNameFilter("a")
		AndFilter targetFilter = new AndFilter(hasParentFilter,ahrefFilter)
		//Parse and return 
		Parser parser = new Parser(listUri)
		def nodeList = parser.parse(targetFilter)
		SimpleNodeIterator iterator = nodeList.elements()
		while(iterator.hasMoreNodes()){
			def node = iterator.nextNode()
			String name = node.getFirstChild().getText()
			String uri = HOST + "/" + node.getAttribute("href")
			result.put(name,uri)
		}		
		log.debug "Recipes of group fetched"
		result
	}

	private String fetchName(Parser parser) {
		log.debug "Fetching recipe name"
		String name;
		TagNameFilter tagNameFilter = new TagNameFilter("h2")
		def nodeList = parser.parse(tagNameFilter)
		SimpleNodeIterator iterator = nodeList.elements()
		while(iterator.hasMoreNodes()){
			name = iterator.nextNode().getFirstChild().getText()
			log.trace name
		}
		name
	}

	def fetchRecipeDetail(Parser parser) {
		log.info "Fetching recipe details"
		Map recipeDetails = [:]
		//Find table
		HasAttributeFilter attFilter = new HasAttributeFilter("class","tarifdetay")
		HasParentFilter parentTableFilter = new HasParentFilter(attFilter)
		//Find row
		TagNameFilter trTagNameFilter = new TagNameFilter("tr")
		AndFilter andFilter = new AndFilter(parentTableFilter,trTagNameFilter)
		//Find header
		HasParentFilter parentTrFilter = new HasParentFilter(andFilter)
		TagNameFilter tagNameFilterTh = new TagNameFilter("th")
		AndFilter thTagNameAndtrParentFilter = new AndFilter(parentTrFilter,tagNameFilterTh)
		//Find property names
		def nodeList = parser.parse(thTagNameAndtrParentFilter)
		SimpleNodeIterator iterator = nodeList.elements()
		while(iterator.hasMoreNodes()){
			Node node = iterator.nextNode()
			String nodeTag = node.getText()
			String firstChildValue = node.getFirstChild().getText()
			if(!firstChildValue.contains(":")){
				String strName = firstChildValue.trim()
				def value = getPropertyValue(node.getParent())
				log.debug "Property name: $strName value: $value"
				if(value != null)
					recipeDetails.put(strName, value)
			}
		}
		recipeDetails
	}

	def getPropertyValue = { Node parentNode ->
		def value
		NodeList nodeList = parentNode.getChildren();
		SimpleNodeIterator iterator = nodeList.elements()
		while(iterator.hasMoreNodes()){
			Node node = iterator.nextNode()
			String nodeTag = node.getText()
			if(nodeTag == "td"){
				value =  node.getFirstChild().getText().trim()
				log.info "Property Value: $value"
			}
		}
		value
	}

	def fetchIngredients = {Parser parser->
		//Find content parent
		def ingredients = []
		TagNameFilter divTagNameFilter = new TagNameFilter("div");
		HasAttributeFilter contentAttFilter = new HasAttributeFilter("class","content");
		AndFilter contentDivFilter = new AndFilter(divTagNameFilter,contentAttFilter);
		HasParentFilter grandParentFilter = new HasParentFilter(contentDivFilter)
		//Find list parent
		TagNameFilter ulTagNameFilter = new TagNameFilter("ul");
		HasAttributeFilter genListtAttFilter = new HasAttributeFilter("class","genlist");
		AndFilter parentAndFilter = new AndFilter(ulTagNameFilter,genListtAttFilter)
		
		HasParentFilter ulParentFilter = new HasParentFilter(new AndFilter(parentAndFilter,grandParentFilter))
		//Find field
		TagNameFilter liTagNameFilter = new TagNameFilter("li");
		AndFilter andFilter = new AndFilter(liTagNameFilter,ulParentFilter);
		//Start to fetch"
		def nodeList = parser.parse(andFilter)
		SimpleNodeIterator iterator = nodeList.elements()
		while(iterator.hasMoreNodes()){
			Node node = iterator.nextNode()
			String nodeTag = node.getText()
			String firstChildValue = node.getFirstChild().getText()
			if(!firstChildValue.contains(":")){
				def strIngredients = firstChildValue.trim()
				log.trace "Ingredients: $strIngredients " 
				ingredients<<strIngredients
			}
		}
		ingredients
	}
	
	def fetchImage = { Parser parser, receipe ->
		//Find img by tag name and id = "stroke" properties
		TagNameFilter imgFilter = new TagNameFilter("img")
		HasAttributeFilter idFilter = new HasAttributeFilter("id","stroke")
		AndFilter andFilter = new AndFilter(imgFilter,idFilter)
		//
		def nodeList = parser.parse(andFilter)
		def node = nodeList.elementAt(0)
		receipe.image = HOST +  node.getAttribute("src");
		log.trace receipe.image
	}
}