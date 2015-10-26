package com.sky.task.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.sky.agent.db.DBUtil;
import com.sky.task.vo.Order;
import com.sky.task.vo.TranOrder;
import com.sky.task.vo.Task;

/**
 * 
 * @author Joe
 *
 */
public class TaskDAO{
	
	public static final Logger LOG = Logger.getLogger(TaskDAO.class);
	public static final String TABLE_NAME = "TASK";
	private OrderDAO orderDAO;
	
	public TaskDAO(){
		this.orderDAO = new OrderDAO();
	}

	/**
	 * 
	 * @param task
	 * @throws SQLException
	 */
	public long add(Task task) throws SQLException{
		String insertSQL = "insert into " + TABLE_NAME + "(ID,NAME,STATUS,CTIME,FTIME,MESSAGE,TYPE) values(?,?,?,?,?,?,?)";;
		Connection conn = null;
		PreparedStatement inStmt = null;
		boolean autoCommit = false;
		try{
			conn = DBUtil.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			inStmt = conn.prepareStatement(insertSQL);
			inStmt.setLong(1, task.getId());
			inStmt.setString(2, task.getName());
			inStmt.setInt(3, task.getStatus());
			inStmt.setLong(4, task.getcTime());
			inStmt.setLong(5, task.getfTime());
			inStmt.setString(6, task.getMessage());
			inStmt.setString(7, task.getType());
			inStmt.execute();
			
			orderDAO.add(conn, task.getOrders());
			conn.commit();
			return task.getId();
		}catch(Exception e){
			LOG.error("Will rollback");
			conn.rollback();
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}finally{
			if(conn != null){
				conn.setAutoCommit(autoCommit);
				conn.close();
			}
			if(inStmt != null){
				inStmt.close();
			}
		}
	}
	
	/**
	 * 更新任务
	 * @param task
	 * @throws SQLException 
	 */
	public long update(Task task) throws SQLException {
		//ID,NAME,STATUS,CTIME,FTIME,MESSAGE
		String upSql = "update " + TABLE_NAME + " set NAME=?,STATUS=?,CTIME=?,FTIME=?,MESSAGE=? where ID=?";
		Connection conn = null;
		PreparedStatement upStmt = null;
		try{
			conn = DBUtil.getConnection();
			conn.setAutoCommit(false);
			upStmt = conn.prepareStatement(upSql);
			upStmt.setString(1, task.getName());
			upStmt.setInt(2, task.getStatus());
			upStmt.setLong(3, task.getcTime());
			upStmt.setLong(4, task.getfTime());
			upStmt.setString(5, task.getMessage());
			upStmt.setLong(6, task.getId());
			upStmt.execute();
			this.orderDAO.update(conn, task.getOrders());
			conn.commit();
			return task.getId();
		}catch(Exception e){
			LOG.error("Will rollback");
			conn.rollback();
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}finally{
			if(upStmt != null){
				upStmt.close();
			}
			if(conn != null){
				conn.close();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Task> getAll() throws SQLException{
		String sql = "select * from " + TABLE_NAME;
		return getBySql(sql);
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public Task getById(long id) throws SQLException{
		//ID,NAME,STATUS,CTIME,FTIME,MESSAGE,TYPE
		String sql = "select * from " + TABLE_NAME + " where id=" + id;
		Connection conn = null;
		Statement stmt = null;
		try{
			conn = DBUtil.getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				Task task = new Task();
				task.setId(rs.getLong("ID"));
				task.setName(rs.getString("NAME"));
				task.setStatus(rs.getInt("STATUS"));
				task.setcTime(rs.getLong("CTIME"));
				task.setfTime(rs.getLong("FTIME"));
				task.setMessage(rs.getString("MESSAGE"));
				task.setType(rs.getString("TYPE"));
				Set<TranOrder> orders = this.orderDAO.getOrders(conn,id);
				task.setOrders(orders);
				return task;
			}
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}finally{
			if(conn != null){
				conn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
		return null;
	}

	
	/**
	 * 
	 * @param task
	 * @throws SQLException
	 */
	public boolean delete(Task task)throws SQLException{
		String sql = "delete from " + TABLE_NAME + " where id=" + task.getId();
		Connection conn = null;
		Statement stmt = null;
		try{
			conn = DBUtil.getConnection();
			this.orderDAO.delete(conn, task.getId());//删除order
			stmt = conn.createStatement();
			boolean re = stmt.execute(sql);
			conn.commit();
			return re;
		}catch(Exception e){
			conn.rollback();
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}finally{
			if(conn != null){
				conn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public List<Task> getError() throws SQLException{
		return getByStatus(Task.ERROR);
	}
	
	/**
	 * 得到某个状态的所有任务
	 * @param status
	 * @return
	 * @throws SQLException
	 */
	public List<Task> getByStatus(int status) throws SQLException{
		String sql = "select * from " + TABLE_NAME + " where status=" + status;
		return getBySql(sql);
	}

	/**
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private List<Task> getBySql(String sql) throws SQLException {
		Connection conn = null;
		Statement stmt = null;
		try{
			conn = DBUtil.getConnection();
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			List<Task> tasks = new ArrayList<Task>();
			while(rs.next()){
				Task task = new Task();
				task.setId(rs.getLong("ID"));
				task.setName(rs.getString("NAME"));
				task.setStatus(rs.getInt("STATUS"));
				task.setcTime(rs.getLong("CTIME"));
				task.setfTime(rs.getLong("FTIME"));
				task.setMessage(rs.getString("MESSAGE"));
				task.setType(rs.getString("TYPE"));
				Set<TranOrder> orders = this.orderDAO.getOrders(conn,task.getId());
				task.setOrders(orders);
				tasks.add(task);
			}
			return tasks;
		}catch(Exception e){
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}finally{
			if(stmt != null){
				stmt.close();
			}
			if(conn != null){
				conn.close();
			}
		}
	}

	public void destory() {
		
	}

	/**
	 * 设置状态
	 * @param id
	 * @param status
	 * @throws SQLException 
	 */
	public boolean setStatus(long id, int status) throws SQLException {
		String sql = "update " + TABLE_NAME + " set status=" + status + " where id=" + id;
		return execute(sql);
	}
	
	/**
	 * 执行sql语句
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	private boolean execute(String sql)throws SQLException{
		Connection conn = null;
		Statement stmt = null;
		try{
			conn = DBUtil.getConnection();
			stmt = conn.createStatement();
			boolean re = stmt.execute(sql);
			conn.commit();
			return re;
		}catch(Exception e){
			conn.rollback();
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}finally{
			if(conn != null){
				conn.close();
			}
			if(stmt != null){
				stmt.close();
			}
		}
	}
	
	public static void main(String[] args) throws SQLException {
		TaskDAO taskDAO = new TaskDAO();
		long id = 2;
		Task task = taskDAO.getById(id);
		System.out.println("getName = " + task.getName());
		System.out.println("getcTime = " + task.getcTime());
		System.out.println("getfTime = " + task.getfTime());
		System.out.println("getStatus = " + task.getStatus());
		System.out.println("getMessage = " + task.getMessage());
		System.out.println("getType = " + task.getType());
		System.out.println("getOrders = " + task.getOrders().size());
		for(Order order : task.getOrders()){
			System.out.println("order .......");
			System.out.println("\tgetId = " + order.getId());
			System.out.println("\tgetMessage = " + order.getMessage());
			System.out.println("\tgetTid = " + order.getTid());
			System.out.println("\tgetClass = " + order.getClass());
		}
	}
}