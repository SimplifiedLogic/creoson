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
package com.simplifiedlogic.nitro.jlink.calls.detail;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailTextLine;
import com.ptc.pfc.pfcDetail.DetailTextLines;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailTextLines
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailTextLines {

	private DetailTextLines lines;
	
	public CallDetailTextLines(DetailTextLines lines) {
		this.lines = lines;
	}
	
	public static CallDetailTextLines create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTextLines,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailTextLines lines = DetailTextLines.create();
		if (lines==null)
			return null;
		return new CallDetailTextLines(lines);
	}
	
	public void append(CallDetailTextLine line) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTextLines,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        lines.append(line.getLine());
	}
	
	public void insert(int idx, CallDetailTextLine line) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTextLines,insert", 0, NitroConstants.DEBUG_JLINK_KEY);
        lines.insert(idx, line.getLine());
	}

	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTextLines,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return lines.getarraysize();
	}
	
	public CallDetailTextLine get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTextLines,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailTextLine line = lines.get(idx);
        return new CallDetailTextLine(line);
	}

	public void clear() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTextLines,clear", 0, NitroConstants.DEBUG_JLINK_KEY);
        lines.clear();
	}

	public DetailTextLines getLines() {
		return lines;
	}
}
