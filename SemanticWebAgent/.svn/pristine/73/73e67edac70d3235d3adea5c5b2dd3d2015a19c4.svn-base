package com.galaksiya.semanticweb.agent.lang

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.vocabulary.RDFS
import com.hp.hpl.jena.vocabulary.XSD

class GTurtleParserTest extends GroovyTestCase {

	static String DIRECTORY_PATH = "/home/erdemeser/workspaceRole/semanticwebagent/SemanticRole/ontologies/"
	String FILE_PATH_DSL = FILE_PATH + "foaftest.ttl"

	String FILE_PATH = FILE_PATH_DSL // "ontologies/organization.owl"
	String BASE_URI = "http://test.com"
	String SCHEMA_URI = "http://galaksiya.com/ontologies/organization.owl"
	def C_PERSON = "${SCHEMA_URI}#Person"
	def C_GROUP = "${SCHEMA_URI}#Group"
	def C_DOCUMENT = "${SCHEMA_URI}#Document"
	def C_PROJECT = "${SCHEMA_URI}#Project"

	def P_NAME = "${SCHEMA_URI}#name"
	def P_WEBBLOG = "${SCHEMA_URI}#webblog"
	def P_HOMEPAGE = "${SCHEMA_URI}#homepage"
	def P_OPENID = "${SCHEMA_URI}#openid"

	OntModel model

	public void setUp() throws Exception {

		model = ModelFactory.createOntologyModel()
		model = ModelFactory.createOntologyModel()

		def clssPerson = model.createClass(C_PERSON)
		def clssGroup = model.createClass(C_GROUP)
		def clssDocument = model.createClass(C_DOCUMENT)
		def clssProject = model.createClass(C_PROJECT)

		def prpName = model.createDatatypeProperty(P_NAME)
		model.add(prpName, RDFS.domain, clssPerson)
		model.add(prpName, RDFS.domain, clssGroup)
		model.add(prpName, RDFS.range, XSD.xstring)

		def prpWebblog = model.createDatatypeProperty(P_WEBBLOG)
		model.add(prpWebblog, RDFS.domain, clssPerson)
		model.add(prpWebblog, RDFS.domain, clssGroup)
		model.add(prpWebblog, RDFS.range, XSD.xstring)

		def prpOpenID = model.createDatatypeProperty(P_OPENID)
		model.add(prpOpenID, RDFS.domain, clssDocument)
		model.add(prpOpenID, RDFS.range, XSD.xstring)

		def prpEndp = model.createDatatypeProperty(P_HOMEPAGE)
		model.add(prpEndp, RDFS.domain, clssProject)
		model.add(prpWebblog, RDFS.range, XSD.xstring)

		model.write ( new FileWriter (new File(FILE_PATH_DSL)), "TURTLE")
	}

	public void testClassParser(){
		def classes = this.parseOntClasses();
		assert 4 == classes.size()
		assert "Person" in classes
		assert "Document" in classes
		assert "Group" in classes
		assert "Project" in classes
	}

	public void testPropertyParser(){
		Map properties = this.parseDatatypeProperties("Person")
		println properties
		assert 2 == properties.size()
		assert properties.containsKey("name")
		assert properties.containsValue("String")
		assert properties.containsKey("webblog")
		assert properties.containsValue("String")
	}

	def parseDatatypeProperties(String domain) {
	//String to be scanned to find the pattern.
		FileReader fileReader = new FileReader(FILE_PATH)
		List lines = fileReader.readLines()
		Iterator it = lines.iterator()
		Map names = [:];
		while(it.hasNext()) {
			String line = it.next()
			def strReourcePattern = "<.*#(.*)>"
			def resourceMatcher = line =~ strReourcePattern
			if(resourceMatcher.matches() && it.hasNext()){
				def subject = resourceMatcher.group(1)
				// is it a owl class?
				line = it.next()
				def strTypePattern = "\\s*a\\s*owl:DatatypeProperty\\s*[.;]"
				def typeMatcher = line =~ strTypePattern
				// then find its domain and range...
				if(typeMatcher.matches() && it.hasNext()){
					line = it.next()
					//find domain;
					def strDomainPattern = "\\s*rdfs:domain.*"
					def domainMatcher = line =~ strDomainPattern
					if(domainMatcher.matches()){
						line = line.substring(line.indexOf("rdfs:domain")+11).trim()
						String[] domainUris = line.split("(\\s?,\\s?)")
						def domains = []
						domainUris.each {
							def strHashPattern = "(.*#(.*)>.*)"
							def resourceNameMather = it =~ strHashPattern
							if (resourceNameMather.matches()) {
								domains.add(resourceNameMather.group(2))
							}
							domains
						}		
						if(domain in domains)
							names.put(subject, "String")
					}
				}
			}
		}
		return names;
	}

	def parseOntClasses() {
		//String to be scanned to find the pattern.
		FileReader fileReader = new FileReader(FILE_PATH)
		List lines = fileReader.readLines()
		Iterator it = lines.iterator()
		List names = new ArrayList();
		while(it.hasNext()) {
			String line = it.next()
			def strReourcePattern = "<.*#(.*)>"
			def resourceMatcher = line =~ strReourcePattern
			if(resourceMatcher.matches() && it.hasNext()){
				def subject = resourceMatcher.group(1)
				// is it a owl class?
				line = it.next()
				def strTypePattern = "\\s*a\\s*owl:Class\\s*[.;]"
				def typeMatcher = line =~ strTypePattern
				def result = typeMatcher.matches();
				// then add to names list
				if(result)
					names.add subject
			}
		}
		return names;
	}

	@Override
	protected void tearDown() throws Exception {
		//		new File(FILE_PATH).delete()
	}
	
	static main(args){
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM)
		FileReader reader = new FileReader(DIRECTORY_PATH + "recipe.owl")
		model.read(reader,null, "TURTLE")
		model.write ( new FileWriter (new File(DIRECTORY_PATH + "recipe.owl")), "N-TRIPLE")
	}
}
