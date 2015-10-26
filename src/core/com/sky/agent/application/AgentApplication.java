package com.sky.agent.application;
//
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.joe.core.jaxb.JacksonConfigurator;
import com.sky.agent.exception.ExceptionMapperSupport;
import com.sky.agent.resource.AgentResource;
import com.sky.agent.resource.TaskResource;
import com.sky.config.resource.ConfigResource;
import com.sky.config.resource.SshKeygenResource;
import com.sky.monitor.rerouce.MonitorResouce;

/**
 * 程序的入口
 * @author Joe
 *
 */
public class AgentApplication extends Application {
	
	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(AgentResource.class);
        classes.add(TaskResource.class);
        classes.add(MonitorResouce.class);
        classes.add(ExceptionMapperSupport.class);
        classes.add(ConfigResource.class);
        classes.add(SshKeygenResource.class);
        classes.add(JacksonConfigurator.class);
		return classes;
	}
}
