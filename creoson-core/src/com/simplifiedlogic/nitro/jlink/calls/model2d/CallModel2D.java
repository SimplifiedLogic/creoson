/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.Models;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcView2D.View2D;
import com.ptc.pfc.pfcView2D.View2Ds;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModels;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2DCreateInstructions;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2Ds;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

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
	
	public Model2D getModel2D() {
		return (Model2D)m;
	}
	
}
