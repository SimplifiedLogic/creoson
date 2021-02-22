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
package com.simplifiedlogic.nitro.jshell.json.request;

/**
 * Constants defining the JSON request parameters for the feature command
 * 
 * @author Adam Andrews
 */
public interface JLFeatureRequestParams {

	// command name
    public static final String COMMAND = "feature";

    // function names
    public static final String FUNC_DELETE			= "delete";
    public static final String FUNC_LIST			= "list";
    public static final String FUNC_RENAME			= "rename";
    public static final String FUNC_RESUME			= "resume";
    public static final String FUNC_SUPPRESS		= "suppress";
    public static final String FUNC_SET_PARAM		= "set_param";
    public static final String FUNC_DELETE_PARAM	= "delete_param";
    public static final String FUNC_PARAM_EXISTS	= "param_exists";
    public static final String FUNC_LIST_PARAMS		= "list_params";
    public static final String FUNC_LIST_PATTERN_FEATURES	= "list_pattern_features";
    public static final String FUNC_LIST_GROUP_FEATURES		= "list_group_features";
    public static final String FUNC_USER_SELECT_CSYS		= "user_select_csys";
    public static final String FUNC_LIST_SELECTED	= "list_selected";
//    public static final String FUNC_ADD_SELECT		= "add_select";

    // request fields
    public static final String PARAM_MODEL      = "file";
    public static final String PARAM_NAME       = "name";
    public static final String PARAM_NAMES      = "names";
    public static final String PARAM_PATTERN_NAME	= "pattern_name";
    public static final String PARAM_GROUP_NAME	= "group_name";
    public static final String PARAM_TYPE       = "type";
    public static final String PARAM_STATUS     = "status";
    public static final String PARAM_CLIP       = "clip";
    public static final String PARAM_RELDELETE  = "relation_delete";
    public static final String PARAM_RELCOMMENT = "relation_comment";
    public static final String PARAM_WCHILDREN  = "with_children";
    public static final String PARAM_PATHS      = "paths";
    public static final String PARAM_NO_DATUM   = "no_datum";
    public static final String PARAM_INC_UNNAMED	= "inc_unnamed";
    public static final String PARAM_NO_COMP	= "no_comp";
    public static final String PARAM_ID			= "feat_id";
    public static final String PARAM_NEWNAME	= "new_name";
    public static final String PARAM_PARAM		= "param";
    public static final String PARAM_PARAMS		= "params";
    public static final String PARAM_MAX		= "max";
//    public static final String PARAM_CLEAR		= "clear";
    public static final String PARAM_PATH       = "path";

}
