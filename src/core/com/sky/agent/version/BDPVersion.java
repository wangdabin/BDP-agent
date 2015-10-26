package com.sky.agent.version;

/**
 * 
 * BDP 版本信息
 * @author Joe
 *
 */
public class BDPVersion {

	/**
	 * 最基础的路径是由web.xml 中的
	 *  <url-pattern>/ws/*</url-pattern> 类定义的
	 *  path定义是一个相对路径
	 */
//	private static final String BASE_WEB_SERVICE_PATH = "/ws";
	private static final String BASE_WEB_SERVICE_PATH = "";
	private static final String BDP_VERSION_PATH = "/v1";
	public static final String BASE_PATH = BASE_WEB_SERVICE_PATH + BDP_VERSION_PATH;
	
	private BDPVersion(){}
	
	/**
	 * 基础路径
	 * @return
	 */
	public static final String getBasePath(){
		return BASE_PATH;
	}
}
