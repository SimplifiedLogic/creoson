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
package com.simplifiedlogic.nitro.rpc;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;

/**
 * This represents a JShell session object.  It also has static functionality for keeping track
 * of all session objects in memory.
 * 
 * @author Adam Andrews
 */
public class JLISession extends AbstractJLISession {

    /**
     * Container for all sessions currently in memory
     */
    private static Hashtable<String, JLISession> sessionMap = new Hashtable<String, JLISession>();

    private static Random random = new Random();
    
    public static final int SESSION_TYPE_NONE = 0;
    public static final int SESSION_TYPE_PROE = 1;
    
	private String sessionId = "";
    private String connId = null;
    private int sessionType = SESSION_TYPE_NONE;
    private long lastUsed = System.currentTimeMillis();
    private String commandLog = null;
    
    /**
     * Container for all external processes started by this session 
     */
    private Hashtable<String, Process> processList = null;
    
    /**
     * Default constructor.  Will automatically generate a new session ID for the session.
     */
    private JLISession() {
        setSessionId(generateSessionId());
    }
    
    /**
     * Constructor using a specific session ID
     * @param sessionId The session ID
     */
    private JLISession(String sessionId) {
        setSessionId(sessionId);
    }
    
    /**
     * Create a new session and keep track of it internally
     * @return The new session object
     */
    public synchronized static JLISession createSession() {
        JLISession sess = new JLISession();
        sessionMap.put(sess.getSessionId(), sess);
        
        return sess;
    }

    /**
     * Get a session object.  Calling this will also update the lastUsed time on the session.
     * @param sessionId The ID for the session to retrieve
     * @return The session object, or null if one does not exist
     */
    public synchronized static JLISession getSession(String sessionId) {
        if (sessionId==null)
            return null;
        JLISession sess = (JLISession)sessionMap.get(sessionId); 
        if (sess != null)
            sess.setLastUsed(System.currentTimeMillis());
        
        return sess;
    }
 
    /**
     * Delete a session from memory
     * @param sess The session object to delete
     * @throws JLIException
     */
    public synchronized static void deleteSession(AbstractJLISession sess) throws JLIException {
        if (sess!=null) {
        	if (sess.getSessionId()!=null)
        		sessionMap.remove(sess.getSessionId());
            if (sess instanceof JLISession) {
	            ((JLISession)sess).cleanSession();
	            ((JLISession)sess).setSessionId(null);
            }
        }
    }
    
    /**
     * Generate a new, random session ID.
     * @return The session ID
     */
    private synchronized static String generateSessionId() {
        // TODO: make this more sophisticated later?
        String sessid = String.valueOf(random.nextLong());
        while (sessionMap.get(sessid)!=null) { 
            sessid = String.valueOf(random.nextLong());
        }
        return sessid;
    }

    /**
     * Delete all sessions which use a specific Creo connection ID.
     * @param deadConnId The Creo connection ID
     * @throws JLIException
     */
    public synchronized static void disconnectSessions(String deadConnId) throws JLIException {
        if (deadConnId==null)
            return;
        Enumeration<String> ids = sessionMap.keys();
        Object id;
        JLISession sess;
        while (ids.hasMoreElements()) {
            id = ids.nextElement();
            sess = (JLISession)sessionMap.get(id);
            if (deadConnId.equals(sess.getConnectionId()))
                deleteSession(sess);
        }
    }

    /**
     * Clean up a session prior to deleting it.  This includes closing the Creo connection.
     * @throws JLIException
     */
    public void cleanSession() throws JLIException {
        if (connId != null) {
           	JLConnectionUtil.closeAsyncConnection(connId);
            connId = null;
        }
        //killSessionProcesses();
        if (processList!=null)
            processList.clear();
    }

