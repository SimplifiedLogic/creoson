/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
 * Constants defining the JSON request parameters for the bom command
 * 
 * @author Adam Andrews
 */
public interface JLBomRequestParams {

	// command name
    public static final String COMMAND = "bom";

    // function names
    public static final String FUNC_GET_PATHS = "get_paths";
    
    // request fields
    public static final String PARAM_MODEL				= "file";
    public static final String PARAM_SKELETON			= "skeletons";
    public static final String PARAM_TOPLEVEL			= "top_level";
    public static final String PARAM_PATHS				= "paths";
    public static final String PARAM_TRANSFORMS			= "get_transforms";
    public static final String PARAM_EXCLUDE_INACTIVE	= "exclude_inactive";

}
