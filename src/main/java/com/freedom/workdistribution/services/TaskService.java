package com.freedom.workdistribution.services;

import com.freedom.workdistribution.model.TaskDistributionRequest;
import com.freedom.workdistribution.model.TaskDistributionResponse;

public interface TaskService {
	
	public TaskDistributionResponse createTask(TaskDistributionRequest request, String request_id) throws Exception;
	public TaskDistributionResponse updateTaskStatus(TaskDistributionRequest request, String request_id) throws Exception;

}
