/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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
import com.ptc.pfc.pfcDimension.DimTolISODIN;
import com.ptc.pfc.pfcDimension.DimTolLimits;
import com.ptc.pfc.pfcDimension.DimTolPlusMinus;
import com.ptc.pfc.pfcDimension.DimTolSymSuperscript;
import com.ptc.pfc.pfcDimension.DimTolSymmetric;
import com.ptc.pfc.pfcDimension.DimTolerance;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDimension.DimTolerance
 * 
 * @author Adam Andrews
 *
 */
public class CallDimTolerance {

	protected DimTolerance tol;
	
	protected CallDimTolerance(DimTolerance tol) {
		this.tol = tol;
	}
	
	public int getType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DimTolerance,GetType", 0, NitroConstants.DEBUG_JLINK_KEY);
        return getTol().GetType().getValue();
	}
	
	public static CallDimTolerance create(DimTolerance tol) {
		if (tol==null)
			return null;
		if (tol instanceof DimTolPlusMinus) 
			return new CallDimTolPlusMinus((DimTolPlusMinus)tol);
		else if (tol instanceof DimTolSymmetric) 
			return new CallDimTolSymmetric((DimTolSymmetric)tol);
		else if (tol instanceof DimTolSymSuperscript) 
			return new CallDimTolSymSuperscript((DimTolSymSuperscript)tol);
		else if (tol instanceof DimTolLimits) 
			return new CallDimTolLimits((DimTolLimits)tol);
		else if (tol instanceof DimTolISODIN) 
			return new CallDimTolISODIN((DimTolISODIN)tol);
		else
			return new CallDimTolerance(tol);
	}
	
	public DimTolerance getTol() {
		return tol;
	}
}
