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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.response.JLFileResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Parent class for any help-doc generator class for a family of functions.
 * 
 * @author Adam Andrews
 *
 */
public abstract class JLJsonCommandHelp {

	/**
	 * Get a list of function templates for this family of functions.
	 * 
	 * @return A list of function templates
	 */
	public abstract List<FunctionTemplate> getHelp();

	/**
	 * Get the command for this family of functions
	 * @return The command (function family) name
	 */
	public abstract String getCommand();

	/**
	 * Get a list of data objects used in function templates for this family of functions.
	 * 
	 * @return A list of data objects
	 */
	public abstract List<FunctionObject> getHelpObjects();
	
	protected Map<String, Object> makeTransform(
			Map<String, Object> origin,
			Map<String, Object> xvector,
			Map<String, Object> yvector,
			Map<String, Object> zvector,
			double xrot, double yrot, double zrot) {

		Map<String, Object> rec = new OrderedMap<String, Object>();
		rec.put(JLFileResponseParams.OUTPUT_ORIGIN, origin);
		rec.put(JLFileResponseParams.OUTPUT_XAXIS, xvector);
		rec.put(JLFileResponseParams.OUTPUT_YAXIS, yvector);
		rec.put(JLFileResponseParams.OUTPUT_ZAXIS, zvector);
		rec.put(JLFileResponseParams.OUTPUT_X_ROT, xrot);
		rec.put(JLFileResponseParams.OUTPUT_Y_ROT, yrot);
		rec.put(JLFileResponseParams.OUTPUT_Z_ROT, zrot);
		
		return rec;
	}

	protected Map<String, Object> makePoint(double x, double y, double z) {
		Map<String, Object> rec = new OrderedMap<String, Object>();
		rec.put(JLFileResponseParams.OUTPUT_X, Double.valueOf(x));
		rec.put(JLFileResponseParams.OUTPUT_Y, Double.valueOf(y));
		rec.put(JLFileResponseParams.OUTPUT_Z, Double.valueOf(z));
		
		return rec;
	}

}
