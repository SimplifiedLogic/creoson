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
import java.util.Map;
import java.util.Vector;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcBase.CableDisplayStyle;
import com.ptc.pfc.pfcBase.DisplayStyle;
import com.ptc.pfc.pfcBase.TangentEdgeDisplayStyle;
import com.ptc.pfc.pfcDetail.AttachmentType;
import com.ptc.pfc.pfcDetail.DetailType;
import com.ptc.pfc.pfcDrawing.DrawingCreateOption;
import com.ptc.pfc.pfcExceptions.XToolkitCantOpen;
import com.ptc.pfc.pfcExceptions.XToolkitFound;
import com.ptc.pfc.pfcExceptions.XToolkitGeneralError;
import com.ptc.pfc.pfcExceptions.XToolkitInUse;
import com.ptc.pfc.pfcExceptions.XToolkitNotFound;
import com.ptc.pfc.pfcModel.PlotPaperSize;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.simplifiedlogic.nitro.jlink.calls.base.CallMatrix3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallOutline3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallTransform3D;
import com.simplifiedlogic.nitro.jlink.calls.base.CallVector3D;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallAttachment;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailItems;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailLeaders;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailSymbolDefInstructions;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailSymbolDefItem;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailSymbolInstInstructions;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailSymbolInstItem;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailVariantText;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailVariantTexts;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallFreeAttachment;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallOffsetAttachment;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallParametricAttachment;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallUnsupportedAttachment;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawing;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawingCreateOptions;
import com.simplifiedlogic.nitro.jlink.calls.drawingformat.CallDrawingFormat;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.calls.model2d.CallModel2D;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.sheet.CallSheetData;
import com.simplifiedlogic.nitro.jlink.calls.simprep.CallSimpRep;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallRegenInstructions;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.calls.view.CallView;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallGeneralViewCreateInstructions;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallProjectionViewCreateInstructions;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2Ds;
import com.simplifiedlogic.nitro.jlink.calls.view2d.CallViewDisplay;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.DrawingFormatData;
import com.simplifiedlogic.nitro.jlink.data.JLBox;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.data.SymbolDefData;
import com.simplifiedlogic.nitro.jlink.data.SymbolInstData;
import com.simplifiedlogic.nitro.jlink.data.ViewDetailData;
import com.simplifiedlogic.nitro.jlink.data.ViewDisplayData;
import com.simplifiedlogic.nitro.jlink.data.ViewScaleResults;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLDrawing;
import com.simplifiedlogic.nitro.jlink.intf.IJLTransfer;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLBoxMaker;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.JLPointMaker;
import com.simplifiedlogic.nitro.util.ModelLooper;
import com.simplifiedlogic.nitro.util.SheetLooper;
import com.simplifiedlogic.nitro.util.View2DLooper;

