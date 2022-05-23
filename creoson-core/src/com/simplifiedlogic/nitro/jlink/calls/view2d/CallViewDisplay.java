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
package com.simplifiedlogic.nitro.jlink.calls.view2d;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.CableDisplayStyle;
import com.ptc.pfc.pfcBase.DisplayStyle;
import com.ptc.pfc.pfcBase.TangentEdgeDisplayStyle;
import com.ptc.pfc.pfcView2D.ViewDisplay;
import com.ptc.pfc.pfcView2D.pfcView2D;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcView2D.ViewDisplay
 * 
 * @author Adam Andrews
 *
 */
public class CallViewDisplay {

	private ViewDisplay display;

	public CallViewDisplay(ViewDisplay display) {
		this.display = display;
	}

	public static CallViewDisplay create(DisplayStyle style, TangentEdgeDisplayStyle tangentStyle, CableDisplayStyle cableStyle, boolean removeQuiltHiddenLines, boolean showConceptModel, boolean showWeldXSection) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcView2D,ViewDisplay_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ViewDisplay disp = pfcView2D.ViewDisplay_Create(style, tangentStyle, cableStyle, removeQuiltHiddenLines, showConceptModel, showWeldXSection);
		if (disp==null)
			return null;
		return new CallViewDisplay(disp);
	}

	public CableDisplayStyle getCableStyle() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,GetCableStyle", 0, NitroConstants.DEBUG_JLINK_KEY);
		return display.GetCableStyle();
	}

	public void setCableStyle(CableDisplayStyle value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,SetCableStyle", 0, NitroConstants.DEBUG_JLINK_KEY);
		display.SetCableStyle(value);
	}

	public boolean getRemoveQuiltHiddenLines() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,GetRemoveQuiltHiddenLines", 0, NitroConstants.DEBUG_JLINK_KEY);
		return display.GetRemoveQuiltHiddenLines();
	}

	public void setRemoveQuiltHiddenLines(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,SetRemoveQuiltHiddenLines", 0, NitroConstants.DEBUG_JLINK_KEY);
		display.SetRemoveQuiltHiddenLines(value);
	}

	public boolean getShowConceptModel() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,GetShowConceptModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		return display.GetShowConceptModel();
	}

	public void setShowConceptModel(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,SetShowConceptModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		display.SetShowConceptModel(value);
	}

	public boolean getShowWeldXSection() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,GetShowWeldXSection", 0, NitroConstants.DEBUG_JLINK_KEY);
		return display.GetShowWeldXSection();
	}

	public void setShowWeldXSection(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,SetShowWeldXSection", 0, NitroConstants.DEBUG_JLINK_KEY);
		display.SetShowWeldXSection(value);
	}
	
	public DisplayStyle getStyle() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,GetStyle", 0, NitroConstants.DEBUG_JLINK_KEY);
		return display.GetStyle();
	}

	public void setStyle(DisplayStyle value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,SetStyle", 0, NitroConstants.DEBUG_JLINK_KEY);
		display.SetStyle(value);
	}

	public TangentEdgeDisplayStyle getTangentStyle() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,GetTangentStyle", 0, NitroConstants.DEBUG_JLINK_KEY);
		return display.GetTangentStyle();
	}

	public void setTangentStyle(TangentEdgeDisplayStyle value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ViewDisplay,SetTangentStyle", 0, NitroConstants.DEBUG_JLINK_KEY);
		display.SetTangentStyle(value);
	}

	public ViewDisplay getDisplay() {
		return display;
	}
}
