package com.simplifiedlogic.nitro.jlink.calls.udfgroup;

import com.ptc.pfc.pfcUDFGroup.UDFGroupCreateInstructions;

public class CallUDFGroupCreateInstructions {

	private UDFGroupCreateInstructions instr;
	
	protected CallUDFGroupCreateInstructions(UDFGroupCreateInstructions instr) {
		this.instr = instr;
	}

	
	public UDFGroupCreateInstructions getInstr() {
		return instr;
	}
}
