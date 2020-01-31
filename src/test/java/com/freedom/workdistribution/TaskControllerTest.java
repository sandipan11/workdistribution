package com.freedom.workdistribution;

import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import com.freedom.workdistribution.dao.TaskDistributionDAOImpl;
import com.freedom.workdistribution.model.Agent;
import com.freedom.workdistribution.model.Task;
import com.freedom.workdistribution.model.TaskDistributionRequest;
import com.freedom.workdistribution.model.TaskDistributionResponse;
import com.freedom.workdistribution.services.controller.TaskRestController;

import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.mockito.Matchers.any;

public class TaskControllerTest {
  
	  @Test
	    public void testCreateTask() throws Exception{
		  
	    		TaskRestController controller = new TaskRestController();
	    		TaskDistributionDAOImpl taskDistributionDAOImpl = PowerMockito.mook(TaskDistributionDAOImpl.class);
	    		TaskDistributionRequest req = new TaskDistributionRequest();
	    		Task task = new Task();
	    		task.setTask_id(100);
	    		task.setName("Testl");
	    		task.setPriority (1);
	    		req.setTask_details(task);
	    		
	    		Agent agent = new Agent();
	    		agent.setAgent_id(100);
	    		agent.setAgent_name("test");
	    		HashMap<String, String> allParams = new HashMap<String, String>();
	    		allParams.put ("request id", "2");

	    		when(taskDistributionDAOImpl.createTask(any(Task.class))).thenReturn(task);
	    		ResponseEntity<TaskDistributionResponse> responseEntity= controller.createTask (req, allParams);
	    		Assert.assertEquals(responseEntity.getStatusCodeValue(),500) ;
	    		Assert.assertTrue(responseEntity.getHeaders().getLocation().getPath().contains("/task/create"));
	    		Assert.assertTrue( responseEntity.getBody().getTask_details().getPriority ()==1);
	    }
	     
}
