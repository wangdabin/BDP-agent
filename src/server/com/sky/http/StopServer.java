package com.sky.http;

import java.io.File;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StopServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		stop(false);
	}

	/**
	 * Stop a running jetty instance.
	 */
	public static void stop(boolean delete) {
		int _port = Integer.getInteger("STOP.PORT", -1).intValue();
		String _key = System.getProperty("STOP.KEY", null);

		try {
			if (_port <= 0)
				System.err
						.println("STOP.PORT system property must be specified");
			if (_key == null) {
				_key = "";
				System.err
						.println("STOP.KEY system property must be specified");
				System.err.println("Using empty key");
			}

			Socket s = new Socket(InetAddress.getByName("127.0.0.1"), _port);
			OutputStream out = s.getOutputStream();
			out.write((_key + "\r\nstop\r\n").getBytes());
			out.flush();
			s.close();
		} catch (ConnectException e) {
			System.err.println("ERROR: Not running!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (delete) {
			System.out.println("WARN: Will delete current application!");
			String bigdataHome = System.getProperty("BIGDATA.HOME");
			if (!StringUtils.isBlank(bigdataHome)) {
				System.out.println("INFO: Find BIGDATA_HOME " + bigdataHome);
				File file = new File(bigdataHome);
				List<File> files = new ArrayList<File>();
				deleteDir(file, files);
				Collections.reverse(files);
				for (File f : files) {
					f.deleteOnExit();
				}
			} else {
				System.err.println("ERROR: BIGDATA_HOME not found.");
			}
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	private static void deleteDir(File dir, List<File> files) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				deleteDir(new File(dir, children[i]), files);
			}
		}
		// 目录此时为空，可以删除
		files.add(dir);
	}
}
