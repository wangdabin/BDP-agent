package com.sky.agent.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sky.agent.service.TaskService;
import com.sky.agent.version.AgentVersion;
import com.sky.agent.vo.ReCode;
import com.sky.agent.vo.ReCode.Data;
import com.sky.task.vo.Task;
import com.sky.utils.AgentConfigUtil;

@Path(AgentVersion.BASE_PATH + TaskResource.PATH)
@Produces(value = {MediaType.APPLICATION_JSON,MediaType.TEXT_XML,MediaType.APPLICATION_XML})
@Consumes(value = {MediaType.APPLICATION_JSON,MediaType.TEXT_XML,MediaType.APPLICATION_XML})
public class TaskResource {
	public static final Logger LOG = Logger.getLogger(TaskResource.class);
	public static final String PATH = "/tasks"; //资源路径
	
	private TaskService taskService;
	
	public TaskResource() throws Exception{
		taskService = new TaskService(AgentConfigUtil.create());
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@GET
	public List<Task> getAll(){
		return taskService.getAll();
	}
	
	@POST
	public ReCode commitTask(Task task){
		LOG.debug("task.getId = " + task.getId() + " task name " + task.getName());
		taskService.addTask(task);
		ReCode reCode = new ReCode();
    	reCode.setData(new Data(task.getId()));
    	reCode.setMsg("commit success");
    	reCode.setErrcode("0");
    	reCode.setRet(0);
    	return reCode;
	}
	
	@GET
	@Path("/{tId}")
	public Task getById(@PathParam("tId") long tId){
		return taskService.getById(tId);
	}
	
	/**
	 * 
	 * @return
	 */
	@PUT
	@Path("/{tId}/kill")
	public ReCode killTask(@PathParam("tId") long tId){
		taskService.killById(tId);
		ReCode reCode = new ReCode();
    	reCode.setData(new Data(tId));
    	reCode.setMsg("kill success");
    	reCode.setErrcode("0");
    	reCode.setRet(0);
    	return reCode;
	}
}
