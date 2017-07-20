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
package com.simplifiedlogic.nitro.jlink.calls.export;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExport.pfcExport;
import com.ptc.pfc.pfcExport.GeometryFlags;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcExport.GeometryFlags
 * 
 * @author Adam Andrews
 *
 */
public class CallGeometryFlags {

	private GeometryFlags flags;
	
	public CallGeometryFlags(GeometryFlags flags) {
		this.flags = flags;
	}
	
	public void setAsSolids(boolean value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("GeometryFlags,SetAsSolids", 0, NitroConstants.DEBUG_JLINK_KEY);
		flags.SetAsSolids(value);
	}

	public static CallGeometryFlags create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcExport,GeometryFlags_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		GeometryFlags flags = pfcExport.GeometryFlags_Create();
		if (flags==null)
			return null;
		return new CallGeometryFlags(flags);
	}
	
	public GeometryFlags getFlags() {
		return flags;
	}
}
