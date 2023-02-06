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
package com.simplifiedlogic.nitro.jlink.calls.udfcreate;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUDFCreate.UDFVariantValue;
import com.ptc.pfc.pfcUDFCreate.UDFVariantValues;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcUDFCreate.UDFVariantValues
 * 
 * @author Adam Andrews
 *
 */
public class CallUDFVariantValues {

	private UDFVariantValues vals;
	
	public CallUDFVariantValues(UDFVariantValues vals) {
		this.vals = vals;
	}
	
	public static CallUDFVariantValues create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFVariantValues,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        UDFVariantValues vals = UDFVariantValues.create();
		if (vals==null)
			return null;
		return new CallUDFVariantValues(vals);
	}
	
	public void append(CallUDFVariantValue val) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFVariantValues,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        vals.append(val.getValue());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFVariantValues,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return vals.getarraysize();
	}
	
	public CallUDFVariantValue get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFVariantValues,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        UDFVariantValue val = vals.get(idx);
        return new CallUDFVariantValue(val);
	}

	public void set(int idx, CallUDFVariantValue value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFVariantValues,set(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        vals.set(idx, value.getValue());
	}

	public UDFVariantValues getValues() {
		return vals;
	}
}
