/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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

import java.util.Hashtable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is the common structure for all JSON responses.  All requests will have this format,
 * though some of the fields are optional.  Any function-specific response values will 
 * be stored in the "data" property.
 * 
 * <p>The "echoString" property is a legacy property used to pass the original JSON request 
 * string back to the client, in addition to the regular output.
 * 
 * @author Adam Andrews
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {

	@JsonInclude(Include.NON_NULL)
	private ServiceStatus status;
	// this is a replacement sessionid in case it has changed
	@JsonInclude(Include.NON_NULL)
	private String sessionId;
	@JsonInclude(Include.NON_NULL)
	private String echoString;
	@JsonInclude
	private Hashtable<String, Object> data;
	
	/**
	 * Get the status object used for error handling
	 * @return The status object
	 */
	public ServiceStatus getStatus() {
		return status;
	}
	/**
	 * Set the status object used for error handling
	 * @param status The status object
	 */
	public void setStatus(ServiceStatus status) {
		this.status = status;
	}
	/**
	 * Get the new (replacement) session ID
	 * @return The new session ID
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * Set the new (replacement session ID
	 * @param sessionId The enw session ID
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * Get the string containing an echo of the original JSON request
	 * @return The request as a JSON string
	 */
	public String getEchoString() {
		return echoString;
	}
	/**
	 * Get the string containing an echo of the original JSON request
	 * @param echoString The request as a JSON string
	 */
	public void setEchoString(String echoString) {
		this.echoString = echoString;
	}
	/**
	 * Get the function-specific output data
	 * @return The output data as a generic Hashtable object
	 */
	public Hashtable<String, Object> getData() {
		return data;
	}
	/**
	 * Set the function-specific output data
	 * @param data The output data as a generic Hashtable object
	 */
	public void setData(Hashtable<String, Object> data) {
		this.data = data;
	}
}
