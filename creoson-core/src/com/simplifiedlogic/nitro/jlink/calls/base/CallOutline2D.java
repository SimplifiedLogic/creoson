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
import com.ptc.pfc.pfcBase.Outline2D;
import com.ptc.pfc.pfcBase.Point2D;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcBase.Outline2D
 * 
 * @author Adam Andrews
 *
 */
public class CallOutline2D {

	private Outline2D outline;
	
	public CallOutline2D(Outline2D outline) {
		this.outline = outline;
	}
	
	public CallPoint2D get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Outline2D,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		Point2D pt = outline.get(idx);
		if (pt==null)
			return null;
		return new CallPoint2D(pt);
	}
	
	public void set(int idx, CallPoint2D value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Outline2D,set(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		outline.set(idx, value.getPoint());
	}
	
	public static CallOutline2D create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Outline2D,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Outline2D outline = Outline2D.create();
		return new CallOutline2D(outline);
	}

	public Outline2D getOutline() {
		return outline;
	}
}