public class JLDrawing implements IJLDrawing {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#create(java.lang.String, java.lang.String, java.lang.String, double, boolean, boolean, boolean, java.lang.String)
	 */
	@Override
	public String create(
			String model, 
			String drawing, 
			String template, 
			double scale, 
			boolean display, 
	        boolean activate,
	        boolean newwin, 
			String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return create(model, drawing, template, scale, display, activate, newwin, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#create(java.lang.String, java.lang.String, java.lang.String, double, boolean, boolean, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String create(
			String model, 
			String drawing, 
			String template, 
			double scale, 
			boolean display, 
	        boolean activate,
	        boolean newwin, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.create: " + model, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (template==null || template.trim().length()==0)
    		throw new JLIException("No template name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, model, true);
	        if (m instanceof CallDrawing)
	        	throw new JLIException("Model is a drawing");
	        
	        CallModelDescriptor descr = m.getDescr();

            CallDrawingCreateOptions opts = CallDrawingCreateOptions.create();
            // don't use this option, because it will force the drawing to display 
            // in a new window and you may want it in the same window instead
//            if (display)
//            	opts.append(DrawingCreateOption.DRAWINGCREATE_DISPLAY_DRAWING);
            opts.append(DrawingCreateOption.DRAWINGCREATE_SHOW_ERROR_DIALOG);
            
            if (drawing==null)
            	drawing = m.getInstanceName();
            if (drawing.toLowerCase().endsWith(IJLTransfer.EXT_DRAWING))
            	drawing = drawing.substring(0, drawing.length()-IJLTransfer.EXT_DRAWING.length());
            
            CallDrawing drw = null;
            try {
            	drw = session.createDrawingFromTemplate(drawing, template, descr, opts);
            }
            catch (XToolkitFound e) {
            	throw new JLIException("Drawing named " + drawing + " already exists in session.");
            }
            catch (XToolkitNotFound e) {
            	throw new JLIException("Could not find template " + template + ".");
            }
            if (drw!=null) {
            	if (scale>0.0) {
            		drw.setSheetScale(1, scale, m);
            	}

            	if (drw.isCommonNameModifiable()) {
	        		int extpos = NitroUtils.findFileExtension(drw.getFileName());
	        		String commonName = extpos>=0 ? drw.getFileName().substring(0, extpos) : drw.getFileName();
	        		drw.setCommonName(commonName);
            	}
            	else
            		System.err.println("Common name for " + drw.getFileName() + " is not modifiable");

            	if (display) {
                    if (newwin) {
                    	CallWindow win = session.createModelWindow(drw);
                        if (win!=null) win.activate();
                    }
            		JlinkUtils.displayModel(session, drw, activate);
            	}

//            	drw.regenerate();
            	JlinkUtils.regenerate(session, sess, new RegenerateDrawing(drw), true);
            	return drw.getFileName();
            }
            
            return null;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.create,"+model, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listModels(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> listModels(String drawingName, String modelName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return listModels(drawingName, modelName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listModels(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> listModels(String drawingName, String modelName, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.list_models: " + drawingName, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        ListModelLooper looper = new ListModelLooper();
	        looper.setSession(session);
	        looper.setDrawing((CallDrawing)m);
	        looper.setNamePattern(modelName);
	        looper.loop();
	        
	        return looper.out;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.list_models,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#addModel(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addModel(String drawingName, String modelName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        addModel(drawingName, modelName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#addModel(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void addModel(String drawingName, String modelName, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.add_model: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (modelName==null || modelName.trim().length()==0)
    		throw new JLIException("No model name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        m = JlinkUtils.getFile(session, modelName, true);
	        
	        try {
	        	drw.addModel(m);
	        }
	        catch (XToolkitInUse e) {
	        	throw new JLIException("Model " + modelName + " already exists in the drawing");
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.add_model,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteModels(java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public void deleteModels(String drawingName, String modelName, boolean deleteViews, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        deleteModels(drawingName, modelName, deleteViews, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteModels(java.lang.String, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void deleteModels(String drawingName, String modelName, boolean deleteViews, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.delete_models: " + drawingName, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        DeleteModelLooper looper = new DeleteModelLooper();
	        looper.setSession(session);
	        looper.setDrawing((CallDrawing)m);
	        looper.setNamePattern(modelName);
	        looper.deleteViews = deleteViews;
	        looper.loop();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.delete_models,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getCurrentModel(java.lang.String, java.lang.String)
	 */
	@Override
	public String getCurrentModel(String filename, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getCurrentModel(filename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getCurrentModel(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String getCurrentModel(String filename, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_current_model: " + filename, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        CallModel m2 = drw.getCurrentSolid();
	        
	        if (m2==null)
	        	return null;
	        return m2.getFileName();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_current_model,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#setCurrentModel(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setCurrentModel(String drawingName, String modelName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        setCurrentModel(drawingName, modelName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#setCurrentModel(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void setCurrentModel(String drawingName, String modelName, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.set_current_model: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (modelName==null || modelName.trim().length()==0)
    		throw new JLIException("No model name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        m = JlinkUtils.getFile(session, modelName, true);
	        
        	drw.setCurrentSolid(m);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.set_current_model,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#regenerate(java.lang.String, java.lang.String)
	 */
	@Override
	public void regenerate(String filename, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        regenerate(filename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#regenerate(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void regenerate(String filename, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.regenerate: " + filename, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
        	JlinkUtils.regenerate(session, sess, new RegenerateDrawing(drw), true);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.regenerate,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#regenerateSheet(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void regenerateSheet(String filename, int sheet, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        regenerateSheet(filename, sheet, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#regenerateSheet(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void regenerateSheet(String filename, int sheet,
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.regenerate_sheet: " + filename, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;

	        RegenerateSheetLooper looper = new RegenerateSheetLooper();
	        looper.setDrawing(drw);
	        looper.setSheetno(sheet);
	        looper.loop();
//	        int numSheets = drw.getNumberOfSheets();
//
//	        if (sheet<0 || sheet>numSheets)
//	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"") + " or 0 for all");
//
//	        // doing this because regenerating a non-current (non-visible?) sheet causes a General Error, at least in Creo 3 M090
//	        int curSheet = drw.getCurrentSheetNumber();
//	        int lastSheet = curSheet;
//	        if (sheet==0) {
//	        	for (int i=0; i<numSheets; i++) {
//	        		lastSheet = i+1;
//		        	drw.setCurrentSheetNumber(lastSheet);
//	        		drw.regenerateSheet(lastSheet);
//	        	}
//	        }
//	        else {
//	        	lastSheet = sheet;
//	        	if (lastSheet!=curSheet)
//		        	drw.setCurrentSheetNumber(lastSheet);
//	        	drw.regenerateSheet(lastSheet);
//	        }
//	        
//	        if (lastSheet!=curSheet)
//	        	drw.setCurrentSheetNumber(curSheet);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.regenerate_sheet,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#selectSheet(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void selectSheet(String filename, int sheet, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        selectSheet(filename, sheet, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#selectSheet(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void selectSheet(String filename, int sheet,
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.select_sheet: " + filename + " sheet:" + sheet, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        int numSheets = drw.getNumberOfSheets();
	        
	        if (sheet<-1 || sheet==0 || sheet>numSheets)
	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"")); // + " or -1 for last sheet");

	        if (sheet==-1)
	        	drw.setCurrentSheetNumber(numSheets);
	        else
	        	drw.setCurrentSheetNumber(sheet);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.select_sheet,"+filename+","+sheet, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#addSheet(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void addSheet(String filename, int position, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        addSheet(filename, position, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#addSheet(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void addSheet(String filename, int position, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.add_sheet: " + filename, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;

	        int numSheets=0;
	        if (position>0) {
		        numSheets = drw.getNumberOfSheets();
		        
		        if (position>numSheets+1)
		        	throw new JLIException("Invalid sheet number, must be 1-" + (numSheets+1) + " or 0 for last sheet");
	        }

	        drw.addSheet();
	        
	        if (position>0 && position<=numSheets) {
	        	drw.reorderSheet(numSheets+1, position);
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.add_sheet,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteSheet(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void deleteSheet(String filename, int sheet, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        deleteSheet(filename, sheet, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteSheet(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void deleteSheet(String filename, int sheet,
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.delete_sheet: " + filename + " sheet:" + sheet, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        int numSheets = drw.getNumberOfSheets();
	        
	        if (sheet<-1 || sheet==0 || sheet>numSheets)
	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"")); // + " or -1 for last sheet");
	        
	        if (numSheets==1)
	        	throw new JLIException("Cannot delete the only sheet");

	        if (sheet==-1)
	        	drw.deleteSheet(numSheets);
	        else
	        	drw.deleteSheet(sheet);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.delete_sheet,"+filename+","+sheet, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getCurSheet(java.lang.String, java.lang.String)
	 */
	@Override
	public int getCurSheet(String filename, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getCurSheet(filename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getCurSheet(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public int getCurSheet(String filename, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_cur_sheet: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return -1;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        int sheet = drw.getCurrentSheetNumber();
	        return sheet;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_cur_sheet,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getNumSheets(java.lang.String, java.lang.String)
	 */
	@Override
	public int getNumSheets(String filename, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getNumSheets(filename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getNumSheets(java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public int getNumSheets(String filename, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_num_sheets: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return -1;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        int numSheets = drw.getNumberOfSheets();
	        return numSheets;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_num_sheets,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#scaleSheet(java.lang.String, int, double, java.lang.String, java.lang.String)
	 */
	@Override
	public void scaleSheet(String filename, int sheet, 
			double scale, String scaleFilename, 
			String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        scaleSheet(filename, sheet, scale, scaleFilename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#scaleSheet(java.lang.String, int, double, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void scaleSheet(String filename, int sheet, 
			double scale, String scaleFilename, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.scale_sheet: " + filename + " sheet:" + sheet, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (sheet==0)
    		throw new JLIException("No sheet number parameter given");
    	if (scale==0.0)
    		throw new JLIException("No scale parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        CallModel drawingModel = null;
	        if (scaleFilename!=null) {
		        drawingModel = JlinkUtils.getFile(session, scaleFilename, true);
	        }
	        
	        int numSheets = drw.getNumberOfSheets();
	        
	        if (sheet<1 || sheet>numSheets)
	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:""));

	        drw.setSheetScale(sheet, scale, drawingModel);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.scale_sheet,"+filename+","+sheet, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getSheetScale(java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public double getSheetScale(String filename, int sheet, 
			String scaleFilename, String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getSheetScale(filename, sheet, scaleFilename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getSheetScale(java.lang.String, int, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public double getSheetScale(String filename, int sheet, 
			String scaleFilename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_sheet_scale: " + filename + " sheet:" + sheet, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (sheet==0)
    		throw new JLIException("No sheet number parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return 1.0;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        CallModel drawingModel = null;
	        if (scaleFilename!=null) {
		        drawingModel = JlinkUtils.getFile(session, scaleFilename, true);
	        }
	        
	        int numSheets = drw.getNumberOfSheets();
	        
	        if (sheet<1 || sheet>numSheets)
	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:""));

	        double scale = drw.getSheetScale(sheet, drawingModel);
	        
	        return scale;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_sheet_scale,"+filename+","+sheet, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getSheetSize(java.lang.String, int, java.lang.String)
	 */
	@Override
	public String getSheetSize(String filename, int sheet, 
			String sessionId)
			throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getSheetSize(filename, sheet, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getSheetSize(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public String getSheetSize(String filename, int sheet, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_sheet_size: " + filename + " sheet:" + sheet, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (sheet==0)
    		throw new JLIException("No sheet number parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        int numSheets = drw.getNumberOfSheets();
	        
	        if (sheet<1 || sheet>numSheets)
	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:""));

	        CallSheetData sheetData = drw.getSheetData(sheet);
	        if (sheetData==null) {
	        	throw new JLIException("No sheet information found");
	        }
	        
	        PlotPaperSize paperSize = sheetData.getSheetSize();
	        
	        return paperSizeToString(paperSize);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_sheet_size,"+filename+","+sheet, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#createGeneralView(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, double, com.simplifiedlogic.nitro.jlink.data.ViewDisplayData, boolean, java.lang.String)
	 */
	@Override
	public void createGeneralView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String modelName,
			String modelViewName,
			JLPoint location,
			double scale,
			ViewDisplayData displayData,
			boolean exploded, 
			String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        createGeneralView(drawingName, newViewName, sheet, modelName, modelViewName, location, scale, displayData, exploded, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#createGeneralView(java.lang.String, java.lang.String, int, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, double, com.simplifiedlogic.nitro.jlink.data.ViewDisplayData, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void createGeneralView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String modelName,
			String modelViewName,
			JLPoint location,
			double scale,
			ViewDisplayData displayData,
			boolean exploded, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.create_gen_view: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (modelViewName==null || modelViewName.trim().length()==0)
    		throw new JLIException("No model view name parameter given");
    	if (location==null)
    		throw new JLIException("No location parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        if (newViewName!=null && drw.getViewByName(newViewName)!=null) {
	        	throw new JLIException("View already exists in drawing: " + newViewName);
	        }
	        
	        if (sheet==0)
	        	sheet = drw.getCurrentSheetNumber();
	        else {
		        int numSheets = drw.getNumberOfSheets();
		        if (sheet<0 || sheet>numSheets)
		        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"") + " or 0 for current sheet");
	        }

	        if (modelName!=null)
	        	m = JlinkUtils.getFile(session, drawingName, false);
	        else
	        	m = drw.getCurrentSolid();
	        CallView modelView = m.getView(modelViewName);
	        if (modelView==null) {
	        	throw new JLIException("View does not exist on model: " + modelViewName);
	        }
	        
        	CallPoint3D loc = JLPointMaker.create3D(location);
	        loc = drawingToScreenPoint(drw, sheet, loc);

	        CallTransform3D orient = modelView.getTransform();
	        CallMatrix3D matrix = orient.getMatrix();
	        normalizeMatrix(matrix);
	        orient.setMatrix(matrix);
	        
	        CallGeneralViewCreateInstructions instr = CallGeneralViewCreateInstructions.create(
	        		m, 
	        		sheet, 
	        		loc, 
	        		orient);
	        if (scale>0.0)
	        	instr.setScale(scale);
        	instr.setExploded(exploded);
	        
	        CallView2D view = drw.createView(instr);
	        if (displayData!=null)
	        	initViewDisplay(view, displayData);

	        if (newViewName!=null) {
	        	try {
			        // call external creo function
		            Object[] inputs = new Object[] {
		            	view,
		            	newViewName
		            };
		            String func="com.simplifiedlogic.nitro.jshell.creo.RenameDrawingView";
			        
		        	JlinkUtils.callCreoFunction(func, inputs);
	        	}
	        	catch (Exception e) {
	        		// ignore error if function not available
	        	}
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.create_gen_view,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#createProjectionView(java.lang.String, java.lang.String, int, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, com.simplifiedlogic.nitro.jlink.data.ViewDisplayData, boolean, java.lang.String)
	 */
	@Override
	public void createProjectionView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String parentViewName,
			JLPoint location,
			ViewDisplayData displayData,
			boolean exploded, 
			String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        createProjectionView(drawingName, newViewName, sheet, parentViewName, location, displayData, exploded, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#createProjectionView(java.lang.String, java.lang.String, int, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, com.simplifiedlogic.nitro.jlink.data.ViewDisplayData, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void createProjectionView(
			String drawingName, 
			String newViewName, 
			int sheet,
			String parentViewName,
			JLPoint location,
			ViewDisplayData displayData,
			boolean exploded, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.create_proj_view: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (parentViewName==null || parentViewName.trim().length()==0)
    		throw new JLIException("No parent view name parameter given");
    	if (location==null)
    		throw new JLIException("No location parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        if (newViewName!=null && drw.getViewByName(newViewName)!=null) {
	        	throw new JLIException("View already exists in drawing: " + newViewName);
	        }

	        CallView2D parentView = drw.getViewByName(parentViewName);
	        if (parentView==null) {
	        	throw new JLIException("Parent view does not exist in drawing: " + parentViewName);
	        }
	        
	        if (sheet==0)
	        	sheet = drw.getCurrentSheetNumber();
	        else {
		        int numSheets = drw.getNumberOfSheets();
		        if (sheet<0 || sheet>numSheets)
		        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"") + " or 0 for current sheet");
	        }

	        JLPoint originParent = getViewOrigin(parentView);
	        CallVector3D vecPoint = CallVector3D.create();
	        vecPoint.set(0, location.getX());
	        vecPoint.set(1, location.getY());
	        vecPoint.set(2, location.getZ());
	        vecPoint = drawingToScreenPoint(drw, sheet, vecPoint);

	        CallPoint3D loc = CallPoint3D.create();
	        loc.set(0, vecPoint.get(0)+originParent.getX());
	        loc.set(1, vecPoint.get(1)+originParent.getY());
	        loc.set(2, vecPoint.get(2)+originParent.getZ());
	        
	        CallProjectionViewCreateInstructions instr = CallProjectionViewCreateInstructions.create(
	        		parentView, 
	        		loc);
        	instr.setExploded(exploded);
	        
	        CallView2D view = drw.createView(instr);
	        if (displayData!=null)
	        	initViewDisplay(view, displayData);
	        else
	        	copyViewDisplay(parentView, view);

	        if (newViewName!=null) {
	        	try {
			        // call external creo function
		            Object[] inputs = new Object[] {
		            	view,
		            	newViewName
		            };
		            String func="com.simplifiedlogic.nitro.jshell.creo.RenameDrawingView";
			        
		        	JlinkUtils.callCreoFunction(func, inputs);
	        	}
	        	catch (Exception e) {
	        		// ignore error if function not available
	        	}
	        }
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.create_proj_view,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listViews(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> listViews(String drawingName, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return listViews(drawingName, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listViews(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<String> listViews(String drawingName, String viewName, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.list_views: " + drawingName, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        ListViewLooper looper = new ListViewLooper();
	        looper.setDrawing((CallDrawing)m);
	        looper.setNamePattern(viewName);
	        looper.loop();
	        
	        return looper.out;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.list_views,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listViewDetails(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<ViewDetailData> listViewDetails(String drawingName, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return listViewDetails(drawingName, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listViewDetails(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public List<ViewDetailData> listViewDetails(String drawingName, String viewName, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.list_view_details: " + drawingName, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        ListViewDetailLooper looper = new ListViewDetailLooper();
	        looper.setDrawing((CallDrawing)m);
	        looper.setNamePattern(viewName);
	        looper.loop();
	        
	        return looper.out;
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.list_view_details,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getViewLoc(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JLPoint getViewLoc(String drawingName, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getViewLoc(drawingName, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getViewLoc(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public JLPoint getViewLoc(String drawingName, String viewName, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_view_loc: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (viewName==null || viewName.trim().length()==0)
    		throw new JLIException("No view name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        CallDrawing drw = (CallDrawing)m;
//	        CallView2D view = drw.getViewByName(viewName);
//	        if (view==null)
//	        	throw new JLIException("View not found in drawing: " + viewName);
	        
	        // need to loop even if there's a specific name, because the same name could be used on different sheets
	        ListViewObjectLooper looper = new ListViewObjectLooper();
	        looper.setDrawing(drw);
	        looper.setNamePattern(viewName);
	        looper.setSheetno(drw.getCurrentSheetNumber());
	        looper.loop();

	        if (looper.out==null || looper.out.size()==0) {
	        	throw new JLIException("View not found in current drawing sheet: " + viewName);
	        }
	        CallView2D view = looper.out.get(0);
	        
	        JLPoint origin = getViewOrigin(view);
	        CallPoint3D origin3D = JLPointMaker.create3D(origin);
	        
	        int sheetno = -1;
	        try {
	        	sheetno = view.getSheetNumber();
	        }
    		catch (XToolkitGeneralError e) {
    			// this error was thrown for a view that was Suppressed
    			throw new JLIException("Cannot get location, view is Erased");
    		}
	        origin3D = screenToDrawingPoint(drw, sheetno, origin3D);
	        
	        return JLPointMaker.create(origin3D);
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_view_loc,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#setViewLoc(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, boolean, java.lang.String)
	 */
	@Override
	public void setViewLoc(String drawingName, String viewName, JLPoint point, boolean relative, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        setViewLoc(drawingName, viewName, point, relative, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#setViewLoc(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void setViewLoc(String drawingName, String viewName, JLPoint point, boolean relative, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.set_view_loc: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (viewName==null || viewName.trim().length()==0)
    		throw new JLIException("No view name parameter given");
    	if (point==null)
    		throw new JLIException("No coordinates given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        CallDrawing drw = (CallDrawing)m;
//	        CallView2D view = drw.getViewByName(viewName);
//	        if (view==null)
//	        	throw new JLIException("View not found in drawing: " + viewName);

	        // need to loop even if there's a specific name, because the same name could be used on different sheets
	        ListViewObjectLooper looper = new ListViewObjectLooper();
	        looper.setDrawing(drw);
	        looper.setNamePattern(viewName);
	        looper.setSheetno(drw.getCurrentSheetNumber());
	        looper.loop();

	        if (looper.out==null || looper.out.size()==0) {
	        	throw new JLIException("View not found in current drawing sheet: " + viewName);
	        }
	        CallView2D view = looper.out.get(0);

	        int sheetno = -1;
	        try {
	        	sheetno = view.getSheetNumber();
	        }
    		catch (XToolkitGeneralError e) {
    			// this error was thrown for a view that was Suppressed
    			throw new JLIException("Cannot set location, view is Erased");
    		}
	        CallVector3D vector = null;
	        if (relative) {

		        // we're doing this because when PTC transforms a point, it takes
		        // into account the matrix's "origin"; when it transforms a vector,
		        // it doesn't.  When moving "relative", the point is really a vector,
		        // and converting it that way makes things come out right.
		        CallVector3D vecPoint = CallVector3D.create();
		        vecPoint.set(0, point.getX());
		        vecPoint.set(1, point.getY());
		        vecPoint.set(2, point.getZ());
		        vector = drawingToScreenPoint(drw, sheetno, vecPoint);
	        }
	        else {
	        	CallPoint3D point3D = JLPointMaker.create3D(point);
		        point3D = drawingToScreenPoint(drw, sheetno, point3D);
	        	JLPoint origin = getViewOrigin(view);
		        
		        vector = CallVector3D.create();
		        vector.set(0, point3D.get(0)-origin.getX());
		        vector.set(1, point3D.get(1)-origin.getY());
	        }
	        
	        view.translate(vector);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.set_view_loc,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteView(java.lang.String, java.lang.String, int, boolean, java.lang.String)
	 */
	@Override
	public void deleteView(String drawingName, String viewName, int sheetno, boolean deleteChildren, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        deleteView(drawingName, viewName, sheetno, deleteChildren, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteView(java.lang.String, java.lang.String, int, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void deleteView(String drawingName, String viewName, int sheetno, boolean deleteChildren, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.delete_view: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (viewName==null || viewName.trim().length()==0)
    		throw new JLIException("No view name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        CallDrawing drw = (CallDrawing)m;

	        // need to loop even if there's a specific name, because the same name could be used on different sheets
	        ListViewObjectLooper looper = new ListViewObjectLooper();
	        looper.setDrawing(drw);
	        looper.setNamePattern(viewName);
	        looper.setSheetno(sheetno);
	        looper.loop();

	        if (looper.out==null || looper.out.size()==0) {
	        	if (sheetno>0)
	        		throw new JLIException("View " + viewName + "does not exist on sheet " + sheetno);
	        	else
	        		throw new JLIException("View not found in drawing: " + viewName);
	        }
	        
	        for (CallView2D view : looper.out)
	        	view.delete(deleteChildren);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.delete_view,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#renameView(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void renameView(String drawingName, String viewName, String newViewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        renameView(drawingName, viewName, newViewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#renameView(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void renameView(String drawingName, String viewName, String newViewName, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.rename_view: " + drawingName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (viewName==null || viewName.trim().length()==0)
    		throw new JLIException("No view name parameter given");
    	if (newViewName==null || newViewName.trim().length()==0)
    		throw new JLIException("No new view name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, drawingName, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (drawingName==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + drawingName);
	        }
	        
	        CallDrawing drw = (CallDrawing)m;
//	        CallView2D view = drw.getViewByName(viewName);
//	        if (view==null)
//	        	throw new JLIException("View not found in drawing: " + viewName);

	        // need to loop even if there's a specific name, because the same name could be used on different sheets
	        ListViewObjectLooper looper = new ListViewObjectLooper();
	        looper.setDrawing(drw);
	        looper.setNamePattern(viewName);
	        looper.setSheetno(drw.getCurrentSheetNumber());
	        looper.loop();

	        if (looper.out==null || looper.out.size()==0) {
	        	throw new JLIException("View not found in drawing: " + viewName);
	        }
	        CallView2D view = looper.out.get(0);

	        // call external creo function
            Object[] inputs = new Object[] {
            	view,
            	newViewName
            };
            String func="com.simplifiedlogic.nitro.jshell.creo.RenameDrawingView";
	        
        	JlinkUtils.callCreoFunction(func, inputs);

        	//JlinkUtils.regenerate(session, new RegenerateDrawing(drw), true);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.rename_view,"+drawingName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#scaleView(java.lang.String, java.lang.String, double, java.lang.String)
	 */
	@Override
	public ViewScaleResults scaleView(String filename, String viewName, 
			double scale, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return scaleView(filename, viewName, scale, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#scaleView(java.lang.String, java.lang.String, double, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public ViewScaleResults scaleView(String filename, String viewName, 
			double scale, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.scale_view: " + filename + " sheet:" + viewName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (scale==0.0)
    		throw new JLIException("No scale parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        ScaleViewLooper looper = new ScaleViewLooper();
	        looper.setDrawing(drw);
	        looper.setNamePattern(viewName);
	        looper.setSheetno(drw.getCurrentSheetNumber());
	        looper.scale = scale;
	        looper.loop();
	        
	        ViewScaleResults result = new ViewScaleResults();
        	result.setFailedViews(looper.failedViews);
        	result.setSuccessViews(looper.successViews);
	        
	        return result;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.scale_view,"+filename+","+viewName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getViewScale(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public double getViewScale(String filename, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getViewScale(filename, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getViewScale(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public double getViewScale(String filename, String viewName, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_view_scale: " + filename + " sheet:" + viewName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (viewName==null)
    		throw new JLIException("No view name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return 1.0;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
//	        CallView2D view = drw.getViewByName(viewName);
//	        if (view==null)
//	        	throw new JLIException("View not found in drawing: " + viewName);
	        
	        // need to loop even if there's a specific name, because the same name could be used on different sheets
	        ListViewObjectLooper looper = new ListViewObjectLooper();
	        looper.setDrawing(drw);
	        looper.setNamePattern(viewName);
	        looper.setSheetno(drw.getCurrentSheetNumber());
	        looper.loop();

	        if (looper.out==null || looper.out.size()==0) {
	        	throw new JLIException("View not found in current drawing sheet: " + viewName);
	        }
	        CallView2D view = looper.out.get(0);
	        
	        double scale = view.getScale();
	        
	        return scale;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_view_scale,"+filename+","+viewName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getViewSheet(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int getViewSheet(String filename, String viewName, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);

        return getViewSheet(filename, viewName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getViewSheet(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public int getViewSheet(String filename, String viewName, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_view_sheet: " + filename + " sheet:" + viewName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (viewName==null)
    		throw new JLIException("No view name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return 0;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        CallView2D view = drw.getViewByName(viewName);
	        if (view==null)
	        	throw new JLIException("View not found in drawing: " + viewName);

	        try {
	        	return view.getSheetNumber();
	        }
    		catch (XToolkitGeneralError e) {
    			// this error was thrown for a view that was Suppressed
    			throw new JLIException("Cannot get sheet number, view is Erased");
    		}
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_view_sheet,"+filename+","+viewName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#viewBoundingBox(java.lang.String, java.lang.String, java.lang.String)
     */
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#viewBoundingBox(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public JLBox viewBoundingBox(String filename, String viewName, String sessionId) throws JLIException {
    	
        JLISession sess = JLISession.getSession(sessionId);
        
        return viewBoundingBox(filename, viewName, sess);
    }
    
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#viewBoundingBox(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#viewBoundingBox(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
     */
    @Override
    public JLBox viewBoundingBox(String filename, String viewName, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.view_bound_box: " + viewName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (viewName==null || viewName.trim().length()==0)
    		throw new JLIException("No view name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
	        
	        CallDrawing drw = (CallDrawing)m;
//	        CallView2D view = drw.getViewByName(viewName);
//	        if (view==null)
//	        	throw new JLIException("View not found in drawing: " + viewName);

	        // need to loop even if there's a specific name, because the same name could be used on different sheets
	        ListViewObjectLooper looper = new ListViewObjectLooper();
	        looper.setDrawing(drw);
	        looper.setNamePattern(viewName);
	        looper.setSheetno(drw.getCurrentSheetNumber());
	        looper.loop();

	        if (looper.out==null || looper.out.size()==0) {
	        	throw new JLIException("View not found in current drawing sheet: " + viewName);
	        }
	        CallView2D view = looper.out.get(0);

	        CallOutline3D outline = view.getOutline();

	        if (outline==null)
	        	throw new JLIException("No outline found for drawing view");
	        
	        int sheet = -1;
	        try {
	        	sheet = view.getSheetNumber();
	        }
    		catch (XToolkitGeneralError e) {
    			// this error was thrown for a view that was Suppressed
    			throw new JLIException("Cannot get bounding box, view is Erased");
    		}
	        CallPoint3D ptMin = screenToDrawingPoint(drw, sheet, outline.get(0));
	        CallPoint3D ptMax = screenToDrawingPoint(drw, sheet, outline.get(1));

	        return JLBoxMaker.create(ptMin, ptMax);
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.view_bound_box,"+viewName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#loadSymbolDef(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SymbolDefData loadSymbolDef(String filename, String symbolDir, String symbolFile, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return loadSymbolDef(filename, symbolDir, symbolFile, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#loadSymbolDef(java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public SymbolDefData loadSymbolDef(String filename, String symbolDir, String symbolFile, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.load_symbol_def: " + symbolFile, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (symbolFile==null || symbolFile.trim().length()==0)
    		throw new JLIException("No symbol file parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        int extpos = NitroUtils.findFileExtension(symbolFile);
	        if (extpos>=0)
	        	symbolFile = symbolFile.substring(0, extpos);

	    	CallDetailSymbolDefItem def = drw.retrieveSymbolDefinition(symbolFile, symbolDir, null, Boolean.TRUE);

	    	CallDetailSymbolDefInstructions instDef = def.getInstructions();

	    	SymbolDefData data = new SymbolDefData();
	    	data.setId(instDef.getId());
	    	data.setName(instDef.getName());

	    	return data;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.load_symbol_def,"+symbolFile, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#isSymbolDefLoaded(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
    public boolean isSymbolDefLoaded(String filename, String symbolFile, String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

        return isSymbolDefLoaded(filename, symbolFile, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#isSymbolDefLoaded(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public boolean isSymbolDefLoaded(String filename, String symbolFile, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.is_symbol_def_loaded: " + symbolFile, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (symbolFile==null || symbolFile.trim().length()==0)
    		throw new JLIException("No symbol file parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        String symbolName = getSymbolNameFromFile(symbolFile);
	        
	        CallDetailSymbolDefItem symbolDef = findSymbolDefItem(drw, symbolName);
		    return symbolDef!=null;

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.is_symbol_def_loaded,"+symbolFile, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteSymbolDef(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
    public void deleteSymbolDef(String filename, String symbolFile, String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

		deleteSymbolDef(filename, symbolFile, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteSymbolDef(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public void deleteSymbolDef(String filename, String symbolFile, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.delete_symbol_def: " + symbolFile, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (symbolFile==null || symbolFile.trim().length()==0)
    		throw new JLIException("No symbol file parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        int numSheets = drw.getNumberOfSheets();
	        
	        String symbolName = getSymbolNameFromFile(symbolFile);
	        
	    	for (int i=0; i<numSheets; i++) {
	    		drw.setCurrentSheetNumber(i+1);

		        CallDetailSymbolDefItem symbolDef = findSymbolDefItem(drw, symbolName);
		        if (symbolDef!=null) {
			        symbolDef.delete();
		        }

		        // regenerate sheet even if symbol not found; it may be needed to make old symbols go away
//		        drw.regenerateSheet(i);
	    	}

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.delete_symbol_def,"+symbolFile, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#createSymbol(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, java.util.Map, int, java.lang.String)
	 */
	@Override
	public void createSymbol(String filename, String symbolFile, JLPoint location, Map<String, Object> replaceValues, int sheet, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        createSymbol(filename, symbolFile, location, replaceValues, sheet, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#createSymbol(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, java.util.Map, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void createSymbol(String filename, String symbolFile, JLPoint location, Map<String, Object> replaceValues, int sheet, AbstractJLISession sess)
			throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.create_symbol: " + symbolFile, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (symbolFile==null || symbolFile.trim().length()==0)
    		throw new JLIException("No symbol file parameter given");
    	if (location==null)
    		throw new JLIException("No coordinates given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        String symbolName = getSymbolNameFromFile(symbolFile);

	        //drw.getDrawing().GetDetailItem(DetailType.DETAIL_SYM_DEFINITION, id)
	        CallDetailSymbolDefItem symbolDef = findSymbolDefItem(drw, symbolName);
	        if (symbolDef==null) {
	        	throw new JLIException("Symbol definition " + symbolFile + " has not been loaded");
	        }

        	CallPoint3D point3D = JLPointMaker.create3D(location);
	        point3D = drawingToScreenPoint(drw, sheet, point3D);

	    	CallDetailSymbolInstInstructions inst2 = CallDetailSymbolInstInstructions.create(symbolDef);    
	    	CallFreeAttachment position = CallFreeAttachment.create(point3D);

	    	CallDetailLeaders allAttachments = CallDetailLeaders.create();
			allAttachments.setItemAttachment(position);
	 
			inst2.setInstAttachment(allAttachments);

			if (replaceValues!=null && replaceValues.size()>0) {
				CallDetailVariantTexts texts = CallDetailVariantTexts.create();
				for (String key : replaceValues.keySet()) {
					Object val = replaceValues.get(key);
					if (val==null)
						val="";
					CallDetailVariantText text = CallDetailVariantText.create(key, val.toString());
					texts.append(text);
				}
				inst2.setTextValues(texts);
			}

			CreateSymbolLooper looper = new CreateSymbolLooper();
			looper.setDrawing(drw);
			looper.setSheetno(sheet);
			looper.inst = inst2;
			
			looper.loop();

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.create_symbol,"+symbolFile, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listSymbols(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
    public List<SymbolInstData> listSymbols(String filename, String symbolFile, int sheet, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return listSymbols(filename, symbolFile, sheet, sess);
    }

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#listSymbols(java.lang.String, java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public List<SymbolInstData> listSymbols(String filename, String symbolFile, int sheet, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.list_symbols: " + symbolFile, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        String symbolName = getSymbolNameFromFile(symbolFile);

			ListSymbolLooper looper = new ListSymbolLooper();
			looper.setDrawing(drw);
			looper.setSheetno(sheet);
			looper.symbolName = symbolName;
			
			looper.loop();

			if (looper.outvals==null)
				return new Vector<SymbolInstData>();
			else
				return looper.outvals;

    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.list_symbols,"+symbolFile, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteSymbolInst(java.lang.String, int, java.lang.String)
	 */
	@Override
    public void deleteSymbolInst(String filename, int symbolId, String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

		deleteSymbolInst(filename, symbolId, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#deleteSymbolInst(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public void deleteSymbolInst(String filename, int symbolId, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.delete_symbol_inst: " + symbolId, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (symbolId<0)
    		throw new JLIException("No symbol file parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
	        CallDrawing drw = (CallDrawing)m;
	        
	        CallModelItem item0 = drw.getItemById(ModelItemType.ITEM_DTL_SYM_INSTANCE, symbolId);
	        CallDetailSymbolInstItem item = (CallDetailSymbolInstItem)item0;
    		// this doesn't ACTUALLY delete the instance, it just removes it from the screen; 
    		// you get an error if you try to run this twice
	        if (item!=null)
	        	item.delete();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.delete_symbol_inst: " + symbolId, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getSheetFormat(java.lang.String, int, java.lang.String)
	 */
	@Override
	public DrawingFormatData getSheetFormat(String filename, int sheet, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        return getSheetFormat(filename, sheet, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#getSheetFormat(java.lang.String, int, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public DrawingFormatData getSheetFormat(String filename, int sheet, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.get_sheet_format: " + filename + " sheet:" + sheet, NitroConstants.DEBUG_KEY);
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

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        int numSheets = drw.getNumberOfSheets();
	        
	        if (sheet<-1 || sheet==0 || sheet>numSheets)
	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"")); // + " or -1 for last sheet");

	        CallDrawingFormat fmt = drw.getSheetFormat(sheet);
	        if (fmt==null)
	        	return null;
	        
	        DrawingFormatData out = new DrawingFormatData();
	        String name;
	        name = fmt.getCommonName();
	        if (name!=null && name.trim().length()>0)
	        	out.setCommonName(name);
	        name = fmt.getFullName();
	        if (name!=null && name.trim().length()>0)
	        	out.setFullName(name);
	        name = fmt.getFileName();
	        if (name!=null && name.trim().length()>0)
	        	out.setFileName(name);
	        
	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.get_sheet_format,"+filename+","+sheet, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#setSheetFormat(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void setSheetFormat(String filename, int sheet, String dirname, String formatFilename, String sessionId) throws JLIException {
        JLISession sess = JLISession.getSession(sessionId);
        
        setSheetFormat(filename, sheet, dirname, formatFilename, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLDrawing#setSheetFormat(java.lang.String, int, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
    public void setSheetFormat(String filename, int sheet, String dirname, String formatFilename, AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("drawing.set_sheet_format: " + filename + " sheet:" + sheet, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (formatFilename==null)
    		throw new JLIException("No format filename parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing))
	        	throw new JLIException("Model is not a drawing");
	        
	        CallDrawing drw = (CallDrawing)m;
	        
	        int numSheets = drw.getNumberOfSheets();
	        if (sheet<-1 || sheet==0 || sheet>numSheets)
	        	throw new JLIException("Invalid sheet number, must be 1" + (numSheets>1?"-"+numSheets:"")); // + " or -1 for last sheet");

	        formatFilename = NitroUtils.removeExtension(formatFilename);
	        formatFilename = formatFilename + ".frm";

	        boolean changedir=false;
	        String curdir = session.getCurrentDirectory();
            if (dirname!=null && !dirname.equals(curdir)) {
                JlinkUtils.changeDirectory(session, dirname);
	            changedir=true;
            }

            try {
	            CallModelDescriptor descr = CallModelDescriptor.createFromFileName(formatFilename);
	            // note that SetPath is ignored by RetrieveModel call...
	            // but it _is_ used by RetrieveModelWithOpts
	            descr.setPath(session.getCurrentDirectory());
	            CallDrawingFormat mFmt=null;
	            try {
	                mFmt = (CallDrawingFormat)session.retrieveModel(descr);
	            }
	            catch (jxthrowable jxe) {
	                throw new JLIException("Could not open file '" + formatFilename + "' in directory " + session.getCurrentDirectory(), jxe);
	            }
		        
	            if (mFmt==null)
	                throw new JLIException("Could not open file '" + formatFilename + "' in directory " + session.getCurrentDirectory());
	            
	            drw.setSheetFormat(sheet, mFmt, null, null);
	            drw.updateTables();
	            drw.regenerateSheet(sheet);
	        }
	        finally {
	            if (changedir && dirname!=null && !dirname.equals(curdir)) {
	                JlinkUtils.changeDirectory(session, curdir);
	            }
	        }
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("drawing.set_sheet_format,"+filename+","+sheet, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /**
     * Convert a Creo PlotPaperSize to a standard string representing the size
     * @param paperSize The Creo paper size
     * @return A string representing the size
     */
    public static String paperSizeToString(PlotPaperSize paperSize) {
    	int val = paperSize.getValue();
    	switch(val) {
    		case PlotPaperSize._ASIZEPLOT: return "A";
    		case PlotPaperSize._BSIZEPLOT: return "B";
    		case PlotPaperSize._CSIZEPLOT: return "C";
    		case PlotPaperSize._DSIZEPLOT: return "D";
    		case PlotPaperSize._ESIZEPLOT: return "E";
    		case PlotPaperSize._A4SIZEPLOT: return "A4";
    		case PlotPaperSize._A3SIZEPLOT: return "A3";
    		case PlotPaperSize._A2SIZEPLOT: return "A2";
    		case PlotPaperSize._A1SIZEPLOT: return "A1";
    		case PlotPaperSize._A0SIZEPLOT: return "A0";
    		case PlotPaperSize._FSIZEPLOT: return "F";
    		case PlotPaperSize._VARIABLESIZEPLOT: return "VARIABLE";
    		case PlotPaperSize._VARIABLESIZE_IN_MM_PLOT: return "VARIABLE_IN_MM";
    		default: return null;
    	}
    }

    /**
     * Get the origin point of a drawing view
     * @param view The drawing view
     * @return The origin point in drawing coordinates
     * @throws jxthrowable
     */
    public static JLPoint getViewOrigin(CallView2D view) throws jxthrowable {
        CallOutline3D outline = view.getOutline();
        CallPoint3D pt1 = outline.get(0);
        CallPoint3D pt2 = outline.get(1);
        
        JLPoint out = new JLPoint();
        out.setX((pt1.get(0)+pt2.get(0))/2.0);
        out.setY((pt1.get(1)+pt2.get(1))/2.0);
        out.setZ((pt1.get(2)+pt2.get(2))/2.0);
        return out;
    }

    /**
     * Convert a screen coordinate to a drawing coordinate
     * @param drw The drawing containing the coordinate
     * @param sheetno The sheet number containing the coordinate
     * @param ptScreen The screen point
     * @return The drawing point
     * @throws jxthrowable
     */
    public static CallPoint3D screenToDrawingPoint(CallModel2D drw, int sheetno, CallPoint3D ptScreen) throws jxthrowable {
    	CallTransform3D sheetTransform = drw.getSheetTransform(sheetno);
    	CallPoint3D ptDrawing = sheetTransform.transformPoint(ptScreen);
    	return ptDrawing;
    }

    /**
     * Convert a screen vector to a drawing vector.  In this case, a vector is 
     * represented by its endpoint, with its starting point assumed to be at 0,0,0. 
     * @param drw The drawing containing the vector
     * @param sheetno The sheet number containing the vector
     * @param vecScreen The screen vector
     * @return The drawing vector
     * @throws jxthrowable
     */
    public static CallVector3D screenToDrawingPoint(CallModel2D drw, int sheetno, CallVector3D vecScreen) throws jxthrowable {
    	CallTransform3D sheetTransform = drw.getSheetTransform(sheetno);
    	CallVector3D vecDrawing = sheetTransform.transformVector(vecScreen);
    	return vecDrawing;
    }

    /**
     * Convert a screen distance to a drawing distance.   
     * @param drw The drawing containing the distance
     * @param sheetno The sheet number containing the distance
     * @param dist The distance
     * @return The drawing distance
     * @throws jxthrowable
     */
    public static double screenToDrawingDistance(CallModel2D drw, int sheetno, double dist) throws jxthrowable {
    	CallPoint3D pt = CallPoint3D.create();
    	pt.set(0, dist);
    	pt = screenToDrawingPoint(drw, sheetno, pt);
    	return pt.get(0);
    }

    /** Convert a drawing coordinate to a screen coordinate
     * @param drw The drawing containing the coordinate
     * @param sheetno The sheet number containing the coordinate
     * @param ptDrawing The drawing point
     * @return The screen point
     * @throws jxthrowable
     */
    public static CallPoint3D drawingToScreenPoint(CallModel2D drw, int sheetno, CallPoint3D ptDrawing) throws jxthrowable {
    	CallTransform3D sheetTransform = drw.getSheetTransform(sheetno);
    	sheetTransform.invert();
    	CallPoint3D ptScreen = sheetTransform.transformPoint(ptDrawing);
    	return ptScreen;
    }

    /**
     * Convert a drawing vector to a screen vector.  In this case, a vector is 
     * represented by its endpoint, with its starting point assumed to be at 0,0,0. 
     * @param drw The drawing containing the vector
     * @param sheetno The sheet number containing the vector
     * @param vecDrawing The drawing vector
     * @return The screen vector
     * @throws jxthrowable
     */
    public static CallVector3D drawingToScreenPoint(CallModel2D drw, int sheetno, CallVector3D vecDrawing) throws jxthrowable {
    	CallTransform3D sheetTransform = drw.getSheetTransform(sheetno);
    	sheetTransform.invert();
    	CallVector3D vecScreen = sheetTransform.transformVector(vecDrawing);
    	return vecScreen;
    }

    /**
     * Convert a drawing distance to a screen distance. 
     * @param drw The drawing containing the distance
     * @param sheetno The sheet number containing the distance
     * @param dist The drawing distance
     * @return The screen distance
     * @throws jxthrowable
     */
    public static double drawingToScreenDistance(CallModel2D drw, int sheetno, double dist) throws jxthrowable {
    	CallPoint3D pt = CallPoint3D.create();
    	pt.set(0, dist);
    	pt = drawingToScreenPoint(drw, sheetno, pt);
    	return pt.get(0);
    }

    /**
     * Normalize a 3D matrix.  I believe this means removing the offset and 
     * scaling parts of the matrix.
     * 
     * <p>The input matrix is modified in place.
     * 
     * @param matrix The matrix to normalize.
     * @throws jxthrowable
     */
    private static void normalizeMatrix(CallMatrix3D matrix) throws jxthrowable {
        // Remove the shift
        for (int i = 0; i < 3; ++i) {
               matrix.set(3, i, 0.0);
        }

        // Get the scaling factor
        double scale = Math.sqrt(matrix.get(0, 0) * matrix.get(0, 0) + matrix.get(0, 1) * matrix.get(0, 1) + matrix.get(0, 2) * matrix.get(0, 2));

        // Remove the scaling
        for (int row = 0; row < 3; ++row) {
               for (int col = 0; col < 3; ++col) {
                     matrix.set(row, col, matrix.get(row, col) / scale);
               }
        }
    }

    /**
     * Initialize a view's ViewDisplay data based on JShell's ViewDisplayData
     * @param view The view to update
     * @param displayData The input display data
     * @throws jxthrowable
     */
    private void initViewDisplay(CallView2D view, ViewDisplayData displayData) throws jxthrowable {
    	if (displayData==null)
    		return;
    	
    	CableDisplayStyle cabstyle = CableDisplayStyle.CABLEDISP_DEFAULT;
    	if (ViewDisplayData.CABLESTYLE_DEFAULT.equalsIgnoreCase(displayData.getCableStyle()))
    		cabstyle = CableDisplayStyle.CABLEDISP_DEFAULT;
    	else if (ViewDisplayData.CABLESTYLE_CENTERLINE.equalsIgnoreCase(displayData.getCableStyle()))
    		cabstyle = CableDisplayStyle.CABLEDISP_CENTERLINE;
    	else if (ViewDisplayData.CABLESTYLE_THICK.equalsIgnoreCase(displayData.getCableStyle()))
    		cabstyle = CableDisplayStyle.CABLEDISP_THICK;
    	
    	DisplayStyle dispstyle = DisplayStyle.DISPSTYLE_DEFAULT;
    	if (ViewDisplayData.STYLE_DEFAULT.equalsIgnoreCase(displayData.getStyle()))
    		dispstyle = DisplayStyle.DISPSTYLE_DEFAULT;
    	else if (ViewDisplayData.STYLE_WIREFRAME.equalsIgnoreCase(displayData.getStyle()))
    		dispstyle = DisplayStyle.DISPSTYLE_WIREFRAME;
    	else if (ViewDisplayData.STYLE_HIDDEN_LINE.equalsIgnoreCase(displayData.getStyle()))
    		dispstyle = DisplayStyle.DISPSTYLE_HIDDEN_LINE;
    	else if (ViewDisplayData.STYLE_NO_HIDDEN.equalsIgnoreCase(displayData.getStyle()))
    		dispstyle = DisplayStyle.DISPSTYLE_NO_HIDDEN;
    	else if (ViewDisplayData.STYLE_SHADED.equalsIgnoreCase(displayData.getStyle()))
    		dispstyle = DisplayStyle.DISPSTYLE_SHADED;
    	else if (ViewDisplayData.STYLE_FOLLOW_ENV.equalsIgnoreCase(displayData.getStyle()))
    		dispstyle = DisplayStyle.DISPSTYLE_FOLLOW_ENVIRONMENT;
    	// this is creo only...
//    	else if (ViewDisplayData.STYLE_SHADED_W_EDGES.equalsIgnoreCase(displayData.getStyle()))
//    		dispstyle = DisplayStyle.DISPSTYLE_SHADED_WITH_EDGES;
    	
    	TangentEdgeDisplayStyle tanstyle = TangentEdgeDisplayStyle.TANEDGE_DEFAULT;
    	if (ViewDisplayData.TANGENT_DEFAULT.equalsIgnoreCase(displayData.getTangentStyle()))
    		tanstyle = TangentEdgeDisplayStyle.TANEDGE_DEFAULT;
    	else if (ViewDisplayData.TANGENT_NONE.equalsIgnoreCase(displayData.getTangentStyle()))
    		tanstyle = TangentEdgeDisplayStyle.TANEDGE_NONE;
    	else if (ViewDisplayData.TANGENT_CENTERLINE.equalsIgnoreCase(displayData.getTangentStyle()))
    		tanstyle = TangentEdgeDisplayStyle.TANEDGE_CENTERLINE;
    	else if (ViewDisplayData.TANGENT_PHANTOM.equalsIgnoreCase(displayData.getTangentStyle()))
    		tanstyle = TangentEdgeDisplayStyle.TANEDGE_PHANTOM;
    	else if (ViewDisplayData.TANGENT_DIMMED.equalsIgnoreCase(displayData.getTangentStyle()))
    		tanstyle = TangentEdgeDisplayStyle.TANEDGE_DIMMED;
    	else if (ViewDisplayData.TANGENT_SOLID.equalsIgnoreCase(displayData.getTangentStyle()))
    		tanstyle = TangentEdgeDisplayStyle.TANEDGE_SOLID;

    	CallViewDisplay disp = CallViewDisplay.create(
    			dispstyle, 
    			tanstyle, 
    			cabstyle, 
    			displayData.isRemoveQuiltHiddenLines(), 
    			displayData.isShowConceptModel(), 
    			displayData.isShowWeldXSection());
    	if (disp!=null)
    		view.setDisplay(disp);
    }

    /**
     * Copy one view's display data to another.  Usually used for setting up a 
     * new Projection view.
     * 
     * @param parent The parent view being copied from
     * @param child The child view being copied to 
     * @throws jxthrowable
     */
    private void copyViewDisplay(CallView2D parent, CallView2D child) throws jxthrowable {
    	CallViewDisplay disp1 = parent.getDisplay();
    	CallViewDisplay disp2 = CallViewDisplay.create(
    			disp1.getStyle(),
    			disp1.getTangentStyle(),
    			disp1.getCableStyle(),
    			disp1.getRemoveQuiltHiddenLines(),
    			disp1.getShowConceptModel(),
    			disp1.getShowWeldXSection());
    	child.setDisplay(disp2);
    }

    /**
     * Find a symbol definition in Creo.
     * @param drw The drawing containing the symbol
     * @param symbolName The symbol name
     * @return The Symbol Definition
     * @throws jxthrowable
     */
    private CallDetailSymbolDefItem findSymbolDefItem(CallDrawing drw, String symbolName) throws jxthrowable {
    	CallDetailItems items = drw.listDetailItems(DetailType.DETAIL_SYM_DEFINITION, null);
    	if (items==null)
    		return null;
    	int len = items.getarraysize();
    	for (int i=0; i<len; i++) {
    		CallDetailSymbolDefItem item = (CallDetailSymbolDefItem)items.get(i);
    		CallDetailSymbolDefInstructions instDef = item.getInstructions();
    		if (symbolName.equals(instDef.getName()))
    			return item;
    	}
    	return null;
    }

    /**
     * Convert a symbol file name (for example, "C:\test\my_symbol.sym.3") to a Creo Symbol Name (for example, "MY_SYMBOL").
     * @param symbolFile The name of the file
     * @return The name of the Symbol
     */
    private String getSymbolNameFromFile(String symbolFile) {
    	if (symbolFile==null)
    		return null;
		int extpos = NitroUtils.findFileExtension(symbolFile);
		if (extpos>=0)
			symbolFile = symbolFile.substring(0, extpos);
		extpos = NitroUtils.findFileBase(symbolFile);
		if (extpos>=0)
			symbolFile = symbolFile.substring(extpos+1);
		if (symbolFile.length()==0)
			return null;
		return symbolFile.toUpperCase();
    }

    /**
     * An implementation of ModelLooper which accumulates a list of model
     * names in a drawing.
     * 
     * @author Adam Andrews
     */
    private class ListModelLooper extends ModelLooper {

        /**
         * The output list of model names
         */
        List<String> out = new Vector<String>();
        
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
        	if (m!=null && m instanceof CallSolid) {
    	        String name = null;
        		try {
        			name = m.getFileName();
        		}
                catch (XToolkitCantOpen e) {
                	JlinkUtils.resolveGenerics(getSession(), m);
                	name = m.getFileName();
                }
                if (name!=null)
                	out.add(name);
        	}
        	
        	return false;
		}
    }

    /**
     * An implementation of View2DLooper which accumulates a list of view
     * names in a drawing.
     * 
     * @author Adam Andrews
     *
     */
    private class ListViewLooper extends View2DLooper {

        /**
         * The output list of view names
         */
        List<String> out = new Vector<String>();
        
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.View2DLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D)
		 */
		@Override
		public boolean loopAction(CallView2D view) throws JLIException, jxthrowable {
        	if (view!=null) {
    	        String name = null;
       			name = view.getName();
                if (name!=null)
                	out.add(name);
        	}
        	return false;
		}
    }

    /**
     * An implementation of View2DLooper which accumulates a list of view
     * info in a drawing.
     * 
     * @author Adam Andrews
     *
     */
    private class ListViewDetailLooper extends View2DLooper {

    	private double textHeight=0;

        /**
         * The output list of view names
         */
        List<ViewDetailData> out = new Vector<ViewDetailData>();
        
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.View2DLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D)
		 */
		@Override
		public boolean loopAction(CallView2D view) throws JLIException, jxthrowable {
			if (textHeight==0) {
	        	textHeight = getDrawing().getTextHeight();
			}

        	if (view!=null) {
        		ViewDetailData vd = new ViewDetailData();
        		
        		vd.setName(view.getName());
        		try {
        			vd.setSheetNo(view.getSheetNumber());
        		}
        		catch (XToolkitGeneralError e) {
        			// this error was thrown for a view that was Suppressed
        			return false;
        		}

        		CallModel m = view.getModel();
        		if (m!=null)
        			vd.setModel(m.getFileName());
        		CallSimpRep rep = view.getSimpRep();
        		if (rep!=null)
        			vd.setSimpRep(rep.getName());

                CallOutline3D outline = view.getOutline();
                CallPoint3D pt0 = screenToDrawingPoint(getDrawing(), vd.getSheetNo(), outline.get(0));
                CallPoint3D pt1 = screenToDrawingPoint(getDrawing(), vd.getSheetNo(), outline.get(1));

                JLPoint pt = new JLPoint();
                pt.setX((pt1.get(0)+pt0.get(0))/2);
                pt.setY((pt1.get(1)+pt0.get(1))/2);
                vd.setLocation(pt);

//            	vd.setTextHeight(drawingToScreenDistance(getDrawing(), 0, textHeight));
                vd.setTextHeight(textHeight); // this is already in drawing units

                out.add(vd);
        	}
        	return false;
		}
    }

    /**
     * An implementation of View2DLooper which accumulates a list of view
     * objects in a drawing.
     * 
     * @author Adam Andrews
     *
     */
    private class ListViewObjectLooper extends View2DLooper {

        /**
         * The output list of view names
         */
        List<CallView2D> out = new Vector<CallView2D>();
        
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.View2DLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D)
		 */
		@Override
		public boolean loopAction(CallView2D view) throws JLIException, jxthrowable {
        	if (view!=null)
        		out.add(view);
        	return false;
		}
    }

    /**
     * An implementation of ModelLooper which deletes models (and possibly
     * their views) from a drawing
     * @author Adam Andrews
     *
     */
    private class DeleteModelLooper extends ModelLooper {
    	/**
    	 * Whether to also delete views that correspond to the deleted models
    	 */
    	public boolean deleteViews=false;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			if (deleteViews) {
				CallView2Ds views = getDrawing().list2DViews();
				if (views!=null) {
					int len = views.getarraysize();
					CallView2D view;
					for (int i=0; i<len; i++) {
						view = views.get(i);
						try {
							if (m.getFullName().equals(view.getModel().getFullName())) {
								view.delete(true);
							}
						}
						catch (jxthrowable e) {
							// ignore delete errors, probably due to trying to 
							// delete a child view that's already been deleted
						}
					}
				}
			}
        	if (m!=null && m instanceof CallSolid) {
        		try {
        			getDrawing().deleteModel(m);
        		}
        		catch (XToolkitInUse e) {
        			throw new JLIException("Cannot delete a model that is in use: " + m.getFileName());
        		}
        	}
        	
        	return false;
		}
    }
    
    /**
     * An implementation of View2DLooper which sets the scale of drawing views
     * @author Adam Andrews
     *
     */
    private class ScaleViewLooper extends View2DLooper {

    	/**
    	 * The scale to set
    	 */
    	double scale = 1.0;
        /**
         * List of view which failed to scale (output)
         */
        List<String> failedViews = new Vector<String>();
        /**
         * List of view which succeeded in scaling (output)
         */
        List<String> successViews = new Vector<String>();
    	
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.View2DLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.view2d.CallView2D)
		 */
		@Override
		public boolean loopAction(CallView2D view) throws JLIException, jxthrowable {
        	if (view!=null) {
        		// handle this bug:
    			// https://support.ptc.com/appserver/cs/view/solution.jsp?n=CS111524&lang=en&source=snippet
        		if (view.getIsScaleUserdefined()) {
        			view.setScale(scale);
        			successViews.add(view.getName());
        		}
        		else
        			failedViews.add(view.getName());
        	}
        	return false;
		}
    }

    /**
     * An implementation of SheetLooper which regenerates sheets
     * in a drawing.
     * 
     * @author Adam Andrews
     *
     */
    public class RegenerateSheetLooper extends SheetLooper {

		@Override
		public boolean loopAction(CallModel2D drawing, int sheetno) throws JLIException,jxthrowable {
        	drawing.regenerateSheet(sheetno);

        	return false;
		}
    }

    /**
     * An implementation of SheetLooper which adds symbols to 
     * sheets in a drawing.
     * 
     * @author Adam Andrews
     *
     */
    public class CreateSymbolLooper extends SheetLooper {

    	public CallDetailSymbolInstInstructions inst;
    	
		@Override
		public boolean loopAction(CallModel2D drawing, int sheetno) throws JLIException,jxthrowable {
			CallDetailSymbolInstItem symInst = (CallDetailSymbolInstItem)drawing.createDetailItem(inst);
			symInst.show(); 

			return false;
		}
    }

    /**
     * An implementation of SheetLooper which lists symbols on 
     * sheets in a drawing.
     * 
     * @author Adam Andrews
     *
     */
    public class ListSymbolLooper extends SheetLooper {

    	public List<SymbolInstData> outvals=null;
    	
    	public String symbolName = null;

		@Override
		public boolean loopAction(CallModel2D drawing, int sheetno) throws JLIException,jxthrowable {

	    	CallDetailItems items = drawing.listDetailItems(DetailType.DETAIL_SYM_INSTANCE, sheetno);
	    	if (items==null)
	    		return false;
	    	
	    	int len = items.getarraysize();
	    	SymbolInstData rec;
	    	for (int i=0; i<len; i++) {
	    		CallDetailSymbolInstItem item = (CallDetailSymbolInstItem)items.get(i);
//	    		CallDetailSymbolInstInstructions instInst = item.getInstructions(true);
	    		CallDetailSymbolInstInstructions instInst = item.getInstructions(true);
	    		CallDetailSymbolDefInstructions instDef = instInst.getSymbolDef().getInstructions();
	    		if (symbolName==null || instDef.getName().equals(symbolName)) {
		    		rec = new SymbolInstData();

		    		rec.setId(item.getId());
		    		if (instDef!=null)
		    			rec.setName(instDef.getName());
		    		rec.setSheet(sheetno);
		    		
		    		CallDetailLeaders leaders = instInst.getInstAttachment();
		    		if (leaders!=null) {
		    			CallAttachment attach = leaders.getItemAttachment();
		    			if (attach!=null) {
				    		CallPoint3D pt=null;
				    		int type = attach.getType().getValue();
				    		switch (type) {
				    			case AttachmentType._ATTACH_FREE: 
				    				CallFreeAttachment freeAttach = (CallFreeAttachment)attach;
				    				pt = freeAttach.getAttachmentPoint();
				    				rec.setAttachType(SymbolInstData.ATTACH_TYPE_FREE);
				    				break;
				    			case AttachmentType._ATTACH_PARAMETRIC:
				    				CallParametricAttachment paramAttach = (CallParametricAttachment)attach;
				    				CallSelection sel = paramAttach.getAttachedGeometry();
				    				pt = sel.getPoint();
				    				rec.setAttachType(SymbolInstData.ATTACH_TYPE_PARAMETRIC);
				    				break;
				    			case AttachmentType._ATTACH_TYPE_UNSUPPORTED: 
				    				CallUnsupportedAttachment unAttach = (CallUnsupportedAttachment)attach;
				    				pt = unAttach.getAttachmentPoint();
				    				rec.setAttachType(SymbolInstData.ATTACH_TYPE_UNKNOWN);
				    				break;
				    			case AttachmentType._ATTACH_OFFSET: 
				    				CallOffsetAttachment offsetAttach = (CallOffsetAttachment)attach;
				    				pt = offsetAttach.getAttachmentPoint();
				    				if (pt==null) {
				    					sel = offsetAttach.getAttachedGeometry();
					    				pt = sel.getPoint();
				    				}
				    				rec.setAttachType(SymbolInstData.ATTACH_TYPE_OFFSET);
				    				break;
				    		}
				    		
				            CallPoint3D pt0 = JLDrawing.screenToDrawingPoint(drawing, sheetno, pt);
				            rec.setLocation(new JLPoint(pt0.get(0), pt0.get(1), pt0.get(2)));
		    			}
		    		}
		        	
			    	if (outvals==null)
			    		outvals = new Vector<SymbolInstData>();
			    	outvals.add(rec);
	    		}

	    	}

			return false;
		}
    }

    /**
     * Implementation of Regenerator which will regenerate a Drawing.
     * 
     * @author Adam Andrews
     *
     */
    public static class RegenerateDrawing implements JlinkUtils.Regenerator {
    	private CallModel2D drw;
    	
    	public RegenerateDrawing(CallModel2D drw) {
    		this.drw = drw;
    	}
		@Override
		public void regenerate() throws jxthrowable {
			drw.regenerate();
		}
		
		@Override
		public CallRegenInstructions getInstructions() {
			return null;
		}
    }
}
