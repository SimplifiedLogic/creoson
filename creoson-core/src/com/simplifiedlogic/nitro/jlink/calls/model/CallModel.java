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
package com.simplifiedlogic.nitro.jlink.calls.model;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcAssembly.Assembly;
import com.ptc.pfc.pfcDrawing.Drawing;
import com.ptc.pfc.pfcLayer.Layer;
import com.ptc.pfc.pfcModel.Dependencies;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel2D.Model2D;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcModelItem.ModelItems;
import com.ptc.pfc.pfcModelItem.Parameter;
import com.ptc.pfc.pfcModelItem.Parameters;
import com.ptc.pfc.pfcPart.Part;
import com.ptc.pfc.pfcSolid.Solid;
import com.ptc.pfc.pfcView.View;
import com.ptc.pfc.pfcView.Views;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallAssembly;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawing;
import com.simplifiedlogic.nitro.jlink.calls.layer.CallLayer;
import com.simplifiedlogic.nitro.jlink.calls.model2d.CallModel2D;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItems;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameterOwner;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameters;
import com.simplifiedlogic.nitro.jlink.calls.part.CallPart;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.calls.view.CallView;
import com.simplifiedlogic.nitro.jlink.calls.view.CallViews;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModel.Model
 * 
 * @author Adam Andrews
 *
 */
public class CallModel implements CallParameterOwner {

	protected Model m;

	public CallModel(Model m) {
		this.m = m;
	}

