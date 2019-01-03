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
package com.simplifiedlogic.nitro.jlink.impl;

import java.util.ArrayList;
import java.util.List;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.FeatureStatus;
import com.ptc.pfc.pfcFeature.FeatureType;
import com.ptc.pfc.pfcModel.ModelType;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallAssembly;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentFeat;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatures;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.BomChild;
import com.simplifiedlogic.nitro.jlink.data.GetPathsOutput;
import com.simplifiedlogic.nitro.jlink.data.SimpRepData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLBom;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.JLMatrixMaker;

/**
 * @author Adam Andrews
 */
public class JLBom implements IJLBom {
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLBom#getPaths(java.lang.String, boolean, boolean, boolean, boolean, java.lang.String)
	 */
    public GetPathsOutput getPaths(
	        String modelname,
	        boolean skeleton, 
	        boolean paths,
	        boolean toplevel,
	        boolean incTransform,
	        boolean transformAsTable, 
	        boolean excludeInactive,
	        String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return getPaths(modelname, skeleton, paths, toplevel, incTransform, transformAsTable, excludeInactive, sess);
    }
    	
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLBom#getPaths(java.lang.String, boolean, boolean, boolean, boolean, com.simplifiedlogic.nitro.rpc.JLISession)
	 */
    public GetPathsOutput getPaths(
	        String modelname,
	        boolean skeleton, 
	        boolean paths,
	        boolean toplevel,
	        boolean incTransform,
	        boolean transformAsTable, 
	        boolean excludeInactive,
	        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("bom.get_paths: " + modelname, NitroConstants.DEBUG_KEY);
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
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, modelname);
	        if (!(solid instanceof CallAssembly))
	            throw new JLIException("File '" + solid.getFileName() + "' must be an assembly.");
	        
	        GetPathsOutput out = new GetPathsOutput();
	        
	        out.setModelname(solid.getFileName());
	        String generic = solid.getGenericName();
	        if (generic==null)
	            generic="";
	        out.setGeneric(generic);

	        SimpRepData simpRep = JlinkUtils.getSimpRepInfo(solid);
	        if (simpRep!=null && !simpRep.isDefaultExclude() && simpRep.getItems()==null)
	        	simpRep = null;
	        
	        ArrayList<Integer> curPath = new ArrayList<Integer>();
	        BomChild dummy = new BomChild();
	        dummy.setFilename(out.getModelname());
	        dummy.setSequencePath("root");
	        walkGetPaths(session, (CallAssembly)solid, solid, dummy, "  ", "root", curPath, 0, skeleton, toplevel, paths, incTransform, transformAsTable, excludeInactive, simpRep);
	
