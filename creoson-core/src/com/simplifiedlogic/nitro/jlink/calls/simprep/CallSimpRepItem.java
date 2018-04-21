/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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
import com.ptc.pfc.pfcSimpRep.SimpRepAction;
import com.ptc.pfc.pfcSimpRep.SimpRepCompItemPath;
import com.ptc.pfc.pfcSimpRep.SimpRepFeatItemPath;
import com.ptc.pfc.pfcSimpRep.SimpRepItem;
import com.ptc.pfc.pfcSimpRep.SimpRepItemPath;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSimpRep.SimpRepItem
 * 
 * @author Adam Andrews
 *
 */
public class CallSimpRepItem {

	protected SimpRepItem item;
	
	public CallSimpRepItem(SimpRepItem item) {
		this.item = item;
	}
	
	public int getAction() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepItem,GetAction", 0, NitroConstants.DEBUG_JLINK_KEY);
		SimpRepAction action = item.GetAction();
		if (action==null)
			return -1;
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepAction,GetType", 0, NitroConstants.DEBUG_JLINK_KEY);
		return action.GetType().getValue();
	}
	
	public CallSimpRepItemPath getItemPath() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepItem,GetItemPath", 0, NitroConstants.DEBUG_JLINK_KEY);
        SimpRepItemPath path = item.GetItemPath();
        if (path==null)
        	return null;
        if (path instanceof SimpRepCompItemPath)
        	return new CallSimpRepCompItemPath((SimpRepCompItemPath)path);
        else if (path instanceof SimpRepFeatItemPath)
        	return new CallSimpRepFeatItemPath((SimpRepFeatItemPath)path);
        else
        	return null;
	}
	
	public SimpRepItem getItem() {
		return item;
	}
}
