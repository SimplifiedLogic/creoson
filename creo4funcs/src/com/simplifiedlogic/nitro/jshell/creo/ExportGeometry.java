/**
 * 
 */
package com.simplifiedlogic.nitro.jshell.creo;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.ExportType;
import com.ptc.pfc.pfcModel.Model;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * @author Adama
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
