package test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jettison.json.JSONObject;

import com.joe.core.rest.RestClient;
import com.sky.task.vo.Task;
import com.sky.task.vo.TranOrder;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class TestClient {

	public static void main(String[] args) throws IOException {
		
//		testFormUrlencoded();
//		testStream();
//		testTask();
		
		testCpuMonitor();
//		testCpuMonitorDel();
		
	}
	
	
	public static final void testCpuMonitor(){
		String url = "http://172.16.6.150:8099/ws/v1/monitor/cpu/add";
		MultivaluedMap<String,String> queryParams = new MultivaluedMapImpl();
		queryParams.add("schedule", "*/2 * * * *");
		ClientResponse resp = RestClient.post(ClientResponse.class, url, queryParams);
		System.out.println("status = " + resp.getStatus());
		JSONObject json = resp.getEntity(JSONObject.class);
		System.out.println(json);
	}
	
	public static final void testCpuMonitorDel(){
		String url = "http://localhost:8099/ws/v1/monitor/cpu/delete";
//		queryParams.add("schedule", "*/2 * * * *");
		ClientResponse resp = RestClient.delete(ClientResponse.class, url, null);
		System.out.println("status = " + resp.getStatus());
		JSONObject json = resp.getEntity(JSONObject.class);
		System.out.println(json);
	}
	
	public static final void testTask(){
		long id = 4;
		Task task = new Task();
		task.setType("shell");
		task.setName("test");
		task.setId(id);
		TranOrder order = new TranOrder();
		order.setId(id);
		order.setCommand("ls");
		order.setParams("/Users/qiaolong");
		order.setTid(id);
		task.addOrder(order);
		testCommit(task);
	}
	
	public static final void testCommit(Task task){
		String targetURL = "http://localhost:8099/ws/v1/tasks";
		Client client = Client.create();
		WebResource resource = client.resource(targetURL);
		JSONObject response = resource.type(MediaType.APPLICATION_JSON_TYPE).post(JSONObject.class,task);
		System.out.println(response);
	}
	
	public static final void testStream() throws IOException{
		String targetURL = "http://localhost:8099/ws/v1/testRest/req";
		String message = "message";
	    URL url = new URL(targetURL);
	    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

	    urlConnection.setDoInput(true);
	    urlConnection.setDoOutput(true);
	    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
	    wr.write(message);
	}
	
	public static final void testFormUrlencoded(){
		Client client = Client.create();
		String uri = "http://localhost:8099/ws/v1/testRest";
		final String addCustomerXML = "<customer>"
				+ "<firstname>Joe</firstname>" + "<lastname>Schmo</lastname>"
				+ "<zipcode>98042</zipcode>" + "</customer>";
		InputStream bais = new ByteArrayInputStream(addCustomerXML.getBytes());
		WebResource resource = client.resource(uri);
//		ClientResponse response = resource.type(MediaType.APPLICATION_XML)
//				.entity(addCustomerXML).post(ClientResponse.class);
		ClientResponse response = resource.type(MediaType.MULTIPART_FORM_DATA)
				.entity(bais).post(ClientResponse.class);
		System.out.println("response status:" + response.getStatus());
		System.out.println("response " + response.getLocation().toString());
	}
}
