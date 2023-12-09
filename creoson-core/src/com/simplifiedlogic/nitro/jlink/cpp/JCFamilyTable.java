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
package com.simplifiedlogic.nitro.jlink.cpp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentFeat;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.rpc.JCException;
import com.simplifiedlogic.nitro.rpc.JCToolkitException;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Class holding native interface methods intended for use by JLFamilyTable.
 */
public class JCFamilyTable {

	public static void main(String[] args) {
		try {

			// TEST CODE!!

        	JCFamilyTable mod = new JCFamilyTable();

        	File lib = new File("../com.simplifiedlogic.nitro.jshell.osgi/src/jshellnative.dll");
        	if (!lib.exists()) {
        		System.err.println("Library not found: "+lib.getAbsolutePath());
        		return;
        	}
        	JCGlobal.setLibraryFile(lib);
			JCGlobal.loadLibrary();
			//String connectionId = JCConnection.connect();

			mod.setDebug(true);
			JCConnection.setDebug(true);

			// compids assume plate_assy.asm
			mod.replaceComponent("testing", "plate_assy.asm", new int[] {49, 53}, "long_round2<screw>.prt");
		}
		catch (Throwable e) {
			if (e instanceof JCException) {
				if (e instanceof JCToolkitException) {
					JCToolkitException tke = (JCToolkitException)e;
					System.err.println("Toolkit error: "+tke.getToolkitName()+" ("+tke.getErrorCode()+")");
				}
				else
					System.err.println(e.getMessage());
				JLIException jlx = JCUtils.createException((JCException) e);
				System.err.println("JLIException: "+jlx.getMessage());
			}
			else
				e.printStackTrace();
		}
	}

    /**
     * Set up and call the native function to replace a familytable component with another one in an assembly.
     * @param jliSession The JShell session
     * @param wrapping_solid The parent assembly of the components being replaced
     * @param components List of component objects to replace
     * @param new_solid The new object to replace the components with
     * @throws JLIException
     * @throws jxthrowable
     */
    public void doReplace(AbstractJLISession jliSession, CallSolid wrapping_solid, List<CallComponentFeat> components, CallSolid new_solid) throws JLIException, jxthrowable {
    	
    	JCGlobal.loadLibrary();

    	List<Integer> ids = new ArrayList<Integer>();
    	for (CallComponentFeat feat : components) {
    		int id = feat.getId();
    		ids.add(Integer.valueOf(id));
    	}
    	int[] arr = JCUtils.intListToArray(ids);

    	System.out.println("Calling native function: replaceComponent");
    	replaceComponent(jliSession.getSessionId(), wrapping_solid.getFileName(), arr, new_solid.getFileName());
    }

    /**
     * Native function to replace a component.
     * @param sessionId
     * @param parentAssemblyName
     * @param compIds
     * @param newSolidName
     */
    public native void replaceComponent(String sessionId, String parentAssemblyName, int[] compIds, String newSolidName);
    /**
     * @param debug
     */
    public native void setDebug(boolean debug);
}
