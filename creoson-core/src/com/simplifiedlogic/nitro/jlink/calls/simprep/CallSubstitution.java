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
import com.ptc.pfc.pfcSimpRep.SubstAsmRep;
import com.ptc.pfc.pfcSimpRep.SubstPrtRep;
import com.ptc.pfc.pfcSimpRep.Substitution;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSimpRep.Substitution
 * 
 * @author Adam Andrews
 *
 */
public class CallSubstitution {

	protected Substitution subst;
	
	protected CallSubstitution(Substitution subst) {
		this.subst = subst;
	}
	
	public int getSubstType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Substitution,GetSubstType", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getSubstitution().GetSubstType().getValue();
	}
	
	public static CallSubstitution create(Substitution action) {
		if (action==null)
			return null;
		if (action instanceof SubstPrtRep)
			return new CallSubstPrtRep((SubstPrtRep)action);
		if (action instanceof SubstAsmRep)
			return new CallSubstAsmRep((SubstAsmRep)action);
		else
			return new CallSubstitution(action);
	}

	public Substitution getSubstitution() {
		return subst;
	}
}
