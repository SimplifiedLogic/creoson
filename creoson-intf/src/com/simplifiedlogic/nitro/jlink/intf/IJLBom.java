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
package com.simplifiedlogic.nitro.jlink.intf;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.GetPathsOutput;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLBom {

	public static final boolean SKELETON_YES = true;
	public static final boolean SKELETON_NO = false;
	public static final boolean PATHS_YES = true;
	public static final boolean PATHS_NO = false;
	public static final boolean TOPLEVEL_YES = true;
	public static final boolean TOPLEVEL_NO = false;
	public static final boolean INCTRANSFORM_YES = true;
	public static final boolean INCTRANSFORM_NO = false;
	public static final boolean TRANSFORMASTABLE_YES = true;
	public static final boolean TRANSFORMASTABLE_NO = false;
	public static final boolean EXCLUDEINACTIVE_YES = true;
	public static final boolean EXCLUDEINACTIVE_NO = false;
	
	public GetPathsOutput getPaths(String modelname, boolean skeleton,
			boolean paths, boolean toplevel, boolean incTransform, 
	        boolean transformAsTable, 
			boolean excludeInactive, 
			String sessionId) throws JLIException;

	public GetPathsOutput getPaths(String modelname, boolean skeleton,
			boolean paths, boolean toplevel, boolean incTransform, 
	        boolean transformAsTable, 
			boolean excludeInactive, 
			AbstractJLISession sess) throws JLIException;

}