/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
 * Information about a Creo parameter
 * @author Adam Andrews
 *
 */
public class ParameterData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String name;
	Object value;
	String type;
	boolean designate;
	int setDesignate;
	boolean encoded;
	int ownerId;
	String ownerName;
	String ownerType;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object data) {
		if (data==null)
			return false;
		if (!(data instanceof ParameterData))
			return super.equals(data);
		if (name!=null && name.equals(((ParameterData)data).getName()))
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
	 * Compares two parameter-data objects based on parameter name
	 * @param data
	 * @return the value 0 if the param name is equal to the data argument's name; a value less than 0 if this param name is lexicographically less than the data argument's name; and a value greater than 0 if this param name is lexicographically greater than the data argument's name.
	 */
	public int compareTo(ParameterData data) {
		if (this.equals(data))
			return 0;
		if (name==data.getName())
			return 0;
		if (name==null)
			return -1;
//		return name.compareTo(data.getName());
		return ParamNameCompare.compareStrings(name, data.getName());
	}
	
	/**
	 * @return The parameter value as a decoded string, if it's marked encoded
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
	 * @return Parameter name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name Parameter name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Parameter value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value Parameter value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return Data type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type Data type
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Whether value is designated
	 */
	public boolean isDesignate() {
		return designate;
	}
	/**
	 * @param designate Whether value is designated
	 */
	public void setDesignate(boolean designate) {
		this.designate = designate;
	}
	/**
	 * @return Whether value is Base64-encoded
	 */
	public boolean isEncoded() {
		return encoded;
	}
	/**
	 * @param encoded Whether value is Base64-encoded
	 */
	public void setEncoded(boolean encoded) {
		this.encoded = encoded;
	}

	/**
	 * @return Owner Name (model or feature name)
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName Owner Name (model or feature name)
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return Owner type (if owner is a feature)
	 */
	public String getOwnerType() {
		return ownerType;
	}

	/**
	 * @param ownerType Owner type (if owner is a feature)
	 */
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	/**
	 * @return Owner ID (if owner is a feature)
	 */
	public int getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId Owner ID (if owner is a feature)
	 */
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getSetDesignate() {
		return setDesignate;
	}

	public void setSetDesignate(int setDesignate) {
		this.setDesignate = setDesignate;
	}

}
