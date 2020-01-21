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
package com.simplifiedlogic.nitro.util;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint2D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallVector3D;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Utility class for creating JLPoint objects
 * 
 * @author Adam Andrews
 *
 */
public class JLPointMaker {

	/**
	 * Create a JLPoint from a Creo Point3D object
	 * @param point The Creo point
	 * @return The resulting JShell point
	 * @throws JLIException
	 */
	public static JLPoint create(CallPoint3D point) throws JLIException {
		try {
	    	JLPoint pt = new JLPoint();
	    	
	    	pt.setX(point.get(0));
	    	pt.setY(point.get(1));
	    	pt.setZ(point.get(2));
	    	
	    	return pt;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
	}

	/**
	 * Create a JLPoint from a Creo Poitn2D object
	 * @param point The Creo point
	 * @return The resulting JShell point
	 * @throws JLIException
	 */
	public static JLPoint create(CallPoint2D point) throws JLIException {
		try {
	    	JLPoint pt = new JLPoint();
	    	
	    	pt.setX(point.get(0));
	    	pt.setY(point.get(1));
	    	pt.setZ(0.0);
	    	
	    	return pt;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
	}
	
	/**
	 * Create a Creo Point3D object from a JLPoint
	 * @param point The JShell point
	 * @return The Creo point
	 * @throws jxthrowable
	 */
	public static CallPoint3D create3D(JLPoint point) throws jxthrowable {
		CallPoint3D pt = CallPoint3D.create();
		pt.set(0, point.getX());
		pt.set(1, point.getY());
		pt.set(2, point.getZ());
		return pt;
	}
	
	/**
	 * Convert a Creo Point3D to a string, for debugging purposes
	 * @param point The Creo point
	 * @return The string value of the point
	 * @throws jxthrowable
	 */
	public static String toString(CallPoint3D point) throws jxthrowable {
		return point.get(0) + " " + point.get(1) + " " + point.get(2);
	}
	
	/**
	 * Convert a Creo Vector3D to a string, for debugging purposes
	 * @param vec The Creo vector
	 * @return The string value of the vector
	 * @throws jxthrowable
	 */
	public static String toString(CallVector3D vec) throws jxthrowable {
		return vec.get(0) + " " + vec.get(1) + " " + vec.get(2);
	}
}
