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
package com.simplifiedlogic.nitro.jshell.json.response;

/**
 * Constants defining the JSON response parameters for the dimension command
 * 
 * @author Adam Andrews
 */
public interface JLDimensionResponseParams {

	// response fields
    public static final String OUTPUT_NAME		= "name";
    public static final String OUTPUT_VALUE		= "value";
    public static final String OUTPUT_TYPE		= "type";
    public static final String OUTPUT_ENCODED	= "encoded";
    public static final String OUTPUT_DIMLIST	= "dimlist";
    public static final String OUTPUT_DWG_DIM	= "dwg_dim";
    
    // dimension detail fields
    public static final String OUTPUT_SHEET			= "sheet";
    public static final String OUTPUT_VIEW_NAME		= "view_name";
    public static final String OUTPUT_DIM_TYPE		= "dim_type";
    public static final String OUTPUT_TEXT			= "text";
    public static final String OUTPUT_LOCATION		= "location";
    
    // dimension tolerance fields
    public static final String OUTPUT_TOLERANCE_TYPE		= "tolerance_type";	
    public static final String OUTPUT_TOL_LOWER_LIMIT		= "tol_lower_limit";
    public static final String OUTPUT_TOL_UPPER_LIMIT		= "tol_upper_limit";
    public static final String OUTPUT_TOL_PLUS				= "tol_plus";
    public static final String OUTPUT_TOL_MINUS				= "tol_minus";
    public static final String OUTPUT_TOL_SYMMETRIC_VALUE	= "tol_symmetric_value";
    public static final String OUTPUT_TOL_TABLE_NAME		= "tol_table_name";
    public static final String OUTPUT_TOL_TABLE_COLUMN		= "tol_table_column";
    public static final String OUTPUT_TOL_TABLE_TYPE		= "tol_table_type";
    
}
