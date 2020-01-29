package com.freedom.workdistribution.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskSkillMapping {
	
	private int task_id;
	private int skill_id;
	private Date last_modified_date;

}
