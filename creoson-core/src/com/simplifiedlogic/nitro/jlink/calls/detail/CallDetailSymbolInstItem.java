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
package com.simplifiedlogic.nitro.jlink.calls.detail;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailSymbolInstInstructions;
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailSymbolInstItem
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailSymbolInstItem extends CallDetailItem {

	public CallDetailSymbolInstItem(DetailSymbolInstItem detailSymbolInstItem) {
		super((DetailSymbolInstItem)detailSymbolInstItem);
	}

	public void show() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailSymbolInstItem,Show", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDetailItem().Show();
	}

	// Creo version of this function takes a boolean argument; WF4 version does not
	public CallDetailSymbolInstInstructions getInstructions(boolean giveParametersAsNames) throws jxthrowable {
//	public CallDetailSymbolInstInstructions getInstructions() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailSymbolInstItem,GetInstructions", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailSymbolInstInstructions instr = getDetailItem().GetInstructions(giveParametersAsNames);
//        DetailSymbolInstInstructions instr = getDetailItem().GetInstructions();
		if (instr==null)
			return null;
		return new CallDetailSymbolInstInstructions(instr);
	}
	
	
	public DetailSymbolInstItem getDetailItem() {
		return (DetailSymbolInstItem)item;
	}
}
