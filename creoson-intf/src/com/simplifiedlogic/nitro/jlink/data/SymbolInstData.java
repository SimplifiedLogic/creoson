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
package com.simplifiedlogic.nitro.jlink.data;

/**
 * Data about a drawing symbol instance
 * 
 * @author Adam Andrews
 *
 */
public class SymbolInstData {

	public static final String ATTACH_TYPE_FREE 		= "free";
	public static final String ATTACH_TYPE_PARAMETRIC 	= "parametric";
	public static final String ATTACH_TYPE_UNKNOWN 		= "unknown";
	public static final String ATTACH_TYPE_OFFSET 		= "offset";

	private int id;
	private String name;
	private int sheet;
	private JLPoint location;
	private String attachType;

	/**
	 * @return The symbol ID
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The symbol ID
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return The symbol name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The symbol name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return The symbol's location
	 */
	public JLPoint getLocation() {
		return location;
	}
	/**
	 * @param location The symbol's location
	 */
	public void setLocation(JLPoint location) {
		this.location = location;
	}
	/**
	 * @return The sheet number containing the symbol
	 */
	public int getSheet() {
		return sheet;
	}
	/**
	 * @param sheet The sheet number containing the symbol
	 */
	public void setSheet(int sheet) {
		this.sheet = sheet;
	}
	/**
	 * @return The symbol instance's attachment type
	 */
	public String getAttachType() {
		return attachType;
	}
	/**
	 * @param attachType The symbol instance's attachment type
	 */
	public void setAttachType(String attachType) {
		this.attachType = attachType;
	}

}
