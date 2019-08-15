package com.simplifiedlogic.nitro.jlink.calls.udfcreate;

import com.ptc.pfc.pfcUDFCreate.UDFVariantValue;

public class CallUDFVariantValue {

	protected UDFVariantValue val;

	public CallUDFVariantValue(UDFVariantValue val) {
		this.val = val;
	}

	
	public UDFVariantValue getValue() {
		return val;
	}
}
