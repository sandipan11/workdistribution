package com.freedom.workdistribution;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.freedom.workdistribution.dao.TaskDistributionDAOImpl;
import com.freedom.workdistribution.model.Task;
import com.freedom.workdistribution.model.TaskDistributionRequest;
import com.freedom.workdistribution.model.TaskDistributionResponse;
import com.freedom.workdistribution.services.controller.TaskRestController;
//import org.junit.platform.runner.JUnitPlatform;
//import org.junit.runner.RunWith;

//@ExtendWith(MockitoExtension.class)
//@RunWith(MockitoJUnitRunner.class)

public class TaskControllerTest {

	  //@InjectMocks
	  public TaskRestController controller;
	  
	  @Test
	    public void testCreateTask() 
	    {
	        MockHttpServletRequest request = new MockHttpServletRequest();
	        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	        TaskDistributionDAOImpl taskDistributionDAOImpl = new TaskDistributionDAOImpl();
	       // when(taskDistributionDAOImpl.createTask().thenReturn(true);
	         
	        Task task = new Task();
	        task.setTask_id(100);
	        task.setName("Test1");
	        task.setPriority(1);
	        
	        HashMap<String,String> allParams = new HashMap<String,String>();
	        
	        TaskDistributionRequest taskRequest = new TaskDistributionRequest();
	        taskRequest.setTask_details(task);
	        ResponseEntity<TaskDistributionResponse> responseEntity = controller.createTask(taskRequest, allParams);
	         
	     //   assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
	        //assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");
	    }
	     
	  /*  @Test
	    public void testFindAll() 
	    {
	        // given
	        Employee employee1 = new Employee(1, "Lokesh", "Gupta", "howtodoinjava@gmail.com");
	        Employee employee2 = new Employee(2, "Alex", "Gussin", "example@gmail.com");
	        Employees employees = new Employees();
	        employees.setEmployeeList(Arrays.asList(employee1, employee2));
	 
	        when(employeeDAO.getAllEmployees()).thenReturn(employees);
	 
	        // when
	        Employees result = employeeController.getEmployees();
	 
	        // then
	        assertThat(result.getEmployeeList().size()).isEqualTo(2);
	         
	        assertThat(result.getEmployeeList().get(0).getFirstName())
	                        .isEqualTo(employee1.getFirstName());
	         
	        assertThat(result.getEmployeeList().get(1).getFirstName())
	                        .isEqualTo(employee2.getFirstName());
	    }*/
}
