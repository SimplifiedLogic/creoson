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
package com.simplifiedlogic.nitro.util;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitCantOpen;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.view.CallView;
import com.simplifiedlogic.nitro.jlink.calls.view.CallViews;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over views on a model, 
 * filters the results by various filters, and calls an action method
 * for each model view which matches the filters.
 *  
 * @author Adam Andrews
 *
 */
public abstract class ViewLooper extends LooperBase {

    /**
     * The model which owns the views
     */
    private CallModel model = null;
    
    /**
     * Output flag indicating that only a single parameter was specified in the name filter
     */
    public boolean singleOp = false;
    
    /**
     * Loop through views in a model
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop() throws JLIException,jxthrowable {
        if (namePattern!=null && !isNamePattern) {
            currentName = null;
            singleOp = true;
            processObjectByName(namePattern);
            return;
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
        CallViews views = null;
        views = model.listViews();
        if (debugKey!=null)
        	DebugLogging.sendTimerMessage("jlink.ListViews", start, debugKey);
        if (views==null)
        	return;
        int sz = views.getarraysize();
        CallView view;
        for (int i=0; i<sz; i++) {
            currentName = null;
            start = System.currentTimeMillis();
            view = views.get(i);
            if (debugKey!=null)
            	DebugLogging.sendTimerMessage("jlink.views.get,"+i, start, debugKey);
            
            if (view==null) continue;
            try {
                if (nameList!=null) {
                	String name = null;
                    try {
                    	start = System.currentTimeMillis();
                        name = view.getName();
                        if (debugKey!=null)
                        	DebugLogging.sendTimerMessage("jlink.GetViewName", start, debugKey);
                    }
                    catch (XToolkitCantOpen e) {
                    	start = System.currentTimeMillis();
                        name = view.getName();
                        if (debugKey!=null)
                        	DebugLogging.sendTimerMessage("jlink.ListViews", start, debugKey);
                    }   
                    if (!checkNameAgainstList(name))
                        continue;
                }
                else {
                    if (!checkName(view))
                        continue;
                }

                boolean abort = loopAction(view);
                if (abort)
                    break;
            }
            catch (jxthrowable jxe) {
                throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving view " + view.getName()), jxe);
            }
        }
    }

    /**
     * Abstract function which is called for each model view which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param view The model view that matches the filters
     * @return True to abort the loop, false to continue it
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract boolean loopAction(CallView view) throws JLIException,jxthrowable;

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.util.LooperBase#processObjectByName(java.lang.String)
     */
    protected void processObjectByName(String name) throws JLIException,jxthrowable {
    	long start = System.currentTimeMillis();
    	CallView view = model.getView(name);
        if (debugKey!=null)
        	DebugLogging.sendTimerMessage("jshell.getView", start, debugKey);
        if (view!=null)
            loopAction(view);
    }
    
    /**
     * See whether the model view matches the name filter
     * @param view The view to check
     * @return Whether the view matches the name filter
     * @throws jxthrowable
     */
    private boolean checkName(CallView view) throws jxthrowable {
        if (namePattern!=null) {
        	long start = System.currentTimeMillis();
            currentName = view.getName();
            if (debugKey!=null)
            	DebugLogging.sendTimerMessage("jlink.GetName", start, debugKey);
            return checkName(currentName);
        }
        // check nameList
        return true;
    }
    
	/**
	 * @return The model that owns the views
	 */
	public CallModel getModel() {
		return model;
	}

	/**
	 * @param model The model that owns the views
	 */
	public void setModel(CallModel model) {
		this.model = model;
	}

}
