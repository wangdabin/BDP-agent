package com.sky.monitor.cpu;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.joe.core.rest.RestClient;
import com.joe.core.vo.ReCode;
import com.joe.monitor.cpu.CpuUnit;
import com.sky.config.ConfigAble;
import com.sky.config.Configed;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author Joe
 *
 */
public class CpuClient extends Configed implements ConfigAble{
	public static final Logger LOG = Logger.getLogger(CpuClient.class);
	
	public static final String RESOURCE_CPU_ADD = "server.ws.resource.cpu.add"; 
	private String addResource;
	public CpuClient() throws IOException{
		this.addResource = AgentConfigUtil.getProperty(RESOURCE_CPU_ADD);
	}

	/**
	 * 
	 * @param cpu
	 * @return
	 */
	public boolean addCpu(CpuUnit cpu){
		ReCode re = RestClient.post(ReCode.class, addResource, cpu);
		if(re.getRet() == ReCode.RET_SUCCESS){
			LOG.debug(re.getMsg());
			return true;
		}else{
			LOG.error(re.getMsg());
			return false;
		}
	}
}
