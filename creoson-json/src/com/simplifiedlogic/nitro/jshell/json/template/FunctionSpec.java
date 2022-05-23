/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.help.OrderedMap;

/**
 * Help-doc class which represents a complete function specification.
 * 
 * @author Adam Andrews
 *
 */
public class FunctionSpec {

	public static final String TYPE_STRING		= "string";
	public static final String TYPE_INTEGER 	= "integer";
	public static final String TYPE_DOUBLE		= "double";
	public static final String TYPE_BOOL		= "boolean";
	public static final String TYPE_ARRAY		= "array";
	public static final String TYPE_OBJECT		= "object";
	public static final String TYPE_OBJARRAY	= "object_array";
	public static final String TYPE_DEPEND		= "depends on data type";
	
	private String functionDescription;
	private List<FunctionArgument> request;
	private List<FunctionReturn> response;
	private List<String> footnotes;
	private boolean hasSessionInput=true;
	private boolean hasSessionOutput=false;
	
	/**
	 * Add a function argument to the spec
	 * @param arg The argument definition
	 */
	public void addArgument(FunctionArgument arg) {
		if (request==null)
			request = new ArrayList<FunctionArgument>();
		request.add(arg);
	}

	/**
	 * Add a return value to the spec
	 * @param ret The return value definition
	 */
	public void addReturn(FunctionReturn ret) {
		if (response==null)
			response = new ArrayList<FunctionReturn>();
		response.add(ret);
	}

	/**
	 * Add a line of footnote to the spec.  These are additional notes which will appear
	 * in the header of the help doc
	 * @param note Footnote text, intended to be a single footnote
	 */
	public void addFootnote(String note) {
		if (footnotes==null)
			footnotes = new ArrayList<String>();
		footnotes.add(note);
	}

	/**
	 * Convert the data into a generic Map object
	 * @param template The function template which owns this spec
	 * @return The converted data object
	 */
	public Map<String, Object> getObject(FunctionTemplate template) {
		Map<String, Object> map = new OrderedMap<String, Object>();
		map.put("function_description", functionDescription);
		map.put("command", template.getCommand());
		map.put("function", template.getFunction());
		if (footnotes!=null && footnotes.size()>0)
			map.put("notes", footnotes);

		List<Map<String, Object>> req = new ArrayList<Map<String, Object>>();
		map.put("request", req);
		if (request!=null) {
			int len = request.size();
			for (int i=0; i<len; i++) {
				req.add(request.get(i).getObject());
			}
		}

		List<Map<String, Object>> resp = new ArrayList<Map<String, Object>>();
		map.put("response", resp);
		if (response!=null) {
			int len = response.size();
			for (int i=0; i<len; i++) {
				resp.add(response.get(i).getObject());
			}
		}
		return map;
	}

	/**
	 * @return The function description
	 */
	public String getFunctionDescription() {
		return functionDescription;
	}
	/**
	 * @param functionDescription The function description
	 */
	public void setFunctionDescription(String functionDescription) {
		this.functionDescription = functionDescription;
	}
	/**
	 * Get the list of function arguments
	 * @return The list of function arguments
	 */
	public List<FunctionArgument> getRequest() {
		return request;
	}
	/**
	 * Get the list of function return values
	 * @return The list of function return values
	 */
	public List<FunctionReturn> getResponse() {
		return response;
	}
	/**
	 * Get the function footnotes
	 * @return The list of footnotes
	 */
	public List<String> getFootnotes() {
		return footnotes;
	}

	/**
	 * @return Whether the function requires a session ID.  Defaults to true.
	 */
	public boolean isHasSessionInput() {
		return hasSessionInput;
	}

	/**
	 * @param hasSessionInput Whether the function requires a session ID.  Defaults to true.
	 */
	public void setHasSessionInput(boolean hasSessionInput) {
		this.hasSessionInput = hasSessionInput;
	}

	/**
	 * @return Whether the function can return a session ID.  Defaults to false.
	 */
	public boolean isHasSessionOutput() {
		return hasSessionOutput;
	}

	/**
	 * @param hasSessionOutput Whether the function can return a session ID.  Defaults to false.
	 */
	public void setHasSessionOutput(boolean hasSessionOutput) {
		this.hasSessionOutput = hasSessionOutput;
	}
}
