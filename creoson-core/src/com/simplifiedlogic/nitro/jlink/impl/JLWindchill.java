/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitBadContext;
import com.ptc.pfc.pfcExceptions.XToolkitFound;
import com.ptc.pfc.pfcExceptions.XToolkitNotFound;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.server.CallServer;
import com.simplifiedlogic.nitro.jlink.calls.server.CallWorkspaceDefinition;
import com.simplifiedlogic.nitro.jlink.calls.server.CallWorkspaceDefinitions;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLFile;
import com.simplifiedlogic.nitro.jlink.intf.IJLWindchill;
import com.simplifiedlogic.nitro.jlink.intf.JShellProvider;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.WorkspaceFileLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLWindchill implements IJLWindchill {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#authorize(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void authorize(String user, String password, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        authorize(user, password, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#authorize(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void authorize(String user, String password, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.authorize: " + user, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (user==null || user.trim().length()==0)
    		throw new JLIException("No user parameter given");
    	if (password==null || password.trim().length()==0)
    		throw new JLIException("No password parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        session.authenticateBrowser(user, password);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.authorize: " + user, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#setServer(java.lang.String, java.lang.String)
	 */
	@Override
	public void setServer(String serverUrl, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        setServer(serverUrl, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#setServer(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void setServer(String serverUrl, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.set_server: " + serverUrl, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (serverUrl==null || serverUrl.trim().length()==0)
    		throw new JLIException("No server URL parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        /* Debugging code for getting a list of servers
    		Servers servers = session.getSession().ListServers();
    		System.out.println("Servers:");
    		if (servers!=null) {
        		int len = servers.getarraysize();
        		for (int i=0; i<len; i++) {
        			Server svr = servers.get(i);
        			System.out.println("    " + svr.GetAlias() + " : " + svr.GetLocation() + ", active=" + svr.GetIsActive());
        		}
    		}
    		else {
    			System.out.println("    No servers found.");
    		}
    		*/

    		CallServer activeServer = session.getActiveServer();
	        CallServer server = null;
	        if (serverUrl!=null) {
		        if (NitroUtils.isValidURL(serverUrl)) {
		        	server = session.getServerByUrl(serverUrl, null);
			        if (server==null)
			        	throw new JLIException("Could not find server for URL " + serverUrl);
		        }
		        else {
		        	server = session.getServerByAlias(serverUrl);
			        if (server==null)
			        	throw new JLIException("Could not find server for Alias " + serverUrl);
		        }
		        
		        if (activeServer==null || !server.getAlias().equals(activeServer.getAlias())) {
		        	server.activate();
		        }
	        }

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.set_server: " + serverUrl, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#serverExists(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean serverExists(String serverUrl, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        return serverExists(serverUrl, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#serverExists(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public boolean serverExists(String serverUrl, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.server_exists: " + serverUrl, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (serverUrl==null || serverUrl.trim().length()==0)
    		throw new JLIException("No server parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallServer server = null;
	        if (NitroUtils.isValidURL(serverUrl)) {
	        	server = session.getServerByUrl(serverUrl, null);
	        }
	        else {
	        	server = session.getServerByAlias(serverUrl);
	        }

			return server!=null;

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.server_exists: " + serverUrl, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#listWorkspaces(java.lang.String)
	 */
	@Override
	public List<String> listWorkspaces(String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        return listWorkspaces(sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#listWorkspaces(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> listWorkspaces(AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.list_workspaces", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

	        CallWorkspaceDefinitions workspaces = server.collectWorkspaces();
	        if (workspaces==null) {
	        	return null;
	        }

	        List<String> out = new ArrayList<String>();
	        int len = workspaces.getarraysize();
	        for (int i=0; i<len; i++) {
	        	CallWorkspaceDefinition wdef = workspaces.get(i);
	        	String wname = wdef.getWorkspaceName();
	        	out.add(wname);
	        }
	        
	        if (out.size()==0)
	        	return null;
	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.list_workspaces", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#setWorkspace(java.lang.String, java.lang.String)
	 */
	@Override
	public void setWorkspace(String workspace, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        setWorkspace(workspace, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#setWorkspace(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void setWorkspace(String workspace, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.set_workspace: " + workspace, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (workspace==null || workspace.trim().length()==0)
    		throw new JLIException("No workspace parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

	        String activeWorkspace = server.getActiveWorkspace();
	    	if (activeWorkspace==null || !activeWorkspace.equals(workspace)) {
	    		if (!workspaceExists(server, workspace))
	    			throw new JLIException("Workspace '" + workspace + "' does not exist on the active server");

	    		IJLFile fileHandler = JShellProvider.getInstance().getJLFile();
	    		if (fileHandler!=null)
	    			fileHandler.erase(null, null, true, sess);
				server.setActiveWorkspace(workspace);
	    	}

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.set_workspace: " + workspace, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#getWorkspace(java.lang.String)
	 */
	@Override
	public String getWorkspace(String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        return getWorkspace(sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#getWorkspace(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String getWorkspace(AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.get_workspace", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

	        String activeWorkspace = server.getActiveWorkspace();

	        return activeWorkspace;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.get_workspace", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#workspaceExists(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean workspaceExists(String workspace, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        return workspaceExists(workspace, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#workspaceExists(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public boolean workspaceExists(String workspace, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.workspace_exists: " + workspace, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (workspace==null || workspace.trim().length()==0)
    		throw new JLIException("No workspace parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

			boolean exists = workspaceExists(server, workspace);

			return exists;

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.workspace_exists: " + workspace, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#createWorkspace(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void createWorkspace(String workspace, String context, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        createWorkspace(workspace, context, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#createWorkspace(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void createWorkspace(String workspace, String context, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.create_workspace: " + workspace, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (workspace==null || workspace.trim().length()==0)
    		throw new JLIException("No workspace parameter given");
    	if (context==null || context.trim().length()==0)
    		throw new JLIException("No context parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

			if (workspaceExists(server, workspace))
				throw new JLIException("Workspace '" + workspace + "' already exists");

			CallWorkspaceDefinition wdef = CallWorkspaceDefinition.create(workspace, context);
	        server.createWorkspace(wdef);
			
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.create_workspace: " + workspace, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#clearWorkspace(java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public void clearWorkspace(String workspace, List<String> filenames, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        clearWorkspace(workspace, filenames, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#clearWorkspace(java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void clearWorkspace(String workspace, List<String> filenames, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.clear_workspace: " + workspace, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (workspace==null || workspace.trim().length()==0)
    		throw new JLIException("No workspace parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

			if (!workspaceExists(server, workspace))
				throw new JLIException("Workspace '" + workspace + "' does not exist");
			
			CallStringSeq seq = null;
			if (filenames!=null && filenames.size()>0) {
				seq = CallStringSeq.create();
				for (String name : filenames) {
					seq.append(name);
				}
			}

    		String activeWorkspace = server.getActiveWorkspace();
	    	if (activeWorkspace==null || !activeWorkspace.equals(workspace))
	    		server.setActiveWorkspace(workspace);

	    	try {
	    		server.removeObjects(seq);
	    	}
	    	catch (XToolkitNotFound e) {
	    		// ignore "not found" error
	    	}
	    	catch (XToolkitFound e) {
	    		// ignore "found" error
	    	}
	    	finally {
		    	if (activeWorkspace!=null && !activeWorkspace.equals(workspace))
		    		server.setActiveWorkspace(activeWorkspace);
	    	}
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.clear_workspace: " + workspace, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#deleteWorkspace(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteWorkspace(String workspace, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        deleteWorkspace(workspace, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#deleteWorkspace(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void deleteWorkspace(String workspace, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.delete_workspace: " + workspace, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (workspace==null || workspace.trim().length()==0)
    		throw new JLIException("No workspace parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

    		String activeWorkspace = server.getActiveWorkspace();
	    	if (activeWorkspace!=null && activeWorkspace.equals(workspace))
				throw new JLIException("Cannot delete the active workspace");

    		if (!workspaceExists(server, workspace))
    			throw new JLIException("Workspace '" + workspace + "' does not exist on the active server");

	        server.deleteWorkspace(workspace);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.delete_workspace: " + workspace, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#fileCheckedOut(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean fileCheckedOut(String workspace, String fileName, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        return fileCheckedOut(workspace, fileName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#fileCheckedOut(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public boolean fileCheckedOut(String workspace, String fileName, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.file_checked_out: " + fileName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (workspace==null || workspace.trim().length()==0)
    		throw new JLIException("No workspace parameter given");

    	if (fileName==null || fileName.trim().length()==0)
    		throw new JLIException("No file name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

	        try {
				boolean checkedOut = server.isObjectCheckedOut(workspace, fileName);
	
				return checkedOut;
	        }
	        catch (XToolkitNotFound e) {
	        	return false;
	        }
	        catch (XToolkitBadContext e) {
	        	return false;
	        }

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.file_checked_out: " + fileName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#listWorkspaceFiles(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> listWorkspaceFiles(String filename, String workspace, String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        return listWorkspaceFiles(filename, workspace, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLWindchill#listWorkspaceFiles(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> listWorkspaceFiles(String filename, String workspace, AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("windchill.list_workspace_files: " + workspace, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (workspace==null || workspace.trim().length()==0)
    		throw new JLIException("No workspace parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallServer server = session.getActiveServer();
	        if (server==null)
	        	throw new JLIException("No server is selected.");

			if (!workspaceExists(server, workspace))
				throw new JLIException("Workspace '" + workspace + "' does not exist");

	        ListLooper looper = new ListLooper();
            looper.setNamePattern(filename);
	        looper.setSession(session);
	        looper.setServerAlias(server.getAlias());
	        looper.setWorkspace(workspace);
	        looper.loop();
	        if (looper.output==null)
	            return new Vector<String>();
	        else
	        	return looper.output;

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("windchill.list_workspace_files: " + workspace, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * Check whether a given workspace exists on a server
     * @param server The server containing the workspace
     * @param workspace Workspace name
     * @return Whether the workspace exists on the server
     * @throws jxthrowable
     */
    private boolean workspaceExists(CallServer server, String workspace) throws jxthrowable {
        CallWorkspaceDefinitions workspaces = server.collectWorkspaces();
        if (workspaces==null)
        	return false;
        int len = workspaces.getarraysize();
        boolean found=false;
        for (int i=0; i<len; i++) {
        	CallWorkspaceDefinition wdef = workspaces.get(i);
        	String wname = wdef.getWorkspaceName();
        	if (workspace!=null && workspace.equals(wname)) {
        		found=true;
        	}
        }
        return found;
    }

    /**
     * An implementation of WorkspaceFileLooper which gets a list of 
     * workspace file names
     * @author Adam Andrews
     */
    private static class ListLooper extends WorkspaceFileLooper {
        /**
         * An output list of model names
         */
        public List<String> output = null;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.WorkspaceFileLooper#loopAction(java.lang.String)
		 */
		@Override
		public boolean loopAction(String fileName) throws JLIException, jxthrowable {
            if (output==null)
                output = new Vector<String>();
            output.add(fileName);

			return false;
		}
        
    }
}
