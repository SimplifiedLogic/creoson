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
import com.ptc.pfc.pfcDetail.DetailText;
import com.ptc.pfc.pfcDetail.DetailTexts;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailTexts
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailTexts {

	private DetailTexts texts;
	
	public CallDetailTexts(DetailTexts texts) {
		this.texts = texts;
	}
	
	public static CallDetailTexts create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTexts,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailTexts texts = DetailTexts.create();
		if (texts==null)
			return null;
		return new CallDetailTexts(texts);
	}
	
	public void append(CallDetailText text) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTexts,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        texts.append(text.getTextData());
	}
	
	public void insert(int idx, CallDetailText text) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTexts,insert", 0, NitroConstants.DEBUG_JLINK_KEY);
        texts.insert(idx, text.getTextData());
	}

	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTexts,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return texts.getarraysize();
	}
	
	public CallDetailText get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailTexts,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailText text = texts.get(idx);
        return new CallDetailText(text);
	}

	public DetailTexts getTexts() {
		return texts;
	}
}
