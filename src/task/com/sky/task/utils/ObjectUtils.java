package com.sky.task.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.sun.jersey.core.util.Base64;

/**
 * 对象工具类
 * @author Joe
 *
 */
public class ObjectUtils {

	/**
	 * object 转成 base64 string
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static final String toString(Serializable obj) throws IOException{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(byteOut);
		oos.writeObject(obj);
		oos.close();
		return new String(Base64.encode(byteOut.toByteArray()));
	}
	
	/**
	 * base64 string 转成 object
	 * @param value
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static final Object toObject(String value) throws IOException, ClassNotFoundException{
		byte[] b = Base64.decode(value.getBytes());
		ByteArrayInputStream bin = new ByteArrayInputStream(b);
		ObjectInputStream oin = new ObjectInputStream(bin);
		oin.close();
		return oin.readObject();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String kk = "kkkkk";
		System.out.println(toString(kk));
		System.out.println(toObject("rO0ABXQABWtra2tr"));
	}
}
