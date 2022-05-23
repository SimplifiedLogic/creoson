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
package com.simplifiedlogic.nitro.jlink.calls.udfcreate;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUDFCreate.UDFCustomCreateInstructions;
import com.ptc.pfc.pfcUDFCreate.UDFReferences;
import com.ptc.pfc.pfcUDFCreate.UDFVariantValues;
import com.ptc.pfc.pfcUDFCreate.pfcUDFCreate;
import com.ptc.pfc.pfcUDFGroup.UDFGroupCreateInstructions;
import com.simplifiedlogic.nitro.jlink.calls.udfgroup.CallUDFGroupCreateInstructions;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcUDFCreate.UDFCustomCreateInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallUDFCustomCreateInstructions extends CallUDFGroupCreateInstructions {

	private CallUDFCustomCreateInstructions(UDFCustomCreateInstructions instr) {
		super((UDFGroupCreateInstructions)instr);
	}

	public static CallUDFCustomCreateInstructions create(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcUDFCreate,UDFCustomCreateInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		UDFCustomCreateInstructions instr = pfcUDFCreate.UDFCustomCreateInstructions_Create(name);
		if (instr==null)
			return null;
		return new CallUDFCustomCreateInstructions(instr);
	}

	public void setReferences(CallUDFReferences value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFCustomCreateInstructions,setReferences", 0, NitroConstants.DEBUG_JLINK_KEY);
		UDFReferences refs = null;
		if (value!=null)
			refs = value.getRefs();
		getInstr().SetReferences(refs);
	}

	public void setVariantValues(CallUDFVariantValues value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UDFCustomCreateInstructions,setVariantValues", 0, NitroConstants.DEBUG_JLINK_KEY);
		UDFVariantValues vals = null;
		if (value!=null)
			vals = value.getValues();
		getInstr().SetVariantValues(vals);
	}

	public UDFCustomCreateInstructions getInstr() {
		return (UDFCustomCreateInstructions)super.getInstr();
	}
}
