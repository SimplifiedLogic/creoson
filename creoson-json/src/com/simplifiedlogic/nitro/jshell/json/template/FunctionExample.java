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
package com.simplifiedlogic.nitro.jshell.json.template;

import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.JShellJsonHandler;
import com.simplifiedlogic.nitro.jshell.json.help.OrderedMap;
import com.simplifiedlogic.nitro.jshell.json.response.ServiceStatus;

/**
 * Help-doc class which represents an example of function usage.
 * 
 * @author Adam Andrews
 *
 */
public class FunctionExample {

	private Map<String, Object> input;
	private Map<String, Object> output;
	private Map<String, Object> status;
	
	/**
	 * Add an input argument to the example
	 * @param name The field name
	 * @param value The field value
	 */
	public void addInput(String name, Object value) {
		if (input==null)
			input = new OrderedMap<String, Object>();
		input.put(name, value);
	}
	
	/**
	 * Add an output result to the example
	 * @param name The field name
	 * @param value The field value
	 */
	public void addOutput(String name, Object value) {
		if (output==null)
			output = new OrderedMap<String, Object>();
		output.put(name, value);
	}
	
	/**
	 * Convert the data into a generic Map object
	 * @param template The function template which owns this spec
	 * @return The converted data object
	 */
	public Map<String, Object> getObject(FunctionTemplate template) {
		Map<String, Object> map = new OrderedMap<String, Object>();
		Map<String, Object> req = new OrderedMap<String, Object>();
		map.put("request", req);
		if (template.getSpec().isHasSessionInput())
			req.put("sessionId", "~sessionId~");
		req.put("command", template.getCommand());
		req.put("function", template.getFunction());
		
		if (input!=null) {
			req.put("data", input);
		}

		Map<String, Object> resp = new OrderedMap<String, Object>();
		map.put("response", resp);
		if (status!=null) {
			resp.put("status", status);
		}
		else if (JShellJsonHandler.alwaysIncludeStatus) {
			Map<String, Object> stat = new OrderedMap<String, Object>();
			stat.put(ServiceStatus.PARAM_ERROR, false);
//			stat.put(ServiceStatus.PARAM_EXPIRED, false);
			resp.put("status", stat);
		}
		if (template.getSpec().isHasSessionOutput())
			resp.put("sessionId", "~sessionId~");
		if (output!=null) {
			resp.put("data", output);
		}
		
		return map;
	}

	/**
	 * Add an error result to the example
	 * @param msg The text of the error
	 */
	public void createError(String msg) {
		status = new OrderedMap<String, Object>();
		status.put(ServiceStatus.PARAM_MESSAGE, msg);
		status.put(ServiceStatus.PARAM_ERROR, true);
//		status.put(ServiceStatus.PARAM_EXPIRED, false);
	}
	
	/**
	 * Get the list of input arguments for the example
	 * @return The list of input arguments
	 */
	public Map<String, Object> getInput() {
		return input;
	}

	/**
	 * Get the list of output results for the example
	 * @return The list of output results
	 */
	public Map<String, Object> getOutput() {
		return output;
	}
}
