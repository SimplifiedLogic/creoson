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
package com.simplifiedlogic.nitro.jlink.calls.argument;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcArgument.Argument;
import com.ptc.pfc.pfcArgument.Arguments;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcArgument.Arguments
 * 
 * @author Adam Andrews
 *
 */
public class CallArguments {

	private Arguments args;
	
	public CallArguments(Arguments args) {
		this.args = args;
	}
	
	public static CallArguments create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Arguments,create", 0, NitroConstants.DEBUG_JLINK_KEY);
		Arguments args = Arguments.create();
		if (args==null)
			return null;
		return new CallArguments(args);
	}
	
	public void append(CallArgument arg) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Arguments,append", 0, NitroConstants.DEBUG_JLINK_KEY);
		args.append(arg.getArg());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Arguments,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return args.getarraysize();
	}
	
	public CallArgument get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Arguments,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
		Argument arg = args.get(idx);
		if (arg==null)
			return null;
		return new CallArgument(arg);
	}

	public Arguments getArgs() {
		return args;
	}
}
