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
package com.simplifiedlogic.nitro.jlink.intf;

import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.FeatSelectData;
import com.simplifiedlogic.nitro.jlink.data.FeatureData;
import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLFeature {

	public static final boolean GETPATHS_YES = true;
	public static final boolean GETPATHS_NO = false;
	public static final boolean NODATUMFEAT_YES = true;
	public static final boolean NODATUMFEAT_NO = false;
	public static final boolean INCLUDEUNNAMED_YES = true;
	public static final boolean INCLUDEUNNAMED_NO = false;
	public static final boolean NOCOMPFEAT_YES = true;
	public static final boolean NOCOMPFEAT_NO = false;
	public static final boolean ENCODED_YES = true;
	public static final boolean ENCODED_NO = false;

	public static final String STATUS_ACTIVE = "ACTIVE";
	public static final String STATUS_INACTIVE = "INACTIVE";
	public static final String STATUS_FAMILY_TABLE_SUPPRESSED = "FAMILY_TABLE_SUPPRESSED";
	public static final String STATUS_SIMP_REP_SUPPRESSED = "SIMP_REP_SUPPRESSED";
	public static final String STATUS_PROGRAM_SUPPRESSED = "PROGRAM_SUPPRESSED";
	public static final String STATUS_SUPPRESSED = "SUPPRESSED";
	public static final String STATUS_UNREGENERATED = "UNREGENERATED";
	
	public static final String NONAME_FEATURE = "no_name";

	public void delete(
			String filename, 
			String featureName, 
			List<String> featureNames, 
			String featureStatus, 
			String featureType, 
			boolean clip, 
			String sessionId) throws JLIException;
	public void delete(
			String filename, 
			String featureName, 
			List<String> featureNames, 
			String featureStatus, 
			String featureType, 
			boolean clip, 
			AbstractJLISession sess) throws JLIException;
	
	public void resume(
			String filename, 
			String featureName, 
			List<String> featureNames,
			int featureId, 
			String featureStatus, 
			String featureType, 
			boolean withChildren, 
			String sessionId) throws JLIException;
	public void resume(
			String filename, 
			String featureName, 
			List<String> featureNames,
			int featureId, 
			String featureStatus, 
			String featureType, 
			boolean withChildren, 
			AbstractJLISession sess) throws JLIException;

	public void suppress(
			String filename, 
			String featureName, 
			List<String> featureNames,
			int featureId, 
			String featureStatus, 
			String featureType, 
			boolean clip,
			boolean withChildren,
			String sessionId) throws JLIException;
	public void suppress(
			String filename, 
			String featureName, 
			List<String> featureNames, 
			int featureId, 
			String featureStatus, 
			String featureType, 
			boolean clip,
			boolean withChildren,
			AbstractJLISession sess) throws JLIException;

	public List<FeatureData> list(
	        String filename,
	        String featureName, 
	        String featureStatus,
	        String featureType,
	        boolean paths,
	        boolean noDatumFeatures, 
	        boolean includeUnnamed, 
	        boolean noComponentFeatures, 
			String sessionId) throws JLIException;
	public List<FeatureData> list(
	        String filename,
	        String featureName, 
	        String featureStatus,
	        String featureType,
	        boolean paths,
	        boolean noDatumFeatures, 
	        boolean includeUnnamed, 
	        boolean noComponentFeatures, 
	        AbstractJLISession sess) throws JLIException;

	public List<FeatureData> listPatternFeatures(
			String filename, 
			String patternName, 
			String featureType, 
			String sessionId) throws JLIException;
	public List<FeatureData> listPatternFeatures(
			String filename, 
			String patternName, 
			String featureType, 
	        AbstractJLISession sess) throws JLIException;

	public List<FeatureData> listGroupFeatures(
			String filename, 
			String groupName, 
			String featureType, 
			String sessionId) throws JLIException;
	public List<FeatureData> listGroupFeatures(
			String filename, 
			String groupName, 
			String featureType, 
	        AbstractJLISession sess) throws JLIException;

	public void rename(
	        String filename,
	        String featureName, 
	        int featureId,
	        String newName,
	        String sessionId) throws JLIException;
	public void rename(
	        String filename,
	        String featureName, 
	        int featureId,
	        String newName,
	        AbstractJLISession sess) throws JLIException;


	public List<FeatSelectData> userSelectCsys(String modelname, int max, 
			String sessionId) throws JLIException;
	public List<FeatSelectData> userSelectCsys(String modelname, int max, 
			AbstractJLISession sess) throws JLIException;

	public void deleteParam(
			String filename, 
			String featName,
			String paramName,
			String sessionId) throws JLIException;
	public void deleteParam(
			String filename, 
			String featName,
			String paramName,
			AbstractJLISession sess) throws JLIException;

	public boolean paramExists(
			String filename, 
			String featName,
			String paramName,
			List<String> paramNames, 
			String sessionId) throws JLIException;
	public boolean paramExists(
			String filename, 
			String featName,
			String paramName,
			List<String> paramNames, 
			AbstractJLISession sess) throws JLIException;

    public List<ParameterData> listParams(String filename, 
    		String featName, int featId, String paramName,
			List<String> paramNames, String typePattern, String valuePattern, 
			boolean encoded, 
	        boolean noDatumFeatures, 
	        boolean includeUnnamed, 
	        boolean noComponentFeatures, 
			String sessionId)
			throws JLIException;
	public List<ParameterData> listParams(String filename, 
			String featName, int featId, String paramName,
			List<String> paramNames, String typePattern, String valuePattern, 
			boolean encoded, 
	        boolean noDatumFeatures, 
	        boolean includeUnnamed, 
	        boolean noComponentFeatures, 
			AbstractJLISession sess)
			throws JLIException;

	public void setParam(String filename, String featName, String paramName,
			Object value, String type, int designate,
			boolean encoded, boolean noCreate, String sessionId) throws JLIException;
	public void setParam(String filename, String featName, String paramName,
			Object value, String type, int designate,
			boolean encoded, boolean noCreate, AbstractJLISession sess) throws JLIException;
	
    public void addUDF(
    		String filename,
    		String udfFile,
    		String csysName,
    		String csysPrompt,
    		Map<String, Number> dimensions,
    		Map<String, Object> parameters,
    		String sessionId) throws JLIException;
    public void addUDF(
    		String filename,
    		String udfFile,
    		String csysName,
    		String csysPrompt,
    		Map<String, Number> dimensions,
    		Map<String, Object> parameters,
    		AbstractJLISession sess) throws JLIException;
}
