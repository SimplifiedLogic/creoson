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
package com.simplifiedlogic.nitro.jlink.calls.modelitem;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ParamValue;
import com.ptc.pfc.pfcModelItem.ParamValueType;
import com.ptc.pfc.pfcModelItem.pfcModelItem;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModelItem.ParamValue
 * 
 * @author Adam Andrews
 *
 */
public class CallParamValue {

	private ParamValue pval;
	
	public CallParamValue(ParamValue pval) {
		this.pval = pval;
	}

	public int getParamValueType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,Getdiscr", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValueType type = pval.Getdiscr();
        return type.getValue();
	}
	
	public int getIntValue() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,GetIntValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		return pval.GetIntValue();
	}

	public double getDoubleValue() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,GetDoubleValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		return pval.GetDoubleValue();
	}

	public boolean getBoolValue() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,GetBoolValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		return pval.GetBoolValue();
	}

	public String getStringValue() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,GetStringValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		return pval.GetStringValue();
	}

	public int getNoteId() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,GetNoteId", 0, NitroConstants.DEBUG_JLINK_KEY);
		return pval.GetNoteId();
	}
	
	public void setIntValue(int value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,SetIntValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		pval.SetIntValue(value);
	}

	public void setDoubleValue(double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,SetDoubleValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		pval.SetDoubleValue(value);
	}

	public void setBoolValue(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,SetBoolValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		pval.SetBoolValue(value);
	}

	public void setStringValue(String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,SetStringValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		pval.SetStringValue(value);
	}

	public void setNoteId(int id) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ParamValue,SetNoteId", 0, NitroConstants.DEBUG_JLINK_KEY);
		pval.SetNoteId(id);
	}

	public static CallParamValue createStringParamValue(String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModelItem,CreateStringParamValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = pfcModelItem.CreateStringParamValue(value);
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public static CallParamValue createDoubleParamValue(Double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModelItem,CreateDoubleParamValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = pfcModelItem.CreateDoubleParamValue(value);
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public static CallParamValue createIntParamValue(int value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModelItem,CreateIntParamValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = pfcModelItem.CreateIntParamValue(value);
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public static CallParamValue createBoolParamValue(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModelItem,CreateBoolParamValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = pfcModelItem.CreateBoolParamValue(value);
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public static CallParamValue createNoteParamValue(int value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModelItem,CreateNoteParamValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = pfcModelItem.CreateNoteParamValue(value);
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public ParamValue getValue() {
		return pval;
	}
}
