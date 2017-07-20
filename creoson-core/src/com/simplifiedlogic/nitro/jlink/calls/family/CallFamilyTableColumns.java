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
import com.ptc.pfc.pfcFamily.FamilyTableColumn;
import com.ptc.pfc.pfcFamily.FamilyTableColumns;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcFamily.FamilyTableColumns
 * 
 * @author Adam Andrews
 *
 */
public class CallFamilyTableColumns {

	private FamilyTableColumns cols;
	
	public CallFamilyTableColumns(FamilyTableColumns cols) {
		this.cols = cols;
	}
	
	public static CallFamilyTableColumns create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyTableColumns,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        FamilyTableColumns cols = FamilyTableColumns.create();
		if (cols==null)
			return null;
		return new CallFamilyTableColumns(cols);
	}
	
	public void append(CallFamilyTableColumn col) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyTableColumns,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        cols.append(col.getCol());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyTableColumns,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return cols.getarraysize();
	}
	
	public CallFamilyTableColumn get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FamilyTableColumns,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        FamilyTableColumn col = cols.get(idx);
		if (col==null)
			return null;
		return new CallFamilyTableColumn(col);
	}

	public FamilyTableColumns getFamilyTableColumns() {
		return cols;
	}
}
