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
package com.simplifiedlogic.nitro.jlink.impl;

import java.util.List;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcGeometry.ContourTraversal;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.simplifiedlogic.nitro.jlink.calls.base.CallOutline3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallContour;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallContours;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallEdge;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallEdges;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallSurface;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.ContourData;
import com.simplifiedlogic.nitro.jlink.data.EdgeData;
import com.simplifiedlogic.nitro.jlink.data.JLBox;
import com.simplifiedlogic.nitro.jlink.data.SurfaceData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLGeometry;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLBoxMaker;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.JLPointMaker;
import com.simplifiedlogic.nitro.util.ModelItemLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLGeometry implements IJLGeometry {

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLGeometry#boundingBox(java.lang.String, java.lang.String)
     */
    @Override
    public JLBox boundingBox(String filename, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return boundingBox(filename, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLGeometry#boundingBox(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public JLBox boundingBox(String filename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.bound_box: " + filename, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        if (!(m instanceof CallSolid))
	            throw new JLIException("File '" + m.getFileName() + "' must be a solid");

	        CallSolid solid = (CallSolid)m;
	        
	        CallOutline3D outline = solid.getGeomOutline();
	        // attempt at an alternate method which produced wrong results
//	        ModelItemTypes types = ModelItemTypes.create();
//	        types.append(ModelItemType.ITEM_AXIS);
//	        types.append(ModelItemType.ITEM_COORD_SYS);
//	        types.append(ModelItemType.ITEM_POINT);
//	        CallOutline3D outline = solid.evalOutline(null, types);

	        if (outline==null)
	        	throw new JLIException("No outline found for part");
	        
	        return JLBoxMaker.create(outline);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.bound_box,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLGeometry#getSurfaces(java.lang.String, java.lang.String)
     */
    @Override
    public List<SurfaceData> getSurfaces(String filename, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return getSurfaces(filename, sess);
    }
    
    @Override
    public List<SurfaceData> getSurfaces(String filename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("geometry.get_surfaces: " + filename, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        SurfaceLooper looper = new SurfaceLooper();
	  
	        looper.loop(m);
	        
	        return looper.result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("geometry.get_surfaces,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLGeometry#getEdges(java.lang.String, java.util.List, java.lang.String)
     */
    @Override
    public List<ContourData> getEdges(String filename, List<Integer> surfaceIds, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return getEdges(filename, surfaceIds, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLGeometry#getEdges(java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public List<ContourData> getEdges(String filename, List<Integer> surfaceIds, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("geometry.get_edges: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

		if (surfaceIds==null || surfaceIds.size()==0)
			throw new JLIException("No surface ids parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, filename, true);

	        List<ContourData> result = new Vector<ContourData>();
	        for (Integer id : surfaceIds) {
	        	CallModelItem item = m.getItemById(ModelItemType.ITEM_SURFACE, id.intValue());
	        	if (!(item instanceof CallSurface))
	        		continue;
	        	CallSurface surface = (CallSurface)item;
	        	
	        	CallContours contours = surface.listContours();
	        	if (contours!=null) {
	        		int len = contours.getarraysize();
	        		CallContour cont;
	        		for (int i=0; i<len; i++) {
	        			cont = contours.get(i);
	        			ContourData cdata = new ContourData();
	        			result.add(cdata);
	        			cdata.setSurfaceId(id.intValue());
	        	    	ContourTraversal traversal = cont.getInternalTraversal();
	        	    	if (traversal.getValue()==ContourTraversal._CONTOUR_TRAV_EXTERNAL)
	        	    		cdata.setTraversalType(ContourData.TRAVERSE_EXTERNAL);
	        	    	else if (traversal.getValue()==ContourTraversal._CONTOUR_TRAV_INTERNAL)
	        	    		cdata.setTraversalType(ContourData.TRAVERSE_INTERNAL);
	        	    	
	        	    	CallEdges edges = cont.listElements();
	        	    	if (edges!=null) {
	        	    		int len2 = edges.getarraysize();
	        	    		if (len2>0) {
	            	    		CallEdge e;
	            	    		EdgeData edata;
	            	    		for (int k=0; k<len2; k++) {
	            	    			e = edges.get(k);
	            	    			edata = new EdgeData();
	            	    			edata.setEdgeId(e.getId());
	            	    	    	edata.setLength(e.evalLength());
	            	    	    	
	            	    			CallPoint3D ptStart = e.eval3DData(0.0).getPoint();
	            	    			CallPoint3D ptEnd = e.eval3DData(1.0).getPoint();
	            	    			edata.setStart(JLPointMaker.create(ptStart));
	            	    			edata.setEnd(JLPointMaker.create(ptEnd));

	            	    			cdata.addEdge(edata);
	            	    		}
	        	    		}
	        	    	}
	        		}
	        	}
	        }
	        
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("geometry.get_edges,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /**
     * An implementation of ModelItemLooper which collects information about surfaces in a model
     * @author Adam Andrews
     *
     */
    private static class SurfaceLooper extends ModelItemLooper {
    	/**
    	 * An output list of surface data
    	 */
    	public List<SurfaceData> result = new Vector<SurfaceData>();
    	
        /**
         * Constructor needed to initialize model item type
         */
    	public SurfaceLooper() {
            setSearchType(ModelItemType.ITEM_SURFACE);
    	}

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallSurface) {
        		CallSurface surf = (CallSurface)item;
	        	
	        	SurfaceData sdata = new SurfaceData();
	        	//sdata.surface = surf;
	        	sdata.setSurfaceId(surf.getId());
	        	
	            sdata.setArea(surf.evalArea());
	
	        	CallOutline3D extents = surf.getXYZExtents();
	        	sdata.setMinExtent(JLPointMaker.create(extents.get(0)));
	        	sdata.setMaxExtent(JLPointMaker.create(extents.get(1)));
	
	        	result.add(sdata);
        	}
        	
        	return false;
        }
    }
}
