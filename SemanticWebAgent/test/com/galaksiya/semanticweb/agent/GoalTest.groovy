package com.galaksiya.semanticweb.agent;

import static org.junit.Assert.*

import com.galaksiya.semanticweb.Condition
import com.galaksiya.semanticweb.agent.data.LearnProjectDetails

class GoalTest extends GroovyTestCase{

	GAgent testAgent;

	/**
	 * Setup method prepares the test fixture such as agents and knowledge-base those will be 
	 * used during goal tests.
	 */
	public void setUp(){
		this.testAgent = new GAgent("testAgent", "conf/sdb.conf", true)
	}

	/**
	 * This test case guarantees that the fundamental elements of a goal such as precondition, postcondition 
	 * and target task can be defined and reached successfully.
	 */
	public void testGoalStructure(){
		LearnProjectDetails goalLearnProjectDeails = new LearnProjectDetails()
		//assert precondition
		Condition precondition = goalLearnProjectDeails.getPrecondition()
		assertNotNull(precondition)
		//assert postcondition 
		Condition postcondition = goalLearnProjectDeails.getPostcondition()
		assertNotNull(postcondition)
	}
	
	
}