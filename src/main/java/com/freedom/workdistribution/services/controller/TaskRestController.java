package com.freedom.workdistribution.services.controller;


import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freedom.workdistribution.exception.NoAgentAvailableException;
import com.freedom.workdistribution.exception.NoTaskFoundException;
import com.freedom.workdistribution.model.TaskDistributionRequest;
import com.freedom.workdistribution.model.TaskDistributionResponse;
import com.freedom.workdistribution.services.impl.TaskServiceImpl;

/**
 * @author Sandipan
 * 
 * This class acts as controller, exposed two rest end point 
 *
 */
@RestController
public class TaskRestController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class);
	
	@Autowired
	TaskServiceImpl serviceImpl;
	
	/**
	 * This method is to handle task creation request
	 * @param request
	 * @param allParams
	 * @return
	 */
	@RequestMapping(value = "/task/create", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<TaskDistributionResponse> createTask(@RequestBody final TaskDistributionRequest request,@RequestParam HashMap<String,String> allParams) {
		
		ResponseEntity<TaskDistributionResponse> resEntity;
		TaskDistributionResponse response = new TaskDistributionResponse();
		if(allParams.get("request_id") != null) {			
			
			try{
				
				String message = "Task has been created Successfully!!";
				response = serviceImpl.createTask(request,allParams.get("request_id"));
				response.setMessage(message);
				resEntity = new ResponseEntity<>(response,HttpStatus.OK);
				LOGGER.info(message);
			
			} catch (NoAgentAvailableException naae) {
				
				LOGGER.error(naae.getMessage());
				response.setMessage(naae.getMessage());
				resEntity = new ResponseEntity<>(response,HttpStatus.PARTIAL_CONTENT);
				
			} catch (NoTaskFoundException ntfe) {
				
				LOGGER.error(ntfe.getMessage());
				response.setMessage(ntfe.getMessage());
				resEntity = new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
				
			} catch (Exception e) {
				
				LOGGER.error(e.getMessage());
				response.setMessage("Not able to process this request:" + allParams.get("request_id"));
				resEntity = new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return resEntity;
			
		} else {
			
			String message = "Bad Request!! Request id is missing";
			LOGGER.error(message);
			response.setMessage(message);
			resEntity = new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
			return resEntity;
		}

	}

	/**
	 * This method responsible to handle request for update Task status
	 * @param request
	 * @param allParams
	 * @return
	 */
	@RequestMapping(value = "/task/update", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<TaskDistributionResponse> updateTask(@RequestBody final TaskDistributionRequest request,@RequestParam HashMap<String,String> allParams) {
		
		TaskDistributionResponse response =  new TaskDistributionResponse();
		ResponseEntity<TaskDistributionResponse> resEntity;
		
		if(allParams.get("request_id") != null) {
			
			try{
				String message = "Task has been update Successfully!!";
				response = serviceImpl.updateTaskStatus(request,allParams.get("request_id"));
				response.setMessage(message);
				resEntity = new ResponseEntity<>(response,HttpStatus.OK);
				LOGGER.info(message);
			} catch (NoTaskFoundException ntfe) {
				
				LOGGER.error(ntfe.getMessage());
				response.setMessage(ntfe.getMessage());
				resEntity = new ResponseEntity<>(response,HttpStatus.NO_CONTENT);
				
			} catch (Exception e) {
				
				LOGGER.error(e.getMessage());
				response.setMessage("Not able to process this request:" + allParams.get("request_id"));
				resEntity = new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			return resEntity;
		} else {
			
			String message = "Bad Request!! Request id is missing";
			LOGGER.error(message);
			response.setMessage(message);
			return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
		}
	}

}
