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
package com.simplifiedlogic.nitro.jshell.creo;

import java.util.List;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcModel.Model;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface;
import com.simplifiedlogic.nitro.rpc.JLIException;


/**
 * A Creo function interface for setting post-regen relations.  This JLink function did not
 * exist in WF4.
 * 
 * <p>Arguments expected:
 * <ol>
 * <li>The model for which to set the post-regen relations.  Must be of type 
 * {@link com.ptc.pfc.pfcModel.Model} or {@link CallModel}
 * 
 * <li>The relations to set, as a java.util.List<String> object.  One entry in the list per
 * line of relations.  If this value is null or empty, the post-regen relations will be 
 * deleted.
 * </ol>
 * @author Adam Andrews
 *
 */
public class SetPostRegenRelations implements CreoFunctionInterface {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface#execute(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Object execute(Object[] args) throws JLIException,jxthrowable {
		Model m = null;
		if (args[0] instanceof CallModel)
			m = ((CallModel)args[0]).getModel();
		else 
			m = (Model)args[0];
		List<String> relations  = (List<String>)args[1];

        if (relations==null || relations.size()==0) {
        	m.DeletePostRegenerationRelations();
        	System.out.println("Deleted relations");
        }
        else {
	        stringseq lines = stringseq.create();
        	int len = relations.size();
        	for (int i=0; i<len; i++) 
        		lines.append(relations.get(i));

        	m.SetPostRegenerationRelations(lines);
        	System.out.println("Set relations");
        }

        return null;
	}
}
