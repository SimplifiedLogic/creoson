/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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
import com.ptc.pfc.pfcAssembly.ComponentPath;
import com.ptc.pfc.pfcAssembly.pfcAssembly;
import com.ptc.pfc.pfcBase.Transform3D;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallIntSeq;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcAssembly.ComponentPath
 * 
 * @author Adam Andrews
 *
 */
public class CallComponentPath {

	private ComponentPath compPath;
	
	public CallComponentPath(ComponentPath compPath) {
		this.compPath = compPath;
	}

	public static CallComponentPath create(CallAssembly masm, CallIntSeq ids) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcAssembly,CreateComponentPath", 0, NitroConstants.DEBUG_JLINK_KEY);
        ComponentPath compPath = pfcAssembly.CreateComponentPath(masm.getAssembly(), ids.getSeq());
		if (compPath==null)
			return null;
		return new CallComponentPath(compPath);
	}
	
	public CallTransform3D getTransform(boolean bottomUp) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentPath,GetTransform", 0, NitroConstants.DEBUG_JLINK_KEY);
		Transform3D trans = compPath.GetTransform(bottomUp);
		if (trans==null)
			return null;
		return new CallTransform3D(trans);
	}
	
	public ComponentPath getCompPath() {
		return compPath;
	}
}
