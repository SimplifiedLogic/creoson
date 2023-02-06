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
import com.ptc.pfc.pfcDetail.DetailItem;
import com.ptc.pfc.pfcDetail.DetailItems;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailTexts
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailItems {

	private DetailItems items;
	
	public CallDetailItems(DetailItems items) {
		this.items = items;
	}
	
	public static CallDetailItems create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailItems,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailItems items = DetailItems.create();
		if (items==null)
			return null;
		return new CallDetailItems(items);
	}
	
	public void append(CallDetailItem item) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailItems,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        items.append(item.getDetailItem());
	}
	
	public void insert(int idx, CallDetailItem item) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailItems,insert", 0, NitroConstants.DEBUG_JLINK_KEY);
        items.insert(idx, item.getDetailItem());
	}

	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailItems,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return items.getarraysize();
	}
	
	public CallDetailItem get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailItems,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailItem item = items.get(idx);
        return CallDetailItem.create(item);
	}

	public DetailItems getItems() {
		return items;
	}
}
