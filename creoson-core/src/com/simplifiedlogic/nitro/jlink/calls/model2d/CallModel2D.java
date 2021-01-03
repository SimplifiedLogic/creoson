/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.model2d;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Transform3D;
import com.ptc.pfc.pfcDetail.DetailItem;
import com.ptc.pfc.pfcDetail.DetailItems;
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;
import com.ptc.pfc.pfcDetail.DetailType;
import com.ptc.pfc.pfcDimension2D.Dimension2Ds;
import com.ptc.pfc.pfcDrawingFormat.DrawingFormat;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.Models;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcSheet.SheetData;
import com.ptc.pfc.pfcView2D.View2D;
import com.ptc.pfc.pfcView2D.View2Ds;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailCreateInstructions;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailItem;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailItems;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailSymbolDefItem;
import com.simplifiedlogic.nitro.jlink.calls.dimension2d.CallDimension2Ds;
import com.simplifiedlogic.nitro.jlink.calls.drawingformat.CallDrawingFormat;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModels;
import com.simplifiedlogic.nitro.jlink.calls.sheet.CallSheetData;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2DCreateInstructions;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2Ds;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModel2D.Model2D
 * 
 * @author Adam Andrews
 *
 */
public class CallModel2D extends CallModel {

	public CallModel2D(Model2D m) {
		super((Model)m);
	}
	
