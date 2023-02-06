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
package com.simplifiedlogic.nitro.jlink.calls.dimension2d;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcDimension.BaseDimension;
import com.ptc.pfc.pfcDimension.DimTolerance;
import com.ptc.pfc.pfcDimension2D.Dimension2D;
import com.ptc.pfc.pfcView2D.View2D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.dimension.CallBaseDimension;
import com.simplifiedlogic.nitro.jlink.calls.dimension.CallDimTolerance;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDimension2D.Dimension2D
 * 
 * @author Adam Andrews
 *
 */
public class CallDimension2D extends CallBaseDimension {

	public CallDimension2D(Dimension2D dim) {
		super((BaseDimension)dim);
	}
	
	public CallView2D getView() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dimension2D,GetView", 0, NitroConstants.DEBUG_JLINK_KEY);
        View2D view = getDim().GetView();
        if (view==null)
        	return null;
        return new CallView2D(view);
	}
	
	public CallPoint3D getLocation() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dimension2D,GetLocation", 0, NitroConstants.DEBUG_JLINK_KEY);
        Point3D pt = getDim().GetLocation();
        if (pt==null)
        	return null;
        return new CallPoint3D(pt);
	}
	
	public CallDimTolerance getTolerance() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dimension2D,GetTolerance", 0, NitroConstants.DEBUG_JLINK_KEY);
        DimTolerance tol = getDim().GetTolerance();
        if (tol==null)
        	return null;
        return CallDimTolerance.create(tol);
	}
	
	public Dimension2D getDim() {
		return (Dimension2D)item;
	}
}
