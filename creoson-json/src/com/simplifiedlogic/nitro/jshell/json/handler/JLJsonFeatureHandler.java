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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.simplifiedlogic.nitro.jlink.data.FeatSelectData;
import com.simplifiedlogic.nitro.jlink.data.FeatureData;
import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.jlink.intf.IJLFeature;
import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.jshell.json.request.JLFeatureRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLParameterRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFeatureResponseParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLParameterResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "feature" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonFeatureHandler extends JLJsonCommandHandler implements JLFeatureRequestParams, JLFeatureResponseParams {

	private IJLFeature featHandler = null;

	/**
	 * @param featHandler
	 */
	public JLJsonFeatureHandler(IJLFeature featHandler) {
		this.featHandler = featHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_DELETE)) return actionDelete(sessionId, input);
		else if (function.equals(FUNC_LIST)) return actionList(sessionId, input);
		else if (function.equals(FUNC_RENAME)) return actionRename(sessionId, input);
		else if (function.equals(FUNC_RESUME)) return actionResume(sessionId, input);
		else if (function.equals(FUNC_SUPPRESS)) return actionSuppress(sessionId, input);
		else if (function.equals(FUNC_SET_PARAM)) return actionSetParam(sessionId, input);
		else if (function.equals(FUNC_DELETE_PARAM)) return actionDeleteParam(sessionId, input);
		else if (function.equals(FUNC_PARAM_EXISTS)) return actionParamExists(sessionId, input);
		else if (function.equals(FUNC_LIST_PARAMS)) return actionListParams(sessionId, input);
		else if (function.equals(FUNC_USER_SELECT_CSYS)) return actionUserSelectCsys(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionDelete(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        List<String> featureNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	featureNames = getStringListValue(namesObj);
        }
        String statusPattern = checkStringParameter(input, PARAM_STATUS, false);
        String typePattern = checkStringParameter(input, PARAM_TYPE, false);
        boolean clip = checkFlagParameter(input, PARAM_CLIP, false, false);
        
        featHandler.delete(modelname, featureName, featureNames, statusPattern, typePattern, clip, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionList(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        String statusPattern = checkStringParameter(input, PARAM_STATUS, false);
        String typePattern = checkStringParameter(input, PARAM_TYPE, false);
        boolean paths = checkFlagParameter(input, PARAM_PATHS, false, false);
        boolean noDatumFeatures = checkFlagParameter(input, PARAM_NO_DATUM, false, false);
        boolean includeUnnamed = checkFlagParameter(input, PARAM_INC_UNNAMED, false, false);
        boolean noComponentFeatures = checkFlagParameter(input, PARAM_NO_COMP, false, false);
        
        List<FeatureData> features = featHandler.list(modelname, featureName, statusPattern, typePattern, paths, 
        		noDatumFeatures, includeUnnamed, noComponentFeatures, sessionId);

        if (features!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outFeats = new Vector<Map<String, Object>>();
			out.put(OUTPUT_FEATLIST, outFeats);
			Map<String, Object> outFeat = null;
			for (FeatureData feat : features) {
				outFeat = new Hashtable<String, Object>();
				if (feat.getName()!=null)
					outFeat.put(PARAM_NAME, feat.getName());
				if (feat.getStatus()!=null)
					outFeat.put(PARAM_STATUS, feat.getStatus());
				if (feat.getFeatureType()!=null)
					outFeat.put(PARAM_TYPE, feat.getFeatureType());
				if (feat.getFeatureId()>0)
					outFeat.put(OUTPUT_ID, Integer.valueOf(feat.getFeatureId()));
				if (feat.getFeatureNumber()>0)
					outFeat.put(OUTPUT_FEATNO, Integer.valueOf(feat.getFeatureNumber()));
				
				outFeats.add(outFeat);
			}
			
			return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionRename(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        int featureId = checkIntParameter(input, PARAM_ID, false, 0);
        String newName = checkStringParameter(input, PARAM_NEWNAME, true);

        if (featureName==null && featureId<=0) 
        	throw new JLIException("Must specify a feature name or ID");

        featHandler.rename(modelname, featureName, featureId, newName, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionResume(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        List<String> featureNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	featureNames = getStringListValue(namesObj);
        }
        String statusPattern = checkStringParameter(input, PARAM_STATUS, false);
        String typePattern = checkStringParameter(input, PARAM_TYPE, false);
        boolean withChildren = checkFlagParameter(input, PARAM_WCHILDREN, false, true);

        featHandler.resume(modelname, featureName, featureNames, statusPattern, typePattern, withChildren, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionSuppress(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        List<String> featureNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	featureNames = getStringListValue(namesObj);
        }
        String statusPattern = checkStringParameter(input, PARAM_STATUS, false);
        String typePattern = checkStringParameter(input, PARAM_TYPE, false);
        boolean clip = checkFlagParameter(input, PARAM_CLIP, false, false);
        boolean withChildren = checkFlagParameter(input, PARAM_WCHILDREN, false, true);

        featHandler.suppress(modelname, featureName, featureNames, statusPattern, typePattern, clip, withChildren, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionDeleteParam(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        String paramName = checkStringParameter(input, PARAM_PARAM, false);

        featHandler.deleteParam(modelname, featureName, paramName, sessionId);

        return null;
	}

	private Hashtable<String, Object> actionParamExists(String sessionId, Hashtable<String, Object> input) throws JLIException {

        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, true);
        String namePattern = checkStringParameter(input, PARAM_PARAM, false);
        List<String> paramNames = null;
        Object namesObj = checkParameter(input, PARAM_PARAMS, false);
        if (namesObj!=null) {
        	paramNames = getStringListValue(namesObj);
        }
        
        boolean exists = featHandler.paramExists(filename, featureName, namePattern, paramNames, sessionId);

		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_EXISTS, exists);
       	return out;
	}

	private Hashtable<String, Object> actionListParams(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        String typePattern = checkStringParameter(input, PARAM_TYPE, false);
        boolean noDatumFeatures = checkFlagParameter(input, PARAM_NO_DATUM, false, false);
        boolean includeUnnamed = checkFlagParameter(input, PARAM_INC_UNNAMED, false, false);
        boolean noComponentFeatures = checkFlagParameter(input, PARAM_NO_COMP, false, false);

        String namePattern = checkStringParameter(input, PARAM_PARAM, false);
        List<String> paramNames = null;
        Object namesObj = checkParameter(input, PARAM_PARAMS, false);
        if (namesObj!=null) {
        	paramNames = getStringListValue(namesObj);
        }
        String valuePattern = checkStringParameter(input, JLParameterRequestParams.PARAM_VALUE, false);
        boolean encoded = checkFlagParameter(input, JLParameterRequestParams.PARAM_ENCODED, false, false);
        
        List<ParameterData> params = featHandler.listParams(modelname, featureName, namePattern, paramNames, 
        		typePattern, valuePattern, encoded, noDatumFeatures, includeUnnamed, noComponentFeatures, sessionId);

        if (params!=null && params.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outParams = new Vector<Map<String, Object>>();
			out.put(OUTPUT_PARAMLIST, outParams);
			Map<String, Object> outParam = null;
			for (ParameterData param : params) {
				outParam = new Hashtable<String, Object>();
				if (param.getName()!=null)
					outParam.put(PARAM_NAME, param.getName());
				if (param.getValue()!=null)
					outParam.put(JLParameterRequestParams.PARAM_VALUE, param.getValue());
				if (param.getType()!=null)
					outParam.put(PARAM_TYPE, param.getType());
				outParam.put(JLParameterRequestParams.PARAM_DESIGNATE, Boolean.valueOf(param.isDesignate()));
				outParam.put(JLParameterRequestParams.PARAM_ENCODED, Boolean.valueOf(param.isEncoded()));
				if (param.getOwnerName()!=null)
					outParam.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, param.getOwnerName());
				if (param.getOwnerId()>0)
					outParam.put(JLParameterResponseParams.OUTPUT_OWNER_ID, param.getOwnerId());
				if (param.getOwnerType()!=null)
					outParam.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, param.getOwnerType());
				
				outParams.add(outParam);
			}
			
			return out;
        }
		return null;
	}

	private Hashtable<String, Object> actionSetParam(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String featureName = checkStringParameter(input, PARAM_NAME, false);
        String paramName = checkStringParameter(input, PARAM_PARAM, true);
        Object value = checkParameter(input, JLParameterRequestParams.PARAM_VALUE, false);
        String type = checkStringParameter(input, PARAM_TYPE, false);
        Object designateObj = checkParameter(input, JLParameterRequestParams.PARAM_DESIGNATE, false);
        int designate=IJLParameter.DESIGNATE_UNKNOWN;
        if (designateObj!=null) {
        	if (checkFlagParameter(input, JLParameterRequestParams.PARAM_DESIGNATE, false, false))
        		designate = IJLParameter.DESIGNATE_ON;
        	else
        		designate = IJLParameter.DESIGNATE_OFF;
        }
        Boolean encoded = checkFlagParameter(input, JLParameterRequestParams.PARAM_ENCODED, false, false);
        boolean noCreate = checkFlagParameter(input, JLParameterRequestParams.PARAM_NO_CREATE, false, false);
        
        featHandler.setParam(filename, featureName, paramName, value, type, designate, encoded, noCreate, sessionId);
        
        return null;
	}

	private Hashtable<String, Object> actionUserSelectCsys(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        int max = checkIntParameter(input, PARAM_MAX, false, Integer.valueOf(1)).intValue();

        List<FeatSelectData> features = featHandler.userSelectCsys(modelname, max, sessionId);

        if (features!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outFeats = new Vector<Map<String, Object>>();
			out.put(OUTPUT_FEATLIST, outFeats);
			Map<String, Object> outFeat = null;
			for (FeatSelectData feat : features) {
				outFeat = new Hashtable<String, Object>();
				if (feat.getName()!=null)
					outFeat.put(PARAM_NAME, feat.getName());
				if (feat.getStatus()!=null)
					outFeat.put(PARAM_STATUS, feat.getStatus());
				if (feat.getFeatureType()!=null)
					outFeat.put(PARAM_TYPE, feat.getFeatureType());
				if (feat.getFeatureId()>0)
					outFeat.put(OUTPUT_ID, Integer.valueOf(feat.getFeatureId()));
				if (feat.getFeatureNumber()>0)
					outFeat.put(OUTPUT_FEATNO, Integer.valueOf(feat.getFeatureNumber()));
				
				outFeats.add(outFeat);
			}
			
			return out;
        }
        return null;
	}

}
