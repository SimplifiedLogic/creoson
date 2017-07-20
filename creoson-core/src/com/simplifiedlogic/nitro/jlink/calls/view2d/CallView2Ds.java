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
import com.ptc.pfc.pfcView2D.View2D;
import com.ptc.pfc.pfcView2D.View2Ds;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcView2D.View2Ds
 * 
 * @author Adam Andrews
 *
 */
public class CallView2Ds {

	private View2Ds views;
	
	public CallView2Ds(View2Ds views) {
		this.views = views;
	}
	
	public static CallView2Ds create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2Ds,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        View2Ds views = View2Ds.create();
		if (views==null)
			return null;
		return new CallView2Ds(views);
	}
	
	public void append(CallView2D m) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2Ds,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        views.append(m.getView());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2Ds,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return views.getarraysize();
	}
	
	public CallView2D get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("View2Ds,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        View2D view = views.get(idx);
        return new CallView2D(view);
	}

	public View2Ds getViews() {
		return views;
	}
}
