/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.units;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUnits.pfcUnits;
import com.ptc.pfc.pfcUnits.UnitConversionOptions;
import com.ptc.pfc.pfcUnits.UnitDimensionConversion;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcUnits.UnitConversionOptions
 * 
 * @author Adam Andrews
 *
 */
public class CallUnitConversionOptions {

	private UnitConversionOptions options;
	
	public CallUnitConversionOptions(UnitConversionOptions options) {
		this.options = options;
	}
	
	public static CallUnitConversionOptions create(UnitDimensionConversion dimensionOption) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcUnits,UnitConversionOptions_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		UnitConversionOptions options = pfcUnits.UnitConversionOptions_Create(dimensionOption);
		return new CallUnitConversionOptions(options);
	}
	
	public void setDimensionOption(UnitDimensionConversion value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitConversionOptions,SetDimensionOption", 0, NitroConstants.DEBUG_JLINK_KEY);
		options.SetDimensionOption(value);
	}

	public void setIgnoreParamUnits(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitConversionOptions,SetIgnoreParamUnits", 0, NitroConstants.DEBUG_JLINK_KEY);
		options.SetIgnoreParamUnits(value);
	}

	public UnitConversionOptions getOptions() {
		return options;
	}
}
