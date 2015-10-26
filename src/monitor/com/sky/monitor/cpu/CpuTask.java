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
package com.sky.monitor.cpu;

import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;

import com.joe.monitor.cpu.CpuUnit;
import com.sky.monitor.vo.BasicTask;
import com.sky.utils.AgentConfigUtil;

/**
 * This task counts from 1 to 30.
 */
public class CpuTask extends BasicTask {

	public static final Logger LOG = Logger.getLogger(CpuTask.class);
	private CpuClient client;
	
	public CpuTask() throws IOException{
		client = new CpuClient();
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
		LOG.info("Start monitor cpu status ...");
		if (executor.isStopped()) {
			return;
		}
		Sigar sigar = new Sigar(); 
		try {
			CpuPerc perc = sigar.getCpuPerc();
			CpuUnit cpu = new CpuUnit();
			cpu.setCpuId("cpu1");
			cpu.setTimeStamp(System.currentTimeMillis());
			cpu.setIp(AgentConfigUtil.getMyIp());
			cpu.setUser(perc.getUser());
			cpu.setSys(perc.getSys());
			cpu.setNice(perc.getNice());
			cpu.setIdle(perc.getIdle());
			cpu.setWait(perc.getWait());
			cpu.setIrq(perc.getIrq());
			cpu.setSoftIrq(perc.getSoftIrq());
			cpu.setStolen(perc.getStolen());
			cpu.setCombined(perc.getCombined());
			client.addCpu(cpu);
			executor.pauseIfRequested();
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		} 
	}

}
