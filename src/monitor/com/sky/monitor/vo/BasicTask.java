package com.sky.monitor.vo;

import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

public class BasicTask extends Task {

	@Override
	public void execute(TaskExecutionContext paramTaskExecutionContext)
			throws RuntimeException {
		System.out.println("do nothing");
	}
}
