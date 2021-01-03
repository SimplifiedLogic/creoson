/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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
import com.simplifiedlogic.nitro.jlink.calls.model2d.CallModel2D;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over sheets in a drawing, 
 * filters the results by various filters, and calls an action method
 * for each sheet which matches the filters.
 *  
 * @author Adam Andrews
 */
public abstract class SheetLooper {

	/**
	 * Drawing which contains the sheets
	 */
    private CallModel2D drawing = null;

    /**
	 * Sheet number to process; if 0, process all sheets
	 */
	private int sheetno;
	
	public void loop() throws JLIException, jxthrowable {
        int numSheets = drawing.getNumberOfSheets();

        if (sheetno<0 || sheetno>numSheets)
        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"") + " or 0 for all");

        // doing this because regenerating a non-current (non-visible?) sheet causes a General Error, at least in Creo 3 M090
        int curSheet = drawing.getCurrentSheetNumber();
        int lastSheet = curSheet;
        if (sheetno==0) {
        	for (int i=0; i<numSheets; i++) {
        		lastSheet = i+1;
	        	drawing.setCurrentSheetNumber(lastSheet);

                boolean abort = loopAction(drawing, lastSheet);
                if (abort)
                    break;
        	}
        }
        else {
        	lastSheet = sheetno;
        	if (lastSheet!=curSheet)
	        	drawing.setCurrentSheetNumber(lastSheet);

    		loopAction(drawing, lastSheet);
        }
        
        if (lastSheet!=curSheet)
        	drawing.setCurrentSheetNumber(curSheet);
	}

	public abstract boolean loopAction(CallModel2D drawing, int sheetno) throws JLIException,jxthrowable;

	public CallModel2D getDrawing() {
		return drawing;
	}

	public void setDrawing(CallModel2D drawing) {
		this.drawing = drawing;
	}

	public int getSheetno() {
		return sheetno;
	}

	public void setSheetno(int sheetno) {
		this.sheetno = sheetno;
	}

}
