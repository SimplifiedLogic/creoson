/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.componentfeat;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.DatumSide;
import com.ptc.pfc.pfcComponentFeat.ComponentConstraint;
import com.ptc.pfc.pfcComponentFeat.ComponentConstraintType;
import com.ptc.pfc.pfcComponentFeat.pfcComponentFeat;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcComponentFeat.ComponentConstraint
 * 
 * @author Adam Andrews
 *
 */
public class CallComponentConstraint {

	private ComponentConstraint ctrt;
	
	public CallComponentConstraint(ComponentConstraint ctrt) {
		this.ctrt = ctrt;
	}
	
	public void setAssemblyReference(CallSelection sel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentConstraint,SetAssemblyReference", 0, NitroConstants.DEBUG_JLINK_KEY);
		ctrt.SetAssemblyReference(sel!=null ? sel.getSel() : null);
	}
	
	public void setAssemblyDatumSide(DatumSide value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentConstraint,SetAssemblyDatumSide", 0, NitroConstants.DEBUG_JLINK_KEY);
		ctrt.SetAssemblyDatumSide(value);
	}
	
	public void setComponentReference(CallSelection sel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentConstraint,SetComponentReference", 0, NitroConstants.DEBUG_JLINK_KEY);
		ctrt.SetComponentReference(sel!=null ? sel.getSel() : null);
	}
	
	public void setComponentDatumSide(DatumSide value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentConstraint,SetComponentDatumSide", 0, NitroConstants.DEBUG_JLINK_KEY);
		ctrt.SetComponentDatumSide(value);
	}
	
	public void setOffset(Double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentConstraint,SetOffset", 0, NitroConstants.DEBUG_JLINK_KEY);
		ctrt.SetOffset(value);
	}
	
	public static CallComponentConstraint create(ComponentConstraintType type) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcComponentFeat,ComponentConstraint_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ComponentConstraint ctrt = pfcComponentFeat.ComponentConstraint_Create(type);
		if (ctrt==null)
			return null;
		return new CallComponentConstraint(ctrt);
	}

	public ComponentConstraint getConstraint() {
		return ctrt;
	}
}
