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
package com.simplifiedlogic.nitro.jshell.json.template;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.help.OrderedMap;

/**
 * The top-level Help-doc class which represents a complete function template.  This includes
 * both the function spec and a series of examples. 
 * 
 * @author Adam Andrews
 *
 */
public class FunctionTemplate {

	private String command;
	private String function;
	private FunctionSpec spec;
	private List<FunctionExample> examples;
	
	/**
	 * Standard constructor
	 * 
	 * @param command The command (function family)
	 * @param function The function name
	 */
	public FunctionTemplate(String command, String function) {
		this.command = command;
		this.function = function;
		spec = new FunctionSpec();
	}

	/**
	 * Add an example to the function
	 * @param ex The example definition
	 */
	public void addExample(FunctionExample ex) {
		if (examples==null)
			examples = new ArrayList<FunctionExample>();
		examples.add(ex);
	}
	
	/**
	 * Convert the data into a generic Map object
	 * @return The converted data object
	 */
	public Map<String, Object> getObject() {
		Map<String, Object> map = new OrderedMap<String, Object>();
		Map<String, Object> smap = spec.getObject(this);
		map.put("spec", smap);
		
		if (examples!=null) {
			List<Map<String, Object>> elist = new ArrayList<Map<String, Object>>();
			map.put("examples", elist);
			for (FunctionExample ex : examples) {
				elist.add(ex.getObject(this));
			}
		}
		
		return map;
	}
	
	/**
	 * @return The function spec
	 */
	public FunctionSpec getSpec() {
		return spec;
	}
	/**
	 * @param spec The function spec
	 */
	public void setSpec(FunctionSpec spec) {
		this.spec = spec;
	}
	/**
	 * @return The list of examples for the function
	 */
	public List<FunctionExample> getExamples() {
		return examples;
	}

	/**
	 * @return The command name
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return The function name
	 */
	public String getFunction() {
		return function;
	}

}