    /**
     * Create a new Async connection to Creo.
     * @throws JLIException
     */
    public void setConnection() throws JLIException {
       	connId = JLConnectionUtil.makeAsyncConnection();
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.data.AbstractJLISession#getConnectionId()
     */
    public String getConnectionId() throws JLIException {
        if (connId==null)
            setConnection();
        return connId;
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.data.AbstractJLISession#setConnectionId(java.lang.String)
     */
    public void setConnectionId(String connId) {
        this.connId = connId;
    }
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.data.AbstractJLISession#getSessionId()
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Set the current JShell session ID
	 * @param sessionId
	 */
	private void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

    /**
     * @return Get the last-used time
     */
    public long getLastUsed() {
        return lastUsed;
    }

    /**
     * @param Set the last-used time
     */
    private void setLastUsed(long l) {
        lastUsed = l;
    }

	/**
	 * @return Returns the sessionType.
	 */
	public int getSessionType() {
		return sessionType;
	}
	
	/**
	 * @param sessionType The sessionType to set.
	 */
	public void setSessionType(int sessionType) {
		this.sessionType = sessionType;
	}

    /**
     * @return Returns the commandLog.
     */
    public String getCommandLog() {
        return commandLog;
    }

    /**
     * Sets the location of the command log.  Writes a new entry to the command log to indicate it.
     * @param commandLog The commandLog to set.
     */
    public void setCommandLog(String commandLog) throws IOException {
        if (commandLog!=null) {
            writeToLog(commandLog, "****starting new session:" + 
                    new Timestamp(System.currentTimeMillis()));
        }
        this.commandLog = commandLog;
    }

    /**
     * Add an external process to the session, so that processes can be tracked
     * @param pr The process that was started
     * @return The JShell internal ID for the process
     */
    public String addProcess(Process pr) {
        if (pr==null)
            return null;
        if (processList==null) {
            processList = new Hashtable<String, Process>();
        }
        String processId = generateSessionId();
        processList.put(processId, pr);
        return processId;
    }
    
    /**
     * Kill an external process
     * @param pr The process to kill
     */
    private void killProcess(Process pr) {
        if (pr==null)
            return;
        pr.destroy();
    }
    
    /**
     * Kill an external process
     * @param processId The ID of the process to kill
     */
    private void killProcess(String processId) {
        if (processId==null || processList==null)
            return;
        Process pr = (Process)processList.get(processId);
        if (pr==null)
            return;
        killProcess(pr);
        processList.remove(processId);
        if (processList.size()==0)
            processList = null;
    }
    
    /**
     * Kill all processes belonging to this session
     */
    public void killSessionProcesses() {
        if (processList==null)
            return;
        Enumeration<String> keys = processList.keys();
        while (keys.hasMoreElements()) {
            killProcess((String)keys.nextElement());
        }
    }
    
    /**
     * Kill all processes in all sessions
     */
    public static void killAllProcesses() {
        if (sessionMap==null || sessionMap.size()==0)
            return;
        Enumeration<String> keys = sessionMap.keys();
        while (keys.hasMoreElements()) {
            String id = (String)keys.nextElement();
            JLISession sess = (JLISession)sessionMap.get(id);
            sess.killSessionProcesses();
        }
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.data.AbstractJLISession#logCommand(java.util.Map)
     */
    public boolean logCommand(Map<Object, Object> cmd) {
        if (cmd==null || commandLog==null)
            return true;
        try {
            writeToLog(commandLog, cmd);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.data.AbstractJLISession#logReturn(java.util.Map)
     */
    public boolean logReturn(Map<Object, Object> cmd) {
        if (cmd==null || commandLog==null)
            return true;
        try {
            writeToLog(commandLog, "   returning:" + cmd);
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }
    
    /**
     * Write an entry to the command log.  The input object will be converted 
     * to a string before writing to the log.
     * 
     * @param logfile The name of the log file
     * @param text The object to write
     * @throws IOException
     */
    private void writeToLog(String logfile, Object text) throws IOException {
        if (text==null || logfile==null)
            return;

        OutputStream os = new FileOutputStream(logfile, true);
        os.write(text.toString().getBytes(Charset.forName("UTF-8")));
        os.write("\n".getBytes(Charset.forName("UTF-8")));
        os.close();
    }
}
