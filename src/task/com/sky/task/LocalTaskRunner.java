package com.sky.task;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.bdp.rest.TaskClient;
import com.sky.config.Configed;
import com.sky.parser.ParserFactory;
import com.sky.task.dao.TaskDAO;
import com.sky.task.dao.TaskDAOFactory;
import com.sky.task.vo.Task;
import com.sky.task.vo.TranOrder;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * 本地执行任务，不需要服务
 * 
 * @author Dragon Joey
 * 
 */
public class LocalTaskRunner extends Configed implements TaskRunner {
	public static final int corePoolSize = 1;
	public static final int maximumPoolSize = 10;

	private ParserFactory parserFactory;
	private TaskDAOFactory taskDAOFactory;
	private Logger LOG;
	private TaskClient client;

	/**
	 * 
	 * @param conf
	 * @throws Exception
	 */
	public LocalTaskRunner(Configuration conf) throws Exception {
		this(conf,Logger.getLogger(LocalTaskRunner.class));
		this.client = new TaskClient();
	}
	
	/**
	 * 
	 * @param conf
	 * @param log
	 * @throws Exception
	 */
	public LocalTaskRunner(Configuration conf, Logger log) throws Exception {
		super(conf);
		this.init();
		this.parserFactory = new ParserFactory(conf);
		this.taskDAOFactory = new TaskDAOFactory(conf);
		this.LOG = log;
	}

	public void init() throws Exception {
		ParserFactory.init(getConf());
		TaskDAOFactory.init(getConf());
	}

	public void destory() throws IOException {
		TaskDAOFactory.destory();
	}

	/**
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public synchronized ParseResult doTask(Task task) throws Exception {
		if (this.startTask(task)) {
			LOG.info("Will execute task task id " + task.getId());
			return run(task);
		} else {
			LOG.info("Will wait task task id " + task.getId());
			return ParseResult.getResult(Long.toString(task.getId()), true, "waiting...", Task.WAITING);
		}
	}

	private ParseResult run(Task task) throws IOException {
		try {
			LOG.info("Will parse task id = " + task.getId());
			ParseResult result = parserFactory.parse(task);
			LOG.info("End task with message " + result.getMessage());
			endTask(task, result.isSuccess());
			return result;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			task.setMessage(e.getMessage());
			endTask(task, false);
			return ParseResult.getResult(Long.toString(task.getId()), false,
					e.getMessage(), Task.ERROR);
		} finally {
			destory();
		}
	}
	
	/**
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public ParseResult killTask(Task task) throws Exception {
		//需要修改
		try {
			LOG.info("Will kill task id = " + task.getId());
			ParseResult result = parserFactory.killTask(task);
			endTask(task, result.isSuccess());
			return result;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			task.setMessage(e.getMessage());
			endTask(task, false);
			return ParseResult.getResult(Long.toString(task.getId()), false,
					e.getMessage(), Task.ERROR);
		} finally {
			destory();
		}
	}

	/**
	 * 设置开始的任务
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private synchronized boolean startTask(Task task) throws Exception {
		try {
			TaskDAO taskDAO = taskDAOFactory.getTaskDAO(task);
			task.setcTime(System.currentTimeMillis());
			long id = taskDAO.add(task);
			LOG.info("Will do " + task.getName());
			taskDAO.setStatus(id, Task.RUNNING); // 设置正在运行
			LOG.info("Save task success ..");
		} catch (Exception e) {
			LOG.warn("Can not save task ..");
			LOG.warn(e.getMessage(), e);
		}
		return true;
	}

	/**
	 * 完善任务结束
	 * 
	 * @param task
	 * @return
	 * @throws Exception
	 */
	private void endTask(Task task, boolean success) {
		try {
			LOG.info("Will end task id = " + task.getId());
			TaskDAO taskDAO = taskDAOFactory.getTaskDAO(task);
			task.setfTime(System.currentTimeMillis());
			if (success) {
				task.setStatus(Task.FINISH);
			} else {
				task.setStatus(Task.ERROR);
			}
			taskDAO.update(task);
			client.taskFinish(AgentConfigUtil.getMyName(), AgentConfigUtil.getMyIp(),task.getId(),task); //通知服务端。。
			if (success) {
				LOG.info("Task id = " + task.getId() + " is success ..");
			} else {
				LOG.info("Task id = " + task.getId() + " is error ..");
			}
		} catch (Exception e) {
			LOG.warn("Task is end ,but can not save task ..");
			LOG.warn(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		Logger LOG = Logger.getLogger(LocalTaskRunner.class);
		Configuration conf = AgentConfigUtil.create();
		
		LocalTaskRunner taskParser = new LocalTaskRunner(conf, LOG);
		Task task = new Task();
		task.setType("basic");
		task.setName("test");
		task.setId(4);
		TranOrder order = new TranOrder();
		order.setType(TranOrder.TYPE_HTTP);
		order.setId(2);
		order.setServiceName("zookeeper");
		order.setVersion("cdh5.1.0");
		order.setTid(5);
		order.setDest("/Users/qiaolong/test/bdp_test");
		task.addOrder(order);
		taskParser.doTask(task);
	}
}