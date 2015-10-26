package com.sky.task.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 执行命令的工具
 * 
 * @author qiaolong
 * 
 */
public class Executor {

	private Logger log;
	private Process process = null;
	private Console defConsole;
	
	private InputStream inputStream = null;
	private InputStream errorStream = null;
	private OutputStream outputStream = null;
	
	/**
	 * 参数
	 */
	private ArrayList<String> args = new ArrayList<String>();
	
	/**
	 * 控制台
	 */
	private List<Console> consoles = new ArrayList<Console>();
	
	public Executor(Logger log) {
		this(log,null);
	}

	public Executor(Logger log,String command) {
		this.log = log;
		this.defConsole = new Log4jConsole(log);
		consoles.add(defConsole);
		
		if (!StringUtils.isBlank(command)){
			args.add(command);
		}
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	/**
	 * 
	 * @param args
	 */
	public void addArgument(String... args) {
		if (args != null && args.length > 0) {
			this.args.addAll(Arrays.asList(args));
		}
	}

	/**
	 * 
	 * @param args
	 */
	public void addArgument(Collection<String> args) {
		if (args != null && args.size() > 0) {
			this.args.addAll(args);
		}
	}

	public void execute() throws IOException {
		String[] cmd = args.toArray(new String[args.size()]);
		Runtime runtime = Runtime.getRuntime();
		process = runtime.exec(cmd);
		runtime.addShutdownHook(new ProcessKiller(log, process));
		inputStream = process.getInputStream();
		outputStream = process.getOutputStream();
		errorStream = process.getErrorStream();
		if(consoles.isEmpty()){
			consoles.add(defConsole);
		}
		//INFO 信息输出
		new ConsoleManager(consoles, inputStream, "INFO").start();
		//错误 信息输出
		new ConsoleManager(consoles, errorStream, "ERROR").start();
	}

	public int waitFor() throws InterruptedException {
		return process.waitFor();
	}

	/**
	 * 添加控制台
	 * @param console
	 */
	public void addConsole(Console console){
		this.consoles.add(console);
	}
	
	public void destroy() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable t) {
				;
			}
			inputStream = null;
		}
		if (outputStream != null) {
			try {
				outputStream.close();
			} catch (Throwable t) {
				;
			}
			outputStream = null;
		}
		if (errorStream != null) {
			try {
				errorStream.close();
			} catch (Throwable t) {
				;
			}
			errorStream = null;
		}
		if (process != null) {
			process.destroy();
			process = null;
		}
	}

	public Process getProcess() {
		return process;
	}
}
