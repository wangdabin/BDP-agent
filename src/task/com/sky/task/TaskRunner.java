package com.sky.task;

import com.sky.config.ConfigAble;
import com.sky.task.vo.Task;

/**
 * 
 * @author Joe
 *
 */
public interface TaskRunner extends ConfigAble{
	
	/**
	 * 执行任务
	 * @param task
	 * @return
	 * @throws Exception
	 */
	ParseResult doTask(Task task) throws Exception;
}
