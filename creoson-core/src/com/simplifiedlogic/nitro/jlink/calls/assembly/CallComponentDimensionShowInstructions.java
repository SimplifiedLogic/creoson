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
import com.ptc.pfc.pfcAssembly.ComponentDimensionShowInstructions;
import com.ptc.pfc.pfcAssembly.ComponentPath;
import com.ptc.pfc.pfcAssembly.pfcAssembly;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcAssembly.ComponentDimensionShowInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallComponentDimensionShowInstructions {

	private ComponentDimensionShowInstructions inst;
	
	public CallComponentDimensionShowInstructions(ComponentDimensionShowInstructions inst) {
		this.inst = inst;
	}
	
	public static CallComponentDimensionShowInstructions create(CallComponentPath path) throws jxthrowable {
		ComponentPath compPath = null;
		if (path!=null)
			compPath = path.getCompPath();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcAssembly,ComponentDimensionShowInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ComponentDimensionShowInstructions inst = pfcAssembly.ComponentDimensionShowInstructions_Create(compPath);
		if (inst==null)
			return null;
		return new CallComponentDimensionShowInstructions(inst);
	}

	public ComponentDimensionShowInstructions getInst() {
		return inst;
	}
}
