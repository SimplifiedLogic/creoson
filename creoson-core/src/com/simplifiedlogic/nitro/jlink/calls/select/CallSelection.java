/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.select;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAssembly.ComponentPath;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcSelect.Selection;
import com.ptc.pfc.pfcSelect.pfcSelect;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallComponentPath;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSelect.Selection
 * 
 * @author Adam Andrews
 *
 */
public class CallSelection {

	private Selection sel;
	
	public CallSelection(Selection sel) {
		this.sel = sel;
	}
	
	public CallModelItem getSelItem() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Selection,GetSelItem", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelItem item = sel.GetSelItem();
		if (item==null)
			return null;
		return CallModelItem.create(item);
	}

	public CallModel getSelModel() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Selection,GetSelModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = sel.GetSelModel();
		if (m==null)
			return null;
		return CallModel.create(m);
	}
	
	public CallComponentPath getPath() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Selection,GetPath", 0, NitroConstants.DEBUG_JLINK_KEY);
		ComponentPath path = sel.GetPath();
		if (path==null)
			return null;
		return new CallComponentPath(path);
	}
	
	public static CallSelection createModelItemSelection(CallModelItem selItem, CallComponentPath path) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcSelect,CreateModelItemSelection", 0, NitroConstants.DEBUG_JLINK_KEY);
		Selection sel = pfcSelect.CreateModelItemSelection(selItem.getItem(), path!=null ? path.getCompPath() : null);
		if (sel==null)
			return null;
		return new CallSelection(sel);
	}

	public Selection getSel() {
		return sel;
	}
}
