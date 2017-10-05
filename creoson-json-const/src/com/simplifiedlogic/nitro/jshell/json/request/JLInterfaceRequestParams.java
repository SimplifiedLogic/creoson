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
 * Constants defining the JSON request parameters for the interface command
 * 
 * @author Adam Andrews
 */
public interface JLInterfaceRequestParams {

	// command name
    public static final String COMMAND = "interface";

    // function names
    public static final String FUNC_EXPORT_IMAGE	= "export_image";
    public static final String FUNC_EXPORT_FILE		= "export_file";
    public static final String FUNC_EXPORT_PDF		= "export_pdf";
    public static final String FUNC_EXPORT_3DPDF	= "export_3dpdf";
    public static final String FUNC_EXPORT_PROGRAM	= "export_program";
    public static final String FUNC_IMPORT_PROGRAM	= "import_program";
    public static final String FUNC_PLOT			= "plot";
    public static final String FUNC_MAPKEY			= "mapkey";

    // request fields
    public static final String PARAM_TYPE		= "type";
    public static final String PARAM_MODEL		= "file";
    public static final String PARAM_FILENAME	= "filename";
    public static final String PARAM_DIRNAME	= "dirname";
    public static final String PARAM_SCRIPT		= "script";
    // request fields for image parameters
    public static final String PARAM_WIDTH    = "width";
    public static final String PARAM_HEIGHT   = "height";
    public static final String PARAM_DPI      = "dpi";
    public static final String PARAM_DEPTH    = "depth";
    public static final String PARAM_DRIVER   = "driver";
    public static final String PARAM_GEOM_FLAGS	= "geom_flags";

    // export file types
    public static final String TYPE_PROPROGRAM = "Pro/PROGRAM";
    public static final String TYPE_BMP        = "BMP";
    public static final String TYPE_EPS        = "EPS";
    public static final String TYPE_JPEG       = "JPEG";
    public static final String TYPE_TIFF       = "TIFF";
    public static final String TYPE_POSTSCRIPT = "POSTSCRIPT";
    public static final String TYPE_STEP       = "STEP";
    public static final String TYPE_IGES       = "IGES";
    public static final String TYPE_VRML       = "VRML";
    public static final String TYPE_CATIA      = "CATIA";
    public static final String TYPE_PV         = "PV";
    public static final String TYPE_SAT        = "SAT";
    public static final String TYPE_DXF        = "DXF";
    
}
