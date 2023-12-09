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

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallAssembly;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Class holding native interface methods intended for use by JLFile.
 */
public class JCFile {

    /**
     * Set up and call the native function to suppress a new component added to an assembly.
     * @param jliSession
     * @param assembly
     * @param newfeat
     * @throws JLIException
     * @throws jxthrowable
     */
    public void suppressNewFeature(AbstractJLISession jliSession, CallAssembly assembly, CallFeature newfeat) throws JLIException, jxthrowable {
    	
		JCGlobal.loadLibrary();
    	
   		int id = newfeat.getId();

    	System.out.println("Calling native function: featureSuppress");
//    	System.out.println("\t"+jliSession.getSessionId());
//    	System.out.println("\t"+assembly.getFileName());
//    	System.out.println("\t"+id);
    	featureSuppress(jliSession.getSessionId(), assembly.getFileName(), id);
    }

    /**
     * Native function to suppress a new component added to an assembly.
     * @param sessionId The JShell session ID
     * @param modelName Name of the model that owns the feature
     * @param featureId Feature ID to suppress
     */
    public native void featureSuppress(String sessionId, String modelName, int featureId);
    /**
     * @param debug Whether to turn on debuggin in the native code
     */
    public native void setDebug(boolean debug);
}
