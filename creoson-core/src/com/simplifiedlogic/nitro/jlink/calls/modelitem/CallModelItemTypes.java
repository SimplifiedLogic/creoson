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
package com.simplifiedlogic.nitro.jlink.calls.modelitem;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcModelItem.ModelItemTypes;
import com.ptc.pfc.pfcModelItem.ModelItems;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModelItem.ModelItems
 * 
 * @author Adam Andrews
 *
 */
public class CallModelItemTypes {

	private ModelItemTypes itemTypes;
	
	public CallModelItemTypes(ModelItemTypes itemTypes) {
		this.itemTypes = itemTypes;
	}
	
	public static CallModelItemTypes create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelItemTypes,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        ModelItemTypes items = ModelItemTypes.create();
		if (items==null)
			return null;
		return new CallModelItemTypes(items);
	}
	
	public void append(ModelItemType value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelItemTypes,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        itemTypes.append(value);
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelItemTypes,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return itemTypes.getarraysize();
	}
	
	public ModelItemType get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelItemTypes,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        ModelItemType type = itemTypes.get(idx);
        return type;
	}

	public ModelItemTypes getItemTypes() {
		return itemTypes;
	}
}
