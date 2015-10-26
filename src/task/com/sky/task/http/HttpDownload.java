package com.sky.task.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.sky.task.vo.TranOrder;

/**
 * 
 * @author Joe
 *
 */
public class HttpDownload {

	private static final Logger LOG = Logger.getLogger(HttpDownload.class);
	private static final int buffer = 64 * 1024;
	private static final String regex = "attachment\\s*;\\s*filename=(.+)";
	private static final Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
	private static final String DOWNLOAD_SUFFIX = ".download";
	private static final String BACKUP_SUFFIX = ".backup";
	private List<DownloadListener> listeners;
	
	public HttpDownload(){
		this.listeners = new ArrayList<DownloadListener>();
	}
	
	/**
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public File download(TranOrder order) throws IOException{
		LOG.debug("Will download " + order.getSrc() + " to " + order.getDest());
		HttpURLConnection urlConn = (HttpURLConnection) new URL(order.getSrc()).openConnection();
		urlConn.setRequestProperty("Accept-Charset", "utf-8");
		urlConn.setRequestProperty("Content-Type", "application/octet-stream");
//		String sProperty = "bytes=" + buffer + "-";
//		urlConn.setRequestProperty("RANGE", sProperty);
		int reCode = urlConn.getResponseCode();
		if(reCode == HttpServletResponse.SC_OK){
			int fileSize = urlConn.getContentLength();
			LOG.debug("File size : " + fileSize);
			String fileName = getFileName(urlConn);
			for(DownloadListener listener : listeners){
				listener.init(order.getSrc(), order.getDest(), fileSize);
			}
			InputStream in = urlConn.getInputStream();
			try{
				return this.doOutput(in, order.getDest(),fileName,fileSize);
			}finally{
				in.close();
			}
		}else{
			String message = "HTTP Request is not success, Response code is " + reCode;
			LOG.error(message);
			throw new IOException(message);
		}
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void addListener(DownloadListener listener){
		this.listeners.add(listener);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void removeListener(DownloadListener listener){
		this.listeners.remove(listener);
	}
	
	/**
	 * 删除所有的监听器
	 */
	public void removeAllListener(){
		this.listeners.clear();
	}
	
	/**
	 * 
	 * @param in
	 * @param dest
	 * @param fileName
	 * @throws IOException
	 */
	private File doOutput(InputStream in,String dest,String fileName,int fileSize)throws IOException{
		File destFile = new File(dest,fileName);//下载到目标文件
		File downloadFile = new File(dest,fileName + DOWNLOAD_SUFFIX); //正在下的文件。。
		File backupFile = new File(dest,fileName + BACKUP_SUFFIX);
		if(!destFile.getParentFile().exists()){
			destFile.getParentFile().mkdirs();
		}
		OutputStream out = new FileOutputStream(downloadFile);
		try{
			byte[] b = new byte[buffer];
			int len = in.read(b);
			while(len > 0){
				out.write(b, 0, len);
				for(DownloadListener listener : listeners){
					listener.count(len);
				}
				len = in.read(b);
			}
			out.flush();
			out.close();
			for(DownloadListener listener : listeners){
				listener.end();
			}
			
			if(downloadFile.length() == fileSize){
				LOG.debug("Download file mather");
			}else{
				LOG.debug("Dest file size : " + fileSize + " but get file size : " + downloadFile.length());
			}
			
			if(destFile.exists()){ //先把目标文件移动到 .backup 文件，移动成功，再把.download 文件移动到目标文件，如果移动成功，再把 .backup 文件删除
				LOG.warn("Dest file " + destFile + " already exists,will backup to " + backupFile);
				boolean back = destFile.renameTo(backupFile);
				if(back){//移动到.backup文件成功
					boolean down = downloadFile.renameTo(destFile);
					if(down){//如果下载的文件移动成功，则删除备份文件
						backupFile.delete();
					}else{//移动失败，重新把backup文件移动回来,并删除下的文件。
						backupFile.renameTo(destFile);
						downloadFile.delete();
						throw new RuntimeException("File " + downloadFile + " can not mv to " + destFile + " update error..");
					}
				}else{
					throw new RuntimeException("File " + destFile + " can not mv to " + backupFile);
				}
			}else{
				downloadFile.renameTo(destFile);
			}
			return destFile;
		}finally{
			try{
				out.close();
			}catch(Exception e){
			}
		}
	}
	
	/**
	 * 
	 * @param urlConn
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String getFileName(URLConnection urlConn) throws UnsupportedEncodingException{
		String contentDisposition = urlConn.getHeaderField(HttpHeaders.CONTENT_DISPOSITION);
		Matcher m = pattern.matcher(contentDisposition);
		m.find();
		return URLDecoder.decode(m.group(1), "utf-8");
	}
	
	public static void main(String[] args) throws IOException {
		String src = "http://192.168.32.59:8098/download/package?serviceName=zookeeper";
		String dest = "/Users/qiaolong/test/test";
		TranOrder order = new TranOrder();
		order.setSrc(src);
		order.setDest(dest);
		HttpDownload download = new HttpDownload();
		download.download(order);
		System.out.println();
	}
}
