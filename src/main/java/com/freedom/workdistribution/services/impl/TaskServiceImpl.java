package com.freedom.workdistribution.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freedom.workdistribution.dao.TaskDistributionDAOImpl;
import com.freedom.workdistribution.exception.NoAgentAvailableException;
import com.freedom.workdistribution.model.Agent;
import com.freedom.workdistribution.model.AgentDetails;
import com.freedom.workdistribution.model.Task;
import com.freedom.workdistribution.model.TaskDistributionRequest;
import com.freedom.workdistribution.model.TaskDistributionResponse;
import com.freedom.workdistribution.model.TaskSkillMapping;
import com.freedom.workdistribution.services.TaskService;

/**
 * @author Sandipan
 *
 *This class is service layer class and containing methods to serve request in service layer and interact with DAO layer
 */
@Service
public class TaskServiceImpl implements TaskService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskServiceImpl.class);

	@Autowired 
	TaskDistributionDAOImpl daoImpl;
	
	/* (non-Javadoc)
	 * @see com.freedom.workdistribution.services.TaskService#createTask(com.freedom.workdistribution.model.TaskDistributionRequest, java.lang.String)
	 * 
	 * This method is responsible to create new task and task -skill mapping
	 */
	@Override
	public TaskDistributionResponse createTask(TaskDistributionRequest request, String request_id) throws Exception {

		LOGGER.info("** START: Task creation **");
		int task_id = createTaskDetails(request);
		AgentDetails agentDetails;
		TaskDistributionResponse response = new TaskDistributionResponse();
		Task task = request.getTask_details();
		task.setTask_id(task_id);
		response.setTask_details(task);
		agentDetails = assignAgentToTask(task_id,request.getRequired_skill_id());
		Agent agent = new Agent();
		agent.setAgent_id(agentDetails.getAgent_id());
		agent.setAgent_name(agentDetails.getAgent_name());
		
		response.setAgent_details(agent);
		
		response.setRequest_id(request_id);
		
		LOGGER.info("** END: Task creation **");
		
		return response;
	}
	
	private int createTaskDetails(TaskDistributionRequest request)throws Exception {
		
		int required_skill_id[] = request.getRequired_skill_id();
		
		Task newlyCreatedTask = daoImpl.createTask(request.getTask_details());
		LOGGER.info("Task Created Successfully : id=" +newlyCreatedTask.getTask_id());
		
		TaskSkillMapping taskskillMapping = new TaskSkillMapping();
		taskskillMapping.setTask_id(newlyCreatedTask.getTask_id());
		
		for(int skill_id: required_skill_id) {
			taskskillMapping.setSkill_id(skill_id);
			
			daoImpl.createTaskSkillsMapping(taskskillMapping);
		}
		LOGGER.info("Task Skill mapping Successfully Created : id=" +newlyCreatedTask.getTask_id());
		
		return newlyCreatedTask.getTask_id();
	}
	
	/**
	 * This method is responsible to assign agent to a task
	 * @param task_id
	 * @param skill_ids
	 * @return
	 * @throws Exception
	 */
	private AgentDetails assignAgentToTask(int task_id, int[] skill_ids)throws Exception {
		
		List<AgentDetails> list= daoImpl.getAgentBySkills(skill_ids);
		LOGGER.info("Get Agent Successfully By Skill id for task_id=" + task_id);
		
		AgentDetails agentDetails = null;
		
		if (!list.isEmpty() && list.size() > 0) {
			agentDetails =  list.get(0);
	    } else {
	    	LOGGER.error("No agent available for the task id: "+ task_id);
	    	throw new NoAgentAvailableException("No agent available for the task id: "+ task_id);
	    }
		
		daoImpl.assignAgentInTask(task_id,agentDetails.getAgent_id());

		return agentDetails;
	}
	
	@Override
	public TaskDistributionResponse updateTaskStatus(TaskDistributionRequest request, String request_id) throws Exception {
		
		LOGGER.info("** START: Task Status Update **");
		
		Task task = request.getTask_details();
		daoImpl.updateTaskStatus(task);
		TaskDistributionResponse response = new TaskDistributionResponse();
		
		daoImpl.assignEndDateInTask(request.getTask_details().getTask_id());
		
		response.setTask_details(task);
		response.setRequest_id(request_id);
		
		daoImpl.assignEndDateInTask(task.getTask_id());
		
		LOGGER.info("** END: Task Status Update **");
		return response;
	}


}
