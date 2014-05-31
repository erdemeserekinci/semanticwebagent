

package com.galaksiya.semanticweb.agent


import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.MethodNode

import com.galaksiya.muse.agent.task.Task
import com.galaksiya.semanticweb.Condition
import com.galaksiya.semanticweb.PostCondition
import com.galaksiya.semanticweb.Precondition
import com.galaksiya.semanticweb.QueryType
import com.hp.hpl.jena.query.QueryExecution
import com.hp.hpl.jena.query.QueryExecutionFactory
import com.hp.hpl.jena.query.QuerySolution
import com.hp.hpl.jena.query.ResultSet
import com.hp.hpl.jena.query.ResultSetFactory
import com.hp.hpl.jena.query.ResultSetRewindable
import com.hp.hpl.jena.rdf.model.ModelFactory
import com.hp.hpl.jena.rdf.model.Resource


abstract class Goal extends GTask{
	abstract Closure perform;
	
	GAgent agent
	
	abstract public Task[] getTasks();

	Goal(){
		super(Goal.class.getSimpleName())
	}

	public resolveGraphs(){
		println "ClassNode " + this.metaClass.classNode
		this.metaClass.classNode.annotations.each {
			uri = it.getMembers().uri.getText()
			schema = it.getMembers().schema.getText()
			schemaLocation = it.getMembers().location.getText()
		}
		// initiate schema
		this.schemaModel = ModelFactory.createOntologyModel();
		if(schema != null){
			FileReader reader = new FileReader(new File(schemaLocation))
			this.schemaModel.read(reader,null,"N-TRIPLE")
		}

		logger.debug "Annotation resolved: uri: $uri, schema: $schema"
		this.addGraphURI(uri, null);
	}

	public Condition knows(Resource resource, List variable){
		Condition condition =new Condition()
		return condition.knows(resource,variable)
	}
	
	public Condition knows(Resource resource){
		Condition condition =new Condition()
		return condition.knows(resource)
	}

	public Condition unknows(Resource resource){
		Condition condition =new Condition()
		return condition.unknows(resource)
	}
	public Condition invokePrecondition(){
		return invokeAnnotatedMethod(Precondition.class.getName())
	}

	public Condition getPrecondition(){
		return getAnnotatedField(Precondition.class.getName())
	}

	public Condition invokePostcondition(){
		return invokeAnnotatedMethod(PostCondition.class.getName())
	}

	public Condition getPostcondition(){
		return getAnnotatedField(PostCondition.class.getName())
	}

	@Override
	public void execute() throws Exception {
		/******************* EXECUTION *****************/
			this.logger.trace "Executing tasks of goal -> Goal: ${name}, Tasks: ${getTasks()}"
			def tasks = getTasks()
			tasks.each {  
				agent.addTask(it)
				it.waitUntilFinish()
			}
			/*******************            *****************/
			//refresh model // Bug reloading mustn't be needed
			this.model = this.agent.taskManager.prepareShortMemory(this)
	}

	public checkPrecondition(){
		Condition precondition = this.getPrecondition()
		checkPrecondition(precondition)
		return precondition
	}
	
	//TODO: Check precondition ve postcondition metodları birbirlerinden farklı olmak zorunda değil_?
	public checkPrecondition(Condition precondition) {
		String query = precondition.toSparql()
		this.logger.trace "Checking precondition -> Goal: ${name}, Precondition: ${query}"
		QueryExecution queryExecution = QueryExecutionFactory.create(query,this.model);
		if(precondition.qtype == QueryType.SELECT){
			ResultSet resultSet = queryExecution.execSelect();
			ResultSetRewindable copyResults = ResultSetFactory.copyResults(resultSet)
			if(copyResults.size()>0){
				copyResults.reset()
				precondition.isSatisfied = true
				def vbs = []
				//collect variables
				for (String variable : copyResults.getResultVars()) {
					Set keySet = precondition.satisfiers.keySet()
					if(keySet.contains(variable)){
						vbs.add(variable)
					}
				}
				copyResults.reset()
				this.logger.trace "Preparing satisfiers for variables: ${vbs}"
				for (QuerySolution solution : copyResults) {
					for (a in vbs){
						Resource resource = solution.getResource(a)
						precondition.satisfiers.get(a).add(resource)
					}
				}
			}else
				precondition.isSatisfied = false
		} else if (precondition.qtype == QueryType.ASK){
			precondition.isSatisfied =  queryExecution.execAsk()
		}
		this.logger.debug "Precondition  satisfaction: ${precondition.isSatisfied} -> Goal: ${name}, Precondition: ${query}"
	}

	public checkPostcondition(){
		// Check post condition
		Condition postcondition = this.getPostcondition()
		checkPostcondition(postcondition)
		return postcondition
	}
	
	public checkPostcondition(Condition postcondition) {
		String query = postcondition.toSparql()
		println "---------Checking post condition--------------"
		this.model.write System.out
		this.logger.trace "Checking post -> Goal: ${name}, Postcondition: ${query}"
		QueryExecution queryExecution = QueryExecutionFactory.create(query,this.model);
		if(postcondition.qtype == QueryType.ASK){
			postcondition.isSatisfied =  queryExecution.execAsk()
		} else if(postcondition.qtype == QueryType.SELECT){
			ResultSet resultSet = queryExecution.execSelect();
			ResultSetRewindable copyResults = ResultSetFactory.copyResults(resultSet)
			if(copyResults.size()>0)
				postcondition.isSatisfied = true
			else 
				postcondition.isSatisfied = false
		}
			this.logger.debug "Postcondition checked -> Goal: ${name}, Postcondition: ${query}"
	}

	private Condition getAnnotatedField(String annotationClassName) {
		Condition result = null
		this.metaClass.classNode.fields.each {
			FieldNode node = it
			node.annotations.each {
				if(it.classNode.name == annotationClassName){
					logger.trace "Got field: ${node.name}"
					result =this.metaClass.getAttribute(this,node.getName())
				}
			}
		}
		if(result == null)
			logger.error "Couldn't find annotated field: ${annotationClassName}"
		result
	}

	private Condition invokeAnnotatedMethod(String annotationClassName) {
		Condition result = null
		this.metaClass.classNode.allDeclaredMethods.each{
			MethodNode method = (MethodNode)it
			method.annotations.each {
				AnnotationNode aNode = it;
				if(aNode.classNode.name == annotationClassName){
					logger.trace "Calling method: ${method.name}"
					try{
						result = this.metaClass.invokeMethod(this,method.name,null)
					}catch (Exception e) {
						logger.error "Exception occured during method call", e
						e.printStackTrace()
					}
				}
			}
		}
		if(result == null)
			logger.error "Couldn't find annotated method: ${annotationClassName}"
		return result
	};
}