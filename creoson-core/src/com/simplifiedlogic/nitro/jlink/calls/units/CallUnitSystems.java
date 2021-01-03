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
package com.simplifiedlogic.nitro.jlink.calls.units;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUnits.UnitSystem;
import com.ptc.pfc.pfcUnits.UnitSystems;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcUnits.UnitSystems
 * 
 * @author Adam Andrews
 *
 */
public class CallUnitSystems {

	private UnitSystems systems;
	
	public CallUnitSystems(UnitSystems systems) {
		this.systems = systems;
	}
	
	public static CallUnitSystems create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitSystems,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        UnitSystems systems = UnitSystems.create();
		if (systems==null)
			return null;
		return new CallUnitSystems(systems);
	}
	
	public void append(CallUnitSystem system) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitSystems,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        systems.append(system.getUnitSystem());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitSystems,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return systems.getarraysize();
	}
	
	public CallUnitSystem get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("UnitSystems,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        UnitSystem unit = systems.get(idx);
        return new CallUnitSystem(unit);
	}

	public UnitSystems getSystems() {
		return systems;
	}
}
