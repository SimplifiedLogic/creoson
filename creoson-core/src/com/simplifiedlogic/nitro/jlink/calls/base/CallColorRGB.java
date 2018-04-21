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
package com.simplifiedlogic.nitro.jlink.calls.base;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.ColorRGB;
import com.ptc.pfc.pfcBase.pfcBase;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcBase.ColorRGB
 * 
 * @author Adam Andrews
 *
 */
public class CallColorRGB {

	private ColorRGB rgb;
	
	public CallColorRGB(ColorRGB rgb) {
		this.rgb = rgb;
	}
	
	public double getRed() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ColorRGB,GetRed", 0, NitroConstants.DEBUG_JLINK_KEY);
		return rgb.GetRed();
	}

	public double getGreen() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ColorRGB,GetGreen", 0, NitroConstants.DEBUG_JLINK_KEY);
		return rgb.GetGreen();
	}

	public double getBlue() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ColorRGB,GetBlue", 0, NitroConstants.DEBUG_JLINK_KEY);
		return rgb.GetBlue();
	}

	public static CallColorRGB create(double inRed, double inGreen, double inBlue) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcBase,ColorRGB_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		ColorRGB color = pfcBase.ColorRGB_Create(inRed, inGreen, inBlue);
		if (color==null)
			return null;
		return new CallColorRGB(color);
	}

	public ColorRGB getRgb() {
		return rgb;
	}
}
