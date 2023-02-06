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
package com.simplifiedlogic.nitro.jlink.calls.part;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcPart.Material;
import com.ptc.pfc.pfcPart.Materials;
import com.ptc.pfc.pfcPart.Part;
import com.ptc.pfc.pfcSolid.Solid;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcPart.Part
 * 
 * @author Adam Andrews
 *
 */
public class CallPart extends CallSolid {

	public CallPart(Part m) {
		super((Solid)m);
	}
	
	public CallMaterial getCurrentMaterial() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Part,GetCurrentMaterial", 0, NitroConstants.DEBUG_JLINK_KEY);
		Material matl = getPart().GetCurrentMaterial();
		if (matl==null)
			return null;
		return new CallMaterial(matl);
	}
	
	public CallMaterial getMaterial(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Part,GetMaterial", 0, NitroConstants.DEBUG_JLINK_KEY);
		Material matl = getPart().GetMaterial(name);
		if (matl==null)
			return null;
		return new CallMaterial(matl);
	}
	
	public CallMaterials listMaterials() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Part,ListMaterials", 0, NitroConstants.DEBUG_JLINK_KEY);
		Part part = getPart();
		Materials materials = part.ListMaterials();
		if (materials==null)
			return null;
		return new CallMaterials(materials);
	}
	
	public CallMaterial retrieveMaterial(String fileName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Part,RetrieveMaterial", 0, NitroConstants.DEBUG_JLINK_KEY);
		Material matl = getPart().RetrieveMaterial(fileName);
		if (matl==null)
			return null;
		return new CallMaterial(matl);
	}
	
	public void setCurrentMaterial(CallMaterial value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Part,SetCurrentMaterial", 0, NitroConstants.DEBUG_JLINK_KEY);
        Material matl=null;
        if (value!=null)
        	matl = value.getMaterial();
        getPart().SetCurrentMaterial(matl);
	}
	
	public Part getPart() {
		return (Part)m;
	}

}
