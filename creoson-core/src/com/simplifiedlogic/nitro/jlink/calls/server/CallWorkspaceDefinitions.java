/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.server;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcServer.WorkspaceDefinition;
import com.ptc.pfc.pfcServer.WorkspaceDefinitions;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcServer.WorkspaceDefinitions
 * 
 * @author Adam Andrews
 *
 */
public class CallWorkspaceDefinitions {

	private WorkspaceDefinitions defs;
	
	public CallWorkspaceDefinitions(WorkspaceDefinitions defs) {
		this.defs = defs;
	}
	
	public static CallWorkspaceDefinitions create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("WorkspaceDefinitions,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        WorkspaceDefinitions defs = WorkspaceDefinitions.create();
		if (defs==null)
			return null;
		return new CallWorkspaceDefinitions(defs);
	}
	
	public void append(CallWorkspaceDefinition def) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("WorkspaceDefinitions,append", 0, NitroConstants.DEBUG_JLINK_KEY);
        defs.append(def.getDefinition());
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("WorkspaceDefinitions,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return defs.getarraysize();
	}
	
	public CallWorkspaceDefinition get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("WorkspaceDefinitions,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        WorkspaceDefinition def = defs.get(idx);
		if (def==null)
			return null;
		return new CallWorkspaceDefinition(def);
	}

	public WorkspaceDefinitions getDefs() {
		return defs;
	}
}
