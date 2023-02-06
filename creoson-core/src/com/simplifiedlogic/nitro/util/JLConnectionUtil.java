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
package com.simplifiedlogic.nitro.util;

import java.awt.Desktop;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitAmbiguous;
import com.simplifiedlogic.nitro.jlink.calls.asyncconnection.CallAsyncConnection;
import com.simplifiedlogic.nitro.jlink.calls.asyncconnection.CallConnectionId;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.impl.JLGlobal;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.rpc.JlinkConnectException;

/**
 * Useful utility methods relating to connections to JShell or Creo.
 * 
 * @author Adam Andrews
 */
public class JLConnectionUtil implements NitroConstants {

	/**
	 * System property for defining the Creo connection timeout, in seconds.  Defaults to 30.
	 */
	public static final String JLINK_CONN_TIMEOUT_PROP		= "sli.jlink.timeout";
	
    /**
     * Collection of current Creo connections
     */
    private static Hashtable<String, CallAsyncConnection> connections = new Hashtable<String, CallAsyncConnection>();
    
    /**
     * Whether to maintain only a single shared connection to Creo or to allow multiple connections
     */
    private static final boolean SINGLE_CONNECT = true;
    
    /**
     * Current connection timeout
     */
    private static int connectionTimeout = 0;
    
    /**
     * Required name of the .bat file needed to start Creo.  Restricted to this name 
     * in an effort to prevent security issues.
     */
    public static final String ALLOWED_PROE_CMD = "nitro_proe_remote.bat";
    
    /**
     * Number of connection retries allowed
     */
    private static final int RETRY_LIMIT        = 20;
    /**
     * Wait time before attempting to connect, in milliseconds
     */
    private static final long CONNECT_WAIT       = 3*1000L; // 3 seconds
    /**
     * Wait time between retries, in milliseconds
     */
    private static final long RETRY_WAIT         = 10*1000L; // 10 seconds

    /**
     * Get Creo's external connection ID for a Creo connection
     * @param async The Creo Async connection
     * @return The external representation of the Creo connection ID
     * @throws JLIException
     */
    public static String getNewConnId(CallAsyncConnection async) throws JLIException {
        try {
            if (async==null) return "";
            CallConnectionId connId = async.getConnectionId();
            return connId.getExternalRep(); // FIXME: may not be unique
        } catch (Exception ex) {
            ex.printStackTrace();
            throw JlinkUtils.createBareException(ex, "Could not get Creo connection ID");
        }
    }
    
    /**
     * Retrieve a Creo connection from internal storage
     * @param connId The Creo connection ID
     * @return The Creo Async connection, or null if the connection was not found
     */
    public static CallAsyncConnection getConnection(String connId) {
        if (connId==null) return null;
        CallAsyncConnection async = (CallAsyncConnection)connections.get(connId);
        return async;
    }
    
    /**
     * Get the Creo session for a connection.  If no connection can be retrieved from
     * internal memory, it returns null.  If the connection's session is invalid,
     * it tries to make a new connection to Creo and return the session for that.
     *  
     * @param connId The connection ID
     * @return The Creo session object for that connection 
     * @throws JLIException
     */
    public synchronized static CallSession getJLSession(String connId) throws JLIException {
        CallAsyncConnection async = getConnection(connId);
        if (async==null) return null;
        try {
            CallSession sess = async.getSession();
            try {
                sess.getCurrentDirectory();
            }
            catch (jxthrowable jxe) {
                // if failure, try to make a new connection
                createConnectionEntry(connId);
                async = getConnection(connId);
                sess = async.getSession();
            }
            return sess;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw JlinkUtils.createBareException(ex, "Could not get Creo Session ID");
        }
    }

    /**
     * Make a new Async connection to Creo; if SINGLE_CONNECT=true and there is
     * an existing connection, it returns that. 
     * @return The Async connection
     * @throws JLIException
     */
    public static String makeAsyncConnection() throws JLIException {

        if (SINGLE_CONNECT) {
            Enumeration<String> keys = connections.keys();
            if (keys.hasMoreElements()) {
                return (String)keys.nextElement();
            }
        }
        
        try {
            String connId = createConnectionEntry(null);
            return connId;
        }
        catch (XToolkitAmbiguous ex) {
            throw new JlinkConnectException("Unable to connect to Creo; more than one instance of Creo is running");
        }
        catch(Exception ex){
            ex.printStackTrace();
            throw new JlinkConnectException("Unable to connect to Creo through J-Link");
        }
    }
    
