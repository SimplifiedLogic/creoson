/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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

import com.simplifiedlogic.nitro.jlink.data.BomChild;
import com.simplifiedlogic.nitro.jlink.data.GetPathsOutput;
import com.simplifiedlogic.nitro.jlink.data.JLTransform;
import com.simplifiedlogic.nitro.jlink.intf.IJLBom;
import com.simplifiedlogic.nitro.jshell.json.request.JLBomRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLBomResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "bom" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonBomHandler extends JLJsonCommandHandler implements JLBomRequestParams, JLBomResponseParams {

	private IJLBom bomHandler = null;

	/**
	 * @param bomHandler
	 */
	public JLJsonBomHandler(IJLBom bomHandler) {
		this.bomHandler = bomHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_GET_PATHS)) return actionGetPaths(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionGetPaths(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        boolean paths = checkFlagParameter(input, PARAM_PATHS, false, false);
        boolean skeleton = checkFlagParameter(input, PARAM_SKELETON, false, false);
        boolean toplevel = checkFlagParameter(input, PARAM_TOPLEVEL, false, false);
        boolean incTransform = checkFlagParameter(input, PARAM_TRANSFORMS, false, false);
        boolean excludeInactive = checkFlagParameter(input, PARAM_EXCLUDE_INACTIVE, false, false);
        boolean incSimpRep = checkFlagParameter(input, PARAM_SIMPREP, false, false);

        GetPathsOutput result = bomHandler.getPaths(modelname, skeleton, paths, toplevel, incTransform, true, excludeInactive, incSimpRep, sessionId);
		
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        if (result!=null) {
			if (result.getModelname()!=null)
				out.put(PARAM_MODEL, result.getModelname());
			if (result.getGeneric()!=null)
				out.put(OUTPUT_GENERIC, result.getGeneric());
			out.put(OUTPUT_HAS_SIMPREP, result.isHasSimpRep());

			if (result.getRoot()!=null) {
				Map<String, Object> child = getBomChild(result.getRoot());
				if (child!=null)
					out.put(OUTPUT_CHILDREN, child);
			}
        }
		return out;
	}
	
	private Map<String, Object> getBomChild(BomChild node) {
		if (node==null)
			return null;
		Map<String, Object> child = new Hashtable<String, Object>();
		if (node.getFilename()!=null)
			child.put(PARAM_MODEL, node.getFilename());
		if (node.getSequencePath()!=null)
			child.put(OUTPUT_SEQ_PATH, node.getSequencePath());
		if (node.getComponentPath()!=null && node.getComponentPath().size()>0)
			child.put(OUTPUT_PATH, node.getComponentPath());
		if (node.getTransformTable()!=null) {
			JLTransform xform = node.getTransformTable();
			Map<String, Object> rec = writeTransform(xform);
			
			child.put(OUTPUT_TRANSFORM, rec);
		}
		
//		if (node.getParent()!=null) {
//			
//		}
		
		if (node.getChildren()!=null && node.getChildren().size()>0) {
			List<Map<String, Object>> children = new Vector<Map<String, Object>>();
			child.put(OUTPUT_CHILDREN, children);
			Map<String, Object> child2 = null;
			for (BomChild node2 : node.getChildren()) {
				child2 = getBomChild(node2);
				if (child2!=null)
					children.add(child2);
			}
		}
		
		return child;
	}
}
