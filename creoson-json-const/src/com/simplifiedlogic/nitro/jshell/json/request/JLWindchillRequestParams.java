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
 * Constants defining the JSON request parameters for the windchill command
 * 
 * @author Adam Andrews
 */
public interface JLWindchillRequestParams {

	// command name
    public static final String COMMAND = "windchill";

    // function names
    public static final String FUNC_AUTHORIZE			= "authorize";
    public static final String FUNC_SET_SERVER			= "set_server";
    public static final String FUNC_SERVER_EXISTS		= "server_exists";
    public static final String FUNC_LIST_WORKSPACES		= "list_workspaces";
    public static final String FUNC_SET_WORKSPACE		= "set_workspace";
    public static final String FUNC_GET_WORKSPACE		= "get_workspace";
    public static final String FUNC_WORKSPACE_EXISTS	= "workspace_exists";
    public static final String FUNC_CREATE_WORKSPACE	= "create_workspace";
    public static final String FUNC_CLEAR_WORKSPACE		= "clear_workspace";
    public static final String FUNC_DELETE_WORKSPACE	= "delete_workspace";
    public static final String FUNC_FILE_CHECKED_OUT	= "file_checked_out";
    public static final String FUNC_LIST_WORKSPACE_FILES	= "list_workspace_files";
    
    // request fields
    public static final String PARAM_USER		= "user";
    public static final String PARAM_PASSWORD	= "password";
    public static final String PARAM_SERVER_URL	= "server_url";
    public static final String PARAM_WORKSPACE	= "workspace";
    public static final String PARAM_CONTEXT	= "context";
    public static final String PARAM_FILENAME	= "filename";

}
