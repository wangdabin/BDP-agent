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
package com.sky.monitor.memory;

import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;

import com.joe.monitor.memory.MemoryUnit;
import com.sky.monitor.vo.BasicTask;
import com.sky.utils.AgentConfigUtil;

/**
 * This task counts from 1 to 30.
 */
public class MemoryTask extends BasicTask {

	public static final Logger LOG = Logger.getLogger(MemoryTask.class);
	private MemoryClient client;
	
	public MemoryTask() throws IOException{
		client = new MemoryClient();
	}
	public boolean canBePaused() {
		return true;
	}

	public boolean canBeStopped() {
		return true;
	}

	public boolean supportsCompletenessTracking() {
		return true;
	}

	public boolean supportsStatusTracking() {
		return true;
	}

	public void execute(TaskExecutionContext executor) throws RuntimeException {
		LOG.debug("Start monitor memory status ...");
		if (executor.isStopped()) {
			return;
		}
		Sigar sigar = new Sigar(); 
		try {
			Mem mem = sigar.getMem();    
			MemoryUnit memory = new MemoryUnit();
			memory.setIp(AgentConfigUtil.getMyIp());
			memory.setTimeStamp(System.currentTimeMillis());
			memory.setName("memory");
			memory.setTotal(mem.getTotal());
			memory.setRam(mem.getRam());
			memory.setUsed(mem.getUsed());
			memory.setFree(mem.getFree());
			memory.setActualUsed(mem.getActualUsed());
			memory.setActualFree(mem.getActualFree());
			memory.setUsedPercent(mem.getUsedPercent());
			memory.setFreePercent(mem.getFreePercent());
			client.addMemory(memory);
			
			executor.pauseIfRequested();
		
		} catch (Exception e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(), e);
			throw new RuntimeException(e);
		} 
	}
}
