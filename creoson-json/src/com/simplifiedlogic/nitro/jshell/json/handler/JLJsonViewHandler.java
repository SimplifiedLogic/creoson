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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.util.Hashtable;
import java.util.List;

import com.simplifiedlogic.nitro.jlink.intf.IJLView;
import com.simplifiedlogic.nitro.jshell.json.request.JLViewRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLViewResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "view" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonViewHandler extends JLJsonCommandHandler implements JLViewRequestParams, JLViewResponseParams {

	private IJLView viewHandler = null;

	/**
	 * @param viewHandler
	 */
	public JLJsonViewHandler(IJLView viewHandler) {
		this.viewHandler = viewHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_LIST))
			return actionList(sessionId, input);
		else if (function.equals(FUNC_LIST_EXPLODED))
			return actionListExploded(sessionId, input);
		else if (function.equals(FUNC_ACTIVATE))
			return actionActivate(sessionId, input);
		else if (function.equals(FUNC_SAVE))
			return actionSave(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionList(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String viewName = checkStringParameter(input, PARAM_NAME, false);

        List<String> result = viewHandler.list(modelName, viewName, sessionId);
        
        if (result!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
        	out.put(OUTPUT_VIEWLIST, result);
        	return out;
        }
        return null;
	}
	
	private Hashtable<String, Object> actionListExploded(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String viewName = checkStringParameter(input, PARAM_NAME, false);

        List<String> result = viewHandler.listExploded(modelName, viewName, sessionId);
        
        if (result!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
        	out.put(OUTPUT_VIEWLIST, result);
        	return out;
        }
        return null;
	}
	
	private Hashtable<String, Object> actionActivate(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String viewName = checkStringParameter(input, PARAM_NAME, true);

        viewHandler.activate(modelName, viewName, sessionId);
        
        return null;
	}

	private Hashtable<String, Object> actionSave(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String viewName = checkStringParameter(input, PARAM_NAME, true);

        viewHandler.save(modelName, viewName, sessionId);
        
        return null;
	}

}
