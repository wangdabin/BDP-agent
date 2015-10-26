package com.sky.task.http;

/**
 * 下载监控
 * @author Joe
 * 
 */
public interface DownloadListener {
	/**
	 * 初始化
	 * @param src
	 * @param dest
	 * @param totalSize
	 */
	public void init(String src, String dest, long totalSize);

	/**
	 * 这一次读了多少空间
	 * @param readSize
	 * @return
	 */
	public boolean count(long readSize);

	/**
	 * 结束
	 */
	public void end();
}
