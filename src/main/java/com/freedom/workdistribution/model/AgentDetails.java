package com.freedom.workdistribution.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AgentDetails {

	private int agent_id;
	private int task_id;
	private int priority;
	private Date start_date;
	private String agent_name;
}
