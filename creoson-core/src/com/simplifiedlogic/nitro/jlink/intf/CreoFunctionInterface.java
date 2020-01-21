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
package com.simplifiedlogic.nitro.jlink.intf;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.rpc.JLIException;


/**
 * Interface for an externally defined function which needs to call Creo-only JLink functions.
 * This interface is required because JShell is intended to be compatible with Pro/E Wildfire 4
 * as well as with Creo.
 * 
 * @author Adam Andrews
 *
 */
public interface CreoFunctionInterface {
	
	
	/**
	 * Execute the function with the given argument list.
	 * 
	 * @param args A list of objects needed by the function as arguments.  The nature of those arguments depends on the function.
	 * @return Return data custom to the function.
	 * @throws JLIException
	 * @throws jxthrowable
	 */
	public Object execute(Object[] args) throws JLIException,jxthrowable;
}
