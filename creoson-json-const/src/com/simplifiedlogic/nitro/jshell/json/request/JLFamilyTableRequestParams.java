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
package com.simplifiedlogic.nitro.jshell.json.request;

/**
 * Constants defining the JSON request parameters for the familytable command
 * 
 * @author Adam Andrews
 */
public interface JLFamilyTableRequestParams {

	// command name
    public static final String COMMAND = "familytable";

    // function names
    public static final String FUNC_EXISTS      	= "exists";
    public static final String FUNC_DELETE      	= "delete";
    public static final String FUNC_DELETE_INST    	= "delete_inst";
    public static final String FUNC_LIST	      	= "list";
    public static final String FUNC_LIST_TREE      	= "list_tree";
    public static final String FUNC_GET_HEADER     	= "get_header";
    public static final String FUNC_GET_ROW      	= "get_row";
    public static final String FUNC_GET_CELL      	= "get_cell";
    public static final String FUNC_SET_CELL      	= "set_cell";
    public static final String FUNC_ADD_INST     	= "add_inst";
    public static final String FUNC_REPLACE     	= "replace";
    public static final String FUNC_CREATE_INST		= "create_inst";
    public static final String FUNC_GET_PARENTS    	= "get_parents";

    // request fields
    public static final String PARAM_MODEL    = "file";
    public static final String PARAM_INSTANCE = "instance";
    public static final String PARAM_COLID    = "colid";
    public static final String PARAM_VALUE    = "value";
    public static final String PARAM_CURMODEL = "cur_model";
    public static final String PARAM_CURINST  = "cur_inst";
    public static final String PARAM_NEWINST  = "new_inst";
    public static final String PARAM_PATH     = "path";
    public static final String PARAM_ERASE    = "erase";

}
