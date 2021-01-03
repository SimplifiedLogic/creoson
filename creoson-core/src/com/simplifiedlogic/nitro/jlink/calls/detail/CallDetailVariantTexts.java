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
import com.ptc.pfc.pfcDetail.DetailVariantText;
import com.ptc.pfc.pfcDetail.DetailVariantTexts;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailTexts
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailVariantTexts {

	private DetailVariantTexts texts;
	
	public CallDetailVariantTexts(DetailVariantTexts texts) {
		this.texts = texts;
	}
	
	public static CallDetailVariantTexts create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailVariantTexts,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailVariantTexts texts = DetailVariantTexts.create();
		if (texts==null)
			return null;
		return new CallDetailVariantTexts(texts);
	}
	
	public void append(CallDetailVariantText text) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailVariantTexts,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        texts.append(text.getTextData());
	}
	
	public void insert(int idx, CallDetailVariantText text) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailVariantTexts,insert", 0, NitroConstants.DEBUG_JLINK_KEY);
        texts.insert(idx, text.getTextData());
	}

	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailVariantTexts,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return texts.getarraysize();
	}
	
	public CallDetailVariantText get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailVariantTexts,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailVariantText text = texts.get(idx);
        return new CallDetailVariantText(text);
	}

	public DetailVariantTexts getTexts() {
		return texts;
	}
}
