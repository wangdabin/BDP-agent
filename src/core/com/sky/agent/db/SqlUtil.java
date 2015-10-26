package com.sky.agent.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 * @author Joe
 *
 */
public class SqlUtil {

	/**
	 * 
	 * @param methods
	 * @param dbName
	 * @param tableName
	 * @return
	 */
	public static String getInsertSql(Method[] methods,String dbName,String tableName){
		StringBuilder fields = new StringBuilder();
		StringBuilder values = new StringBuilder();
		StringBuilder sql = new StringBuilder();
		
		if(dbName != null && dbName.trim().length() != 0){
			fields.append("insert into ").append(dbName).append(".").append(tableName);
		}else{
			fields.append("insert into ").append(tableName);
		}
		
		fields.append("(");
		values.append("values(");
		boolean flag = false;
		for(Method method : methods){
			String name = method.getName();
			if(FieldUtils.isGet(name)){
				fields.append(FieldUtils.toGetField(name)).append(",");
				values.append("?").append(",");
				flag = true;
			}
		}
		if(flag){
			fields.setLength(fields.length() - 1);
			values.setLength(values.length() - 1);
		}
		fields.append(")");
		values.append(")");
		sql.append(fields).append(" ").append(values);
		return sql.toString();
	}
	
	/**
	 * 
	 * @param methods
	 * @param dbName
	 * @param tableName
	 * @param byField 根据某个字段更新
	 * @return
	 */
	public static String getUpdateSql(Method[] methods,String dbName,String tableName,String byField){
		StringBuilder fields = new StringBuilder();
		StringBuilder sql = new StringBuilder();
		
		if(dbName != null && dbName.trim().length() != 0){
			fields.append("update ").append(dbName).append(".").append(tableName);
		}else{
			fields.append("update ").append(tableName);
		}
		
		fields.append(" set ");
		boolean flag = false;
		for(Method method : methods){
			String name = method.getName();
			if(FieldUtils.isGet(name)){
				String field = FieldUtils.toGetField(name);
				if(byField.equalsIgnoreCase(field)){
					continue;
				}	
				fields.append(FieldUtils.toGetField(name)).append("=").append("?,");
				flag = true;
			}
		}
		if(flag){
			fields.setLength(fields.length() - 1);
		}
		
		sql.append(fields).append(" where ").append(byField).append("=?");
		return sql.toString();
	}
	
	
	public static void main(String[] args) {
		System.out.println((Double)null);
	}
	
	/**
	 * 
	 * @param clazz
	 * @param dbName
	 * @param tableName
	 * @return
	 */
	public static String getInsertSql(Class<?> clazz,String dbName,String tableName){
		return getInsertSql(clazz.getMethods(), dbName, tableName);
	}
	
	/**
	 * 
	 * @param dbName
	 * @param tableName
	 * @param value
	 * @return
	 */
	public static String getInsertSql(String dbName,String tableName,Object value){
		return getInsertSql(value.getClass(), dbName, tableName);
	}
	
	/**
	 * 填充 PreparedStatement 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws SQLException 
	 */
	public static void fillInsertStatement(PreparedStatement stmt,Object obj,Method[] methods) throws Exception{
		int i = 1;
		for(Method method : methods){
			String name = method.getName();
			if(FieldUtils.isGet(name)){
				Object value = method.invoke(obj, null);
				Class<?> reClass = method.getReturnType();
				if(reClass.equals(Long.class)){
					if(value == null){
						value = new Long(0);
					}
					stmt.setLong(i++, (Long)value);
				}else if(reClass.equals(Double.class)){
					if(value == null){
						value = new Double(0.0);
					}
					stmt.setDouble(i++, (Double)value);
				}else{
					stmt.setString(i++, (String)value);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param rs
	 * @param obj
	 * @param methods
	 * @throws SQLException 
	 */
	public static void fillObject(ResultSet rs,Object obj,Method[] methods) throws Exception{
		for(Method method : methods){
			String name = method.getName();
			if(FieldUtils.isSet(name)){
				String field = FieldUtils.toSetField(name);
				Object value = rs.getObject(field);
				if(value != null){
					method.invoke(obj, value);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param stmt
	 * @param obj
	 * @param methods
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public static void fillUpdateStatement(PreparedStatement stmt,Object obj,Method[] methods,String byField) throws Exception{
		int i = 1;
		Method byMethod = null;
		for(Method method : methods){
			String name = method.getName();
			if(FieldUtils.isGet(name)){
				String field = FieldUtils.toGetField(name);
				if(field.equalsIgnoreCase(byField)){
					byMethod = method;
					continue;
				}
				Object value = method.invoke(obj, null);
				Class<?> reClass = method.getReturnType();
				if(reClass.equals(Long.class)){
					if(value == null){
						value = new Long(0);
					}
					stmt.setLong(i++, (Long)value);
				}else if(reClass.equals(Double.class)){
					if(value == null){
						value = new Double(0.0);
					}
					stmt.setDouble(i++, (Double)value);
				}else{
					stmt.setString(i++, (String)value);
				}
			}
		}
		
		Object value = byMethod.invoke(obj, null);
		Class<?> reClass = byMethod.getReturnType();
		if(reClass.equals(Long.class)){
			if(value == null){
				value = new Long(0);
			}
			stmt.setLong(i++, (Long)value);
		}else if(reClass.equals(Double.class)){
			if(value == null){
				value = new Double(0.0);
			}
			stmt.setDouble(i++, (Double)value);
		}else{
			stmt.setString(i++, (String)value);
		}
	}
}
