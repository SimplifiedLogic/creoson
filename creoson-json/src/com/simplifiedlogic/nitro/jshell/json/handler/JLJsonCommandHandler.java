/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.DataUtils;
import com.simplifiedlogic.nitro.jlink.data.JLInertia;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.data.JLTransform;
import com.simplifiedlogic.nitro.jshell.json.request.JLFileRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFileResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Parent class for any handler of JSON function calls for a family of functions.
 * 
 * @author Adam Andrews
 *
 */
public abstract class JLJsonCommandHandler {

	/**
	 * Handle a server request for this family of functions.
	 * 
	 * @param sessionId The current session id.
	 * @param function The name of the function to execute.
	 * @param input Input parameter data for the function, converted from JSON.
	 * @return Results of the function, to be converted to JSON.
	 * @throws JLIException
	 */
	public abstract Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException;

    /**
     * Get a parameter value from input data.
     * 
     * @param input The input data to search.
     * @param parmname Name of the parameter.
     * @param required Whether the parameter is required; if true, the method will throw an exception if the parameter is missing.
     * @return The value of the parameter, or null if the parameter was not found.
     * @throws JLIException
     */
    public Object checkParameter(Map<String, Object> input, String parmname, boolean required) throws JLIException {
    	if (input==null) {
            if (required)
                throw new JLIException("No '" + parmname + "' parameter given");
    		return null;
    	}
        Object parm;
        parm = input.get(parmname);
        if (parm==null) {
            if (required)
                throw new JLIException("No '" + parmname + "' parameter given");
        }
        else {
        	// if it's a string value, check whether it's empty
            if (parm instanceof String && ((String)parm).trim().length()==0 && required) 
                throw new JLIException("No '" + parmname + "' parameter given");
        }
        return parm;
    }

    /**
     * Get a String parameter value from input data.
     * 
     * @param input The input data to search.
     * @param parmname Name of the parameter.
     * @param required Whether the parameter is required; if true, the method will throw an exception if the parameter is missing.
     * @return The value of the parameter converted to a string, or null if the parameter was not found.
     * @throws JLIException
     */
    public String checkStringParameter(Map<String, Object> input, String parmname, boolean required) throws JLIException {
        Object parm = checkParameter(input, parmname, required);
        if (parm==null && !required) return null;
        
        String val = DataUtils.getStringValue(parm); 
        if (val.length()==0) {
        	if (required)
                throw new JLIException("No '" + parmname + "' parameter given");
            else
            	return null;
        }
        return val;
    }

    /**
     * Get a boolean parameter value from input data.
     * 
     * @param input The input data to search.
     * @param parmname Name of the parameter.
     * @param required Whether the parameter is required; if true, the method will throw an exception if the parameter is missing.
     * @param dfltVal Default value for the parameter does not exist and is not required.
     * @return The value of the parameter converted to a boolean value, or the default value if the parameter was not found.
     * @throws JLIException
     */
    public boolean checkFlagParameter(Map<String, Object> input, String parmname, boolean required, boolean dfltVal) throws JLIException {
        Object parm = checkParameter(input, parmname, required);
        if (parm==null && !required) return dfltVal;
        
        if (parm instanceof Number) {
        	Number num = (Number)parm;
        	return num.intValue()!=0;
        }
        
    	return DataUtils.getBooleanValue(parm);
    }

    /**
     * Get an integer parameter value from input data.
     * 
     * @param input The input data to search.
     * @param parmname Name of the parameter.
     * @param required Whether the parameter is required; if true, the method will throw an exception if the parameter is missing.
     * @param dfltVal Default value for the parameter does not exist and is not required.
     * @return The value of the parameter converted to an integer, or the default value if the parameter was not found.
     * @throws JLIException
     */
    public Integer checkIntParameter(Map<String, Object> input, String parmname, boolean required, Integer dfltVal) throws JLIException {
        Object parm = checkParameter(input, parmname, required);
        if (parm==null && !required) return dfltVal;

        try {
            return new Integer(DataUtils.getIntValue(parm));
        }
        catch (NumberFormatException e) {
            throw new JLIException("Invalid integer value for parameter '" + parmname + "' : " + parm.toString());
        }
        catch (IOException e) {
            throw new JLIException("Error decoding value for parameter '" + parmname + "' : " + parm.toString());
        }
    }

    /**
     * Get a double parameter value from input data.
     * 
     * @param input The input data to search.
     * @param parmname Name of the parameter.
     * @param required Whether the parameter is required; if true, the method will throw an exception if the parameter is missing.
     * @return The value of the parameter converted to a double, or null if the parameter was not found.
     * @throws JLIException
     */
    public Double checkDoubleParameter(Map<String, Object> input, String parmname, boolean required) throws JLIException {
        Object parm = checkParameter(input, parmname, required);
        if (parm==null && !required) return null;

        try {
            return new Double(DataUtils.getDoubleValue(parm));
        }
        catch (NumberFormatException e) {
            throw new JLIException("Invalid double value for parameter '" + parmname + "' : " + parm.toString());
        }
        catch (IOException e) {
            throw new JLIException("Error decoding value for parameter '" + parmname + "' : " + parm.toString());
        }
    }

