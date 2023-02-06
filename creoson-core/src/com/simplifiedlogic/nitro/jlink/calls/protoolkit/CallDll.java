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
package com.simplifiedlogic.nitro.jlink.calls.protoolkit;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcProToolkit.Dll;
import com.ptc.pfc.pfcProToolkit.FunctionReturn;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.argument.CallArguments;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcProToolkit.Dll
 * 
 * @author Adam Andrews
 *
 */
public class CallDll {

	private Dll dll = null;
	
	public CallDll(Dll dll) {
		this.dll = dll;
	}
	
	public String getId() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dll,GetId", 0, NitroConstants.DEBUG_JLINK_KEY);
		return dll.GetId();
	}
	
	public void unload() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dll,Unload", 0, NitroConstants.DEBUG_JLINK_KEY);
        dll.Unload();
	}
	
	public CallFunctionReturn executeFunction(String functionName, CallArguments inputArguments) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dll,ExecuteFunction", 0, NitroConstants.DEBUG_JLINK_KEY);
		FunctionReturn ret = dll.ExecuteFunction(
				functionName, 
				inputArguments!=null ? inputArguments.getArgs() : null);
		if (ret==null)
			return null;
		return new CallFunctionReturn(ret);
	}

	public Dll getDll() {
		return dll;
	}
	
}
