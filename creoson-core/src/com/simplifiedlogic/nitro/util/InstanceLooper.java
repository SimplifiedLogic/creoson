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
package com.simplifiedlogic.nitro.util;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyMember;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableColumns;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableRow;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableRows;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.impl.NitroUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over instances in a family table, 
 * filters the results by various filters, and calls an action method
 * for each instance which matches the filters.
 *  
 * @author Adam Andrews
 */
public abstract class InstanceLooper {

    private String namePattern = null;
    private boolean getColumns = false;

    private boolean isNamePattern = false;
    protected String currentName = null;
    
    /**
     * Loop through instances in a model
     * @param fmbr The model which owns the instances
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop(CallFamilyMember fmbr) throws JLIException,jxthrowable {
    	if (fmbr==null)
    		return;
    	
    	CallFamilyTableColumns cols = null;
    	if (isGetColumns())
    		cols = fmbr.listColumns();
    	
        if (namePattern!=null && !isNamePattern) {
            currentName = null;
            CallFamilyTableRow row = fmbr.getRow(namePattern);
            if (row!=null)
            	loopAction(row, cols);
            return;
        }
        
        CallFamilyTableRows rows = fmbr.listRows();
    	if (rows==null)
    		return;
        int sz = rows.getarraysize();
        CallFamilyTableRow onerow;
        for (int i=0; i<sz; i++) {
            currentName = null;
            onerow = rows.get(i);
            if (onerow==null) continue;
            try {
                if (!checkName(onerow))
                    continue;

                boolean abort = loopAction(onerow, cols);
                if (abort)
                    break;
            }
            catch (jxthrowable jxe) {
                throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving instance " + onerow.getInstanceName()), jxe);
            }
        }
    }

    /**
     * Abstract function which is called for each instance which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param onerow The family table row for the instance
     * @param cols A list of column definitions for the row
     * @return True to abort the loop, false to continue it
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract boolean loopAction(CallFamilyTableRow onerow, CallFamilyTableColumns cols) throws JLIException, jxthrowable;

    /**
     * See whether the instance matches the name filter
     * @param onerow The family table row for the instance
     * @return Whether the instance matches the name filter
     * @throws jxthrowable
     */
    private boolean checkName(CallFamilyTableRow onerow) throws jxthrowable {
        if (namePattern!=null) {
            currentName = onerow.getInstanceName();
            if (currentName==null)
                return false;
            if (isNamePattern && !currentName.toLowerCase().matches(namePattern))
                return false;
            if (!isNamePattern && !currentName.equalsIgnoreCase(namePattern))
                return false;
        }
        return true;
    }
    
    /**
     * @return The instance name filter
     */
    public String getNamePattern() {
        return namePattern;
    }

    /**
     * @param namePattern The instance name filter
     */
    public void setNamePattern(String namePattern) {
        isNamePattern = NitroUtils.isPattern(namePattern);
        if (isNamePattern)
            this.namePattern = NitroUtils.transformPattern(namePattern.toLowerCase());
        else
            this.namePattern = namePattern;
    }

	/**
	 * @return Whether to pass the column data to loopAction
	 */
	public boolean isGetColumns() {
		return getColumns;
	}

	/**
	 * @param getColumns Whether to pass the column data to loopAction
	 */
	public void setGetColumns(boolean getColumns) {
		this.getColumns = getColumns;
	}

}
