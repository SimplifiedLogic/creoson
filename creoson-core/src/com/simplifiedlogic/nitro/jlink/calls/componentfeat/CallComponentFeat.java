/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.componentfeat;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcComponentFeat.CompModelReplace;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallComponentPath;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcComponentFeat.ComponentFeat
 * 
 * @author Adam Andrews
 *
 */
public class CallComponentFeat extends CallFeature {

	public CallComponentFeat(ComponentFeat feat) {
		super((ComponentFeat)feat);
	}
	
	public CallModelDescriptor getModelDescr() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentFeat,GetModelDescr", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelDescriptor desc = getComponentFeat().GetModelDescr();
		if (desc==null)
			return null;
		return new CallModelDescriptor(desc);
	}
	
	public CallCompModelReplace createReplaceOp(CallModel newModel) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentFeat,CreateReplaceOp", 0, NitroConstants.DEBUG_JLINK_KEY);
		CompModelReplace op = getComponentFeat().CreateReplaceOp(newModel.getModel());
		if (op==null)
			return null;
		return new CallCompModelReplace(op);
	}
	
	public void setConstraints(CallComponentConstraints constraints, CallComponentPath referenceAssembly) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentFeat,SetConstraints", 0, NitroConstants.DEBUG_JLINK_KEY);
		getComponentFeat().SetConstraints(
				constraints!=null ? constraints.getConstraints() : null, 
				referenceAssembly!=null ? referenceAssembly.getCompPath() : null);
	}
	
	public void redefineThroughUI() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ComponentFeat,RedefineThroughUI", 0, NitroConstants.DEBUG_JLINK_KEY);
		getComponentFeat().RedefineThroughUI();
	}
	
	public ComponentFeat getComponentFeat() {
		return (ComponentFeat)item;
	}
}
