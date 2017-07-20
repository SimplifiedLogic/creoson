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
package com.simplifiedlogic.nitro.util;

import java.util.List;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameterOwner;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameters;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.impl.NitroUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over parameters on an object, 
 * filters the results by various filters, and calls an action method
 * for each parameter which matches the filters.
 *  
 * @author Adam Andrews
 */
public abstract class ParamLooper {

    private String namePattern = null;
    private String[] nameList = null;
    private boolean isNamePattern = false;
    
    /**
     * The name of the current parameter in the loop
     */
    protected String currentName = null;
    
    /**
     * Loop through parameters on an object
     * @param m The parameter owner
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop(CallParameterOwner m) throws JLIException,jxthrowable {
        if (namePattern!=null && !isNamePattern) {
            currentName = null;
            CallParameter p = m.getParam(namePattern);
            if (p!=null)
            	loopAction(p);
            return;
        }
        
        if (nameList!=null && !isNamePattern) {
        	CallParameter p;
        	for (int i=0; i<nameList.length; i++) {
                currentName = null;
                p = m.getParam(nameList[i]);
                if (p!=null) {
                	boolean abort = loopAction(p);
                    if (abort)
                        break;
                }
        	}
        	return;
        }

        CallParameters params = m.listParams();
    	if (params==null)
    		return;
        int sz = params.getarraysize();
        CallParameter p;
        for (int i=0; i<sz; i++) {
            currentName = null;
            p = params.get(i);
            if (p==null) continue;
            try {
                if (!checkName(p))
                    continue;

                boolean abort = loopAction(p);
                if (abort)
                    break;
            }
            catch (jxthrowable jxe) {
                throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving parameter " + p.getName()), jxe);
            }
        }
    }

    /**
     * Abstract function which is called for each parameter which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param p The parameter that matches the filters
     * @return True to abort the loop, false to continue it
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract boolean loopAction(CallParameter p) throws JLIException, jxthrowable;

    /**
     * See whether the parameter matches the name filter
     * @param p The parameter to check
     * @return Whether the parameter matches the name filter
     * @throws jxthrowable
     */
    private boolean checkName(CallParameter p) throws jxthrowable {
        if (namePattern!=null) {
            currentName = p.getName();
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
     * @return The name filter
     */
    public String getNamePattern() {
        return namePattern;
    }

    /**
     * @param namePattern The name filter
     */
    public void setNamePattern(String namePattern) {
    	if (namePattern!=null && namePattern.indexOf(' ')!=-1) {
    		this.namePattern = null;
    		this.isNamePattern = false;
    		this.nameList = namePattern.split(" ");
			int len = this.nameList.length;
			for (int i=0; i<len; i++) {
				if (NitroUtils.isPattern(this.nameList[i])) {
					this.isNamePattern=true;
					this.nameList[i] = NitroUtils.transformPattern(this.nameList[i].toLowerCase());
				}
			}
			return;
    	}
        isNamePattern = NitroUtils.isPattern(namePattern);
        if (isNamePattern)
            this.namePattern = NitroUtils.transformPattern(namePattern.toLowerCase());
        else
            this.namePattern = namePattern;
    }

	/**
	 * @return The list of name filters
	 */
	public String[] getNameList() {
		return nameList;
	}

	/**
	 * @param nameList The list of name filters
	 */
	public void setNameList(List<String> nameList) {
		this.namePattern = null;
		this.isNamePattern = false;
		if (nameList!=null) {
			int len = nameList.size();
			this.nameList = new String[len];
			String name;
			for (int i=0; i<len; i++) {
				name = nameList.get(i);
				if (NitroUtils.isPattern(name)) {
					this.isNamePattern=true;
					this.nameList[i] = NitroUtils.transformPattern(name.toLowerCase());
				}
				else
					this.nameList[i] = name;
			}
		}
		else {
			isNamePattern=false;
			this.nameList = null;
		}
	}

}
