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
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.rpc.JCException;
import com.simplifiedlogic.nitro.rpc.JCToolkitException;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Class holding native interface methods intended for use by JLFeature.
 */
public class JCFeature {

	public static void main(String[] args) {
		try {

			// TEST CODE!!

        	JCFeature mod = new JCFeature();

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

			// featids assume bracket-2.prt
//			mod.featuresSuppress("testing", "bracket-2.prt", new int[] {209, 368}, false);
//			mod.featuresSuppress("testing", "thick_bracket<bracket-2>.prt", new int[] {209, 368}, false);
//			mod.featuresSuppress("testing", null, new int[] {209}, false);

//			mod.featuresSuppress("testing", "bracket-2.prt", new int[] {368}, false);

//			mod.featuresResume("testing", "bracket-2.prt", new int[] {209});
//			mod.featuresResume("testing", "bracket-2.prt", new int[] {405});

//			mod.featuresDelete("testing", "bracket-2.prt", new int[] {405}, false);
//			mod.featuresDelete("testing", "bracket-2.prt", new int[] {209}, false);
//			mod.featuresDelete("testing", "bracket-2.prt", new int[] {209}, true);

			// featids assume feature_test.prt from unit tests
			mod.featuresSuppress("testing", "feature_test.prt", new int[] {657}, false);
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
		System.out.println("Done!");
	}

    /**
     * Set up and call the native function to suppress a list of features.
     * @param jliSession The JShell session
     * @param solid The Solid that owns the features
     * @param features The features to suppress 
     * @param clip Whether to clip children
     * @throws JLIException
     * @throws jxthrowable
     */
    public void doSuppress(AbstractJLISession jliSession, CallSolid solid, List<CallFeature> features, boolean clip) throws JLIException, jxthrowable {
    	
		JCGlobal.loadLibrary();
    	
    	List<Integer> ids = new ArrayList<Integer>();
    	for (CallFeature feat : features) {
    		int id = feat.getId();
    		ids.add(Integer.valueOf(id));
    	}
    	int[] arr = JCUtils.intListToArray(ids);

    	System.out.println("Calling native function: featureSuppress");
//    	System.out.println("\t"+jliSession.getSessionId());
//    	System.out.println("\t"+solid.getFileName());
//    	System.out.println("\t"+ids);
//    	System.out.println("\t"+clip);
    	featuresSuppress(jliSession.getSessionId(), solid.getFileName(), arr, clip);
    }

    /**
     * Set up and call the native function to resume a list of features.
     * @param jliSession The JShell session
     * @param solid The Solid that owns the features
     * @param features The features to resume 
     * @throws JLIException
     * @throws jxthrowable
     */
    public void doResume(AbstractJLISession jliSession, CallSolid solid, List<CallFeature> features) throws JLIException, jxthrowable {
    	
		JCGlobal.loadLibrary();
    	
    	List<Integer> ids = new ArrayList<Integer>();
    	for (CallFeature feat : features) {
    		int id = feat.getId();
    		ids.add(Integer.valueOf(id));
    	}
    	int[] arr = JCUtils.intListToArray(ids);

    	System.out.println("Calling native function: featureResume");
//    	System.out.println("\t"+jliSession.getSessionId());
//    	System.out.println("\t"+solid.getFileName());
//    	System.out.println("\t"+ids);
    	featuresResume(jliSession.getSessionId(), solid.getFileName(), arr);
    }

    /**
     * Set up and call the native function to delete a list of features.
     * @param jliSession The JShell session
     * @param solid The Solid that owns the features
     * @param features The features to delete 
     * @param clip Whether to clip children
     * @throws JLIException
     * @throws jxthrowable
     */
    public void doDelete(AbstractJLISession jliSession, CallSolid solid, List<CallFeature> features, boolean clip) throws JLIException, jxthrowable {
    	
		JCGlobal.loadLibrary();
    	
    	List<Integer> ids = new ArrayList<Integer>();
    	for (CallFeature feat : features) {
    		int id = feat.getId();
    		ids.add(Integer.valueOf(id));
    	}
    	int[] arr = JCUtils.intListToArray(ids);

    	System.out.println("Calling native function: featureDelete");
//    	System.out.println("\t"+jliSession.getSessionId());
//    	System.out.println("\t"+solid.getFileName());
//    	System.out.println("\t"+ids);
//    	System.out.println("\t"+clip);
    	featuresDelete(jliSession.getSessionId(), solid.getFileName(), arr, clip);
    }

    /**
     * Native function to suppress a list of feature IDs.
     * @param sessionId The JShell session ID
     * @param modelName Name of the model that owns the features
     * @param featureIds Array of feature IDs to suppress
     * @param clip Whether to clip the features
     */
    public native void featuresSuppress(String sessionId, String modelName, int[] featureIds, boolean clip);
    /**
     * Native function to resume a list of feature IDs.
     * @param sessionId The JShell session ID
     * @param modelName Name of the model that owns the features
     * @param featureIds Array of feature IDs to resume
     */
    public native void featuresResume(String sessionId, String modelName, int[] featureIds);
    /**
     * Native function to delete a list of feature IDs.
     * @param sessionId The JShell session ID
     * @param modelName Name of the model that owns the features
     * @param featureIds Array of feature IDs to delete
     * @param clip Whether to clip the features
     */
    public native void featuresDelete(String sessionId, String modelName, int[] featureIds, boolean clip);
    /**
     * @param debug Whether to turn on debuggin in the native code
     */
    public native void setDebug(boolean debug);
}
