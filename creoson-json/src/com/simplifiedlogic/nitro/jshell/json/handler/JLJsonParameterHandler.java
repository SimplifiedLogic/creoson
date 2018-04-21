/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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
import java.util.Map;
import java.util.Vector;

import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.jshell.json.request.JLParameterRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLParameterResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "parameter" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonParameterHandler extends JLJsonCommandHandler implements JLParameterRequestParams, JLParameterResponseParams {

	private IJLParameter paramHandler = null;

	/**
	 * @param paramHandler
	 */
	public JLJsonParameterHandler(IJLParameter paramHandler) {
		this.paramHandler = paramHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_LIST))
			return actionList(sessionId, input);
		else if (function.equals(FUNC_SET))
			return actionSet(sessionId, input);
		else if (function.equals(FUNC_DELETE))
			return actionDelete(sessionId, input);
		else if (function.equals(FUNC_COPY))
			return actionCopy(sessionId, input);
		else if (function.equals(FUNC_EXISTS))
			return actionExists(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionSet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String paramName = checkStringParameter(input, PARAM_NAME, true);
        Object value = checkParameter(input, PARAM_VALUE, false);
        String type = checkStringParameter(input, PARAM_TYPE, false);
        Object designateObj = checkParameter(input, PARAM_DESIGNATE, false);
        int designate=IJLParameter.DESIGNATE_UNKNOWN;
        if (designateObj!=null) {
        	if (checkFlagParameter(input, PARAM_DESIGNATE, false, false))
        		designate = IJLParameter.DESIGNATE_ON;
        	else
        		designate = IJLParameter.DESIGNATE_OFF;
        }
        Boolean encoded = checkFlagParameter(input, PARAM_ENCODED, false, false);
        boolean noCreate = checkFlagParameter(input, PARAM_NO_CREATE, false, false);
        
        paramHandler.set(filename, paramName, value, type, designate, encoded, noCreate, sessionId);
        
        return null;
	}

	private Hashtable<String, Object> actionDelete(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String paramName = checkStringParameter(input, PARAM_NAME, true);
        
        paramHandler.delete(filename, paramName, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionCopy(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String paramname = checkStringParameter(input, PARAM_NAME, true);
        String to_name = checkStringParameter(input, PARAM_TONAME, false);
        String to_model = checkStringParameter(input, PARAM_TOMODEL, false);
        Object designateObj = checkParameter(input, PARAM_DESIGNATE, false);
        int designate=IJLParameter.DESIGNATE_UNKNOWN;
        if (designateObj!=null) {
        	if (checkFlagParameter(input, PARAM_DESIGNATE, false, false))
        		designate = IJLParameter.DESIGNATE_ON;
        	else
        		designate = IJLParameter.DESIGNATE_OFF;
        }

        paramHandler.copy(modelname, paramname, to_model, to_name, designate, sessionId);
        
		return null;
	}
	
	private Hashtable<String, Object> actionList(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String namePattern = checkStringParameter(input, PARAM_NAME, false);
        List<String> paramNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	paramNames = getStringListValue(namesObj);
        }
        boolean encoded = checkFlagParameter(input, PARAM_ENCODED, false, false);
        String valuePattern = checkStringParameter(input, PARAM_VALUE, false);

        List<ParameterData> params = paramHandler.list(filename, namePattern, paramNames, valuePattern, encoded, sessionId);
        
        if (params!=null && params.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outParams = new Vector<Map<String, Object>>();
			out.put(OUTPUT_PARAMLIST, outParams);
			Map<String, Object> outParam = null;
			for (ParameterData param : params) {
				outParam = new Hashtable<String, Object>();
				if (param.getName()!=null)
					outParam.put(PARAM_NAME, param.getName());
				if (param.getValue()!=null)
					outParam.put(PARAM_VALUE, param.getValue());
				if (param.getType()!=null)
					outParam.put(PARAM_TYPE, param.getType());
				outParam.put(PARAM_DESIGNATE, Boolean.valueOf(param.isDesignate()));
				outParam.put(PARAM_ENCODED, Boolean.valueOf(param.isEncoded()));
				if (param.getOwnerName()!=null)
					outParam.put(OUTPUT_OWNER_NAME, param.getOwnerName());
				
				outParams.add(outParam);
			}
			
			return out;
        }
		return null;
	}

	private Hashtable<String, Object> actionExists(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String namePattern = checkStringParameter(input, PARAM_NAME, false);
        List<String> paramNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	paramNames = getStringListValue(namesObj);
        }
        
        boolean exists = paramHandler.exists(filename, namePattern, paramNames, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_EXISTS, exists);
       	return out;
	}

}
