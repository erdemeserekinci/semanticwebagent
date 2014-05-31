package com.galaksiya.semanticweb.agent.data

import com.galaksiya.muse.agent.task.Task
import com.galaksiya.semanticweb.Condition
import com.galaksiya.semanticweb.PostCondition
import com.galaksiya.semanticweb.Precondition
import com.galaksiya.semanticweb.agent.Goal
import com.galaksiya.semanticweb.agent.Ontology

@Ontology(
uri = "http://www.projects.com/resource/",
schema = "http://www.galaksiya/ontologies/recipe.n3",
location = "/home/erdemeser/workspaceRole/SemanticWebAgent/ontologies/organization.n3")
class LearnProjectDetails extends Goal {

	@Precondition
	public Condition projectDetailsUnkown = knows Project that hasAlias is NOT_THING

	@PostCondition
	public Condition projectDetailsKnown = 	unknows Project that hasAlias is NOT_THING
	
	@Override
	public Task[] getTasks() {
		
	}
}