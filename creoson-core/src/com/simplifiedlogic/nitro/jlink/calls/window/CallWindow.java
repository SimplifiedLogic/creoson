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
package com.simplifiedlogic.nitro.jlink.calls.window;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcWindow.Window;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcWindow.Window
 * 
 * @author Adam Andrews
 *
 */
public class CallWindow {

	private Window win;
	
	public CallWindow(Window win) {
		this.win = win;
	}
	
	public void repaint() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Window,Repaint", 0, NitroConstants.DEBUG_JLINK_KEY);
		win.Repaint();
	}

	public void refresh() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Window,Refresh", 0, NitroConstants.DEBUG_JLINK_KEY);
		win.Refresh();
	}

	public void activate() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Window,Activate", 0, NitroConstants.DEBUG_JLINK_KEY);
		win.Activate();
	}

	public void close() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Window,Close", 0, NitroConstants.DEBUG_JLINK_KEY);
		win.Close();
	}

	public void exportRasterImage(String imageFileName, CallRasterImageExportInstructions instructions) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Window,ExportRasterImage", 0, NitroConstants.DEBUG_JLINK_KEY);
		win.ExportRasterImage(imageFileName, instructions.getInstr());
	}
	
	public CallModel getModel() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Window,GetModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = win.GetModel();
		if (m==null)
			return null;
		return CallModel.create(m);
	}

	public Window getWindow() {
		return win;
	}
}
