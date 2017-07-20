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
package com.simplifiedlogic.nitro.jlink.impl;

import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawing;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.view.CallView;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.intf.IJLView;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.ViewLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLView implements IJLView {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLView#list(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> list(String modelName, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return list(modelName, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLView#list(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> list(String modelName, String viewName, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("view.list: " + modelName, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, modelName, false);
	        if (m instanceof CallDrawing) {
	        	if (modelName==null)
	        		throw new JLIException("Active model is a drawing");
	        	else
	        		throw new JLIException("Model is a drawing: " + modelName);
	        }

	        ListLooper looper = new ListLooper();
	        looper.setModel(m);
	        looper.setNamePattern(viewName);
	        looper.loop();
	        
	        return looper.out;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("view.list,"+modelName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLView#activate(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void activate(String modelName, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        activate(modelName, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLView#activate(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void activate(String modelName, String viewName, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("view.activate: " + modelName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, modelName, false);
	        if (m instanceof CallDrawing) {
	        	if (modelName==null)
	        		throw new JLIException("Active model is a drawing");
	        	else
	        		throw new JLIException("Model is a drawing: " + modelName);
	        }
	        
	        CallView view = m.retrieveView(viewName);
	        if (view==null)
	        	throw new JLIException("View " + viewName + " does not exist in model.");
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("view.activate,"+modelName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLView#save(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void save(String modelName, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        save(modelName, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLView#save(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void save(String modelName, String viewName, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("view.save: " + modelName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, modelName, false);
	        if (m instanceof CallDrawing) {
	        	if (modelName==null)
	        		throw new JLIException("Active model is a drawing");
	        	else
	        		throw new JLIException("Model is a drawing: " + modelName);
	        }
	        
	        m.saveView(viewName);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("view.save,"+modelName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * An implementation of ViewLooper which collects a list of view names for a model
     * @author Adam Andrews
     *
     */
    private class ListLooper extends ViewLooper {

        /**
         * An output list of view names
         */
        List<String> out = new Vector<String>();
        
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ViewLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.view.CallView)
		 */
		@Override
		public boolean loopAction(CallView view) throws JLIException, jxthrowable {
        	if (view!=null) {
    	        String name = null;
       			name = view.getName();
                if (name!=null)
                	out.add(name);
        	}
        	return false;
		}
    }

}
