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
package com.simplifiedlogic.nitro.util;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.base.CallOutline2D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallOutline3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint2D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.data.JLBox;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Utility class for creating JLBox objects
 * 
 * @author Adam Andrews
 *
 */
public class JLBoxMaker {

	/**
	 * Create a JLBox from a Creo Outline3D object
	 * @param outline The Creo outline
	 * @return The resulting JShell box
	 * @throws JLIException
	 */
	public static JLBox create(CallOutline3D outline) throws JLIException {
		try {
	    	CallPoint3D min = outline.get(0);
	    	CallPoint3D max = outline.get(1);
	    	return create(min, max);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
	}

	/**
	 * Create a JLBox from a pair of Creo Point3D objects
	 * @param min The minimum point
	 * @param max The maximum point
	 * @return The resulting JShell box
	 * @throws JLIException
	 */
	public static JLBox create(CallPoint3D min, CallPoint3D max) throws JLIException {
		try {
	    	JLBox box = new JLBox();
	    	
	    	box.setXmin(min.get(0));
	    	box.setYmin(min.get(1));
	    	box.setZmin(min.get(2));
	    	
	    	box.setXmax(max.get(0));
	    	box.setYmax(max.get(1));
	    	box.setZmax(max.get(2));
	    	
	    	return box;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
	}

	/**
	 * Create a JLBox from a Creo Outline2D object.  The min and max Z values are set to 0.0
	 * @param outline The Creo outline
	 * @return The resulting JShell box
	 * @throws JLIException
	 */
	public static JLBox create(CallOutline2D outline) throws JLIException {
		try {
	    	CallPoint2D min = outline.get(0);
	    	CallPoint2D max = outline.get(1);
	    	JLBox box = new JLBox();
	    	
	    	box.setXmin(min.get(0));
	    	box.setYmin(min.get(1));
	    	box.setZmin(0.0);
	    	
	    	box.setXmax(max.get(0));
	    	box.setYmax(max.get(1));
	    	box.setZmax(0.0);
	    	
	    	return box;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
	}
}
