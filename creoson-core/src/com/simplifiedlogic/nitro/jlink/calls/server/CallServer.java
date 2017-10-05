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
package com.simplifiedlogic.nitro.jlink.calls.server;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcServer.Server;
import com.ptc.pfc.pfcServer.ServerLocation;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcServer.Server
 * 
 * @author Adam Andrews
 *
 */
public class CallServer extends CallServerLocation {

	public CallServer(Server server) {
		super((ServerLocation)server);
	}
	
	public void activate() throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Server.Activate", 0, NitroConstants.DEBUG_JLINK_KEY);
		getServer().Activate();
	}
	
	public String getAlias() throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Server.GetAlias", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getServer().GetAlias();
	}
	
	public String getActiveWorkspace() throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Server.GetActiveWorkspace", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getServer().GetActiveWorkspace();
	}
	
	public void setActiveWorkspace(String value) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Server.SetActiveWorkspace", 0, NitroConstants.DEBUG_JLINK_KEY);
		getServer().SetActiveWorkspace(value);
	}
	
	public void createWorkspace(CallWorkspaceDefinition definition) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Server.CreateWorkspace", 0, NitroConstants.DEBUG_JLINK_KEY);
		getServer().CreateWorkspace(definition.getDefinition());
	}
	
	public void removeObjects(CallStringSeq modelNames) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Server.RemoveObjects", 0, NitroConstants.DEBUG_JLINK_KEY);
		getServer().RemoveObjects(modelNames!=null ? modelNames.getSeq() : null);
	}
	
	public boolean isObjectCheckedOut(String workspaceName, String objectName) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Server.IsObjectCheckedOut", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getServer().IsObjectCheckedOut(workspaceName, objectName);
	}

	public Server getServer() {
		return (Server)getLocation();
	}

}
