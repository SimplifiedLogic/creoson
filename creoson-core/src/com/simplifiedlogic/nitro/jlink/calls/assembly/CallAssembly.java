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
package com.simplifiedlogic.nitro.jlink.calls.assembly;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcSolid.Solid;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcAssembly.Assembly
 * @author Adam Andrews
 *
 */
public class CallAssembly extends CallSolid {

	public CallAssembly(Assembly m) {
		super((Solid)m);
	}
	
	public CallFeature assembleComponent(CallSolid model, CallTransform3D transform) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Assembly,AssembleComponent", 0, NitroConstants.DEBUG_JLINK_KEY);
		Feature feat = getAssembly().AssembleComponent(model.getSolid(), transform!=null ? transform.getTransform() : null);
		if (feat==null)
			return null;
		return CallFeature.createFeature(feat);
	}
	
	public void assembleSkeleton(CallSolid solid) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Assembly,AssembleSkeleton", 0, NitroConstants.DEBUG_JLINK_KEY);
		getAssembly().AssembleSkeleton(solid.getSolid());
	}
	
	public CallSolid getSkeleton() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Assembly,GetSkeleton", 0, NitroConstants.DEBUG_JLINK_KEY);
		Solid solid = getAssembly().GetSkeleton();
		if (solid==null)
			return null;
		return new CallSolid(solid);
	}
	
	public Assembly getAssembly() {
		return (Assembly)m;
	}
}
