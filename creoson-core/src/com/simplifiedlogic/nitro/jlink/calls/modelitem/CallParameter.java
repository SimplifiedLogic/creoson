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
import com.ptc.pfc.pfcModelItem.Parameter;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModelItem.Parameter
 * 
 * @author Adam Andrews
 *
 */
public class CallParameter {

	private Parameter param;
	
	public CallParameter(Parameter param) {
		this.param = param;
	}

	public CallParamValue getValue() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Parameter,GetValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = param.GetValue();
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public void setValue(CallParamValue pval) throws jxthrowable {
		if (pval==null) 
			return;
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Parameter,SetValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		param.SetValue(pval.getValue());
	}
	
	public String getName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Parameter,GetName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return param.GetName();
	}
	
	public boolean getIsDesignated() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Parameter,GetIsDesignated", 0, NitroConstants.DEBUG_JLINK_KEY);
		return param.GetIsDesignated();
	}
	
	public void setIsDesignated(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Parameter,SetIsDesignated", 0, NitroConstants.DEBUG_JLINK_KEY);
		param.SetIsDesignated(value);
	}
	
	public void delete() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Parameter,Delete", 0, NitroConstants.DEBUG_JLINK_KEY);
		param.Delete();
	}
	
	public Parameter getParam() {
		return param;
	}
}
