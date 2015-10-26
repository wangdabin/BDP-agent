package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sky.agent.version.AgentVersion;

@Path(AgentVersion.BASE_PATH + "/testRest")
public class RestTest {

	
	@Context
	private HttpServletRequest request;

	@POST
	@Path("/req")
	public Response consumeRequest() {
	    try {
	        final BufferedReader rd = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
	        String line = null;
	        final StringBuffer buffer = new StringBuffer(2048);
	        while ((line = rd.readLine()) != null) {
	            buffer.append(line);
	        }
	        final String data = buffer.toString();
	        return Response.ok().entity(data).build();
	    } catch (final Exception e) {
	        return Response.status(Status.BAD_REQUEST).entity("No data supplied").build();
	    }
	}
	
	@POST
//	@Path("reurl")
//	@Consumes("application/xml")
	// 提交内容为xml流
	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
	public Response returnResponse(InputStream is) {
		try {
			byte[] buf = new byte[is.available()]; // the inputstream 's length
													// is 0! i can't get the
													// data from inputstream.
			System.out.println(buf.length);
			is.read(buf, 0, buf.length);
			System.out.println(new String(buf));
			return Response.created(new URI("http://g.cn")).build();
		} catch (URISyntaxException e) {
			throw new WebApplicationException(e,
					Response.Status.INTERNAL_SERVER_ERROR);
		} catch (IOException e1) {
			throw new WebApplicationException(e1,
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void testname() throws Exception {
//		// initialize our custom reader
//		TransientAnnotationReader reader = new TransientAnnotationReader();
//		reader.addTransientField(Throwable.class.getDeclaredField("stackTrace"));
//		reader.addTransientMethod(Throwable.class.getDeclaredMethod("getStackTrace"));
//
//		// initialize JAXB context
//		Map jaxbConfig = new HashMap();
//		jaxbConfig.put(JAXBRIContext.ANNOTATION_READER, reader);
//		String yourPackages = "deltix.qsrv.pub:deltix.qsrv.comm.xml:"; // replace this
//		JAXBContext ctx = JAXBContext.newInstance (yourPackages, TransientAnnotationReader.class.getClassLoader(), jaxbConfig);
//
//		// XMLlize something
//		Marshaller m = ctx.createMarshaller ();
//		m.marshal (object, System.out);
	}
}
