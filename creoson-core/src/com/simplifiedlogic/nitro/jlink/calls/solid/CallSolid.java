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
package com.simplifiedlogic.nitro.jlink.calls.solid;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.Outline3D;
import com.ptc.pfc.pfcFamily.FamilyMember;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcFeature.FeatureType;
import com.ptc.pfc.pfcFeature.Features;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcSimpRep.SimpRep;
import com.ptc.pfc.pfcSolid.MassProperty;
import com.ptc.pfc.pfcSolid.Solid;
import com.ptc.pfc.pfcUnits.UnitSystem;
import com.ptc.pfc.pfcUnits.UnitSystems;
import com.simplifiedlogic.nitro.jlink.calls.base.CallOutline3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyMember;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatureOperations;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatures;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItemTypes;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRep;
import com.simplifiedlogic.nitro.jlink.calls.units.CallUnitConversionOptions;
import com.simplifiedlogic.nitro.jlink.calls.units.CallUnitSystem;
import com.simplifiedlogic.nitro.jlink.calls.units.CallUnitSystems;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSolid.Solid
 * 
 * @author Adam Andrews
 *
 */
public class CallSolid extends CallFamilyMember {

	public CallSolid(Solid m) {
		super((FamilyMember)m);
	}
	
	public CallFeatures listFeaturesByType(boolean visibleOnly, FeatureType type) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,ListFeaturesByType", 0, NitroConstants.DEBUG_JLINK_KEY);
		Solid solid = getSolid();
		Features feats = solid.ListFeaturesByType(visibleOnly, type);
		if (feats==null)
			return null;
		return new CallFeatures(feats);
	}
	
	public boolean getIsSkeleton() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetIsSkeleton", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getSolid().GetIsSkeleton();
	}
	
	public void executeFeatureOps(CallFeatureOperations ops, CallRegenInstructions instrs) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,ExecuteFeatureOps", 0, NitroConstants.DEBUG_JLINK_KEY);
		getSolid().ExecuteFeatureOps(ops.getFeatureOperations(), instrs!=null ? instrs.getInstr() : null);
	}
	
	public CallFeature getFeatureById(int id) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetFeatureById", 0, NitroConstants.DEBUG_JLINK_KEY);
		Feature feat = getSolid().GetFeatureById(id);
		return CallFeature.createFeature(feat);
	}
	
	public CallFeature getFeatureByName(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetFeatureByName", 0, NitroConstants.DEBUG_JLINK_KEY);
		Feature feat = getSolid().GetFeatureByName(name);
		return CallFeature.createFeature(feat);
	}
	
	public CallOutline3D getGeomOutline() throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetGeomOutline", 0, NitroConstants.DEBUG_JLINK_KEY);
		Outline3D outline = getSolid().GetGeomOutline();
		if (outline==null)
			return null;
		return new CallOutline3D(outline);
	}

	public CallOutline3D evalOutline(CallTransform3D transform, CallModelItemTypes types) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,EvalOutline", 0, NitroConstants.DEBUG_JLINK_KEY);
		Outline3D outline = getSolid().EvalOutline(
				transform!=null ? transform.getTransform() : null,
				types!=null ? types.getItemTypes() : null);
		if (outline==null)
			return null;
		return new CallOutline3D(outline);
	}

	public CallMassProperty getMassProperty(String coordSysName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetMassProperty", 0, NitroConstants.DEBUG_JLINK_KEY);
		MassProperty massprop = getSolid().GetMassProperty(coordSysName);
		if (massprop==null)
			return null;
		return new CallMassProperty(massprop);
	}
	
	public CallUnitSystem getPrincipalUnits() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetPrincipalUnits", 0, NitroConstants.DEBUG_JLINK_KEY);
		UnitSystem system = getSolid().GetPrincipalUnits();
		if (system==null)
			return null;
		return new CallUnitSystem(system);
	}
	
	public void setPrincipalUnits(CallUnitSystem units, CallUnitConversionOptions options) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetPrincipalUnits", 0, NitroConstants.DEBUG_JLINK_KEY);
		getSolid().SetPrincipalUnits(units.getUnitSystem(), options.getOptions());
	}
	
	public CallUnitSystems listUnitSystems() throws jxthrowable {
		UnitSystems systems = getSolid().ListUnitSystems();
		if (systems==null)
			return null;
		return new CallUnitSystems(systems);
	}
	
	public void regenerate(CallRegenInstructions instrs) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,Regenerate", 0, NitroConstants.DEBUG_JLINK_KEY);
		getSolid().Regenerate(instrs!=null ? instrs.getInstr() : null);
	}
	
	public CallModelItem createNote(CallStringSeq lines, CallModelItem owner) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,CreateNote", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelItem item = getSolid().CreateNote(lines!=null ? lines.getSeq() : null, owner!=null ? owner.getItem() : null);
		if (item==null)
			return null;
		return CallModelItem.create(item);
	}
	
	public CallSimpRep getActiveSimpRep() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetActiveSimpRep", 0, NitroConstants.DEBUG_JLINK_KEY);
		SimpRep rep = getSolid().GetActiveSimpRep();
		if (rep==null)
			return null;
		return new CallSimpRep(rep);
	}
	
	public CallSimpRep getSimpRep(String simpRepName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,GetSimpRep", 0, NitroConstants.DEBUG_JLINK_KEY);
		SimpRep rep = getSolid().GetSimpRep(simpRepName);
		if (rep==null)
			return null;
		return new CallSimpRep(rep);
	}

	public boolean hasRetrievalErrors() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Solid,HasRetrievalErrors", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getSolid().HasRetrievalErrors();
	}

	public Solid getSolid() {
		return (Solid)m;
	}
}
