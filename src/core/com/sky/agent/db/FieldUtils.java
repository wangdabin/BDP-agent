package com.sky.agent.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * 
 * 整理字段
 * @author user
 *
 */
public class FieldUtils {

	private static final Pattern GET_PATTERN = Pattern.compile("^get|^is", Pattern.CASE_INSENSITIVE);
	private static final Pattern SET_PATTERN = Pattern.compile("^set", Pattern.CASE_INSENSITIVE);
	/**
	 * 
	 * @param methods
	 * @param dbName
	 * @param tableName
	 * @return
	 */
	public static String getFields(Class<?> clazz,String dbName,String tableName){
		Annotation classUse = clazz.getAnnotation(Use.class);
		Method[] methods = clazz.getMethods();
		StringBuilder fields = new StringBuilder();
		boolean flag = false;
		for(Method method : methods){
			String name = method.getName();
			if(isGet(name)){
				Annotation methodUse = method.getAnnotation(Use.class);
				if(classUse != null || methodUse != null){
					if(dbName.trim().length() != 0){
						fields.append(dbName).append(".");
					}
					fields.append(tableName).append(".");
					fields.append(toGetField(name)).append(",");
					flag = true;
				}
			}
		}
		if(flag){
			fields.setLength(fields.length() - 1);
		}
		return fields.toString();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static final boolean isGet(String name){
		return GET_PATTERN.matcher(name).find() && !name.equals("getClass");
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static final boolean isSet(String name){
		return SET_PATTERN.matcher(name).find();
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String toGetField(String name){
		return GET_PATTERN.matcher(name).replaceFirst("");
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String toSetField(String name){
		return SET_PATTERN.matcher(name).replaceFirst("");
	}
}
