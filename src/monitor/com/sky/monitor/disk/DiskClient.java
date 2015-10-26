package com.sky.monitor.disk;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.joe.core.rest.RestClient;
import com.joe.core.vo.ReCode;
import com.joe.monitor.filesystem.DiskUnit;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author Joe
 *
 */
public class DiskClient {
	public static final Logger LOG = Logger.getLogger(DiskClient.class);
	
	public static final String RESOURCE_DISK_ADD = "server.ws.resource.disk.add"; 
	private String addResource;
	public DiskClient() throws IOException{
		this.addResource = AgentConfigUtil.getProperty(RESOURCE_DISK_ADD);
	}

	/**
	 * 
	 * @param cpu
	 * @return
	 */
	public boolean addDisk(DiskUnit disk){
		ReCode re = RestClient.post(ReCode.class, addResource, disk);
		if(re.getRet() == ReCode.RET_SUCCESS){
			LOG.debug(re.getMsg());
			return true;
		}else{
			LOG.error(re.getMsg());
			return false;
		}
	}
}
