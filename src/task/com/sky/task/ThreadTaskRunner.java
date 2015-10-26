package com.sky.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.bdp.rest.TaskClient;
import com.sky.config.Configed;
import com.sky.parser.ParserFactory;
import com.sky.task.dao.TaskDAO;
import com.sky.task.dao.TaskDAOFactory;
import com.sky.task.vo.Task;
import com.sky.utils.AgentConfigUtil;

/**
 * 以线程模式执行任务
 * @author Dragon Joey
 * 
 */
public class ThreadTaskRunner extends Configed implements TaskRunner {
	private static final Logger LOG = Logger.getLogger(ThreadTaskRunner.class);
	public static final int corePoolSize = 1;
	public static final int maximumPoolSize = 10;
	private static final ExecutorService poolExe = new ThreadPoolExecutor(
			corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	private ParserFactory parserFactory;
	private TaskDAOFactory taskDAOFactory;
	private TaskClient client;

	public ThreadTaskRunner(Configuration conf) throws Exception {
		super(conf);
		this.init();
		this.parserFactory = new ParserFactory(conf);
		this.taskDAOFactory = new TaskDAOFactory(conf);
		this.client = new TaskClient();
	}

	public void init() throws Exception {
		TaskDAOFactory.init(getConf());
		ParserFactory.init(getConf());
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
			poolExe.execute(new TaskThread(task));
			// 正在运行
			return ParseResult.getResult(Long.toString(task.getId()), true,
					"running...", Task.RUNNING);
		} else {
			// 等待
			LOG.info("Will wait task task id " + task.getId());
			return ParseResult.getResult(Long.toString(task.getId()), true,
					"waiting...", Task.WAITING);
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
		TaskDAO taskDAO = taskDAOFactory.getTaskDAO(task);
		long id = taskDAO.add(task);
		LOG.info("Will do task name " + task.getName() + ", id " + id);
		taskDAO.setStatus(id, Task.RUNNING); // 设置正在运行
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
			LOG.info("Will end task id = " + task.getId() + " success " + success);
			TaskDAO taskDAO = taskDAOFactory.getTaskDAO(task);
			task.setfTime(System.currentTimeMillis()); // 设置结束时间
			if (success) {
				task.setStatus(Task.FINISH);
			} else {
				task.setStatus(Task.ERROR);
			}
			taskDAO.update(task);
			client.taskFinish(AgentConfigUtil.getMyName(), AgentConfigUtil.getMyIp(),task.getId(), task); //通知服务端。。
			if (success) {
				LOG.info("Task id = " + task.getId() + " is success ..");
			} else {
				LOG.info("Task id = " + task.getId() + " is error ..");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}

	class TaskThread extends Thread {
		private final Task task;

		TaskThread(Task task) {
			this.task = task;
		}

		@Override
		public void run() {
			try {
				LOG.info("Will parse task id = " + task.getId());
				ParseResult result = parserFactory.parse(task);
				endTask(task, result.isSuccess());
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				task.setMessage(e.getMessage());
				endTask(task, false);
			}
		}
	}
}