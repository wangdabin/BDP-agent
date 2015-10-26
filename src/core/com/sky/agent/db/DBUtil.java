package com.sky.agent.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.log4j.Logger;

import com.sky.utils.AgentConfigUtil;

/**
 * 
 * @author Joe
 *
 */
public class DBUtil {

	public static final Logger LOG = Logger.getLogger(DBUtil.class);
	/**
	 * @param url
	 * @param username
	 * @param password
	 * @param driverClassName
	 * @return
	 */
	public static Connection getConnection(String url,String username,String password,String driverClassName) {
		Connection conn = null;
		try {
			Class.forName(driverClassName);
			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			throw new RuntimeException(ex);
		}
		return conn;
	}
	
	public static Connection getConnection() throws IOException{
		String driver = AgentConfigUtil.getProperty("db.driver", "smallsql.database.SSDriver");
		String url = AgentConfigUtil.getProperty("db.url", "jdbc:smallsql:/Users/qiaolong/database/SMALLSQL?create=true");
		return getConnection(url, "", "", driver);
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println(Class.forName("smallsql.server.SSDriver"));
	}
}
