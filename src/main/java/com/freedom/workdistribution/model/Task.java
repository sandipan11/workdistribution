package com.freedom.workdistribution.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Task {
	
	private String name;
	private int task_id;
	private String description;
	private String status;
	private Date start_date;
	private Date last_modified_date;
	private int priority;

}
