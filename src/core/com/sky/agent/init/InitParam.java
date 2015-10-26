package com.sky.agent.init;

import java.net.InetAddress;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

import com.sky.utils.AgentConfigConstant;
import com.sky.utils.AgentConfigUtil;

/**
 * 功能:用来初始化agent启动时的参数
 * @author 111216ll
 *
 */
public class InitParam {
	
	public static void main(String[] args) throws Exception {
		printUsage(args);
		//读取对应的配置文件,修改对应的参数
		Configuration conf = AgentConfigUtil.create();
		conf.setProperty(AgentConfigConstant.BDP_SERVER_IP,args[1]);
		conf.setProperty(AgentConfigConstant.SKY_AGENT_LOCAL_IP,args[2]);
		if(args.length == 4){
			if(!StringUtils.isBlank(args[3])){
				conf.setProperty(AgentConfigConstant.SKY_AGENT_LOCAL_NAME,args[3]);
			}else{
				conf.setProperty(AgentConfigConstant.SKY_AGENT_LOCAL_NAME,InetAddress.getLocalHost().getHostName());
			}
		}else{
			conf.setProperty(AgentConfigConstant.SKY_AGENT_LOCAL_NAME,InetAddress.getLocalHost().getHostName());
		}
		
		AgentConfigUtil.updateConfig();
		StreamUtils.setValue(AgentConfigConstant.JAVA_HOME,args[0]);
		//修改完毕
	}
	
	/**
	 * 校验输入参数
     */
	private static final void printUsage(String[] args) {
		String useAge = "Usage: " + InitParam.class.getName()
				+ "<JAVA_HOME> <BDP_SERVER_IP> <SKY_AGENT_LOCAL_IP> [SKY_AGENT_LOCAL_NAME]";
			if (args.length < 3) {
			    System.err.println(useAge);
			    System.exit(-1);
			}
	}
}
