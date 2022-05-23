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
package com.simplifiedlogic.nitro.jlink.calls.model;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExport.AssemblyConfiguration;
import com.ptc.pfc.pfcExport.ProductViewExportInstructions;
import com.ptc.pfc.pfcExport.ProductViewExportOptions;
import com.ptc.pfc.pfcExport.ProductViewFormat;
import com.ptc.pfc.pfcExport.pfcExport;
import com.ptc.pfc.pfcModel.ExportInstructions;
import com.ptc.pfc.pfcModel.pfcModel;
import com.simplifiedlogic.nitro.jlink.calls.export.CallGeometryFlags;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModel.ExportInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallExportInstructions {

	private ExportInstructions instr;
	
	private CallExportInstructions(ExportInstructions instr) {
		this.instr = instr;
	}

	public static CallExportInstructions createProgramExport() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModel,ProgramExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ExportInstructions instr = pfcModel.ProgramExportInstructions_Create();
		if (instr==null)
			return null;
		return new CallExportInstructions(instr);
	}
	
	public static CallExportInstructions createVRMLExport(String dirname) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModel,VRMLModelExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ExportInstructions instr = pfcModel.VRMLModelExportInstructions_Create(dirname);
		if (instr==null)
			return null;
		return new CallExportInstructions(instr);
	}
	
	public static CallExportInstructions createDXFExport() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModel,DXFExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ExportInstructions instr = pfcModel.DXFExportInstructions_Create();
		if (instr==null)
			return null;
		return new CallExportInstructions(instr);
	}
	
	public static CallExportInstructions createPlotExport(String plotterName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModel,PlotInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ExportInstructions instr = pfcModel.PlotInstructions_Create(plotterName);
		if (instr==null)
			return null;
		return new CallExportInstructions(instr);
	}
	
	public static CallExportInstructions createSTEP3DExport(AssemblyConfiguration inConfiguration, CallGeometryFlags inGeometry) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcExport,STEP3DExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ExportInstructions instr = pfcExport.STEP3DExportInstructions_Create(inConfiguration, inGeometry.getFlags());
		if (instr==null)
			return null;
		return new CallExportInstructions(instr);
	}
	
	public static CallExportInstructions createIGES3DExport(AssemblyConfiguration inConfiguration, CallGeometryFlags inGeometry) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcExport,IGES3DNewExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ExportInstructions instr = pfcExport.IGES3DNewExportInstructions_Create(inConfiguration, inGeometry.getFlags());
		if (instr==null)
			return null;
		return new CallExportInstructions(instr);
	}
	
	public static CallExportInstructions createProductViewExport(ProductViewFormat format) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcExport,ProductViewExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
        ProductViewExportInstructions pxi = pfcExport.ProductViewExportInstructions_Create();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcExport,ProductViewExportOptions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
        ProductViewExportOptions opt = pfcExport.ProductViewExportOptions_Create(format);
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ProductViewExportInstructions,SetPVExportOptions", 0, NitroConstants.DEBUG_JLINK_KEY);
        pxi.SetPVExportOptions(opt);
		if (pxi==null)
			return null;
		return new CallExportInstructions(pxi);
	}
	
	public static CallExportInstructions createNEUTRALFileExport() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcExport,NEUTRALFileExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ExportInstructions instr = pfcExport.NEUTRALFileExportInstructions_Create();
		if (instr==null)
			return null;
		return new CallExportInstructions(instr);
	}

	public ExportInstructions getInstr() {
		return instr;
	}
}
