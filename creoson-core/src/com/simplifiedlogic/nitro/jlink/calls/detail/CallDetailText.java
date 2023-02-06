/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.detail;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailText;
import com.ptc.pfc.pfcDetail.pfcDetail;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailText
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailText {

	protected DetailText textData;
	
	public CallDetailText(DetailText textData) {
		this.textData = textData;
	}
	
	public static CallDetailText create(String inText) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcDetail,DetailText_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailText textData = pfcDetail.DetailText_Create(inText);
		if (textData==null)
			return null;
		return new CallDetailText(textData);
	}

	public String getText() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,GetText", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getTextData().GetText();
	}
	
	public void setText(String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,SetText", 0, NitroConstants.DEBUG_JLINK_KEY);
		getTextData().SetText(value);
	}
	
	public String getFontName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,GetFontName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getTextData().GetFontName();
	}
	
	public void setFontName(String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,SetFontName", 0, NitroConstants.DEBUG_JLINK_KEY);
		getTextData().SetFontName(value);
	}
	
	public Boolean getIsUnderlined() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,GetIsUnderlined", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Boolean ret = getTextData().GetIsUnderlined();
//		if (ret==null)
//			return false;
//		return ret.booleanValue();
        return getTextData().GetIsUnderlined();
	}
	
	public void setIsUnderlined(Boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,SetIsUnderlined", 0, NitroConstants.DEBUG_JLINK_KEY);
		getTextData().SetIsUnderlined(value);
	}
	
	public Double getTextHeight() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,GetTextHeight", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Double ret = getTextData().GetTextHeight();
//		if (ret==null)
//			return 0.0;
//		return ret.doubleValue();
        return getTextData().GetTextHeight();
	}
	
	public void setTextHeight(Double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,SetTextHeight", 0, NitroConstants.DEBUG_JLINK_KEY);
		getTextData().SetTextHeight(value);
	}
	
	public Double getTextSlantAngle() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,GetTextSlantAngle", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Double ret = getTextData().GetTextSlantAngle();
//		if (ret==null)
//			return 0.0;
//		return ret.doubleValue();
		return getTextData().GetTextSlantAngle();
	}
	
	public void setTextSlantAngle(Double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,SetTextSlantAngle", 0, NitroConstants.DEBUG_JLINK_KEY);
		getTextData().SetTextSlantAngle(value);
	}
	
	public Double getTextThickness() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,GetTextThickness", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Double ret = getTextData().GetTextThickness();
//		if (ret==null)
//			return 0.0;
//		return ret.doubleValue();
		return getTextData().GetTextThickness();
	}
	
	public void setTextThickness(Double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,SetTextThickness", 0, NitroConstants.DEBUG_JLINK_KEY);
		getTextData().SetTextThickness(value);
	}
	
	public Double getTextWidthFactor() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,GetTextWidthFactor", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Double ret = getTextData().GetTextWidthFactor();
//		if (ret==null)
//			return 0.0;
//		return ret.doubleValue();
		return getTextData().GetTextWidthFactor();
	}

	public void setTextWidthFactor(Double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailText,SetTextWidthFactor", 0, NitroConstants.DEBUG_JLINK_KEY);
		getTextData().SetTextWidthFactor(value);
	}

	public void copySettings(CallDetailText text) throws jxthrowable {
		text.setFontName(getFontName());
		text.setIsUnderlined(getIsUnderlined());
		text.setTextHeight(getTextHeight());
		text.setTextSlantAngle(getTextSlantAngle());
		text.setTextThickness(getTextThickness());
		text.setTextWidthFactor(getTextWidthFactor());
	}
	
	public DetailText getTextData() {
		return textData;
	}
}
