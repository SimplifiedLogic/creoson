/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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
import com.ptc.pfc.pfcExceptions.XToolkitGeneralError;
import com.simplifiedlogic.nitro.jlink.calls.model2d.CallModel2D;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2Ds;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over views on a drawing, 
 * filters the results by various filters, and calls an action method
 * for each drawing view which matches the filters.
 *  
 * @author Adam Andrews
 */
public abstract class View2DLooper extends LooperBase {

    /**
     * The drawing which owns the views
     */
    private CallModel2D drawing = null;
    
    /**
     * Sheet number filter
     */
    private int sheetno = -1;
    
    /**
     * Output flag indicating that only a single parameter was specified in the name filter
     */
    public boolean singleOp = false;
    
    /**
     * Loop through views in a drawing
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop() throws JLIException,jxthrowable {
//        if (namePattern!=null && !isNamePattern) {
//            currentName = null;
//            singleOp = true;
//            processObjectByName(namePattern);
//            return;
//        }

        singleOp = false;
//        if (nameList!=null && !isNamePattern) {
//            for (int i=0; i<nameList.length; i++) {
//                currentName = null;
//                processObjectByName(nameList[i]);
//            }
//            return;
//        }

        long start;
        start = System.currentTimeMillis();
        CallView2Ds views = null;
        views = drawing.list2DViews();
        if (debugKey!=null)
        	DebugLogging.sendTimerMessage("jlink.ListViews", start, debugKey);
        if (views==null)
        	return;
        int sz = views.getarraysize();
        CallView2D view;
        for (int i=0; i<sz; i++) {
            currentName = null;
            start = System.currentTimeMillis();
            view = views.get(i);
            if (debugKey!=null)
            	DebugLogging.sendTimerMessage("jlink.views.get,"+i, start, debugKey);
            
            if (view==null) continue;
            try {
            	if (sheetno>0) { // sheet numbers start at 1
            		int viewsheet = -1;
            		try {
            			viewsheet = view.getSheetNumber();
            		}
            		catch (XToolkitGeneralError e) {
            			// this error was thrown for a view that was Suppressed
            			continue;
            		}
            		if (viewsheet!=sheetno)
            			continue;
            	}

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
                        //System.out.println("    child: " + child);
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
     * Abstract function which is called for each drawing view which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param view The drawing view that matches the filters
     * @return True to abort the loop, false to continue it
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract boolean loopAction(CallView2D view) throws JLIException,jxthrowable;

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.util.LooperBase#processObjectByName(java.lang.String)
     */
    protected void processObjectByName(String name) throws JLIException,jxthrowable {
//    	long start = System.currentTimeMillis();
//    	CallView2D view = drawing.getViewByName(name);
//        if (debugKey!=null)
//        	DebugLogging.sendTimerMessage("jshell.getViewByName", start, debugKey);
//        if (view!=null)
//            loopAction(view);
    }
    
    /**
     * See whether the drawing view matches the name filter
     * @param view The view to check
     * @return Whether the view matches the name filter
     * @throws jxthrowable
     */
    private boolean checkName(CallView2D view) throws jxthrowable {
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
	 * @return The drawing that owns the views
	 */
	public CallModel2D getDrawing() {
		return drawing;
	}

	/**
	 * @param drawing The drawing that owns the views
	 */
	public void setDrawing(CallModel2D drawing) {
		this.drawing = drawing;
	}

	/**
	 * @return The sheet number filter
	 */
	public int getSheetno() {
		return sheetno;
	}

	/**
	 * @param sheetno The sheet number filter
	 */
	public void setSheetno(int sheetno) {
		this.sheetno = sheetno;
	}

}
