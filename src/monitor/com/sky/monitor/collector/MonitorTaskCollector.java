/*
 * cron4j - A pure Java cron-like scheduler
 * 
 * Copyright (C) 2007-2010 Carlo Pelliccia (www.sauronsoftware.it)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version
 * 2.1, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License 2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License version 2.1 along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.sky.monitor.collector;

import it.sauronsoftware.cron4j.TaskCollector;
import it.sauronsoftware.cron4j.TaskTable;

import org.apache.log4j.Logger;

import com.sky.monitor.MonitorManager;

/**
 * 
 * 监控任务控制
 * @author Joe
 *
 */
public class MonitorTaskCollector implements TaskCollector {

	public static final Logger LOG = Logger.getLogger(MonitorTaskCollector.class);
//	public TaskTable getTasks() {
//		try {
//			String cpuPatt = AgentConfigUtil.getProperty("agent.monitor.cpu", "*/2 * * * *");
//			SchedulingPattern cpuSchedu = new SchedulingPattern(cpuPatt);
//			LOG.debug("Init cpu task " + cpuPatt);
//			
//			String memoryPatt = AgentConfigUtil.getProperty("agent.monitor.memory", "*/2 * * * *");
//			SchedulingPattern memorySchedu = new SchedulingPattern(memoryPatt);
//			LOG.debug("Init memory task " + memoryPatt);
//			
//			String diskPatt = AgentConfigUtil.getProperty("agent.monitor.disk", "*/2 * * * *");
//			SchedulingPattern diskSchedu = new SchedulingPattern(diskPatt);
//			LOG.debug("Init disk task " + diskPatt);
//			
//			String netPatt = AgentConfigUtil.getProperty("agent.monitor.net", "*/2 * * * *");
//			SchedulingPattern netSchedu = new SchedulingPattern(netPatt);
//			LOG.debug("Init net task " + netPatt);
//			
//			Task cpuTask = new CpuTask(); // cpu任务
//			Task memoryTask = new MemoryTask(); //内存任务
//			Task diskTask = new DiskTask();//硬盘任务
//			Task netTask = new NetTask();//网络任务
//			
//			TaskTable tasks = new TaskTable();
//			tasks.add(cpuSchedu, cpuTask);
//			tasks.add(memorySchedu, memoryTask);
//			tasks.add(diskSchedu, diskTask);
//			tasks.add(netSchedu, netTask);
//			return tasks;
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	@Override
	public TaskTable getTasks() {
		return MonitorManager.getTaskTable();
	}
}
