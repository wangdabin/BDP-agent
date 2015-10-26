package com.sky.monitor.rerouce;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sky.agent.version.AgentVersion;
import com.sky.agent.vo.ReCode;
import com.sky.agent.vo.ReCode.Data;
import com.sky.monitor.MonitorManager;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author qiaolong
 *
 */
@Path(AgentVersion.BASE_PATH + MonitorResouce.PATH)
@Produces(value = {MediaType.TEXT_XML,MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
@Consumes(value = {MediaType.TEXT_XML,MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
public class MonitorResouce {
	public static final Logger LOG = Logger.getLogger(MonitorResouce.class);
	public static final String PATH = "/monitor"; //资源路径

	public MonitorResouce(){
	}
	
	/**
	 * 添加主机的监控
	 * @param type 主机、服务 
	 * 		name：cpu、内存、磁盘等
	 * @param schedule
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@POST
	@Path("/{type}/{name}/add")
	public ReCode addMonitor(@PathParam("type") String type,@PathParam("name") String name,@QueryParam("schedule") String schedule) throws UnknownHostException, IOException{
		MonitorManager.addTask(type, name, schedule);
		ReCode reCode = new ReCode();
    	reCode.setData(new Data(AgentConfigUtil.getMyName()));
    	reCode.setMsg("commit success");
    	reCode.setErrcode("0");
    	reCode.setRet(0);
    	return reCode;
	}
	
	/**
	 * 删除指定类型的监控
	 * @param type 类型 、 执行规则
	 * @param schedule
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@DELETE
	@Path("/{type}/{name}/delete")
	public ReCode deleteMonitor(@PathParam("type") String type,@PathParam("name") String name) throws UnknownHostException, IOException{
		MonitorManager.removeTask(type,name);
		ReCode reCode = new ReCode();
    	reCode.setData(new Data(AgentConfigUtil.getMyName()));
    	reCode.setMsg("delete success");
    	reCode.setErrcode("0");
    	reCode.setRet(0);
    	return reCode;
	}
}
