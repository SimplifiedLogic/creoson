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

import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitFound;
import com.ptc.pfc.pfcExceptions.XToolkitInvalidName;
import com.ptc.pfc.pfcLayer.DisplayStatus;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.simplifiedlogic.nitro.jlink.calls.layer.CallLayer;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.LayerData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLLayer;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.ModelItemLooper;
import com.simplifiedlogic.nitro.util.ModelLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLLayer implements IJLLayer {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#delete(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(String filename, String layerName, String sessionId) throws JLIException {
		JLISession sess = JLISession.getSession(sessionId);
        
        delete(filename, layerName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#delete(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void delete(String filename, String layerName, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("layer.delete: " + layerName, NitroConstants.DEBUG_KEY);
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

	        DeleteLooper looper = new DeleteLooper();
        	looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.layerName = layerName;
	        looper.loop();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("layer.delete,"+layerName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#list(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<LayerData> list(String filename, String layerName, String sessionId) throws JLIException {
		JLISession sess = JLISession.getSession(sessionId);
        
        return list(filename, layerName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#list(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<LayerData> list(String filename, String layerName, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("layer.list: " + layerName, NitroConstants.DEBUG_KEY);
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
	
	        CallModel m = JlinkUtils.getFile(session, filename, true);

	        ListLooper looper = new ListLooper();
	        looper.setNamePattern(layerName);
	        
	        looper.loop(m);
	        
	        if (looper.output==null)
	            return new Vector<LayerData>();
	        else
	            return looper.output;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("layer.list,"+layerName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#exists(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean exists(String filename, String layerName, String sessionId) throws JLIException {
		JLISession sess = JLISession.getSession(sessionId);
        
        return exists(filename, layerName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#exists(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public boolean exists(String filename, String layerName, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("layer.exists: " + layerName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallModel m = JlinkUtils.getFile(session, filename, true);

	        ExistsLooper looper = new ExistsLooper();
	        looper.setNamePattern(layerName);
	        
	        looper.loop(m);
	        return looper.exists;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("layer.exists,"+layerName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#show(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public void show(String filename, String layerName, boolean show, String sessionId) throws JLIException {
		JLISession sess = JLISession.getSession(sessionId);
        
        show(filename, layerName, show, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#show(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void show(String filename, String layerName, boolean show, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("layer.show: " + layerName, NitroConstants.DEBUG_KEY);
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
	
	        ShowLooper looper = new ShowLooper();
        	looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.layerName = layerName;
	        looper.show = show;
	        
	        looper.loop();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("layer.show,"+layerName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#create(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void create(String filename, String layerName, String sessionId) throws JLIException {
		JLISession sess = JLISession.getSession(sessionId);
        
        create(filename, layerName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLLayer#create(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void create(String filename, String layerName, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("layer.create: " + layerName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (layerName==null || layerName.trim().length()==0)
    		throw new JLIException("No layer name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel m = JlinkUtils.getFile(session, filename, true);

	        try {
	        	m.createLayer(layerName);
	        }
	    	catch (XToolkitFound e) {
	    		throw new JLIException("Layer already exists: " + layerName);
	    	}
	    	catch (XToolkitInvalidName e) {
	    		throw new JLIException("Invalid layer name: " + layerName);
	    	}
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("layer.delete,"+layerName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * An implementation of ModelLooper which deletes layers in a list of models.  This uses an instance of DeleteLooper2 to loop through layers on each model
     * @author Adam Andrews
     *
     */
	private class DeleteLooper extends ModelLooper {
    	/**
    	 * The layer name or pattern to delete
    	 */
    	String layerName;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			// call a sub-looper to delete layers on the model
	        DeleteLooper2 looper = new DeleteLooper2();
	        looper.setNamePattern(layerName);

	        looper.loop(m);
	        
			return false;
		}
    }

	/**
	 * An implementation of ModelItemLooper which deletes layers from a model
	 * @author Adam Andrews
	 *
	 */
	private static class DeleteLooper2 extends ModelItemLooper {
        /**
         * Constructor needed to initialize model item type
         */
        public DeleteLooper2() {
            setSearchType(ModelItemType.ITEM_LAYER);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallLayer) {
        		CallLayer layer = (CallLayer)item;
	            layer.delete();
        	}

        	return false;
        }
	}

	/**
	 * An implementation of ModelItemLooper which collects data about layers on a model
	 * @author Adam Andrews
	 *
	 */
	private static class ListLooper extends ModelItemLooper {
        /**
         * The output list of layer data
         */
        public Vector<LayerData> output = null;

        /**
         * Constructor needed to initialize model item type
         */
        public ListLooper() {
            setSearchType(ModelItemType.ITEM_LAYER);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallLayer) {
        		CallLayer layer = (CallLayer)item;
	            
	            LayerData outvals = new LayerData();
	            outvals.setLayerId(layer.getId());
	            outvals.setName(layer.getName());
	            outvals.setStatus(JlinkUtils.translateDisplayStatus(layer.getDisplayStatus()));
	            
	            if (output==null)
	            	output = new Vector<LayerData>();
	            output.add(outvals);
        	}

        	return false;
        }
	}

    /**
     * An implementation of ModelLooper which shows or hides layers in a list of models.  This uses an instance of ShowLooper2 to loop through the layers in a model
     * @author Adam Ansrews
     *
     */
	private class ShowLooper extends ModelLooper {
    	/**
    	 * The layer name or pattern to show/hide
    	 */
    	String layerName;
    	/**
    	 * True to show the layers, false to hide them
    	 */
    	boolean show;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			// call a sub-looper to show/hide layers in a model
	        ShowLooper2 looper = new ShowLooper2();
	        looper.setNamePattern(layerName);
	        looper.show = show;
	        
	        looper.loop(m);
	        
			return false;
		}
    }

	/**
	 * An implementation of ModelItemLooper which shows or hides the layers in a model.
	 * @author Adam Andrews
	 *
	 */
	private static class ShowLooper2 extends ModelItemLooper {
    	/**
    	 * True to show the layers, false to hide them
    	 */
		boolean show = false;
		
        /**
         * Constructor needed to initialize model item type
         */
        public ShowLooper2() {
            setSearchType(ModelItemType.ITEM_LAYER);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallLayer) {
        		CallLayer layer = (CallLayer)item;
	            if (show)
	            	layer.setStatus(DisplayStatus.LAYER_NORMAL);
	            else
	            	layer.setStatus(DisplayStatus.LAYER_BLANK);
        	}

        	return false;
        }
	}

	/**
	 * An implementation of ModelItemLooper which checks whether a layer exists
	 * @author Adam Andrews
	 *
	 */
	private static class ExistsLooper extends ModelItemLooper {
		/**
		 * Output value indicating whether the layer exists
		 */
		boolean exists = false;

        /**
         * Constructor needed to initialize model item type
         */
        public ExistsLooper() {
            setSearchType(ModelItemType.ITEM_LAYER);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallLayer) {
    	    	exists=true;
    	    	return true;
        	}

        	return false;
        }
	}

    
}
