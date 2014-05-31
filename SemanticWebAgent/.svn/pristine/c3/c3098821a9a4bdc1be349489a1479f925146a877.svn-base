package com.galaksiya.semanticweb.agent

import com.galaksiya.muse.agent.Agent
import com.galaksiya.semanticweb.Condition
import com.hp.hpl.jena.rdf.model.Model
import com.hp.hpl.jena.rdf.model.Resource

class GAgent extends Agent implements Runnable{

	Vector<Goal> goals = new Vector<Goal>()

	Thread dmThread;

	public GAgent(String agentName, String knowledgeConfFilePath, boolean monitoring){
		super(agentName, knowledgeConfFilePath, monitoring)
		dmThread = new Thread(this)
		dmThread.start()
	}

	public Condition knows(Resource resource){
		return new Condition();
	}

	public void adopt(Goal goal){
		goal.agent = this
		goals.add(goal)
		this.logger.debug "Goal adopted: ${goal.name}"
	}

	@Override
	public void run() {
		while(true){
			Vector<Goal> executedGoals = new Vector<Goal>();
			goals.each {
				// prepare model
				Model model = this.taskManager.prepareShortMemory(it)
				it.setModel(model)
				
				// 
				Condition precondition = it.checkPrecondition()
				this.logger.debug "Precondition checked: ${precondition.isSatisfied} , Goal: ${it.name}"
				if(precondition.isSatisfied){
					this.addTask(it)
					it.waitUntilFinish()
				}
				// reload memory
				model = this.taskManager.prepareShortMemory(it)
				it.setModel(model)
				
				//  
				Condition postcondition = it.checkPostcondition()
				this.logger.debug "Postcondition checked: ${postcondition.isSatisfied} , Goal: ${it.name}"
				if(postcondition.isSatisfied){
					executedGoals.add(it)
				}
			}
			this.goals.removeAll(executedGoals)
			Thread.sleep(1000)
		}
	}
}
