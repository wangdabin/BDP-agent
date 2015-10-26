package com.sky.task.shell;

import org.apache.log4j.Logger;

/**
 * 默认控制台
 * @author Joe
 *
 */
public class Log4jConsole implements Console{

	private Logger log;
	
	public Log4jConsole(Logger log){
		this.log = log;
	}
	
	@Override
	public void writeError(String line) {
		this.log.error(line);
	}

	@Override
	public void writeInfo(String line) {
		this.log.info(line);
	}

}
