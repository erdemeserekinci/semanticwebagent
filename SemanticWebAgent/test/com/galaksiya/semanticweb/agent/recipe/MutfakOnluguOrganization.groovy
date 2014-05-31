package com.galaksiya.semanticweb.agent.recipe

import com.galaksiya.semanticweb.agent.GAgent
import com.galaksiya.semanticweb.agent.recipe.goal.LearnRecipeDetails
import com.galaksiya.semanticweb.agent.recipe.goal.LearnRecipeGroupsGoal
import com.galaksiya.semanticweb.agent.recipe.goal.LearnRecipesOfGroup
import com.galaksiya.semanticweb.agent.recipe.task.ViewRecipeKnowledgeTask

class MutfakOnluguOrganization extends GroovyTestCase{

	public MutfakOnluguOrganization() {
		super();
		test();
	}

	public void test(){
		GAgent agent = new GAgent("MutfakOnluguTestAgent", "conf/sdb.conf", true);
		//Add task
		LearnRecipeGroupsGoal goal1 = new LearnRecipeGroupsGoal();
		LearnRecipesOfGroup goal2 = new LearnRecipesOfGroup()
		LearnRecipeDetails goal3 = new LearnRecipeDetails()
		ViewRecipeKnowledgeTask viewerTask = new ViewRecipeKnowledgeTask()

		agent.forget()
		agent.adopt(goal1)
		agent.adopt(goal2)
		agent.adopt(goal3)
		
		while(agent.goals.size()>0)
			Thread.sleep(500)
		
		agent.addTask(viewerTask)
	}

	static main(args){
		new MutfakOnluguOrganization();
	}
}