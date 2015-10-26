package com.sky.agent.exception;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.sun.jersey.api.NotFoundException;

/**
 * 统一异常处理器
 */
@Provider
public class ExceptionMapperSupport implements ExceptionMapper<Exception> {

	private static final Logger LOG = Logger.getLogger(ExceptionMapperSupport.class);
	public static final String NAME = "exception";
	
	@Context
	private HttpServletRequest request;

	@Context
	private HttpHeaders headers;
	
	public ExceptionMapperSupport(){
	}

	/**
	 * 异常处理
	 * 
	 * @param exception
	 * @return 异常处理后的Response对象
	 */
	public Response toResponse(Exception exception) {
		String message = "internal.server.error";
		Status statusCode = Status.INTERNAL_SERVER_ERROR;
		String acceptHeader = request.getHeader("accept");
		MediaType mediaType = null;
		if (MediaType.APPLICATION_XML.equals(acceptHeader)) {
			mediaType = MediaType.APPLICATION_XML_TYPE;
		} else if (MediaType.APPLICATION_JSON.equals(acceptHeader)) {
			mediaType = MediaType.APPLICATION_JSON_TYPE;
		} else {
			mediaType = headers.getMediaType();
			if (mediaType == null) {
				mediaType = MediaType.APPLICATION_XML_TYPE;
			}
		}
		if (exception instanceof RuntimeException) {
			message = exception.getMessage();
			statusCode = Status.INTERNAL_SERVER_ERROR;
		} else if (exception instanceof NotFoundException) {
			message = "Not found exception";
			statusCode = Status.NOT_FOUND;
		}
		ErrorMessage errorMessage = new ErrorMessage();
		errorMessage.setCode(statusCode.getStatusCode());
		errorMessage.setMessage(message);
		LOG.error(message, exception);
		return Response.ok(errorMessage).type(mediaType).status(statusCode).build();
	}
}
