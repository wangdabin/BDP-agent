package com.sky.config.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.joe.agent.utils.Constants;
import com.joe.core.annotation.RestResource;
import com.joe.core.vo.ReCode;
import com.joe.core.vo.ReCode.Data;
import com.sky.agent.init.StreamUtils;
import com.sky.agent.resource.AgentResource;
import com.sky.agent.version.AgentVersion;

/**
 * @ClassName: SshKeygenResource
 * @Description: 集群互信资源
 * @author WDB
 * @date 2015-5-11 上午11:13:07
 */
@RestResource(name = "sshkeygen",type = "sshkeygen")
@Produces(value = {MediaType.APPLICATION_JSON})
@Consumes(value = {MediaType.APPLICATION_JSON})
@Path(AgentVersion.BASE_PATH + SshKeygenResource.PATH)
public class SshKeygenResource {
	public static final Logger LOG = Logger.getLogger(AgentResource.class);
	public static final String PATH = "/sshkeygen"; //资源路径
	
	/**
	 * @Title: find
	 * @Description: 得到当前主机的生成的公钥
	 * @param @param idRsaPubFile
	 * @throws UnsupportedEncodingException 
	 */
	@GET
	@Path("/idrsapub")
	public String find(@QueryParam("file") String idRsaPubFile) throws UnsupportedEncodingException{
		idRsaPubFile = URLDecoder.decode(idRsaPubFile, "UTF-8");
		String result = StreamUtils.fileToString(idRsaPubFile);
		return result;
	}
	
	@PUT
	@Path("/authorized_keys")
	public ReCode authorized_keys(@QueryParam("file") String configFile,String authorized_keys){
		StreamUtils.stringToFile(authorized_keys,configFile);
		ReCode re = new ReCode();
		re.setData(new Data(configFile));
		re.setErrcode(Constants.NOT_ERROR);
		re.setRet(Constants.RET_SUCCESS);
		re.setMsg(configFile);
		return re;
	}
	
}
