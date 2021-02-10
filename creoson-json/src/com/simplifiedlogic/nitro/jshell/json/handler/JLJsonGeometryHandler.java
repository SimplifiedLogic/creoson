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

import com.simplifiedlogic.nitro.jlink.data.ContourData;
import com.simplifiedlogic.nitro.jlink.data.EdgeData;
import com.simplifiedlogic.nitro.jlink.data.JLBox;
import com.simplifiedlogic.nitro.jlink.data.SurfaceData;
import com.simplifiedlogic.nitro.jlink.intf.IJLGeometry;
import com.simplifiedlogic.nitro.jshell.json.request.JLGeometryRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLGeometryResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "geometry" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonGeometryHandler extends JLJsonCommandHandler implements JLGeometryRequestParams, JLGeometryResponseParams {

	private IJLGeometry geomHandler = null;

	/**
	 * @param geomHandler
	 */
	public JLJsonGeometryHandler(IJLGeometry geomHandler) {
		this.geomHandler = geomHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_BOUND_BOX)) return actionBoundingBox(sessionId, input);
		else if (function.equals(FUNC_GET_SURFACES)) return actionGetSurfaces(sessionId, input);
		else if (function.equals(FUNC_GET_EDGES)) return actionGetEdges(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}

	private Hashtable<String, Object> actionBoundingBox(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        
        JLBox box = geomHandler.boundingBox(filename, sessionId);
        
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        if (box!=null) {
			out.put(OUTPUT_XMIN, box.getXmin());
			out.put(OUTPUT_XMAX, box.getXmax());
			out.put(OUTPUT_YMIN, box.getYmin());
			out.put(OUTPUT_YMAX, box.getYmax());
			out.put(OUTPUT_ZMIN, box.getZmin());
			out.put(OUTPUT_ZMAX, box.getZmax());
        }
    	return out;
	}

	private Hashtable<String, Object> actionGetSurfaces(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String filename = checkStringParameter(input, PARAM_MODEL, false);

        List<SurfaceData> surfaces = geomHandler.getSurfaces(filename, sessionId);
        
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        if (surfaces!=null) {
			Vector<Map<String, Object>> outSurfaces = new Vector<Map<String, Object>>();
			out.put(OUTPUT_SURFLIST, outSurfaces);
			Map<String, Object> outSurf = null;
			for (SurfaceData surf : surfaces) {
				outSurf = new Hashtable<String, Object>();
				if (surf.getSurfaceId()>0)
					outSurf.put(OUTPUT_SURFACE_ID, surf.getSurfaceId());
				if (surf.getFeatureId()>0)
					outSurf.put(OUTPUT_FEATURE_ID, surf.getFeatureId());
				outSurf.put(OUTPUT_AREA, surf.getArea());
				if (surf.getMinExtent()!=null)
					outSurf.put(OUTPUT_MIN_EXTENT, writePoint(surf.getMinExtent()));
				if (surf.getMaxExtent()!=null)
					outSurf.put(OUTPUT_MAX_EXTENT, writePoint(surf.getMaxExtent()));
				
				outSurfaces.add(outSurf);
			}
        }
    	return out;
	}

	private Hashtable<String, Object> actionGetEdges(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        List<Integer> idList = getIntArray(PARAM_SURFACE_IDS, checkParameter(input, PARAM_SURFACE_IDS, false));

        List<ContourData> conts = geomHandler.getEdges(filename, idList, sessionId);
        
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        if (conts!=null) {
			Vector<Map<String, Object>> outContours = new Vector<Map<String, Object>>();
			out.put(OUTPUT_CONTOURLIST, outContours);
			Map<String, Object> outCont = null;
			for (ContourData cont : conts) {
				outCont = new Hashtable<String, Object>();
				if (cont.getSurfaceId()>0)
					outCont.put(OUTPUT_SURFACE_ID, cont.getSurfaceId());
				if (cont.getTraversalType()==ContourData.TRAVERSE_INTERNAL)
					outCont.put(OUTPUT_TRAVERSAL, TRAVERSE_INTERNAL);
				else
					outCont.put(OUTPUT_TRAVERSAL, TRAVERSE_EXTERNAL);
				if (cont.getEdges()!=null) {
					Vector<Map<String, Object>> outEdges = new Vector<Map<String, Object>>();
					outCont.put(OUTPUT_EDGELIST, outEdges);
					Map<String, Object> outEdge = null;
					for (EdgeData edge : cont.getEdges()) {
						outEdge = new Hashtable<String, Object>();
						outEdge.put(OUTPUT_EDGE_ID, edge.getEdgeId());
						outEdge.put(OUTPUT_LENGTH, edge.getLength());
						if (edge.getStart()!=null)
							outEdge.put(OUTPUT_START, writePoint(edge.getStart()));
						if (edge.getEnd()!=null)
							outEdge.put(OUTPUT_END, writePoint(edge.getEnd()));

						outEdges.add(outEdge);
					}
				}
				
				outContours.add(outCont);
			}
        }
    	return out;
	}

}
