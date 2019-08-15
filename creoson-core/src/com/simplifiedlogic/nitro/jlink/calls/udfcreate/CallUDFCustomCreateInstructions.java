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
