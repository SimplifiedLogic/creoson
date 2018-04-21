/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.model;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.Dependencies;
import com.ptc.pfc.pfcModel.Dependency;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModel.Dependencies
 * 
 * @author Adam Andrews
 *
 */
public class CallDependencies {

	private Dependencies deps;
	
	public CallDependencies(Dependencies deps) {
		this.deps = deps;
	}
	
	public static CallDependencies create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dependencies,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        Dependencies deps = Dependencies.create();
		if (deps==null)
			return null;
		return new CallDependencies(deps);
	}
	
	public void append(CallDependency dep) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dependencies,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        deps.append(dep.getDependency());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dependencies,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return deps.getarraysize();
	}
	
	public CallDependency get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Dependencies,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        Dependency dep = deps.get(idx);
        return new CallDependency(dep);
	}

	public Dependencies getDependencies() {
		return deps;
	}
}
