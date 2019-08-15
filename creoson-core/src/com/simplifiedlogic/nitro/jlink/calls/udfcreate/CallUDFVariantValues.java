package com.simplifiedlogic.nitro.jlink.calls.udfcreate;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUDFCreate.UDFVariantValue;
import com.ptc.pfc.pfcUDFCreate.UDFVariantValues;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

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
