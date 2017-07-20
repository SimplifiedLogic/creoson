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
package com.simplifiedlogic.nitro.jlink.calls.family;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFamily.FamilyMember;
import com.ptc.pfc.pfcFamily.FamilyTableColumn;
import com.ptc.pfc.pfcFamily.FamilyTableColumns;
import com.ptc.pfc.pfcFamily.FamilyTableRow;
import com.ptc.pfc.pfcFamily.FamilyTableRows;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModelItem.ParamValue;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValues;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcFamily.FamilyMember
 * 
 * @author Adam Andrews
 *
 */
public class CallFamilyMember extends CallModel {

	public CallFamilyMember(FamilyMember fmbr) {
		super((Model)fmbr);
	}

	public CallFamilyTableRows listRows() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,ListRows", 0, NitroConstants.DEBUG_JLINK_KEY);
		FamilyTableRows rows = getFmbr().ListRows();
		if (rows==null)
			return null;
		return new CallFamilyTableRows(rows);
	}
	
	public CallFamilyTableColumns listColumns() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,ListColumns", 0, NitroConstants.DEBUG_JLINK_KEY);
		FamilyTableColumns cols = getFmbr().ListColumns();
		if (cols==null)
			return null;
		return new CallFamilyTableColumns(cols);
	}
	
	public CallFamilyTableRow getRow(String instanceName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,GetRow", 0, NitroConstants.DEBUG_JLINK_KEY);
		FamilyTableRow row = getFmbr().GetRow(instanceName);
		if (row==null)
			return null;
		return new CallFamilyTableRow(row);
	}
	
	public CallFamilyTableColumn getColumn(String symbol) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,GetColumn", 0, NitroConstants.DEBUG_JLINK_KEY);
		FamilyTableColumn col = getFmbr().GetColumn(symbol);
		if (col==null)
			return null;
		return new CallFamilyTableColumn(col);
	}
	
	public CallParamValue getCell(CallFamilyTableColumn col, CallFamilyTableRow row) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,GetCell", 0, NitroConstants.DEBUG_JLINK_KEY);
		ParamValue pval = getFmbr().GetCell(col.getCol(), row.getRow());
		if (pval==null)
			return null;
		return new CallParamValue(pval);
	}
	
	public void setCell(CallFamilyTableColumn col, CallFamilyTableRow row, CallParamValue pval) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,SetCell", 0, NitroConstants.DEBUG_JLINK_KEY);
		getFmbr().SetCell(col.getCol(), row.getRow(), pval.getValue());
	}
	
	public CallFamilyTableRow addRow(String instanceName, CallParamValues pvals) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,AddRow", 0, NitroConstants.DEBUG_JLINK_KEY);
		FamilyTableRow row = getFmbr().AddRow(instanceName, pvals!=null ? pvals.getValues() : null);
		if (row==null)
			return null;
		return new CallFamilyTableRow(row);
	}
	
	public void removeRow(CallFamilyTableRow row) throws jxthrowable {
		if (row==null)
			return;
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,getRow", 0, NitroConstants.DEBUG_JLINK_KEY);
		getFmbr().RemoveRow(row.getRow());
	}
	
	public void removeColumn(CallFamilyTableColumn col) throws jxthrowable {
		if (col==null)
			return;
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,getCol", 0, NitroConstants.DEBUG_JLINK_KEY);
		getFmbr().RemoveColumn(col.getCol());
	}
	
	public CallModelDescriptor getImmediateGenericInfo() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyMember,GetImmediateGenericInfo", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelDescriptor descr = getFmbr().GetImmediateGenericInfo();
		if (descr==null)
			return null;
		return new CallModelDescriptor(descr);
	}
	
	public FamilyMember getFmbr() {
		return (FamilyMember)m;
	}
}
