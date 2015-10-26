package com.sky.monitor;

import it.sauronsoftware.cron4j.TaskTable;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sky.monitor.cpu.CpuTask;
import com.sky.monitor.disk.DiskTask;
import com.sky.monitor.memory.MemoryTask;
import com.sky.monitor.net.NetTask;
import com.sky.monitor.vo.BasicTask;
import com.sky.monitor.vo.TaskInfo;

/**
 * 监控管理
 * 
 * @author qiaolong
 *
 */
public class MonitorManager {

	private static final Logger LOG = Logger.getLogger(MonitorManager.class);
	private static final TaskTable tasks = new TaskTable();
	private static final Map<String,Map<String,TaskInfo>> taskInfos = Collections.synchronizedMap(new HashMap<String,Map<String,TaskInfo>>());
	
	private static final Map<String,Map<String,BasicTask>> tasksMap = Collections.synchronizedMap(new HashMap<String,Map<String,BasicTask>>());
	private static final Map<String,BasicTask> basicTask = Collections.synchronizedMap(new HashMap<String,BasicTask>());
	public static final String BASIC_TYPE = "host";
	
	static{
		try {
			basicTask.put("cpu", new CpuTask());
			basicTask.put("disk", new DiskTask());
			basicTask.put("mem", new MemoryTask());
			basicTask.put("net", new NetTask());
			
			tasksMap.put(BASIC_TYPE, basicTask);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 得到当前的任务表
	 * @return
	 */
	public static final TaskTable getTaskTable(){
		synchronized (tasks) {
			clear(tasks);
			for(Map<String,TaskInfo> taskInfoMap : taskInfos.values()){
				for(TaskInfo taskInfo : taskInfoMap.values()){
					tasks.add(taskInfo.getSchedulePattern(), taskInfo.getTask());
				}
			}
			return tasks;
		}
	}
	
	/**
	 * 添加监控任务
	 * @param type
	 * @param name
	 * @param schedule
	 * @param task
	 */
	public synchronized static final void addTask(String type,String name,String schedule,BasicTask task){
		LOG.debug("Will add monitor type : " + type + ",schedule : " + schedule);
		Map<String,TaskInfo> taskMap = taskInfos.get(type);
		if(taskMap == null){
			taskMap = createSynchronizedMap(new HashMap<String,TaskInfo>());
			taskInfos.put(type, taskMap);
		}
		taskMap.put(name, new TaskInfo(schedule, task));
	}
	
	/**
	 * 
	 * @param type
	 * @param name
	 * @param schedule
	 */
	public synchronized static final void addTask(String type,String name,String schedule){
		BasicTask task = getBasicTask(type, name);
		if(task != null){
			addTask(type, name, schedule, task);
		}else{
			throw new RuntimeException("Type : " + type + " name : " + name + " unsupport.");
		}
	}
	
	/**
	 * 添加监控任务
	 * @param name
	 * @param schedule
	 * @param task
	 */
	public static final void addHostTask(String name,String schedule){
		addTask(BASIC_TYPE, name,schedule, basicTask.get(name));
	}
	
	
	/**
	 * 删除监控
	 * @param type
	 */
	public static final void removeTask(String type,String name){
		LOG.debug("Will remove host monitor type : " + name);
		Map<String,TaskInfo> taskMap = taskInfos.get(BASIC_TYPE);
		if(taskMap != null){
			taskMap.remove(name);
		}
	}
	
	/**
	 * 删除监控
	 * @param type
	 */
	public static final void removeHostTask(String name){
		LOG.debug("Will remove host monitor type : " + name);
		Map<String,TaskInfo> taskMap = taskInfos.get(BASIC_TYPE);
		if(taskMap != null){
			taskMap.remove(name);
		}
	}
	
	/**
	 * 清空所有的任务
	 * @param tasks
	 */
	private static void clear(TaskTable tasks){
		for(int i =  tasks.size() - 1;i >= 0;i --){
			tasks.remove(i);
		}
	}
	
	/**
	 * 得到BasicTask
	 * @param type
	 * @param name
	 * @return
	 */
	private static BasicTask getBasicTask(String type,String name){
		Map<String,BasicTask> taskMap  = tasksMap.get(type);
		if(taskMap != null){
			return taskMap.get(name);
		}
		return null;
	}
	
	/**
	 * 创建一个安全的map
	 * @param map
	 * @return
	 */
	private static final <K,V> Map<K,V> createSynchronizedMap(Map<K,V> map){
		return Collections.synchronizedMap(map);
	}
}
