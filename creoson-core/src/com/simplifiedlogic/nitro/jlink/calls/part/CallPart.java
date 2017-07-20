package com.simplifiedlogic.nitro.jlink.calls.part;

import com.ptc.pfc.pfcPart.Part;
import com.ptc.pfc.pfcSolid.Solid;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcPart.Part
 * 
 * @author Adam Andrews
 *
 */
public class CallPart extends CallSolid {

	public CallPart(Part m) {
		super((Solid)m);
	}
	
	public Part getPart() {
		return (Part)m;
	}

}
