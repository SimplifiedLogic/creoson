/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.ExportType;
import com.ptc.pfc.pfcModel.Model;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * A Creo function interface for exporting geometry to a file using JLink functions 
 * that that were introduced in Creo 4.0.
 * 
 * <p>Arguments expected:
 * <ol>
 * <li>The model for which to set the post-regen relations.  Must be of type 
 * {@link com.ptc.pfc.pfcModel.Model} or {@link CallModel}
 * 
 * <li>The name of the export file as a java.lang.String.  The file name may include a path.
 * 
 * <li>The export type.  Must be of type 
 * {@link com.ptc.pfc.pfcModel.ExportType}
 *
 * <li>The name of the export profile as a java.lang.String.  The file name may include a path. (optional)
 * 
 * </ol>
 * @author Adam Andrews
 *
 */
public class ExportGeometry implements CreoFunctionInterface {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] args) throws JLIException, jxthrowable {
		Model m = null;
		if (args[0] instanceof CallModel)
			m = ((CallModel)args[0]).getModel();
		else 
			m = (Model)args[0];
		String newFilename = (String)args[1];
		ExportType exportType = (ExportType)args[2];
		String profileFile = null;
		if (args.length>3) 
			profileFile = (String)args[3];

		System.out.println("Exporting using ExportIntf3D, config opt="+profileFile);
		m.ExportIntf3D(newFilename, exportType, null); // profile argument is currently not supported

		return null;
	}

}
