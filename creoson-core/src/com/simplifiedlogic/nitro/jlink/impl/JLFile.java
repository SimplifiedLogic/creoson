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
package com.simplifiedlogic.nitro.jlink.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.DatumSide;
import com.ptc.pfc.pfcBase.UnitType;
import com.ptc.pfc.pfcComponentFeat.ComponentConstraintType;
import com.ptc.pfc.pfcExceptions.XToolkitCantOpen;
import com.ptc.pfc.pfcExceptions.XToolkitInUse;
import com.ptc.pfc.pfcExceptions.XToolkitNotFound;
import com.ptc.pfc.pfcExceptions.XToolkitUserAbort;
import com.ptc.pfc.pfcFeature.FeatureStatus;
import com.ptc.pfc.pfcFeature.FeatureType;
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.ptc.pfc.pfcUnits.UnitDimensionConversion;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallAssembly;
import com.simplifiedlogic.nitro.jlink.calls.assembly.CallComponentPath;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentConstraint;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentConstraints;
import com.simplifiedlogic.nitro.jlink.calls.componentfeat.CallComponentFeat;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableRow;
import com.simplifiedlogic.nitro.jlink.calls.family.CallFamilyTableRows;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatureOperations;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeatures;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallSuppressOperation;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallCoordSystem;
import com.simplifiedlogic.nitro.jlink.calls.mfg.CallMFG;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.calls.model2d.CallModel2D;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItems;
import com.simplifiedlogic.nitro.jlink.calls.part.CallMaterial;
import com.simplifiedlogic.nitro.jlink.calls.part.CallPart;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallIntSeq;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallInertia;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallMassProperty;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallRegenInstructions;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.calls.units.CallUnit;
import com.simplifiedlogic.nitro.jlink.calls.units.CallUnitConversionOptions;
import com.simplifiedlogic.nitro.jlink.calls.units.CallUnitSystem;
import com.simplifiedlogic.nitro.jlink.calls.units.CallUnitSystems;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.AssembleInstructions;
import com.simplifiedlogic.nitro.jlink.data.FileAssembleResults;
import com.simplifiedlogic.nitro.jlink.data.FileInfoResults;
import com.simplifiedlogic.nitro.jlink.data.FileListInstancesResults;
import com.simplifiedlogic.nitro.jlink.data.FileOpenResults;
import com.simplifiedlogic.nitro.jlink.data.JLAccuracy;
import com.simplifiedlogic.nitro.jlink.data.JLConstraint;
import com.simplifiedlogic.nitro.jlink.data.JLConstraintInput;
import com.simplifiedlogic.nitro.jlink.data.JLMatrix;
import com.simplifiedlogic.nitro.jlink.data.JLTransform;
import com.simplifiedlogic.nitro.jlink.data.ListMaterialResults;
import com.simplifiedlogic.nitro.jlink.data.MasspropsData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLFile;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.FileListFilter;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.JLMatrixMaker;
import com.simplifiedlogic.nitro.util.MaterialLooper;
import com.simplifiedlogic.nitro.util.ModelItemLooper;
import com.simplifiedlogic.nitro.util.ModelLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLFile implements IJLFile {

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFile#open(java.lang.String, java.lang.String, java.util.List, java.lang.String, boolean, boolean, boolean, boolean, java.lang.String)
	 */
    public FileOpenResults open(
        String dirname, 
        String filename,
        List<String> filenames,
        String genericname,
        boolean display, 
        boolean activate,
        boolean newwin,
        boolean forceRegen,
        String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return open(dirname, filename, filenames, genericname, display, activate, newwin, forceRegen, sess);
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFile#open(java.lang.String, java.lang.String, java.util.List, java.lang.String, boolean, boolean, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
    public FileOpenResults open(
        String dirname, 
        String filename,
        List<String> filenames,
        String genericname,
        boolean display, 
        boolean activate,
        boolean newwin,
        boolean forceRegen,
        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.open: " + filename, NitroConstants.DEBUG_KEY);
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

	        if ("*.*".equals(filename) || "*".equals(filename))
	        	throw new JLIException("Not allowed to open all files in a directory");
	        
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);
	
	        String savedir = session.getCurrentDirectory();
	        if (dirname==null)
	            dirname = savedir;
	
	        FileOpenResults out = new FileOpenResults();
	        
	        int fileRevision=0;
	        if (filenames!=null ||
	            NitroUtils.isPattern(filename) ||
	            (filename!=null && filename.indexOf(' ')!=-1)) {
	
	            // special handling for lists
	
	            FileOpenResults tmpResults = new FileOpenResults();
	            String[] found_names = null;
	            
	            if (dirname != null && !dirname.equals(savedir)) {
	                JlinkUtils.changeDirectory(session, dirname);
	            }
	
	            try {
	                Vector<String> out_files = new Vector<String>();
	    
	                FileListFilter filter = new FileListFilter();
	                if (filenames!=null)
	                    filter.setNameList(filenames);
	                else if (filename==null)
	                    filter.setNamePattern(null);
	                else
	                    filter.setNamePattern(filename);
	                filter.setListDirs(false);
	                filter.setListFiles(true);
	    
	                if (filter.getNameList()!=null && !filter.getIsNamePattern()) {
	                    // get each specific file
	                    String[] namelist = filter.getNameList();
	                    int len = namelist.length;
	                    for (int i=0; i<len; i++) {
	                        tmpResults = new FileOpenResults();
	                        try {
	                        	CallModel m = doFileOpen(session, tmpResults, false, 
	                                    dirname, null, namelist[i], null,  
	                                    false, false, false, false);
	                            if (m!=null && len==1) {
	                            	CallModelDescriptor descr = m.getDescr();
	                            	fileRevision = descr.getFileVersion();
	                            }
	                        }
	                        catch (JLIException e) { 
	                            //ignore file not found exception 
	                        }
	                        found_names = tmpResults.getFilenames();
	                        if (found_names!=null && found_names.length>0)
	                            out_files.add(namelist[i]);
	                    }
	                }
	                else {
	                    // normal pattern matching
	                    File dir = new File(dirname);
	                    if (!dir.exists() || !dir.isDirectory())
	                        throw new JLIException("Directory '" + dirname + "' does not exist");
	    
	                    File[] files = dir.listFiles(filter);
	                    int numfiles = files.length;
	                    if (numfiles>0) {
	                        String last_subname = null;
	                        String subname = null;
	                        for (int i=0; i<numfiles; i++) {
	                            subname = NitroUtils.removeNumericExtension(files[i].getName());
	                            if (last_subname==null || !subname.equalsIgnoreCase(last_subname)) {
	                                tmpResults = new FileOpenResults();
	                                try {
	                                    CallModel m = doFileOpen(session, tmpResults, false, 
	                                            dirname, null, subname, null,  
	                                            false, false, false, false);
	                                    if (m!=null && numfiles==1) {
	                                    	CallModelDescriptor descr = m.getDescr();
	                                    	fileRevision = descr.getFileVersion();
	                                    }
	                                }
	                                catch (JLIException e) { 
	                                    //ignore file not found exception 
	                                }
	                                found_names = tmpResults.getFilenames();
	                                if (found_names!=null && found_names.length>0)
	                                    out_files.add(subname);
	                            }
	                            last_subname = subname;
	                        }
	                    }
	                }
	                out.setDirname(dirname.replace('\\', '/'));
	                if (found_names==null)
	                	found_names=new String[0];
	                out.setFilenames((String[])out_files.toArray(found_names));
	                if (fileRevision>0)
	                	out.setFileRevision(fileRevision);
	            }
	            finally {
	                if (dirname != null && !dirname.equals(savedir)) {
	                    JlinkUtils.changeDirectory(session, savedir);
	                }
	            }
	            return out;
	        }
	        CallModel m = doFileOpen(session, out, true, 
	                dirname, savedir, filename, genericname,  
	                display, newwin, activate, forceRegen);
	        if (m!=null) {
	        	CallModelDescriptor descr = null;
	        	try {
		            descr = m.getDescr();
	        	}
                catch (XToolkitCantOpen e) {
                	JlinkUtils.resolveGenerics(session, m);
		            descr = m.getDescr();
                }
                if (descr!=null)
                	fileRevision = descr.getFileVersion();
	        }
	
	        if (fileRevision>0)
	        	out.setFileRevision(fileRevision);
	        
	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.open,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /**
     * Perform a file open.  Utility method that may be called from multiple places in the code. 
     * @param session The Creo session
     * @param out The results of the file-open, to be returned to the user
     * @param change_dir Whether to change Creo's working directory
     * @param dirname Directory name for the file
     * @param savedir Creo's original working directory before this operation
     * @param filename File name
     * @param generic The generic instance for the file
     * @param display Whether to display the model after opening it
     * @param newwin Whether to display the model in a new window (false will open the model in the currently active window)
     * @param activate Whether to make the model the active model
     * @param regen_force Whether to force regeneration after opening the file
     * @return The Model object that was opened
     * @throws JLIException
     * @throws jxthrowable
     */
    private CallModel doFileOpen(CallSession session, FileOpenResults out, boolean change_dir, 
            String dirname, String savedir, String filename, String generic,  
            boolean display, boolean newwin, boolean activate, boolean regen_force) throws JLIException,jxthrowable {
        
    	CallModel m = JlinkUtils.getFile(session, dirname, filename);
        if (m!=null) {
            // TODO: check path?

            if (display) {
                if (newwin) {
                	CallWindow win = session.createModelWindow(m);
                    if (win!=null) win.activate();
                }
                JlinkUtils.displayModel(session, m, activate);
            }
            
            if (m instanceof CallSolid && regen_force) {
            	CallSolid solid = (CallSolid)m;
            	CallRegenInstructions reginst = CallRegenInstructions.create(Boolean.FALSE, Boolean.TRUE, null);
                JlinkUtils.regenerate(session, new RegenerateModel(solid, reginst), true);
                CallWindow win = session.getModelWindow(solid);
                if (win!=null)
                    win.refresh();
            }
            
            String path = JlinkUtils.getPath(m);
            if (path!=null)
            	out.setDirname(path.replace('\\', '/'));
            else
	            out.setDirname("");
            out.setFilenames(new String[] {m.getFullName() + "." + m.getDescr().getExtension()});
        }
        else {
            if (change_dir && dirname != null && !dirname.equals(savedir)) {
                JlinkUtils.changeDirectory(session, dirname);
            }

            try {
                CallModelDescriptor descr = CallModelDescriptor.createFromFileName(filename);
                // note that SetPath is ignored by RetrieveModel call...
                // but it _is_ used by RetrieveModelWithOpts
                descr.setPath(session.getCurrentDirectory());
                String topGeneric = generic;
                if (generic!=null) {
                	if (topGeneric!=null && topGeneric.indexOf('<')>=0)
                		topGeneric = JlinkUtils.extractTopGeneric(topGeneric);
                    descr.setGenericName(topGeneric);
                }
                try {
                    m = session.retrieveModel(descr);
                }
                catch (jxthrowable jxe) {
                	if (generic!=null && generic.indexOf('<')>=0)
                		generic = JlinkUtils.extractInstance(generic);
               		filename = JlinkUtils.instGenericToFilename(filename, generic);
                    throw new JLIException("Could not open file '" + filename + "' in directory " + session.getCurrentDirectory(), jxe);
                }
                if (display) {
                    if (newwin) {
                        CallWindow win = session.createModelWindow(m);
                        if (win!=null) win.activate();
                    }
                    JlinkUtils.displayModel(session, m, activate);
                }
    
                if (m instanceof CallSolid && regen_force) {
                	CallSolid solid = (CallSolid)m;
                	CallRegenInstructions reginst = CallRegenInstructions.create(Boolean.FALSE, Boolean.TRUE, null);
                    JlinkUtils.regenerate(session, new RegenerateModel(solid, reginst), true);
                    CallWindow win = session.getModelWindow(solid);
                    if (win!=null)
                        win.refresh();
                }
                
                String path = JlinkUtils.getPath(m);
                if (path!=null)
                	out.setDirname(path.replace('\\', '/'));
                else
                	out.setDirname(dirname.replace('\\', '/'));
                out.setFilenames(new String[] {m.getFullName() + "." + m.getDescr().getExtension()});
            }
            finally {
                if (change_dir && dirname != null && !dirname.equals(savedir)) {
                    JlinkUtils.changeDirectory(session, savedir);
                }
            }
        }
        return m;
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#openErrors(java.lang.String, java.lang.String)
     */
    public boolean openErrors(
	        String filename,
            String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return openErrors(filename, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#openErrors(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public boolean openErrors(
	        String filename,
	        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.open_errors: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (NitroUtils.isPattern(filename))
	            throw new JLIException("Name patterns are not supported for this command");
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallSolid solid = JlinkUtils.getModelSolid(session, filename);

	        return solid.hasRetrievalErrors();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.open_errors,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#rename(java.lang.String, java.lang.String, boolean, java.lang.String)
     */
    public String rename(
	        String filename,
	        String newname,
	        boolean onlyInSession,
            String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return rename(filename, newname, onlyInSession, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#rename(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public String rename(
	        String filename,
	        String newname,
	        boolean onlyInSession,
	        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.rename: " + filename + " to " + newname, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (newname==null || newname.trim().length()==0)
    		throw new JLIException("No new-name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (NitroUtils.isPattern(filename))
	            throw new JLIException("Name patterns are not supported for this command");
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        int extpos = NitroUtils.findFileExtension(newname);
	        String sub_name = (extpos==-1 ? newname : newname.substring(0, extpos));
	
	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        String modelname = m.getFileName();
	        int extpos2 = NitroUtils.findFileExtension(modelname);
	        String sub_name2 = (extpos2==-1 ? modelname : modelname.substring(0, extpos2));
	        
	        if (sub_name.equals(sub_name2)) {
	            return null;
	        }
	        
	        String pathdir = JlinkUtils.getPath(m);
	
	        // all this change-directory stuff is to get round the fact that a) the
	        // RenameFilesToo flag on Rename() isn't implemented, and b) it will 
	        // rename the disk file if and only if the file is in the current directory
	        boolean changedir=false;
	        String curdir = session.getCurrentDirectory();
	        if (onlyInSession) {
	        	// we're doing this because creo renames it on disk anyway, if you're in the model's working dir already
	            JlinkUtils.changeDirectory(session, System.getProperty("java.io.tmpdir"));
	            changedir=true;
	        }
	        else {
	            if (!NitroUtils.compareDirNames(curdir, pathdir)) {
	                JlinkUtils.changeDirectory(session, pathdir);
	                changedir=true;
	            }            
	        }
	
	        try {
	        	m.rename(sub_name, new Boolean(onlyInSession));
	        
	        	return m.getFileName();
	        }
	        catch (jxthrowable e) {
	        	throw new JLIException("Error renaming model; check to see there isn't another model in memory with the same name.");
	        }
	        finally {
	            if (changedir)
	                JlinkUtils.changeDirectory(session, curdir);
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.rename,"+filename+","+newname, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#save(java.lang.String, java.util.List, java.lang.String)
     */
    public void save(
    		String filename,
    		List<String> filenames, 
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        save(filename, filenames, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#save(java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void save(
    		String filename,
    		List<String> filenames, 
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.save: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        SaveLooper looper = new SaveLooper();
	        if (filenames!=null)
	            looper.setNameList(filenames);
	        else
	            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.loop();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.save,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#backup(java.lang.String, java.lang.String, java.lang.String)
     */
    public void backup(
    		String filename,
            String targetdir,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        backup(filename, targetdir, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#backup(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void backup(
    		String filename,
            String targetdir,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.backup: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (filename==null || filename.trim().length()==0)
    		throw new JLIException("No file name parameter given");
    	if (targetdir==null || targetdir.trim().length()==0)
    		throw new JLIException("No target directory parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        String savedir = session.getCurrentDirectory();
	        if (targetdir==null)
	            targetdir = savedir;
	        else
	            targetdir = JlinkUtils.resolveRelativePath(session, targetdir);

	        NitroUtils.validateDirFile(targetdir, filename, true);

	        CallModel m = JlinkUtils.getFile(session, targetdir, filename);
	        if (m!=null) {
	        	CallModelDescriptor descr = m.getDescr();
	            String savepath = descr.getPath();
	            descr.setPath(targetdir);
	            m.backup(descr);
//	            m.copy(filename, null); // alternate way to do it
	            descr = m.getDescr();
	            descr.setPath(savepath);  // does not appear to affect the model's actual descr
	            
	            descr = m.getDescr();
	        }
	        else {
	            throw new JLIException("File '" + filename + "' was not open.");
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.backup,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
/*    
    public void copy(
    		String filename,
            String targetdir,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        copy(filename, targetdir, sess);
    }
    	
    public void copy(
    		String filename,
            String targetdir,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.copy: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        Session session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        String olddir = session.GetCurrentDirectory();
	        if (targetdir==null)
	            targetdir = olddir;
	        else
	            targetdir = JlinkUtils.resolveRelativePath(session, targetdir);

	        NitroUtils.validateDirFile(targetdir, filename, true);

	        Model m = JlinkUtils.getFile(session, targetdir, filename);
	        if (m!=null) {
		        if (targetdir != null && !targetdir.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, targetdir);
		            try {
		            	System.out.println("current dir: " + session.GetCurrentDirectory());
			            m.Copy(filename, null);
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
		            m.Copy(filename, null);
		        }
	        }
	        else {
	            throw new JLIException("File '" + filename + "' was not open.");
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.copy,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
*/    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#erase(java.lang.String, java.util.List, boolean, java.lang.String)
     */
    public void erase(
    		String filename,
            List<String> filenames,
            boolean eraseChildren,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        erase(filename, filenames, eraseChildren, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#erase(java.lang.String, java.util.List, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void erase(
    		String filename,
            List<String> filenames,
            boolean eraseChildren,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.erase: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
            // do it this way because after using EraseWithDependencies, some items in the 
            // list will no longer be valid (and ignoring that exception seems to cause Pro/E to crash)
            EraseLooper looper = new EraseLooper();
	        if (filenames!=null)
	            looper.setNameList(filenames);
	        else
	            looper.setNamePattern(filename);
            looper.setDefaultToActive(false);
            looper.eraseChildren = eraseChildren;
            // loop until the list is empty or we've gone through the list without erasing anything
            looper.erasedSomething = true;
            while (looper.erasedSomething) {
                looper.erasedSomething = false;
                looper.setSession(session);
                looper.loop();
                if (looper.singleOp)
                	break;
            }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.erase,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#eraseNotDisplayed(java.lang.String)
     */
    public void eraseNotDisplayed(
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        eraseNotDisplayed(sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#eraseNotDisplayed(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void eraseNotDisplayed(
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.erase_not_displayed", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        session.eraseUndisplayedModels();

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.erase_not_displayed", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#regenerate(java.lang.String, java.util.List, boolean, java.lang.String)
     */
    public void regenerate(
    		String filename,
            List<String> filenames,
            boolean display,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        regenerate(filename, filenames, display, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#regenerate(java.lang.String, java.util.List, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void regenerate(
    		String filename,
            List<String> filenames,
            boolean display,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.regenerate: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        // Things that can regenerate:
	        //      Model2D
	        //      SheetOwner
	        //      View2D
	        //      Solid
	        //      XSection
	        //      ComponentFeat
	
	        // required for WF5 and higher
	        boolean resolveMode = JlinkUtils.prefixResolveModeFix(session, NitroConstants.DEBUG_REGEN_KEY);

	        try {
		        RegenerateLooper looper = new RegenerateLooper();
		        if (filenames!=null)
		            looper.setNameList(filenames);
		        else
		            looper.setNamePattern(filename);
		        looper.display = display;
	            looper.session = session;
		        looper.setDefaultToActive(true);
		        looper.setSession(session);
		        looper.setDebugKey(NitroConstants.DEBUG_REGEN_KEY);
		        looper.loop();
	        }
	        finally {
	        	JlinkUtils.postfixResolveModeFix(session, resolveMode, NitroConstants.DEBUG_REGEN_KEY);
	        }
    	}
    	catch (jxthrowable e) {
    		System.err.println("Error regenerating: " + e.getClass().getName() + " : "+ e.getMessage());
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.regenerate,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFile#getActive(java.lang.String)
	 */
    public FileOpenResults getActive(
            String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return getActive(sess);
    }

    /* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.impl.IJLFile#getActive(com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
    public FileOpenResults getActive(
        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.get_active", NitroConstants.DEBUG_KEY);
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
	
	        FileOpenResults out = new FileOpenResults();
	        
	        CallModel m;
	        
	        m = session.getCurrentModel();
	
	        String dirname = "";
	        String filename = "";
	        
	        String mfgName=null;
	        if (m==null) {
	            //throw new JLIException("No current model");
	            dirname = session.getCurrentDirectory();
	        }
	        else {
	            filename = m.getFileName();
                int mtype = m.getType();
                if (mtype==ModelType._MDL_MFG) {
                    CallModel m2 = JlinkUtils.getMfgModel(session, (CallMFG)m);
                    mfgName = m2.getFileName();
//                    filename = m2.getFileName();
                }

	            dirname = JlinkUtils.getPath(m);
	        }
	        
	        out.setDirname(dirname.replace('\\', '/'));
	        if (mfgName==null)
	        	out.setFilenames(new String[] {filename});
	        else
	        	out.setFilenames(new String[] {filename, mfgName});
	
	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.get_active", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#list(java.lang.String, java.util.List, java.lang.String)
     */
    public List<String> list(
    		String filename,
            List<String> filenames,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return list(filename, filenames, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#list(java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public List<String> list(
    		String filename,
            List<String> filenames,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.list: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        ListLooper looper = new ListLooper();
	        if (filenames!=null)
	            looper.setNameList(filenames);
	        else
	            looper.setNamePattern(filename);
	        looper.setDefaultToActive(false);
	        looper.setSession(session);
	        looper.loop();
	        if (looper.output==null)
	            return new Vector<String>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.list,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#exists(java.lang.String, java.lang.String)
     */
    public boolean exists(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return exists(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#exists(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public boolean exists(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.exists: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (filename==null || filename.trim().length()==0)
    		throw new JLIException("No file name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (NitroUtils.isPattern(filename))
	            throw new JLIException("Name patterns are not supported for this command");
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (m!=null) {
	        	return true;
	        }
	        else {
	        	return false;
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.exists,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getFileinfo(java.lang.String, java.lang.String)
     */
    public FileInfoResults getFileinfo(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return getFileinfo(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getFileinfo(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public FileInfoResults getFileinfo(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.get_fileinfo: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (NitroUtils.isPattern(filename))
	            throw new JLIException("Name patterns are not supported for this command");
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (m!=null) {
	        	FileInfoResults result = new FileInfoResults();
	        	CallModelDescriptor descr = m.getDescr();
                result.setFilename(m.getFileName());
	        	result.setFileRevision(descr.getFileVersion());
	        	
	        	result.setDirname(JlinkUtils.getPath(m));
	        	return result;
	        }
	        else {
	        	return null;
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.get_fileinfo,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#hasInstances(java.lang.String, java.lang.String)
     */
    public boolean hasInstances(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return hasInstances(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#hasInstances(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public boolean hasInstances(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.has_instances: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (NitroUtils.isPattern(filename))
	            throw new JLIException("Name patterns are not supported for this command");
	        
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        if (!(m instanceof CallSolid)) {
	            return false;
	        }
	        CallSolid solid = (CallSolid)m;

	        CallFamilyTableRows rows = null;
	        try {
	        	rows = solid.listRows();
	        }
	        catch (Exception e) {
	        	// fall through
	        }
	        if (rows!=null && rows.getarraysize()>0) {
	            return true;
	        }
	        
	        return false;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.has_instances,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#listInstances(java.lang.String, java.lang.String)
     */
    @Override
    public FileListInstancesResults listInstances(String filename, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return listInstances(filename, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#listInstances(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public FileListInstancesResults listInstances(String filename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.list_instances: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            if (NitroUtils.isPattern(filename))
                throw new JLIException("Name patterns are not supported for this command");
            
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        if (!(m instanceof CallSolid))
	            throw new JLIException("File '" + m.getFileName() + "' must be a solid");

	        CallSolid solid = (CallSolid)m;

	        if (filename==null)
	            filename = solid.getFileName();
	        int extpos = NitroUtils.findFileExtension(filename);
	        String generic = (extpos==-1 ? filename : filename.substring(0, extpos));
	        String ext = (extpos==-1 ? "" : filename.substring(extpos));

	        CallFamilyTableRows rows = solid.listRows();
	        FileListInstancesResults results = new FileListInstancesResults();
	        results.setDirname(JlinkUtils.getPath(solid).replace('\\', '/'));
	        results.setGeneric(generic);
	        
	        CallFamilyTableRow onerow;
	        int len = rows.getarraysize();
	        for (int i=0; i<len; i++) {
	            try {
		            onerow = rows.get(i);
	                results.addInstance(onerow.getInstanceName() + ext);
	            }
	            catch (jxthrowable jxe) {
	                throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving instance " + i));
	            }
	        }

	        return results;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.list_instances,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#massprops(java.lang.String, java.lang.String)
     */
    @Override
    public MasspropsData massprops(String filename, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return massprops(filename, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#massprops(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public MasspropsData massprops(String filename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.massprops: " + filename, NitroConstants.DEBUG_KEY);
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

	        CallMassProperty prop = solid.getMassProperty(null);

	        if (prop==null)
	        	throw new JLIException("No mass properties found for part");
	        
	        MasspropsData ret = new MasspropsData();
	        ret.setVolume(prop.getVolume());
	        ret.setMass(prop.getMass());
	        ret.setDensity(prop.getDensity());
	        ret.setSurfaceArea(prop.getSurfaceArea());
	        
	        CallInertia inertia = prop.getCenterGravityInertiaTensor();
	        if (inertia!=null) {
	        	ret.setCenterGravityInertiaTensor(JLMatrixMaker.writeInertia(inertia));
	        }
	        
	        inertia = prop.getCoordSysInertia();
	        if (inertia!=null) {
	        	ret.setCoordSysInertia(JLMatrixMaker.writeInertia(inertia));
	        }
	        
	        inertia = prop.getCoordSysInertiaTensor();
	        if (inertia!=null) {
	        	ret.setCoordSysInertiaTensor(JLMatrixMaker.writeInertia(inertia));
	        }
	        
	        return ret;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.massprops,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getMassUnits(java.lang.String, java.lang.String)
     */
    @Override
    public String getMassUnits(String filename, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return getMassUnits(filename, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getMassUnits(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public String getMassUnits(String filename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.get_mass_units: " + filename, NitroConstants.DEBUG_KEY);
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

            CallUnitSystem system = solid.getPrincipalUnits();

	        if (system==null)
//	        	throw new JLIException("No Unit System found for part");
	        	return null;

	        CallUnit unit = system.getUnit(UnitType.UNIT_MASS);
	        if (unit==null)
//	        	throw new JLIException("No Mass Unit found for part");
	        	return null;
	        
	        String ret = unit.getName();
	        
	        return ret;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.get_mass_units,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setMassUnits(java.lang.String, java.lang.String, boolean, java.lang.String)
     */
    @Override
    public void setMassUnits(String filename, String units, boolean convert, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        setMassUnits(filename, units, convert, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setMassUnits(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public void setMassUnits(String filename, String units, boolean convert, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.set_mass_units: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (units==null || units.trim().length()==0)
    		throw new JLIException("No units parameter given");
    	units = units.toLowerCase();

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        if (!(m instanceof CallSolid))
	            throw new JLIException("File '" + m.getFileName() + "' must be a solid");

	        CallSolid solid = (CallSolid)m;

            CallUnitSystem system = solid.getPrincipalUnits();

	        if (system!=null) {
	        	CallUnit unit = system.getUnit(UnitType.UNIT_MASS);
	        	if (unit!=null && unit.getName().equals(units))
	        		return;
	        }
	        
            CallUnitSystems systems = solid.listUnitSystems();
            boolean found=false;
            if (systems!=null) {
	            int len = systems.getarraysize();
	            for (int i=0; i<len; i++) {
	            	system = systems.get(i);
		        	CallUnit unit = system.getUnit(UnitType.UNIT_MASS);
		        	if (unit==null)
		        		continue;
		        	if (unit.getName().equals(units)) {
		        		UnitDimensionConversion convertOpt;
		        		if (convert)
		        			convertOpt = UnitDimensionConversion.UNITCONVERT_SAME_SIZE;
		        		else
		        			convertOpt = UnitDimensionConversion.UNITCONVERT_SAME_DIMS;
		        		CallUnitConversionOptions opts = CallUnitConversionOptions.create(convertOpt);
		        		opts.setDimensionOption(convertOpt);
		        		solid.setPrincipalUnits(system, opts);
		        		found=true;
		        		break;
		        	}
	            }
            }
            
            if (!found)
            	throw new JLIException("No mass unit '" + units + "' was found.");	
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.set_mass_units,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getLengthUnits(java.lang.String, java.lang.String)
     */
    @Override
    public String getLengthUnits(String filename, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return getLengthUnits(filename, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getLengthUnits(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public String getLengthUnits(String filename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.get_length_units: " + filename, NitroConstants.DEBUG_KEY);
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

            CallUnitSystem system = solid.getPrincipalUnits();

	        if (system==null)
//	        	throw new JLIException("No Unit System found for part");
	        	return null;

	        CallUnit unit = system.getUnit(UnitType.UNIT_LENGTH);
	        if (unit==null)
//	        	throw new JLIException("No Length Unit found for part");
	        	return null;
	        
	        String ret = unit.getName();
	        
	        return ret;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.get_length_units,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setLengthUnits(java.lang.String, java.lang.String, boolean, java.lang.String)
     */
    @Override
    public void setLengthUnits(String filename, String units, boolean convert, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        setLengthUnits(filename, units, convert, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setLengthUnits(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public void setLengthUnits(String filename, String units, boolean convert, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.set_length_units: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (units==null || units.trim().length()==0)
    		throw new JLIException("No units parameter given");
    	units = units.toLowerCase();

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        if (!(m instanceof CallSolid))
	            throw new JLIException("File '" + m.getFileName() + "' must be a solid");

	        CallSolid solid = (CallSolid)m;

            CallUnitSystem system = solid.getPrincipalUnits();

	        if (system!=null) {
	        	CallUnit unit = system.getUnit(UnitType.UNIT_LENGTH);
	        	if (unit!=null && unit.getName().equals(units))
	        		return;
	        }
	        
            CallUnitSystems systems = solid.listUnitSystems();
            boolean found=false;
            if (systems!=null) {
	            int len = systems.getarraysize();
	            for (int i=0; i<len; i++) {
	            	system = systems.get(i);
		        	CallUnit unit = system.getUnit(UnitType.UNIT_LENGTH);
		        	if (unit==null)
		        		continue;
		        	if (unit.getName().equals(units)) {
		        		UnitDimensionConversion convertOpt;
		        		if (convert)
		        			convertOpt = UnitDimensionConversion.UNITCONVERT_SAME_SIZE;
		        		else
		        			convertOpt = UnitDimensionConversion.UNITCONVERT_SAME_DIMS;
		        		CallUnitConversionOptions opts = CallUnitConversionOptions.create(convertOpt);
		        		opts.setDimensionOption(convertOpt);
		        		solid.setPrincipalUnits(system, opts);
		        		found=true;
		        		break;
		        	}
	            }
            }
            
            if (!found)
            	throw new JLIException("No length unit '" + units + "' was found.");	
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.set_length_units,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getRelations(java.lang.String, java.lang.String)
     */
    public List<String> getRelations(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return getRelations(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getRelations(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public List<String> getRelations(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.relations_get: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);
	
	        CallStringSeq lines = m.getRelations();
	        if (lines==null)
	        	return null;
	        int sz = lines.getarraysize();
	        if (sz==0)
	        	return null;
	        List<String> result = new ArrayList<String>();
	        for (int i=0; i<sz; i++) {
	        	result.add(lines.get(i));
	        }
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.relations_get,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setRelations(java.lang.String, java.util.List, java.lang.String)
     */
    public void setRelations(
    		String filename,
    		List<String> relations,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        setRelations(filename, relations, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setRelations(java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void setRelations(
    		String filename,
    		List<String> relations,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.relations_set: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);

	        if (relations==null || relations.size()==0)
	        	m.deleteRelations();
	        else {
		        CallStringSeq lines = CallStringSeq.create();
	        	int len = relations.size();
	        	for (int i=0; i<len; i++) 
	        		lines.append(relations.get(i));
	        	m.setRelations(lines);
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.relations_set,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getPostRegenRelations(java.lang.String, java.lang.String)
     */
    public List<String> getPostRegenRelations(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return getPostRegenRelations(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getPostRegenRelations(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public List<String> getPostRegenRelations(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.postregen_relations_get: " + filename, NitroConstants.DEBUG_KEY);
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
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);
	
	        CallStringSeq lines = m.getPostRegenerationRelations();
	        if (lines==null)
	        	return null;
	        int sz = lines.getarraysize();
	        if (sz==0)
	        	return null;
	        List<String> result = new ArrayList<String>();
	        for (int i=0; i<sz; i++) {
	        	result.add(lines.get(i));
	        }
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.postregen_relations_get,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setPostRegenRelations(java.lang.String, java.util.List, java.lang.String)
     */
    public void setPostRegenRelations(
    		String filename,
    		List<String> relations,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        setPostRegenRelations(filename, relations, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setPostRegenRelations(java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void setPostRegenRelations(
    		String filename,
    		List<String> relations,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.postregen_relations_set: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);

            Object[] inputs = new Object[] {
            	m,
            	relations
            };
	        JlinkUtils.callCreoFunction("com.simplifiedlogic.nitro.jshell.creo.SetPostRegenRelations", inputs);
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.postregen_relations_set,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#assemble(com.simplifiedlogic.nitro.jlink.data.AssembleInstructions, java.lang.String)
     */
    public FileAssembleResults assemble(AssembleInstructions instro, String sessionId) throws JLIException {
    	return assemble(
    			instro.getDirname(),
    			instro.getFilename(),
    			instro.getGenericName(),
    			instro.getIntoAsm(),
    			instro.getComponentPath(),
    			instro.getTransData(),
    			instro.getConstraints(),
    			instro.isPackageAssembly(),
    			instro.getRefModel(),
    			instro.isWalkChildren(),
    			instro.isAssembleToRoot(),
    			instro.isSuppress(),
    			sessionId);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#assemble(com.simplifiedlogic.nitro.jlink.data.AssembleInstructions, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public FileAssembleResults assemble(AssembleInstructions instro, AbstractJLISession sess) throws JLIException {
    	return assemble(
    			instro.getDirname(),
    			instro.getFilename(),
    			instro.getGenericName(),
    			instro.getIntoAsm(),
    			instro.getComponentPath(),
    			instro.getTransData(),
    			instro.getConstraints(),
    			instro.isPackageAssembly(),
    			instro.getRefModel(),
    			instro.isWalkChildren(),
    			instro.isAssembleToRoot(),
    			instro.isSuppress(),
    			sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#assemble(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.JLTransform, java.util.List, boolean, java.lang.String, boolean, boolean, java.lang.String)
     */
    public FileAssembleResults assemble(
    		String dirname,
    		String filename,
    		String genericName,
    		String intoAsm,
    		List<Integer> componentPath,
    		JLTransform transData,
    		List<JLConstraintInput> constraints,
    		boolean packageAssembly,
    		String refModel,
    		boolean walkChildren,
    		boolean assembleToRoot,
    		boolean suppress,
            String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return assemble(dirname, filename, genericName, intoAsm, componentPath, transData, constraints, packageAssembly, refModel, walkChildren, assembleToRoot, suppress, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#assemble(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.JLTransform, java.util.List, boolean, java.lang.String, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public FileAssembleResults assemble(
    		String dirname,
    		String filename,
    		String genericName,
    		String intoAsm,
    		List<Integer> componentPath,
    		JLTransform transData,
    		List<JLConstraintInput> constraintsInput,
    		boolean packageAssembly,
    		String refModel,
    		boolean walkChildren,
    		boolean assembleToRoot,
    		boolean suppress,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.assemble: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (filename==null || filename.trim().length()==0)
    		throw new JLIException("No file name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        JLMatrix initMatrix = JLMatrixMaker.create(transData);
	        
	        // translate constraints
	        List<JLConstraint> constraints = null;
	        if (constraintsInput!=null) {
	        	constraints = new ArrayList<JLConstraint>();
	        	for (JLConstraintInput in : constraintsInput)
	        		constraints.add(new JLConstraint(in));
	        }

	        // resolve directory
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        FileAssembleResults out = new FileAssembleResults();

	        // get component
	        String savedir = session.getCurrentDirectory();
	        if (dirname==null)
	            dirname = savedir;
	        CallModel m = doFileOpen(session, out, true, 
	                dirname, savedir, filename, genericName,  
	                false, false, false, false);
	        
	        // validate assembly
	        CallModel assemblyModel = JlinkUtils.getFile(session, intoAsm, false);
	        if (assemblyModel==null) {
	        	if (intoAsm==null)
	        		throw new JLIException("No active assembly to assemble into");
	        	else
	        		throw new JLIException("Assembly " + intoAsm + " was not open");
	        }
	        if (!(assemblyModel instanceof CallAssembly))
	            throw new JLIException("The model " + (intoAsm==null?assemblyModel.getFileName():intoAsm) + " is not an assembly");
	        CallAssembly assembly = (CallAssembly)assemblyModel;
	        String assemblyName = assembly.getFileName();
	        
	        // if path given, get sub-component of assembly
	        CallModel subComponent = null;
	        Pattern subComponentPattern = null;
	        CallComponentPath compPath = null;
	        CallIntSeq ids = CallIntSeq.create();
	        if (componentPath!=null) {
	        	CallFeature f = JlinkUtils.getFeatureForPath(session, assembly, componentPath);
	            if (!(f instanceof CallComponentFeat))
	                throw new JLIException("Component path does not point to a component feature");
	            subComponent = session.getModelFromDescr(((CallComponentFeat)f).getModelDescr());
	            // create pro/e component path
	            int len = componentPath.size();
	            for (int i=0; i<len; i++) {
	                ids.append(((Integer)componentPath.get(i)).intValue());
	            }
	        }
	        else if (refModel!=null) {
	        	if (NitroUtils.isPattern(refModel))
	        		subComponentPattern = Pattern.compile(NitroUtils.transformPattern(refModel.toUpperCase()));
	        	else
	        		subComponent = JlinkUtils.getFile(session, refModel, true);
	        }
	        else
	            subComponent = assembly;
	        compPath = CallComponentPath.create(assembly, ids);
	        
	        CallModelDescriptor descr = m.getDescr();
	        out.setFileRevision(descr.getFileVersion());

	        // this is really only needed when suppress=true
	        // required for WF5 and higher
	        boolean resolveMode = JlinkUtils.prefixResolveModeFix(session);

	        try {
		        if (((CallSolid)m).getIsSkeleton()) {
		        	try {
		        		assembly.assembleSkeleton((CallSolid)m);
		        	}
		        	catch (jxthrowable e) {
		        		throw new JLIException("Creo does not allow you to assemble more than one skeleton into an assembly");
		        	}
	
		        	CallSolid skel = assembly.getSkeleton();
		            if (skel!=null) {
		            	CallFeatures featList = assembly.listFeaturesByType(true, FeatureType.FEATTYPE_COMPONENT);
		                if (featList!=null) {
		                    int len = featList.getarraysize();
		                    CallComponentFeat compFeat;
		                    for (int i=0; i<len; i++) {
		                        compFeat = (CallComponentFeat)featList.get(i);
		                        String fname = compFeat.getModelDescr().getFileName();
		                        if (fname.equalsIgnoreCase(filename)) {
		        			        out.setFeatureId(compFeat.getId());
		    	                    if (suppress) {
		    	                    	suppressNewFeature(session, assembly, compFeat);
		    	                    }
		                        	break;
		                        }
		                    }
		                }
		            }
		        }
		        else {
		            List<WalkResult> subpaths = new Vector<WalkResult>();
		            if (walkChildren || (componentPath==null && refModel!=null)) {
		                if (subComponent==assembly)
			                subpaths.add(new WalkResult(compPath, assembly, assembly));
		                else if (subComponentPattern!=null && subComponentPattern.matcher(assemblyName.toUpperCase()).matches()) //assemblyName.matches(subComponentPattern))
			                subpaths.add(new WalkResult(compPath, assembly, assembly));
		                	
		                ArrayList<Integer> curPath = new ArrayList<Integer>();
		                ArrayList<String> assembliesProcessed = new ArrayList<String>();
		                walkAssemble(session, assembly, assembly, "root", curPath, 0, (subComponent!=null ? subComponent.getFileName() : null), subComponentPattern, subpaths,
		                        assembliesProcessed, !walkChildren, assembleToRoot);
		                if (subpaths.size()==0) {
		                    throw new JLIException("File '" + subComponent.getFileName() + "' does not exist in assembly");
		                }
		            }
		            else {
		            	if (subComponent!=null)
		            		subpaths.add(new WalkResult(compPath, assembly, subComponent));
		            }
		            
			        // assemble component
			        CallTransform3D transform = CallTransform3D.create(JLMatrixMaker.export(initMatrix));
	
			        // add constraints
			        int len = 0;
			        if (constraints!=null)
			        	len = constraints.size();
			        WalkResult res;
			        if (len>0) {
			        	int cntAssembled = 0;
		                JLConstraint csys_con = getCsysConstraint(constraints);
		                String asmrefCsys=null;
		                if (csys_con!=null && csys_con.asmref!=null)
		                	asmrefCsys = csys_con.asmref;
			            int num_comps = subpaths.size();
			            for (int i=0; i<num_comps; i++) {
			                res = (WalkResult)subpaths.get(i);
			                compPath = res.compPath;
			                assembly = assembleToRoot ? (CallAssembly)assemblyModel : res.parent;
			                subComponent = res.item;
			                if (asmrefCsys!=null && NitroUtils.isPattern(asmrefCsys)) {
			                    List<JLConstraint> other_con = getNonCsysConstraint(constraints);
			                    //AssembleLooper looper = new AssembleLooper(assembly, subComponent, m, compPath, transform, csys_con, other_con);
			                    AssembleLooper2 looper = new AssembleLooper2();
			                    looper.setNamePattern(asmrefCsys);
			                    
			                    looper.loop(subComponent);
			                    
			                    if (looper.items!=null && looper.items.size()>0) {
			                        Collections.sort(looper.items);
			                        
			                        int num_items = looper.items.size();
			                        ModelItemEntry entry;
			                        for (int k=0; k<num_items; k++) {
			                            entry = (ModelItemEntry)looper.items.get(k);
			                            assembleItem(session, assembly, subComponent, m, compPath, transform, csys_con, other_con, entry.item, suppress, packageAssembly);
			                        }
			                        looper.items.clear();
			                        cntAssembled += num_items;
			                    }
			                }
			                else {
			                	CallComponentConstraints constrs = null;
			                	try {
			                		constrs = makeConstraints(subComponent, m, compPath, constraints);
			                	}
			                	catch (JLIException e) {
			                		// constraints are ANDed together, so if one of them fails then the whole set of constraints does
			                		// if this fails, then fall through and let the check at the end throw an error
			                		continue;
			                	}
			    
			                    CallComponentFeat newfeat = (CallComponentFeat)assembly.assembleComponent((CallSolid)m, transform);
			                    
			                    if (constrs!=null)
			                        newfeat.setConstraints(constrs, null);
			                    else if (!packageAssembly)
						        	newfeat.redefineThroughUI();
	
			                    if (suppress) {
			                    	suppressNewFeature(session, assembly, newfeat);
			                    }
	
			                    cntAssembled++;
			                }
			            }
	
		                if (asmrefCsys!=null && cntAssembled==0)
		                    throw new JLIException("No Coord Systems matching '" + asmrefCsys + "' were found in the assembly component");
			        }
			        else {
			            try {
			            	CallComponentFeat newfeat = (CallComponentFeat)assembly.assembleComponent((CallSolid)m, transform);
			                int cid = newfeat.getId();
			                out.setFeatureId(cid);
	
					        if (!packageAssembly)
					        	newfeat.redefineThroughUI();
	
		                    if (suppress) {
		                    	suppressNewFeature(session, assembly, newfeat);
		                    }
	
			            }
			            catch (XToolkitUserAbort e) {
			                // ignore user aborts
			            }
			        }
	
		        }
	        }
	        finally {
		        // this is really only needed when suppress=true
	        	JlinkUtils.postfixResolveModeFix(session, resolveMode);
	        }

	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.assemble,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /**
     * Get the CSYS constraint (if any) from a list of input constraints
     * @param constraints A list of input constraints
     * @return The CSYS constraint from the list
     * @throws JLIException
     * @throws jxthrowable
     */
    private JLConstraint getCsysConstraint(List<JLConstraint> constraints) throws JLIException, jxthrowable {
        int len = constraints.size();

        JLConstraint con;
        JLConstraint ccon=null;
        for (int i=0; i<len; i++) {
            con = (JLConstraint)constraints.get(i);
            if (con.type!=ComponentConstraintType._ASM_CONSTRAINT_CSYS)
                continue;
            if (ccon!=null) 
                throw new JLIException("Not allowed to have more than one CSYS constraint");
            
            ccon = con;
        }
        return ccon;
    }
    
    /**
     * Get the non-CSYS constraints (if any) from a list of input constraints
     * @param constraints A list of input constraints
     * @return The non-CSYS constraint from the list
     * @throws jxthrowable
     */
    private List<JLConstraint> getNonCsysConstraint(List<JLConstraint> constraints) throws jxthrowable {
        int len = constraints.size();

        JLConstraint con;
        List<JLConstraint> newcons = new Vector<JLConstraint>();
        for (int i=0; i<len; i++) {
            con = (JLConstraint)constraints.get(i);
            if (con.type==ComponentConstraintType._ASM_CONSTRAINT_CSYS)
                continue;
            
            newcons.add(con);
        }
        if (newcons.size()==0)
            return null;
        return newcons;
    }
    
    /**
     * Make a set of Creo constraints from inputs.  Calls makeConstraint() to make each individual constraint.
     * @param subComponent The assembly the component will be assembled to
     * @param m The component being assembled
     * @param compPath The component path of the component that the new item will be constrained to
     * @param constraints The list of input constraint data
     * @return A set of Creo constraints
     * @throws JLIException
     * @throws jxthrowable
     */
    public CallComponentConstraints makeConstraints(CallModel subComponent, CallModel m, CallComponentPath compPath, List<JLConstraint> constraints) throws JLIException, jxthrowable {
        if (constraints==null)
            return null;
        int len = constraints.size();

        JLConstraint con;
        CallComponentConstraints constrs = null;
        for (int i=0; i<len; i++) {
            con = (JLConstraint)constraints.get(i);
            CallComponentConstraint ctrt = makeConstraint(subComponent, m, compPath, con);
            if (ctrt==null)
            	continue;
            if (constrs==null)
            	constrs = CallComponentConstraints.create();
            constrs.append(ctrt);
        }
        return constrs;
    }

    /**
     * Create a Creo constraint from input constraint data
     * @param subComponent The assembly the component will be assembled to
     * @param m The component being assembled
     * @param compPath The component path of the component that the new item will be constrained to
     * @param con Input constraint data
     * @return The new Creo constraint
     * @throws JLIException
     * @throws jxthrowable
     */
    protected CallComponentConstraint makeConstraint(CallModel subComponent, CallModel m, CallComponentPath compPath, JLConstraint con) throws JLIException, jxthrowable {

    	CallModelItem asmItem, compItem;

        asmItem = null;
        compItem = null;
        if (con.type==ComponentConstraintType._ASM_CONSTRAINT_CSYS) {
            if (con.asmref!=null) {
            	// set the assembly reference
                asmItem = subComponent.getItemByName(ModelItemType.ITEM_COORD_SYS, con.asmref);
                if (asmItem==null)
                    throw new JLIException("Coord System '" + con.asmref + "' was not found in the assembly component");
            }
            else
            	return null;
            if (con.compref!=null) {
            	// set the component reference
                compItem = m.getItemByName(ModelItemType.ITEM_COORD_SYS, con.compref);
                if (compItem==null)
                    throw new JLIException("Coord System '" + con.compref + "' was not found in the new component");
            }
            else
            	return null;
        }
        CallComponentConstraint ctrt = CallComponentConstraint.create(ComponentConstraintType.FromInt(con.type));
        if (asmItem!=null) {
        	CallSelection compConstrain = CallSelection.createModelItemSelection(asmItem, compPath);
            ctrt.setAssemblyReference(compConstrain);
        }
        if (compItem!=null) {
        	CallSelection compSel = CallSelection.createModelItemSelection(compItem, null);
            ctrt.setComponentReference(compSel);
        }
        if (con.offset!=null) {
            ctrt.setOffset(con.offset);
        }
        if (con.asmDatum!=DatumSide._DATUM_SIDE_NONE) {
            ctrt.setAssemblyDatumSide(DatumSide.FromInt(con.asmDatum));
        }
        if (con.compDatum!=DatumSide._DATUM_SIDE_NONE) {
            ctrt.setComponentDatumSide(DatumSide.FromInt(con.asmDatum));
        }
        return ctrt;
    }

    /**
     * Assemble a component into an assembly
     * @param assembly The assembly to receive the component
     * @param subComponent
     * @param part
     * @param compPath
     * @param transform The 3D transform to apply to the component
     * @param csys_con A CSYS-type constraint
     * @param other_con Other constraints
     * @param item
     * @throws JLIException
     * @throws jxthrowable
     */
    private void assembleItem(
    		CallSession session, 
    		CallAssembly assembly,
    		CallModel subComponent,
    		CallModel part,
    		CallComponentPath compPath,
    		CallTransform3D transform,
            JLConstraint csys_con,
            List<JLConstraint> other_con,
            CallModelItem item,
            boolean suppress,
            boolean packageAssembly
            ) throws JLIException, jxthrowable {
        
    	CallComponentConstraints constrs = makeConstraints(subComponent, part, compPath, other_con);
        if (csys_con!=null && csys_con.compref!=null) {
            JLConstraint clone = csys_con.copy();
            clone.asmref = item.getName();
            CallComponentConstraint ctrt = makeConstraint(subComponent, part, compPath, clone);
            if (ctrt!=null) {
                if (constrs==null)
                    constrs = CallComponentConstraints.create();
                constrs.append(ctrt);
            }
        }                

        CallComponentFeat newfeat = (CallComponentFeat)assembly.assembleComponent((CallSolid)part, transform);
        
        if (constrs!=null)
            newfeat.setConstraints(constrs, null);
        else if (!packageAssembly)
        	newfeat.redefineThroughUI();

        if (suppress) {
        	suppressNewFeature(session, assembly, newfeat);
        }

    }

    private void suppressNewFeature(CallSession session, CallAssembly assembly, CallFeature newfeat) throws jxthrowable {
    	//System.out.println("Suppressing new feature: "+newfeat.getId());
        CallFeatureOperations featOps = CallFeatureOperations.create();
        CallSuppressOperation supop = newfeat.createSuppressOp();
        supop.setClip(true);
        featOps.append(supop);
    	assembly.executeFeatureOps(featOps, null);
//        JlinkUtils.regenerate(session, new RegenerateFeatureOps(assembly, featOps), true);
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#refresh(java.lang.String, java.lang.String)
     */
    public void refresh(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        refresh(filename, sess);
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#refresh(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void refresh(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.refresh: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        CallWindow win = null;
	        if (m!=null && m instanceof CallSolid) {
	            // get window for part
	            win = session.getModelWindow((CallSolid)m);
	        }
	        if (win==null) {
	            // get default window
	            win = session.getCurrentWindow();
	        }
	        if (win!=null)
	            win.refresh();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.refresh,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#repaint(java.lang.String, java.lang.String)
     */
    public void repaint(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        repaint(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#repaint(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void repaint(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.repaint: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
	        CallWindow win = null;
	        if (m!=null && m instanceof CallSolid) {
	            // get window for part
	            win = session.getModelWindow((CallSolid)m);
	        }
	        if (win==null) {
	            // get default window
	            win = session.getCurrentWindow();
	        }
	        if (win!=null)
	            win.repaint();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.repaint,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getTransform(java.lang.String, java.util.List, java.lang.String, java.lang.String)
     */
    public JLTransform getTransform(
    		String asm, 
    		List<Integer> path, 
    		String csys, 
    		String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
    	return getTransform(asm, path, csys, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getTransform(java.lang.String, java.util.List, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public JLTransform getTransform(
    		String asm, 
    		List<Integer> path, 
    		String csys, 
    		AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.get_transform", NitroConstants.DEBUG_KEY);
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
	
	        CallModel assemblyModel = JlinkUtils.getFile(session, asm, false);
	        if (!(assemblyModel instanceof CallAssembly))
	            throw new JLIException("The model " + (asm==null?assemblyModel.getFileName():asm) + " is not an assembly");
	        CallAssembly assembly = (CallAssembly)assemblyModel;

	        // if path given, get sub-component of assembly
	        CallModel subComponent;
	        CallComponentPath compPath = null;
	        CallIntSeq ids = CallIntSeq.create ();
	        if (path!=null) {
	            CallFeature f = JlinkUtils.getFeatureForPath(session, assembly, path);
	            if (!(f instanceof CallComponentFeat))
	                throw new JLIException("Component path does not point to a component feature");
	            subComponent = session.getModelFromDescr(((CallComponentFeat)f).getModelDescr());
	            // create pro/e component path
	            int len = path.size();
	            for (int i=0; i<len; i++) {
	                ids.append(((Integer)path.get(i)).intValue());
	            }
	        }
	        else
	            subComponent = assemblyModel;

	        CallCoordSystem partSys = null;
	        if (csys!=null) {
	            partSys = (CallCoordSystem)subComponent.getItemByName(ModelItemType.ITEM_COORD_SYS, csys);
	            if (partSys==null)
	                throw new JLIException("Coord System '" + csys + "' was not found.");
	            
	        }
	        else {
	        	CallModelItems itemList = subComponent.listItems(ModelItemType.ITEM_COORD_SYS);
	            if (itemList!=null && itemList.getarraysize()>0) {
	                partSys = (CallCoordSystem)itemList.get(0);
	            }
	        }
	        if (partSys==null) {
	            throw new JLIException("No Coord System found.");
	        }

	        compPath = CallComponentPath.create(assembly, ids);
	        
	        CallTransform3D featTrans = compPath.getTransform(true);
	        
	        JLMatrix mat1 = JLMatrixMaker.create(featTrans.getMatrix());

	        CallTransform3D partTransform = partSys.getCoordSys();

	        JLMatrix mat2 = JLMatrixMaker.create(partTransform.getMatrix());
	        JLMatrixMaker.dot(mat1, mat2);

	        JLTransform out = JLMatrixMaker.writeTransformTable(mat1);
	        return out;

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.get_transform", start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#isActive(java.lang.String, java.lang.String)
     */
    public boolean isActive(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return isActive(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#isActive(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public boolean isActive(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.is_active: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (filename==null || filename.trim().length()==0)
    		throw new JLIException("No file name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (m!=null) {
	        	return JlinkUtils.isModelActive(session, m);
	        }
	        else {
	            return false;
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.is_active,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#display(java.lang.String, boolean, java.lang.String)
     */
    public void display(
    		String filename,
    		boolean activate, 
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        display(filename, activate, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#display(java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void display(
    		String filename,
    		boolean activate, 
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.display: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (filename==null || filename.trim().length()==0)
    		throw new JLIException("No file name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        if (m!=null) {
	        	JlinkUtils.displayModel(session, m, activate);
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.display,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#closeWindow(java.lang.String, java.lang.String)
     */
    public void closeWindow(
    		String filename,
            String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        closeWindow(filename, sess);
    }
    	
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#closeWindow(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public void closeWindow(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.close_window: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	    	if (filename!=null && filename.trim().length()>0) {
		        CallModel m = JlinkUtils.getFile(session, filename, true);
		        if (m!=null) {
		        	CallWindow win = session.getModelWindow(m);
		        	if (win!=null)
		        		win.close();
		        }
	    	}
	    	else {
	    		CallWindow win = session.getCurrentWindow();
	        	if (win!=null)
	        		win.close();
	    	}
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.close_window,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#listSimpReps(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<String> listSimpReps(String filename, String name, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return listSimpReps(filename, name, sess);
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#listSimpReps(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public List<String> listSimpReps(String filename, String name, AbstractJLISession sess) throws JLIException { 
		DebugLogging.sendDebugMessage("file.list_simp_reps: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            if (NitroUtils.isPattern(filename))
                throw new JLIException("Name patterns are not supported for this command");
            
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, filename, true);
	        
            ListSimpRepLooper looper = new ListSimpRepLooper();
            looper.setNamePattern(name);
            
            looper.loop(m);

            return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.list_simp_reps,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getCurrentMaterial(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public List<ListMaterialResults> getCurrentMaterial(String filename, boolean includeNonMatchingParts, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getCurrentMaterial(filename, includeNonMatchingParts, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getCurrentMaterial(java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<ListMaterialResults> getCurrentMaterial(String filename, boolean includeNonMatchingParts, AbstractJLISession sess) throws JLIException {
		DebugLogging.sendDebugMessage("file.get_cur_material: " + filename, NitroConstants.DEBUG_KEY);
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

	        GetCurrentMaterialLooper looper = new GetCurrentMaterialLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.includeNonMatchingParts = includeNonMatchingParts;
	        looper.loop();

	        return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.get_cur_material,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#listMaterials(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public List<ListMaterialResults> listMaterials(String filename, String materialName, boolean includeNonMatchingParts, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);

        return listMaterials(filename, materialName, includeNonMatchingParts, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#listMaterials(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<ListMaterialResults> listMaterials(String filename, String materialName, boolean includeNonMatchingParts, AbstractJLISession sess) throws JLIException {
		DebugLogging.sendDebugMessage("file.list_materials: " + filename, NitroConstants.DEBUG_KEY);
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

	        if (materialName!=null)
	        	materialName = NitroUtils.removeExtension(materialName);

	        ListModelMaterialLooper looper = new ListModelMaterialLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.materialName = materialName;
	        looper.includeNonMatchingParts = includeNonMatchingParts;
	        looper.loop();
	        
	        return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.list_materials,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#loadMaterialFile(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<String> loadMaterialFile(String filename, String dirname, String materialName, boolean set, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);

        return loadMaterialFile(filename, dirname, materialName, set, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#loadMaterialFile(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> loadMaterialFile(String filename, String dirname, String materialFile, boolean set, AbstractJLISession sess) throws JLIException {
		DebugLogging.sendDebugMessage("file.load_material_file: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (materialFile==null || materialFile.trim().length()==0)
    		throw new JLIException("No Material File parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        materialFile = NitroUtils.removeExtension(materialFile);

	        LoadMaterialLooper looper = new LoadMaterialLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.materialName = materialFile;
	        looper.setCurrent = set;

	        if (dirname!=null) {
		        dirname = JlinkUtils.resolveRelativePath(session, dirname);

		        String savedir = session.getCurrentDirectory();
	            if (!dirname.equals(savedir)) {
	                JlinkUtils.changeDirectory(session, dirname);
	            }
	    		try {
	    	        looper.loop();
	            }
	            finally {
	                if (dirname != null && !dirname.equals(savedir)) {
	                    JlinkUtils.changeDirectory(session, savedir);
	                }
	            }
	        }
	        else {
		        looper.loop();
	        }
    		
	        if (looper.output==null)
	            return new Vector<String>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.load_material_file,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setCurrentMaterial(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> setCurrentMaterial(String filename, String materialName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return setCurrentMaterial(filename, materialName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#setCurrentMaterial(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> setCurrentMaterial(String filename, String materialName, AbstractJLISession sess) throws JLIException {
		DebugLogging.sendDebugMessage("file.set_cur_material: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (materialName==null || materialName.trim().length()==0)
    		throw new JLIException("No Material Name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            if (NitroUtils.isPattern(materialName))
                throw new JLIException("Material Name patterns are not supported for this command");
            
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        if (materialName!=null) {
		        materialName = NitroUtils.removeExtension(materialName);
	
		        HasMaterialLooper looper = new HasMaterialLooper();
	            looper.setNamePattern(filename);
		        looper.setDefaultToActive(true);
		        looper.setSession(session);
		        looper.materialName = materialName;
		        looper.loop();
	        }

	        // NOTE: matl=null does NOT appear to actually work.  No error, but the material
	        // does not get unset.
	        SetCurrentMaterialLooper looper = new SetCurrentMaterialLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.materialName = materialName;
	        looper.loop();

	        if (looper.output==null)
	            return new Vector<String>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.set_cur_material,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#deleteMaterial(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> deleteMaterial(String filename, String materialName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
		
		return deleteMaterial(filename, materialName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#deleteMaterial(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> deleteMaterial(String filename, String materialName, AbstractJLISession sess) throws JLIException {
		DebugLogging.sendDebugMessage("file.delete_material: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (materialName==null || materialName.trim().length()==0)
    		throw new JLIException("No Material Name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            if (NitroUtils.isPattern(materialName))
                throw new JLIException("Material Name patterns are not supported for this command");
            
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        materialName = NitroUtils.removeExtension(materialName);

	        DeleteMaterialLooper looper = new DeleteMaterialLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.materialName = materialName;
	        looper.loop();

	        if (looper.output==null)
	            return new Vector<String>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.delete_material,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getAccuracy(java.lang.String, java.lang.String)
     */
    public JLAccuracy getAccuracy(
    		String filename,
    		String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
		
		return getAccuracy(filename, sess);
    }

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLFile#getAccuracy(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    public JLAccuracy getAccuracy(
    		String filename,
            AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("file.get_accuracy: " + filename, NitroConstants.DEBUG_KEY);
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

	        Double d = solid.getAbsoluteAccuracy();
	        boolean relative = false;
	        if (d==null) {
	        	d = solid.getRelativeAccuracy();
	        	if (d!=null)
	        		relative=true;
	        }
	        if (d==null)
	        	return null;
	        
	        JLAccuracy result = new JLAccuracy();
	        result.setAccuracy(d.doubleValue());
	        result.setRelative(relative);
	        return result;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("file.get_accuracy,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

    /**
     * Walk the hierarchy of an assembly to find a list of sub-components to assemble a new component to
     * 
     * @param session
     * @param baseAssembly
     * @param wrapping_solid
     * @param base_seq
     * @param curPath
     * @param pathlen
     * @param searchName
     * @param searchPattern
     * @param results An output list of results from the walk
     * @param assembliesProcessed
     * @param onelevel
     * @param assembleToRoot
     * @throws JLIException
     * @throws jxthrowable
     */
    private void walkAssemble(CallSession session, CallAssembly baseAssembly, CallSolid wrapping_solid, String base_seq, List<Integer> curPath, int pathlen, String searchName, Pattern searchPattern, List<WalkResult> results, List<String> assembliesProcessed, boolean onelevel, boolean assembleToRoot) throws JLIException, jxthrowable {
    	CallFeatures components = wrapping_solid.listFeaturesByType(Boolean.FALSE, FeatureType.FEATTYPE_COMPONENT);
        if (components==null)
            return;
        int len = components.getarraysize();
        if (len==0) return;

        CallComponentFeat component;
        CallModelDescriptor desc = null;
        int type;
        String newseq;
        CallModel child;
        String filename;
        int id=0;
        int seq=0;
        for (int i=0; i<len; i++) {
            component = (CallComponentFeat)components.get(i);
            try {
                if (component.getStatus()!=FeatureStatus._FEAT_ACTIVE)
                    continue;
//                try {
                    desc = component.getModelDescr();
//                }
//                catch (XToolkitCantOpen e) {
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
//                }
                child = null;
                type = desc.getType();
    
                newseq = base_seq + '.' + ++seq;
                id = component.getId();
                if (curPath.size()>pathlen)
                    curPath.set(pathlen, new Integer(id));
                else
                    curPath.add(new Integer(id));
                
                filename = desc.getFileName();
                if ((searchName!=null && filename.equalsIgnoreCase(searchName)) || 
                	(searchPattern!=null && searchPattern.matcher(filename.toUpperCase()).matches())) {
                    CallIntSeq ids = CallIntSeq.create();
                    for (int k=0; k<pathlen+1; k++) {
                        ids.append(((Integer)curPath.get(k)).intValue());
                    }
                    if (child==null)
                        child = session.getModelFromDescr(desc);
                    WalkResult res = new WalkResult(CallComponentPath.create(baseAssembly, ids), (CallAssembly)wrapping_solid, child);
                    results.add(res);
                }
    
                if (!onelevel) {
                    // recurse into the child components
                    if (type==ModelType._MDL_ASSEMBLY) {
                        // don't need to assemble into the same assembly more than once, so skip dups
                        if (!assembleToRoot) {
	                        if (assembliesProcessed.contains(filename))
	                            continue;
	                        assembliesProcessed.add(filename);
                        }
                        if (child==null)
                            child = session.getModelFromDescr(desc);
                        if (child!=null && child instanceof CallSolid) {
    //                        System.out.println(indent + "checking children for " + instname);
                            walkAssemble(session, baseAssembly, (CallSolid)child, newseq, curPath, pathlen+1, searchName, searchPattern, results, assembliesProcessed, onelevel, assembleToRoot);
                        }
                    }
                }
            }
            catch (jxthrowable jxe) {
                throw new JLIException(JlinkUtils.ptcError(jxe, "A PTC error has occurred when retrieving component " + component.getModelDescr().getFileName()));
            }
        }
    }
    
    /**
     * @author Adam Andrews
     *
     */
    private class WalkResult {
    	/**
    	 * 
    	 */
    	CallComponentPath compPath;
    	/**
    	 * 
    	 */
    	CallAssembly parent;
    	/**
    	 * 
    	 */
    	CallModel item;
        
        /**
         * @param compPath
         * @param parent
         * @param item
         */
        public WalkResult(CallComponentPath compPath, CallAssembly parent, CallModel item) {
            this.compPath = compPath;
            this.parent = parent;
            this.item = item;
        }
    }

    /**
     * An implementation of ModelLooper which gets a list of open model names
     * @author Adam Andrews
     *
     */
    private static class ListLooper extends ModelLooper {
        /**
         * An output list of model names
         */
        public List<String> output = null;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {
        	String name = null;
            try {
                name = m.getFileName();
            }
            catch (XToolkitCantOpen e) {
            	JlinkUtils.resolveGenerics(getSession(), m);
                name = m.getFileName();
            }   
            if (currentName==null)
            	currentName = name;
            
            if (output==null)
                output = new Vector<String>();
            output.add(currentName);

            return false;
        }
    }

    /**
     * An implementation of ModelLooper which saves a list of models
     * @author Adam Andrews
     *
     */
    private static class SaveLooper extends ModelLooper {
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {
        	m.save();
            return false;
        }
    }

    /**
     * An implementation of ModelLooper which regenerates a list of models
     * @author Adam Andrews
     *
     */
    private static class RegenerateLooper extends ModelLooper {
        /**
         * Whether to display each model before regenerating
         */
        public boolean display = false;
        /**
         * The Creo session
         */
        public CallSession session = null;
        
        public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
            if (display) {
                JlinkUtils.displayModel(getSession(), m, true);
            }

            // Things that can regenerate:
            //      Model2D
            //      SheetOwner
            //      View2D
            //      Solid
            //      XSection
            //      ComponentFeat

            if (m instanceof CallSolid) {
            	CallSolid solid = (CallSolid)m;
            	CallRegenInstructions reginst = CallRegenInstructions.create(Boolean.FALSE, null, null);
                JlinkUtils.regenerate(session, new RegenerateModel(solid, reginst), false, NitroConstants.DEBUG_REGEN_KEY);
            }
            else if (m instanceof CallModel2D) {
                if (!display) {
//                    Window win = getSession().GetModelWindow(m);
//                    if (win!=null) // can cause a general toolkit error the second time around
                        JlinkUtils.displayModel(getSession(), m, true);
                }
                CallModel2D m2d = (CallModel2D)m;
                JlinkUtils.regenerate(session, new JLDrawing.RegenerateDrawing(m2d), false, NitroConstants.DEBUG_REGEN_KEY);
            }
            CallWindow win = getSession().getModelWindow(m);
            if (win!=null) {
            	long start = System.currentTimeMillis();
                win.refresh();
                DebugLogging.sendTimerMessage("jlink.Refresh", start, NitroConstants.DEBUG_REGEN_KEY);
            }
            
            return false;
        }
    }

    /**
     * An implenetation of ModelLooper which erases a list of models
     * @author Adam Andrews
     *
     */
    private static class EraseLooper extends ModelLooper {
        /**
         * Output field which indicates whether anything was erased in this call
         */
        public boolean erasedSomething = true;
        /**
         * Whether to also erase children of the model(s)
         */
        public boolean eraseChildren = true;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
            
            // loop until the list is empty or we've gone through the list without erasing anything
            if (m!=null) {
                if (currentName==null)
                    currentName = m.getFileName();
                try {
                    if (eraseChildren)
                        m.eraseWithDependencies();
                    else
                        m.erase();
                    erasedSomething = true;
                    //System.err.println("Erased "+currentName);
                    return true; // as soon as we erase anything, break out of the loop so we can get a new list
                } 
                catch (XToolkitInUse e) { // caused when erasing a model that's a dependency of another
                    //System.err.println("Failed to erase "+currentName+": "+e.getClass().getName());
                	if (getNamePattern()!=null || getNameList()!=null) {
	                    throw new JLIException("File '" + currentName + "' could not be erased because it was still in use");
                	}
                }
                //catch (XToolkitBadInputs e) {} // caused when erasing a model that's already been erased
            }
            return false;
            
        }
    }
    
    /**
     * An implementation of ModelItemLooper which returns a list of coordinate systems on a model
     * @author Adam Andrews
     *
     */
    private class AssembleLooper2 extends ModelItemLooper {
        /**
         * Output list of corodinate systems
         */
        List<ModelItemEntry> items = new Vector<ModelItemEntry>();

        /**
         * Constructor needed to initialize model item type
         */
        public AssembleLooper2() {
            setSearchType(ModelItemType.ITEM_COORD_SYS);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
            if (currentName==null)
                currentName = item.getName();
            ModelItemEntry entry = new ModelItemEntry();
            entry.name = item.getName();
            entry.item = item;
            items.add(entry);
        	return false;
        }
    }
    
    /**
     * Implementation of ModelItemLooper which loops over Simplified Reps and 
     * outputs a list of names
     * @author Adam Andrews
     *
     */
    private class ListSimpRepLooper extends ModelItemLooper {
        /**
         * The output list of dimension data
         */
        public Vector<String> output = null;
        
        /**
         * Default constructor which sets the search type.
         */
        public ListSimpRepLooper() {
            setSearchType(ModelItemType.ITEM_SIMPREP);
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	try {
	            if (currentName==null)
	                currentName = item.getName();
	            if (output==null)
	                output = new Vector<String>();

	         	output.add(item.getName());
        	}
        	catch (jxthrowable e) {
//        		e.printStackTrace();
        		System.err.println("Error looping through Simplified Reps: " + e.getMessage());
        	}
        	return false;
        }
    }

    /**
     * Implentation of java.lang.Comparable which allows model items to be sorted by name
     * @author Adam Andrews
     *
     */
    private class ModelItemEntry implements Comparable<ModelItemEntry> {
        /**
         * Name of thd model item object, stored separately to save on JNI calls
         */
        public String name;
        /**
         * The model item object
         */
        public CallModelItem item;
        
        /* (non-Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(ModelItemEntry arg0) {
            String argname = ((ModelItemEntry)arg0).name;
            return name.compareTo(argname);
        }
    }
    
    /**
     * Implementation of Regenerator which will regenerate a Model
     * @author Adam Andrews
     *
     */
    private static class RegenerateModel implements JlinkUtils.Regenerator {

    	/**
    	 * The model to be regenerated
    	 */
    	private CallSolid solid;
    	/**
    	 * The regenerate instructions
    	 */
    	private CallRegenInstructions reginst;
    	
    	/**
    	 * Default constructor
    	 * @param solid The model to be regenerated
    	 * @param reginst The regenerate instructions
    	 */
    	public RegenerateModel(CallSolid solid, CallRegenInstructions reginst) {
    		this.solid = solid;
    		this.reginst = reginst;
    	}
    	
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.jlink.impl.JlinkUtils.Regenerator#regenerate()
		 */
		@Override
		public void regenerate() throws jxthrowable {
			solid.regenerate(reginst);
		}
    	
    }

    /**
     * Implementation of Regenerator which will execute feature operations.  
	 * This was set up as a Regenerator because a regenerate is forced.
     * @author Adam Andrews
     *
     */
    private static class RegenerateFeatureOps implements JlinkUtils.Regenerator {

    	/**
    	 * The model containing the features
    	 */
    	private CallSolid solid;
    	/**
    	 * The feature operations to execute
    	 */
    	private CallFeatureOperations featOps;
    	
    	/**
    	 * Default constructor
    	 * @param solid The model containing the features
    	 * @param featOps The list of feature operations to execute
    	 */
    	public RegenerateFeatureOps(CallSolid solid, CallFeatureOperations featOps) {
    		this.solid = solid;
    		this.featOps = featOps;
    	}
    	
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.jlink.impl.JlinkUtils.Regenerator#regenerate()
		 */
		@Override
		public void regenerate() throws jxthrowable {
            CallRegenInstructions inst = CallRegenInstructions.create(Boolean.FALSE, null, null); 
            solid.executeFeatureOps(featOps, inst);
		}
    	
    }

    /**
     * An implementation of MaterialLooper which collects a list of material data
     * @author Adam Andrews
     *
     */
    private static class ListMaterialLooper extends MaterialLooper {
        /**
         * The output list of feature data
         */
        public Vector<String> output = null;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.MaterialLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.part.CallMaterial)
         */
        public void loopAction(CallMaterial matl) throws JLIException, jxthrowable {
            if (currentName==null)
            	currentName = matl.getName();
            
            if (output==null)
                output = new Vector<String>();
            output.add(currentName);
        }
    }

    /**
     * An implementation of ModelLooper which loads a material into a list of models
     * @author Adam Andrews
     *
     */
    private static class LoadMaterialLooper extends ModelLooper {
        /**
         * An output list of model names
         */
        public List<String> output = null;
    	
    	/**
    	 * Name of the material to load
    	 */
    	public String materialName;

    	/**
    	 * Whether to set the material to be current
    	 */
    	public boolean setCurrent;

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {

        	if (!(m instanceof CallPart))
        		return false;
        	CallPart p = (CallPart)m;
        	
        	CallMaterial matl = null;
        	
        	matl = p.getMaterial(materialName);
        	if (matl==null) {
	    		try {
	    			matl = p.retrieveMaterial(materialName);
	    		}
	    		catch (XToolkitNotFound e) {
	    			throw new JLIException("Material file not found: "+materialName);
	    		}
        	}
    		if (setCurrent)
    			p.setCurrentMaterial(matl);

        	String name = null;
            try {
                name = m.getFileName();
            }
            catch (XToolkitCantOpen e) {
            	JlinkUtils.resolveGenerics(getSession(), m);
                name = m.getFileName();
            }   
            if (currentName==null)
            	currentName = name;
            
            if (output==null)
                output = new Vector<String>();
            output.add(currentName);

    		return false;
        }
    }

    /**
     * An implementation of ModelLooper which checks whether a material 
     * has been loaded into all parts in a list
     * @author Adam Andrews
     *
     */
    private static class HasMaterialLooper extends ModelLooper {
    	
    	/**
    	 * Name of the material to check
    	 */
    	public String materialName;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {

        	if (!(m instanceof CallPart))
        		return false;
        	CallPart p = (CallPart)m;

	        CallMaterial matl = p.getMaterial(materialName);
	        if (matl==null)
	        	throw new JLIException("Material "+materialName+" has not been loaded on file: "+m.getFileName());

    		return false;
        }
    }

    /**
     * An implementation of ModelLooper which sets the current material for a list of models
     * @author Adam Andrews
     *
     */
    private static class SetCurrentMaterialLooper extends ModelLooper {
        /**
         * An output list of model names
         */
        public List<String> output = null;
    	
    	/**
    	 * Name of the material to load
    	 */
    	public String materialName;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {

        	if (!(m instanceof CallPart))
        		return false;
        	CallPart p = (CallPart)m;
        	CallMaterial matl = null;
        	if (materialName!=null) {
	    		try {
	    			matl = p.getMaterial(materialName);
	    		}
	    		catch (XToolkitNotFound e) {
	    			throw new JLIException("Material file not found: "+materialName);
	    		}
        	}
        	p.setCurrentMaterial(matl);

        	String name = null;
            try {
                name = m.getFileName();
            }
            catch (XToolkitCantOpen e) {
            	JlinkUtils.resolveGenerics(getSession(), m);
                name = m.getFileName();
            }   
            if (currentName==null)
            	currentName = name;
            
            if (output==null)
                output = new Vector<String>();
            output.add(currentName);

    		return false;
        }
    }

    /**
     * An implementation of ModelLooper which lists the materials for a list of models
     * @author Adam Andrews
     *
     */
    private static class ListModelMaterialLooper extends ModelLooper {
        /**
         * An output list of model and material names
         */
        public List<ListMaterialResults> output = null;
    	
    	/**
    	 * Name of the material to load
    	 */
    	public String materialName;
    	
    	/**
    	 * Whether to include parts which don't have any matching materials
    	 */
    	public boolean includeNonMatchingParts = false;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {

        	if (!(m instanceof CallPart))
        		return false;
        	CallPart p = (CallPart)m;
        	
            ListMaterialLooper looper = new ListMaterialLooper();
            looper.setNamePattern(materialName);
            looper.loop(p);
            
            if (looper.output==null && !includeNonMatchingParts)
            	return false;

        	String name = null;
            try {
                name = m.getFileName();
            }
            catch (XToolkitCantOpen e) {
            	JlinkUtils.resolveGenerics(getSession(), m);
                name = m.getFileName();
            }   
            if (currentName==null)
            	currentName = name;
            
            if (output==null)
                output = new Vector<ListMaterialResults>();
            
            if (looper.output!=null) {
            	ListMaterialResults res = null;
            	for (String matl : looper.output){
                	res = new ListMaterialResults();
                	res.setFilename(name);
                	res.setMaterialName(matl);
                	output.add(res);
            	}
            }
            else {
            	ListMaterialResults res = new ListMaterialResults();
            	res.setFilename(name);
            	res.setMaterialName(null);
            	output.add(res);
            }

    		return false;
        }
    }

    /**
     * An implementation of ModelLooper which gets the current material for a list of models
     * @author Adam Andrews
     *
     */
    private static class GetCurrentMaterialLooper extends ModelLooper {
        /**
         * An output list of model and material names
         */
        public List<ListMaterialResults> output = null;
    	
    	/**
    	 * Whether to include parts which don't have any matching materials
    	 */
    	public boolean includeNonMatchingParts = false;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {

        	if (!(m instanceof CallPart))
        		return false;
        	CallPart p = (CallPart)m;
        	
	        CallMaterial matl = null;
	        try {
	        	matl = p.getCurrentMaterial();
	        }
	        catch (XToolkitNotFound e) {
	        	matl = null;
	        }
        	
            if (matl==null && !includeNonMatchingParts)
            	return false;

        	String name = null;
            try {
                name = m.getFileName();
            }
            catch (XToolkitCantOpen e) {
            	JlinkUtils.resolveGenerics(getSession(), m);
                name = m.getFileName();
            }   
            if (currentName==null)
            	currentName = name;
            
            if (output==null)
                output = new Vector<ListMaterialResults>();
            
        	ListMaterialResults res = new ListMaterialResults();
        	res.setFilename(name);
        	if (matl!=null)
        		res.setMaterialName(matl.getName());
        	output.add(res);

    		return false;
        }
    }

    /**
     * An implementation of ModelLooper which deletes a specific material from a list of models
     * @author Adam Andrews
     *
     */
    private static class DeleteMaterialLooper extends ModelLooper {
        /**
         * An output list of model names
         */
        public List<String> output = null;
    	
    	/**
    	 * Name of the material to delete
    	 */
    	public String materialName;
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
         */
        public boolean loopAction(CallModel m) throws JLIException,jxthrowable {

        	if (!(m instanceof CallPart))
        		return false;
        	CallPart p = (CallPart)m;
        	CallMaterial matl = null;
    		try {
    			matl = p.getMaterial(materialName);
    		}
    		catch (XToolkitNotFound e) {
    			// fall through
    		}
			// if the model doesn't have the material, just return
			if (matl==null)
				return false;
        	matl.delete();

        	String name = null;
            try {
                name = m.getFileName();
            }
            catch (XToolkitCantOpen e) {
            	JlinkUtils.resolveGenerics(getSession(), m);
                name = m.getFileName();
            }   
            if (currentName==null)
            	currentName = name;
            
            if (output==null)
                output = new Vector<String>();
            output.add(currentName);

    		return false;
        }
    }
}
