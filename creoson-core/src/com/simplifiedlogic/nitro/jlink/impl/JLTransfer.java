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

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcExceptions.XToolkitNotDisplayed;
import com.ptc.pfc.pfcExport.AssemblyConfiguration;
import com.ptc.pfc.pfcExport.ProductViewFormat;
import com.ptc.pfc.pfcModel.ExportType;
import com.ptc.pfc.pfcModel.ModelType;
import com.ptc.pfc.pfcWindow.DotsPerInch;
import com.ptc.pfc.pfcWindow.RasterDepth;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawing;
import com.simplifiedlogic.nitro.jlink.calls.export.CallGeometryFlags;
import com.simplifiedlogic.nitro.jlink.calls.mfg.CallMFG;
import com.simplifiedlogic.nitro.jlink.calls.model.CallExportInstructions;
import com.simplifiedlogic.nitro.jlink.calls.model.CallImportInstructions;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.window.CallRasterImageExportInstructions;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.ExportResults;
import com.simplifiedlogic.nitro.jlink.intf.IJLTransfer;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;

/**
 * @author Adam Andrews
 *
 */
public class JLTransfer implements IJLTransfer {

    public static final int TRAN_PROPROGRAM = 0;
    public static final int TRAN_BMP        = 1;
    public static final int TRAN_EPS        = 2;
    public static final int TRAN_JPEG       = 3;
    public static final int TRAN_TIFF       = 4;
    public static final int TRAN_POSTSCRIPT = 5;
    public static final int TRAN_PLOT       = 6;
    public static final int TRAN_STEP       = 7;
    public static final int TRAN_IGES       = 8;
    public static final int TRAN_VRML       = 9;
    public static final int TRAN_CATIA      = 10;
    public static final int TRAN_PV         = 11;
    public static final int TRAN_DXF		= 12;
    public static final int TRAN_PDF        = 13;
    
