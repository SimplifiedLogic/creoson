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
 * Constants defining the JSON response parameters for the drawing command
 * 
 * @author Adam Andrews
 */
public interface JLDrawingResponseParams {

	// response fields
	public static final String OUTPUT_DRAWING	= "drawing";
	public static final String OUTPUT_MODEL		= "file";
	public static final String OUTPUT_MODELS	= "files";
    public static final String OUTPUT_SHEET		= "sheet";
    public static final String OUTPUT_NUM_SHEETS	= "num_sheets";
    public static final String OUTPUT_SCALE		= "scale";
    public static final String OUTPUT_SIZE		= "size";
	public static final String OUTPUT_VIEWS		= "views";
	public static final String OUTPUT_FAILED_VIEWS	= "failed_views";
	public static final String OUTPUT_SUCCESS_VIEWS	= "success_views";

    public static final String OUTPUT_XMIN	= "xmin";
	public static final String OUTPUT_XMAX	= "xmax";
	public static final String OUTPUT_YMIN	= "ymin";
	public static final String OUTPUT_YMAX	= "ymax";
}
