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
import com.ptc.pfc.pfcExceptions.XToolkitCantOpen;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModels;
import com.simplifiedlogic.nitro.jlink.calls.model2d.CallModel2D;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over models that are either
 * a) in memory or b) in a drawing, 
 * filters the results by various filters, and calls an action method
 * for each model which matches the filters.
 *  
 * @author Adam Andrews
 */
public abstract class ModelLooper extends LooperBase {

    private CallSession session = null;
    private CallModel2D drawing = null;
    
    private boolean defaultToActive = false;

    /**
     * Output flag indicating that only a single model was specified in the name filter
     */
    public boolean singleOp = false;
    
    public void loop() throws JLIException,jxthrowable {
    	if (drawing==null) {
	        if ((namePattern==null && defaultToActive) || (namePattern!=null && !isNamePattern)) {
	            currentName = null;
	            singleOp = true;
	            processObjectByName(namePattern);
	            return;
	        }
    	}

        singleOp = false;
        if (nameList!=null && !isNamePattern) {
            for (int i=0; i<nameList.length; i++) {
                currentName = null;
                processObjectByName(nameList[i]);
            }
            return;
        }

        long start;
        start = System.currentTimeMillis();
        CallModels models = null;
        if (drawing!=null) {
        	// get the models in the drawing
        	models = drawing.listModels();
        }
        else if (session!=null) {
        	// get the models in memory
        	models = session.listModels();
        }
        if (debugKey!=null)
        	DebugLogging.sendTimerMessage("jlink.ListModels", start, debugKey);
        if (models==null)
        	return;
        int sz = models.getarraysize();
        CallModel m;
        for (int i=0; i<sz; i++) {
            currentName = null;
            start = System.currentTimeMillis();
            m = models.get(i);
            if (debugKey!=null)
            	DebugLogging.sendTimerMessage("jlink.models.get,"+i, start, debugKey);
            
            if (m==null) continue;
            try {
                if (nameList!=null) {
                	String name = null;
                    try {
                    	start = System.currentTimeMillis();
                        name = m.getFileName();
                        if (debugKey!=null)
                        	DebugLogging.sendTimerMessage("jlink.GetFileName", start, debugKey);
                    }
                    catch (XToolkitCantOpen e) {
                    	start = System.currentTimeMillis();
                    	if (session!=null)
                    		JlinkUtils.resolveGenerics(session, m);
                        if (debugKey!=null)
                        	DebugLogging.sendTimerMessage("jshell.resolveGenerics", start, debugKey);
                    	start = System.currentTimeMillis();
                        name = m.getFileName();
                        if (debugKey!=null)
                        	DebugLogging.sendTimerMessage("jlink.ListModels", start, debugKey);
                    }   
                    if (!checkNameAgainstList(name))
                        continue;
                }
                else {
                    if (!checkName(m))
                        continue;
                }

                boolean abort = loopAction(m);
                if (abort)
                    break;
            }
            catch (jxthrowable jxe) {
                throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving file " + currentName), jxe);
            }
        }
    }

    /**
     * Abstract function which is called for each model which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param m The model which matched the filters
     * @return True to abort the loop, false to continue it
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract boolean loopAction(CallModel m) throws JLIException,jxthrowable;

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.util.LooperBase#processObjectByName(java.lang.String)
     */
    protected void processObjectByName(String name) throws JLIException,jxthrowable {
    	long start = System.currentTimeMillis();
    	CallModel m = JlinkUtils.getFile(session, name, false);
        if (debugKey!=null)
        	DebugLogging.sendTimerMessage("jshell.getFile", start, debugKey);
        if (m!=null)
            loopAction(m);
    }
    
    /**
     * See whether the model matches the name filter
     * @param m The model to check
     * @return Whether the model matches the name filter
     * @throws jxthrowable
     */
    private boolean checkName(CallModel m) throws jxthrowable {
        if (namePattern!=null) {
        	long start = System.currentTimeMillis();
            currentName = m.getFileName();
            if (debugKey!=null)
            	DebugLogging.sendTimerMessage("jlink.GetFileName", start, debugKey);
            return checkName(currentName);
        }
        // check nameList
        return true;
    }
    
	/**
	 * @return Whether to just loop on the active model if no filters are given
	 */
	public boolean isDefaultToActive() {
		return defaultToActive;
	}
	/**
	 * @param defaultToActive Whether to just loop on the active model if no filters are given
	 */
	public void setDefaultToActive(boolean defaultToActive) {
		this.defaultToActive = defaultToActive;
	}

    /**
     * @return The Creo session
     */
    public CallSession getSession() {
        return session;
    }

    /**
     * @param session The Creo session
     */
    public void setSession(CallSession session) {
        this.session = session;
    }

	/**
	 * @return The drawing containing the models, if specified
	 */
	public CallModel2D getDrawing() {
		return drawing;
	}

	/**
	 * @param drawing The drawing containing the models, if specified
	 */
	public void setDrawing(CallModel2D drawing) {
		this.drawing = drawing;
	}

}
