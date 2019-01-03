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
package com.simplifiedlogic.nitro.jlink.calls.view2d;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcView2D.GeneralViewCreateInstructions;
import com.ptc.pfc.pfcView2D.View2DCreateInstructions;
import com.ptc.pfc.pfcView2D.pfcView2D;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcView2D.GeneralViewCreateInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallGeneralViewCreateInstructions extends CallView2DCreateInstructions {

	public CallGeneralViewCreateInstructions(GeneralViewCreateInstructions instr) {
		super((View2DCreateInstructions)instr);
	}
	
	public static CallGeneralViewCreateInstructions create(CallModel viewModel, int sheetNumber, CallPoint3D location, CallTransform3D orientation) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcView2D,GeneralViewCreateInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
        GeneralViewCreateInstructions instr = pfcView2D.GeneralViewCreateInstructions_Create(
        		viewModel!=null ? viewModel.getModel() : null, 
        		sheetNumber,
        		location!=null ? location.getPoint() : null,
        		orientation!=null ? orientation.getTransform() : null);
		if (instr==null)
			return null;
		return new CallGeneralViewCreateInstructions(instr);
	}
	
	public void setScale(double scale) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("GeneralViewCreateInstructions,SetScale", 0, NitroConstants.DEBUG_JLINK_KEY);
        getGeneralInstr().SetScale(scale);
	}

	public void setExploded(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("GeneralViewCreateInstructions,SetExploded", 0, NitroConstants.DEBUG_JLINK_KEY);
        getGeneralInstr().SetExploded(value);
	}

	public GeneralViewCreateInstructions getGeneralInstr() {
		return (GeneralViewCreateInstructions)super.getInstr();
	}
}
