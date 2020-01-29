package com.freedom.workdistribution.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDistributionRequest {
	
	private Task task_details;
	private int[] required_skill_id;

}
