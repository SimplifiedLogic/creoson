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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.data.JLStatus;
import com.simplifiedlogic.nitro.jlink.intf.IJLConnection;
import com.simplifiedlogic.nitro.jshell.json.request.JLConnectRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLConnectResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "connection" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonConnectionHandler extends JLJsonCommandHandler implements JLConnectRequestParams, JLConnectResponseParams {

	private IJLConnection connHandler = null;

    public static final String ALLOWED_CREOSON_CMD = "creoson_run.bat";

	/**
	 * @param connHandler
	 */
	public JLJsonConnectionHandler(IJLConnection connHandler) {
		this.connHandler = connHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_CONNECT))
			return actionConnect(input);
		else if (function.equals(FUNC_DISCONNECT))
			return actionDisconnect(sessionId, input);
		else if (function.equals(FUNC_IS_RUNNING))
			return actionIsCreoRunning(sessionId, input);
		else if (function.equals(FUNC_START_PROE))
			return actionStartCreo(sessionId, input);
		else if (function.equals(FUNC_STOP_PROE))
			return actionStopCreo(sessionId, input);
		else if (function.equals(FUNC_KILL_PROE))
			return actionKillCreo(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionConnect(Hashtable<String, Object> input) throws JLIException {
		JLStatus connectStatus = connHandler.connect(JLConnectRequestParams.APP_PROE);
		if (connectStatus.isExpired())
			throw new JLIException("Session has expired");
		if (!connectStatus.isSuccess()) {
			if (connectStatus.getMessage()!=null)
				throw new JLIException(connectStatus.getMessage());
			else
				throw new JLIException("Could not connect");
		}
		Hashtable<String, Object> out = new Hashtable<String, Object>();
		if (connectStatus.getSessionId()!=null) {
			out.put(OUTPUT_SESSIONID, connectStatus.getSessionId());
		}
		return out;
	}

	private Hashtable<String, Object> actionDisconnect(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		connHandler.disconnect(sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionIsCreoRunning(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		boolean running = connHandler.isProeRunning(sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_RUNNING, running);
       	return out;
	}

	private Hashtable<String, Object> actionStartCreo(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String startDir = checkStringParameter(input, PARAM_START_DIR, true);
        String startCommand = checkStringParameter(input, PARAM_START_COMMAND, true);
        int retries = checkIntParameter(input, PARAM_RETRIES, false, -1);
        boolean useDesktop = checkFlagParameter(input, PARAM_USE_DESKTOP, false, false);
//        boolean startTempService = checkFlagParameter(input, PARAM_START_TEMP_SERVICE, false, false);
//        String creosonCommandDir = checkStringParameter(input, PARAM_CREOSON_COMMAND_DIR, false);
//        int port = checkIntParameter(input, PARAM_CREOSON_PORT, false, 0);
        
//        if (startTempService && creosonCommandDir==null) 
//        	throw new JLIException("If "+PARAM_START_TEMP_SERVICE+" is true then "+PARAM_CREOSON_COMMAND_DIR+" must also be set.");
//        if (startTempService && port==0) 
//        	throw new JLIException("If "+PARAM_START_TEMP_SERVICE+" is true then "+PARAM_CREOSON_PORT+" must also be set.");

//        if (startTempService)
//        	startSecondCreoson(startDir, startCommand, retries, useDesktop, creosonCommandDir, port);
//        else
        	connHandler.startProe(startDir, startCommand, retries, useDesktop, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionStopCreo(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		connHandler.stopProe(sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionKillCreo(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
		connHandler.killProe();
		
		return null;
	}

    private void startSecondCreoson(String path, String cmd, int retries, boolean useDesktop, String creosonCommandDir, int port) throws JLIException {

    	File f = null;
    	if (creosonCommandDir!=null)
    		f = new File(creosonCommandDir, ALLOWED_CREOSON_CMD);
    	else
    		f = new File(ALLOWED_CREOSON_CMD);
        if (!f.exists())
            throw new JLIException("Command file '" + f.getAbsolutePath() + "' does not exist");
        if (f.isDirectory())
            throw new JLIException("Command file '" + f.getAbsolutePath() + "' is a directory");

    	StringBuffer buf = new StringBuffer();
    	buf.append("\"");
    	buf.append(f.getAbsolutePath());
    	buf.append("\"");
    	buf.append(" ");
    	if (path!=null) {
        	buf.append("\"");
    		buf.append(PARAM_START_DIR);
    		buf.append("=");
    		buf.append(path);
        	buf.append("\"");
        	buf.append(" ");
    	}
    	if (cmd!=null) {
        	buf.append("\"");
    		buf.append(PARAM_START_COMMAND);
    		buf.append("=");
    		buf.append(cmd);
        	buf.append("\"");
        	buf.append(" ");
    	}
    	buf.append(PARAM_RETRIES);
    	buf.append("=");
    	buf.append(String.valueOf(retries));
    	buf.append(" ");
    	buf.append("port");
    	buf.append("=");
    	buf.append(String.valueOf(port));
    	buf.append(" ");
    	
    	buf.append(" > creoson_start_log.txt");
    	
    	String runcmd = buf.toString();
    	path = creosonCommandDir;
    	
    	System.out.println("Creoson command: "+runcmd);

        try {
	    	if (useDesktop && Desktop.isDesktopSupported()) {
	    		File dir = new File(path);
	    		String oldDir = System.getProperty("user.dir");
	    		System.setProperty("user.dir", dir.getAbsolutePath()); // this does not seem to work for setting creo's working dir
	    		String newDir = System.getProperty("user.dir");
	    		try {
	    			Desktop.getDesktop().open(f);
	    		}
	    		finally {
	    			System.setProperty("user.dir", oldDir);
	    		}
	    	}
	    	else {
	            runcmd = "cmd.exe /c " + cmd;
	            f = new File(path);
	            
	            Runtime rt = Runtime.getRuntime();
	            
	    		Map<String, String> env = System.getenv();
	    		Map<String, String> env2 = new HashMap<String, String>();
	    		env2.put("JSON_PORT", String.valueOf(port));
	    		String[] envp = convertEnv(env, env2);

	    		buf = new StringBuffer();
	    		buf.append("\"");
	    		buf.append(env.get("JAVA_HOME"));
	    		buf.append("\\bin\\java\"");
	    		buf.append(" ");
	    		buf.append("-classpath ");
	    		buf.append("\"");
	    		buf.append(System.getProperty("java.class.path"));
	    		buf.append("\"");
	    	    buf.append(" -Dsli.jlink.timeout=200 ");
	    		buf.append("-Dsli.socket.port=");
	    		buf.append(String.valueOf(port));
	    		buf.append(" ");
	    		buf.append("com.simplifiedlogic.nitro.jshell.MainServer ");

	        	if (path!=null) {
	            	buf.append("\"");
	        		buf.append(PARAM_START_DIR);
	        		buf.append("=");
	        		buf.append(path);
	            	buf.append("\"");
	            	buf.append(" ");
	        	}
	        	if (cmd!=null) {
	            	buf.append("\"");
	        		buf.append(PARAM_START_COMMAND);
	        		buf.append("=");
	        		buf.append(cmd);
	            	buf.append("\"");
	            	buf.append(" ");
	        	}
	        	buf.append(PARAM_RETRIES);
	        	buf.append("=");
	        	buf.append(String.valueOf(retries));
	        	buf.append(" ");
//	        	buf.append(" > d:\\ptc\\githup\\creoson_start_log7.txt");

	        	System.out.println(buf.toString());
	        	
//	            Process pr = rt.exec(runcmd, envp, f.getParentFile());
	            Process pr = rt.exec(buf.toString(), envp, f.getParentFile());

	//           pr.waitFor();
	    	}

        }
        catch (Exception e) {
        	e.printStackTrace();
        	throw new JLIException(e);
        }
    }

	public static String[] convertEnv(Map<String, String> env, Map<String, String> env2) {
		if (env==null)
			return convertEnv(env2, null);
		
		List<String> list = new ArrayList<String>();
		String key, value;
		Iterator<String> iter = env.keySet().iterator();
		while (iter.hasNext()) {
			key = iter.next();
			value = env.get(key);
			key = key.toUpperCase();
			if (env2!=null && env2.get(key)!=null) {
				value = env2.remove(key);
			}
			list.add(key + "=" + value);
		}
		iter = env2.keySet().iterator();
		while (iter.hasNext()) {
			key = iter.next();
			value = env2.get(key);
			key = key.toUpperCase();
			list.add(key + "=" + value);
		}
		int len = list.size();
		String[] list2 = new String[len];
		for (int i=0; i<len; i++) 
			list2[i] = list.get(i);
		return list2;
	}

}
