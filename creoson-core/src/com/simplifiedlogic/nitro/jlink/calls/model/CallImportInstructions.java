/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.model;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.ImportInstructions;
import com.ptc.pfc.pfcModel.pfcModel;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModel.ImportInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallImportInstructions {

	private ImportInstructions instr;
	
	private CallImportInstructions(ImportInstructions instr) {
		this.instr = instr;
	}

	public static CallImportInstructions createProgramImport() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModel,ProgramImportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
        ImportInstructions instr = pfcModel.ProgramImportInstructions_Create();
		if (instr==null)
			return null;
		return new CallImportInstructions(instr);
	}
	
	public ImportInstructions getInstr() {
		return instr;
	}
}
