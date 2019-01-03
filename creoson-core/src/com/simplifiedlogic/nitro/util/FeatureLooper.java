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
package com.simplifiedlogic.nitro.util;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.FeatureType;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatures;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.impl.JLFeature;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.jlink.impl.NitroUtils;
import com.simplifiedlogic.nitro.jlink.intf.IJLFeature;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over features on an object, 
 * filters the results by various filters, and calls an action method
 * for each feature which matches the filters.
 *  
 * @author Adam Andrews
 */
public abstract class FeatureLooper extends LooperBase {
	
    private FeatureType searchType = null;
    private String typePattern = null;
    private String statusPattern = null;
    private boolean includeUnnamed = false;
    private boolean isNamePattern = false;
    private boolean isTypePattern = false;
    private boolean isStatusPattern = false;
    private boolean visibleOnly = true; // default to true, but can be overridden 
    
    /**
     * The status of the current feature in the loop
     */
    protected String currentStatus = null;
    /**
     * The name of the current feature in the loop
     */
    protected String currentName = null;
    /**
     * The feature-type of the current feature in the loop
     */
    protected String currentType = null;

    /**
     * Loop through features on a Solid
     * @param solid The solid which owns the features
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop(CallSolid solid) throws JLIException,jxthrowable {
        // special case -- exact feature name given
        if (namePattern!=null && !isNamePattern) {
            currentStatus = null;
            currentType = null;
            currentName = null;
            CallFeature feat = null;
            if (namePattern.length() <= JLFeature.FEATURE_LIMIT) {
            	feat = solid.getFeatureByName(namePattern);
            }
            if (feat!=null) {
                if (!checkStatus(feat))
                    return;
                
                if (!checkType(feat))
                    return;

                loopAction(feat);
                return;
            }
        }

        // list the solid's features by feature type
        CallFeatures featList = solid.listFeaturesByType(Boolean.valueOf(visibleOnly), searchType);
        if (featList==null)
        	return;
        processLoop(featList);
    }
    
    /**
     * Loop through the child features of a feature
     * @param feat The feature which owns the child features
     * @throws JLIException
     * @throws jxthrowable
     */
    public void loop(CallFeature feat) throws JLIException,jxthrowable {
    	CallFeatures featList = feat.listChildren();
        processLoop(featList);
    }
    
    /**
     * Loop through the feature list
     * @param featList The list of features
     * @throws JLIException
     * @throws jxthrowable
     */
    private void processLoop(CallFeatures featList) throws JLIException, jxthrowable {
        if (featList!=null) {
            int len = featList.getarraysize();
            CallFeature feat = null;
            for (int i=0; i<len; i++) {
                currentStatus = null;
                currentType = null;
                currentName = null;
                feat = featList.get(i);
                if (!checkStatus(feat))
                    continue;
                
                if (!checkType(feat))
                    continue;

                currentName = JlinkUtils.getFeatureName(feat);

                if (!includeUnnamed && currentName!=null && currentName.equals(IJLFeature.NONAME_FEATURE))
                    continue;
                if (!checkName(currentName, includeUnnamed) || !checkNameAgainstList(currentName, includeUnnamed))
                    continue;

                loopAction(feat);
            }
        }
    }
    
    /**
     * Abstract function which is called for each feature which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param feat The feature which matched the filters
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract void loopAction(CallFeature feat) throws JLIException, jxthrowable;

    /**
     * See whether the feature matches the status filter
     * @param feat The feature to check
     * @return Whether the feature matches the filter
     * @throws jxthrowable
     */
    private boolean checkStatus(CallFeature feat) throws jxthrowable {
        if (statusPattern!=null) {
            currentStatus = JlinkUtils.translateFeatureStatus(feat.getStatus());
            if (isStatusPattern && !currentStatus.toLowerCase().matches(statusPattern))
                return false;
            if (!isStatusPattern && !currentStatus.equalsIgnoreCase(statusPattern))
                return false;
        }
        return true;
    }

    /**
     * See whether the feature matches the feature-type filter
     * @param feat The feature to check
     * @return Whether the feature matches the filter
     * @throws jxthrowable
     */
    private boolean checkType(CallFeature feat) throws jxthrowable {
        if (typePattern!=null) {
            currentType = feat.getFeatTypeName();
            if (isTypePattern && !currentType.toLowerCase().matches(typePattern))
                return false;
            if (!isTypePattern && !currentType.equalsIgnoreCase(typePattern))
                return false;
        }
        return true;
    }
    
	/**
	 * @return The feature type filter
	 */
	public FeatureType getSearchType() {
		return searchType;
	}
	/**
	 * @param searchType The feature type filter
	 */
	public void setSearchType(FeatureType searchType) {
		this.searchType = searchType;
	}
	/**
	 * @return The feature status filter
	 */
	public String getStatusPattern() {
		return statusPattern;
	}
	/**
	 * @param statusPattern The feature status filter
	 */
	public void setStatusPattern(String statusPattern) {
        isStatusPattern = NitroUtils.isPattern(statusPattern);
        if (isStatusPattern)
            this.statusPattern = NitroUtils.transformPattern(statusPattern.toLowerCase());
        else
            this.statusPattern = statusPattern;
	}
	/**
	 * @return The feature type pattern filter (supports wildcards)
	 */
	public String getTypePattern() {
		return typePattern;
	}
	/**
	 * @param typePattern The feature type pattern filter (supports wildcards)
	 */
	public void setTypePattern(String typePattern) {
        isTypePattern = NitroUtils.isPattern(typePattern);
        if (isTypePattern)
            this.typePattern = NitroUtils.transformPattern(typePattern.toLowerCase());
        else
            this.typePattern = typePattern;
	}

    protected void processObjectByName(String name) throws JLIException,jxthrowable {
    	
    }

	/**
	 * @return Whether to include unnamed features
	 */
	public boolean isIncludeUnnamed() {
		return includeUnnamed;
	}

	/**
	 * @param includeUnnamed Whether to include unnamed features
	 */
	public void setIncludeUnnamed(boolean includeUnnamed) {
		this.includeUnnamed = includeUnnamed;
	}

	/**
	 * @return visibleOnly Whether to skip over invisible features
	 */
	public boolean isVisibleOnly() {
		return visibleOnly;
	}

	/**
	 * @param visibleOnly Whether to skip over invisible features
	 */
	public void setVisibleOnly(boolean visibleOnly) {
		this.visibleOnly = visibleOnly;
	}

}
