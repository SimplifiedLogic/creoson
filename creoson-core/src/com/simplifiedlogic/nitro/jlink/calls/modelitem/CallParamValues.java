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
package com.simplifiedlogic.nitro.jlink.calls.modelitem;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ParamValue;
import com.ptc.pfc.pfcModelItem.ParamValues;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModelItem.ParamValues
 * 
 * @author Adam Andrews
 *
 */
public class CallParamValues {

	private ParamValues pvals;
	
	public CallParamValues(ParamValues pvals) {
		this.pvals = pvals;
	}
	
	public static CallParamValues create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValues,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        ParamValues pvals = ParamValues.create();
		if (pvals==null)
			return null;
		return new CallParamValues(pvals);
	}
	
	public void append(CallParamValue pval) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValues,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        pvals.append(pval.getValue());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValues,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return pvals.getarraysize();
	}
	
	public CallParamValue get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValues,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        ParamValue pval = pvals.get(idx);
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}

	public ParamValues getValues() {
		return pvals;
	}
}
