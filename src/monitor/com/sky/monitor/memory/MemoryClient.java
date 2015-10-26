package com.sky.monitor.memory;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.joe.core.rest.RestClient;
import com.joe.core.vo.ReCode;
import com.joe.monitor.memory.MemoryUnit;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author Joe
 *
 */
public class MemoryClient {
	public static final Logger LOG = Logger.getLogger(MemoryClient.class);
	
	public static final String RESOURCE_MEMORY_ADD = "server.ws.resource.memory.add"; 
	private String addResource;
	public MemoryClient() throws IOException{
		this.addResource = AgentConfigUtil.getProperty(RESOURCE_MEMORY_ADD);
	}

	/**
	 * 
	 * @param cpu
	 * @return
	 */
	public boolean addMemory(MemoryUnit memory){
		ReCode re = RestClient.post(ReCode.class, addResource, memory);
		if(re.getRet() == ReCode.RET_SUCCESS){
			LOG.debug(re.getMsg());
			return true;
		}else{
			LOG.error(re.getMsg());
			return false;
		}
	}
}
