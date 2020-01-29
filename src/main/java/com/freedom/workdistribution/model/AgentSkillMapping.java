package com.freedom.workdistribution.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgentSkillMapping {
	
	private String skill_id;
	private String agent_id;
	private Date last_modified_date;

}
