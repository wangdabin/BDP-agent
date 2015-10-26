package com.sky.agent.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.configuration.Configuration;

import com.sky.task.TaskRunner;
import com.sky.task.ThreadTaskRunner;
import com.sky.task.dao.TaskDAO;
import com.sky.task.vo.Task;

/**
 * s
 * @author Joe
 *
 */
public class TaskService {

	private TaskDAO taskDAO = new TaskDAO();
	private TaskRunner taskParser;
	
	public TaskService(Configuration conf) throws Exception{
//		taskParser = new LocalTaskRunner(conf);
		taskParser = new ThreadTaskRunner(conf);
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Task> getAll() {
		try{
			return taskDAO.getAll();	
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public Task getById(long id){
		try{
			return taskDAO.getById(id);	
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}
	
	public void killById(long id){
		
	}
	
	/**
	 * 
	 * @param task
	 * @throws SQLException 
	 */
	public void addTask(Task task) {
		try{
			taskParser.doTask(task);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
