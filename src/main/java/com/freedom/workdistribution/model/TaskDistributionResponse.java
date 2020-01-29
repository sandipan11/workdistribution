package com.freedom.workdistribution.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDistributionResponse {
	
	private String request_id;
	private Task task_details;
	private Agent agent_details;
	
	private String message;
	private String response_code;

}
