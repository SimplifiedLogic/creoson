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
package com.simplifiedlogic.nitro.jlink.calls.asyncconnection;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcAsyncConnection.AsyncConnection;
import com.ptc.pfc.pfcAsyncConnection.ConnectionId;
import com.ptc.pfc.pfcAsyncConnection.pfcAsyncConnection;
import com.ptc.pfc.pfcSession.Session;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcAsyncConnection.AsyncConnection
 * 
 * @author Adam Andrews
 *
 */
public class CallAsyncConnection {

	private AsyncConnection conn;
	
	public CallAsyncConnection(AsyncConnection conn) {
		this.conn = conn;
	}

	public static CallAsyncConnection connect(String display, String userId, String textPath, Integer timeoutSec) throws jxthrowable {
    	long start = System.currentTimeMillis();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcAsyncConnection,AsyncConnection_Connect", 0, NitroConstants.DEBUG_JLINK_KEY);
		AsyncConnection async = pfcAsyncConnection.AsyncConnection_Connect(display, userId, textPath, timeoutSec);
        DebugLogging.sendTimerMessage("jlink.async_connect", start, NitroConstants.DEBUG_CONNECT_KEY);
		if (async==null)
			return null;
		return new CallAsyncConnection(async);
	}
	
	public static CallAsyncConnection start(String cmdLine, String textPath) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcAsyncConnection,AsyncConnection_Start", 0, NitroConstants.DEBUG_JLINK_KEY);
		AsyncConnection async = pfcAsyncConnection.AsyncConnection_Start(cmdLine, textPath);
		if (async==null)
			return null;
		return new CallAsyncConnection(async);
	}
	
	public CallSession getSession() throws jxthrowable {
    	long start = System.currentTimeMillis();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("AsyncConnection,GetSession", 0, NitroConstants.DEBUG_JLINK_KEY);
		Session sess = conn.GetSession();
        DebugLogging.sendTimerMessage("jlink.GetSession", start, NitroConstants.DEBUG_CONNECT_KEY);
		if (sess==null)
			return null;
		return new CallSession(sess);
	}
	
	public CallConnectionId getConnectionId() throws jxthrowable {
        long start=System.currentTimeMillis();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("AsyncConnection,GetConnectionId", 0, NitroConstants.DEBUG_JLINK_KEY);
		ConnectionId id = conn.GetConnectionId();
        DebugLogging.sendTimerMessage("jlink.GetConnectionId", start, NitroConstants.DEBUG_CONNECT_KEY);
		if (id==null)
			return null;
		return new CallConnectionId(id);
	}
	
	public void disconnect(Integer timeoutSec) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("AsyncConnection,Disconnect", 0, NitroConstants.DEBUG_JLINK_KEY);
    	long start = System.currentTimeMillis();
		conn.Disconnect(timeoutSec);
        DebugLogging.sendTimerMessage("jlink.Disconnect", start, NitroConstants.DEBUG_CONNECT_KEY);
	}
	
	public boolean isRunning() throws jxthrowable {
    	long start = System.currentTimeMillis();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("AsyncConnection,IsRunning", 0, NitroConstants.DEBUG_JLINK_KEY);
        boolean running = conn.IsRunning();
        DebugLogging.sendTimerMessage("jlink.IsRunning", start, NitroConstants.DEBUG_CONNECT_KEY);
        return running;
	}
	
	public void end() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("AsyncConnection,End", 0, NitroConstants.DEBUG_JLINK_KEY);
		conn.End();
	}

	public AsyncConnection getConn() {
		return conn;
	}
}
