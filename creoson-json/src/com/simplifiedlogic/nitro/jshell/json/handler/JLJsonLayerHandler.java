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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.simplifiedlogic.nitro.jlink.data.LayerData;
import com.simplifiedlogic.nitro.jlink.intf.IJLLayer;
import com.simplifiedlogic.nitro.jshell.json.request.JLLayerRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLLayerResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "layer" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonLayerHandler extends JLJsonCommandHandler implements JLLayerRequestParams, JLLayerResponseParams {

	private IJLLayer layerHandler = null;

	/**
	 * @param layerHandler
	 */
	public JLJsonLayerHandler(IJLLayer layerHandler) {
		this.layerHandler = layerHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_LIST))
			return actionList(sessionId, input);
		else if (function.equals(FUNC_DELETE))
			return actionDelete(sessionId, input);
		else if (function.equals(FUNC_SHOW))
			return actionShow(sessionId, input);
		else if (function.equals(FUNC_EXISTS))
			return actionExists(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionList(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String layerName = checkStringParameter(input, PARAM_NAME, false);

        List<LayerData> layers = layerHandler.list(modelName, layerName, sessionId);
        
        if (layers!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outLayers = new Vector<Map<String, Object>>();
        	out.put(OUTPUT_LAYERS, outLayers);
			Map<String, Object> outLayer = null;
			for (LayerData layer : layers) {
				outLayer = new Hashtable<String, Object>();
				if (layer.getName()!=null)
					outLayer.put(OUTPUT_NAME, layer.getName());
				if (layer.getStatus()!=null)
					outLayer.put(OUTPUT_STATUS, layer.getStatus());
				if (layer.getLayerId()>0)
					outLayer.put(OUTPUT_ID, layer.getLayerId());
				
				outLayers.add(outLayer);
			}
			
        	return out;
        }
        return null;
	}
	
	private Hashtable<String, Object> actionDelete(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String layerName = checkStringParameter(input, PARAM_NAME, false);

        layerHandler.delete(modelName, layerName, sessionId);
        
        return null;
	}
	
	private Hashtable<String, Object> actionShow(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String layerName = checkStringParameter(input, PARAM_NAME, false);
        boolean show = checkFlagParameter(input, PARAM_SHOW, false, true);

        layerHandler.show(modelName, layerName, show, sessionId);
        
        return null;
	}
	
	private Hashtable<String, Object> actionExists(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelName = checkStringParameter(input, PARAM_MODEL, false);
        String layerName = checkStringParameter(input, PARAM_NAME, false);

        boolean exists = layerHandler.exists(modelName, layerName, sessionId);
        
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_EXISTS, exists);
        return out;
	}
	
}
