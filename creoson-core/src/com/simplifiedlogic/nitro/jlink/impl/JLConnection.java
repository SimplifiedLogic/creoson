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
package com.simplifiedlogic.nitro.jlink.impl;

import java.io.File;
import java.io.IOException;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.JLStatus;
import com.simplifiedlogic.nitro.jlink.intf.IJLConnection;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;

/**
 * @author Adam Andrews
 *
 */
public class JLConnection implements IJLConnection {

    public String defaultCommandLog = null;

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLConnection#connect(java.lang.String, java.lang.String)
	 */
    public JLStatus connect(String system) throws JLIException {
    	long start = 0;
		DebugLogging.sendDebugMessage("connection.connect", NitroConstants.DEBUG_KEY);
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
			boolean success = true;
			JLISession sess = null;
	        JLStatus stat = new JLStatus();
	
	        String sessionId = handleConnect(system, null, stat);
			if (sessionId==null) {
				success = false;
				sess = null;
			}
			else {
				sess = JLISession.getSession(sessionId);
	//	        if (sess!=null) sess.logReturn(out_cmd);
			}
			
			stat.setSessionId(sessionId);
			stat.setSuccess(success);
			if (!success && stat.getMessage()==null)
				stat.setMessage("Errors occurred during the processing of this command");
			
			return stat;
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("connection.connect", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
    
	/**
	 * Perform the connection to JShell. 
	 * @param system The system to connect to.  This was intended to support connections to various types of systems, but the only one currently supported is "pro/e".
	 * @param commandlog The name of the commandLog file for command debugging (optional)
	 * @param stat Object for returning status of the connection
	 * @return The new JShell session ID
	 * @throws JLIException
	 */
	private String handleConnect(String system, String commandlog, JLStatus stat) throws JLIException {
		if ("".equals(commandlog))
			commandlog = null;
		if (commandlog==null)
			commandlog = defaultCommandLog;

		JLISession sess = JLISession.createSession();
        if (sess==null) {
            throw new JLIException("Could not create session");
        }
        String sessionId = sess.getSessionId();
        if (sessionId==null) {
            throw new JLIException("Error connecting to Creo");
        }

        if (APP_PROE.equalsIgnoreCase(system))
            sess.setSessionType(JLISession.SESSION_TYPE_PROE);
        else {
            throw new JLIException("Unknown system type on connect");
        }

        try {
            sess.setCommandLog(commandlog);
        }
        catch (IOException e) {
            //throw new JLIException("Could not write to command log:" + commandlog);
        }

        stat.setSessionId(sessionId);
        return sessionId;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#disconnect(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void disconnect(AbstractJLISession sess) throws JLIException {
		JLISession.deleteSession(sess);
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#disconnect(java.lang.String)
	 */
	@Override
	public void disconnect(String sessionId) throws JLIException {
		if (sessionId!=null)
			disconnect(JLISession.getSession(sessionId));
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#getSession(java.lang.String)
	 */
	@Override
	public AbstractJLISession getSession(String sessionId) {
		return JLISession.getSession(sessionId);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#startProe(java.lang.String, java.lang.String, int, boolean, java.lang.String)
	 */
	@Override
    public AbstractJLISession startProe(String startDir, String startCommand, int retries, boolean useDesktop, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
    	return startProe(startDir, startCommand, retries, useDesktop, sess);
    }
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#startProe(java.lang.String, java.lang.String, int, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public AbstractJLISession startProe(String startDir, String startCommand, int retries, boolean useDesktop, AbstractJLISession sess) throws JLIException {
        if (startDir==null || startDir.length()==0)
            throw new JLIException("Must specify a startDir parameter");
        if (startCommand==null || startCommand.length()==0)
            throw new JLIException("Must specify a startCommand parameter");
        if (!startCommand.equalsIgnoreCase(JLConnectionUtil.ALLOWED_PROE_CMD))
            throw new JLIException("You may only specify '" + JLConnectionUtil.ALLOWED_PROE_CMD + "' for the startCommand parameter");

        File f = null;
        if (startDir!=null) {
            f = new File(startDir);
            if (!f.exists())
                throw new JLIException("startDir value '" + startDir + "' does not exist");
            if (!f.isDirectory())
                throw new JLIException("startDir value '" + startDir + "' is not a directory");
        }

        try {
	        String connId = JLConnectionUtil.startProe(null, startDir, startCommand, retries, useDesktop);
	        if (sess!=null) {
	        	sess.setConnectionId(connId);
	        }
	        
	        return sess;
        }
        catch (JLIException e) {
        	throw e;
        }
        catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
        }
        catch (Exception e) {
        	throw new JLIException(e);
        }
	}
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#stopProe(java.lang.String)
	 */
	@Override
    public void stopProe(String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
    	stopProe(sess);
    }
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#stopProe(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public void stopProe(AbstractJLISession sess) throws JLIException {
		try {
			JLConnectionUtil.stopProe();
			
			JLISession.deleteSession(sess);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    }
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#isProeRunning()
	 */
	@Override
    public boolean isProeRunning() throws JLIException {
		return JLConnectionUtil.isRunning();
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#isProeRunning(java.lang.String)
	 */
	@Override
    public boolean isProeRunning(String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
    	return isProeRunning(sess);
    }
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#isProeRunning(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public boolean isProeRunning(AbstractJLISession sess) throws JLIException {
		if (sess!=null) {
			try {
				return JLConnectionUtil.isRunning(sess.getConnectionId());
			}
			catch (Throwable e) {
				return false;
			}
		}
		else
			return JLConnectionUtil.isRunning();
    }

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLConnection#killProe()
	 */
	@Override
	public void killProe() throws JLIException {
		JLConnectionUtil.killProe();
	}
	
}
