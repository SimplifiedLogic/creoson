/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
 * Permission is hereby granted, free of charge, to any person obtaining a copy 
 * of this software and associated documentation files (the "Software"), to deal 
 * in the Software without restriction, including without limitation the rights 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions: The above copyright 
 * notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", 
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.simplifiedlogic.nitro.jshell.json.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class contains general Status data to be returned in JSON response to the user.
 * Primarily meant for error handling.
 * 
 * <p>Although there is an "expired" flag defined, its use is not supprted by the server
 * currently.
 * 
 * @author Adam Andrews
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceStatus {

	// response fields for JSON
	public static final String PARAM_ERROR 		= "error";
	public static final String PARAM_MESSAGE 	= "message";
	public static final String PARAM_EXPIRED 	= "expired";
	
	@JsonInclude
	private boolean error;
	@JsonInclude(Include.NON_NULL)
	private String message;
	@JsonInclude(Include.NON_DEFAULT)
	private boolean expired;
	
	/**
	 * Construct a new status object with an error message.  This will
	 * set the error flag.
	 * 
	 * @param msg The text of the error message
	 * @return The new ServiceStatus object
	 */
	public static ServiceStatus error(String msg) {
		ServiceStatus status = new ServiceStatus();
		status.setError(true);
		status.setMessage(msg);
		return status;
	}
	
	/**
	 * Construct a new status object with an expired status.  This will
	 * set the error flag and a standard error message.
	 * 
	 * @return The new ServiceStatus object
	 */
	public static ServiceStatus expired() {
		ServiceStatus status = new ServiceStatus();
		status.setError(true);
		status.setMessage("Your session has expired; please connect again");
		status.setExpired(true);
		return status;
	}
	
	/**
	 * Default constructor
	 */
	public ServiceStatus() {
	}
	
	/**
	 * Get the error flag
	 * @return The error flag
	 */
	public boolean getError() {
		return error;
	}
	/**
	 * Set the error flag
	 * @param error True if an error has occurred
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	/**
	 * Get the status (error) message text
	 * @return The message text
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Set the status (error) message text
	 * @param message The message text
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * Get the expired flag
	 * @return The expired flag
	 */
	public boolean isExpired() {
		return expired;
	}
	/**
	 * Set the expired flag
	 * @param expired True if the session has expired
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	
}
