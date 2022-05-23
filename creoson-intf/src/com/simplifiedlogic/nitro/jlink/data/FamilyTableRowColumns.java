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
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;

/**
 * Information about one row in a family table
 * @author Adam Andrews
 *
 */
public class FamilyTableRowColumns implements Serializable {

	private static final long serialVersionUID = 1L;
	
    public static final int DATA_TYPE_STRING  = 1;
    public static final int DATA_TYPE_DOUBLE  = 2;
    public static final int DATA_TYPE_INTEGER = 3;
    public static final int DATA_TYPE_BOOL    = 4;
    public static final int DATA_TYPE_NOTE    = 5;

    String colid;
	Object value;
	int dataType;
	String columnType;
	
	/**
	 * @return Column ID
	 */
	public String getColid() {
		return colid;
	}
	/**
	 * @param colid Column ID
	 */
	public void setColid(String colid) {
		this.colid = colid;
	}
	/**
	 * @return Cell value
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * @param value Cell value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	/**
	 * @return Data type
	 */
	public int getDataType() {
		return dataType;
	}
	/**
	 * @param dataType Data type
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return Column Type; a string corresponding to the Creo column type
	 */
	public String getColumnType() {
		return columnType;
	}
	/**
	 * @param columnType Column Type; a string corresponding to the Creo column type
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
}
