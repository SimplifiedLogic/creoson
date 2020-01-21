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
package com.simplifiedlogic.nitro.jlink.calls.base;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Vector3D;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcBase.Vector3D
 * 
 * @author Adam Andrews
 *
 */
public class CallVector3D {

	private Vector3D vector;
	
	public CallVector3D(Vector3D vector) {
		this.vector = vector;
	}
	
	public double get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Vector3D,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		return vector.get(idx);
	}
	
	public void set(int idx, double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Vector3D,set(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		vector.set(idx, value);
	}
	
	public static CallVector3D create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Vector3D,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Vector3D vector = Vector3D.create();
		return new CallVector3D(vector);
	}

	public Vector3D getVector() {
		return vector;
	}
}
