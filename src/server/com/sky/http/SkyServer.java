package com.sky.http;

import java.io.IOException;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.sky.jetty.HttpServer;
import com.sky.utils.AgentConfigUtil;

/**
 * 服务启动器
 * @author Joe
 *
 */
public class SkyServer {

	private static final Logger LOG = Logger.getLogger(SkyServer.class);
    private HttpServer server;
    private int port;

    public SkyServer(Configuration conf, int port) throws Exception {
		if (port <= 0 || port >= 65535) {
		    this.port = Integer.parseInt(conf.getString("sky.http.port", "8099"));
		} else {
		    this.port = port;
		}
		server = new HttpServer(conf,"webapp", "0.0.0.0", this.port, true);
	}

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
		String usage = "Usage: SkyServer  [port]";
		int port = 0;
		if (args.length == 1) {
			try{
				port = Integer.parseInt(args[0]);
			}catch(Exception e){
				System.out.println(usage);
			}
		}
		Configuration conf = AgentConfigUtil.create();
		SkyServer server = new SkyServer(conf, port);
		try{
			server.start();
		}catch(Exception e){
			try{
				server.stop();
			}catch(Exception e1){}
			LOG.error(e);
			System.exit(-1);
		}
    }

    public void start() throws IOException {
	this.server.start();
    }

    public void stop() throws Exception {
	if (server != null)
	    this.server.stop();
    }
}
