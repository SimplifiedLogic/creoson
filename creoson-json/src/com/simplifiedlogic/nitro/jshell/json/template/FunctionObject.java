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
 * Help-doc class which represents the definition of a shared data object.
 * 
 * @author Adam Andrews
 *
 */
public class FunctionObject {

	private String objectName;
	private String description;
	private List<FunctionArgument> data;
	private List<String> notes;
	
	/**
	 * Standard constructor
	 * @param name The name of the data object
	 */
	public FunctionObject(String name) {
		this.objectName = name;
	}

	/**
	 * Convert the data into a generic Map object
	 * @return The converted data object
	 */
	public Map<String, Object> getObject() {
		Map<String, Object> map = new OrderedMap<String, Object>();
		map.put("object_name", objectName);
		if (description!=null)
			map.put("description", description);
		if (notes!=null && notes.size()>0)
			map.put("notes", notes);

		List<Map<String, Object>> req = new ArrayList<Map<String, Object>>();
		map.put("data", req);
		if (data!=null) {
			int len = data.size();
			for (int i=0; i<len; i++) {
				req.add(data.get(i).getObject());
			}
		}

		return map;
	}

	/**
	 * Add a field to the data object
	 * @param arg The field definition
	 */
	public void add(FunctionArgument arg) {
		if (data==null)
			data = new ArrayList<FunctionArgument>();
		data.add(arg);
	}

	/**
	 * Add a line of footnote to the data object.  These are additional notes 
	 * which will appear in the header of the help doc
	 * @param note Footnote text, intended to be a single footnote
	 */
	public void addFootnote(String note) {
		if (notes==null)
			notes = new ArrayList<String>();
		notes.add(note);
	}

	/**
	 * @return The data object name
	 */
	public String getObjectName() {
		return objectName;
	}
	/**
	 * @param objectName The data object name
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	/**
	 * Get the list of fields for the object
	 * @return The list of fields
	 */
	public List<FunctionArgument> getData() {
		return data;
	}

	/**
	 * @return The object's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The object's description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getNotes() {
		return notes;
	}
}
