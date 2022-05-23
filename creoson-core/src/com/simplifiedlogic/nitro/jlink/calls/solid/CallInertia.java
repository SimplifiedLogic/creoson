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
package com.simplifiedlogic.nitro.jlink.calls.solid;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSolid.Inertia;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSolid.Inertia
 * 
 * @author Adam Andrews
 *
 */
public class CallInertia {

	private Inertia inertia;
	
	public CallInertia(Inertia inertia) {
		this.inertia = inertia;
	}
	
	public double get(int idx0, int idx1) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Inertia,get(" + idx0 + " " + idx1 + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		return inertia.get(idx0, idx1);
	}
	
	public void set(int idx0, int idx1, double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Inertia,set(" + idx0 + " " + idx1 + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        inertia.set(idx0, idx1, value);
	}
	
	public static CallInertia create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Inertia,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Inertia inertia = Inertia.create();
		return new CallInertia(inertia);
	}

	public Inertia getInertia() {
		return inertia;
	}
}
