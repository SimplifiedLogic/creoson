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
package com.simplifiedlogic.nitro.jlink.calls.detail;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.ColorRGB;
import com.ptc.pfc.pfcDetail.DetailCreateInstructions;
import com.ptc.pfc.pfcDetail.DetailLeaders;
import com.ptc.pfc.pfcDetail.DetailNoteInstructions;
import com.ptc.pfc.pfcDetail.DetailTextLines;
import com.ptc.pfc.pfcDetail.HorizontalJustification;
import com.ptc.pfc.pfcDetail.VerticalJustification;
import com.ptc.pfc.pfcDetail.pfcDetail;
import com.simplifiedlogic.nitro.jlink.calls.base.CallColorRGB;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.DetailNoteInstructions
 * 
 * @author Adam Andrews
 *
 */
public class CallDetailNoteInstructions extends CallDetailCreateInstructions {

	public CallDetailNoteInstructions(DetailNoteInstructions inst) {
		super((DetailCreateInstructions)inst);
	}
	
	public static CallDetailNoteInstructions create(CallDetailTextLines lines) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcDetail,DetailNoteInstructions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		DetailNoteInstructions instr = pfcDetail.DetailNoteInstructions_Create(lines.getLines());
		if (instr==null)
			return null;
		return new CallDetailNoteInstructions(instr);
		
	}

	public CallDetailTextLines getTextLines() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetTextLines", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailTextLines lines = getInst().GetTextLines();
        if (lines==null)
        	return null;
		return new CallDetailTextLines(lines);
	}
	
	public CallColorRGB getColor() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetColor", 0, NitroConstants.DEBUG_JLINK_KEY);
		ColorRGB color = getInst().GetColor();
		if (color==null)
			return null;
		return new CallColorRGB(color);
	}
	
	public void setColor(CallColorRGB value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,SetColor", 0, NitroConstants.DEBUG_JLINK_KEY);
		if (value==null)
			getInst().SetColor(null);
		else
			getInst().SetColor(value.getRgb());
	}
	
	public HorizontalJustification getHorizontal() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetHorizontal", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getInst().GetHorizontal();
	}
	
	public void setHorizontal(HorizontalJustification value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,SetHorizontal", 0, NitroConstants.DEBUG_JLINK_KEY);
		getInst().SetHorizontal(value);
	}
	
	public VerticalJustification getVertical() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetVertical", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getInst().GetVertical();
	}
	
	public void setVertical(VerticalJustification value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,SetVertical", 0, NitroConstants.DEBUG_JLINK_KEY);
		getInst().SetVertical(value);
	}
	
	public Boolean getIsDisplayed() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetIsDisplayed", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Boolean ret = getInst().GetIsDisplayed();
//		if (ret==null)
//			return false;
//		return ret.booleanValue();
        return getInst().GetIsDisplayed();
	}
	
	public void setIsDisplayed(Boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,SetIsDisplayed", 0, NitroConstants.DEBUG_JLINK_KEY);
		getInst().SetIsDisplayed(value);
	}
	
	public Boolean getIsMirrored() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetIsMirrored", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Boolean ret = getInst().GetIsMirrored();
//		if (ret==null)
//			return false;
//		return ret.booleanValue();
        return getInst().GetIsMirrored();
	}
	
	public void setIsMirrored(Boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,SetIsMirrored", 0, NitroConstants.DEBUG_JLINK_KEY);
		getInst().SetIsMirrored(value);
	}
	
	public Double getTextAngle() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetTextAngle", 0, NitroConstants.DEBUG_JLINK_KEY);
//		Double ret = getInst().GetTextAngle();
//		if (ret==null)
//			return 0.0;
//		return ret.doubleValue();
        return getInst().GetTextAngle();
	}
	
	public void setTextAngle(Double value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,SetTextAngle", 0, NitroConstants.DEBUG_JLINK_KEY);
		getInst().SetTextAngle(value);
	}
	
	public void setLeader(CallDetailLeaders value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,SetLeader", 0, NitroConstants.DEBUG_JLINK_KEY);
		if (value==null)
			getInst().SetLeader(null);
		else
			getInst().SetLeader(value.getLeaders());
	}

	public CallDetailLeaders getLeader() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("DetailNoteInstructions,GetLeader", 0, NitroConstants.DEBUG_JLINK_KEY);
        DetailLeaders leaders = getInst().GetLeader();
        if (leaders==null)
        	return null;
        return new CallDetailLeaders(leaders);
	}

	public void copySettings(CallDetailNoteInstructions targetInst) throws jxthrowable {
		targetInst.setColor(getColor());
		targetInst.setHorizontal(getHorizontal());
		targetInst.setIsDisplayed(getIsDisplayed());
		targetInst.setIsMirrored(getIsMirrored());
		targetInst.setTextAngle(getTextAngle());
		targetInst.setVertical(getVertical());
	}
	
	public DetailNoteInstructions getInst() {
		return ((DetailNoteInstructions)inst);
	}
}
