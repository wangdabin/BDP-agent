package com.sky.agent.init;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import org.apache.commons.configuration.Configuration;

import com.sky.utils.AgentConfigConstant;
import com.sky.utils.AgentConfigUtil;

/**
 * @author 111216ll 对文件流的处理的工具
 */
public class StreamUtils {

	/**
	 * 将对应的输入流转成对应的输出流
	 * 
	 * @param fis
	 *            输入流
	 * @param os
	 *            输出流
	 * @throws IOException
	 */
	public static void input2Output(InputStream fis, OutputStream os)
			throws IOException {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(fis);
			bos = new BufferedOutputStream(os);
			byte[] bytes = new byte[1024];
			while (bis.read(bytes) > 0) {
				bos.write(bytes, 0, bytes.length);
			}
		} finally {
			bos.flush();
			bis.close();
			bos.close();
		}
	}

	/**
	 * 
	 * @param path
	 *            文件路径
	 * @param name
	 *            文件名称
	 * @return 输入流
	 * @throws IOException
	 */
	public static InputStream getInputStreamByFileName(String path, String name)
			throws IOException {
		return new FileInputStream(new File(path, name));
	}

	public static InputStream getInputStreamByFileName(String name)
			throws IOException {
		File file = new File(name);
		return new FileInputStream(file);
	}

	public static OutputStream getOutputStreamByFileName(String name)
			throws IOException {
		return new FileOutputStream(new File(name));
	}

	/**
	 * 功能：修改对应配置文件中的某个字段名 完成的必要条件：修改成功
	 * 
	 * @author WDB
	 * @throws Exception
	 */
	public static void setValue(String filedName, String value)
			throws Exception {
		Configuration conf = AgentConfigUtil.create();
		InputStream input = StreamUtils.getInputStreamByFileName(conf
				.getString(AgentConfigConstant.ENV_SH_PATH));
		StringBuffer buffer = new StringBuffer();
		Scanner scanner = new Scanner(input);
		boolean flag = false;
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			if (line.contains(filedName + "=")) {
				line = line.replaceFirst(filedName + "=.*", filedName + "="
						+ value);
			}
			if (flag) {
				buffer.append("," + line);
			} else {
				buffer.append(line);
			}
			flag = true;
		}
		scanner.close();
		OutputStream output = StreamUtils.getOutputStreamByFileName(conf
				.getString(AgentConfigConstant.ENV_SH_PATH));
		PrintWriter pw = new PrintWriter(output);
		String[] lines = buffer.toString().split(",");
		for (int i = 0; i < lines.length; i++) {
			pw.println(lines[i]);
		}
		pw.close();
	}
	/**
	 * @Title: fileToString
	 * @Description: 将对应的文件转化成String
	 * @throws
	 */
	public static String fileToString(String fileName){
		try{
			StringWriter sw = new StringWriter();
			BufferedWriter bw = new BufferedWriter(sw);
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String line; 
			while((line = br.readLine()) != null){
				bw.write(line);
			}
			bw.close();
			br.close();
			sw.close();
			return sw.toString();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	public static void stringToFile(String authorized_keys, String configFile) {
		try{
			File file =  new File(configFile);
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(configFile));
			bw.write(authorized_keys);
			bw.close();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
}
