/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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
import com.ptc.pfc.pfcDetail.DetailTexts;
import com.ptc.pfc.pfcDetail.pfcDetail;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailTextLine
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailTextLine {

	protected DetailTextLine line;
	
	public CallDetailTextLine(DetailTextLine line) {
		this.line = line;;
	}
	
	public static CallDetailTextLine create(CallDetailTexts inTexts) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcDetail,DetailTextLine_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailTextLine line = pfcDetail.DetailTextLine_Create(inTexts.getTexts());
		if (line==null)
			return null;
		return new CallDetailTextLine(line);
	}

	public CallDetailTexts getTexts() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTextLine,GetTexts", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailTexts texts = getLine().GetTexts();
        if (texts==null)
        	return null;
		return new CallDetailTexts(texts);
	}
	
	public DetailTextLine getLine() {
		return line;
	}
}
