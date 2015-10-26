package com.sky.task.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;

import com.sky.config.ConfigAble;
import com.sky.config.Configed;
import com.sky.task.vo.Task;

/**
 * 
 * @author Dragon Joey
 * 
 */
public class TaskDAOFactory extends Configed implements ConfigAble {

	private static final Map<Byte, TaskDAO> taskDAOs = new HashMap<Byte, TaskDAO>();

	public TaskDAOFactory() {
	}

	/**
	 * 
	 * @param conf
	 */
	public TaskDAOFactory(Configuration conf) {
		super(conf);
	}

	/**
	 * 
	 * @param conf
	 */
	public static final void init(Configuration conf) {
		TaskDAO cebankDAO = new TaskDAO();
		taskDAOs.put((byte) 0, cebankDAO);
	}

	/**
	 * @throws IOException
	 * 
	 */
	public static final void destory() throws IOException {
		for (TaskDAO dao : taskDAOs.values()) {
			dao.destory();
		}
		taskDAOs.clear();
	}

	/**
	 * 
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public TaskDAO getTaskDAO(Task task) throws IOException {
		TaskDAO taskDAO = taskDAOs.get(task.getType());
		if(taskDAO == null){
			taskDAO = new TaskDAO();
		}
		return taskDAO;
	}
}
