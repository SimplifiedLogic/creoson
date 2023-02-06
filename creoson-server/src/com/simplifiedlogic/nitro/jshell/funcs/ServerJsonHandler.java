/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jshell.funcs;

import java.util.Hashtable;

import com.simplifiedlogic.nitro.jshell.ServerException;

/**
 * Handle server-specific JSON requests
 * 
 * @author Adam Andrews
 *
 */
public class ServerJsonHandler implements ServerRequestParams, ServerResponseParams {

	/**
	 * Receive a request for the "server" family of functions and pass it on to the
	 * appropriate action handler
	 * 
	 * @param sessionId The user's current session ID
	 * @param function Name of the function to execute
	 * @param input Structure containing any JSON data passed in by the user
	 * @return A standard response containing the results of the request
	 * @throws ServerException
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws ServerException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_PWD)) return actionPwd(sessionId, input);
		else {
			throw new ServerException("Unknown function name: " + function);
		}
	}

	/**
	 * Handle a "pwd" request.
	 * 
	 * @param sessionId The user's current session ID
	 * @param input Structure containing any JSON data passed in by the user
	 * @return A standard response containing the results of the request
	 * @throws ServerException
	 */
	private Hashtable<String, Object> actionPwd(String sessionId, Hashtable<String, Object> input) throws ServerException {
		// validate session?  we can't without access to the jshell layer

		String dir = System.getProperty("user.dir");

		if (dir!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
	        out.put(OUTPUT_DIRNAME, dir.replace('\\', '/'));
	       	return out;
		}
		return null;
	}

}
