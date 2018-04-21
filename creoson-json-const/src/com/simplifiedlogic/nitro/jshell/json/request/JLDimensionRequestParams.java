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
package com.simplifiedlogic.nitro.jshell.json.request;

/**
 * Constants defining the JSON request parameters for the dimension command
 * 
 * @author Adam Andrews
 */
public interface JLDimensionRequestParams {

	// command name
    public static final String COMMAND = "dimension";

    // function names
    public static final String FUNC_SET			= "set";
    public static final String FUNC_COPY		= "copy";
    public static final String FUNC_LIST		= "list";
    public static final String FUNC_SHOW		= "show";
    public static final String FUNC_USER_SELECT	= "user_select";

    // request fields
    public static final String PARAM_MODEL      = "file";
    public static final String PARAM_NAME       = "name";
    public static final String PARAM_NAMES      = "names";
    public static final String PARAM_TONAME     = "to_name";
    public static final String PARAM_VALUE      = "value";
    public static final String PARAM_TOMODEL    = "to_file";
    public static final String PARAM_ENCODED    = "encoded";
    public static final String PARAM_SHOW       = "show";
    public static final String PARAM_PATH       = "path";
    public static final String PARAM_ASSEMBLY   = "assembly";
    public static final String PARAM_MAX		= "max";
    public static final String PARAM_RELATION_ID	= "relation_id";
    
}
