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
package com.simplifiedlogic.nitro.jlink.calls.udfcreate;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUDFCreate.UDFReference;
import com.ptc.pfc.pfcUDFCreate.UDFReferences;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcUDFCreate.UDFReferences
 * 
 * @author Adam Andrews
 *
 */
public class CallUDFReferences {

	private UDFReferences refs;
	
	public CallUDFReferences(UDFReferences refs) {
		this.refs = refs;
	}
	
	public static CallUDFReferences create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFReferences,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        UDFReferences refs = UDFReferences.create();
		if (refs==null)
			return null;
		return new CallUDFReferences(refs);
	}
	
	public void append(CallUDFReference ref) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFReferences,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        refs.append(ref.getRef());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFReferences,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return refs.getarraysize();
	}
	
	public CallUDFReference get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFReferences,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        UDFReference ref = refs.get(idx);
        return new CallUDFReference(ref);
	}

	public void set(int idx, CallUDFReference value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFReferences,set(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        refs.set(idx, value.getRef());
	}

	public UDFReferences getRefs() {
		return refs;
	}
}
