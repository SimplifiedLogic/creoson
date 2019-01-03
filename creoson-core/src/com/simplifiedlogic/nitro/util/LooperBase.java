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
package com.simplifiedlogic.nitro.util;

import java.util.List;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.impl.NitroUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Base class for the looper classes.  Looper classes are designed to loop
 * through a list of Creo objects and call a handler function for each
 * object which matches various filters.
 * 
 * @author Adam Andrews
 *
 */
public abstract class LooperBase {

    /**
     * Name filter
     */
    protected String namePattern = null;
    /**
     * Whether the filter is a pattern (has wildcards)
     */
    protected boolean isNamePattern = false;
    /**
     * List of names or name patterns to filter by
     */
    protected String[] nameList = null;
    /**
     * List of flags indicating whether any of the nameList entries are patterns (have wildcards)
     */
    protected boolean[] isNameEntryPattern = null;
    /**
     * Key for debugging
     */
    protected String debugKey = null;

    /**
     * Name of the current object in the loop
     */
    protected String currentName = null;

    /**
     * See whether a string matches the name filter
     * @param name The string to check
     * @return Returns whether a string matches the name filter
     */
    protected boolean checkName(String name) {
    	return checkName(name, false);
    }

    /**
     * See whether a string matches the name filter
     * @param name The string to check
     * @param includeUnnamed Whether to allow the name to be null
     * @return Returns whether a string matches the name filter; if includeUnnamed is true, then the method will also return true if the name is null
     */
    protected boolean checkName(String name, boolean includeUnnamed) {
        if (name==null)
            return includeUnnamed;
        if (namePattern!=null) {
	        if (isNamePattern && !name.toLowerCase().matches(namePattern))
	            return false;
	        if (!isNamePattern && !name.equalsIgnoreCase(namePattern))
	            return false;
        }
        return true;
    }
    
    /**
     * See whether a string matches any name or pattern in nameList
     * @param name The string to check
     * @return Returns whether a string matches the nameList filters
     */
    protected boolean checkNameAgainstList(String name) {
    	return checkNameAgainstList(name, false);
    }

    /**
     * See whether a string matches any name or pattern in nameList
     * @param name The string to check
     * @param includeUnnamed Whether to allow the name to be null
     * @return Returns whether a string matches the nameList filters; if includeUnnamed is true, then the method will also return true if the name is null
     */
    protected boolean checkNameAgainstList(String name, boolean includeUnnamed) {
        if (nameList==null) 
            return true;
        if (name==null)
            return includeUnnamed;
        int len = nameList.length;
        // check all non-wildcard entries
        for (int i=0; i<len; i++) {
            if (!isNameEntryPattern[i] && name.equalsIgnoreCase(nameList[i]))
                return true;
        }
        // check all wildcard entries
        for (int i=0; i<len; i++) {
            if (isNameEntryPattern[i] && name.toLowerCase().matches(nameList[i]))
                return true;
        }
        return false;
    }
    
    /**
     * @return Returns the name filter
     */
    public String getNamePattern() {
        return namePattern;
    }

    /**
     * @param namePattern The name filter
     */
    public void setNamePattern(String namePattern) {
        if (namePattern!=null && namePattern.indexOf(' ')!=-1) {
            setNameList(namePattern.split(" "));
            return;
        }
        isNamePattern = NitroUtils.isPattern(namePattern);
        if (isNamePattern)
            this.namePattern = NitroUtils.transformPattern(namePattern.toLowerCase());
        else
            this.namePattern = namePattern;
    }

    /**
     * @return Returns the list of name filters
     */
    public String[] getNameList() {
        return nameList;
    }

    /**
     * @param nameList The list of name filters
     */
    public void setNameList(List nameList) {
        this.namePattern = null;
        this.isNamePattern = false;
        if (nameList!=null) {
            setNameList(nameList.toArray());
        }
        else {
            isNamePattern=false;
            this.nameList = null;
        }
    }
    
    /**
     * @param nameArray The list of name filters
     */
    public void setNameList(Object[] nameArray) {
        this.namePattern = null;
        this.isNamePattern = false;
        if (nameArray!=null) {
            int len = nameArray.length;
            this.nameList = new String[len];
            this.isNameEntryPattern = new boolean[len];
            String name;
            for (int i=0; i<len; i++) {
                name = nameArray[i].toString();
                if (NitroUtils.isPattern(name)) {
                    this.isNamePattern=true;
                    this.isNameEntryPattern[i] = true;
                    this.nameList[i] = NitroUtils.transformPattern(name.toLowerCase());
                }
                else {
                    this.nameList[i] = name;
                    this.isNameEntryPattern[i] = false;
                }
            }
        }
        else {
            isNamePattern=false;
            this.nameList = null;
        }
    }
    
    /**
     * Abstract function which is called for each object whose name matches the filters.
     * This must be implemented by a class extending this looper class, though
     * many implementers use a dummy method.  May be used to force a call to loopAction
     * for a specific name.
     * @param name The name of the object that matched the filters
     * @throws JLIException
     * @throws Exception
     */
    protected abstract void processObjectByName(String name) throws JLIException,jxthrowable;

    /**
     * @return Returns the isNamePattern.
     */
    public boolean getIsNamePattern() {
        return isNamePattern;
    }

    /**
     * @param isNamePattern The isNamePattern to set.
     */
    public void setIsNamePattern(boolean isNamePattern) {
        this.isNamePattern = isNamePattern;
    }

	public String getDebugKey() {
		return debugKey;
	}

	public void setDebugKey(String debugKey) {
		this.debugKey = debugKey;
	}
}
