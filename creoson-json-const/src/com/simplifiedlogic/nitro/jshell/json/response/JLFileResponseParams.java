/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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
 * Constants defining the JSON response parameters for the file command
 * 
 * @author Adam Andrews
 */
public interface JLFileResponseParams {

	// response fields
    public static final String OUTPUT_MODEL		= "file";
    public static final String OUTPUT_MODELS	= "files";
    public static final String OUTPUT_GENERIC	= "generic";
    public static final String OUTPUT_DIRNAME	= "dirname";
    public static final String OUTPUT_FILES		= "files";
    public static final String OUTPUT_REPS		= "reps";
    public static final String OUTPUT_EXISTS	= "exists";
    public static final String OUTPUT_ERRORS	= "errors";
    public static final String OUTPUT_INSTANCES	= "instances";
    public static final String OUTPUT_VOLUME	= "volume";
    public static final String OUTPUT_MASS		= "mass";
    public static final String OUTPUT_DENSITY	= "density";
    public static final String OUTPUT_SURFACE_AREA	= "surface_area";
    public static final String OUTPUT_RELATIONS	= "relations";
    public static final String OUTPUT_TRANSFORM	= "transform";
    public static final String OUTPUT_FEATUREID	= "featureid";
    public static final String OUTPUT_REVISION	= "revision";
    public static final String OUTPUT_ACTIVE	= "active";
    public static final String OUTPUT_UNITS		= "units";
    public static final String OUTPUT_MATERIAL	= "material";
    public static final String OUTPUT_MATERIALS	= "materials";
    public static final String OUTPUT_CTR_GRAV	= "ctr_grav";
    public static final String OUTPUT_CTR_GRAV_INERTIA_TENSOR	= "ctr_grav_inertia_tensor";
    public static final String OUTPUT_COORD_SYS_INERTIA			= "coord_sys_inertia";
    public static final String OUTPUT_COORD_SYS_INERTIA_TENSOR	= "coord_sys_inertia_tensor";
    public static final String OUTPUT_ACCURACY	= "accuracy";
    public static final String OUTPUT_RELATIVE	= "relative";
    

    // point/matrix/transform params
	public static final String OUTPUT_X			= "x";
	public static final String OUTPUT_Y			= "y";
	public static final String OUTPUT_Z			= "z";
	public static final String OUTPUT_XAXIS		= "x_axis";
	public static final String OUTPUT_YAXIS		= "y_axis";
	public static final String OUTPUT_ZAXIS		= "z_axis";
	public static final String OUTPUT_ORIGIN	= "origin";
	public static final String OUTPUT_X_ROT		= "x_rot";
	public static final String OUTPUT_Y_ROT		= "y_rot";
	public static final String OUTPUT_Z_ROT		= "z_rot";

}
