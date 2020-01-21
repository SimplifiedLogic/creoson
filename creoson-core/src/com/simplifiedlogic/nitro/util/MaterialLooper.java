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
import com.simplifiedlogic.nitro.jlink.calls.part.CallMaterial;
import com.simplifiedlogic.nitro.jlink.calls.part.CallMaterials;
import com.simplifiedlogic.nitro.jlink.calls.part.CallPart;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over materials on a part, 
 * filters the results by various filters, and calls an action method
 * for each material which matches the filters.
 *  
 * @author Adam Andrews
 */
public abstract class MaterialLooper extends LooperBase {

    /**
     * The name of the current material in the loop
     */
    protected String currentName = null;

    /**
     * Loop through materials on a Part
     * @param part The part which owns the materials
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop(CallPart part) throws JLIException,jxthrowable {
        // special case -- exact feature name given
        if (namePattern!=null && !isNamePattern) {
            currentName = null;
            CallMaterial matl = part.getMaterial(namePattern);
            if (matl!=null) {
                loopAction(matl);
                return;
            }
        }

        // list the solid's features by feature type
        CallMaterials matlList = part.listMaterials();
        if (matlList==null)
        	return;
        processLoop(matlList);
    }
    
    /**
     * Loop through the feature list
     * @param matlList The list of materials
     * @throws JLIException
     * @throws jxthrowable
     */
    private void processLoop(CallMaterials matlList) throws JLIException, jxthrowable {
        if (matlList!=null) {
            int len = matlList.getarraysize();
            CallMaterial matl = null;
            for (int i=0; i<len; i++) {
                currentName = null;
                matl = matlList.get(i);

                currentName = matl.getName();

                if (!checkName(currentName) || !checkNameAgainstList(currentName))
                    continue;

                loopAction(matl);
            }
        }
    }
    
    /**
     * Abstract function which is called for each material which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param matl The material which matched the filters
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract void loopAction(CallMaterial matl) throws JLIException, jxthrowable;

    @Override
	protected void processObjectByName(String name) throws JLIException, jxthrowable {
	}

}