    /**
     * Attempt to connect asynchronously to Creo, and store that connection in memory.
     * 
     * @param connId Connection ID to use.  If null, a new connection ID is created.
     * @return The connection ID for the new connection.
     * @throws JLIException
     * @throws jxthrowable
     */
    private static String createConnectionEntry(String connId) throws JLIException, jxthrowable {
        JLGlobal.loadLibrary();
        
        int timeout = getConnectionTimeout();
        //System.out.println("Connection timeout: " + timeout);
        CallAsyncConnection async = 
            CallAsyncConnection.connect( null, null, null, new Integer(timeout));

        if (connId==null)
            connId = getNewConnId(async);
        connections.put(connId, async);
//        session = async.GetSession();
        
        return connId;
    }
    
    /**
     * Close a Creo Async connection.  This function also issues a System.gc() 
     * to clean up memory.
     * 
     * @param connId The connection ID to close
     * @throws JLIException
     */
    public static void closeAsyncConnection(String connId) throws JLIException {

        if (connId==null)
            return;
        
        if (SINGLE_CONNECT) {
            // TODO: decrease list of users of the connection?
            return;
        }
        else {
        
	        CallAsyncConnection async = connections.get(connId);
	        if (async==null)
	            return;
	        try {
	            JLGlobal.loadLibrary(); 
	
	            connections.remove(connId);
	            System.out.println("GC on close connection!");
	        	long start = System.currentTimeMillis();
	    		DebugLogging.sendTimerMessage("System,gc", 0, NitroConstants.DEBUG_JLINK_KEY);
	            System.gc();
	            DebugLogging.sendTimerMessage("System.gc", start, NitroConstants.DEBUG_CONNECT_KEY);
	            async.disconnect(new Integer(1000)); 
	        }
	        catch(Exception ex){
	            ex.printStackTrace();
	            throw JlinkUtils.createBareException(ex, "Could not disconnect async connection");
	        }
        }
    }
    
    /**
     * Start Creo using a .bat file and make a Creo Async connection afterwards 
     * 
     * @param connId Creo connection ID to reuse, if possible
     * @param path Directory location for the .bat file
     * @param cmd The name of the command file
     * @param retries Number of times to retry making the Creo connection
     * @param useDesktop Use the desktop to start Creo rather than the Runtime
     * @return The new connection ID
     * @throws JLIException
     * @throws jxthrowable
     * @throws Exception
     */
    public static String startProe(String connId, String path, String cmd, int retries, boolean useDesktop) throws JLIException,jxthrowable,Exception {
        if (SINGLE_CONNECT && isRunning())
            throw new JLIException("Creo is already running");

        if (path!=null) {
            if (!path.endsWith("/") && !path.endsWith("\\"))
                cmd = path + "/" + cmd;
            else
                cmd = path + cmd;
        }
        
        File f = new File(cmd);
        if (!f.exists())
            throw new JLIException("Command file '" + cmd + "' does not exist");
        if (f.isDirectory())
            throw new JLIException("Command file '" + cmd + "' is a directory");

        // we're doing this because AsyncConnection_Start() freezes up when running a .bat file,
        // and the UG troubleshooting steps don't seem to help
        if (cmd.trim().toLowerCase().endsWith(".bat")) {
        	if (useDesktop && Desktop.isDesktopSupported()) {
        		File dir = new File(path);
//        		String oldDir = System.getProperty("user.dir");
//        		System.setProperty("user.dir", dir.getAbsolutePath()); // this does not seem to work for setting creo's working dir
        		try {
        			Desktop.getDesktop().open(f);
        		}
        		finally {
//        			System.setProperty("user.dir", oldDir);
        		}
        	}
        	else {
	            cmd = "cmd.exe /c " + cmd;
	            f = new File(path);
	            
	            Runtime rt = Runtime.getRuntime();
	            Process pr = rt.exec(cmd, null, f.getParentFile());
	//           	pr.waitFor();
        	}
            JLGlobal.loadLibrary(); 
            String newConnId = null;
            if (retries < 0)
            	retries = RETRY_LIMIT;
            if (retries>0) {
            	Thread.sleep(CONNECT_WAIT);
	            for (int i=0; i<retries; i++) {
	                try {
	                    newConnId = createConnectionEntry(connId);
	                    break;
	                }
	                catch (com.ptc.pfc.pfcExceptions.XToolkitNotFound e) {
	                    // continue;
	                }
	                Thread.sleep(RETRY_WAIT);
	            }
	            if (newConnId==null)
	                throw new JlinkConnectException("Started Creo, but failed to connect after " + RETRY_LIMIT + " attempts.");
            }

            connId = newConnId;
        }
        else {
            JLGlobal.loadLibrary(); 
        
            CallAsyncConnection async = 
                CallAsyncConnection.start(cmd, null);
    
            if (connId==null)
                connId = getNewConnId(async);
            connections.put(connId, async);
        }
        
        return connId;
    }

