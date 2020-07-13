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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ParamValueType;
import com.simplifiedlogic.nitro.jlink.DataUtils;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParamValue;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameterOwner;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.ParameterCollData;
import com.simplifiedlogic.nitro.jlink.data.ParameterCollFileData;
import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.jlink.data.ParameterDeleteData;
import com.simplifiedlogic.nitro.jlink.data.ParameterDesignateData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.ModelLooper;
import com.simplifiedlogic.nitro.util.ParamListLooper;
import com.simplifiedlogic.nitro.util.ParamLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLParameter implements IJLParameter {

    /**
     * Maximum length of a parameter value
     */
    public static final int VALUE_LIMIT = 80;
    /**
     * Maximum length of a parameter name
     */
    public static final int PARAMETER_LIMIT = 31;
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#set(java.lang.String, java.lang.String, java.lang.Object, java.lang.String, int, boolean, boolean, java.lang.String)
     */
    @Override
	public void set(
			String filename,
			String paramName,
			Object value,
			String type,
			int designate,
			boolean encoded,
			boolean noCreate,
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        set(filename, paramName, value, type, designate, encoded, noCreate, sess);
	}
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#set(java.lang.String, java.lang.String, java.lang.Object, java.lang.String, int, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
	public void set(
			String filename,
			String paramName,
			Object value,
			String type,
			int designate,
			boolean encoded,
			boolean noCreate,
			AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("parameter.set: " + paramName + "=" + value, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (paramName==null || paramName.trim().length()==0)
    		throw new JLIException("No parameter name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        paramName = validateParameter(paramName, value, encoded);
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        SetLooper looper = new SetLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.setDebugKey(NitroConstants.DEBUG_SET_PARAM_KEY);
	        looper.paramName = paramName;
	        looper.value = value;
	        looper.type = type;
	        looper.designate = designate;
	        looper.encoded = encoded;
	        looper.noCreate = noCreate;
	        looper.loop();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("parameter.set," + paramName + "," + value, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}		
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#setBatch(com.simplifiedlogic.nitro.jlink.data.ParameterBatchData, boolean, boolean, java.lang.String)
     */
    @Override
	public void setBatch(
			ParameterCollData batch, 
			boolean encoded,
			boolean noCreate,
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        setBatch(batch, encoded, noCreate, sess);
	}
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#setBatch(com.simplifiedlogic.nitro.jlink.data.ParameterBatchData, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
	public void setBatch(
			ParameterCollData batch, 
			boolean encoded,
			boolean noCreate,
			AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("parameter.setBatch", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (batch==null || batch.size()==0)
    		throw new JLIException("No batch parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
    		for (ParameterCollFileData file : batch.getFiles()) {
    			if (file.size()==0)
    				continue;
    			for (ParameterData param : file.getParams())
    		        param.setName(validateParameter(param.getName(), param.getValue(), encoded));
    		}
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        SetBatchLooper looper = new SetBatchLooper();
            looper.setNameList(batch.getFileNames());
	        looper.setDefaultToActive(false);
	        looper.setSession(session);
	        looper.setDebugKey(NitroConstants.DEBUG_SET_PARAM_KEY);
	        looper.batch = batch;
	        looper.encoded = encoded;
	        looper.noCreate = noCreate;
	        looper.loop();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("parameter.setBatch", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}		
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#setDesignated(java.lang.String, java.lang.String, boolean, java.lang.String)
     */
    @Override
	public void setDesignated(
			String filename,
			String paramName,
			boolean designate,
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        setDesignated(filename, paramName, designate, sess);
	}
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#setDesignated(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
	public void setDesignated(
			String filename,
			String paramName,
			boolean designate,
			AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("parameter.set_designated: " + paramName + "=" + designate, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (paramName==null || paramName.trim().length()==0)
    		throw new JLIException("No parameter name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        SetDesignateLooper looper = new SetDesignateLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.setDebugKey(NitroConstants.DEBUG_SET_PARAM_KEY);
	        looper.paramName = paramName;
	        looper.designate = designate;
	        looper.loop();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("parameter.set_designated," + paramName + "," + designate, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}		
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#delete(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void delete(
			String filename,
			String paramName,
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        delete(filename, paramName, sess);
	}
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#delete(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	public void delete(
			String filename,
			String paramName,
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("parameter.delete: " + paramName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (paramName==null || paramName.trim().length()==0)
    		throw new JLIException("No parameter name parameter given");

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
        	looper.paramName = paramName;

        	looper.loop();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("parameter.delete,"+paramName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#copy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, java.lang.String)
	 */
	public void copy(
			String filename, 
			String paramName,
			String toModel, 
			String toName, 
			int designate,
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        copy(filename, paramName, toModel, toName, designate, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#copy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	public void copy(
			String filename, 
			String paramName,
			String toModel, 
			String toName, 
			int designate,
			AbstractJLISession sess) throws JLIException {
		

		DebugLogging.sendDebugMessage("parameter.copy: " + paramName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (paramName==null || paramName.trim().length()==0)
    		throw new JLIException("No source parameter name parameter given");
    	// toName is allowed to be null
//    	if (toName==null || toName.trim().length()==0)
//    		throw new JLIException("No target parameter name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        toName = validateParameter(toName, null, false);
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, true);

	        BasicListLooper looper = new BasicListLooper();
	        if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        looper.loop(m);

	        if (looper.params==null || looper.params.size()==0)
	            throw new JLIException("No matching parameters: " + paramName);
	        
	        CopyLooper looper2 = new CopyLooper();
            looper2.setNamePattern(toModel);
	        looper2.setDefaultToActive(true);
	        looper2.setSession(session);
	        looper2.inputs = looper.params;
	        looper2.toName = toName;
	        looper2.designate = designate;
	        looper2.exceptFileName = m.getFileName();
	        looper2.loop();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("parameter.copy,"+paramName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}


    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLParameter#list(java.lang.String, java.lang.String, java.util.List, boolean, java.lang.String)
	 */
    public List<ParameterData> list(
	        String filename,
	        String paramName, 
	        List<String> paramNames, 
	        String valuePattern, 
	        boolean encoded,
	        String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return list(filename, paramName, paramNames, valuePattern, encoded, sess);
    }
    	
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLParameter#list(java.lang.String, java.lang.String, java.util.List, boolean, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public List<ParameterData> list(
	        String filename,
	        String paramName, 
	        List<String> paramNames, 
	        String valuePattern, 
	        boolean encoded,
	        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("parameter.list: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        ListLooper looper = new ListLooper();
	        looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.paramName = paramName;
	        looper.paramNames = paramNames;
	        looper.encoded = encoded;
	        looper.valuePattern = valuePattern;

	        looper.loop();
	        if (looper.output==null)
	            return new Vector<ParameterData>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("parameter.list,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }	

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#exists(java.lang.String, java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public boolean exists(String filename, String paramName, List<String> paramNames, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exists(filename, paramName, paramNames, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLParameter#exists(java.lang.String, java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public boolean exists(String filename, String paramName, List<String> paramNames, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("parameter.exists: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);
	
	        ExistsLooper looper = new ExistsLooper();
	        if (paramNames!=null)
	        	looper.setNameList(paramNames);
	        else if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        
	        looper.loop(m);
	        return looper.exists;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("parameter.exists,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * Validate a parameter name and value; checks that <ul>
     * <li>The name is less than PARAMETER_LIMIT in length
     * <li>The name does not contain any invalid characters
     * <li>The value is less than VALUE_LIMIT in length
     * </ul>
     * It will also reformat the parameter name (convert it to upper case)
     * 
     * @param param The parameter name to check
     * @param value The parameter value to check (optional)
     * @param encoded Whether the parameter value is Base64-encoded
     * @return The reformatted parameter name 
     * @throws JLIException
     */
    protected static String validateParameter(String param, Object value, boolean encoded) throws JLIException {
        
        if (param==null) return null;
        
        String upper = param.toUpperCase();
        
        int len = upper.length();
        if (len > PARAMETER_LIMIT)
            throw new JLIException("parameter name exceeds maximum length allowed");

        NitroUtils.validateNameChars(param);

        if (value!=null) {
            String v = DataUtils.getStringValue(value, encoded);
            if (v.length() > VALUE_LIMIT) {
                throw new JLIException("parameter value exceeds maximum length allowed");
            }
        }
        
        return upper;
    }
    
    /**
     * Copy a single parameter to a parameter in another model or to another parameter in the same model
     * @param param The parameter object to copy
     * @param m The model containing the target parameter
     * @param toName The name of the target parameter
     * @param designate Whether the target parameter should be designated/not designated/not changed.  Valid values are DESIGNATE_ON, DESIGNATE_OFF, and DESIGNATE_UNKNOWN.
     * @throws JLIException
     * @throws jxthrowable
     */
    public void copyOneParam(CallParameter param, CallModel m, String toName, int designate) throws JLIException, jxthrowable {
		String paramName = param.getName();
		if (toName==null)
			toName = paramName;
		
		CallParameter to_param = m.getParam(toName);
        if (to_param!=null) {
            if (param.getValue().getParamValueType() != to_param.getValue().getParamValueType())
                throw new JLIException("Destination parameter exists but is a different type than the source parameter");
        }

        int type = param.getValue().getParamValueType();
        if (to_param!=null) {
        	// update an existing parameter
        	CallParamValue pval = to_param.getValue();
            switch (type) {
                case ParamValueType._PARAM_STRING:
                    pval.setStringValue(param.getValue().getStringValue());
                    break;
                case ParamValueType._PARAM_DOUBLE:
                    pval.setDoubleValue(param.getValue().getDoubleValue());
                    break;
                case ParamValueType._PARAM_INTEGER:
                    pval.setIntValue(param.getValue().getIntValue());
                    break;
                case ParamValueType._PARAM_BOOLEAN:
                    pval.setBoolValue(param.getValue().getBoolValue());
                    break;
                case ParamValueType._PARAM_NOTE:
                    pval.setNoteId(param.getValue().getNoteId());
                    break;
            }
            to_param.setValue(pval);
        }        
        else {
        	// create a new parameter
        	CallParamValue pval = null;
            switch (type) {
                case ParamValueType._PARAM_STRING:
                	pval = CallParamValue.createStringParamValue(param.getValue().getStringValue());
                    break;
                case ParamValueType._PARAM_DOUBLE:
                	pval = CallParamValue.createDoubleParamValue(param.getValue().getDoubleValue());
                    break;
                case ParamValueType._PARAM_INTEGER:
                	pval = CallParamValue.createIntParamValue(param.getValue().getIntValue());
                    break;
                case ParamValueType._PARAM_BOOLEAN:
                	pval = CallParamValue.createBoolParamValue(param.getValue().getBoolValue());
                    break;
                case ParamValueType._PARAM_NOTE:
                    pval = CallParamValue.createNoteParamValue(param.getValue().getNoteId());
                    break;
            }
            
            if (pval==null)
                throw new JLIException("Unable to create parameter value");
            
            to_param = m.createParam(toName, pval);
            if (to_param==null)
                throw new JLIException("Unable to create parameter");
        }

        if (designate!=DESIGNATE_UNKNOWN) {
            if (designate==DESIGNATE_ON) {
            	// make target parameter designated
                if (!to_param.getIsDesignated())
                    to_param.setIsDesignated(true);
            }
            else {
            	// make target parameter not designated
                if (to_param.getIsDesignated())
                    to_param.setIsDesignated(false);
            }
        }
        else {
        	// copy the original parameter's designated status
            boolean des = param.getIsDesignated();
            if (des!=to_param.getIsDesignated())
            	to_param.setIsDesignated(des);
        }
    }
    
	/**
	 * Set or create a single parameter on a model or feature
	 * @param model The model which contains the parameter (even if it's a feature parameter)
	 * @param owner The owner of the parameter.  If this is a model parameter, then this will be the same as the "model" argument.  If it's a feature parameter, this will be the feature.
	 * @param paramName The parameter name
	 * @param value The new parameter value
	 * @param type The parameter data type
	 * @param designate Whether the parameter should be designated/not designated/not changed.  Valid values are DESIGNATE_ON, DESIGNATE_OFF, and DESIGNATE_UNKNOWN.
	 * @param noCreate Whether to NOT create the parameter if it does not exist
	 * @param encoded Whether the parameter value is Base64-encoded
	 * @throws JLIException
	 * @throws jxthrowable
	 */
	public static void setOneParameter(CallModel model, CallParameterOwner owner, String paramName, Object value, String type, int designate, boolean noCreate, boolean encoded) throws JLIException, jxthrowable {
		long start;
		start = System.currentTimeMillis();
		CallParameter param = owner.getParam(paramName);
        DebugLogging.sendTimerMessage("jlink.GetParam," + paramName, start, NitroConstants.DEBUG_SET_PARAM_KEY);
        if (param!=null) {
        	start = System.currentTimeMillis();
        	CallParamValue pval = param.getValue();
	        DebugLogging.sendTimerMessage("jlink.GetValue", start, NitroConstants.DEBUG_SET_PARAM_KEY);

	        start = System.currentTimeMillis();
            JlinkUtils.setParamValue(model, pval, value, encoded);
	        DebugLogging.sendTimerMessage("jlink.SetTypeValue", start, NitroConstants.DEBUG_SET_PARAM_KEY);
	        
        	start = System.currentTimeMillis();
            param.setValue(pval);
	        DebugLogging.sendTimerMessage("jlink.SetValue", start, NitroConstants.DEBUG_SET_PARAM_KEY);
        }
        else if (noCreate) {
        	throw new JLIException("Parameter " + paramName + " does not exist, cannot set");
        }
        else {
            if (type==null) type=TYPE_STRING;
            CallParamValue pval=null;
            try {
	        	start = System.currentTimeMillis();
	            if (type.equalsIgnoreCase(TYPE_STRING)) {
	            	if (value==null)
	            		pval = CallParamValue.createStringParamValue("");
	            	else {
	            		String val = DataUtils.getStringValue(value, encoded);
		                pval = CallParamValue.createStringParamValue(val);
	            	}
	            }
	            else if (type.equalsIgnoreCase(TYPE_DOUBLE)) 
	                pval = CallParamValue.createDoubleParamValue(value==null?0.0:DataUtils.getDoubleValue(value, encoded));
	            else if (type.equalsIgnoreCase(TYPE_INTEGER)) 
	                pval = CallParamValue.createIntParamValue(value==null?0:DataUtils.getIntValue(value, encoded));
	            else if (type.equalsIgnoreCase(TYPE_BOOL)) 
	                pval = CallParamValue.createBoolParamValue(value==null?false:DataUtils.getBooleanValue(value, encoded));
	            else if (type.equalsIgnoreCase(TYPE_NOTE)) {
	            	CallModelItem item = JlinkUtils.getNote(model, value==null?"":value.toString());
	                if (item==null)
	                    throw new JLIException("Cannot find note:" + value.toString().toUpperCase());
	                pval = CallParamValue.createNoteParamValue(item.getId());
	            }
	            else 
	                throw new JLIException("Unsupported value type: " + type);
		        DebugLogging.sendTimerMessage("jlink.CreateTypeParamValue", start, NitroConstants.DEBUG_SET_PARAM_KEY);
        	}
        	catch (JLIException e) {
        		throw e;
        	}
        	catch (Exception e) {
        		throw new JLIException("Unable to set data value " + value + " for type " + type + ": " + e.getMessage());
        	}

            if (pval==null)
                throw new JLIException("Unable to create parameter value");
            
        	start = System.currentTimeMillis();
            param = owner.createParam(paramName, pval);
	        DebugLogging.sendTimerMessage("jlink.CreateParam", start, NitroConstants.DEBUG_SET_PARAM_KEY);
            if (param==null)
                throw new JLIException("Unable to create parameter");

        	start = System.currentTimeMillis();
            param.setIsDesignated(false);
	        DebugLogging.sendTimerMessage("jlink.SetIsDesignated,false", start, NitroConstants.DEBUG_SET_PARAM_KEY);
        }
        
        if (designate!=DESIGNATE_UNKNOWN) {
	        if (designate==DESIGNATE_ON) {
	        	// make parameter designated
	            if (!param.getIsDesignated()) {
		        	start = System.currentTimeMillis();
	            	param.setIsDesignated(true);
			        DebugLogging.sendTimerMessage("jlink.SetIsDesignated,true", start, NitroConstants.DEBUG_SET_PARAM_KEY);
	            }
	        }
	        else {
	        	// make parameter not designated
	            if (param.getIsDesignated()) {
		        	start = System.currentTimeMillis();
	            	param.setIsDesignated(false);
			        DebugLogging.sendTimerMessage("jlink.SetIsDesignated,false", start, NitroConstants.DEBUG_SET_PARAM_KEY);
	            }
	        }
        }
	}

	/**
	 * An implementation of ParamLooper which checks whether a parameter exists
	 * @author Adam Andrews
	 *
	 */
	public class ExistsLooper extends ParamLooper {
		/**
		 * Output value indicating whether the parameter exists
		 */
		boolean exists = false;
		
	    /* (non-Javadoc)
	     * @see com.simplifiedlogic.nitro.util.ParamLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter)
	     */
	    public boolean loopAction(CallParameter p) throws JLIException, jxthrowable {
	    	exists=true;
	    	return true;
	    }
    }
    
    /**
     * An implementation of ModelLooper which sets one parameter on a list of models
     * @author Adam Andrews
     *
     */
    public class SetLooper extends ModelLooper {
		/**
		 * Parameter name to set
		 */
		String paramName;
		/**
		 * Parameter value to set
		 */
		Object value;
		/**
		 * Parameter data type
		 */
		String type;
		/**
		 * Whether the parameter should be designated/not designated/not changed.  Valid values are DESIGNATE_ON, DESIGNATE_OFF, and DESIGNATE_UNKNOWN.
		 */
		int designate;
		/**
		 *  Whether to NOT create the parameter if it does not exist
		 */
		boolean noCreate;
		/**
		 * Whether the value is Base64-encoded
		 */
		boolean encoded;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			setOneParameter(m, m, paramName, value, type, designate, noCreate, encoded);
			return false;
		}
    	
    }

    /**
     * An implementation of ModelLooper which sets a list of parameters on a list of models
     * @author Adam Andrews
     *
     */
    public class SetBatchLooper extends ModelLooper {
    	
    	/**
    	 * The structure of files/parameters to set
    	 */
    	ParameterCollData batch;
		/**
		 *  Whether to NOT create the parameter if it does not exist
		 */
		boolean noCreate;
		/**
		 * Whether the values are Base64-encoded
		 */
		boolean encoded;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			String name = m.getFileName();
			ParameterCollFileData fileData = null;
			for (ParameterCollFileData file : batch.getFiles()) {
				if (file.getFileName().equalsIgnoreCase(name)) {
					fileData = file;
					break;
				}
			}
			if (fileData==null)
				return false;
			if (fileData.size()==0)
				return false;
			for (ParameterData param : fileData.getParams()) {
				if (param instanceof ParameterDesignateData) {
					CallParameter p = m.getParam(param.getName());
					if (p!=null) {
						boolean designate = param.isDesignate();
				        if (designate) {
				        	// make parameter designated
				            if (!p.getIsDesignated()) {
				            	p.setIsDesignated(true);
				            }
				        }
				        else {
				        	// make parameter not designated
				            if (p.getIsDesignated()) {
				            	p.setIsDesignated(false);
				            }
				        }
					}
				}
				else if (param instanceof ParameterDeleteData) {
					CallParameter p = m.getParam(param.getName());
					if (p!=null)
						p.delete();
				}
				else {
					setOneParameter(m, m, param.getName(), param.getValue(), param.getType(), param.getSetDesignate(), noCreate, encoded);
				}
			}
			return false;
		}
    	
    }

    /**
     * An implementation of ModelLooper which sets a parameter's designated state 
     * on a list of models
     * @author Adam Andrews
     *
     */
    public class SetDesignateLooper extends ModelLooper {
		/**
		 * Parameter name to set
		 */
		String paramName;
		/**
		 * Whether the parameter should be designated/not designated.
		 */
		boolean designate;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			SetDesignateLooper2 looper = new SetDesignateLooper2();
	        if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        looper.model = m;
	        looper.designate = designate;
	        looper.loop(m);
			return false;
		}
    	
    }

    /**
     * An implementation of ParamLooper which sets designate state of 
     * matching parameters on a model
     * @author Adam Andrews
     *
     */
    public class SetDesignateLooper2 extends ParamLooper {
    	/**
    	 * The model containing the parameters to delete
    	 */
    	CallModel model;
		/**
		 * Whether the parameter should be designated/not designated.
		 */
		boolean designate;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ParamLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter)
         */
        public boolean loopAction(CallParameter p) throws JLIException, jxthrowable {
        	//System.err.println("Setting model "+model.getFileName()+" param "+p.getName()+" designate: "+designate);
	        if (designate) {
	        	// make parameter designated
	            if (!p.getIsDesignated()) {
	            	p.setIsDesignated(true);
	            }
	        }
	        else {
	        	// make parameter not designated
	            if (p.getIsDesignated()) {
	            	p.setIsDesignated(false);
	            }
	        }
            return false;
        }
    }

    /**
	 * An implementation of ParamLooper which just collects parameter objects for a model
     * @author Adam Andrews
     *
     */
    public class BasicListLooper extends ParamLooper {
    	/**
    	 * An output list of parameter objects
    	 */
    	List<CallParameter> params = null;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ParamLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter)
         */
        public boolean loopAction(CallParameter p) throws JLIException, jxthrowable {
            if (currentName==null)
                currentName = p.getName();
            
            // throw an error if a space is encountered
            if (currentName.trim().indexOf(' ')>=0) {
                throw new JLIException("Found a parameter name that contains a space:" + currentName);
            }
            
            if (params==null)
                params = new ArrayList<CallParameter>();
        	
            params.add(p);
            return false;
        }
    }

    /**
     * An implementation of ModelLooper which copies one or more parameters to a list of models
     * @author Adam Andrews
     *
     */
    public class CopyLooper extends ModelLooper {
		/**
		 * An inpu list of parameters to copy
		 */
		List<CallParameter> inputs;
		/**
		 * The new parameter name.  If there is more than one parameter being copied, this argument is ignored.
		 */
		String toName;
		/**
		 * Whether the parameter should be designated/not designated/not changed.  Valid values are DESIGNATE_ON, DESIGNATE_OFF, and DESIGNATE_UNKNOWN.
		 */
		int designate;
		/**
		 * Name of a model to skip over when looping.  Intended to use to skip over the model that currently contains the parameter.
		 */
		String exceptFileName;

		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			if (inputs.size()>1) {
				toName=null;
				designate=IJLParameter.DESIGNATE_UNKNOWN;
			}
			if (exceptFileName!=null && m.getFileName().equals(exceptFileName)) {
				if (namePattern!=null && !namePattern.equalsIgnoreCase(m.getFileName()))
					return false;
			}

			for (CallParameter param : inputs) {
				copyOneParam(param, m, toName, designate);
			}
			return false;
		}
    }
    
    /**
     * An implementation of ModelLooper which deletes parameters from a list of models.  This uses and instance of DeleteLooper2 to delete the parameters on one model.
     * @author Adam Andrews
     *
     */
    public class DeleteLooper extends ModelLooper {
    	/**
    	 * The parameter name or pattern to delete
    	 */
    	String paramName;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
	        DeleteLooper2 looper = new DeleteLooper2();
	        if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        looper.model = m;
	        looper.loop(m);
			return false;
		}
    }

    /**
     * An implementation of ParamLooper which deletes matching parameters on a model
     * @author Adam Andrews
     *
     */
    public class DeleteLooper2 extends ParamLooper {
    	/**
    	 * The model containing the parameters to delete
    	 */
    	CallModel model;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ParamLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter)
         */
        public boolean loopAction(CallParameter p) throws JLIException, jxthrowable {
            try {
                p.delete();
            }
            catch (Exception e) {
                throw new JLIException("Could not delete parameter " + p.getName() + " from model " + model.getFileName());
            }
            return false;
        }
    }

    /**
     * An implementation of ModelLooper which collects information about parameters on a list of models.  This calls an instance of ParamListLooper to list the parameters on one model.
     * @author Adam Andrews
     *
     */
    public class ListLooper extends ModelLooper {
        /**
         * The parameter name or pattern to search for.  Ignored if paramNames is used.
         */
        String paramName; 
        /**
         * A list of parameter names to search for
         */
        List<String> paramNames; 
    	/**
    	 * Whether to return the parameter values as byte arrays
    	 */
    	boolean encoded;
    	/**
    	 * Value pattern filter
    	 */
    	String valuePattern;
        /**
         * An output list of parameter data
         */
        public Vector<ParameterData> output = null;

    	/* (non-Javadoc)
    	 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
    	 */
    	@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
	        ParamListLooper looper = new ParamListLooper();
	        looper.model = m;
	        looper.owner = m;
	        if (paramNames!=null)
	        	looper.setNameList(paramNames);
	        else if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        looper.encoded = encoded;
	        looper.setValuePattern(valuePattern);

	        looper.loop(m);
	        if (looper.output!=null) {
	        	if (output==null) {
	        		output = new Vector<ParameterData>();
	        	}
	        	String ownerName = m.getFileName();
	        	for (ParameterData param : looper.output) {
		        	if (ownerName!=null)
		        		param.setOwnerName(ownerName);
	        		output.add(param);
	        	}
	        }
			return false;
		}
    	
    }

}
