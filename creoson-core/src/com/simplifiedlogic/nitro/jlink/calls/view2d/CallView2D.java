/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
import com.ptc.pfc.pfcBase.Outline3D;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcView2D.View2D;
import com.ptc.pfc.pfcView2D.ViewDisplay;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.base.CallOutline3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallVector3D;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcView2D.View2D
 * 
 * @author Adam Andrews
 *
 */
public class CallView2D {

	private View2D view;
	
	public CallView2D(View2D view) {
		this.view = view;
	}

	public String getName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,GetName", 0, NitroConstants.DEBUG_JLINK_KEY);
        return view.GetName();
	}

	// not in WF4
//	public void setName(String value) throws jxthrowable {
//		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,SetName", 0, NitroConstants.DEBUG_JLINK_KEY);
//		view.SetName(value);
//	}
	
	public CallOutline3D getOutline() throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,GetOutline", 0, NitroConstants.DEBUG_JLINK_KEY);
		Outline3D outline = view.GetOutline();
		if (outline==null)
			return null;
		return new CallOutline3D(outline);
	}

	public void translate(CallVector3D byvector) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,Translate", 0, NitroConstants.DEBUG_JLINK_KEY);
		if (byvector!=null)
			view.Translate(byvector.getVector());
	}

	public void delete(boolean deleteChildren) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,Delete", 0, NitroConstants.DEBUG_JLINK_KEY);
		view.Delete(deleteChildren);
	}

	public CallModel getModel() throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,GetModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = view.GetModel();
		if (m==null)
			return null;
		return new CallModel(m);
	}

	public int getSheetNumber() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,GetSheetNumber", 0, NitroConstants.DEBUG_JLINK_KEY);
        return view.GetSheetNumber();
	}

	public double getScale() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,GetScale", 0, NitroConstants.DEBUG_JLINK_KEY);
        return view.GetScale();
	}

	public void setScale(double scale) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,SetScale", 0, NitroConstants.DEBUG_JLINK_KEY);
        view.SetScale(scale);
	}

	public boolean getIsScaleUserdefined() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,GetIsScaleUserdefined", 0, NitroConstants.DEBUG_JLINK_KEY);
		return view.GetIsScaleUserdefined();
	}
	
	public CallViewDisplay getDisplay() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,GetDisplay", 0, NitroConstants.DEBUG_JLINK_KEY);
		ViewDisplay disp = view.GetDisplay();
		if (disp==null)
			return null;
		return new CallViewDisplay(disp);
	}

	public void setDisplay(CallViewDisplay value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2D,SetDisplay", 0, NitroConstants.DEBUG_JLINK_KEY);
		view.SetDisplay(value.getDisplay());
	}

	public View2D getView() {
		return view;
	}
}
