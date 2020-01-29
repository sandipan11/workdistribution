package com.freedom.workdistribution.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAssignment {
	
	private String task_id;
	private String agent_id;
	private String status;
	private String comments;
	private Date end_date;
	private Date start_date;
	private Date last_modified_date;

}
