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
import com.ptc.pfc.pfcDetail.DetailCreateInstructions;
import com.ptc.pfc.pfcDetail.DetailLeaders;
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;
import com.ptc.pfc.pfcDetail.DetailSymbolInstInstructions;
import com.ptc.pfc.pfcDetail.pfcDetail;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailSymbolInstInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailSymbolInstInstructions extends CallDetailCreateInstructions {

	public CallDetailSymbolInstInstructions(DetailSymbolInstInstructions inst) {
		super((DetailCreateInstructions)inst);
	}
	
	public static CallDetailSymbolInstInstructions create(CallDetailSymbolDefItem inSymbolDef) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcDetail,DetailSymbolInstInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailSymbolInstInstructions instr = pfcDetail.DetailSymbolInstInstructions_Create(inSymbolDef.getDetailItem());
		if (instr==null)
			return null;
		return new CallDetailSymbolInstInstructions(instr);
	}
	
	public CallDetailSymbolDefItem getSymbolDef() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailSymbolInstInstructions,GetSymbolDef", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailSymbolDefItem def = getInst().GetSymbolDef();
        if (def==null)
        	return null;
        return new CallDetailSymbolDefItem(def);
	}

	public void setInstAttachment(CallDetailLeaders value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailSymbolInstInstructions,SetInstAttachment", 0, NitroConstants.DEBUG_JLINK_KEY);
		getInst().SetInstAttachment(value!=null ? value.getLeaders() : null);
	}

	public void setTextValues(CallDetailVariantTexts value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailSymbolInstInstructions,SetTextValues", 0, NitroConstants.DEBUG_JLINK_KEY);
		getInst().SetTextValues(value!=null ? value.getTexts() : null);
	}
	
	public CallDetailLeaders getInstAttachment() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailSymbolInstInstructions,GetInstAttachment", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailLeaders leaders = getInst().GetInstAttachment();
		if (leaders==null)
			return null;
		return new CallDetailLeaders(leaders);
	}

	public DetailSymbolInstInstructions getInst() {
		return ((DetailSymbolInstInstructions)inst);
	}
}
