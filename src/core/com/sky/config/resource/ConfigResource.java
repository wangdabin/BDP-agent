package com.sky.config.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.joe.agent.utils.Constants;
import com.joe.config.plugin.Config;
import com.joe.core.annotation.RestResource;
import com.joe.core.vo.ReCode;
import com.joe.core.vo.ReCode.Data;
import com.joe.plugin.manager.PluginFactory;
import com.sky.agent.resource.AgentResource;
import com.sky.agent.version.AgentVersion;
import com.sky.config.utils.ConfigConvert;
import com.sky.service.define.KeyValue;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author qiaolong
 *
 */
@RestResource(name = "config",type = "config")
@Produces(value = {MediaType.APPLICATION_JSON})
@Consumes(value = {MediaType.APPLICATION_JSON})
@Path(AgentVersion.BASE_PATH + ConfigResource.PATH)
public class ConfigResource {
	public static final Logger LOG = Logger.getLogger(AgentResource.class);
	public static final String PATH = "/config"; //资源路径
	private PluginFactory factory;
	
	public ConfigResource() throws IOException{
		factory = PluginFactory.getFactory(AgentConfigUtil.create());
	}
	
	@GET
	@Path("/keyvalues")
	public List<KeyValue> findAll(@QueryParam("exId") String exId, @QueryParam("file") String configFile){
		Config config = getConfig(exId);
		try {
			load(config,configFile);
			List<KeyValue> kvs = ConfigConvert.convertTo(config);
			return kvs;
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加或者更新keyvalue
	 * @param exId
	 * @param configFile
	 * @param kv
	 */
	@POST
	@Path("/keyvalue")
	public ReCode newKeyValue(@QueryParam("exId") String exId, @QueryParam("file") String configFile,KeyValue kv){
		Config config = getConfig(exId);
		try {
			ConfigConvert.addKeyValue(config, kv);
			save(config,configFile);
			return createReCode(configFile, "Config file " + configFile + " add keyvalues success!");
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加或者更新keyvalue
	 * @param exId
	 * @param configFile
	 * @param kv
	 */
	@POST
	@Path("/keyvalues")
	public ReCode newKeyValues(@QueryParam("exId") String exId, @QueryParam("file") String configFile,List<KeyValue> kvs){
		Config config = getConfig(exId);
		try {
			ConfigConvert.addKeyValues(config, kvs);
			save(config,configFile);
			return createReCode(configFile, "Config file " + configFile + " add keyvalues success!");
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加或者更新keyvalue
	 * @param exId
	 * @param configFile
	 * @param kv
	 */
	@PUT
	@Path("/keyvalue")
	public ReCode updateKeyValue(@QueryParam("exId") String exId, @QueryParam("file") String configFile,KeyValue kv){
		Config config = getConfig(exId);
		try {
			load(config,configFile);
			ConfigConvert.addKeyValue(config, kv);
			save(config,configFile);
			return createReCode(configFile, "Config file " + configFile + " add key " + kv.getConfigKey() + " success!");
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加或者更新keyvalue
	 * @param exId
	 * @param configFile
	 * @param kv
	 */
	@PUT
	@Path("/keyvalues")
	public ReCode updateKeyValues(@QueryParam("exId") String exId, @QueryParam("file") String configFile,List<KeyValue> kvs){
		LOG.debug("exId = " + exId + ", configFile = " + configFile + ", kvs = " + kvs);
		Config config = getConfig(exId);
		try {
			load(config,configFile);
			ConfigConvert.addKeyValues(config, kvs);
			save(config,configFile);
			return createReCode(configFile, "Config file " + configFile + " add keyvalues success!");
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加keyvalue
	 * @param exId
	 * @param configFile
	 * @param kv
	 */
	@DELETE
	@Path("/keyvalue")
	public ReCode delete(@QueryParam("exId") String exId, @QueryParam("file") String configFile,String key){
		Config config = getConfig(exId);
		try {
			load(config,configFile);
			config.remove(key);
			save(config,configFile);
			return createReCode(configFile, "Config file " + configFile + " remove key " + key + " success!");
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 添加keyvalue
	 * @param exId
	 * @param configFile
	 * @param kv
	 */
	@DELETE
	@Path("/keyvalues")
	public ReCode delete(@QueryParam("exId") String exId, @QueryParam("file") String configFile,List<String> keys){
		Config config = getConfig(exId);
		try {
			load(config,configFile);
			for(String key : keys){
				config.remove(key);
			}
			save(config,configFile);
			return createReCode(configFile, "Config file " + configFile + " remove keys success!");
		} catch (IOException e) {
			LOG.error(e);
			throw new RuntimeException(e);
		}
	}
	
	private void load(Config config,String resource) throws IOException{
		File file = new File(resource);
		if(file.exists()){
			config.load(resource);
		}
	}
	
	private void save(Config config,String resource) throws IOException{
		File file = new File(resource);
		if(!file.exists()){
			file.getParentFile().mkdirs();
		}
		config.save(new FileOutputStream(file));
	}
	
	/**
	 * 创建返回值
	 * 
	 * @param key
	 * @param msg
	 * @return
	 */
	private ReCode createReCode(String key, String msg) {
		ReCode re = new ReCode();
		re.setData(new Data(key));
		re.setErrcode(Constants.NOT_ERROR);
		re.setRet(Constants.RET_SUCCESS);
		re.setMsg(msg);
		return re;
	}
	
	/**
	 * 
	 * @param exId
	 * @return
	 */
	private Config getConfig(String exId) {
		try{
			return factory.getInstanceByXpId(Config.class, Config.X_POINT_ID, exId);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
