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
package com.sky.monitor.net;

import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.Sigar;

import com.joe.monitor.net.NetInfo;
import com.joe.monitor.net.NetInterfaceUnit;
import com.sky.monitor.vo.BasicTask;
import com.sky.utils.AgentConfigUtil;

/**
 * This task counts from 1 to 30.
 */
public class NetTask extends BasicTask {

	public static final Logger LOG = Logger.getLogger(NetTask.class);
	private NetClient client;
	
	public NetTask() throws IOException{
		client = new NetClient();
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
			String[] netIfs = sigar.getNetInterfaceList();
			long time = System.currentTimeMillis();
			for(String name:netIfs){
				
				executor.setStatusMessage("Net name : " + name);
				NetInterfaceStat stat = sigar.getNetInterfaceStat(name);
				NetInterfaceUnit net = new NetInterfaceUnit();
				NetInfo netInfo = new NetInfo();
				netInfo.setName(name);
				netInfo.setEnble(true);
				netInfo.setTimeStamp(time);
				netInfo.setIp(AgentConfigUtil.getMyIp());
				
				net.setNetInfo(netInfo);
				net.setTimeStamp(time);
				net.setRxBytes(stat.getRxBytes());
				net.setTxBytes(stat.getTxBytes());
				net.setSpeed(stat.getSpeed());
				client.addNet(net);
				executor.pauseIfRequested();
				if (executor.isStopped()) {
					break;
				}
			}
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		} 
	}

}
