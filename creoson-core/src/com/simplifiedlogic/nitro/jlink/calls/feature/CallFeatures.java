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
package com.simplifiedlogic.nitro.jlink.calls.feature;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.Features;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcFeature.Features
 * 
 * @author Adam Andrews
 *
 */
public class CallFeatures {

	private Features features;
	
	public CallFeatures(Features features) {
		this.features = features;
	}
	
	public static CallFeatures create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Features,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Features features = Features.create();
		if (features==null)
			return null;
		return new CallFeatures(features);
	}
	
	public void append(CallFeature feat) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Features,append", 0, NitroConstants.DEBUG_JLINK_KEY);
		features.append(feat.getFeature());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Features,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return features.getarraysize();
	}
	
	public CallFeature get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Features,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        Feature feat = features.get(idx);
        return CallFeature.createFeature(feat);
	}

	public Features getFeatures() {
		return features;
	}
}