	public int getType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetType", 0, NitroConstants.DEBUG_JLINK_KEY);
		return m.GetType().getValue();
	}
	
	public String getFileName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetFileName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return m.GetFileName();
	}
	
	public String getGenericName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetGenericName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return m.GetGenericName();
	}
	
	public String getFullName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetFullName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return m.GetFullName();
	}
	
	public String getInstanceName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetInstanceName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return m.GetInstanceName();
	}

	public String getCommonName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetCommonName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return m.GetCommonName();
	}

	public void setCommonName(String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,SetCommonName", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.SetCommonName(value);
	}

	public boolean isCommonNameModifiable() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,IsCommonNameModifiable", 0, NitroConstants.DEBUG_JLINK_KEY);
		return m.IsCommonNameModifiable();
	}

	public CallModelItem getItemById(ModelItemType type, int id) throws jxthrowable {
		ModelItem item = null;
        try {
            if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetItemById", 0, NitroConstants.DEBUG_JLINK_KEY);
            item = m.GetItemById(type, id);
        } catch (Exception e) {
            // if an item does not exist with the ID, an exception is thrown
            return null;
        }
        if (item==null)
        	return null;
        return CallModelItem.create(item);
	}
	
	public CallModelItem getItemByName(ModelItemType type, String name) throws jxthrowable {
		ModelItem item = null;
        try {
            if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetItemByName", 0, NitroConstants.DEBUG_JLINK_KEY);
            item = m.GetItemByName(type, name);
        } catch (Exception e) {
            // if an item does not exist with the name, is an exception thrown?
            return null;
        }
        if (item==null)
        	return null;
        return CallModelItem.create(item);
	}
	
	@Override
	public CallParameter getParam(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetParam", 0, NitroConstants.DEBUG_JLINK_KEY);
		Parameter param = m.GetParam(name);
		if (param==null)
			return null;
		return new CallParameter(param);
	}
	
	public Integer getRelationId() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetRelationId", 0, NitroConstants.DEBUG_JLINK_KEY);
		Integer id = m.GetRelationId();
		return id;
	}
	
	public void erase() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,Erase", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.Erase();
	}
	
	public void eraseWithDependencies() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,EraseWithDependencies", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.EraseWithDependencies();
	}
	
	public CallModelDescriptor getDescr() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetDescr", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelDescriptor descr = m.GetDescr();
		if (descr==null)
			return null;
		return new CallModelDescriptor(descr);
	}
	
	public void rename(String newName, Boolean renameFilesToo) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,Rename", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.Rename(newName, renameFilesToo);
	}
	
	public void backup(CallModelDescriptor whereTo) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,Backup", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.Backup(whereTo.getDesc());
	}
	
	public void save() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,Save", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.Save();
	}
	
	public void export(String fileName, CallExportInstructions instr) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,Export", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.Export(fileName, instr!=null ? instr.getInstr() : null);
	}
	
	public void importData(String fileName, CallImportInstructions instr) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,Import", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.Import(fileName, instr!=null ? instr.getInstr() : null);
	}
	
	public void display() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,Display", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.Display();
	}
	
	public CallStringSeq getRelations() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetRelations", 0, NitroConstants.DEBUG_JLINK_KEY);
		stringseq rels = m.GetRelations();
		if (rels==null)
			return null;
		return new CallStringSeq(rels);
	}
	
	public CallStringSeq getPostRegenerationRelations() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetPostRegenerationRelations", 0, NitroConstants.DEBUG_JLINK_KEY);
		stringseq rels = m.GetPostRegenerationRelations();
		if (rels==null)
			return null;
		return new CallStringSeq(rels);
	}
	
	public void deleteRelations() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,DeleteRelations", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.DeleteRelations();
	}

	public void setRelations(CallStringSeq seq) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,SetRelations", 0, NitroConstants.DEBUG_JLINK_KEY);
		m.SetRelations(seq!=null ? seq.getSeq() : null);
	}
	
	public CallModelItems listItems(ModelItemType type) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,ListItems", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelItems items = m.ListItems(type);
		if (items==null)
			return null;
		return new CallModelItems(items);
	}
	
	public CallDependencies listDependencies() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,ListDependencies", 0, NitroConstants.DEBUG_JLINK_KEY);
        Dependencies deps = m.ListDependencies();
		if (deps==null)
			return null;
		return new CallDependencies(deps);
	}
	
	@Override
	public CallParameters listParams() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,ListParams", 0, NitroConstants.DEBUG_JLINK_KEY);
        Parameters params = m.ListParams();
		if (params==null)
			return null;
		return new CallParameters(params);
	}
	
	@Override
	public CallParameter createParam(String name, CallParamValue value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,CreateParam", 0, NitroConstants.DEBUG_JLINK_KEY);
		Parameter param = m.CreateParam(name, value!=null ? value.getValue() : null);
		if (param==null)
			return null;
		return new CallParameter(param);
	}

	public CallViews listViews() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,ListViews", 0, NitroConstants.DEBUG_JLINK_KEY);
		Views views = getModel().ListViews();
		if (views==null)
			return null;
		return new CallViews(views);
	}
	
	public CallView getView(String viewName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,GetView", 0, NitroConstants.DEBUG_JLINK_KEY);
		View view = getModel().GetView(viewName);
		if (view==null)
			return null;
		return new CallView(view);
	}
	
	public CallView retrieveView(String viewName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,RetrieveView", 0, NitroConstants.DEBUG_JLINK_KEY);
		View view = getModel().RetrieveView(viewName);
		if (view==null)
			return null;
		return new CallView(view);
	}
	
	public CallView saveView(String viewName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,SaveView", 0, NitroConstants.DEBUG_JLINK_KEY);
		View view = getModel().SaveView(viewName);
		if (view==null)
			return null;
		return new CallView(view);
	}
	
	public CallLayer createLayer(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Model,CreateLayer", 0, NitroConstants.DEBUG_JLINK_KEY);
		Layer layer = getModel().CreateLayer(name);
		if (layer==null)
			return null;
		return new CallLayer(layer);
	}
	
	public static CallModel create(Model m) {
		if (m==null)
			return null;
		if (m instanceof Assembly)
			return new CallAssembly((Assembly)m);
		else if (m instanceof Part)
			return new CallPart((Part)m);
		else if (m instanceof Solid)
			return new CallSolid((Solid)m);
		else if (m instanceof Drawing)
			return new CallDrawing((Drawing)m);
		else if (m instanceof Model2D)
			return new CallModel2D((Model2D)m);
		else
			return new CallModel(m);
	}
	
	public Model getModel() {
		return m;
	}

}
