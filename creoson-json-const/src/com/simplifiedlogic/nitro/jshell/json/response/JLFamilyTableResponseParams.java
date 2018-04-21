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
package com.simplifiedlogic.nitro.jshell.json.response;

/**
 * Constants defining the JSON response parameters for the familytable command
 * 
 * @author Adam Andrews
 */
public interface JLFamilyTableResponseParams {

	// response fields
    public static final String OUTPUT_INSTANCE  = "instance";
    public static final String OUTPUT_INSTANCES = "instances";
    public static final String OUTPUT_EXISTS    = "exists";
    public static final String OUTPUT_COLUMNS   = "columns";
    public static final String OUTPUT_NAME   	= "name";
    public static final String OUTPUT_CHILDREN	= "children";
    public static final String OUTPUT_PARENTS	= "parents";
    public static final String OUTPUT_TOTAL		= "total";
    public static final String OUTPUT_COLID		= "colid";
    public static final String OUTPUT_VALUE		= "value";
    public static final String OUTPUT_DATATYPE	= "datatype";
    public static final String OUTPUT_COLTYPE	= "coltype";

}
