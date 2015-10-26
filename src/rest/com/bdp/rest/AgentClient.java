package com.bdp.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;

import com.joe.core.rest.AbstractClient;
import com.sky.utils.AgentConfigUtil;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 *@author computer
 *@version 2015-03-14 22:42:06
 * Automatic generation
 *
*/
public   class  AgentClient extends AbstractClient {

public     AgentClient () throws java.io.IOException{
super(AgentConfigUtil.create());
}
//POST
public   com.joe.core.vo.ReCode  add (com.joe.agent.vo.Agent param0) {
String resource = getConf().getString("server.ws.resource.agents.add");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
return this.doPost(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,param0);
}
//DELETE
public   com.joe.core.vo.ReCode  delete (int param0) {
String resource = getConf().getString("server.ws.resource.agents.aid");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{aid\\}",""+param0);
return this.doDelete(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//PUT
public   com.joe.core.vo.ReCode  update (com.joe.agent.vo.Agent param0) {
String resource = getConf().getString("server.ws.resource.agents.update");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
return this.doPut(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,param0);
}
//PUT
public   com.joe.core.vo.ReCode  doStop (java.lang.String param0,java.lang.String param1) {
String resource = getConf().getString("server.ws.resource.agents.hostname.dostop");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{hostname\\}",""+param0);
queryParams.add("ip",""+param1);
return this.doPost(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//DELETE
public   com.joe.core.vo.ReCode  doPing (java.lang.String param0,java.lang.String param1) {
String resource = getConf().getString("server.ws.resource.agents.hostname.doping");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{hostname\\}",""+param0);
queryParams.add("ip",""+param1);
return this.doDelete(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//GET
public   com.joe.agent.vo.Agent  getSingleInfo (int param0) {
String resource = getConf().getString("server.ws.resource.agents.aid");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{aid\\}",""+param0);
return this.doGet(com.joe.agent.vo.Agent.class,resource, queryParams, headerParams, cookies);
}
//GET
public   com.joe.agent.vo.Agent  getAgentByIp (java.lang.String param0) {
String resource = getConf().getString("server.ws.resource.agents.ip.ip");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{ip\\}",""+param0);
return this.doGet(com.joe.agent.vo.Agent.class,resource, queryParams, headerParams, cookies);
}
//GET
public   java.util.List<com.joe.agent.vo.Agent>  getAgentInfo () {
String resource = getConf().getString("server.ws.resource.agents.list");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
return this.doGet(new GenericType<java.util.List<com.joe.agent.vo.Agent>>(){},resource, queryParams, headerParams, cookies);
}
//PUT
public   com.joe.core.vo.ReCode  updateRunStatus (java.lang.String param0,java.lang.String param1) {
String resource = getConf().getString("server.ws.resource.agents.run.ip");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{ip\\}",""+param0);
queryParams.add("status",""+param1);
return this.doPut(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//PUT
public   com.joe.core.vo.ReCode  updateInstallStatus (java.lang.String param0,double param1) {
String resource = getConf().getString("server.ws.resource.agents.install.ip");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{ip\\}",""+param0);
queryParams.add("status",""+param1);
return this.doPut(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//GET
public   java.util.List<com.joe.agent.vo.Agent>  getAll () {
String resource = getConf().getString("server.ws.resource.agents");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
return this.doGet(new GenericType<java.util.List<com.joe.agent.vo.Agent>>(){},resource, queryParams, headerParams, cookies);
}
//DELETE
public   com.joe.core.vo.ReCode  doDelete (java.lang.String param0,java.lang.String param1) {
String resource = getConf().getString("server.ws.resource.agents.hostname.dodelete");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{hostname\\}",""+param0);
queryParams.add("ip",""+param1);
return this.doDelete(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//POST
public   com.joe.core.vo.ReCode  doStart (java.lang.String param0,java.lang.String param1) {
String resource = getConf().getString("server.ws.resource.agents.hostname.dostart");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{hostname\\}",""+param0);
queryParams.add("ip",""+param1);
return this.doPost(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}

}