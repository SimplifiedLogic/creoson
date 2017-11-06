/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.drawing;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Transform3D;
import com.ptc.pfc.pfcDetail.DetailItem;
import com.ptc.pfc.pfcDrawing.Drawing;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcSheet.SheetData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailCreateInstructions;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailItem;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model2d.CallModel2D;
import com.simplifiedlogic.nitro.jlink.calls.sheet.CallSheetData;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDrawing.Drawing
 * 
 * @author Adam Andrews
 *
 */
public class CallDrawing extends CallModel2D {

	public CallDrawing(Drawing m) {
		super((Model2D)m);
	}
	
	public int getNumberOfSheets() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,GetNumberOfSheets", 0, NitroConstants.DEBUG_JLINK_KEY);
        int numSheets = getDrawing().GetNumberOfSheets();
        return numSheets;
	}
	
	public void regenerateSheet(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,RegenerateSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDrawing().RegenerateSheet(sheetNumber);
	}
	
	public void setCurrentSheetNumber(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,SetCurrentSheetNumber", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDrawing().SetCurrentSheetNumber(sheetNumber);
	}
	
	public int getCurrentSheetNumber() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,GetCurrentSheetNumber", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getDrawing().GetCurrentSheetNumber();
	}
	
	public void addSheet() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,AddSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDrawing().AddSheet();
	}

	public void deleteSheet(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,DeleteSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDrawing().DeleteSheet(sheetNumber);
	}
	
	public void setSheetScale(int sheetNumber, double scale, CallModel drawingModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,SetSheetScale", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDrawing().SetSheetScale(sheetNumber, scale, drawingModel!=null ? drawingModel.getModel() : null);
	}
	
	public double getSheetScale(int sheetNumber, CallModel drawingModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,GetSheetScale", 0, NitroConstants.DEBUG_JLINK_KEY);
		double scale = getDrawing().GetSheetScale(sheetNumber, drawingModel!=null ? drawingModel.getModel() : null);
		return scale;
	}
	
	public CallSheetData getSheetData(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,GetSheetData", 0, NitroConstants.DEBUG_JLINK_KEY);
		SheetData data = getDrawing().GetSheetData(sheetNumber);
		if (data==null)
			return null;
		return new CallSheetData(data);
	}

	public void reorderSheet(int fromSheetNumber, int to) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,ReorderSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDrawing().ReorderSheet(fromSheetNumber, to);
	}

	public CallDetailItem createDetailItem(CallDetailCreateInstructions instructions) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,CreateDetailItem", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailItem item = getDrawing().CreateDetailItem(instructions.getInst());
		if (item==null)
			return null;
		return CallDetailItem.create(item);
	}
	
	public CallTransform3D getSheetTransform(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Drawing,GetSheetTransform", 0, NitroConstants.DEBUG_JLINK_KEY);
		Transform3D transform = getDrawing().GetSheetTransform(sheetNumber);
		if (transform==null)
			return null;
		return new CallTransform3D(transform);
	}

	public Drawing getDrawing() {
		return (Drawing)m;
	}
}
