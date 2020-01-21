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
 * Constants defining the JSON request parameters for the creo command
 * 
 * @author Adam Andrews
 */
public interface JLCreoRequestParams {

	// command name
    public static final String COMMAND = "creo";

    // function names
	public static final String FUNC_CD 				= "cd";
	public static final String FUNC_MKDIR 			= "mkdir";
	public static final String FUNC_RMDIR 			= "rmdir";
	public static final String FUNC_PWD 			= "pwd";
	public static final String FUNC_LIST_DIRS 		= "list_dirs";
	public static final String FUNC_LIST_FILES 		= "list_files";
	public static final String FUNC_DELETE_FILES 	= "delete_files";
	public static final String FUNC_SET_CONFIG		= "set_config";
	public static final String FUNC_GET_CONFIG		= "get_config";
	public static final String FUNC_SET_STD_COLOR	= "set_std_color";
	public static final String FUNC_GET_STD_COLOR	= "get_std_color";
	
	// request fields
    public static final String PARAM_DIRNAME	= "dirname";
    public static final String PARAM_FILENAME	= "filename";
    public static final String PARAM_FILENAMES	= "filenames";
    public static final String PARAM_NAME		= "name";
    public static final String PARAM_VALUE		= "value";
    public static final String PARAM_COLOR_TYPE	= "color_type";
    public static final String PARAM_RED		= "red";
    public static final String PARAM_GREEN		= "green";
    public static final String PARAM_BLUE		= "blue";
    public static final String PARAM_IGNORE_ERRORS	= "ignore_errors";

    // standard color values
	public static final String STD_COLOR_LETTER 			= "letter";
	public static final String STD_COLOR_HIGHLIGHT 			= "highlight";
	public static final String STD_COLOR_DRAWING 			= "drawing";
	public static final String STD_COLOR_BACKGROUND 		= "background";
	public static final String STD_COLOR_HALF_TONE 			= "half_tone";
	public static final String STD_COLOR_EDGE_HIGHLIGHT 	= "edge_highlight";
	public static final String STD_COLOR_DIMMED 			= "dimmed";
	public static final String STD_COLOR_ERROR 				= "error";
	public static final String STD_COLOR_WARNING 			= "warning";
	public static final String STD_COLOR_SHEETMETAL 		= "sheetmetal";
	public static final String STD_COLOR_CURVE 				= "curve";
	public static final String STD_COLOR_PRESEL_HIGHLIGHT 	= "presel_highlight";
	public static final String STD_COLOR_SELECTED 			= "selected";
	public static final String STD_COLOR_SECONDARY_SELECTED = "secondary_selected";
	public static final String STD_COLOR_PREVIEW 			= "preview";
	public static final String STD_COLOR_SECONDARY_PREVIEW 	= "secondary_preview";
	public static final String STD_COLOR_DATUM 				= "datum";
	public static final String STD_COLOR_QUILT 				= "quilt";
    
}
