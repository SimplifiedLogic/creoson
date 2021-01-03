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
package com.simplifiedlogic.nitro.jlink.calls.simprep;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSimpRep.SimpRepItem;
import com.ptc.pfc.pfcSimpRep.SimpRepItems;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSimpRep.SimpRepItems
 * 
 * @author Adam Andrews
 *
 */
public class CallSimpRepItems {

	private SimpRepItems items;
	
	public CallSimpRepItems(SimpRepItems items) {
		this.items = items;
	}
	
	public static CallSimpRepItems create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepItems,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        SimpRepItems items = SimpRepItems.create();
		if (items==null)
			return null;
		return new CallSimpRepItems(items);
	}
	
	public void append(CallSimpRepItem item) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepItems,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        items.append(item.getItem());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepItems,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return items.getarraysize();
	}
	
	public CallSimpRepItem get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepItems,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        SimpRepItem item = items.get(idx);
        return new CallSimpRepItem(item);
	}

	public SimpRepItems getItems() {
		return items;
	}
}
