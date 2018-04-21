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
package com.simplifiedlogic.nitro.jlink.calls.view2d;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcView2D.ProjectionViewCreateInstructions;
import com.ptc.pfc.pfcView2D.View2DCreateInstructions;
import com.ptc.pfc.pfcView2D.pfcView2D;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcView2D.ProjectionViewCreateInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallProjectionViewCreateInstructions extends CallView2DCreateInstructions {

	public CallProjectionViewCreateInstructions(ProjectionViewCreateInstructions instr) {
		super((View2DCreateInstructions)instr);
	}
	
	public static CallProjectionViewCreateInstructions create(CallView2D parentView, CallPoint3D location) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcView2D,ProjectionViewCreateInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
        ProjectionViewCreateInstructions instr = pfcView2D.ProjectionViewCreateInstructions_Create(
        		parentView!=null ? parentView.getView() : null, 
        		location!=null ? location.getPoint() : null);
		if (instr==null)
			return null;
		return new CallProjectionViewCreateInstructions(instr);
	}
	
	public void setExploded(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ProjectionViewCreateInstructions,SetExploded", 0, NitroConstants.DEBUG_JLINK_KEY);
        getProjectionInstr().SetExploded(value);
	}

	public ProjectionViewCreateInstructions getProjectionInstr() {
		return (ProjectionViewCreateInstructions)super.getInstr();
	}
}
