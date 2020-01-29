package com.freedom.workdistribution.model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Agent {
	
	private String agent_name;
	private int agent_id;
	private String status;
	private Date last_modified_date;
	
}