    /**
     * Get a map parameter value from the input data; this represents a sub-object within the input object
     * @param input The input data to search
     * @param parmname Name of the parameter
     * @param required Whether the parameter is required; if true, the method will throw an exception if the parameter is missing.
     * @return The value of the parameter converted to a Map<String, Object>, or null if the parameter was not found.
     * @throws JLIException
     */
    @SuppressWarnings("unchecked")
	public Map<String , Object> checkMapParameter(Map<String, Object> input, String parmname, boolean required) throws JLIException {
        Object parm = checkParameter(input, parmname, required);
        if (parm==null && !required) return null;
		if (!(parm instanceof Map<?, ?>))
			throw new JLIException("Invalid input data: " + parm.toString());

		return (Map<String, Object>)parm;
    }

    /**
     * Convert an object to an integer array.
     * 
     * <p>If the value is a java.util.List, it tries to convert each entry in the list
     * to an Integer and add it to the result.  If any entry in the list is not
     * and Integer object, an exception will be thrown.
     * 
     * <p>If the value is a java.lang.String, it assumes the string is a list of numbers
     * separated by spaces and converts each word to an Integer and adds it to the result.
     * If any entry in the string cannot be converted to an Integer object, an 
     * exception will be thrown.
     * 
     * @param paramName The name of the parameter - used only for error messages
     * @param value The object value to convert
     * @return The value as a list of Integers
     * @throws JLIException
     */
    public List<Integer> getIntArray(String paramName, Object value) throws JLIException {
    	List<Integer> result = null;
        if (value!=null) {
            if (value instanceof List) {
            	List<?> vlist = (List<?>)value;
            	result = new ArrayList<Integer>(vlist.size());
                if (result!=null) {
                    int len=vlist.size();
                    for (int i=0; i<len; i++) {
                        if (!(vlist.get(i) instanceof Integer))
                            throw new JLIException("Invalid data type for item " + i + " in the " + paramName + " array argument");
                        result.add((Integer)vlist.get(i));
                    }
                }
            }
            else if (value instanceof String) {
                if (((String)value).trim().length()==0)
                    return null;
                String[] pathSplit = ((String)value).split(" ");
                result = new ArrayList<Integer>(pathSplit.length);
                int n;
                for (int i=0; i<pathSplit.length; i++) {
                    try {
                        n = Integer.parseInt(pathSplit[i]);
                    }
                    catch (Exception e) {
                        throw new JLIException("Invalid data type for item " + i + " in the " + paramName + " array argument");
                    }
                    result.add(new Integer(n));
                }
            }
            else if (value instanceof Integer) {
                result = new ArrayList<Integer>(1);
                result.add((Integer)value);
            }
            else
                throw new JLIException("Invalid data type for " + paramName + " argument");
        }
        return result;
    }

	/**
	 * Converts a generic object to a java.util.List<String>.  If the object is
	 * a List or an array, it converts each entry into a String and adds it to 
	 * the result.  Otherwise it just converts the object to a single-entry list.
	 * 
	 * @param str The object to convert
	 * @return The value of the object as a string list
	 */
	public static List<String> getStringListValue(Object str) {
		if (str!=null) {
			if (str instanceof List<?>) {
				List<String> out = new ArrayList<String>();
				for (Object o : (List<?>)str) {
					if (o!=null)
						out.add(o.toString());
				}
				if (out.size()==0) return null;
				return out;
			}
			else if (str instanceof Object[]) {
				List<String> out = new ArrayList<String>();
				for (Object o : (Object[])str) {
					if (o!=null)
						out.add(o.toString());
				}
				if (out.size()==0) return null;
				return out;
			}
			else {
				List<String> out = new ArrayList<String>();
				out.add(str.toString());
				return out;
			}
		}
		return null;
	}
	
    /**
     * Convert a JLPoint object to a generic JSON structure
     * 
     * @param pt The JLPoint object to write
     * @return The JSON data as a Hashtable
     */
    protected Hashtable<String, Object> writePoint(JLPoint pt) {
    	if (pt==null)
    		return null;
		Hashtable<String, Object> out = new Hashtable<String, Object>();
		out.put(JLFileResponseParams.OUTPUT_X, pt.getX());
		out.put(JLFileResponseParams.OUTPUT_Y, pt.getY());
		out.put(JLFileResponseParams.OUTPUT_Z, pt.getZ());
		return out;
    }

