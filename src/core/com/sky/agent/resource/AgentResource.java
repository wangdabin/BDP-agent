package com.sky.agent.resource;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.bdp.rest.AgentClient;
import com.sky.agent.version.AgentVersion;
import com.sky.agent.vo.ReCode;
import com.sky.agent.vo.ReCode.Data;
import com.sky.http.StopServer;
import com.sky.utils.AgentConfigUtil;

@Path(AgentVersion.BASE_PATH + AgentResource.PATH)
public class AgentResource {
	public static final Logger LOG = Logger.getLogger(AgentResource.class);
	public static final String PATH = "/agent"; //资源路径

	@POST
	@Path("/delete")
	@Produces(value = {MediaType.TEXT_XML,MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public ReCode delete() throws UnknownHostException, IOException{
		ReCode reCode = new ReCode();
    	reCode.setData(new Data(AgentConfigUtil.getMyName()));
    	reCode.setMsg("commit success");
    	reCode.setErrcode("0");
    	reCode.setRet(0);
		new StopThread(Boolean.TRUE).start();
    	return reCode;
	}
	
	@POST
	@Path("/stop")
	@Produces(value = {MediaType.TEXT_XML,MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
	public ReCode stop() throws UnknownHostException, IOException{
		ReCode reCode = new ReCode();
    	reCode.setData(new Data(AgentConfigUtil.getMyName()));
    	reCode.setMsg("commit success");
    	reCode.setErrcode("0");
    	reCode.setRet(0);
		new StopThread(Boolean.FALSE).start();
    	return reCode;
	}
	
	class StopThread extends Thread{
		boolean delete;
		StopThread(boolean delete){
			this.delete = delete;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(10000);
				AgentClient client = new AgentClient();
				if(delete){
					client.doDelete(AgentConfigUtil.getMyName(), AgentConfigUtil.getMyIp());
				}else{
					client.doStop(AgentConfigUtil.getMyName(), AgentConfigUtil.getMyIp());
				}
				StopServer.stop(delete);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
