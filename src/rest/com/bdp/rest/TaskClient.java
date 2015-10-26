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
public   class  TaskClient extends AbstractClient {

public     TaskClient () throws java.io.IOException{
super(AgentConfigUtil.create());
}
//PUT
public   com.joe.core.vo.ReCode  kill (long param0) {
String resource = getConf().getString("server.ws.resource.tasks.tid.kill");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{tid\\}",""+param0);
return this.doPut(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//GET
public   com.sky.task.vo.Task  getSingleTask (long param0) {
String resource = getConf().getString("server.ws.resource.tasks.tid");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{tid\\}",""+param0);
return this.doGet(com.sky.task.vo.Task.class,resource, queryParams, headerParams, cookies);
}
//GET
public   java.util.List<com.sky.task.vo.TranOrder>  getOrders (long param0) {
String resource = getConf().getString("server.ws.resource.tasks.tid.orders");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{tid\\}",""+param0);
return this.doGet(new GenericType<java.util.List<com.sky.task.vo.TranOrder>>(){},resource, queryParams, headerParams, cookies);
}
//GET
public   com.sky.task.vo.TranOrder  getOrder (long param0) {
String resource = getConf().getString("server.ws.resource.tasks.task.orders.oid");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{oid\\}",""+param0);
return this.doGet(com.sky.task.vo.TranOrder.class,resource, queryParams, headerParams, cookies);
}
//GET
public   java.util.List<com.sky.task.vo.Task>  getTaskInfo () {
String resource = getConf().getString("server.ws.resource.tasks.list");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
return this.doGet(new GenericType<java.util.List<com.sky.task.vo.Task>>(){},resource, queryParams, headerParams, cookies);
}
//GET
public   java.util.List<com.joe.agent.vo.LogFile>  getLogs (int param0,int param1) {
String resource = getConf().getString("server.ws.resource.tasks.logs");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
queryParams.add("pageNo",""+param0);
queryParams.add("pageSize",""+param1);
return this.doGet(new GenericType<java.util.List<com.joe.agent.vo.LogFile>>(){},resource, queryParams, headerParams, cookies);
}
//GET
public   com.joe.agent.vo.LogFile  getSingleLog (long param0) {
String resource = getConf().getString("server.ws.resource.tasks.logs.tid");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{tid\\}",""+param0);
return this.doGet(com.joe.agent.vo.LogFile.class,resource, queryParams, headerParams, cookies);
}
//PUT
public   com.joe.core.vo.ReCode  taskFinish (java.lang.String param0,java.lang.String param1,long param2,com.sky.task.vo.Task param3) {
String resource = getConf().getString("server.ws.resource.tasks.hostname.tid.status");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{hostname\\}",""+param0);
queryParams.add("ip",""+param1);
resource = resource.replaceAll("\\{tid\\}",""+param2);
return this.doPut(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,param3);
}
//PUT
public   com.joe.core.vo.ReCode  orderCompletion (java.lang.String param0,long param1,java.lang.String param2,double param3) {
String resource = getConf().getString("server.ws.resource.tasks.hostname.task.oid");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
resource = resource.replaceAll("\\{hostname\\}",""+param0);
resource = resource.replaceAll("\\{oid\\}",""+param1);
queryParams.add("ip",""+param2);
queryParams.add("completion",""+param3);
return this.doPut(com.joe.core.vo.ReCode.class,resource, queryParams, headerParams, cookies,null);
}
//GET
public   java.util.List<com.sky.task.vo.Task>  getAll () {
String resource = getConf().getString("server.ws.resource.tasks");
MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
Map<String, String> headerParams = new HashMap<String, String>();
List<Cookie> cookies = new ArrayList<Cookie>();
return this.doGet(new GenericType<java.util.List<com.sky.task.vo.Task>>(){},resource, queryParams, headerParams, cookies);
}

}