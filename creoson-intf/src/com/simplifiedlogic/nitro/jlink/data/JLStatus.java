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
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;

/**
 * Data about the status of a connection to JShell, to be returned to a calling application.
 * 
 * @author Adam Andrews
 *
 */
public class JLStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean success=false;
	private String sessionId;
	private int errorCode=0;
	private boolean expired=false;
	private String message=null;
	
	/**
	 * Set an error condition into the status 
	 * @param errorCode A system-defined error code
	 * @param expired Whether the user's session is expired
	 * @param message An error message
	 */
	public void setError(int errorCode, boolean expired, String message) {
		this.errorCode = errorCode;
		this.expired = expired;
		this.message = message;
	}
	
	/**
	 * @return A system-defined error code
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode A system-defined error code
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return Whether the user's session is expired
	 */
	public boolean isExpired() {
		return expired;
	}
	/**
	 * @param expired Whether the user's session is expired
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
	}
	/**
	 * @return An error or informational message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message An error or informational message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return A session ID
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId A session ID
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return Whether the connection was successful
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success Whether the connection was successful
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
