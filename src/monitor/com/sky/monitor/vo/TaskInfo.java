package com.sky.monitor.vo;

import it.sauronsoftware.cron4j.SchedulingPattern;

/**
 * 
 * @author qiaolong
 *
 */
public class TaskInfo {

	private String schedule;
	private BasicTask task;

	public TaskInfo(String schedule, BasicTask task) {
		super();
		this.schedule = schedule;
		this.task = task;
	}
	
	public String getSchedule() {
		return schedule;
	}

	public SchedulingPattern getSchedulePattern(){
		return new SchedulingPattern(schedule);
	}
	
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public BasicTask getTask() {
		return task;
	}

	public void setTask(BasicTask task) {
		this.task = task;
	}
}
