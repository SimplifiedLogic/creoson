/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.solid;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSolid.Inertia;
import com.ptc.pfc.pfcSolid.MassProperty;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSolid.MassProperty
 * 
 * @author Adam Andrews
 *
 */
public class CallMassProperty {

	private MassProperty massprop;
	
	public CallMassProperty(MassProperty massprop) {
		this.massprop = massprop;
	}
	
	public double getVolume() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("MassProperty,GetVolume", 0, NitroConstants.DEBUG_JLINK_KEY);
		return massprop.GetVolume();
	}

	public double getMass() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("MassProperty,GetMass", 0, NitroConstants.DEBUG_JLINK_KEY);
		return massprop.GetMass();
	}

	public double getDensity() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("MassProperty,GetDensity", 0, NitroConstants.DEBUG_JLINK_KEY);
		return massprop.GetDensity();
	}

	public double getSurfaceArea() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("MassProperty,GetSurfaceArea", 0, NitroConstants.DEBUG_JLINK_KEY);
		return massprop.GetSurfaceArea();
	}

	public CallInertia getCenterGravityInertiaTensor() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("MassProperty,GetCenterGravityInertiaTensor", 0, NitroConstants.DEBUG_JLINK_KEY);
		Inertia inertia = massprop.GetCenterGravityInertiaTensor();
		if (inertia==null)
			return null;
		return new CallInertia(inertia);
	}

	public CallInertia getCoordSysInertia() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("MassProperty,GetCoordSysInertia", 0, NitroConstants.DEBUG_JLINK_KEY);
		Inertia inertia = massprop.GetCoordSysInertia();
		if (inertia==null)
			return null;
		return new CallInertia(inertia);
	}

	public CallInertia getCoordSysInertiaTensor() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("MassProperty,GetCoordSysInertiaTensor", 0, NitroConstants.DEBUG_JLINK_KEY);
		Inertia inertia = massprop.GetCoordSysInertiaTensor();
		if (inertia==null)
			return null;
		return new CallInertia(inertia);
	}

	public MassProperty getMassprop() {
		return massprop;
	}
}
