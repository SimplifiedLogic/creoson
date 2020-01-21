/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
import com.ptc.pfc.pfcDetail.DetailNoteItem;
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailItem
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailItem extends CallModelItem {

	public CallDetailItem(DetailItem detailItem) {
		super((ModelItem)detailItem);
	}
	
	public int getType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailItem,GetType", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getDetailItem().GetType().getValue();
	}
	
	public static CallDetailItem create(DetailItem item) {
		if (item==null)
			return null;
		if (item instanceof DetailNoteItem) 
			return new CallDetailNoteItem((DetailNoteItem)item);
		else if (item instanceof DetailSymbolDefItem) 
			return new CallDetailSymbolDefItem((DetailSymbolDefItem)item);
		else if (item instanceof DetailSymbolInstItem) 
			return new CallDetailSymbolInstItem((DetailSymbolInstItem)item);
		else
			return new CallDetailItem(item);
	}

	public void delete() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailItem,Remove", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDetailItem().Delete();
	}
		
	public DetailItem getDetailItem() {
		return (DetailItem)item;
	}
}
