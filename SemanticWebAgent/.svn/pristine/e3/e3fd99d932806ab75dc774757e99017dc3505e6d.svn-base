package com.galaksiya.semanticweb.agent.lang

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.vocabulary.RDFS
import com.hp.hpl.jena.vocabulary.XSD

class GN3ParserTest extends GroovyTestCase {

	String FILE_PATH_DSL = "/home/erdemeser/workspaceRole/semanticwebagent/SemanticRole/ontologies/organization.n3"

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

		def prpName = model.createDatatypeProperty(P_NAME)
		model.add(prpName, RDFS.domain, clssPerson)
		model.add(prpName, RDFS.domain, clssGroup)
		model.add(prpName, RDFS.range, XSD.xstring)

		def prpWebblog = model.createDatatypeProperty(P_WEBBLOG)
		model.add(prpWebblog, RDFS.domain, clssPerson)
		model.add(prpWebblog, RDFS.domain, clssGroup)
		model.add(prpWebblog, RDFS.range, XSD.xstring)

		def clssDocument = model.createClass(C_DOCUMENT)
		def prpOpenID = model.createDatatypeProperty(P_OPENID)
		model.add(prpOpenID, RDFS.domain, clssDocument)
		model.add(prpOpenID, RDFS.range, XSD.xstring)

		def clssProject = model.createClass(C_PROJECT)
		def prpEndp = model.createDatatypeProperty(P_HOMEPAGE)
		model.add(prpEndp, RDFS.domain, clssProject)
		model.add(prpWebblog, RDFS.range, XSD.xstring)

		model.write ( new FileWriter (new File(FILE_PATH_DSL)), "N-TRIPLE")
//		model.write System.out, "N-TRIPLE"
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
		def lines = fileReader.readLines()
		def map = [:];
		lines.each {
			def strPattern = NTRIPLE_REGEX
			def m = it =~ strPattern
			m.matches()
			def subject = m.group(1)
			def predicate = m.group(2)
			def object = m.group(3)
			if(predicate == "domain" && object == domain){
				map.put(subject, "String")
			}
		}
		return map;
	}

	def parseOntClasses() {
		//String to be scanned to find the pattern.
		FileReader fileReader = new FileReader(FILE_PATH)
		def lines = fileReader.readLines()
		List names = new ArrayList();
		lines.each {
			def strPattern = NTRIPLE_REGEX
			def m = it =~ strPattern
			m.matches()
			def subject = m.group(1)
			def predicate = m.group(2)
			def object = m.group(3)
			if(predicate == "type" && object == "Class")
				names.add(subject)
		}
		return names;
	}
	
	def NTRIPLE_REGEX = "<.*[#/](.*)> <.*[#/](.*)> <.*[#/](.*)> ."

	@Override
	protected void tearDown() throws Exception {
		//		new File(FILE_PATH).delete()
	}
}