	public CallModels listModels() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,ListModels", 0, NitroConstants.DEBUG_JLINK_KEY);
        Models models = getModel2D().ListModels();
		if (models==null)
			return null;
		return new CallModels(models);
	}
	
	public void addModel(CallModel newModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,AddModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		getModel2D().AddModel(newModel.getModel());
	}
	
	public void deleteModel(CallModel model) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,DeleteModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		getModel2D().DeleteModel(model.getModel());
	}
	
	public CallModel getCurrentSolid() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetCurrentSolid", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model model = getModel2D().GetCurrentSolid();
		if (model==null)
			return null;
		return new CallModel(model);
	}
	
	public void setCurrentSolid(CallModel newModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,SetCurrentSolid", 0, NitroConstants.DEBUG_JLINK_KEY);
		getModel2D().SetCurrentSolid(newModel.getModel());
	}
	
	public void regenerate() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,Regenerate", 0, NitroConstants.DEBUG_JLINK_KEY);
		getModel2D().Regenerate();
	}
	
	public CallView2Ds list2DViews() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,List2DViews", 0, NitroConstants.DEBUG_JLINK_KEY);
		View2Ds views = getModel2D().List2DViews();
		if (views==null)
			return null;
		return new CallView2Ds(views);
	}
	
	public CallView2D getViewByName(String viewName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetViewByName", 0, NitroConstants.DEBUG_JLINK_KEY);
		View2D view = getModel2D().GetViewByName(viewName);
		if (view==null)
			return null;
		return new CallView2D(view);
	}
	
	public CallView2D createView(CallView2DCreateInstructions instructions) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetViewByName", 0, NitroConstants.DEBUG_JLINK_KEY);
		View2D view = getModel2D().CreateView(instructions.getInstr());
		if (view==null)
			return null;
		return new CallView2D(view);
	}
	
	public CallDetailItem createDetailItem(CallDetailCreateInstructions instructions) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,CreateDetailItem", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailItem item = getModel2D().CreateDetailItem(instructions.getInst());
		if (item==null)
			return null;
		return CallDetailItem.create(item);
	}
	
	public CallDetailItems listDetailItems(DetailType type, Integer sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,CreateDetailItem", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailItems items = getModel2D().ListDetailItems(type, sheetNumber);
		if (items==null)
			return null;
		return new CallDetailItems(items);
	}
	
	public CallDetailSymbolDefItem retrieveSymbolDefinition(String fileName, String filePath, Integer version, Boolean updateUnconditionally) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,RetrieveSymbolDefinition", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailSymbolDefItem item = getModel2D().RetrieveSymbolDefinition(fileName, filePath, version, updateUnconditionally);
		if (item==null)
			return null;
		return new CallDetailSymbolDefItem(item);
	}

	public CallDimension2Ds listShownDimensions(CallModel model, ModelItemType type) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,ListShownDimensions", 0, NitroConstants.DEBUG_JLINK_KEY);
		Dimension2Ds dims = getModel2D().ListShownDimensions(model.getModel(), type);
		if (dims==null)
			return null;
		return new CallDimension2Ds(dims);
	}
	
	public double getTextHeight() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetTextHeight", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getModel2D().GetTextHeight();
	}
	
	public CallTransform3D getSheetTransform(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetSheetTransform", 0, NitroConstants.DEBUG_JLINK_KEY);
		Transform3D transform = getModel2D().GetSheetTransform(sheetNumber);
		if (transform==null)
			return null;
		return new CallTransform3D(transform);
	}

	public int getNumberOfSheets() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetNumberOfSheets", 0, NitroConstants.DEBUG_JLINK_KEY);
        int numSheets = getModel2D().GetNumberOfSheets();
        return numSheets;
	}
	
	public void regenerateSheet(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,RegenerateSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
        getModel2D().RegenerateSheet(sheetNumber);
	}
	
	public void setCurrentSheetNumber(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,SetCurrentSheetNumber", 0, NitroConstants.DEBUG_JLINK_KEY);
        getModel2D().SetCurrentSheetNumber(sheetNumber);
	}
	
	public int getCurrentSheetNumber() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetCurrentSheetNumber", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getModel2D().GetCurrentSheetNumber();
	}
	
	public void addSheet() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,AddSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
        getModel2D().AddSheet();
	}

	public void deleteSheet(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,DeleteSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
        getModel2D().DeleteSheet(sheetNumber);
	}
	
	public void setSheetScale(int sheetNumber, double scale, CallModel drawingModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,SetSheetScale", 0, NitroConstants.DEBUG_JLINK_KEY);
        getModel2D().SetSheetScale(sheetNumber, scale, drawingModel!=null ? drawingModel.getModel() : null);
	}
	
	public double getSheetScale(int sheetNumber, CallModel drawingModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetSheetScale", 0, NitroConstants.DEBUG_JLINK_KEY);
		double scale = getModel2D().GetSheetScale(sheetNumber, drawingModel!=null ? drawingModel.getModel() : null);
		return scale;
	}
	
	public CallSheetData getSheetData(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetSheetData", 0, NitroConstants.DEBUG_JLINK_KEY);
		SheetData data = getModel2D().GetSheetData(sheetNumber);
		if (data==null)
			return null;
		return new CallSheetData(data);
	}

	public void reorderSheet(int fromSheetNumber, int to) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,ReorderSheet", 0, NitroConstants.DEBUG_JLINK_KEY);
        getModel2D().ReorderSheet(fromSheetNumber, to);
	}

	public CallDrawingFormat getSheetFormat(int sheetNumber) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,GetSheetFormat", 0, NitroConstants.DEBUG_JLINK_KEY);
		DrawingFormat fmt = getModel2D().GetSheetFormat(sheetNumber);
		if (fmt==null)
			return null;
		return new CallDrawingFormat(fmt);
	}

	public void setSheetFormat(int sheetNumber, CallDrawingFormat format, Integer formatSheetNumber, CallModel drawingModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,SetSheetFormat", 0, NitroConstants.DEBUG_JLINK_KEY);
        DrawingFormat f = null;
        if (format!=null)
        	f = format.getFormat();

        Model m = null;
        if (drawingModel!=null)
        	m = drawingModel.getModel();

		getModel2D().SetSheetFormat(sheetNumber, f, formatSheetNumber, m);
	}

	public void updateTables() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model2D,UpdateTables", 0, NitroConstants.DEBUG_JLINK_KEY);
        getModel2D().UpdateTables();
	}

	public Model2D getModel2D() {
		return (Model2D)m;
	}
	
}
