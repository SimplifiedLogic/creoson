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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.simplifiedlogic.nitro.jlink.intf.IJLWindchill;
import com.simplifiedlogic.nitro.jshell.json.request.JLWindchillRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLWindchillResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "windchill" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonWindchillHandler extends JLJsonCommandHandler implements JLWindchillRequestParams, JLWindchillResponseParams {

	private IJLWindchill windHandler = null;

	/**
	 * @param windHandler
	 */
	public JLJsonWindchillHandler(IJLWindchill windHandler) {
		this.windHandler = windHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_AUTHORIZE)) return actionAuthorize(sessionId, input);
		else if (function.equals(FUNC_CLEAR_WORKSPACE)) return actionClearWorkspace(sessionId, input);
		else if (function.equals(FUNC_CREATE_WORKSPACE)) return actionCreateWorkspace(sessionId, input);
		else if (function.equals(FUNC_DELETE_WORKSPACE)) return actionDeleteWorkspace(sessionId, input);
		else if (function.equals(FUNC_LIST_WORKSPACES)) return actionListWorkspaces(sessionId, input);
		else if (function.equals(FUNC_SET_SERVER)) return actionSetServer(sessionId, input);
		else if (function.equals(FUNC_SET_WORKSPACE)) return actionSetWorkspace(sessionId, input);
		else if (function.equals(FUNC_GET_WORKSPACE)) return actionGetWorkspace(sessionId, input);
		else if (function.equals(FUNC_SERVER_EXISTS)) return actionServerExists(sessionId, input);
		else if (function.equals(FUNC_WORKSPACE_EXISTS)) return actionWorkspaceExists(sessionId, input);
		else if (function.equals(FUNC_FILE_CHECKED_OUT)) return actionFileCheckedOut(sessionId, input);
		else if (function.equals(FUNC_LIST_WORKSPACE_FILES)) return actionListWorkspaceFiles(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}

	private Hashtable<String, Object> actionAuthorize(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String user = checkStringParameter(input, PARAM_USER, true);
		String password = checkStringParameter(input, PARAM_PASSWORD, true);
		
		windHandler.authorize(user, password, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionSetServer(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String serverUrl = checkStringParameter(input, PARAM_SERVER_URL, true);
		
		windHandler.setServer(serverUrl, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionServerExists(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String serverUrl = checkStringParameter(input, PARAM_SERVER_URL, true);
		
		boolean exists = windHandler.serverExists(serverUrl, sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_EXISTS, exists);
       	return out;
	}

	private Hashtable<String, Object> actionListWorkspaces(String sessionId, @SuppressWarnings("unused") Hashtable<String, Object> input) throws JLIException {
		List<String> ws = windHandler.listWorkspaces(sessionId);
		
		if (ws!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
	        out.put(OUTPUT_WORKSPACES, ws);
	       	return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionSetWorkspace(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String workspace = checkStringParameter(input, PARAM_WORKSPACE, true);
		
		windHandler.setWorkspace(workspace, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionGetWorkspace(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String workspace = windHandler.getWorkspace(sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_WORKSPACE, workspace);
       	return out;
	}

	private Hashtable<String, Object> actionWorkspaceExists(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String workspace = checkStringParameter(input, PARAM_WORKSPACE, true);
		
		boolean exists = windHandler.workspaceExists(workspace, sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_EXISTS, exists);
       	return out;
	}

	private Hashtable<String, Object> actionCreateWorkspace(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String workspace = checkStringParameter(input, PARAM_WORKSPACE, true);
		String context = checkStringParameter(input, PARAM_CONTEXT, true);
		
		windHandler.createWorkspace(workspace, context, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionClearWorkspace(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String workspace = checkStringParameter(input, PARAM_WORKSPACE, true);
        Object namesObj = checkParameter(input, PARAM_FILENAMES, false);
        List<String> filenames = null;
        if (namesObj!=null) {
        	filenames = getStringListValue(namesObj);
        }
        else {
        	// allow for someone leaving the "s" off
        	String filename = checkStringParameter(input, PARAM_FILENAME, false);
        	if (filename!=null) {
        		filenames = new ArrayList<String>();
        		filenames.add(filename);
        	}
        }
		
		windHandler.clearWorkspace(workspace, filenames, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionDeleteWorkspace(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String workspace = checkStringParameter(input, PARAM_WORKSPACE, true);
		
		windHandler.deleteWorkspace(workspace, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionFileCheckedOut(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String workspace = checkStringParameter(input, PARAM_WORKSPACE, true);
		String fileName = checkStringParameter(input, PARAM_FILENAME, true);
		
		boolean checkedOut = windHandler.fileCheckedOut(workspace, fileName, sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_CHECKED_OUT, checkedOut);
       	return out;
	}

	private Hashtable<String, Object> actionListWorkspaceFiles(String sessionId, Hashtable<String, Object> input) throws JLIException {
		String fileName = checkStringParameter(input, PARAM_FILENAME, true);
		String workspace = checkStringParameter(input, PARAM_WORKSPACE, true);
		
		List<String> files = windHandler.listWorkspaceFiles(fileName, workspace, sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_FILELIST, files);
       	return out;
	}

}
