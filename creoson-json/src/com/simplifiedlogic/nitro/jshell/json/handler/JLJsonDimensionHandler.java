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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.simplifiedlogic.nitro.jlink.data.DimData;
import com.simplifiedlogic.nitro.jlink.data.DimDetailData;
import com.simplifiedlogic.nitro.jlink.data.DimSelectData;
import com.simplifiedlogic.nitro.jlink.data.DimToleranceData;
import com.simplifiedlogic.nitro.jlink.intf.IJLDimension;
import com.simplifiedlogic.nitro.jshell.json.request.JLDimensionRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLDimensionResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "dimension" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonDimensionHandler extends JLJsonCommandHandler implements JLDimensionRequestParams, JLDimensionResponseParams {

	private IJLDimension dimHandler = null;

	/**
	 * @param dimHandler
	 */
	public JLJsonDimensionHandler(IJLDimension dimHandler) {
		this.dimHandler = dimHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_SET)) return actionSet(sessionId, input);
		else if (function.equals(FUNC_COPY)) return actionCopy(sessionId, input);
		else if (function.equals(FUNC_LIST)) return actionList(sessionId, input);
		else if (function.equals(FUNC_LIST_DETAIL)) return actionListDetail(sessionId, input);
		else if (function.equals(FUNC_SHOW)) return actionShow(sessionId, input);
		else if (function.equals(FUNC_USER_SELECT)) return actionUserSelect(sessionId, input);
		else if (function.equals(FUNC_SET_TEXT)) return actionSetText(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionSet(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String dimname = checkStringParameter(input, PARAM_NAME, true);
        Object value = checkParameter(input, PARAM_VALUE, false);
        boolean encoded = checkFlagParameter(input, PARAM_ENCODED, false, false);
        
        dimHandler.set(modelname, dimname, value, encoded, sessionId);
        
		return null;
	}
	
	private Hashtable<String, Object> actionCopy(String sessionId, Hashtable<String, Object> input) throws JLIException {
	    String modelname = checkStringParameter(input, PARAM_MODEL, false);
	    String dimname = checkStringParameter(input, PARAM_NAME, true);
	    String to_name = checkStringParameter(input, PARAM_TONAME, true);
	    String to_model = checkStringParameter(input, PARAM_TOMODEL, false);
	    
	    dimHandler.copy(modelname, dimname, to_name, to_model, sessionId);

		return null;
	}

	private Hashtable<String, Object> actionList(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String namePattern = checkStringParameter(input, PARAM_NAME, false);
        List<String> dimNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	dimNames = getStringListValue(namesObj);
        }
        String dimType = checkStringParameter(input, PARAM_DIM_TYPE, false);
        boolean encoded = checkFlagParameter(input, PARAM_ENCODED, false, false);
        boolean select = checkFlagParameter(input, PARAM_SELECT, false, false);

        List<DimData> dims = dimHandler.list(modelname, namePattern, dimNames, dimType, encoded, select, sessionId);
        
        if (dims!=null && dims.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outDims = new Vector<Map<String, Object>>();
			out.put(OUTPUT_DIMLIST, outDims);
			Map<String, Object> outDim = null;
			for (DimData dim : dims) {
				outDim = new Hashtable<String, Object>();
				if (dim.getName()!=null)
					outDim.put(OUTPUT_NAME, dim.getName());
				if (dim.getValue()!=null)
					outDim.put(OUTPUT_VALUE, dim.getValue());
				outDim.put(OUTPUT_ENCODED, Boolean.valueOf(dim.isEncoded()));
				outDim.put(OUTPUT_DWG_DIM, Boolean.valueOf(dim.isDrawingDimension()));
				
				outDims.add(outDim);
			}
			
			return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionListDetail(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String namePattern = checkStringParameter(input, PARAM_NAME, false);
        List<String> dimNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	dimNames = getStringListValue(namesObj);
        }
        String dimType = checkStringParameter(input, PARAM_DIM_TYPE, false);
        boolean encoded = checkFlagParameter(input, PARAM_ENCODED, false, false);
        boolean select = checkFlagParameter(input, PARAM_SELECT, false, false);

        List<DimDetailData> dims = dimHandler.listDetail(modelname, namePattern, dimNames, dimType, encoded, select, sessionId);
        
        if (dims!=null && dims.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outDims = new Vector<Map<String, Object>>();
			out.put(OUTPUT_DIMLIST, outDims);
			Map<String, Object> outDim = null;
			for (DimDetailData dim : dims) {
				outDim = new Hashtable<String, Object>();
				if (dim.getName()!=null)
					outDim.put(OUTPUT_NAME, dim.getName());
				if (dim.getValue()!=null)
					outDim.put(OUTPUT_VALUE, dim.getValue());
				outDim.put(OUTPUT_ENCODED, Boolean.valueOf(dim.isEncoded()));
				outDim.put(OUTPUT_DWG_DIM, Boolean.valueOf(dim.isDrawingDimension()));
				
				DimToleranceData tol = dim.getTolerance();
				if (tol!=null) {
					if (tol.getToleranceType()!=null)
						outDim.put(OUTPUT_TOLERANCE_TYPE, tol.getToleranceType());
					if (DimToleranceData.TYPE_LIMITS.equals(tol.getToleranceType())) {
						outDim.put(OUTPUT_TOL_LOWER_LIMIT, tol.getLowerLimit());
						outDim.put(OUTPUT_TOL_UPPER_LIMIT, tol.getUpperLimit());
					}
					else if (DimToleranceData.TYPE_PLUS_MINUS.equals(tol.getToleranceType())) {
						outDim.put(OUTPUT_TOL_PLUS, tol.getPlus());
						outDim.put(OUTPUT_TOL_MINUS, tol.getMinus());
					}
					else if (DimToleranceData.TYPE_SYMMETRIC.equals(tol.getToleranceType())) {
						outDim.put(OUTPUT_TOL_SYMMETRIC_VALUE, tol.getSymmetricValue());
					}
					else if (DimToleranceData.TYPE_SYM_SUPERSCRIPT.equals(tol.getToleranceType())) {
						outDim.put(OUTPUT_TOL_SYMMETRIC_VALUE, tol.getSymmetricValue());
					}
					else if (DimToleranceData.TYPE_ISODIN.equals(tol.getToleranceType())) {
						if (tol.getTableName()!=null)
							outDim.put(OUTPUT_TOL_TABLE_NAME, tol.getTableName());
						outDim.put(OUTPUT_TOL_TABLE_COLUMN, tol.getTableColumn());
						if (tol.getTableType()!=null)
							outDim.put(OUTPUT_TOL_TABLE_TYPE, tol.getTableType());
					}
				}
				outDim.put(OUTPUT_SHEET, dim.getSheetNo());
				if (dim.getViewName()!=null)
					outDim.put(OUTPUT_VIEW_NAME, dim.getViewName());
				if (dim.getDimType()!=null)
					outDim.put(OUTPUT_DIM_TYPE, dim.getDimType());
				if (dim.getText()!=null && dim.getText().length>0)
					outDim.put(OUTPUT_TEXT, dim.getText());

				Map<String, Object> recPt = writePoint(dim.getLocation());
				if (recPt!=null)
					outDim.put(OUTPUT_LOCATION, recPt);
				
				outDims.add(outDim);
			}
			
			return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionShow(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String asmname = checkStringParameter(input, PARAM_ASSEMBLY, false);
        String dimname = checkStringParameter(input, PARAM_NAME, true);
        List<Integer> pathList = getIntArray(PARAM_PATH, checkParameter(input, PARAM_PATH, false));
        int[] path = null;
        if (pathList!=null && pathList.size()>0) {
        	int len = pathList.size();
        	path = new int[len];
        	for (int i=0; i<len; i++)
        		path[i] = pathList.get(i);
        }
        boolean show = checkFlagParameter(input, PARAM_SHOW, false, true);
        
        dimHandler.show(modelname, asmname, dimname, path, show, sessionId);
        
		return null;
	}

	private Hashtable<String, Object> actionUserSelect(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        int max = checkIntParameter(input, PARAM_MAX, false, Integer.valueOf(1)).intValue();

        List<DimSelectData> dims = dimHandler.userSelect(modelname, max, sessionId);
        
        if (dims!=null && dims.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outDims = new Vector<Map<String, Object>>();
			out.put(OUTPUT_DIMLIST, outDims);
			Map<String, Object> outDim = null;
			for (DimSelectData dim : dims) {
				outDim = new Hashtable<String, Object>();
				if (dim.getName()!=null)
					outDim.put(OUTPUT_NAME, dim.getName());
				if (dim.getValue()!=null)
					outDim.put(OUTPUT_VALUE, dim.getValue());
				outDim.put(OUTPUT_ENCODED, Boolean.valueOf(dim.isEncoded()));

				if (dim.getModelname()!=null)
					outDim.put(PARAM_MODEL, dim.getModelname());
				if (dim.getRelationId()!=null)
					outDim.put(PARAM_RELATION_ID, dim.getRelationId());
				
				outDims.add(outDim);
			}
			
			return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionSetText(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String dimname = checkStringParameter(input, PARAM_NAME, true);
        Object valueObj = checkParameter(input, PARAM_TEXT, false);
//        Object prefixObj = checkParameter(input, PARAM_PREFIX, false);
//        Object suffixObj = checkParameter(input, PARAM_SUFFIX, false);
        boolean encoded = checkFlagParameter(input, PARAM_ENCODED, false, false);

//        dimHandler.setText(modelname, dimname, valueObj, prefixObj, suffixObj, encoded, sessionId);
        dimHandler.setText(modelname, dimname, valueObj, encoded, sessionId);
        
        return null;
	}

}