    public static final String OPTION_INTF3D_OUT_DEFAULT = "intf3d_out_default_option";

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportProgram(java.lang.String, java.lang.String)
	 */
	@Override
	public ExportResults exportProgram(String model, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportProgram(model, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportProgram(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportProgram(String model, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.export_program: " + model, NitroConstants.DEBUG_KEY);
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
	        m = JlinkUtils.getFile(session, model, true);
	        
	        int mtype = m.getType();
	        if (mtype==ModelType._MDL_MFG) {
	            m = JlinkUtils.getMfgModel(session, (CallMFG)m);
	        }
	
	        String filename = generateFilenameForModel(m, TRAN_PROPROGRAM);
	        String savedir = session.getCurrentDirectory();

	        NitroUtils.validateDirFile(savedir, filename, true);
	        
	        CallExportInstructions pxi = CallExportInstructions.createProgramExport();
	
	        m.export(filename, pxi);
	        
	        ExportResults result = new ExportResults();
	        result.setDirname(savedir);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.export_program,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportSTEP(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public ExportResults exportSTEP(String model, String filename, String dirname, String geomType, boolean advanced, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportSTEP(model, filename, dirname, geomType, advanced, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportSTEP(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportSTEP(String model, String filename, String dirname, String geomType, boolean advanced, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.exportSTEP: " + model, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

		if (!advanced)
			validateGeomType(geomType);

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();

	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	        
	        CallModel m;
	        m = JlinkUtils.getFile(session, model, true);

	        if (filename==null)
	        	filename = generateFilenameForModel(m, TRAN_STEP);
	        else
	        	filename = NitroUtils.setFileExtension(filename, EXT_STEP);

	        JlinkUtils.validateFilename(filename);

	        String olddir = session.getCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        NitroUtils.validateDirFile(dirname, filename, true);
	        
	        if (!advanced) {
		        CallGeometryFlags geomFlags = CallGeometryFlags.create();
		        //if (session.IsGeometryRepSupported(ExportType.EXPORT_STEP, ))
		        resolveGeomFlags(session, geomFlags, geomType);
	
		        AssemblyConfiguration assemConfig = null;
		        if (session.isConfigurationSupported(ExportType.EXPORT_STEP, AssemblyConfiguration.EXPORT_ASM_SINGLE_FILE))
		            assemConfig = AssemblyConfiguration.EXPORT_ASM_SINGLE_FILE;
		        else if (session.isConfigurationSupported(ExportType.EXPORT_STEP, AssemblyConfiguration.EXPORT_ASM_FLAT_FILE))
		            assemConfig = AssemblyConfiguration.EXPORT_ASM_FLAT_FILE;
		        else if (session.isConfigurationSupported(ExportType.EXPORT_STEP, AssemblyConfiguration.EXPORT_ASM_ASSEMBLY_PARTS))
		            assemConfig = AssemblyConfiguration.EXPORT_ASM_ASSEMBLY_PARTS;
		        else if (session.isConfigurationSupported(ExportType.EXPORT_STEP, AssemblyConfiguration.EXPORT_ASM_MULTI_FILES))
		            assemConfig = AssemblyConfiguration.EXPORT_ASM_MULTI_FILES;
		        if (assemConfig==null)
		            throw new JLIException("Export configuration not supported");
		        
		        CallExportInstructions pxi = CallExportInstructions.createSTEP3DExport(assemConfig, geomFlags);
	
		        if (dirname != null && !dirname.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, dirname);
		            try {
		            	m.export(filename, pxi);
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
		        	m.export(filename, pxi);
		        }
	        }
	        else {
		        if (dirname != null && !dirname.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, dirname);
		            try {
		            	exportAdvanced(session, m, filename, ExportType.EXPORT_STEP, "export_profiles_step");
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
	            	exportAdvanced(session, m, filename, ExportType.EXPORT_STEP, "export_profiles_step");
		        }
	        }

	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.exportSTEP,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportIGES(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public ExportResults exportIGES(String model, String filename, String dirname, String geomType, boolean advanced, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportIGES(model, filename, dirname, geomType, advanced, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportIGES(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportIGES(String model, String filename, String dirname, String geomType, boolean advanced, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.exportIGES: " + model, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

		if (!advanced)
			validateGeomType(geomType);

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();

	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	        
	        CallModel m;
	        m = JlinkUtils.getFile(session, model, true);

	        if (filename==null)
	        	filename = generateFilenameForModel(m, TRAN_IGES);
	        else
	        	filename = NitroUtils.setFileExtension(filename, EXT_IGES);

	        JlinkUtils.validateFilename(filename);

	        String olddir = session.getCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        NitroUtils.validateDirFile(dirname, filename, true);
	        
	        if (!advanced) {
		        CallGeometryFlags geomFlags = CallGeometryFlags.create();
		        //if (session.IsGeometryRepSupported(ExportType.EXPORT_IGES, ))
		        resolveGeomFlags(session, geomFlags, geomType);
	
		        AssemblyConfiguration assemConfig = null;
	//	        if (session.IsConfigurationSupported(ExportType.EXPORT_IGES, AssemblyConfiguration.EXPORT_ASM_FLAT_FILE))
		                assemConfig = AssemblyConfiguration.EXPORT_ASM_FLAT_FILE;
	//	        else 
	//	            if (session.IsConfigurationSupported(ExportType.EXPORT_IGES, AssemblyConfiguration.EXPORT_ASM_ASSEMBLY_PARTS))
	//	                assemConfig = AssemblyConfiguration.EXPORT_ASM_ASSEMBLY_PARTS;
	//	        else 
	//	            if (session.IsConfigurationSupported(ExportType.EXPORT_IGES, AssemblyConfiguration.EXPORT_ASM_MULTI_FILES))
	//	                assemConfig = AssemblyConfiguration.EXPORT_ASM_MULTI_FILES;
		        if (assemConfig==null)
		            throw new JLIException("Export configuration not supported");
		        
		        CallExportInstructions pxi = CallExportInstructions.createIGES3DExport(assemConfig, geomFlags);
	
		        if (dirname != null && !dirname.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, dirname);
		            try {
		            	m.export(filename, pxi);
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
		        	m.export(filename, pxi);
		        }
	        }	        
	        else {
		        if (dirname != null && !dirname.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, dirname);
		            try {
		            	exportAdvanced(session, m, filename, ExportType.EXPORT_IGES_3D, "export_profiles_iges");
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
	            	exportAdvanced(session, m, filename, ExportType.EXPORT_IGES_3D, "export_profiles_iges");
		        }
	        }

	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.exportIGES,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportVRML(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ExportResults exportVRML(String model, String filename, String dirname, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportVRML(model, filename, dirname, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportVRML(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportVRML(String model, String filename, String dirname, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.exportVRML: " + model, NitroConstants.DEBUG_KEY);
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
	        m = JlinkUtils.getFile(session, model, true);

	        if (filename==null)
	        	filename = generateFilenameForModel(m, TRAN_VRML);
	        else
	        	filename = NitroUtils.setFileExtension(filename, EXT_VRML);

	        JlinkUtils.validateFilename(filename);

	        String olddir = session.getCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        NitroUtils.validateDirFile(dirname, filename, true);

	        CallExportInstructions pxi = CallExportInstructions.createVRMLExport(dirname);

	        if (dirname != null && !dirname.equals(olddir)) {
	            JlinkUtils.changeDirectory(session, dirname);
	            try {
	            	m.export(filename, pxi);
	            }
	            finally {
	            	JlinkUtils.changeDirectory(session, olddir);
	            }
	        }
	        else {
	        	m.export(filename, pxi);
	        }
	        
	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.exportVRML,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportCATIA(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ExportResults exportCATIA(String model, String filename, String dirname, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportCATIA(model, filename, dirname, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportCATIA(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportCATIA(String model, String filename, String dirname, AbstractJLISession sess)
			throws JLIException {
		// this was disabled due to the syntax for CATIA export changing between WF4 and Creo.
		
/*
		DebugLogging.sendDebugMessage("interface.exportCATIA: " + model, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

		validateGeomType(geomType);

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        if (sess.isNoConnect())
	            return null;

	        JLGlobal.loadLibrary();

	        Session session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	        
	        Model m;
	        m = JlinkUtils.getFile(session, model, true);

	        if (filename==null)
	        	filename = generateFilenameForModel(m, TRAN_CATIA);
	        else
	        	filename = NitroUtils.setFileExtension(filename, EXT_CATIA);

	        JlinkUtils.validateFilename(filename);

	        String olddir = session.GetCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        NitroUtils.validateDirFile(dirname, filename, true);
	        
	        GeometryFlags geomFlags = pfcExport.GeometryFlags_Create();
	        //if (session.IsGeometryRepSupported(ExportType.EXPORT_CATIA, ))
	        resolveGeomFlags(session, geomFlags, geomType);

	        AssemblyConfiguration assemConfig = null;
	        if (session.IsConfigurationSupported(ExportType.EXPORT_CATIA, AssemblyConfiguration.EXPORT_ASM_SINGLE_FILE))
	            assemConfig = AssemblyConfiguration.EXPORT_ASM_SINGLE_FILE;
	        else if (session.IsConfigurationSupported(ExportType.EXPORT_CATIA, AssemblyConfiguration.EXPORT_ASM_FLAT_FILE))
	            assemConfig = AssemblyConfiguration.EXPORT_ASM_FLAT_FILE;
	        else if (session.IsConfigurationSupported(ExportType.EXPORT_CATIA, AssemblyConfiguration.EXPORT_ASM_ASSEMBLY_PARTS))
	            assemConfig = AssemblyConfiguration.EXPORT_ASM_ASSEMBLY_PARTS;
	        else if (session.IsConfigurationSupported(ExportType.EXPORT_CATIA, AssemblyConfiguration.EXPORT_ASM_MULTI_FILES))
	            assemConfig = AssemblyConfiguration.EXPORT_ASM_MULTI_FILES;
	        if (assemConfig==null)
	            throw new JLIException("Export configuration not supported");
	        
	        CATIA3DExportInstructions pxi = pfcExport.CATIA3DExportInstructions_Create(assemConfig, geomFlags);

	        if (dirname != null && !dirname.equals(olddir)) {
	            JlinkUtils.changeDirectory(session, dirname);
	            try {
	            	m.Export(filename, pxi);
	            }
	            finally {
	            	JlinkUtils.changeDirectory(session, olddir);
	            }
	        }
	        else {
	        	m.Export(filename, pxi);
	        }
	        
	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.exportCATIA,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
*/
		throw new JLIException("CATIA Export not supported");
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportPV(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ExportResults exportPV(String model, String filename, String dirname, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportPV(model, filename, dirname, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportPV(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportPV(String model, String filename, String dirname, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.exportPV: " + model, NitroConstants.DEBUG_KEY);
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
	        m = JlinkUtils.getFile(session, model, true);

	        if (filename==null)
	        	filename = generateFilenameForModel(m, TRAN_PV);
	        else
	        	filename = NitroUtils.setFileExtension(filename, EXT_PV);

	        JlinkUtils.validateFilename(filename);

	        String olddir = session.getCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        NitroUtils.validateDirFile(dirname, filename, true);
	        
	        CallExportInstructions pxi = CallExportInstructions.createProductViewExport(ProductViewFormat.PV_FORMAT_PVZ);

	        if (dirname != null && !dirname.equals(olddir)) {
	            JlinkUtils.changeDirectory(session, dirname);
	            try {
	            	m.export(filename, pxi);
	            }
	            finally {
	            	JlinkUtils.changeDirectory(session, olddir);
	            }
	        }
	        else {
	        	m.export(filename, pxi);
	        }
	        
	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.exportPV,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportDXF(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public ExportResults exportDXF(String model, String filename, String dirname, boolean advanced, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportDXF(model, filename, dirname, advanced, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportDXF(java.lang.String, java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportDXF(String model, String filename, String dirname, boolean advanced, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.exportDXF: " + model, NitroConstants.DEBUG_KEY);
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
	        m = JlinkUtils.getFile(session, model, true);

	        if (m==null)
	            throw new JLIException("Could not find model to export");
	        if (!(m instanceof CallDrawing))
	            throw new JLIException("DXF Export only allowed on drawings");
	        
	        if (filename==null)
	        	filename = generateFilenameForModel(m, TRAN_DXF);
	        else
	        	filename = NitroUtils.setFileExtension(filename, EXT_DXF);

	        JlinkUtils.validateFilename(filename);

	        String olddir = session.getCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        NitroUtils.validateDirFile(dirname, filename, true);
	        
	        if (!advanced) {
		        CallExportInstructions pxi = CallExportInstructions.createDXFExport();

		        if (dirname != null && !dirname.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, dirname);
		            try {
		            	m.export(filename, pxi);
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
		        	m.export(filename, pxi);
		        }
	        }
	        else {
		        if (dirname != null && !dirname.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, dirname);
		            try {
		            	exportAdvanced(session, m, filename, ExportType.EXPORT_DXF, "export_profiles_dxf");
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
	            	exportAdvanced(session, m, filename, ExportType.EXPORT_DXF, "export_profiles_dxf");
		        }
	        }

	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.exportDXF,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#plot(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ExportResults plot(String model, String dirname, String driver, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return plot(model, dirname, driver, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#plot(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults plot(String model, String dirname, String driver, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.plot: " + model, NitroConstants.DEBUG_KEY);
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
	        
	        String olddir = session.getCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        if (driver==null)
	        	driver = TYPE_POSTSCRIPT;

	        CallModel m;
	        if (model!=null)
	        	m = session.getModelFromFileName(model);
	        else
	            m = session.getCurrentModel();
	        
	        if (m==null)
	            throw new JLIException("Could not find model to export");
	        
	        String filename = m.getFileName();

	        String ext;
	        if (TYPE_POSTSCRIPT.equalsIgnoreCase(driver)) {
	            ext = EXT_POSTSCRIPT;
	            // ext = EXT_PLOT;
	        }
	        else if (TYPE_JPEG.equalsIgnoreCase(driver)) {
	            ext = EXT_JPEG;
	        }
	        else if (TYPE_TIFF.equalsIgnoreCase(driver)) {
	            ext = EXT_TIFF;
	        }
	        else {
	            throw new JLIException("Invalid driver type");
	        }
	        filename = NitroUtils.setFileExtension(filename, ext);

	        NitroUtils.validateDirFile(dirname, filename, true);
	            
//	        PlotInstructions pxi = pfcModel.PlotInstructions_Create("HP LaserJet 1200 Series PS (MS)");
	        CallExportInstructions pxi = CallExportInstructions.createPlotExport(driver);

	        if (dirname != null && !dirname.equals(olddir)) {
	            JlinkUtils.changeDirectory(session, dirname);
	            try {
	            	m.export(filename, pxi);
	            }
	            finally {
	            	JlinkUtils.changeDirectory(session, olddir);
	            }
	        }
	        else {
	        	m.export(filename, pxi);
	        }

	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.plot,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#mapkey(java.lang.String, java.lang.String)
	 */
	@Override
	public void mapkey(String script, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        mapkey(script, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#mapkey(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void mapkey(String script, AbstractJLISession sess)
			throws JLIException {

        // TODO: support filename/dirname parameters as well

		DebugLogging.sendDebugMessage("interface.mapkey", NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (script==null || script.trim().length()==0)
    		throw new JLIException("No script parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();

	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        // strip out newlines (can't do a replace, because creo doesn't like a space where the newline is)
	        StringBuffer buf = new StringBuffer();
	        int len = script.length();
	        for (int i=0; i<len; i++) {
	        	char c = script.charAt(i);
	        	if (c!='\n')
	        		buf.append(c);
	        }
	        script = buf.toString();
	        
	        session.runMacro(script);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.mapkey", start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#importProgram(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String importProgram(String dirname, String filename, String model, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return importProgram(dirname, filename, model, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#importProgram(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String importProgram(String dirname, String filename, String model, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.import_program: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();

    	if (model!=null && filename!=null) 
            throw new JLIException("Cannot specify both model and input file");

    	try {
	        JLGlobal.loadLibrary();

	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	        
            dirname = JlinkUtils.resolveRelativePath(session, dirname);

            String savedir = session.getCurrentDirectory();
            
            if (dirname == null) {
                dirname = savedir;
            }

            CallModel m;
            if (filename == null) {
                if (model==null) {
                    m = session.getCurrentModel();
                    if (m==null)
                        throw new JLIException("No current model");
                }
                else {
                    m = session.getModelFromFileName(model);
                    if (m==null)
                        throw new JLIException("No model found for name " + model);
                }

                int mtype = m.getType();
                if (mtype==ModelType._MDL_MFG) {
                    m = JlinkUtils.getMfgModel(session, (CallMFG)m);
                }
                filename = generateFilenameForModel(m, TRAN_PROPROGRAM);
                model = m.getFileName();
            }
            else {
                model = generateModelNameForFile(filename);
                m = session.getModelFromFileName(model);
                if (m==null)
                    throw new JLIException("No model found for file " + model);

                int mtype = m.getType();
                if (mtype==ModelType._MDL_MFG) {
                    m = JlinkUtils.getMfgModel(session, (CallMFG)m);
                }
            }

            NitroUtils.validateDirFile(dirname, filename, false);
            
            CallImportInstructions pii = CallImportInstructions.createProgramImport();

            if (dirname != null && !dirname.equals(savedir)) {
                JlinkUtils.changeDirectory(session, dirname);
                try {
                	m.importData(filename, pii);
                }
                finally {
                	JlinkUtils.changeDirectory(session, savedir);
                }
            }
            else {
                m.importData(filename, pii);
            }

            return model;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.import_program,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportBMP(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ExportResults exportBMP(String model, String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportBMP(model, filename, height, width, dpi, depth, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportBMP(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportBMP(String model, String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess)
			throws JLIException {

		return exportImage(model, filename, height, width, dpi, depth, TRAN_BMP, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportEPS(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ExportResults exportEPS(String model, String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportEPS(model, filename, height, width, dpi, depth, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportEPS(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportEPS(String model, String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess)
			throws JLIException {

		return exportImage(model, filename, height, width, dpi, depth, TRAN_EPS, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportJPEG(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ExportResults exportJPEG(String model, String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportJPEG(model, filename, height, width, dpi, depth, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportJPEG(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportJPEG(String model, String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess)
			throws JLIException {

		return exportImage(model, filename, height, width, dpi, depth, TRAN_JPEG, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportTIFF(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ExportResults exportTIFF(String model, String filename, Double height, Double width, Integer dpi, Integer depth, String sessionId)
			throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
        
        return exportTIFF(model, filename, height, width, dpi, depth, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportTIFF(java.lang.String, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.Integer, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportTIFF(String model, String filename, Double height, Double width, Integer dpi, Integer depth, AbstractJLISession sess)
			throws JLIException {

		return exportImage(model, filename, height, width, dpi, depth, TRAN_TIFF, sess);
	}
	
	private ExportResults exportImage(String model, String filename, Double height, Double width, Integer dpi, Integer depth, int type, AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("interface.export_image: " + model + " (" + type + ")", NitroConstants.DEBUG_KEY);
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

	        CallModel m = null;
	        if (model!=null)
	            m = session.getModelFromFileName(model);
	        else
	            m = session.getCurrentModel();
	        if (filename==null) {
	            if (m==null)
	                filename = "screen";
	            else
	                filename = m.getFileName();
	        }

	        JlinkUtils.validateFilename(filename);

	        CallWindow win;
	        if (m==null) win = session.getCurrentWindow();
	        else win = session.getModelWindow(m);
	        
	        if (win==null)
	            throw new JLIException("Could not find window to export");
	        
	        double rasterHeight = (height==null?7.5:height.doubleValue());
	        double rasterWidth = (width==null?10.0:width.doubleValue());
	        DotsPerInch dpiO;
	        RasterDepth depthO;
	        if (dpi!=null) {
	        	switch (dpi.intValue()) {
		    		case 100: dpiO = DotsPerInch.RASTERDPI_100; break;
		    		case 200: dpiO = DotsPerInch.RASTERDPI_200; break;
		    		case 300: dpiO = DotsPerInch.RASTERDPI_300; break;
		    		case 400: dpiO = DotsPerInch.RASTERDPI_400; break;
	        		default:
	                    throw new JLIException("Invalid value for dpi parameter: " + dpi);
	        	}
	        }
	        else
	        	dpiO = DotsPerInch.RASTERDPI_100;
	        if (depth!=null) {
	        	switch (depth.intValue()) {
		    		case 8: depthO = RasterDepth.RASTERDEPTH_8; break;
		    		case 24: depthO = RasterDepth.RASTERDEPTH_24; break;
		    		default:
		                throw new JLIException("Invalid value for depth parameter: " + depth);
		    	}
	        }
	        else
	            depthO = RasterDepth.RASTERDEPTH_24;
	        
	        CallRasterImageExportInstructions rxi = null;
	        String ext;
	        switch (type) {
	            case TRAN_BMP: 
	                rxi = CallRasterImageExportInstructions.createBitmapExport(rasterHeight, rasterWidth);
	                ext = EXT_BMP;
	                break;
	            case TRAN_EPS: 
	                rxi = CallRasterImageExportInstructions.createEPSExport(rasterHeight, rasterWidth); 
	                ext = EXT_EPS;
	                break;
	            case TRAN_JPEG: 
	                rxi = CallRasterImageExportInstructions.createJPEGExport(rasterHeight, rasterWidth); 
	                ext = EXT_JPEG;
	                break;
	            case TRAN_TIFF: 
	                rxi = CallRasterImageExportInstructions.createTIFFExport(rasterHeight, rasterWidth); 
	                ext = EXT_TIFF;
	                break;
	            default:
	                throw new JLIException("Unknown image type: " + type);
	        }
	        rxi.setImageDepth(depthO);
	        rxi.setDotsPerInch(dpiO);

	        String savedir = session.getCurrentDirectory();
	        
	        filename = NitroUtils.setFileExtension(filename, ext);
	        
	        NitroUtils.validateDirFile(savedir, filename, true);

	        win.exportRasterImage(filename, rxi);
	        
	        ExportResults result = new ExportResults();
	        result.setDirname(savedir);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.export_image,"+model+","+type, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportPDF(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.Double, java.lang.Double, java.lang.Integer, java.lang.String)
	 */
	@Override
	public ExportResults exportPDF(String model, String filename, String dirname, boolean export3D, Double height, Double width, Integer dpi, Boolean useDrawingSettings, String sessionId) throws JLIException {

		JLISession sess = JLISession.getSession(sessionId);
		
		return exportPDF(model, filename, dirname, export3D, height, width, dpi, useDrawingSettings, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLTransfer#exportPDF(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.Double, java.lang.Double, java.lang.Integer, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ExportResults exportPDF(String model, String filename, String dirname, boolean export3D, Double height, Double width, Integer dpi, Boolean useDrawingSettings, AbstractJLISession sess)
			throws JLIException {

		DebugLogging.sendDebugMessage("interface.export_pdf: " + model + (export3D?" (3D)":""), NitroConstants.DEBUG_KEY);
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
	        m = JlinkUtils.getFile(session, model, true);

	        if (filename==null)
	        	filename = generateFilenameForModel(m, TRAN_PDF);
	        else
	        	filename = NitroUtils.setFileExtension(filename, EXT_PDF);

	        JlinkUtils.validateFilename(filename);

	        String olddir = session.getCurrentDirectory();
	        if (dirname == null) {
	            dirname = olddir;
	        }
	        dirname = JlinkUtils.resolveRelativePath(session, dirname);

	        NitroUtils.validateDirFile(dirname, filename, true);

	        CallModel modelActive = null;
	        if (m!=null) {
	        	CallWindow modelWindow = session.getModelWindow(m);
		        if (modelWindow==null) {
		        	CallWindow winActive = session.getCurrentWindow();
			        modelActive = winActive.getModel();
			        
			        m.display();
		        }
	        }
	        
	        try {
	            Object[] inputs = new Object[] {
	            	m,
	            	filename,
	            	height,
	            	width,
	            	dpi,
	            	useDrawingSettings
	            };
	            String func;
	            if (export3D)
	            	func="com.simplifiedlogic.nitro.jshell.creo.ExportPDF3D";
	            else
	            	func="com.simplifiedlogic.nitro.jshell.creo.ExportPDF";
		        
		        if (dirname != null && !dirname.equals(olddir)) {
		            JlinkUtils.changeDirectory(session, dirname);
		            try {
		            	JlinkUtils.callCreoFunction(func, inputs);
		            }
		            finally {
		            	JlinkUtils.changeDirectory(session, olddir);
		            }
		        }
		        else {
	            	JlinkUtils.callCreoFunction(func, inputs);
		        }
	        }
	        finally {
	        	if (modelActive!=null)
	        		modelActive.display();
	        }
	        
	        ExportResults result = new ExportResults();
	        result.setDirname(dirname);
	        result.setFilename(filename);
	
	        return result;
    	}
    	catch (jxthrowable e) {
    		if (e instanceof XToolkitNotDisplayed)
    			throw new JLIException("Exported PDF model is not displayed");
    		else
    			throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("interface.export_pdf,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * Given a model name, generate a file name by replacing the file extension with the one appropriate for the export type
     * @param m The model whose file name needs to be generated
     * @param type The export type
     * @return The generated file name
     * @throws jxthrowable
     * @throws JLIException
     */
    private static String generateFilenameForModel(CallModel m, int type) throws jxthrowable,JLIException {
        if (m==null)
            return "";
        String filename = m.getFileName();
        int mtype = m.getType();
        String ext;
        if (type==TRAN_PROPROGRAM) {
            switch (mtype) {
                case ModelType._MDL_ASSEMBLY: ext = EXT_ASSEM_PROG; break;
                case ModelType._MDL_PART: ext = EXT_PART_PROG; break;
                case ModelType._MDL_MFG: ext = EXT_ASSEM_PROG; break;
                default:
                    throw new JLIException("Invalid model type: " + mtype);
            }
        }
        else if (type==TRAN_PLOT)
        	ext = EXT_PLOT;
        else if (type==TRAN_STEP)
            ext = EXT_STEP;
        else if (type==TRAN_IGES)
            ext = EXT_IGES;
        else if (type==TRAN_VRML)
            ext = EXT_VRML;
        else if (type==TRAN_CATIA)
            ext = EXT_CATIA;
        else if (type==TRAN_PV)
            ext = EXT_PV;
        else if (type==TRAN_DXF)
            ext = EXT_DXF;
        else if (type==TRAN_PDF)
            ext = EXT_PDF;
        else
            throw new JLIException("Invalid type to generateFilenameFromModel: " + type);

        return NitroUtils.setFileExtension(filename, ext);
    }
    
    /**
     * Convert a file name to a model name by stripping off the file extension and using it
     * to detect whether the model is an assembly or a part.  Only meant to be used with
     * Pro/Program export files.
     * @param filename The Pro/Program export file name
     * @return The generated model name
     * @throws JLIException
     */
    private static String generateModelNameForFile(String filename) throws JLIException {
        if (filename==null)
            return "";
        int pos = NitroUtils.findFileExtension(filename);
        String ext = null;
        if (pos>=0) {
        	String old_ext = filename.substring(pos);
        	if (old_ext.equalsIgnoreCase(EXT_ASSEM_PROG)) ext = EXT_ASSEMBLY;
            else if (old_ext.equalsIgnoreCase(EXT_PART_PROG)) ext = EXT_PART;
        }
        if (ext==null)
            throw new JLIException("Unknown file extension");
        
        return filename.substring(0, pos) + ext;
    }

    public static void validateGeomType(String geomType) throws JLIException {
        if (geomType==null ||
        	geomType.equals(GEOM_SOLIDS) ||
        	geomType.equals(GEOM_SURFACES) ||
        	geomType.equals(GEOM_WIREFRAME) ||
        	geomType.equals(GEOM_QUILTS) ||
        	geomType.equals(GEOM_WIREFRAME_SURFACES) ||
        	geomType.equals(GEOM_DEFAULT))
        	return;
        
        throw new JLIException("Invalid geometry type: " + geomType);
    	
    }

    public static void resolveGeomFlags(CallSession session, CallGeometryFlags geomFlags, String geomType) throws jxthrowable {
        if (geomType!=null && geomType.equals(GEOM_DEFAULT)) {
            geomType = session.getConfigOption(OPTION_INTF3D_OUT_DEFAULT);
            // worry about Creo's names for the options changing to be different from ours...
        }

        if (geomType==null)
        	geomFlags.setAsSolids(true);
        else if (geomType.equals(GEOM_SOLIDS))
        	geomFlags.setAsSolids(true);
        else if (geomType.equals(GEOM_SURFACES))
        	geomFlags.setAsSurfaces(true);
        else if (geomType.equals(GEOM_WIREFRAME))
        	geomFlags.setAsWireframe(true);
        else if (geomType.equals(GEOM_QUILTS))
        	geomFlags.setAsQuilts(true);
        else if (geomType.equals(GEOM_WIREFRAME_SURFACES)) {
        	geomFlags.setAsWireframe(true);
        	geomFlags.setAsSurfaces(true);
        }
    }

    public void exportAdvanced(CallSession session, CallModel model, String filename, ExportType exportType, String configOption) throws JLIException,jxthrowable {
    	String prof = session.getConfigOption(configOption);
//    	if (prof==null || prof.length()==0) {
//    		// error?
//    	}
        Object[] inputs = new Object[] {
            	model,
            	filename,
            	exportType,
            	prof
            };
        String func="com.simplifiedlogic.nitro.jshell.creo.ExportGeometry";
        
    	JlinkUtils.callCreoFunction(func, inputs);
    }

}
