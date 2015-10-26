package com.sky.task.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author Joe
 * 
 */
public class ConsoleManager extends Thread {

	private static final Logger LOG = Logger.getLogger(ConsoleManager.class);
	private List<Console> consoles;
	InputStream in;
	String type;

	public ConsoleManager(List<Console> consoles, InputStream in, String type) {
		this.consoles = consoles;
		this.type = type;
		this.in = in;
	}

	public void run() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while ((line = reader.readLine()) != null) {// will block
				if (type.equalsIgnoreCase("ERROR")) {
					for (Console console : consoles) {
						console.writeError(line);
					}
				} else {
					for (Console console : consoles) {
						console.writeInfo(line);
					}
				}
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
