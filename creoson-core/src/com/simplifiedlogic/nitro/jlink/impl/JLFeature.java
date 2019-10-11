/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
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
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitBadInputs;
import com.ptc.pfc.pfcExceptions.XToolkitNotExist;
import com.ptc.pfc.pfcExceptions.XToolkitUserAbort;
import com.ptc.pfc.pfcFeature.FeatureStatus;
import com.ptc.pfc.pfcFeature.FeatureType;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallDeleteOperation;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatureGroup;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatureOperations;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeaturePattern;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatures;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallGroupPattern;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallResumeOperation;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallSuppressOperation;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallCoordSystem;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelectionOptions;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelections;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.FeatSelectData;
import com.simplifiedlogic.nitro.jlink.data.FeatureData;
import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLFeature;
import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.FeatureLooper;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.ModelLooper;
import com.simplifiedlogic.nitro.util.ParamListLooper;
import com.simplifiedlogic.nitro.util.ParamLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLFeature implements IJLFeature {

    /**
     * Maximum number of characters in a feature name
     */
    public static final int FEATURE_LIMIT = 31;

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#delete(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, java.lang.String)
     */
    @Override
	public void delete(String filename, String featureName, List<String> featureNames, 
			String featureStatus, String featureType, boolean clip,
			String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        delete(filename, featureName, featureNames, featureStatus, featureType, clip, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#delete(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void delete(String filename, String featureName, List<String> featureNames, 
			String featureStatus, String featureType, boolean clip,
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.delete: " + featureName, NitroConstants.DEBUG_KEY);
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
	
	        // required for WF5 and higher
	        boolean resolveMode = JlinkUtils.prefixResolveModeFix(session);

	        try {
		        DeleteLooper looper = new DeleteLooper();
	        	looper.setNamePattern(filename);
		        looper.setDefaultToActive(true);
		        looper.setSession(session);
		    	if (featureNames!=null)
		    		looper.featureNameList = featureNames;
		    	else if (featureName==null)
		    		looper.featureName = null;
		    	else
		    		looper.featureName = featureName;
		    	looper.featureStatus = featureStatus;
		    	looper.featureType = featureType;
		        looper.clip = clip;
		        
		        looper.loop();
	        }
	        finally {
	        	JlinkUtils.postfixResolveModeFix(session, resolveMode);
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.delete,"+featureName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#list(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean, boolean, boolean, java.lang.String)
	 */
	@Override
	public List<FeatureData> list(String filename, String featureName,
			String featureStatus, String featureType, boolean paths,
			boolean noDatumFeatures, boolean includeUnnamed, 
			boolean noComponentFeatures, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return list(filename, featureName, featureStatus, featureType, paths, noDatumFeatures, includeUnnamed, noComponentFeatures, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#list(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, boolean, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<FeatureData> list(String filename, String featureName,
			String featureStatus, String featureType, boolean paths,
			boolean noDatumFeatures, boolean includeUnnamed, 
			boolean noComponentFeatures, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.list: " + featureName, NitroConstants.DEBUG_KEY);
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
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);

	        ListLooper looper = new ListLooper();
	        looper.paths = paths;
	        looper.noDatumFeatures = noDatumFeatures;
	        looper.noComponentFeatures = noComponentFeatures;
	        looper.setNamePattern(featureName);
	        looper.setStatusPattern(featureStatus);
	        looper.setTypePattern(featureType);
	        looper.setIncludeUnnamed(includeUnnamed);
	  
	        looper.loop(solid);
	        
	        if (looper.output==null)
	            return new Vector<FeatureData>();
	        else
	            return looper.output;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.list,"+featureName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#listPatternFeatures(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<FeatureData> listPatternFeatures(String filename, String patternName, String featureType, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return listPatternFeatures(filename, patternName, featureType, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#listPatternFeatures(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<FeatureData> listPatternFeatures(String filename, String patternName, String featureType, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.list_pattern_features: " + patternName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (patternName==null) 
	        	throw new JLIException("Must specify a pattern name");

	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);

        	CallFeature feat = solid.getFeatureByName(patternName);
        	if (feat==null)
        		throw new JLIException("Feature '"+patternName+"' not found");

            List<FeatureData> output = new Vector<FeatureData>();

    		CallGroupPattern groupPattern = feat.getGroupPattern();
    		if (groupPattern!=null) {
    			CallFeatures feats = groupPattern.listFeatMembers(); 
        		if (feats!=null) {
        			int len = feats.getarraysize();
        			if (len>0) {
        				String lastGroup=null;
    	    			for (int i=0; i<len; i++) {
    	    				feat = feats.get(i);
    	    				if (featureType==null) {
    		    				if (feat.getIsGroupMember() && feat.getFeatType()==FeatureType._FEATTYPE_GROUP_HEAD) {
    	    	    				addFeatureData(feat, output);
    		    				}
    	    				}
    	    				else {
    		    				if (feat.getFeatType()!=FeatureType._FEATTYPE_GROUP_HEAD) {
    		    					String currentType = feat.getFeatTypeName();
    		    					if (currentType.equalsIgnoreCase(featureType)) {
    		    						CallFeatureGroup group = feat.getGroup();
    		    						if (group!=null) {
    		    							String groupName = group.getGroupName();
    		    							if (!groupName.equals(lastGroup)) {
    		    								addFeatureData(feat, output);
    		    								lastGroup = groupName;
    		    							}
    		    						}
    		    					}
    		    				}
    	    				}
    	    			}
        			}
        		}
    		}
        	else {
	            CallFeaturePattern pattern = feat.getPattern();
	        	if (pattern!=null) {
	    			CallFeatures feats = pattern.listMembers(); 
	        		if (feats!=null) {
	        			int len = feats.getarraysize();
	        			if (len>0) {
	    	    			for (int i=0; i<len; i++) {
	    	    				feat = feats.get(i);
	    	    				addFeatureData(feat, output);
	    	    			}
	        			}
	        		}
	
	        	}
        		else
        			throw new JLIException("Feature '"+patternName+"' is not a pattern or pattern member");
        	}

	        return output;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.list_pattern_features,"+patternName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#listGroupFeatures(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<FeatureData> listGroupFeatures(String filename, String groupName, String featureType, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return listGroupFeatures(filename, groupName, featureType, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#listGroupFeatures(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<FeatureData> listGroupFeatures(String filename, String groupName, String featureType, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.list_group_features: " + groupName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (groupName==null) 
	        	throw new JLIException("Must specify a group name");

	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);

        	CallFeature feat = solid.getFeatureByName(groupName);
        	if (feat==null)
        		throw new JLIException("Feature '"+groupName+"' not found");
        	if (feat.getFeatType()!=FeatureType._FEATTYPE_GROUP_HEAD)
        		throw new JLIException("Feature '"+groupName+"' is not a group");

            List<FeatureData> output = new Vector<FeatureData>();

			CallFeatureGroup group = feat.getGroup();
        	if (group==null)
        		throw new JLIException("Feature '"+groupName+"' is not a group");

			CallFeatures feats = group.listMembers(); 
    		if (feats!=null) {
    			int len = feats.getarraysize();
    			for (int i=0; i<len; i++) {
    				feat = feats.get(i);
    				if (featureType==null) {
	    				if (feat.getFeatType()!=FeatureType._FEATTYPE_GROUP_HEAD) {
    	    				addFeatureData(feat, output);
	    				}
    				}
    				else {
	    				if (feat.getFeatType()!=FeatureType._FEATTYPE_GROUP_HEAD) {
	    					String currentType = feat.getFeatTypeName();
	    					if (currentType.equalsIgnoreCase(featureType)) {
								addFeatureData(feat, output);
	    					}
	    				}
    				}
    			}
			}

	        return output;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.list_group_features,"+groupName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
	
	private void addFeatureData(CallFeature feat, List<FeatureData> output) throws jxthrowable {
		FeatureData item = new FeatureData();
		item.setName(feat.getName());
		item.setFeatureId(feat.getId());
		item.setFeatureNumber(feat.getNumber());
		item.setFeatureType(feat.getFeatTypeName());
		item.setStatus(JlinkUtils.translateFeatureStatus(feat.getStatus()));
		output.add(item);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#rename(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public void rename(String filename, String featureName, int featureId,
			String newName, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        rename(filename, featureName, featureId, newName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#rename(java.lang.String, java.lang.String, int, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void rename(String filename, String featureName, int featureId,
			String newName, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.rename: " + featureName + " -> " + newName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (NitroUtils.isPattern(featureName))
	            throw new JLIException("Name patterns are not supported for this command");
	        if (featureName==null && featureId<=0) 
	        	throw new JLIException("Must specify a feature name or ID");
	        if (newName==null || newName.length()==0)
	        	throw new JLIException("Must specify a new name");
	        
	        newName = validateFeatureName(newName);	        

	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
		        return;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);

	        CallFeature feat = null;
	        if (featureId>0) {
	        	try {
	        		feat = solid.getFeatureById(featureId);
	        	}
                catch (XToolkitBadInputs e) {}  // ignore this exception because it's thrown when the ID is not found
                catch (XToolkitNotExist e) {}  // ignore this exception because it's thrown when the ID is not found
		        if (feat==null)
		        	throw new JLIException("No feature found for ID: " + featureId);
	        }
	        else {
	        	feat = solid.getFeatureByName(featureName);
		        if (feat==null)
		        	throw new JLIException("No feature found for name: " + featureName);
	        }
	        if (feat.getFeatType()==FeatureType._FEATTYPE_COMPONENT)
	        	throw new JLIException("Not allowed to rename a Component feature");
	        
	        if (newName.equals(feat.getName()))
	        	return;
	        
	        CallFeature oldFeat = solid.getFeatureByName(newName);
	        if (oldFeat!=null)
	        	throw new JLIException("A feature already exists with that name");

	        // catch bad-input exceptions which occur when the feature is not allowed to
	        // be renamed.  There doesn't seem to be a way to detect that beforehand.
	        try {
	        	feat.setName(newName);
	        }
	        catch (XToolkitBadInputs e) {
	        	throw new JLIException("Not allowed to rename feature: " + feat.getName());
	        }
	    	
	        return;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.rename,"+featureName+","+newName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#resume(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public void resume(String filename, String featureName, List<String> featureNames, 
			int featureId, String featureStatus, String featureType, boolean withChildren,
			String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        resume(filename, featureName, featureNames, featureId, featureStatus, featureType, withChildren, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#resume(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void resume(String filename, String featureName, List<String> featureNames, 
			int featureId, String featureStatus, String featureType, boolean withChildren,
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.resume: " + featureName, NitroConstants.DEBUG_KEY);
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
	
	        // required for WF5 and higher
	        boolean resolveMode = JlinkUtils.prefixResolveModeFix(session);

	        try {
		        ResumeLooper looper = new ResumeLooper();
	        	looper.setNamePattern(filename);
		        looper.setDefaultToActive(true);
		        looper.setSession(session);
		        if (featureNames!=null)
		    		looper.featureNameList = featureNames;
		    	else if (featureName==null)
		    		looper.featureName = null;
		    	else
		    		looper.featureName = featureName;
		    	looper.featureStatus = featureStatus;
		    	looper.featureType = featureType;
		    	looper.withChildren = withChildren;
		    	looper.featureId = featureId;

		        looper.loop();
		        
	        }
	        finally {
	        	JlinkUtils.postfixResolveModeFix(session, resolveMode);
	        }
	        
	    	
	        return;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.resume,"+featureName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * Walk recursively through a hierarchy of sub-features and accumulate a list of resume operations 
     * @param features The list of feature objects to check
     * @param featOps The output list of accumulated resume operations
     * @throws JLIException
     * @throws jxthrowable
     */
    private void resumeChildren(Vector<CallFeature> features, CallFeatureOperations featOps) throws JLIException,jxthrowable {
        if (features!=null) {
            int len = features.size();
            CallFeature feat;
            ResumeLooper2 subLooper;
            // loop through each input feature
            for (int i=0; i<len; i++) {
                feat = (CallFeature)features.elementAt(i);
                // use a looper to resume sub-features
                subLooper = new ResumeLooper2();
                subLooper.setStatusPattern("suppressed");
                subLooper.featOps = featOps;
                
                subLooper.loop(feat);
                
                // restore this code if going with separate featOps
//                if (subLooper.featOps!=null && subLooper.featOps.getarraysize()>0) {
//                    parentSolid.ExecuteFeatureOps(subLooper.featOps, null);
//                }

                // if any new resume operations were added in the looper, walk the children of this feature
                if (subLooper.featuresResumed!=null) {
                    resumeChildren(subLooper.featuresResumed, subLooper.featOps);
                }
            }
        }
    }
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#suppress(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, boolean, java.lang.String)
	 */
	@Override
	public void suppress(String filename, String featureName, List<String> featureNames, 
			int featureId, String featureStatus, String featureType, boolean clip,
			boolean withChildren, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        suppress(filename, featureName, featureNames, featureId, featureStatus, featureType, clip, withChildren, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#suppress(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void suppress(String filename, String featureName, List<String> featureNames, 
			int featureId, String featureStatus, String featureType, boolean clip,
			boolean withChildren, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.suppress: " + featureName, NitroConstants.DEBUG_KEY);
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
	
	        // required for WF5 and higher
	        boolean resolveMode = JlinkUtils.prefixResolveModeFix(session);

	        try {
		        SuppressLooper looper = new SuppressLooper();
	        	looper.setNamePattern(filename);
		        looper.setDefaultToActive(true);
		        looper.setSession(session);
		        if (featureNames!=null)
		    		looper.featureNameList = featureNames;
		    	else if (featureName==null)
		    		looper.featureName = null;
		    	else
		    		looper.featureName = featureName;
		    	looper.featureStatus = featureStatus;
		    	looper.featureType = featureType;
		    	looper.withChildren = withChildren;
		    	looper.clip = clip;
		    	looper.featureId = featureId;

		        looper.loop();
		        
	        }
	        finally {
	        	JlinkUtils.postfixResolveModeFix(session, resolveMode);
	        }
	    	
	        return;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.suppress,"+featureName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * Walk recursively through a hierarchy of sub-features and accumulate a list of suppress operations 
     * @param features The list of feature objects to check
     * @param featOps The output list of accumulated suppress operations
     * @param clip Whether to clip-suppress suppressed features to the end of the structure
     * @throws JLIException
     * @throws jxthrowable
     */
    private void suppressChildren(Vector<CallFeature> features, CallFeatureOperations featOps, boolean clip) throws JLIException,jxthrowable {
        if (features!=null) {
            int len = features.size();
            CallFeature feat;
            SuppressLooper2 subLooper;
            // loop through each input feature
            for (int i=0; i<len; i++) {
                feat = (CallFeature)features.elementAt(i);
                // use a looper to resume sub-features
                subLooper = new SuppressLooper2();
                subLooper.featOps = featOps;
                subLooper.clip = clip;
                
                subLooper.loop(feat);
                
                // restore this code if going with separate featOps
//                if (subLooper.featOps!=null && subLooper.featOps.getarraysize()>0) {
//                    parentSolid.ExecuteFeatureOps(subLooper.featOps, null);
//                }

                // if any new suppress operations were added in the looper, walk the children of this feature
                if (subLooper.featuresSuppressed!=null) {
                    suppressChildren(subLooper.featuresSuppressed, subLooper.featOps, clip);
                }
            }
        }
    }
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#userSelectCsys(java.lang.String, int, java.lang.String)
	 */
	@Override
	public List<FeatSelectData> userSelectCsys(String modelname, int max, String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

        return userSelectCsys(modelname, max, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#userSelectCsys(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<FeatSelectData> userSelectCsys(String modelname, int max, AbstractJLISession sess) throws JLIException {
		DebugLogging.sendDebugMessage("feature.user_select_csys: " + modelname, NitroConstants.DEBUG_KEY);
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

	        List<FeatSelectData> output = new Vector<FeatSelectData>();

	        boolean complete = false;
	        while (!complete) {
	        	CallSelectionOptions selopts = CallSelectionOptions.create("csys");
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
	            	CallSelection sel;
	            	for (int i=0; i<len; i++) {
	                    sel = sels.get(i);
	                    CallModelItem modelItem = sel.getSelItem();
	                    if (modelItem instanceof CallCoordSystem) {
	                    	CallCoordSystem feat = (CallCoordSystem)modelItem;
	                    	FeatSelectData f = new FeatSelectData();
	                        f.setName(feat.getName());
	                        f.setFeatureType("csys");
                            int id = feat.getId();
	                        f.setFeatureId(new Integer(id));
	                        
	                        output.add(f);
	                    }
//	                    else if (modelItem instanceof Feature) {
//	                    	ComponentPath path = sel.GetPath();
//	    	                ComponentDimensionShowInstructions inst = null;
//	    	                if (path!=null) {
//	    	                    inst = pfcAssembly.ComponentDimensionShowInstructions_Create(path);
//	    	                }                
//	                    	
//	                    	ModelItems items = ((Feature)modelItem).ListSubItems(ModelItemType.ITEM_DIMENSION);
//	                    	if (items!=null) {
//		                    	int dlen = items.getarraysize();
//		                    	if (dlen>0) {
//		    	            		// repaint to clear last dims
//		    	                    Window win = session.GetCurrentWindow();
//		    	                    if (win!=null)
//		    	                    	win.Repaint();
//		    	                    
//			                    	Dimension dim;
//			                    	for (int k=0; k<dlen; k++) {
//			                    	    dim = (Dimension)items.get(k);
//			                    	    dim.Show(inst);
//			                    	}
//		                    	}
//	                    	}
//	                    	
//	            	        complete=false;
//	                    }
	                    else {
	            	        complete=false;
	                    }
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
        		DebugLogging.sendTimerMessage("feature.user_select_csys,"+modelname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#deleteParam(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void deleteParam(
			String filename, 
			String featName,
			String paramName,
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        deleteParam(filename, featName, paramName, sess);
	}
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#deleteParam(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	public void deleteParam(
			String filename, 
			String featName,
			String paramName,
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("feature.delete_param: " + paramName, NitroConstants.DEBUG_KEY);
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
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);

	        DeleteParamLooper2 looper = new DeleteParamLooper2();
	        looper.setNamePattern(featName);
	        if (featName==null)
	        	looper.setIncludeUnnamed(true);
	    	looper.paramName = paramName;

	        looper.loop(solid);

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.delete_param,"+paramName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#paramExists(java.lang.String, java.lang.String, java.lang.String, java.util.List, java.lang.String)
	 */
	@Override
	public boolean paramExists(String filename, String featName, String paramName, List<String> paramNames, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return paramExists(filename, featName, paramName, paramNames, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#paramExists(java.lang.String, java.lang.String, java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public boolean paramExists(String filename, String featName, String paramName, List<String> paramNames, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("feature.param_exists: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (featName==null || featName.length()==0)
	        	throw new JLIException("Must specify a feature name");

	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);
	        CallFeature feat = solid.getFeatureByName(featName);
	        if (feat==null)
	        	throw new JLIException("Feature " + featName + " does not exist on model");
	
	        ExistsParamLooper looper = new ExistsParamLooper();
	        if (paramNames!=null)
	        	looper.setNameList(paramNames);
	        else if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        
	        looper.loop(feat);
	        return looper.exists;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.param_exists,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#listParams(java.lang.String, java.lang.String, int, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, boolean, boolean, boolean, java.lang.String)
     */
    public List<ParameterData> listParams(
	        String filename,
	        String featName, 
	        int featId, 
	        String paramName, 
	        List<String> paramNames, 
	        String typePattern, 
	        String valuePattern, 
	        boolean encoded,
	        boolean noDatumFeatures, 
	        boolean includeUnnamed, 
	        boolean noComponentFeatures, 
	        String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return listParams(filename, featName, featId, paramName, paramNames, typePattern, valuePattern, encoded, noDatumFeatures, includeUnnamed, noComponentFeatures, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#listParams(java.lang.String, java.lang.String, int, java.lang.String, java.util.List, java.lang.String, java.lang.String, boolean, boolean, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public List<ParameterData> listParams(
	        String filename,
	        String featName, 
	        int featId, 
	        String paramName, 
	        List<String> paramNames, 
	        String typePattern, 
	        String valuePattern, 
	        boolean encoded,
	        boolean noDatumFeatures, 
	        boolean includeUnnamed, 
	        boolean noComponentFeatures, 
	        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("feature.list_params: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);
	        
	        if (featId>0) {
	        	CallFeature feat = null;
	        	try {
	        		feat = solid.getFeatureById(featId);
	        	}
                catch (XToolkitBadInputs e) {}  // ignore this exception because it's thrown when the ID is not found
                catch (XToolkitNotExist e) {}  // ignore this exception because it's thrown when the ID is not found
		        if (feat==null)
		        	throw new JLIException("No feature found for ID: " + featId);
		        
	        	int type = feat.getFeatType();
	            if (noDatumFeatures) {
	            	if (type==FeatureType._FEATTYPE_COORD_SYS || 
	            		type==FeatureType._FEATTYPE_CURVE || 
	            		type==FeatureType._FEATTYPE_DATUM_AXIS || 
	            		type==FeatureType._FEATTYPE_DATUM_PLANE || 
	            		type==FeatureType._FEATTYPE_DATUM_POINT || 
	            		type==FeatureType._FEATTYPE_DATUM_QUILT || 
	            		type==FeatureType._FEATTYPE_DATUM_SURFACE)
	            		return new Vector<ParameterData>();
	            }
	            if (noComponentFeatures) {
	            	if (type==FeatureType._FEATTYPE_COMPONENT)
	            		return new Vector<ParameterData>();
	            }

				// call a sub-looper to find feature parameters
		        ParamListLooper looper = new ParamListLooper();
		        looper.model = solid;
		        looper.owner = feat;
		        if (paramNames!=null)
		        	looper.setNameList(paramNames);
		        else if (paramName==null)
		        	looper.setNamePattern(null);
		        else
		        	looper.setNamePattern(paramName);
		        looper.encoded = encoded;
		        looper.setValuePattern(valuePattern);

		        looper.loop(feat);
		        
		        Vector<ParameterData> output = null;
		        if (looper.output!=null) {
		        	if (output==null) {
		        		output = new Vector<ParameterData>();
		        	}
		        	// get these values once to save on jni calls
		        	String ownerName = feat.getName();
		        	String ownerType = feat.getFeatTypeName();
		        	int featid = feat.getId();
		        	for (ParameterData param : looper.output) {
			        	if (ownerName!=null)
			        		param.setOwnerName(ownerName);
			        	if (ownerType!=null)
			        		param.setOwnerType(ownerType);
			        	param.setOwnerId(featid);
		        		output.add(param);
		        	}
		        }
		        
		        if (output==null)
		            return new Vector<ParameterData>();
		        else
		        	return output;
	        }
	        else {
		        ListParamLooper looper = new ListParamLooper();
		        looper.model = solid;
		        looper.setNamePattern(featName);
		        looper.paramName = paramName;
		        looper.paramNames = paramNames;
		        looper.encoded = encoded;
		        looper.setTypePattern(typePattern);
		        looper.valuePattern = valuePattern;
		        looper.noDatumFeatures = noDatumFeatures;
		        looper.noComponentFeatures = noComponentFeatures;
		        looper.setIncludeUnnamed(includeUnnamed);
	
		        looper.loop(solid);
	
		        if (looper.output==null)
		            return new Vector<ParameterData>();
		        else
		        	return looper.output;
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.list_params,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }	

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#setParam(java.lang.String, java.lang.String, java.lang.String, java.lang.Object, java.lang.String, int, boolean, boolean, java.lang.String)
     */
    @Override
	public void setParam(
			String filename,
			String featName, 
			String paramName,
			Object value,
			String type,
			int designate,
			boolean encoded,
			boolean noCreate,
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        setParam(filename, featName, paramName, value, type, designate, encoded, noCreate, sess);
	}
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFeature#setParam(java.lang.String, java.lang.String, java.lang.String, java.lang.Object, java.lang.String, int, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
	public void setParam(
			String filename,
			String featName, 
			String paramName,
			Object value,
			String type,
			int designate,
			boolean encoded,
			boolean noCreate,
			AbstractJLISession sess) throws JLIException {
		
		DebugLogging.sendDebugMessage("feature.set_param: " + paramName + "=" + value, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (paramName==null || paramName.trim().length()==0)
    		throw new JLIException("No parameter name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        paramName = JLParameter.validateParameter(paramName, value, encoded);
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);
	        
	        SetLooper looper = new SetLooper();
	        looper.model = solid;
            looper.setNamePattern(featName);
	        looper.setDebugKey(NitroConstants.DEBUG_SET_PARAM_KEY);
	        looper.paramName = paramName;
	        looper.value = value;
	        looper.type = type;
	        looper.designate = designate;
	        looper.encoded = encoded;
	        looper.noCreate = noCreate;
	        if (featName==null)
	        	looper.setIncludeUnnamed(true);
	        looper.loop(solid);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("feature.set_param," + paramName + "," + value, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}		

    /**
     * Check whether a feature name is valid.  Checks for length and invalid characters.  Returns a reformatted feature name.
     * @param feat The feature name
     * @return The reformatted name
     * @throws JLIException
     */
    protected static String validateFeatureName(String feat) throws JLIException {
        
        if (feat==null) return null;
        
        String upper = feat.toUpperCase();
        
        int len = upper.length();
        if (len > FEATURE_LIMIT)
            throw new JLIException("feature name exceeds maximum length allowed");

        NitroUtils.validateNameChars(feat);

        return upper;
    }
    
    /**
     * An implementation of FeatureLooper which collects a list of feature data
     * @author Adam Andrews
     *
     */
    private static class ListLooper extends FeatureLooper {
        /**
         * Whether to return component paths for each feature
         */
        public boolean paths = false;
        /**
         * Whether to ignore datum-type features; these are COORD_SYS, CURVE, DATUM_AXIS, DATUM_PLANE, DATUM_POINT, DATUM_QUILT, and DATUM_SURFACE features.
         */
        public boolean noDatumFeatures = false;
        /**
         * Wether to ignore component features
         */
        public boolean noComponentFeatures = false;
        /**
         * The output list of feature data
         */
        public Vector<FeatureData> output = null;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.FeatureLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature)
         */
        public void loopAction(CallFeature feat) throws JLIException, jxthrowable {
        	int type = feat.getFeatType();
            if (noDatumFeatures) {
            	if (type==FeatureType._FEATTYPE_COORD_SYS || 
            		type==FeatureType._FEATTYPE_CURVE || 
            		type==FeatureType._FEATTYPE_DATUM_AXIS || 
            		type==FeatureType._FEATTYPE_DATUM_PLANE || 
            		type==FeatureType._FEATTYPE_DATUM_POINT || 
            		type==FeatureType._FEATTYPE_DATUM_QUILT || 
            		type==FeatureType._FEATTYPE_DATUM_SURFACE)
            		return;
            }
            if (noComponentFeatures) {
            	if (type==FeatureType._FEATTYPE_COMPONENT)
            		return;
            }

            if (currentStatus==null)
            	currentStatus = JlinkUtils.translateFeatureStatus(feat.getStatus());

            if (currentName==null)
            	currentName = JlinkUtils.getFeatureName(feat);
            
            if (currentType==null)
            	currentType = feat.getFeatTypeName();
            
            FeatureData outvals = new FeatureData();
            outvals.setName(currentName==null?"":currentName);
            outvals.setStatus(currentStatus==null?"":currentStatus);
            outvals.setFeatureType(currentType==null?"":currentType);
            outvals.setFeatureId(feat.getId());
            if (paths) {
                int id = feat.getId();
                outvals.setFeatureId(id);
                Integer no = feat.getNumber();
                if (no!=null)
                	outvals.setFeatureNumber(no.intValue());
            }
            if (output==null)
                output = new Vector<FeatureData>();
            output.add(outvals);
        }
    }

    /**
     * An implementation of ModelLooper which will delete features from a list of models.  This calls an instance of DeleteLooper2 to loop through features on the models.
     * 
     * @author Adam Andrews
     *
     */
    private class DeleteLooper extends ModelLooper {
    	/**
    	 * Name or pattern of the feature to delete.  Only used if featureNameList is null
    	 */
    	String featureName;
    	/**
    	 * List of feature names to delete
    	 */
    	List<String> featureNameList;
    	/**
    	 * Feature status filter
    	 */
    	String featureStatus;
    	/**
    	 * Feature type filter
    	 */
    	String featureType;
        /**
         * Whether to clip-delete ANY features from this feature through the end of the structure
         */
        boolean clip;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			if (!(m instanceof CallSolid))
				return false;
			CallSolid solid = (CallSolid)m;
			// call a sub-looper to accumulate delete operations
	        DeleteLooper2 looper = new DeleteLooper2();
	        looper.setNamePattern(featureName);
	    	if (featureNameList!=null)
	    		looper.setNameList(featureNameList);
	    	else if (featureName==null)
	    		looper.setNamePattern(null);
	    	else
	    		looper.setNamePattern(featureName);
	        looper.setStatusPattern(featureStatus);
	        looper.setTypePattern(featureType);
	        if (featureName==null && featureNameList==null)
	        	looper.setIncludeUnnamed(true);
	        looper.clip = clip;

	        looper.loop(solid);
	        
	        // perform the delete operations
	        if (looper.featOps!=null && looper.featOps.getarraysize()>0) {
	            solid.executeFeatureOps(looper.featOps, null);
	            
	            CallWindow win = getSession().getModelWindow(solid);
	            if (win!=null)
	                win.refresh();
	        }
	        else {
//	        	throw new JLIException("No features found to delete");
	        }
			return false;
		}
    }

    /**
     * An implementation of FeatureLooper which deletes features from a model.  Called by DeleteLooper.
     * @author Adam Andrews
     *
     */
    private static class DeleteLooper2 extends FeatureLooper {
        /**
         * Whether to clip-delete ANY features from this feature through the end of the structure
         */
        public boolean clip = false;
        /**
         * A list of delete operations accumulated for processing
         */
        public CallFeatureOperations featOps = null;

        public void loopAction(CallFeature feat) throws JLIException, jxthrowable {
        	if (featOps==null)
			    featOps = CallFeatureOperations.create();

        	CallDeleteOperation delop = feat.createDeleteOp();
            delop.setClip(clip);
            featOps.append(delop);
        }
    }

    /**
     * An implementation of ModelLooper to resume features on a list of models.  This calls an instance of ResumeLooper2 to loop through features on the models.
     * @author Adam Andrews
     *
     */
    private class ResumeLooper extends ModelLooper {
    	/**
    	 * Name or pattern of the feature to resume.  Ignored if featureId>0.
    	 */
    	String featureName;
    	/**
    	 * ID of the feature to resume
    	 */
    	int featureId;
    	/**
    	 * List of feature names to resume
    	 */
    	List<String> featureNameList;
    	/**
    	 * Feature status filter
    	 */
    	String featureStatus;
    	/**
    	 * Feature type filter
    	 */
    	String featureType;
    	/**
    	 * Whether to also resume children of the features
    	 */
    	boolean withChildren;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			if (!(m instanceof CallSolid))
				return false;
			CallSolid solid = (CallSolid)m;
	        ResumeLooper2 looper = new ResumeLooper2();
			if (featureId>0) {
				CallFeature feat = solid.getFeatureById(featureId);
				if (feat!=null) {
					looper.loopAction(feat);
				}
			}
			else {
				// call a sub-looper to accumulate resume operations
		        if (featureNameList!=null)
		    		looper.setNameList(featureNameList);
		    	else if (featureName==null)
		    		looper.setNamePattern(null);
		    	else 
		    		looper.setNamePattern(featureName);
		        looper.setStatusPattern(featureStatus);
		        looper.setTypePattern(featureType);
		        if (featureName==null)
		        	looper.setIncludeUnnamed(true);
	
		        looper.loop(solid);
			}
	        
	        // remove this code if going with separate featOps
	        if (withChildren) {
	            if (looper.featuresResumed!=null) {
	                resumeChildren(looper.featuresResumed, looper.featOps);
	            }
	        }

	        // perform the resume operations
	        boolean resumed = false;
	        if (looper.featOps!=null && looper.featOps.getarraysize()>0) {
	            solid.executeFeatureOps(looper.featOps, null);
	            resumed = true;
	        }

	        // restore this code if going with separate featOps
//	        if (with_children.equals(INT_TRUE)) {
//	            if (looper.featuresResumed!=null) {
//	                resumeChildren(looper.featuresResumed, solid);
//	            }
//	        }

	        if (resumed) {
	            CallWindow win = getSession().getModelWindow(solid);
	            if (win!=null)
	                win.refresh();
	        }
			return false;
		}
    }

    /**
     * An implementation of FeatureLooper to resume a list of features on a model
     * @author Adam Andrews
     *
     */
    private static class ResumeLooper2 extends FeatureLooper {
        /**
         * A list of resume operations accumulated for processing
         */
        public CallFeatureOperations featOps = null;
        /**
         * A list of feature objects that were resumed
         */
        public Vector<CallFeature> featuresResumed = null;

        public void loopAction(CallFeature feat) throws JLIException, jxthrowable {

            // Note: only resume user-suppressed features, not ones suppressed by the app
            if (feat!=null && feat.getStatus() == FeatureStatus._FEAT_SUPPRESSED) {
                if (featOps==null)
                    featOps = CallFeatureOperations.create();

                // accumulate the resume operation and feature object
                CallResumeOperation resop = feat.createResumeOp();
                // resop.SetWithParents(with_parents); // legacy code
                resop.setWithParents(true);
                featOps.append(resop);
                
                if (featuresResumed==null)
                    featuresResumed = new Vector<CallFeature>();
                featuresResumed.add(feat);
            }
        }
    }

    /**
     * An implementation of ModelLooper to suppress features on a list of models.  This calls an instance of SuppressLooper2 to loop through features on the models.
     * @author Adam Andrews
     *
     */
    private class SuppressLooper extends ModelLooper {
    	/**
    	 * Name or pattern of the feature to suppress.  Ignored if featureId>0.
    	 */
    	String featureName;
    	/**
    	 * ID of the feature to suppress
    	 */
    	int featureId;
    	/**
    	 * List of feature names to suppress
    	 */
    	List<String> featureNameList;
    	/**
    	 * Feature status filter
    	 */
    	String featureStatus;
    	/**
    	 * Feature type filter
    	 */
    	String featureType;
    	/**
    	 * Whether to also suppress children of the features
    	 */
    	boolean withChildren;
        /**
         * Whether to clip-suppress ANY features from this feature through the end of the structure
         */
    	boolean clip;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			if (!(m instanceof CallSolid))
				return false;
			CallSolid solid = (CallSolid)m;
	        SuppressLooper2 looper = new SuppressLooper2();
			if (featureId>0) {
				CallFeature feat = solid.getFeatureById(featureId);
				if (feat!=null) {
					looper.loopAction(feat);
				}
			}
			else {
				// call a sub-looper to accumulate suppress operations
		        if (featureNameList!=null)
		    		looper.setNameList(featureNameList);
		    	else if (featureName==null)
		    		looper.setNamePattern(null);
		    	else 
		    		looper.setNamePattern(featureName);
		        looper.setStatusPattern(featureStatus);
		        looper.setTypePattern(featureType);
		        if (featureName==null)
		        	looper.setIncludeUnnamed(true);
		        looper.clip = clip;
	
		        looper.loop(solid);
			}

	        // remove this code if going with separate featOps
	        if (withChildren) {
	            if (looper.featuresSuppressed!=null) {
	                suppressChildren(looper.featuresSuppressed, looper.featOps, clip);
	            }
	        }

	        // perform the suppress operations
	        if (looper.featOps!=null && looper.featOps.getarraysize()>0) {
	        	solid.executeFeatureOps(looper.featOps, null);
	            
	            CallWindow win = getSession().getModelWindow(solid);
	            if (win!=null)
	                win.refresh();
	        }

			return false;
		}
    }

    /**
     * An implementation of FeatureLooper to suppress a list of features on a model
     * @author Adam Andrews
     *
     */
    private static class SuppressLooper2 extends FeatureLooper {
        /**
         * Whether to clip-suppress ANY features from this feature through the end of the structure
         */
        public boolean clip = false;
        /**
         * A list of suppress operations accumulated for processing
         */
        public CallFeatureOperations featOps = null;
        /**
         * A list of feature objects that were resumed
         */
        public Vector<CallFeature> featuresSuppressed = null;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.FeatureLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature)
         */
        public void loopAction(CallFeature feat) throws JLIException, jxthrowable {
            if (!JlinkUtils.isSuppressed(feat)) {
                if (featOps==null)
                    featOps = CallFeatureOperations.create();

                // accumulate the suppress operation and feature object
                CallSuppressOperation supop = feat.createSuppressOp();
                supop.setClip(clip);
                if (clip) {
	                supop.setAllowGroupMembers(true);
	                supop.setAllowChildGroupMembers(true);
                }
                featOps.append(supop);
                
                if (featuresSuppressed==null)
                    featuresSuppressed = new Vector<CallFeature>();
                featuresSuppressed.add(feat);
            }
        }
    }

    /**
     * An implementation of FeatureLooper which deletes parameters from a list of features.  This calls an instance of DeleteParamLooper3 to loop through parameters on the features.
     * @author Adam Andrews
     *
     */
    private static class DeleteParamLooper2 extends FeatureLooper {
    	/**
    	 * Parameter name or pattern to delete
    	 */
    	String paramName;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.FeatureLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature)
         */
        public void loopAction(CallFeature feat) throws JLIException, jxthrowable {
	        DeleteParamLooper3 looper = new DeleteParamLooper3();
	        if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        looper.loop(feat);
        }
    }

    /**
     * An implementation of ParamLooper which deletes parameters from a feature.
     * @author Adam Andrews
     *
     */
    private static class DeleteParamLooper3 extends ParamLooper {
    	/**
    	 * The feature to check
    	 */
    	CallFeature feat;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ParamLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter)
         */
        public boolean loopAction(CallParameter p) throws JLIException, jxthrowable {
            try {
                p.delete();
            }
            catch (Exception e) {
                throw new JLIException("Could not delete parameter " + p.getName() + " from feature " + feat.getName());
            }
            return false;
        }
    }

    /**
     * An implementation of ParamLooper which checks whether parameters exist on a feature
     * @author Adam Andrews
     *
     */
    private class ExistsParamLooper extends ParamLooper {
		/**
		 * Output value indicating whether the parameters exist
		 */
		boolean exists = false;
		
	    /* (non-Javadoc)
	     * @see com.simplifiedlogic.nitro.util.ParamLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallParameter)
	     */
	    public boolean loopAction(CallParameter p) throws JLIException, jxthrowable {
	    	// if this action was called at all, then the parameter(s) was found so set the flag
	    	exists=true;
	    	return true;
	    }
    }
    
    /**
     * An implementation of FeatureLooper which returns a list of parameter data on a list of features.  This calls and instance of ParamListLooper to loop through the parameters on each feature.
     * @author Adam Andrews
     *
     */
    private static class ListParamLooper extends FeatureLooper {
    	/**
    	 * The model which owns the feature
    	 */
    	CallModel model;
        /**
         * The parameter name or pattern to search for.  Only used if paramNames is null.
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
    	 * Parameter value filter pattern
    	 */
    	String valuePattern;
        /**
         * Whether to ignore datum-type features; these are COORD_SYS, CURVE, DATUM_AXIS, DATUM_PLANE, DATUM_POINT, DATUM_QUILT, and DATUM_SURFACE features.
         */
        public boolean noDatumFeatures = false;
        /**
         * Whether to ignore component features
         */
        public boolean noComponentFeatures = false;
        /**
         * The output list of parameter data
         */
        public Vector<ParameterData> output = null;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.FeatureLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature)
         */
        public void loopAction(CallFeature feat) throws JLIException, jxthrowable {
        	int type = feat.getFeatType();
            if (noDatumFeatures) {
            	if (type==FeatureType._FEATTYPE_COORD_SYS || 
            		type==FeatureType._FEATTYPE_CURVE || 
            		type==FeatureType._FEATTYPE_DATUM_AXIS || 
            		type==FeatureType._FEATTYPE_DATUM_PLANE || 
            		type==FeatureType._FEATTYPE_DATUM_POINT || 
            		type==FeatureType._FEATTYPE_DATUM_QUILT || 
            		type==FeatureType._FEATTYPE_DATUM_SURFACE)
            		return;
            }
            if (noComponentFeatures) {
            	if (type==FeatureType._FEATTYPE_COMPONENT)
            		return;
            }

			// call a sub-looper to find feature parameters
	        ParamListLooper looper = new ParamListLooper();
	        looper.model = model;
	        looper.owner = feat;
	        if (paramNames!=null)
	        	looper.setNameList(paramNames);
	        else if (paramName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(paramName);
	        looper.encoded = encoded;
	        looper.setValuePattern(valuePattern);

	        looper.loop(feat);
	        
	        if (looper.output!=null) {
	        	if (output==null) {
	        		output = new Vector<ParameterData>();
	        	}
	        	// get these values once to save on jni calls
	        	String ownerName = feat.getName();
	        	String ownerType = feat.getFeatTypeName();
	        	int featid = feat.getId();
	        	for (ParameterData param : looper.output) {
		        	if (ownerName!=null)
		        		param.setOwnerName(ownerName);
		        	if (ownerType!=null)
		        		param.setOwnerType(ownerType);
		        	param.setOwnerId(featid);
	        		output.add(param);
	        	}
	        }
        }
    }

    /**
     * An implementation of FeatureLooper to set a parameter value on a feature.
     * @author Adam Andrews
     *
     */
    public class SetLooper extends FeatureLooper {
    	/**
    	 * The model which owns the feature
    	 */
    	CallModel model;
		/**
		 * The parameter name to set
		 */
		String paramName;
		/**
		 * The parameter value to set
		 */
		Object value;
		/**
		 * The parameter type, if the parameter does not already exist
		 */
		String type;
		/**
		 * Set the parameter to be designated/not designated, or unchanged
		 * @see IJLParameter.DESIGNATE_ON, IJLParameter.DESIGNATE_OFF, IJLParameter.DESIGNATE_UNKNOWN
		 */
		int designate;
		/**
		 * Whether to NOT create the parameter if it does not already exist
		 */
		boolean noCreate;
		/**
		 * Whether the parameter value is a byte array
		 */
		boolean encoded;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.FeatureLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature)
		 */
		@Override
        public void loopAction(CallFeature feat) throws JLIException, jxthrowable {
			JLParameter.setOneParameter(model, feat, paramName, value, type, designate, noCreate, encoded);
		}
    	
    }

}
