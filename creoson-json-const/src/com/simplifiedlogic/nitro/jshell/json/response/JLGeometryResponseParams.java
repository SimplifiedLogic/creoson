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
 * Constants defining the JSON response parameters for the geometry command
 * 
 * @author Adam Andrews
 */
public interface JLGeometryResponseParams {

	// response fields
    public static final String OUTPUT_SURFLIST  	= "surflist";
    public static final String OUTPUT_CONTOURLIST  	= "contourlist";
    public static final String OUTPUT_EDGELIST  	= "edgelist";
    public static final String OUTPUT_SURFACE_ID	= "surface_id";
    public static final String OUTPUT_FEATURE_ID	= "feature_id";
    public static final String OUTPUT_EDGE_ID		= "edge_id";
    public static final String OUTPUT_AREA  		= "area";
    public static final String OUTPUT_LENGTH  		= "length";
    public static final String OUTPUT_MIN_EXTENT	= "min_extent";
    public static final String OUTPUT_MAX_EXTENT	= "max_extent";
    public static final String OUTPUT_TRAVERSAL		= "traversal";
    public static final String OUTPUT_START  		= "start";
    public static final String OUTPUT_END	  		= "end";

    public static final String OUTPUT_XMIN	= "xmin";
	public static final String OUTPUT_XMAX	= "xmax";
	public static final String OUTPUT_YMIN	= "ymin";
	public static final String OUTPUT_YMAX	= "ymax";
	public static final String OUTPUT_ZMIN	= "zmin";
	public static final String OUTPUT_ZMAX	= "zmax";

	// traversal field values
	public static final String TRAVERSE_INTERNAL = "internal";
	public static final String TRAVERSE_EXTERNAL = "external";

}
