package com.sky.task.parser;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.bdp.rest.TaskClient;
import com.sky.config.ConfigAble;
import com.sky.config.Configed;
import com.sky.parser.ParserFactory;
import com.sky.task.ParseResult;
import com.sky.task.utils.DelayUtil;
import com.sky.task.vo.ParseAble;
import com.sky.task.vo.Task;
import com.sky.task.vo.TranOrder;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author Dragon Joey
 * 
 */
public abstract class AbstractTaskParser extends Configed implements ConfigAble,
		TaskParser {

	public static final double COMPLETION_FINISH = 100.0;
	public static final double COMPLETION_ERROR = -1.0;
	
	protected ParserFactory factory;
	private TaskClient client;
	public AbstractTaskParser() {
	}

	public AbstractTaskParser(Configuration conf) throws IOException {
		super(conf);
	}

	@Override
	public void destory(ParseAble task) throws Exception {
		
	}
	
	@Override
	public ParseResult parse(ParseAble parseAble) throws Exception {
		return this.parse((Task)parseAble);
	}

	public ParseResult parse(Task task) throws Exception {
		DelayUtil de = new DelayUtil();
		de.setStart(System.currentTimeMillis());
		de.printStart(getLOG(), "Task " + task.getName());
		try {
			this.doParse(task);
			de.setEnd(System.currentTimeMillis());
			de.printEnd(getLOG(), "Task " + task.getName());
			return ParseResult.getFinishResult(Long.toString(task.getId()));
		} catch (Exception e) {
			getLOG().error(e.getMessage(), e);
			de.setEnd(System.currentTimeMillis());
			de.printEnd(getLOG(), "Task " + task.getName());
			return ParseResult.getResult(Long.toString(task.getId()), false,e.getMessage(), Task.ERROR);
		}
	}

	/**
	 * 
	 * @param order
	 * @throws Exception
	 */
	protected abstract void doOrder(TranOrder order) throws Exception;

	private void orderFinish(long orderId,double completion) throws UnknownHostException, IOException{
		client.orderCompletion(AgentConfigUtil.getMyName(),orderId, AgentConfigUtil.getMyIp(), completion); //任务完成
	}
	
	/**
	 * 
	 * @param task
	 * @throws Exception
	 */
	protected void doParse(Task task) throws Exception {
		boolean error = false;
		Set<TranOrder> orders = task.getOrders();
		List<TranOrder> sortOrders = new ArrayList<TranOrder>(orders);
		Collections.sort(sortOrders);
		for (TranOrder order : sortOrders) {
			String destHost = order.getDestHost();
			String myHost = AgentConfigUtil.getMyIp();
			if(!StringUtils.isBlank(destHost)){//当目标主机不为空
				if(!destHost.equals(AgentConfigUtil.getMyIp())){//目标主机，与当前agent主机不相同，将不执行此Order
					order.setSuccess(true);
					order.setMessage("success");
					try{
						order.setCompletion(COMPLETION_FINISH);
						orderFinish(order.getId(),COMPLETION_FINISH);
					}catch(Exception e){
						getLOG().error(e.getMessage(), e);
					}
					getLOG().debug("Order id " + order.getTid() + " desthost " + destHost + " my host " + myHost + " order not execute,success") ;
					continue;
				}
			}
			if(error){ //如果已经有order错误了，将不再执行剩下的order
				order.setSuccess(false);
				order.setMessage("Some order is error no execute");
				try{
					order.setCompletion(COMPLETION_ERROR);
					orderFinish(order.getId(),COMPLETION_ERROR);
				}catch(Exception e){
					getLOG().error(e.getMessage(), e);
				}
				getLOG().error("Some order is error no execute");
				getLOG().error("order id " + order.getId() + "  no execute.");
			}else{
				try {
					this.doOrder(order);
					order.setSuccess(true);
					order.setMessage("success");
					try{
						order.setCompletion(COMPLETION_FINISH);
						orderFinish(order.getId(),COMPLETION_FINISH);
					}catch(Exception e){
						getLOG().error(e.getMessage(), e);
					}
				} catch (Exception e) {
					error = true;
					order.setSuccess(false);
					try{
						order.setCompletion(COMPLETION_ERROR);
						orderFinish(order.getId(),COMPLETION_ERROR);
					}catch(Exception e1){
						getLOG().error(e1.getMessage(), e1);
					}
					order.setMessage(e.getMessage());
					getLOG().error(e.getMessage(), e);
					getLOG().error("Parse order id " + order.getId() + "  error.");
				}
			}
		}
		if (error) {
			throw new IOException("Some order error");
		}
	}

	@Override
	public ParseResult kill(ParseAble parseAble) throws Exception {
		return this.killTask((Task) parseAble);
	}
	/**
	 * 杀死正在运行的任务
	 * @param task
	 * @return 
	 * @throws Exception
	 */
	public ParseResult killTask(Task task) throws Exception {
		return this.killRunningOrder();
	}
	
	/**
	 * 杀死正在运行的任务
	 * @throws Exception
	 */
	protected abstract ParseResult killRunningOrder() throws Exception;
	
	protected abstract Logger getLOG();
	
	@Override
	public void setParserFactory(ParserFactory factory) {
		this.factory = factory;
	}
	
	@Override
	public void setConf(Configuration conf) {
		super.setConf(conf);
		try {
			client = new TaskClient();
			client.setConf(conf);
		} catch (IOException e) {
			getLOG().error(e);
		}
	}
}
