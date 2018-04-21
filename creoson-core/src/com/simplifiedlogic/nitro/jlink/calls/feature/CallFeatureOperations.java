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
package com.simplifiedlogic.nitro.jlink.calls.feature;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.FeatureOperation;
import com.ptc.pfc.pfcFeature.FeatureOperations;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcFeature.FeatureOperations
 * 
 * @author Adam Andrews
 *
 */
public class CallFeatureOperations {

	private FeatureOperations featOps;
	
	public CallFeatureOperations(FeatureOperations featOps) {
		this.featOps = featOps;
	}
	
	public static CallFeatureOperations create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeatureOperations,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        FeatureOperations featOps = FeatureOperations.create();
		if (featOps==null)
			return null;
		return new CallFeatureOperations(featOps);
	}
	
	public void append(CallFeatureOperation featOp) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeatureOperations,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        featOps.append(featOp.getFeatOp());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeatureOperations,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return featOps.getarraysize();
	}
	
	public CallFeatureOperation get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeatureOperations,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        FeatureOperation featOp = featOps.get(idx);
		if (featOp==null)
			return null;
		return new CallFeatureOperation(featOp);
	}

	public void insert(int idx, CallFeatureOperation featOp) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeatureOperations,insert", 0, NitroConstants.DEBUG_JLINK_KEY);
        featOps.insert(idx, featOp.getFeatOp());
	}
	
	public FeatureOperations getFeatureOperations() {
		return featOps;
	}
}
