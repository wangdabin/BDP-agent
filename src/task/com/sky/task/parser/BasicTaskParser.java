package com.sky.task.parser;

import org.apache.log4j.Logger;

import com.sky.task.ParseResult;
import com.sky.task.vo.TranOrder;

/**
 * 基本的任务解析工具
 * @author Joe
 *
 */
public class BasicTaskParser extends AbstractTaskParser{

	public static final Logger LOG = Logger.getLogger(BasicTaskParser.class);
	
	@Override
	protected void doOrder(TranOrder order) throws Exception {
		factory.parse(order);
	}

	@Override
	protected ParseResult killRunningOrder() throws Exception {
		return null;
	}

	@Override
	protected Logger getLOG() {
		return LOG;
	}
}
