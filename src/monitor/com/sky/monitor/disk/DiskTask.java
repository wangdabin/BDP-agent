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
package com.sky.monitor.disk;

import it.sauronsoftware.cron4j.TaskExecutionContext;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Sigar;

import com.joe.monitor.filesystem.DiskInfo;
import com.joe.monitor.filesystem.DiskUnit;
import com.sky.monitor.vo.BasicTask;
import com.sky.utils.AgentConfigUtil;

/**
 * This task counts from 1 to 30.
 */
public class DiskTask extends BasicTask {

	public static final Logger LOG = Logger.getLogger(DiskTask.class);
	private DiskClient client;
	
	public DiskTask() throws IOException{
		client = new DiskClient();
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
		LOG.debug("Start monitor disk status ...");
		if (executor.isStopped()) {
			return;
		}
		Sigar sigar = new Sigar(); 
		try {
			FileSystem[] fsArr = sigar.getFileSystemList(); 
			long time = System.currentTimeMillis();
			for( FileSystem fs:fsArr ){
				FileSystemUsage stat = sigar.getFileSystemUsage(fs.getDirName());  
				executor.setStatusMessage("Dir name : " + fs.getDirName() + " dev name : " + fs.getDevName());
				DiskUnit disk = new DiskUnit();
				DiskInfo info = new DiskInfo();
				info.setIp(AgentConfigUtil.getMyIp());
				info.setTimeStamp(time);
				info.setDirName(fs.getDirName());
				info.setDevName(fs.getDevName());
				info.setTotal(stat.getTotal());
				disk.setTimestamp(time);
				disk.setDiskInfo(info);
				disk.setFree(stat.getFree());
				disk.setUsed(stat.getUsed());
				disk.setAvail(stat.getAvail());
				disk.setFiles(stat.getFiles());
				disk.setFreefiles(stat.getFreeFiles());
				disk.setDiskReadBytes(stat.getDiskReadBytes());
				disk.setDiskWriteBytes(stat.getDiskWriteBytes());
				disk.setUsedPercent(stat.getUsePercent());
				client.addDisk(disk);
				
				executor.pauseIfRequested(); // 
				
			}
			
		} catch (Exception e) {
			LOG.debug(e.getMessage(), e);
			LOG.error(e.getMessage());
			throw new RuntimeException(e);
		} 
	}

}
