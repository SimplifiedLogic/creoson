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
package com.simplifiedlogic.nitro.jlink.calls.window;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcWindow.DotsPerInch;
import com.ptc.pfc.pfcWindow.RasterDepth;
import com.ptc.pfc.pfcWindow.RasterImageExportInstructions;
import com.ptc.pfc.pfcWindow.pfcWindow;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcWindow.RasterImageExportInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallRasterImageExportInstructions {

	private RasterImageExportInstructions instr;

	private CallRasterImageExportInstructions(RasterImageExportInstructions instr) {
		this.instr = instr;
	}
	
	public void setImageDepth(RasterDepth depth) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("RasterImageExportInstructions,SetImageDepth", 0, NitroConstants.DEBUG_JLINK_KEY);
		instr.SetImageDepth(depth);
	}
	
	public void setDotsPerInch(DotsPerInch dpi) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("RasterImageExportInstructions,SetDotsPerInch", 0, NitroConstants.DEBUG_JLINK_KEY);
		instr.SetDotsPerInch(dpi);
	}
	
	public static CallRasterImageExportInstructions createBitmapExport(double rasterHeight, double rasterWidth) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcWindow,BitmapImageExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		RasterImageExportInstructions rxi = pfcWindow.BitmapImageExportInstructions_Create(rasterHeight, rasterWidth);
		if (rxi==null)
			return null;
		return new CallRasterImageExportInstructions(rxi);
	}

	public static CallRasterImageExportInstructions createEPSExport(double rasterHeight, double rasterWidth) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcWindow,EPSImageExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		RasterImageExportInstructions rxi = pfcWindow.EPSImageExportInstructions_Create(rasterHeight, rasterWidth);
		if (rxi==null)
			return null;
		return new CallRasterImageExportInstructions(rxi);
	}

	public static CallRasterImageExportInstructions createJPEGExport(double rasterHeight, double rasterWidth) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcWindow,JPEGImageExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		RasterImageExportInstructions rxi = pfcWindow.JPEGImageExportInstructions_Create(rasterHeight, rasterWidth);
		if (rxi==null)
			return null;
		return new CallRasterImageExportInstructions(rxi);
	}

	public static CallRasterImageExportInstructions createTIFFExport(double rasterHeight, double rasterWidth) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcWindow,TIFFImageExportInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		RasterImageExportInstructions rxi = pfcWindow.TIFFImageExportInstructions_Create(rasterHeight, rasterWidth);
		if (rxi==null)
			return null;
		return new CallRasterImageExportInstructions(rxi);
	}

	public RasterImageExportInstructions getInstr() {
		return instr;
	}
}
