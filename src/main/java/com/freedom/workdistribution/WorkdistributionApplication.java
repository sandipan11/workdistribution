package com.freedom.workdistribution;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.freedom.workdistribution")
@SpringBootApplication
public class WorkdistributionApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkdistributionApplication.class, args);
	}

}