    /**
     * Kill all Creo connections.  Loops through all connections that are
     * stored internally and issues a stop for each.
     * @throws JLIException
     * @throws jxthrowable
     */
    public static void stopProe() throws JLIException, jxthrowable {
        Enumeration<String> ids = connections.keys();
        String id;
        while (ids.hasMoreElements()) {
            id = ids.nextElement().toString();
            stopProe(id);
        }
    }

    /**
     * Kill a specific Creo connection.
     * @param connId The connection ID to close
     * @throws JLIException
     * @throws jxthrowable
     */
    public static void stopProe(String connId) throws JLIException,jxthrowable {
        CallAsyncConnection async = connections.get(connId);
        if (async!=null) {
            JLGlobal.loadLibrary(); 
            
            JLISession.disconnectSessions(connId);
            connections.remove(connId);
            if (async.isRunning())
                async.end();
        }
    }

    
    /**
     * Check whether a Creo connection is connected to an active Creo session.
     * If SINGLE_CONNECT=true and the connection is not running, it tries to 
     * open a new connection.
     * @param connId The Creo connection ID
     * @return Whether the connection is running
     * @throws jxthrowable
     */
    public static boolean isRunning(String connId) throws jxthrowable {
        if (SINGLE_CONNECT)
            return isRunning();
        else {
	        if (connId==null)
	            return isRunning();
	        
	        CallAsyncConnection async = connections.get(connId);
	        if (async==null)
	            return false;
	
	        JLGlobal.loadLibrary(); 
	        
	        boolean running = async.isRunning();
	        return running;
        }
    }

    /**
     * Check whether any of the Creo connections are connected to an active 
     * Creo session.  If none are running it attempts to make a new connection.
     * @return Whether any connection is running
     */
    public static boolean isRunning() {
        try {
            JLGlobal.loadLibrary(); 

            Enumeration<String> keys = connections.keys();
            CallAsyncConnection async = null;
            Object id = null;
            while (keys.hasMoreElements()) {
                id = keys.nextElement();
                async = (CallAsyncConnection)connections.get(id);
                if (async.isRunning()) {
                    return true;
                }
                else {
                    JLISession.disconnectSessions(id.toString());
                    connections.remove(id);
                }
            }

            async = CallAsyncConnection.connect(null, null, null, new Integer(10));
            if (async!=null) {
                connections.put(getNewConnId(async), async);
                return true;
            }
            else
                return false;
        }
        catch (Throwable e) { 
            return false; 
        }
    }
    
    /**
     * Kill the xtop and nmsd processes on the system
     */
    public static void killProe() {
		ProcessUtils.killProcess("xtop.exe");
		ProcessUtils.killProcess("nmsd.exe");
    }
    
    /**
     * Get the connection timeout as defined by the system property JLINK_CONN_TIMEOUT_PROP
     * If the system property is not defined, the timeout defaults to 30.
     * @return The connection timeout.
     */
    public static int getConnectionTimeout() {
    	if (connectionTimeout==0) {
	    	String maxStr = System.getProperty(JLINK_CONN_TIMEOUT_PROP);
        	if (maxStr!=null) {
        		try {
        			connectionTimeout = Integer.parseInt(maxStr);
        		}
        		catch (Exception e) {
        		}
        	}
        	if (connectionTimeout==0)
        		connectionTimeout = 30;
    	}
    	return connectionTimeout;
    }
}
