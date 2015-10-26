package com.sky.agent.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.configuration.ConfigurationException;


/**
 * sql 执行者
 * 
 * @author Joe
 * 
 */
public class SqlExecute {

	public static void main(String[] args) throws IOException, SQLException, ConfigurationException {
//		String file = "/Users/qiaolong/workspace/BDP-agent/sql/agent-mysql.sql";
		String usage = "Usage : SqlExecute <file>";
		if(args.length != 1){
			System.out.println(usage);
			System.exit(-1);
		}
		File file = new File(args[0]);
		if(file.exists()){
			executeSqlFile(file);	
		}else{
			System.out.println("File " + file + " is not exists .");
		}
	}

	/**
	 * 执行sql文件建表
	 * @param file
	 * @throws IOException
	 * @throws SQLException
	 * @throws ConfigurationException
	 */
	public static void executeSqlFile(File file) throws IOException,
			SQLException, ConfigurationException {
		Connection conn = DBUtil.getConnection();
		Statement stmt = conn.createStatement();
		StringBuffer command = new StringBuffer();
		BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String line = input.readLine();
		while (line != null) {
			line = line.trim();
			command.append(line);
			if (line.endsWith(";") && command.length() > 0) {
				command.setLength(command.length() - 1);
				boolean isRS = stmt.execute(command.toString());
				if (isRS) {
					printRS(stmt.getResultSet());
				}
				command.setLength(0);
			}else{
				command.append("\n");
			}
			line = input.readLine();
		}
		input.close();
	}

	private static void printRS(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int count = md.getColumnCount();
		for (int i = 1; i <= count; i++) {
			System.out.print(md.getColumnLabel(i));
			System.out.print('\t');
		}
		System.out.println();
		while (rs.next()) {
			for (int i = 1; i <= count; i++) {
				System.out.print(rs.getObject(i));
				System.out.print('\t');
			}
			System.out.println();
		}
	}
}
