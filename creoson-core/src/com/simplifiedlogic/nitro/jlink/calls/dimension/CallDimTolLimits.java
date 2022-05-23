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
package com.simplifiedlogic.nitro.jlink.calls.dimension;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension.DimTolLimits;
import com.ptc.pfc.pfcDimension.DimTolerance;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDimension.DimTolLimits
 * 
 * @author Adam Andrews
 *
 */
public class CallDimTolLimits extends CallDimTolerance {

	public CallDimTolLimits(DimTolLimits tol) {
		super((DimTolerance)tol);
	}
	
	public double getLowerLimit() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DimTolLimits,GetLowerLimit", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getTol().GetLowerLimit();
	}
	
	public double getUpperLimit() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DimTolLimits,GetUpperLimit", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getTol().GetUpperLimit();
	}
	
	public DimTolLimits getTol() {
		return (DimTolLimits)tol;
	}
}
