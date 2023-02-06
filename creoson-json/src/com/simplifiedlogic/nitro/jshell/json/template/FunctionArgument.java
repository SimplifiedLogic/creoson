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
package com.simplifiedlogic.nitro.jshell.json.template;

import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.help.OrderedMap;

/**
 * Help-doc class which represents a single JSON argument to a CREOSON function.
 * 
 * @author Adam Andrews
 *
 */
public class FunctionArgument {

	private String name;
	private String description;
	private String type;
	private String arrayType;
	private boolean required;
	private boolean wildcards;
	private String defaultValue;
	private String[] validValues;
	
	/**
	 * Standard constructor.
	 * 
	 * @param name Field name
	 * @param type Data type
	 */
	public FunctionArgument(String name, String type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Constructor used to declare arguments of a more complex data type, such as:<p>
	 * - Shared objects (type=TYPE_OBJECT, arrayType=shared object name)<br>
	 * - Arrays of primitives (type=TYPE_ARRAY, arrayType=primitive data type)<br>
	 * - Arrays of shared objects (type=TYPE_OBJARRAY, arrayType=shared object name)
	 * 
	 * @param name Field Type
	 * @param type Data type - should be one of TYPE_OBJECT, TYPE_ARRAY, or TYPE_OBJARRAY
	 * @param arrayType Data sub-type
	 */
	public FunctionArgument(String name, String type, String arrayType) {
		this(name, type);
		this.arrayType = arrayType;
	}

	/**
	 * Convert the data into a generic Map object
	 * @return The converted data object
	 */
	public Map<String, Object> getObject() {
		Map<String, Object> map = new OrderedMap<String, Object>();
		if (name!=null)
			map.put("name", name);
		if (type!=null) {
			if (FunctionSpec.TYPE_OBJECT.equals(type) || FunctionSpec.TYPE_ARRAY.equals(type) || FunctionSpec.TYPE_OBJARRAY.equals(type)) {
				if (arrayType!=null)
					map.put("type", type+":"+arrayType);
				else
					map.put("type", type);
			}
			else
				map.put("type", type);
		}
		if (description!=null)
			map.put("description", description);
		if (required)
			map.put("required", required);
		if (defaultValue!=null)
			map.put("default", defaultValue);
		if (wildcards)
			map.put("wildcards_allowed", wildcards);
		if (validValues!=null && validValues.length>0) {
			StringBuffer buf = new StringBuffer();
			int len = validValues.length;
			for (int i=0; i<len; i++) {
				if (i>0) buf.append(", ");
				buf.append(validValues[i]);
			}
			map.put("valid_values", buf.toString());
		}
		
		return map;
	}

	/**
	 * @return The field name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The field name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return The field's description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The field's description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return The field's data type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The field's data type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Whether the field is required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required Whether the field is required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * @return The field's default value.  This may be a description of default behavior rather than a specific value. 
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @param defaultValue The field's default value.  This may be a description of default behavior rather than a specific value.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @return A list of valid values for the field
	 */
	public String[] getValidValues() {
		return validValues;
	}
	/**
	 * @param validValues A list of valid values for the field
	 */
	public void setValidValues(String[] validValues) {
		this.validValues = validValues;
	}

	/**
	 * @return The data sub-type if type is TYPE_OBJECT, TYPE_ARRAY, or TYPE_OBJARRAY
	 */
	public String getArrayType() {
		return arrayType;
	}

	/**
	 * @param arrayType The data sub-type if type is TYPE_OBJECT, TYPE_ARRAY, or TYPE_OBJARRAY
	 */
	public void setArrayType(String arrayType) {
		this.arrayType = arrayType;
	}

	/**
	 * @return Whether the field allows wildcards
	 */
	public boolean isWildcards() {
		return wildcards;
	}

	/**
	 * @param wildcards Whether the field allows wildcards
	 */
	public void setWildcards(boolean wildcards) {
		this.wildcards = wildcards;
	}
}
