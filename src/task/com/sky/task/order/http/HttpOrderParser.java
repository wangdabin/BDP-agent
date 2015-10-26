package com.sky.task.order.http;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.sky.task.http.HttpDownload;
import com.sky.task.order.AbstractOrderParser;
import com.sky.task.shell.Executor;
import com.sky.task.vo.TranOrder;
import com.sky.utils.AgentConfigUtil;

/**
 * 解析http任务
 * @author Joe
 *
 */
public class HttpOrderParser extends AbstractOrderParser {

	public static final Logger LOG = Logger.getLogger(HttpOrderParser.class);

	public static final String DOWNLOAD_PACKAGE_CONF = "server.download.package.url";
	public static final String DOWNLOAD_CONFIG_CONF = "server.download.config.url";
	
	public HttpOrderParser(){
	}
	
	/**
	 * 查找解压后的路径
	 * @param parent
	 * @param serviceName
	 * @return
	 */
	private File findUncompressDir(File parent,final String serviceName){
		File[] files = parent.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(serviceName);
			}
		});
		if(files.length == 1){
			return files[0];
		}else{
			throw new RuntimeException("" + files.length);
		}
	}
	
	@Override
	protected void doOrder(TranOrder order) throws Exception {
		String[] coreFiles = order.getCoreFiles();
		if(coreFiles == null || coreFiles.length == 0){ // 下载安装包，并解压，然后移动到service目录
			HttpDownload httpDownload = new HttpDownload();
			File file = httpDownload.download(order);
			if(file.getName().toLowerCase().endsWith(".tar.gz")){//是压缩文件将进行解压缩
				LOG.info("Will tar -zxvf " + file.getAbsolutePath());
				Executor executor = new Executor(LOG,"tar");
				executor.addArgument("-zxvf",file.getAbsolutePath(),"-C",order.getDest());
				executor.execute();		
				int code = executor.waitFor();
				if(code != 0){
					throw new RuntimeException("return code is " + code);
				}else{
					LOG.debug("Delete package " + file);
					file.delete();
				}
				
				//移动目录。。。
				
				File destFile = new File(order.getDest(),order.getServiceName());
				File uncompressFile = findUncompressDir(destFile.getParentFile(),order.getServiceName());
				LOG.info("Will mv " + uncompressFile + " " + destFile.getAbsolutePath());
				boolean mvCode = uncompressFile.renameTo(destFile);
				
				if(!mvCode){
					throw new RuntimeException("return code is " + code);
				}
			}else{
				LOG.warn("Will delete " + file);
				file.delete();
				throw new RuntimeException("Download service " + order.getServiceName() + " version " + order.getVersion() + " file " + file.getName() + " is not compressed!");
			}
		}else{ // 配置文件下载，配置文件下载之前要先备份，再进行下载覆盖。
			String vs[] = coreFiles;
			for(String configFile : vs){
				File destFile = new File(order.getDest(),configFile);//目标文件。。。
				//备份原来的文件。。。
				
			}
			HttpDownload httpDownload = new HttpDownload();
			File file = httpDownload.download(order);
			LOG.info("Download file " + file + " success..");
		}
	}

	@Override
	public void kill(TranOrder order) throws Exception {
		
	}

	@Override
	protected void destory(TranOrder order) throws Exception {
		
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf = AgentConfigUtil.create();
		HttpOrderParser parser = new HttpOrderParser();
		parser.setConf(conf);
		
//		String src = "http://192.168.32.59:8098/download/package?serviceName=zookeeper";
		String dest = "/Users/qiaolong/test/test";
		TranOrder order = new TranOrder();
		order.setSrc("");
		order.setServiceName("zookeeper");
		order.setVersion("chd5.1.0");
		order.setDest(dest);
		parser.doOrder(order);
		System.out.println();
	}

}
