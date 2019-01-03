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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.util.Hashtable;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.JLStatus;
import com.simplifiedlogic.nitro.jlink.intf.IJLConnection;
import com.simplifiedlogic.nitro.jshell.json.request.JLConnectRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLConnectResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "connection" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonConnectionHandler extends JLJsonCommandHandler implements JLConnectRequestParams, JLConnectResponseParams {

	private IJLConnection connHandler = null;

	/**
	 * @param connHandler
	 */
	public JLJsonConnectionHandler(IJLConnection connHandler) {
		this.connHandler = connHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_CONNECT))
			return actionConnect(input);
		else if (function.equals(FUNC_DISCONNECT))
			return actionDisconnect(sessionId, input);
		else if (function.equals(FUNC_IS_RUNNING))
			return actionIsCreoRunning(sessionId, input);
		else if (function.equals(FUNC_START_PROE))
			return actionStartCreo(sessionId, input);
		else if (function.equals(FUNC_STOP_PROE))
			return actionStopCreo(sessionId, input);
		else if (function.equals(FUNC_KILL_PROE))
			return actionKillCreo(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionConnect(Hashtable<String, Object> input) throws JLIException {
		JLStatus connectStatus = connHandler.connect(JLConnectRequestParams.APP_PROE);
		if (connectStatus.isExpired())
			throw new JLIException("Session has expired");
		if (!connectStatus.isSuccess()) {
			if (connectStatus.getMessage()!=null)
				throw new JLIException(connectStatus.getMessage());
			else
				throw new JLIException("Could not connect");
		}
		if (connectStatus.getSessionId()!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_SESSIONID, connectStatus.getSessionId());
			return out;
		}
		
		return null;
	}

	private Hashtable<String, Object> actionDisconnect(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		connHandler.disconnect(sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionIsCreoRunning(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		boolean running = connHandler.isProeRunning(sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_RUNNING, running);
       	return out;
	}

	private Hashtable<String, Object> actionStartCreo(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String startDir = checkStringParameter(input, PARAM_START_DIR, true);
        String startCommand = checkStringParameter(input, PARAM_START_COMMAND, true);
        int retries = checkIntParameter(input, PARAM_RETRIES, false, -1);
		
		connHandler.startProe(startDir, startCommand, retries, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionStopCreo(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		connHandler.stopProe(sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionKillCreo(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		connHandler.killProe();
		
		return null;
	}

}
