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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.request.JLGeometryRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLGeometryResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "geometry" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonGeometryHelp extends JLJsonCommandHelp implements JLGeometryRequestParams, JLGeometryResponseParams {

	public static final String OBJ_CONTOUR_DATA = "ContourData";
	public static final String OBJ_EDGE_DATA = "EdgeData";
	public static final String OBJ_SURFACE_DATA = "SurfaceData";
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getCommand()
	 */
	public String getCommand() {
		return COMMAND;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpBoundingBox());
		list.add(helpGetEdges());
		list.add(helpGetSurfaces());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpContourData());
		list.add(helpEdgeData());
		list.add(helpSurfaceData());
		return list;
	}
	
	private FunctionTemplate helpBoundingBox() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_BOUND_BOX);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the bounding box for a model");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_XMIN, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Minimum X-coordinate of model");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_XMAX, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Maximum X-coordinate of model");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_YMIN, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Minimum Y-coordinate of model");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_YMAX, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Maximum Y-coordinate of model");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_ZMIN, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Minimum Z-coordinate of model");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_ZMAX, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Maximum Z-coordinate of model");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_XMIN, 5.0);
    	ex.addOutput(OUTPUT_XMAX, 30.0);
    	ex.addOutput(OUTPUT_YMIN, 12.5);
    	ex.addOutput(OUTPUT_YMAX, 15.0);
    	ex.addOutput(OUTPUT_ZMIN, 7.0);
    	ex.addOutput(OUTPUT_ZMAX, 15.3);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetSurfaces() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_SURFACES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the list of surfaces for a model");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_SURFLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_SURFACE_DATA);
    	ret.setDescription("List of surface information");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	ex.addOutput(OUTPUT_SURFLIST, list);
    	list.add(makeSurface(
    			43, 
    			79718.7771691097,
    			makePoint(0,0,-150.0),
    			makePoint(400,200,-150.0)));
    	list.add(makeSurface(
    			48, 
    			63202.03595752723,
    			makePoint(0,0,150.0),
    			makePoint(376.3,176.3,150.0)));
    	list.add(makeSurface(
    			53, 
    			119837.85406364943,
    			makePoint(0,0,-150.0),
    			makePoint(400.0,0,150.0)));
    	list.add(makeSurface(
    			55, 
    			46898.69161398972,
    			makePoint(400.0,0,-150.0),
    			makePoint(400.0,163.8,136.31679862020587)));
    	list.add(makeSurface(
    			57, 
    			104162.0513380309,
    			makePoint(0,0,-150.0),
    			makePoint(363.8,200.0,136.31679862020587)));
    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpSurfaceData() {
    	FunctionObject obj = new FunctionObject(OBJ_SURFACE_DATA);
    	obj.setDescription("Information about a surface");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_SURFACE_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Surface ID");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_AREA, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Surface area");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_MIN_EXTENT, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Minimum extent");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_MAX_EXTENT, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Maximum extent");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpGetEdges() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_EDGES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the list of edges for a model for given surfaces");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SURFACE_IDS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("List of surface IDs");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_CONTOURLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_CONTOUR_DATA);
    	ret.setDescription("List of contour information");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_SURFACE_IDS, new int[] {43, 48});
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
    	ex.addOutput(OUTPUT_CONTOURLIST, list);

    	list2 = new ArrayList<Map<String, Object>>();
    	list.add(makeContour(43, TRAVERSE_EXTERNAL, list2));
    	list2.add(makeEdge(
    			126, 
    			56.862827029975136,
    			makePoint(363.8,200.0,-150.0),
    			makePoint(400.0,163.8,-150.0)));
    	list2.add(makeEdge(
    			131, 
    			363.80000000000007,
    			makePoint(363.8, 200.0, -150.0),
    			makePoint(0.0, 200.0, -150.0)));
    	list2.add(makeEdge(
    			47, 
    			200.0,
    			makePoint(0.0, 200.0, -150.0),
    			makePoint(0.0, 0.0, -150.0)));
    	list2.add(makeEdge(
    			44, 
    			400.0,
    			makePoint(0.0, 0.0, -150.0),
    			makePoint(400.0, 0.0, -150.0)));
    	list2.add(makeEdge(
    			130, 
    			163.8,
    			makePoint(400.0, 0.0, -150.0),
    			makePoint(400.0, 163.8, 400.0)));

    	list2 = new ArrayList<Map<String, Object>>();
    	list.add(makeContour(48, TRAVERSE_INTERNAL, list2));
    	list2.add(makeEdge(
    			91, 
    			98.78345852457763,
    			makePoint(124.08212623658257, 132.63415039216727, 150.0),
    			makePoint(61.1946233569836, 132.63415039216727, 150.0)));
    	list2.add(makeEdge(
    			92, 
    			98.78345852457714,
    			makePoint(61.1946233569836, 132.63415039216727, 150.0),
    			makePoint(124.08212623658257, 132.63415039216727, 150.0)));

    	list2 = new ArrayList<Map<String, Object>>();
    	list.add(makeContour(48, TRAVERSE_EXTERNAL, list2));
    	list2.add(makeEdge(
    			190, 
    			363.80000000000007,
    			makePoint(363.8, 176.3, 150.0),
    			makePoint(0.0, 176.3, 150.0)));
    	list2.add(makeEdge(
    			191, 
    			19.63495410228761,
    			makePoint(376.3, 163.8, 150.0),
    			makePoint(363.8, 176.3, 150.0)));
    	list2.add(makeEdge(
    			194, 
    			163.8,
    			makePoint(376.3, 0.0, 150.0),
    			makePoint(376.3, 163.8, 150.0)));
    	list2.add(makeEdge(
    			196, 
    			376.30000000000007,
    			makePoint(0, 0, 150.0),
    			makePoint(376.3, 0, 150.0)));
    	list2.add(makeEdge(
    			197, 
    			176.3,
    			makePoint(0, 176.3, 150.0),
    			makePoint(0, 0, 150.0)));

    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpContourData() {
    	FunctionObject obj = new FunctionObject(OBJ_CONTOUR_DATA);
    	obj.setDescription("Information about a contour, which is a collection of connected edges");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_SURFACE_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Surface ID");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TRAVERSAL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Traversal type");
    	arg.setValidValues(new String[] {TRAVERSE_INTERNAL, TRAVERSE_EXTERNAL});
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_EDGELIST, FunctionSpec.TYPE_OBJARRAY, OBJ_EDGE_DATA);
    	arg.setDescription("List of edges");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionObject helpEdgeData() {
    	FunctionObject obj = new FunctionObject(OBJ_EDGE_DATA);
    	obj.setDescription("Information about an edge");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_EDGE_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Edge ID");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_LENGTH, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Edge length");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_START, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Edge start point");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_END, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Edge end point");
    	obj.add(arg);

        return obj;
    }
    
	private Map<String, Object> makeSurface(
			int id,
			double area,
			Map<String, Object> minExtent,
			Map<String, Object> maxExtent) {

		Map<String, Object> rec = new OrderedMap<String, Object>();
		rec.put(OUTPUT_SURFACE_ID, id);
		rec.put(OUTPUT_AREA, area);
		if (minExtent!=null)
			rec.put(OUTPUT_MIN_EXTENT, minExtent);
		if (maxExtent!=null)
			rec.put(OUTPUT_MAX_EXTENT, maxExtent);
		
		return rec;
	}

	private Map<String, Object> makeEdge(
			int id,
			double length,
			Map<String, Object> start,
			Map<String, Object> end) {

		Map<String, Object> rec = new OrderedMap<String, Object>();
		rec.put(OUTPUT_EDGE_ID, id);
		rec.put(OUTPUT_LENGTH, length);
		if (start!=null)
			rec.put(OUTPUT_START, start);
		if (end!=null)
			rec.put(OUTPUT_END, end);
		
		return rec;
	}

	private Map<String, Object> makeContour(
			int surfaceId,
			String traversal,
			List<Map<String, Object>> edges) {

		Map<String, Object> rec = new OrderedMap<String, Object>();
		rec.put(OUTPUT_SURFACE_ID, surfaceId);
		if (traversal!=null)
			rec.put(OUTPUT_TRAVERSAL, traversal);
		if (edges!=null)
			rec.put(OUTPUT_EDGELIST, edges);
		
		return rec;
	}

}
