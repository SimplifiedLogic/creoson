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

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcView2D.View2D;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D;
import com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * A Creo function interface for renaming a drawing view.  This JLink function did not
 * exist in WF4.
 * 
 * <p>Arguments expected:
 * <ol>
 * <li>The drawing view to rename.  Must be of type {@link com.ptc.pfc.pfcView2D.View2D} or 
 * {@link CallView2D}
 * 
 * <li>The new name for the view, as a java.lang.String.
 * </ol>
 * @author Adam Andrews
 *
 */
public class RenameDrawingView implements CreoFunctionInterface {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.CreoFunctionInterface#execute(java.lang.Object[])
	 */
	@Override
	public Object execute(Object[] args) throws JLIException,jxthrowable {
		View2D view = null;
		if (args[0] instanceof CallView2D)
			view = ((CallView2D)args[0]).getView();
		else 
			view = (View2D)args[0];
		String newViewName = (String)args[1];
		
		view.SetName(newViewName);
		
		return null;
	}

}
