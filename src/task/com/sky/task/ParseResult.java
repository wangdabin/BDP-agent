package com.sky.task;

import com.sky.task.vo.Task;

/**
 * 
 * @author Dragon Joey
 * 
 */
public class ParseResult {

    private String id;
    private boolean success;
    private int status;
    private String message;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
	return success;
    }

    /**
     * @param success
     *            the success to set
     */
    public void setSuccess(boolean success) {
	this.success = success;
    }

    /**
     * @return the status
     */
    public int getStatus() {
	return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
	this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
	this.message = message;
    }

    /**
     * 
     * @param id
     * @param success
     * @param message
     * @param status
     * @return
     */
    public static ParseResult getResult(String id,boolean success, String message,
	    int status) {
	ParseResult result = new ParseResult();
	result.setId(id);
	result.setSuccess(success);
	result.setMessage(message);
	result.setStatus(status);
	return result;
    }

    /**
     * 
     * @param id
     * @return
     */
    public static ParseResult getFinishResult(String id) {
	return getResult(id, true, "success", Task.FINISH);
    }
}
