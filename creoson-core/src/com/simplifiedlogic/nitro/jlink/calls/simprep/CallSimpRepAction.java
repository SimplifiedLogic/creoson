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
package com.simplifiedlogic.nitro.jlink.calls.simprep;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSimpRep.SimpRepAction;
import com.ptc.pfc.pfcSimpRep.SimpRepSubstitute;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSimpRep.SimpRepAction
 * 
 * @author Adam Andrews
 *
 */
public class CallSimpRepAction {

	protected SimpRepAction action;
	
	protected CallSimpRepAction(SimpRepAction action) {
		this.action = action;
	}
	
	public int getType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepAction,GetType", 0, NitroConstants.DEBUG_JLINK_KEY);
		return action.GetType().getValue();
	}
	
	public static CallSimpRepAction create(SimpRepAction action) {
		if (action==null)
			return null;
		if (action instanceof SimpRepSubstitute)
			return new CallSimpRepSubstitute((SimpRepSubstitute)action);
		else
			return new CallSimpRepAction(action);
	}

	public SimpRepAction getAction() {
		return action;
	}
}
