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
package com.simplifiedlogic.nitro.jlink.calls.feature;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcComponentFeat.ComponentFeat;
import com.ptc.pfc.pfcFeature.DeleteOperation;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.FeatureGroup;
import com.ptc.pfc.pfcFeature.FeaturePattern;
import com.ptc.pfc.pfcFeature.Features;
import com.ptc.pfc.pfcFeature.GroupPattern;
import com.ptc.pfc.pfcFeature.ResumeOperation;
import com.ptc.pfc.pfcFeature.SuppressOperation;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcModelItem.ModelItems;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcModelItem.Parameters;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentFeat;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItems;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameterOwner;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameters;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcFeature.Feature
 * 
 * @author Adam Andrews
 *
 */
public class CallFeature extends CallModelItem implements CallParameterOwner {

	public CallFeature(Feature feat) {
		super((ModelItem)feat);
	}

	public int getStatus() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetStatus", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getFeature().GetStatus().getValue();
	}
	
	public CallModelItems listSubItems(ModelItemType type) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,ListSubItems", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelItems items = getFeature().ListSubItems(type);
		if (items==null)
			return null;
		return new CallModelItems(items);
	}
	
	public CallFeatures listChildren() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,ListChildren", 0, NitroConstants.DEBUG_JLINK_KEY);
		Features features = getFeature().ListChildren();
		if (features==null)
			return null;
		return new CallFeatures(features);
	}
	
	public int getFeatType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetFeatType", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getFeature().GetFeatType().getValue();
	}
	
	public String getFeatTypeName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetFeatTypeName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getFeature().GetFeatTypeName();
	}
	
	public Integer getNumber() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetNumber", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getFeature().GetNumber();
	}
	
	public CallDeleteOperation createDeleteOp() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,CreateDeleteOp", 0, NitroConstants.DEBUG_JLINK_KEY);
		DeleteOperation op = getFeature().CreateDeleteOp();
		if (op==null)
			return null;
		return new CallDeleteOperation(op);
	}
	
	public CallResumeOperation createResumeOp() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,CreateResumeOp", 0, NitroConstants.DEBUG_JLINK_KEY);
		ResumeOperation op = getFeature().CreateResumeOp();
		if (op==null)
			return null;
		return new CallResumeOperation(op);
	}
	
	public CallSuppressOperation createSuppressOp() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,CreateSuppressOp", 0, NitroConstants.DEBUG_JLINK_KEY);
		SuppressOperation op = getFeature().CreateSuppressOp();
		if (op==null)
			return null;
		return new CallSuppressOperation(op);
	}
	
	public static CallFeature createFeature(Feature feat) {
		if (feat==null)
			return null;
		if (feat instanceof ComponentFeat)
			return new CallComponentFeat((ComponentFeat)feat);
		else
			return new CallFeature(feat);
	}
	
	@Override
	public CallParameter getParam(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetParam", 0, NitroConstants.DEBUG_JLINK_KEY);
		Parameter param = getFeature().GetParam(name);
		if (param==null)
			return null;
		return new CallParameter(param);
	}

	@Override
	public CallParameters listParams() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,ListParams", 0, NitroConstants.DEBUG_JLINK_KEY);
        Parameters params = getFeature().ListParams();
		if (params==null)
			return null;
		return new CallParameters(params);
	}
	
	@Override
	public CallParameter createParam(String name, CallParamValue value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,CreateParam", 0, NitroConstants.DEBUG_JLINK_KEY);
		Parameter param = getFeature().CreateParam(name, value!=null ? value.getValue() : null);
		if (param==null)
			return null;
		return new CallParameter(param);
	}

	public CallFeaturePattern getPattern() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetPattern", 0, NitroConstants.DEBUG_JLINK_KEY);
		FeaturePattern pat = getFeature().GetPattern();
		if (pat==null)
			return null;
		return new CallFeaturePattern(pat);
	}

	public CallGroupPattern getGroupPattern() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetGroupPattern", 0, NitroConstants.DEBUG_JLINK_KEY);
        GroupPattern pat = getFeature().GetGroupPattern();
		if (pat==null)
			return null;
		return new CallGroupPattern(pat);
	}

	public CallFeatureGroup getGroup() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetGroup", 0, NitroConstants.DEBUG_JLINK_KEY);
        FeatureGroup group = getFeature().GetGroup();
		if (group==null)
			return null;
		return new CallFeatureGroup(group);
	}

	public boolean getIsGroupMember() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Feature,GetIsGroupMember", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getFeature().GetIsGroupMember();
	}
	
	public Feature getFeature() {
		return (Feature)item;
	}

}
