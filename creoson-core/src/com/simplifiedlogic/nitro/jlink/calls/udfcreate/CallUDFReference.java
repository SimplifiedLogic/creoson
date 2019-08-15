package com.simplifiedlogic.nitro.jlink.calls.udfcreate;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUDFCreate.UDFReference;
import com.ptc.pfc.pfcUDFCreate.pfcUDFCreate;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

public class CallUDFReference {

	protected UDFReference ref;

	public CallUDFReference(UDFReference ref) {
		this.ref = ref;
	}

	public static CallUDFReference create(String promptForReference, CallSelection referenceItem) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcUDFCreate,UDFReference_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		UDFReference ref = pfcUDFCreate.UDFReference_Create(promptForReference, referenceItem.getSel());
		if (ref==null)
			return null;
		return new CallUDFReference(ref);
	}
	
	public UDFReference getRef() {
		return ref;
	}

}
