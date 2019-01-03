/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
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
import com.ptc.pfc.pfcBase.Matrix3D;
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcBase.Transform3D;
import com.ptc.pfc.pfcBase.Vector3D;
import com.ptc.pfc.pfcBase.pfcBase;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcBase.Transform3D
 * 
 * @author Adam Andrews
 *
 */
public class CallTransform3D {

	private Transform3D transform;
	
	public CallTransform3D(Transform3D transform) {
		this.transform = transform;
	}

	public CallMatrix3D getMatrix() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,GetMatrix", 0, NitroConstants.DEBUG_JLINK_KEY);
		Matrix3D mat = transform.GetMatrix();
		if (mat==null)
			return null;
		return new CallMatrix3D(mat);
	}
	
	public CallPoint3D getOrigin() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,GetOrigin", 0, NitroConstants.DEBUG_JLINK_KEY);
		Point3D p = transform.GetOrigin();
		if (p==null)
			return null;
		return new CallPoint3D(p);
	}

	public CallVector3D getXAxis() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,GetXAxis", 0, NitroConstants.DEBUG_JLINK_KEY);
		Vector3D v = transform.GetXAxis();
		if (v==null)
			return null;
		return new CallVector3D(v);
	}

	public CallVector3D getYAxis() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,GetYAxis", 0, NitroConstants.DEBUG_JLINK_KEY);
		Vector3D v = transform.GetYAxis();
		if (v==null)
			return null;
		return new CallVector3D(v);
	}

	public CallVector3D getZAxis() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,GetZAxis", 0, NitroConstants.DEBUG_JLINK_KEY);
		Vector3D v = transform.GetZAxis();
		if (v==null)
			return null;
		return new CallVector3D(v);
	}

	public static CallTransform3D create(CallMatrix3D matrix) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcBase,Transform3D_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
		Transform3D transform = pfcBase.Transform3D_Create(matrix.getMatrix());
		if (transform==null)
			return null;
		return new CallTransform3D(transform);
	}
	
	public void invert() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D.Invert", 0, NitroConstants.DEBUG_JLINK_KEY);
		transform.Invert();
	}
	
	public CallPoint3D transformPoint(CallPoint3D input) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,TransformPoint", 0, NitroConstants.DEBUG_JLINK_KEY);
		Point3D p = transform.TransformPoint(input.getPoint());
		if (p==null)
			return null;
		return new CallPoint3D(p);
	}

	public CallVector3D transformVector(CallVector3D input) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,TransformVector", 0, NitroConstants.DEBUG_JLINK_KEY);
        Vector3D v = transform.TransformVector(input.getVector());
		if (v==null)
			return null;
		return new CallVector3D(v);
	}
	
	public void setMatrix(CallMatrix3D value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Transform3D,SetMatrix", 0, NitroConstants.DEBUG_JLINK_KEY);
        if (value==null)
        	transform.SetMatrix(null);
        else
        	transform.SetMatrix(value.getMatrix());
	}

	public Transform3D getTransform() {
		return transform;
	}
}
