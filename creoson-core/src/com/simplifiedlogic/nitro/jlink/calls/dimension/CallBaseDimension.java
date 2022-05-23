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
package com.simplifiedlogic.nitro.jlink.calls.dimension;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcDimension.DimensionType;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ParamValue;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallComponentDimensionShowInstructions;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDimension.BaseDimension
 * 
 * @author Adam Andrews
 *
 */
public class CallBaseDimension extends CallModelItem {

	public CallBaseDimension(BaseDimension dim) {
		super((ModelItem)dim);
	}
	
	public CallParamValue getValue() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("BaseDimension,GetValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = getDim().GetValue();
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public void setValue(CallParamValue value) throws jxthrowable {
		if (value==null)
			return;
		ParamValue pval = value.getValue();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("BaseDimension,SetValue", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDim().SetValue(pval);
	}
	
	public void show(CallComponentDimensionShowInstructions instructions) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("BaseDimension,Show", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDim().Show(instructions!=null ? instructions.getInst() : null);
	}

	public void erase() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("BaseDimension,Erase", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDim().Erase();
	}

	public Integer getDimType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("BaseDimension,GetDimType", 0, NitroConstants.DEBUG_JLINK_KEY);
        DimensionType type = getDim().GetDimType();
        if (type==null)
        	return null;
        return type.getValue();
	}
	
	public CallStringSeq getTexts() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("BaseDimension,GetTexts", 0, NitroConstants.DEBUG_JLINK_KEY);
		stringseq texts = getDim().GetTexts();
		if (texts==null)
			return null;
		return new CallStringSeq(texts);
	}
	
	public void setTexts(CallStringSeq value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("BaseDimension,SetTexts", 0, NitroConstants.DEBUG_JLINK_KEY);
		getDim().SetTexts(value!=null ? value.getSeq() : null);
	}
	
	public BaseDimension getDim() {
		return (BaseDimension)item;
	}
}
