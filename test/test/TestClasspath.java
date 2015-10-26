package test;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;

public class TestClasspath {

	public static void main(String[] args) {
		ClassLoader loader = TestClasspath.class.getClassLoader();
	    try {
	      for (Enumeration itr = loader.getResources("");
	          itr.hasMoreElements();) {
	        URL url = (URL) itr.nextElement();
	        System.out.println(url);
	        if ("jar".equals(url.getProtocol())) {
	          String toReturn = url.getPath();
	          if (toReturn.startsWith("file:")) {
	            toReturn = toReturn.substring("file:".length());
	          }
	          toReturn = URLDecoder.decode(toReturn, "UTF-8");
	          System.out.println(toReturn.replaceAll("!.*$", "")); 
	        }
	      }
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	}
}
