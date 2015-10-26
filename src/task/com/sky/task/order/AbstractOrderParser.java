package com.sky.task.order;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;

import com.sky.config.Configed;
import com.sky.task.ParseResult;
import com.sky.task.vo.Order;
import com.sky.task.vo.ParseAble;
import com.sky.task.vo.TranOrder;

/**
 * 
 * @author Joe
 *
 */
public abstract class AbstractOrderParser extends Configed implements OrderParser {

	public AbstractOrderParser() {
	}

	/**
	 * 
	 * @param conf
	 * @throws IOException
	 */
	public AbstractOrderParser(Configuration conf) throws IOException {
		super(conf);
	}
	
	@Override
	public ParseResult parse(ParseAble parseAble) throws Exception {
		return this.parse((Order) parseAble);
	}
	
	protected ParseResult parse(Order order) throws Exception{
		if(order instanceof TranOrder){
			this.doOrder((TranOrder) order);
		}
		return null;
	}
	
	protected abstract void doOrder(TranOrder order) throws Exception;

	@Override
	public ParseResult kill(ParseAble parseAble) throws Exception {
		return this.kill((Order) parseAble);
	}
	
	/**
	 * 装车order 对象执行
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public ParseResult kill(Order order) throws Exception {
		if(order instanceof TranOrder){
			this.kill((TranOrder) order);
		}
		return null;
	}
	
	/**
	 * 转成 TranOrder 对象执行
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public abstract void kill(TranOrder order) throws Exception;

	@Override
	public void destory(ParseAble parseAble) throws Exception {
		this.destory((Order) parseAble);
	}
	
	/**
	 * 
	 * @param order
	 * @throws Exception
	 */
	protected void destory(Order order) throws Exception{
		if(order instanceof TranOrder){
			this.destory((TranOrder)order);
		}
	}
	
	/**
	 * 
	 * @param order
	 * @throws Exception
	 */
	protected abstract void destory(TranOrder order) throws Exception;
}
