/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.protoolkit;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcArgument.Arguments;
import com.ptc.pfc.pfcProToolkit.FunctionReturn;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.argument.CallArguments;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcProToolkit.FunctionReturn
 * 
 * @author Adam Andrews
 *
 */
public class CallFunctionReturn {

	private FunctionReturn ret;
	
	public CallFunctionReturn(FunctionReturn ret) {
		this.ret = ret;
	}
	
	public int getFunctionReturn() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FunctionReturn,GetFunctionReturn", 0, NitroConstants.DEBUG_JLINK_KEY);
		return ret.GetFunctionReturn();
	}
	
	public CallArguments getOutputArguments() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FunctionReturn,GetOutputArguments", 0, NitroConstants.DEBUG_JLINK_KEY);
		Arguments args = ret.GetOutputArguments();
		if (args==null)
			return null;
		return new CallArguments(args);
	}

	public FunctionReturn getRet() {
		return ret;
	}
}
