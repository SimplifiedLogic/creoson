package com.simplifiedlogic.nitro.jlink.calls.udfcreate;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUDFCreate.pfcUDFCreate;
import com.ptc.pfc.pfcUDFCreate.UDFVariantDimension;
import com.ptc.pfc.pfcUDFCreate.UDFVariantValue;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

public class CallUDFVariantDimension extends CallUDFVariantValue {

	public CallUDFVariantDimension(UDFVariantDimension val) {
		super((UDFVariantValue)val);
	}

	public static CallUDFVariantDimension create(String name, double dimensionValue) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcUDFCreate,UDFVariantDimension_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		UDFVariantDimension dim = pfcUDFCreate.UDFVariantDimension_Create(name, dimensionValue);
		if (dim==null)
			return null;
		return new CallUDFVariantDimension(dim);
	}
	
	public UDFVariantDimension getValue() {
		return (UDFVariantDimension)super.getValue();
	}
}
