package com.sky.agent.init;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.bdp.rest.AgentClient;
import com.joe.core.vo.ReCode;
import com.sky.agent.db.SqlExecute;
import com.sky.utils.AgentConfigUtil;

/**
 * 
 * 
 * @author Joe
 *
 */
public class InitListener implements ServletContextListener{

	private static final Logger LOG = Logger.getLogger(InitListener.class);
	public static final String AGENT_INITED = "sky.agent.inited";//是否已经初始化
	@Override
	public void contextDestroyed(ServletContextEvent context) {
		try {
			AgentClient client = new AgentClient();
			// Agent 停止。。。
			client.doStop(AgentConfigUtil.getMyName(), AgentConfigUtil.getMyIp());
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent context) {
		try {
			Configuration conf = AgentConfigUtil.create();
			
			//初始化数据库
			String dbDir = conf.getString("db.dir","database");
			File database = new File(dbDir);
			if(!database.exists()){ // 数据库不存在时，进行创建
				database.mkdirs();
				String sqlFile = conf.getString("db.sql.file","sql/agent-small.sql");
				SqlExecute.executeSqlFile(new File(sqlFile));
			}
			
			AgentClient client = new AgentClient();
			ReCode code = client.updateInstallStatus(AgentConfigUtil.getMyIp(), 100);//更新安装状态
			if(code.getRet() == 0){
				LOG.info("Agent started ..");
			}
			
			// Agent 启动。。。
			client.doStart(AgentConfigUtil.getMyName(), AgentConfigUtil.getMyIp());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) throws IOException, ConfigurationException, SQLException {
		File database = new File("database");
		if(!database.exists()){
			database.mkdirs();
			SqlExecute.executeSqlFile(new File("sql","agent-small.sql"));
		}
	}
}
