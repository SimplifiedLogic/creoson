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
package com.simplifiedlogic.nitro.jlink.calls.dimension2d;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDimension2D.Dimension2D;
import com.ptc.pfc.pfcDimension2D.Dimension2Ds;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDimension2D.Dimension2Ds
 * 
 * @author Adam Andrews
 *
 */
public class CallDimension2Ds {

	private Dimension2Ds dims;
	
	public CallDimension2Ds(Dimension2Ds dims) {
		this.dims = dims;
	}
	
	public static CallDimension2Ds create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dimension2Ds,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Dimension2Ds dims = Dimension2Ds.create();
		if (dims==null)
			return null;
		return new CallDimension2Ds(dims);
	}
	
	public void append(CallDimension2D value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dimension2Ds,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        dims.append(value.getDim());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dimension2Ds,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return dims.getarraysize();
	}
	
	public CallDimension2D get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dimension2Ds,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        Dimension2D dim = dims.get(idx);
        return new CallDimension2D(dim);
	}

	public Dimension2Ds getDims() {
		return dims;
	}
}
