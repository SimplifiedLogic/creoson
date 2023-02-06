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
package com.simplifiedlogic.nitro.jlink.calls.simprep;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSimpRep.SimpRepAction;
import com.ptc.pfc.pfcSimpRep.SimpRepSubstitute;
import com.ptc.pfc.pfcSimpRep.Substitution;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSimpRep.SimpRepSubstitute
 * 
 * @author Adam Andrews
 *
 */
public class CallSimpRepSubstitute extends CallSimpRepAction {

	public CallSimpRepSubstitute(SimpRepSubstitute action) {
		super((SimpRepAction)action);
	}

	public CallSubstitution getSubstituteData() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("SimpRepSubstitute,GetSubstituteData", 0, NitroConstants.DEBUG_JLINK_KEY);
        Substitution subst = getAction().GetSubstituteData();
		if (subst==null)
			return null;
		return CallSubstitution.create(subst);
	}
	
	public SimpRepSubstitute getAction() {
		return (SimpRepSubstitute)action;
	}

}