	    	out.setRoot(dummy);
	        
	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("bom.get_paths," + modelname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /**
     * Recursive function used to walk an assembly and report on the components 
     * found.  This takes a solid, gets information about each sub-component, then
     * calls this walk method recursively to walk through any sub-assemblies. 
     * 
     * @param session The Creo session
     * @param baseAssembly The original assembly that the walk started from, used for calculating the 3D transforms
     * @param wrapping_solid The current solid being visited in the assembly 
     * @param parent The output data object which represents the current solid
     * @param indent The indentation level for debugging output
     * @param base_seq The sequence path for the current solid
     * @param curPath The component path for the current solid
     * @param pathlen The length of the component path; this allows you to use a portion of the component path
     * @param skeleton Whether to report and walk skeleton components in the output
     * @param toplevel Whether to only report the top-level components, and not walk sub-assemblies
     * @param paths Whether to return component paths
     * @param incTransform Whether to return the 3D transforms of components
     * @param transformAsTable Whether to report the 3D transform as a JLTransform rather than a JLMatrix (default)
     * @param excludeInactive Whether to exclude inactive components
     * @param simpRep Simplified Representation data for the baseAssembly
     * @throws JLIException
     * @throws jxthrowable
     */
    private void walkGetPaths(CallSession session, 
    		CallAssembly baseAssembly, 
    		CallSolid wrapping_solid, 
    		BomChild parent, 
    		String indent, 
    		String base_seq, 
    		List<Integer> curPath, int pathlen, 
    		boolean skeleton, boolean toplevel, boolean paths, 
    		boolean incTransform, boolean transformAsTable, boolean excludeInactive, 
    		SimpRepData simpRep) throws JLIException,jxthrowable {
    	
        // TODO: (10/29/2013) Skip over PRO_MDL_CE_SOLID components encountered, otherwise app may crash with no error.
        CallFeatures components = wrapping_solid.listFeaturesByType(Boolean.FALSE, FeatureType.FEATTYPE_COMPONENT);
        if (components==null)
            return;
        int len = components.getarraysize();
        if (len==0) return;
        // System.out.println(indent + "processing " + wrapping_solid.getFileName() + ", " + len + " child components");

        CallComponentFeat component;
        CallModelDescriptor desc = null;
        int type;
        String newseq;
        CallModel childModel;
        String filename;
        int id=0;
        int seq=0;
        CallFeature feat;
        for (int i=0; i<len; i++) {
        	feat = components.get(i);
        	if (!(feat instanceof CallComponentFeat))
        		continue;
            component = (CallComponentFeat)feat;
            try {
            	int status = component.getStatus();
            	// System.out.println("Comp ID:" + component.getId() + ", status=" + status);
                if (status!=FeatureStatus._FEAT_ACTIVE) {
                	if (!excludeInactive) {
                        if (status!=FeatureStatus._FEAT_INACTIVE && status!=FeatureStatus._FEAT_UNREGENERATED)
                    		continue;
                	}
                	else
                		continue;
                }
//                System.out.println("model item type: " + component.getFeatType() + " feature type: " + component.getFeatTypeName());
//              try {
                  desc = component.getModelDescr();
//              }
//              catch (XToolkitCantOpen e) {
                  /*
                   * Note: From Pro/ENGINEER Wildfire 4.0 onwards, the
                      methods pfcModel.Model.GetFullName,
                      pfcModel.Model.GetGenericName, and
                      pfcModel.Model.GetDescr throw an exception
                      pfcExceptions.XtoolkitCantOpen if called on a
                      model instance whose immediate generic is not in
                      session. Handle this exception and typecast the model
                      as pfcSolid.Solid, which in turn can be typecast as
                      pfcFamily.FamilyMember, and use the method
                      pfcFamily.FamilyMember.GetImmediateGenericI
                      nfo to get the model descriptor of the immediate generic
                      model. The model descriptor can be used to derive the
                      full name or generic name of the model. If you wish to
                      switch off this behavior and continue to run legacy
                      applications in the pre-Wildfire 4.0 mode, set the
                      configuration option
                      retrieve_instance_dependencies to
                      "instance_and_generic_deps"
                   */
//              }
                if (desc==null)
                	continue;
                childModel = null;
                if (!skeleton) {
                	childModel = session.getModelFromDescr(desc);
                    if (childModel instanceof CallSolid) {
                        boolean skel = ((CallSolid)childModel).getIsSkeleton();
                        if (skel)
                            continue;
                    }
                }
                type = desc.getType();
    
                newseq = base_seq + '.' + ++seq;
                if (paths || incTransform || simpRep!=null) {
                    id = component.getId();
                    if (curPath.size()>pathlen)
                        curPath.set(pathlen, new Integer(id));
                    else
                        curPath.add(new Integer(id));
                }
                
                // workaround for PTC bug https://support.ptc.com/apps/case_logger_viewer/auth/ssl/case=13183655
                if (simpRep!=null && simpRep.excludes(curPath.subList(0, pathlen+1)))
                	continue;
                
                filename = desc.getFileName();
    
                BomChild child = new BomChild();
                
                child.setSequencePath(newseq);
                child.setFilename(filename.toUpperCase());
                if (paths) {
                	List<Integer> newPath = new ArrayList<Integer>();
                	for (int k=0; k<pathlen+1; k++) {
                		newPath.add(curPath.get(k));
                	}
                    child.setComponentPath(newPath);
                    // System.out.println(filename.toUpperCase() + " " + newseq + " (" + newpath + ") (" + genPath(curPath, pathlen+1) + ")");
                }
                if (incTransform) {
                	if (transformAsTable)
                		child.setTransformTable(JlinkUtils.genTransformTable(baseAssembly, curPath, pathlen+1));
                	else
                		child.setTransform(JLMatrixMaker.create(JlinkUtils.genTransformTable2(baseAssembly, curPath, pathlen+1)));
                }
                parent.add(child);
                
                // recurse into the child components
                if (!toplevel && type==ModelType._MDL_ASSEMBLY) {
                    if (childModel==null)
                    	childModel = session.getModelFromDescr(desc);
                    if (childModel!=null && childModel instanceof CallSolid) {
                        // System.out.println(indent + "checking children for " + child.getFilename());
                        walkGetPaths(session, baseAssembly, (CallSolid)childModel, child, indent+"  ", newseq, curPath, pathlen+1, skeleton, toplevel, paths, incTransform, transformAsTable, excludeInactive, simpRep);
                    }
                }
            }
            catch (jxthrowable jxe) {
        		throw JlinkUtils.createException(jxe, "A PTC error has occurred when retrieving model " + component.getModelDescr().getFileName());
            }
        }
    }
}
