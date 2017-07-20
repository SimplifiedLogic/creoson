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
import com.ptc.pfc.pfcBase.UnitType;
import com.ptc.pfc.pfcUnits.Unit;
import com.ptc.pfc.pfcUnits.UnitSystem;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcUnits.UnitSystem
 * 
 * @author Adam Andrews
 *
 */
public class CallUnitSystem {

	private UnitSystem system;
	
	public CallUnitSystem(UnitSystem system) {
		this.system = system;
	}
	
	public String getName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitSystem,GetName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return system.GetName();
	}
	
	public CallUnit getUnit(UnitType type) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitSystem,GetUnit", 0, NitroConstants.DEBUG_JLINK_KEY);
		Unit unit = system.GetUnit(type);
		if (unit==null)
			return null;
		return new CallUnit(unit);
	}

	public UnitSystem getUnitSystem() {
		return system;
	}
}
