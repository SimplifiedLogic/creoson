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

import java.util.Vector;
import java.util.regex.Pattern;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameterOwner;
import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.impl.NitroUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * An implementation of ParamLooper which collects a list of data for
 * for parameters in a given model or feature.
 * 
 * @author Adam Andrews
 *
 */
public class ParamListLooper extends ParamLooper {
    /**
     * An output list of parameter data
     */
    public Vector<ParameterData> output = null;
    /**
     * Whether to return parameter values as byte arrays
     */
    public boolean encoded = false;
    /**
     * The model containing the parameters (which is different from the owner if listing feature parameters) 
     */
    public CallModel model;
    /**
     * The object that owns the parameters (a model or feature)
     */
    public CallParameterOwner owner;

	private String valuePattern;
	private Pattern valuePtn;
    
    /**
     * @param valuePattern The parameter value filter
     */
    public void setValuePattern(String valuePattern) {
    	if (valuePattern!=null) {
            this.valuePattern = NitroUtils.transformPattern(valuePattern.toLowerCase());
            this.valuePtn = Pattern.compile(this.valuePattern);
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.util.ParamLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter)
     */
    public boolean loopAction(CallParameter p) throws JLIException, jxthrowable {
        if (currentName==null)
            currentName = p.getName();
        
        // throw an error if a space is encountered
        if (currentName.trim().indexOf(' ')>=0) {
            throw new JLIException("Found a parameter name that contains a space:" + currentName);
        }
        
        if (output==null)
            output = new Vector<ParameterData>();

        ParameterData outvals = new ParameterData();
     	if (!JlinkUtils.getParamInfo(model, owner, p, outvals, encoded, valuePtn))
     		return false;
     	output.add(outvals);
     	
        return false;
    }
}
