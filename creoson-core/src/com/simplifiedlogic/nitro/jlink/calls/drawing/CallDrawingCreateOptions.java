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
package com.simplifiedlogic.nitro.jlink.calls.drawing;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDrawing.DrawingCreateOption;
import com.ptc.pfc.pfcDrawing.DrawingCreateOptions;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDrawing.DrawingCreateOptions
 * 
 * @author Adam Andrews
 *
 */
public class CallDrawingCreateOptions {

	private DrawingCreateOptions options;
	
	public CallDrawingCreateOptions(DrawingCreateOptions options) {
		this.options = options;
	}
	
	public static CallDrawingCreateOptions create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DrawingCreateOptions,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        DrawingCreateOptions options = DrawingCreateOptions.create();
		if (options==null)
			return null;
		return new CallDrawingCreateOptions(options);
	}
	
	public void append(DrawingCreateOption opt) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DrawingCreateOptions,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        options.append(opt);
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DrawingCreateOptions,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return options.getarraysize();
	}
	
	public DrawingCreateOption get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DrawingCreateOptions,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        DrawingCreateOption opt = options.get(idx);
        return opt;
	}

	public DrawingCreateOptions getOptions() {
		return options;
	}
}
