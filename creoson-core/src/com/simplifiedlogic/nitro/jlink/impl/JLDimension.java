/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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

import java.nio.charset.Charset;
import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitUserAbort;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcModelItem.ParamValueType;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallAssembly;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallComponentDimensionShowInstructions;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallComponentPath;
import com.simplifiedlogic.nitro.jlink.calls.dimension.CallBaseDimension;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItems;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelectionOptions;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelections;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallIntSeq;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.DimData;
import com.simplifiedlogic.nitro.jlink.data.DimSelectData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLDimension;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.ModelItemLooper;

/**
 * @author Adam Andrews
 */
public class JLDimension implements IJLDimension {

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#set(java.lang.String, java.lang.String, java.lang.Object, boolean, java.lang.String)
	 */
    public void set(
    		String modelname, 
    		String dimname, 
    		Object value, 
    		boolean encoded, 
    		String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

        set(modelname, dimname, value, encoded, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#set(java.lang.String, java.lang.String, java.lang.Object, boolean, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void set(
    		String modelname, 
    		String dimname, 
    		Object value, 
    		boolean encoded, 
    		AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("dimension.set: " + dimname + "=" + value, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (dimname==null || dimname.trim().length()==0)
    		throw new JLIException("No dimension name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, modelname, true);
	        
	        CallBaseDimension dim = null;
	        CallParameter param = null;
	        CallParamValue pval = null;
	        int id;
	
	        dim = JlinkUtils.getDimension(m, dimname, false);
	        if (dim==null) {
	            throw new JLIException("No such dimension " + dimname);
	        }
	        pval = dim.getValue();
	        JlinkUtils.setParamValue(m, pval, value, encoded);
	        try {
	            dim.setValue(pval);
	        }
	        catch (Exception e) {
	            id = dim.getId();
	            String pname = "D" + id;
	            param = m.getParam(pname);
	            if (param==null) {
	                throw new JLIException("Cannot modify dimension " + dimname + ", no such parameter " + pname);
	            }
	            pval = param.getValue();
	            JlinkUtils.setParamValue(m, pval, value, encoded);
	            param.setValue(pval);
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("dimension.set," + dimname + "," + value, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#copy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
    public void copy(
    		String modelname, 
    		String dimname, 
    		String to_name,
    		String to_model,
    		String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        copy(modelname, dimname, to_name, to_model, sess);
    }
        
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#copy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void copy(
    		String modelname, 
    		String dimname, 
    		String to_name,
    		String to_model,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("dimension.copy: " + dimname + " to " + to_name, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (dimname==null || dimname.trim().length()==0)
    		throw new JLIException("No dimension name parameter given");
    	if (to_name==null || to_name.trim().length()==0)
    		throw new JLIException("No to-name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        // if the parameters are the same, don't do anything
	        if (dimname.equals(to_name) && (to_model==null || to_model.equals(modelname)))
	            return;
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel from_m;
	        from_m = JlinkUtils.getFile(session, modelname, true);
	
	        CallModel to_m;
	        if (to_model!=null && !to_model.equals(modelname)) {
	            to_m = JlinkUtils.getFile(session, to_model, true);
	        }
	        else
	            to_m = from_m;
	        
	        CallBaseDimension from_dim = JlinkUtils.getDimension(from_m, dimname, false);
	        if (from_dim==null) {
	            throw new JLIException("No such dimension " + dimname);
	        }
	        
	        CallBaseDimension to_dim = JlinkUtils.getDimension(to_m, to_name, false);
	        if (to_dim==null) {
	            throw new JLIException("No such dimension " + to_name);
	        }
	
	        CallParamValue to_pval = to_dim.getValue();
	        CallParamValue from_pval = from_dim.getValue();
	        int to_type = to_pval.getParamValueType();
	        int from_type = from_pval.getParamValueType();
	        if (from_type != to_type)
	            throw new JLIException("Destination dimension exists but is a different type than the source dimension");
	
	        JlinkUtils.setParamValue(to_pval, from_pval);
	
	        try {
	            to_dim.setValue(to_pval);
	        }
	        catch (Exception e) {
	            int id = to_dim.getId();
	            String pname = "D" + id;
	            CallParameter param = to_m.getParam(pname);
	            if (param==null) {
	                throw new JLIException("Cannot modify dimension " + dimname + ", no such parameter " + pname);
	            }
	            to_pval = param.getValue();
	            if (from_type != to_type)
	                throw new JLIException("Destination dimension exists but is a different type than the source dimension");
	            JlinkUtils.setParamValue(to_pval, from_pval);
	            param.setValue(to_pval);
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("dimension.copy,"+dimname+","+to_name, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#list(java.lang.String, java.lang.Object, boolean, java.lang.String)
	 */
    public List<DimData> list(
    		String modelname,
    		String dimName,
    		List<String> dimNames,
    		boolean encoded, 
    		String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
    	return list(modelname, dimName, dimNames, encoded, sess);
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#list(java.lang.String, java.lang.Object, boolean, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
	public List<DimData> list(
    		String modelname,
    		String dimName,
    		List<String> dimNames,
    		boolean encoded, 
    		AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("dimension.list: " + modelname, NitroConstants.DEBUG_KEY);
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
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, modelname, true);
	
	        ListLooper looper = new ListLooper();
	        looper.model = m;
	        if (dimNames!=null)
	        	looper.setNameList(dimNames);
	        else if (dimName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(dimName);
	        looper.encoded = encoded;
	        
	        looper.loop(m);
	        if (looper.output==null)
	            return new Vector<DimData>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("dimension.list,"+modelname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#show(java.lang.String, java.lang.String, java.lang.String, int[], boolean, java.lang.String)
	 */
    public void show(
    		String modelname, 
    		String asmname,
    		String dimname,
    		int[] path,
    		boolean show,
    		String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

        show(modelname, asmname, dimname, path, show, sess);
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLDimension#show(java.lang.String, java.lang.String, java.lang.String, int[], boolean, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public void show(
    		String modelname, 
    		String asmname,
    		String dimname,
    		int[] path,
    		boolean show,
    		AbstractJLISession sess) throws JLIException {
        
		DebugLogging.sendDebugMessage("dimension.show: " + dimname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (dimname==null || dimname.trim().length()==0)
    		throw new JLIException("No dimension name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, modelname, true);
	        CallModel masm = JlinkUtils.getFile(session, asmname, true);
	        
	        CallBaseDimension dim = null;
	
	        dim = JlinkUtils.getDimension(m, dimname, false);
	        if (dim==null) {
	            throw new JLIException("No such dimension " + dimname);
	        }
	        
	        if (show) {
	            if (modelname==null) {
	                dim.show(null);
	            }
	            else {
	                CallComponentDimensionShowInstructions inst = null;
	                if (path!=null) {
	                    if (!(masm instanceof CallAssembly))
	                        throw new JLIException("File '" + masm.getFileName() + "' must be an assembly.");
	
	                    CallComponentPath compPath = null;
	                    CallIntSeq ids = CallIntSeq.create();
	                    // create pro/e component path
	                    int len = path.length;
	                    for (int i=0; i<len; i++) {
	                        ids.append(path[i]);
	                    }
	                    compPath = CallComponentPath.create((CallAssembly)masm, ids);
	                    inst = CallComponentDimensionShowInstructions.create(compPath);
	                }                
	                dim.show(inst);
	            }
	        }
	        else
	            dim.erase();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("dimension.show,"+dimname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDimension#userSelect(java.lang.String, int, java.lang.String)
	 */
	@Override
	public List<DimSelectData> userSelect(String modelname, int max, String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

        return userSelect(modelname, max, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDimension#userSelect(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<DimSelectData> userSelect(String modelname, int max, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("dimension.user_select: " + modelname, NitroConstants.DEBUG_KEY);
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
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, modelname, true);
	
	        List<DimSelectData> output = new Vector<DimSelectData>();

	        boolean complete = false;
	        boolean nosel = true;
	        while (!complete) {
            	CallSelectionOptions selopts = CallSelectionOptions.create("feature,dimension,ref_dim,membfeat");
//	            SelectionOptions selopts = pfcSelect.SelectionOptions_Create(null);
	            selopts.setMaxNumSels(new Integer(max));
	            CallSelections sels = null;
	            try {
	            	sels = session.select(selopts, null);
	            }
	            catch (XToolkitUserAbort e) {
	            	// this exception is thrown when the user cancels the selection
	            	sels = null;
	            }
	            if (sels==null)
	            	break;
	            
	            int len = sels.getarraysize();
	            if (len>0) {
	            	complete=true;
	    	        nosel=false;
	                CallSelection sel;
	            	for (int i=0; i<len; i++) {
	                    sel = sels.get(i);
	                    CallModelItem modelItem = sel.getSelItem();
	                    if (modelItem instanceof CallBaseDimension) {
	                    	CallBaseDimension dim = (CallBaseDimension)modelItem;
	                    	DimSelectData d = new DimSelectData();
	                    	getDimInfo(m, dim, d, false);
	                    	CallModel m2 = sel.getSelModel();
	                    	if (m2!=null) {
	                    		d.setModelname(m2.getFileName());
	                    		d.setRelationId(m2.getRelationId());
	                    	}
	                    	output.add(d);
	                    }
	                    else if (modelItem instanceof CallFeature) {
	                    	CallComponentPath path = sel.getPath();
	    	                CallComponentDimensionShowInstructions inst = null;
	    	                if (path!=null) {
	    	                    inst = CallComponentDimensionShowInstructions.create(path);
	    	                }                
	                    	
	                    	CallModelItems items = ((CallFeature)modelItem).listSubItems(ModelItemType.ITEM_DIMENSION);
	                    	if (items!=null) {
		                    	int dlen = items.getarraysize();
		                    	if (dlen>0) {
		    	            		// repaint to clear last dims
		    	                    CallWindow win = session.getCurrentWindow();
		    	                    if (win!=null)
		    	                    	win.repaint();
		    	                    
			                    	CallBaseDimension dim;
			                    	for (int k=0; k<dlen; k++) {
			                    	    dim = (CallBaseDimension)items.get(k);
			                    	    dim.show(inst);
			                    	}
		                    	}
	                    	}
	                    	
	            	        complete=false;
	                    }
	                    else {
	                    	nosel=true;
	            	        complete=false;
	                    }
	            	}
	            	if (nosel) {
                    	// show assembly dimensions
            	        ShowLooper looper = new ShowLooper();
        	        	looper.loop(m);
	            	}
	            }
	            else
	            	break;
	        }
	        
	        if (output.size()==0)
	        	return null;
	        else
	        	return output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("dimension.user_select,"+modelname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * Get detailed information about a dimension.
     * 
     * @param m The model which contains the dimension
     * @param dim The dimension object
     * @param out The output data which will be updated with the dimension information
     * @param encoded Whether the dimension value should be returned as a byte array 
     * @throws JLIException
     * @throws jxthrowable
     */
    protected void getDimInfo(CallModel m, CallBaseDimension dim, DimData out, boolean encoded) throws JLIException,jxthrowable {
        if (dim==null || out==null)
            return;
        
        CallParamValue pval = dim.getValue();
        Object value = null;
        
        int type = pval.getParamValueType();
        switch (type) {
            case ParamValueType._PARAM_STRING: 
                value = pval.getStringValue();
                if (!encoded) {
                    if (NitroUtils.hasBinary(value.toString()))
                        encoded = true;
                	String v = value.toString();
                    if (NitroUtils.hasBinary(v)) {
                    	//v = SymbolUtils.translateSymbolsToText(v);
                        encoded = NitroUtils.hasBinary(v);
                        value = v;
                    }
                }
                break;
            case ParamValueType._PARAM_INTEGER: 
                value = new Integer(pval.getIntValue());
                break;
            case ParamValueType._PARAM_BOOLEAN: 
                value = new Boolean(pval.getBoolValue());
                break;
            case ParamValueType._PARAM_DOUBLE: 
                value = new Double(pval.getDoubleValue());
                break;
            case ParamValueType._PARAM_NOTE:
                try {
                    CallModelItem item = m.getItemById(ModelItemType.ITEM_NOTE, pval.getNoteId());
                    if (item==null)
                        item = m.getItemById(ModelItemType.ITEM_DTL_NOTE, pval.getNoteId());
                    if (item==null)
                        value = "";
                    else
                        value = item.getName();
                } catch (Exception e) {
                    // if an item does not exist with the ID, an exception is thrown
                    value = "";
                }
                break;
            default:
                value = "";
                break;
        }
        out.setName(dim.getName());
        if (encoded) {
            out.setValue(value.toString().getBytes(Charset.forName("UTF-8")));
            out.setEncoded(true);
        }
        else {
        	out.setValue(value);
            out.setEncoded(false);
        }
    }
    
    /**
     * Implementation of ModelItemLooper which loops over Dimensions and 
     * outputs a list of dimension data
     * @author Adam Andrews
     *
     */
    private class ListLooper extends ModelItemLooper {
        /**
         * The output list of dimension data
         */
        public Vector<DimData> output = null;
        /**
         * Whether to output values as byte arrays
         */
        public boolean encoded = false;
        /**
         * The model that contains the dimensions
         */
        public CallModel model;
        
        /**
         * Default constructor which sets the search type.
         */
        public ListLooper() {
            setSearchType(ModelItemType.ITEM_DIMENSION);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	try {
	            if (currentName==null)
	                currentName = item.getName();
	            if (output==null)
	                output = new Vector<DimData>();

	        	DimData outvals = new DimData();

	            if (item instanceof CallBaseDimension) {
		            CallBaseDimension dim = (CallBaseDimension)item;
		         	getDimInfo(model, dim, outvals, encoded);
	            }

	         	output.add(outvals);
        	}
        	catch (jxthrowable e) {
//        		e.printStackTrace();
        		System.err.println("Error looping through dimensions: " + e.getLocalizedMessage());
        	}
        	return false;
        }
    }

    /**
     * Implementation of ModelItemLooper which loops over Dimensions and 
     * shows the dimensions
     * @author Adam Andrews
     *
     */
    private class ShowLooper extends ModelItemLooper {
        /**
         * Default constructor which sets the search type.
         */
        public ShowLooper() {
            setSearchType(ModelItemType.ITEM_DIMENSION);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
            if (currentName==null)
                currentName = item.getName();
            CallBaseDimension dim = (CallBaseDimension)item;
            dim.show(null);
        	return false;
        }
    }

}
