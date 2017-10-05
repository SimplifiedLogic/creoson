/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jshell.json.request;

import java.util.Hashtable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This is the common structure for all JSON requests.  All requests will have this format,
 * though some of the fields are optional.  Any function-specific input parameters will 
 * be stored in the "data" property.
 * 
 * <p>The "echo" property is a legacy property that will cause the original JSON string to 
 * be passed back to the client in the response, in addition to the regular function output.
 * 
 * @author Adam Andrews
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseRequest {

	@JsonInclude(Include.NON_NULL)
	private String sessionId;
	@JsonInclude(Include.NON_NULL)
	private String command;
	@JsonInclude(Include.NON_NULL)
	private String function;
	@JsonInclude(Include.NON_DEFAULT)
	private boolean echo;
	@JsonInclude(Include.NON_EMPTY)
	private Hashtable<String, Object> data;
	
	/**
	 * Forwarding method for adding a data item to the request
	 * @param key The item's key
	 * @param value The item's value
	 * @return The old value for the key, or null if there was no old value
	 */
	public Object put(String key, Object value) {
		if (data==null)
			data = new Hashtable<String, Object>();
		return data.put(key, value);
	}

	/**
	 * Forwarding method for getting a data item from the request
	 * @param key The item's key
	 * @return The value for the key, or null if there was no value
	 */
	public Object get(String key) {
		if (data==null)
			return null;
		return data.get(key);
	}

	/**
	 * Get the user's session ID
	 * @return The session ID
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * Set the user's session ID
	 * @param sessionId The session iD
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	/**
	 * Get the request's command (function family)
	 * @return The command for the request
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * Set the request's command (function family)
	 * @param command The command for the request
	 */
	public void setCommand(String command) {
		this.command = command;
	}
	/**
	 * Get the function name within the command
	 * @return The function name
	 */
	public String getFunction() {
		return function;
	}
	/**
	 * Set the function name within the command
	 * @param function The function name
	 */
	public void setFunction(String function) {
		this.function = function;
	}
	/**
	 * Get the echo property
	 * @return True if the request string is to be echoed back to the client
	 */
	public boolean isEcho() {
		return echo;
	}
	/**
	 * Set the echo property
	 * @param echo True if the request string is to be echoed back to the client
	 */
	public void setEcho(boolean echo) {
		this.echo = echo;
	}
	/**
	 * Get the function-specific input data
	 * @return The input data as a generic Hashtable object
	 */
	public Hashtable<String, Object> getData() {
		return data;
	}
	/**
	 * Set the function-specific input data
	 * @param data The input data as a generic Hashtable object
	 */
	public void setData(Hashtable<String, Object> data) {
		this.data = data;
	}
	
}
