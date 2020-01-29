package com.freedom.workdistribution.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Skill {
	
	private String name;
	private String skill_id;
	private String description;
	private Date last_modified_date;

}
