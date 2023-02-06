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
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * Information about a Creo dimension
 * 
 * @author Adam Andrews
 *
 */
public class DimData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private Object value;
	private boolean encoded;
	private boolean drawingDimension;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object data) {
		if (data==null)
			return false;
		if (!(data instanceof DimData))
			return super.equals(data);
		if (name!=null && name.equals(((DimData)data).getName()))
			return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		if (name!=null)
			return name.hashCode();
		return super.hashCode();
	}
	
	/**
	 * Compare two dimensions using the dimension name.
	 * @see ParamNameCompare
	 * @param data 
	 * @return the value 0 if the dimension name is equal to the data argument's name; a value less than 0 if this dimensnion name is lexicographically less than the data argument's name; and a value greater than 0 if this dimension name is lexicographically greater than the data argument's name.
	 */
	public int compareTo(DimData data) {
		if (this.equals(data))
			return 0;
		if (name==data.getName())
			return 0;
		if (name==null)
			return -1;
		return ParamNameCompare.compareStrings(name, data.getName());
	}
	
	/**
	 * Return the dimension value as a string
	 * @return The dimension value converted to a string
	 */
	public String getDecodedStringValue() {
		String val;
		if (encoded && value instanceof byte[])
			val = new String((byte[])value, Charset.forName("UTF-8"));
		else
			val = String.valueOf(value);
		return val;
	}
	
	/**
	 * @return Dimension name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name Dimension name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Dimension value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value Dimension value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return Value is Base64-encoded
	 */
	public boolean isEncoded() {
		return encoded;
	}
	/**
	 * @param encoded Value is Base64-encoded
	 */
	public void setEncoded(boolean encoded) {
		this.encoded = encoded;
	}

	public boolean isDrawingDimension() {
		return drawingDimension;
	}

	public void setDrawingDimension(boolean drawingDimension) {
		this.drawingDimension = drawingDimension;
	}
}
