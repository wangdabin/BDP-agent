/*
 * cron4j - A pure Java cron-like scheduler
 * 
 * Copyright (C) 2007-2010 Carlo Pelliccia (www.sauronsoftware.it)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version
 * 2.1, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License 2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License version 2.1 along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.sky.task.listener;

import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.TaskCollector;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.sky.monitor.collector.MonitorTaskCollector;

public class SchedulerServletContextListener implements ServletContextListener {

	public static final Logger LOG = Logger.getLogger(SchedulerServletContextListener.class);
	
	public void contextInitialized(ServletContextEvent event) {
		LOG.info("Init task collector");
		ServletContext context = event.getServletContext();
		// 1. Creates the scheduler.
		Scheduler scheduler = new Scheduler();
		// 2. Registers a custom task collector.
		TaskCollector collector = new MonitorTaskCollector();
		scheduler.addTaskCollector(collector);
		// 3. Starts the scheduler.
		scheduler.start();
		// 4. Registers the scheduler.
		context.setAttribute(Constants.SCHEDULER, scheduler);
	}

	public void contextDestroyed(ServletContextEvent event) {
		LOG.info("Destroy task collector");
		ServletContext context = event.getServletContext();
		// 1. Retrieves the scheduler from the context.
		Scheduler scheduler = (Scheduler) context.getAttribute(Constants.SCHEDULER);
		// 2. Removes the scheduler from the context.
		context.removeAttribute(Constants.SCHEDULER);
		// 3. Stops the scheduler.
		scheduler.stop();
	}

}
