package com.sky.task.order.shell;

import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.sky.task.order.AbstractOrderParser;
import com.sky.task.shell.Executor;
import com.sky.task.vo.TranOrder;

/**
 * 
 * @author Joe
 *
 */
public class ShellTaskParser extends AbstractOrderParser {

	public static final Logger LOG = Logger.getLogger(ShellTaskParser.class);
	private Executor executor ;
	

	@Override
	protected void doOrder(TranOrder order) throws Exception {
		LOG.debug("Command " + order.getCommand() + " params " + Arrays.toString(order.getParams()));
		executor = new Executor(LOG,order.getCommand());
		executor.addArgument(order.getParams());
		executor.execute();		
		int code = executor.waitFor();
		if(code != 0){
			throw new RuntimeException("return code is " + code);
		}
	}

	@Override
	public void kill(TranOrder order) throws Exception {
		
	}

	@Override
	protected void destory(TranOrder order) throws Exception {
		
	}
	
	public static void main(String[] args) throws IOException {
		Executor executor = new Executor(LOG,"sh");
		executor.addArgument("-c");
		executor.addArgument("echo 中\n国 > /Users/qiaolong/test/test/kk.txt");
//		executor.addArgument("");
//		executor.addArgument(">");
//		executor.addArgument("");
//		executor.addArgument("a+x","/User/qiaolong/test/test");
		executor.execute();
	}

}