    /**
     * Convert a generic JSON structure to a JLPoint object
     * @param rec The JSON data as a Map
     * @return The JLPoint object result
     * @throws JLIException
     */
    protected JLPoint readPoint(Map<String, Object> rec) throws JLIException {
    	if (rec==null)
    		return null;
    	Double x = checkDoubleParameter(rec, JLFileRequestParams.PARAM_X, false);
    	Double y = checkDoubleParameter(rec, JLFileRequestParams.PARAM_Y, false);
    	Double z = checkDoubleParameter(rec, JLFileRequestParams.PARAM_Z, false);
    	JLPoint pt = new JLPoint();
    	if (x!=null) pt.setX(x.doubleValue());
    	if (y!=null) pt.setY(y.doubleValue());
    	if (z!=null) pt.setZ(z.doubleValue());
    	return pt;
    }

    /**
     * Convert a JLTransform object to a generic JSON structure
     * 
     * @param xform The JLTransform object to write
     * @return The JSON data as a Hashtable.
     */
    protected Hashtable<String, Object> writeTransform(JLTransform xform) {
    	if (xform==null)
    		return null;
		Hashtable<String, Object> rec = new Hashtable<String, Object>();
		Map<String, Object> recPt;
		recPt = writePoint(xform.origin);
		if (recPt!=null)
			rec.put(JLFileResponseParams.OUTPUT_ORIGIN, recPt);
		recPt = writePoint(xform.xvector);
		if (recPt!=null)
			rec.put(JLFileResponseParams.OUTPUT_XAXIS, recPt);
		recPt = writePoint(xform.yvector);
		if (recPt!=null)
			rec.put(JLFileResponseParams.OUTPUT_YAXIS, recPt);
		recPt = writePoint(xform.zvector);
		if (recPt!=null)
			rec.put(JLFileResponseParams.OUTPUT_ZAXIS, recPt);

		if (xform.xrot!=null)
			rec.put(JLFileResponseParams.OUTPUT_X_ROT, xform.xrot);
		if (xform.yrot!=null)
			rec.put(JLFileResponseParams.OUTPUT_Y_ROT, xform.yrot);
		if (xform.zrot!=null)
			rec.put(JLFileResponseParams.OUTPUT_Z_ROT, xform.zrot);
		return rec;
    }

    /**
     * Convert an input JSON structure into a JLTransform object
     * 
     * @param rec The JSON structure as a Map 
     * @return The JLTransform object
     * @throws JLIException
     */
    protected JLTransform readTransform(Map<String, Object> rec) throws JLIException {
    	if (rec==null)
    		return null;
    	Map<String, Object> map;
    	Double rot;
    	JLTransform trans = new JLTransform();
    	map = checkMapParameter(rec, JLFileRequestParams.PARAM_ORIGIN, false);
    	if (map!=null) trans.origin = readPoint(map);

    	map = checkMapParameter(rec, JLFileRequestParams.PARAM_XAXIS, false);
    	if (map!=null) trans.xvector = readPoint(map);

    	map = checkMapParameter(rec, JLFileRequestParams.PARAM_YAXIS, false);
    	if (map!=null) trans.yvector = readPoint(map);

    	map = checkMapParameter(rec, JLFileRequestParams.PARAM_ZAXIS, false);
    	if (map!=null) trans.zvector = readPoint(map);
    	
    	rot = checkDoubleParameter(rec, JLFileRequestParams.PARAM_X_ROT, false);
    	if (rot!=null) trans.xrot = rot.doubleValue();

    	rot = checkDoubleParameter(rec, JLFileRequestParams.PARAM_Y_ROT, false);
    	if (rot!=null) trans.yrot = rot.doubleValue();

    	rot = checkDoubleParameter(rec, JLFileRequestParams.PARAM_Z_ROT, false);
    	if (rot!=null) trans.zrot = rot.doubleValue();

    	return trans;
    }

    /**
     * Convert a JLInertia object to a generic JSON structure
     * 
     * @param xform The JLInertia object to write
     * @return The JSON data as a Hashtable.
     */
    protected Hashtable<String, Object> writeInertia(JLInertia xform) {
    	if (xform==null)
    		return null;
		Hashtable<String, Object> rec = new Hashtable<String, Object>();
		Map<String, Object> recPt;
		recPt = writePoint(xform.xvector);
		if (recPt!=null)
			rec.put(JLFileResponseParams.OUTPUT_XAXIS, recPt);
		recPt = writePoint(xform.yvector);
		if (recPt!=null)
			rec.put(JLFileResponseParams.OUTPUT_YAXIS, recPt);
		recPt = writePoint(xform.zvector);
		if (recPt!=null)
			rec.put(JLFileResponseParams.OUTPUT_ZAXIS, recPt);

		return rec;
    }

    /**
     * Check whether a string contains a pattern (wildcard) value.  This checks for the
     * characters *, ?, | or [ in the string.
     * @param pattern The pattern to check
     * @return Whether the string contains a pattern
     */
    public boolean isPattern(String pattern) {
        if (pattern==null || pattern.length()==0) return false;
        if (pattern.indexOf('*') >= 0) return true;
        if (pattern.indexOf('?') >= 0) return true;
        if (pattern.indexOf('|') >= 0) return true;
        if (pattern.indexOf('[') >= 0) return true;
        return false;
    }
    
}
