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
package com.simplifiedlogic.nitro.jlink.calls.base;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Outline3D;
import com.ptc.pfc.pfcBase.Point3D;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcBase.Outline3D
 * 
 * @author Adam Andrews
 *
 */
public class CallOutline3D {

	private Outline3D outline;
	
	public CallOutline3D(Outline3D outline) {
		this.outline = outline;
	}
	
	public CallPoint3D get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Outline3D,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		Point3D pt = outline.get(idx);
		if (pt==null)
			return null;
		return new CallPoint3D(pt);
	}
	
	public void set(int idx, CallPoint3D value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Outline3D,set(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		outline.set(idx, value.getPoint());
	}
	
	public static CallOutline3D create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Outline3D,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Outline3D outline = Outline3D.create();
		return new CallOutline3D(outline);
	}

	public Outline3D getOutline() {
		return outline;
	}
}
