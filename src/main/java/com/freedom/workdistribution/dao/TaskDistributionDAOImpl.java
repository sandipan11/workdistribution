package com.freedom.workdistribution.dao;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.freedom.workdistribution.exception.NoTaskFoundException;
import com.freedom.workdistribution.model.AgentDetails;
import com.freedom.workdistribution.model.Task;
import com.freedom.workdistribution.model.TaskSkillMapping;
import com.freedom.workdistribution.utils.CommonUtilities;

/**
 * @author Sandipan
 * 
 * This is DAO Layer class and responsible to interact with database
 *
 */
@Component
public class TaskDistributionDAOImpl {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskDistributionDAOImpl.class);
	
	private final String CREATE_TASK_SQL = "INSERT INTO TASK(name, description, status,last_modified_date,start_date,priority) values(:name,:description,:status,:last_modified_date,:start_date,:priority)";
	private final String CREATE_TASK_SKILL_SQL = "INSERT INTO TASK_SKILL_REL(task_id, skill_id,last_modified_date) values(:task_id,:skill_id,:last_modified_date)";
	private final String CREATE_TASK_ASSIGNMENT_SQL = "INSERT INTO TASK_ASSIGNMENT (agent_id,last_modified_date,task_id,status,comments,start_date) values(:agent_id,:last_modified_date,:task_id,:status,:comments,:start_date)";
	
	private final String UPDATE_TASK_SQL = "UPDATE TASK SET status = :status, last_modified_date = :last_modified_date WHERE id = :task_id";
	private final String UPDATE_TASK_ASSIGNMENT_ENDDATE_SQL = "UPDATE TASK_ASSIGNMENT SET end_date = :end_date, last_modified_date = :last_modified_date WHERE task_id = :task_id";
	
	private final String SELECT_AGENT_SQL = "SELECT HH.agent_id, HH.task_id,HH.priority,HH.start_date,agent.agent_name FROM " + 
											"(SELECT  GG.agent_id, GG.task_id, GG.priority, GG.start_date from " + 
											"(SELECT  FF.agent_id, FF.task_id, IFNULL(FF.priority, -1) AS priority, FF.start_date from  " + 
											"(SELECT AA.agent_id, AA.task_id, AA.priority, AA.start_date from " + 
											"(SELECT D.agent_id, D.task_id, task.priority, IFNULL(D.start_date, NOW()) AS start_date FROM  " + 
											"(SELECT C.agent_id, task_assignment.task_id, task_assignment.start_date, task_assignment.end_date FROM " + 
											"(SELECT agent_id from  " + 
											"(SELECT agent_id , count(*) from (select agent_id,skill_id from agent_skill_rel where skill_id in (:skill_ids) ) A group by agent_id having count(*) > :skill_ids_count) B) C " + 
											"LEFT JOIN task_assignment ON C.agent_id = task_assignment.agent_id and task_assignment.end_date IS NULL) D " + 
											"LEFT JOIN task ON D.task_id = task.id  ORDER By D.start_date) AA " + 
											"INNER JOIN (" + 
											"	SELECT BB.agent_id, BB.priority from " + 
												"(SELECT E.agent_id, MAX(E.priority) priority FROM  " + 
												"(SELECT D.agent_id, D.task_id, task.priority, task.id, IFNULL(D.start_date, NOW()) AS START_DATE FROM  " + 
												"(SELECT C.agent_id, task_assignment.task_id, task_assignment.start_date, task_assignment.end_date FROM " + 
												"(SELECT agent_id from  " + 
												"(SELECT agent_id , count(*) from (select agent_id,skill_id from agent_skill_rel where skill_id in (:skill_ids) ) A group by agent_id having count(*) > :skill_ids_count) B) C " + 
												"LEFT JOIN task_assignment ON C.agent_id = task_assignment.agent_id and task_assignment.end_date IS NULL) D " + 
												"LEFT JOIN task ON D.task_id = task.id  ORDER By D.start_date) E group by E.agent_id) BB " + 
												") F ON AA.agent_id = F.agent_id WHERE ( AA.priority = F.priority OR AA.priority IS NULL OR F.priority IS NULL )) FF  " + 
											") GG  WHERE GG.priority <1 ORDER By GG.start_date DESC LIMIT 1) HH " + 
											"LEFT JOIN agent ON HH.agent_id = agent.agent_id "; 
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	/**
	 * This method responsible to create a task in Task table in database
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public Task createTask(final Task task) throws Exception,SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
			SqlParameterSource parameters = new MapSqlParameterSource()
					.addValue("name", task.getName())
					.addValue("description", task.getDescription())
					.addValue("status", task.getStatus())
					.addValue("last_modified_date", CommonUtilities.getCurrentDateTime())
					.addValue("start_date", CommonUtilities.getCurrentDateTime())
					.addValue("priority", task.getPriority());
			namedParameterJdbcTemplate.update(CREATE_TASK_SQL, parameters, holder);
			int task_id = ((BigInteger) holder.getKeyList().get(0).get("GENERATED_KEY")).intValue();
			task.setTask_id(task_id);
			return task;
		}
	
	/**
	 * This method is responsible to create task-skill mapping in data base
	 * 
	 * @param taskSkillMapping
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int createTaskSkillsMapping(final TaskSkillMapping taskSkillMapping) throws Exception,SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
			SqlParameterSource parameters = new MapSqlParameterSource()
					.addValue("task_id", taskSkillMapping.getTask_id())
					.addValue("skill_id", taskSkillMapping.getSkill_id())
					.addValue("last_modified_date", CommonUtilities.getCurrentDateTime());
			int result = namedParameterJdbcTemplate.update(CREATE_TASK_SKILL_SQL, parameters, holder);
			return result;
		}
	
	/**
	 * This method responsible to update status of a task.
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int updateTaskStatus(final Task task) throws Exception,SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
			SqlParameterSource parameters = new MapSqlParameterSource()
					.addValue("status", task.getStatus())
					.addValue("last_modified_date", CommonUtilities.getCurrentDateTime())
					.addValue("task_id", task.getTask_id());
			
			int updateStatus = namedParameterJdbcTemplate.update(UPDATE_TASK_SQL, parameters,holder); 
	        if(updateStatus == 0){
	            LOGGER.error("No Task found with ID " +task.getTask_id() + "to update");
	        	throw new NoTaskFoundException("No Task found with ID " +task.getTask_id() + "to update" );
	        }

			return updateStatus;
		}
	

	/**
	 * This method is responsible to fetch all agent by skills
	 * 
	 * @param skill_ids
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public List<AgentDetails> getAgentBySkills(final int skill_ids[]) throws Exception,SQLException{
		
			SqlParameterSource parameters = new MapSqlParameterSource()
					.addValue("skill_ids", Arrays.stream(skill_ids).boxed().collect(Collectors.toList()))
					.addValue("skill_ids_count", skill_ids.length - 1);
			
			List<AgentDetails> agentDetails = namedParameterJdbcTemplate.query(SELECT_AGENT_SQL, parameters,new AgentDetailsMapper()); 
	        
			return agentDetails;
		}
	
	/**
	 * This method is responsible to assign a task to a agent
	 * 
	 * @param task_id
	 * @param agent_id
	 * @throws Exception
	 * @throws SQLException
	 */
	public void assignAgentInTask(int task_id, int agent_id) throws Exception,SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("agent_id", agent_id)
				.addValue("last_modified_date", CommonUtilities.getCurrentDateTime())
				.addValue("task_id", task_id)
				.addValue("status", 1)
				.addValue("comments", "Task  assigned")
				.addValue("start_date", CommonUtilities.getCurrentDateTime());

		
		int updateStatus = namedParameterJdbcTemplate.update(CREATE_TASK_ASSIGNMENT_SQL, parameters,holder); 
        
		if(updateStatus == 0){
			LOGGER.error("No Task found with task_id " + task_id + "to assign agent");
            throw new NoTaskFoundException("No Task found with task_id " + task_id + "to assign agent" );
        }
		
	}
	
	/**
	 * This method is responsible to update end date in task
	 * @param task_id
	 * @throws Exception
	 * @throws SQLException
	 */
	public void assignEndDateInTask(int task_id) throws Exception,SQLException{
		KeyHolder holder = new GeneratedKeyHolder();
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("end_date", CommonUtilities.getCurrentDateTime())
				.addValue("last_modified_date", CommonUtilities.getCurrentDateTime())
				.addValue("task_id", task_id);
		
		int updateStatus = namedParameterJdbcTemplate.update(UPDATE_TASK_ASSIGNMENT_ENDDATE_SQL, parameters,holder); 
        
		if(updateStatus == 0){
           
            throw new NoTaskFoundException("No Task found with task_id " +task_id + "to update end_date" );
        }
		
	}
	
	private static final class AgentDetailsMapper implements RowMapper<AgentDetails> {
        public AgentDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        	AgentDetails agent = new AgentDetails();
        	agent.setAgent_id(rs.getInt("agent_id"));
        	agent.setTask_id(rs.getInt("task_id"));
        	agent.setPriority(rs.getInt("priority"));
        	agent.setStart_date(rs.getDate("START_DATE"));
        	agent.setAgent_name(rs.getString("agent_name"));
            return agent;
        }
    }

}
