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
package com.simplifiedlogic.nitro.jlink.calls.units;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcUnits.Units;
import com.ptc.pfc.pfcUnits.Unit;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcUnits.Unit
 * 
 * @author Adam Andrews
 *
 */
public class CallUnits {

	private Units units;
    private int currentIndex = 0;
	
	public CallUnits(Units units) {
		this.units = units;
	}

    public static CallUnits create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Units,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Units units = Units.create();
		if (units==null)
			return null;
		return new CallUnits(units);
	}

    public void append(CallUnit unit) throws jxthrowable {
        if(null == unit)
            return;
        insert(currentIndex, unit);
        currentIndex++;
	}

    public void insert(int index, CallUnit unit) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Units,insert", 0, NitroConstants.DEBUG_JLINK_KEY);
        if(null == unit)
            return;
        units.insert(index, unit.getUnit());
	}

	public Units getUnits() {
		return units;
	}

    public int getarraysize() throws jxthrowable{
        return units.getarraysize();
    }

    public CallUnit get(int i) throws jxthrowable{
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Units,Get", 0, NitroConstants.DEBUG_JLINK_KEY);
        Unit unit = units.get(i);
        return new CallUnit(unit);
    }
}
