/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.view;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcView.View;
import com.ptc.pfc.pfcView.Views;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcView.Views
 * 
 * @author Adam Andrews
 *
 */
public class CallViews {

	private Views views;
	
	public CallViews(Views views) {
		this.views = views;
	}
	
	public static CallViews create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Views,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Views views = Views.create();
		if (views==null)
			return null;
		return new CallViews(views);
	}
	
	public void append(CallView m) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Views,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        views.append(m.getView());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Views,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return views.getarraysize();
	}
	
	public CallView get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Views,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        View view = views.get(idx);
        return new CallView(view);
	}

	public Views getViews() {
		return views;
	}
}
