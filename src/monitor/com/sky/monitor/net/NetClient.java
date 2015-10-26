package com.sky.monitor.net;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.joe.core.rest.RestClient;
import com.joe.core.vo.ReCode;
import com.joe.monitor.net.NetInterfaceUnit;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author Joe
 *
 */
public class NetClient {
	public static final Logger LOG = Logger.getLogger(NetClient.class);
	
	public static final String RESOURCE_NET_ADD = "server.ws.resource.net.add"; 
	private String addResource;
	public NetClient() throws IOException{
		this.addResource = AgentConfigUtil.getProperty(RESOURCE_NET_ADD);
	}

	/**
	 * 
	 * @param cpu
	 * @return
	 */
	public boolean addNet(NetInterfaceUnit net){
		ReCode re = RestClient.post(ReCode.class, addResource, net);
		if(re.getRet() == ReCode.RET_SUCCESS){
			LOG.debug(re.getMsg());
			return true;
		}else{
			LOG.error(re.getMsg());
			return false;
		}
	}
}
