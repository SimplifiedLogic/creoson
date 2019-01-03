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
package com.simplifiedlogic.nitro.jlink.calls.session;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcBase.ColorRGB;
import com.ptc.pfc.pfcBase.StdColor;
import com.ptc.pfc.pfcDrawing.Drawing;
import com.ptc.pfc.pfcExport.AssemblyConfiguration;
import com.ptc.pfc.pfcModel.ExportType;
import com.ptc.pfc.pfcModel.Model;
import com.ptc.pfc.pfcModel.Models;
import com.ptc.pfc.pfcProToolkit.Dll;
import com.ptc.pfc.pfcSelect.Selections;
import com.ptc.pfc.pfcServer.Server;
import com.ptc.pfc.pfcSession.FileListOpt;
import com.ptc.pfc.pfcSession.Session;
import com.ptc.pfc.pfcWindow.Window;
import com.simplifiedlogic.nitro.jlink.calls.base.CallColorRGB;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawing;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawingCreateOptions;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModelDescriptor;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModels;
import com.simplifiedlogic.nitro.jlink.calls.protoolkit.CallDll;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelectionOptions;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelections;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.server.CallServer;
import com.simplifiedlogic.nitro.jlink.calls.window.CallWindow;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcSession.Session
 * 
 * @author Adam Andrews
 *
 */
public class CallSession {

	private Session session = null;
	
	public CallSession(Session session) {
		this.session = session;
	}
	
	public String getCurrentDirectory() throws jxthrowable {
    	long start = System.currentTimeMillis();
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetCurrentDirectory", 0, NitroConstants.DEBUG_JLINK_KEY);
		String dir = session.GetCurrentDirectory();
        DebugLogging.sendTimerMessage("jlink.GetCurrentDirectory", start, NitroConstants.DEBUG_CONNECT_KEY);
        return dir;
	}
	
	public void changeDirectory(String dir) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,ChangeDirectory", 0, NitroConstants.DEBUG_JLINK_KEY);
		session.ChangeDirectory(dir);
	}

	public CallModel getCurrentModel() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetCurrentModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = session.GetCurrentModel();
		return CallModel.create(m);
	}

	public CallModel getActiveModel() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetActiveModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = session.GetActiveModel();
		return CallModel.create(m);
	}

	public CallModel getModelFromFileName(String fileName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetModelFromFileName", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = session.GetModelFromFileName(fileName);
		return CallModel.create(m);
	}

	public CallModel getModelFromDescr(CallModelDescriptor desc) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetModelFromDescr", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = session.GetModelFromDescr(desc.getDesc());
		return CallModel.create(m);
	}
	
	public CallSelections select(CallSelectionOptions opts, CallSelections initialSels) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,Select", 0, NitroConstants.DEBUG_JLINK_KEY);
		Selections sels = session.Select(opts.getSelopts(), initialSels!=null ? initialSels.getSelections() : null);
		if (sels==null)
			return null;
		return new CallSelections(sels);
	}
	
	public CallWindow getCurrentWindow() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetCurrentWindow", 0, NitroConstants.DEBUG_JLINK_KEY);
		Window win = session.GetCurrentWindow();
		if (win==null)
			return null;
		return new CallWindow(win);
	}

	public CallWindow getModelWindow(CallModel m) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetModelWindow", 0, NitroConstants.DEBUG_JLINK_KEY);
		Window win = session.GetModelWindow(m.getModel());
		if (win==null)
			return null;
		return new CallWindow(win);
	}
	
	public CallWindow createModelWindow(CallModel m) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,CreateModelWindow", 0, NitroConstants.DEBUG_JLINK_KEY);
		Window win = session.CreateModelWindow(m.getModel());
		if (win==null)
			return null;
		return new CallWindow(win);
	}

	public void eraseUndisplayedModels() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,EraseUndisplayedModels", 0, NitroConstants.DEBUG_JLINK_KEY);
		session.EraseUndisplayedModels();
	}
	
	public CallModel retrieveModel(CallModelDescriptor desc) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,RetrieveModel", 0, NitroConstants.DEBUG_JLINK_KEY);
		Model m = session.RetrieveModel(desc.getDesc());
		if (m==null)
			return null;
		return CallModel.create(m);
	}
	
	public CallDll loadProToolkitDll(String applicationName, String dllPath, String textPath, boolean userDisplay) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,LoadProToolkitDll", 0, NitroConstants.DEBUG_JLINK_KEY);
        Dll dllRef = session.LoadProToolkitDll(
                applicationName, 
                dllPath,
                textPath, 
                userDisplay);
        
		if (dllRef==null)
			return null;
        return new CallDll(dllRef);
	}
	
	public CallDll getProToolkitDll(String applicationId) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetProToolkitDll", 0, NitroConstants.DEBUG_JLINK_KEY);
        Dll dllRef = session.GetProToolkitDll(applicationId);
        
		if (dllRef==null)
			return null;
        return new CallDll(dllRef);
	}
	
	public String getConfigOption(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetConfigOption", 0, NitroConstants.DEBUG_JLINK_KEY);
		return session.GetConfigOption(name);
	}
	
	public CallStringSeq getConfigOptionValues(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetConfigOptionValues", 0, NitroConstants.DEBUG_JLINK_KEY);
		stringseq values = session.GetConfigOptionValues(name);
		if (values==null)
			return null;
		return new CallStringSeq(values);
	}
	
	public void setConfigOption(String name, String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,SetConfigOption", 0, NitroConstants.DEBUG_JLINK_KEY);
		session.SetConfigOption(name, value);
	}
	
	public CallColorRGB getRGBFromStdColor(StdColor clr) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetRGBFromStdColor", 0, NitroConstants.DEBUG_JLINK_KEY);
		ColorRGB rgb = session.GetRGBFromStdColor(clr);
		if (rgb==null)
			return null;
		return new CallColorRGB(rgb);
	}

	public void setStdColorFromRGB(StdColor clr, CallColorRGB rgb) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,SetStdColorFromRGB", 0, NitroConstants.DEBUG_JLINK_KEY);
		session.SetStdColorFromRGB(clr, rgb.getRgb());
	}
	
	public boolean isConfigurationSupported(ExportType type, AssemblyConfiguration configuration) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,IsConfigurationSupported", 0, NitroConstants.DEBUG_JLINK_KEY);
		return session.IsConfigurationSupported(type, configuration);
	}
	
	public void runMacro(String macro) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,RunMacro", 0, NitroConstants.DEBUG_JLINK_KEY);
		session.RunMacro(macro);
	}
	
	public void authenticateBrowser(String username, String password) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,AuthenticateBrowser", 0, NitroConstants.DEBUG_JLINK_KEY);
		session.AuthenticateBrowser(username, password);
	}
	
	public CallServer getActiveServer() throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetActiveServer", 0, NitroConstants.DEBUG_JLINK_KEY);
		Server server = session.GetActiveServer();
		if (server==null)
			return null;
		return new CallServer(server);
	}
	
	public CallServer getServerByUrl(String url, String workspaceName) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetServerByUrl", 0, NitroConstants.DEBUG_JLINK_KEY);
		Server server = session.GetServerByUrl(url, workspaceName);
		if (server==null)
			return null;
		return new CallServer(server);
	}

	public CallServer getServerByAlias(String alias) throws jxthrowable {
		if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,GetServerByAlias", 0, NitroConstants.DEBUG_JLINK_KEY);
		Server server = session.GetServerByAlias(alias);
		if (server==null)
			return null;
		return new CallServer(server);
	}
	
	public CallModels listModels() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,ListModels", 0, NitroConstants.DEBUG_JLINK_KEY);
		Models models = session.ListModels();
		if (models==null)
			return null;
		return new CallModels(models);
	}
	
	public CallStringSeq listFiles(String filter, FileListOpt version, String path) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,ListFiles", 0, NitroConstants.DEBUG_JLINK_KEY);
		stringseq values = session.ListFiles(filter, version, path);
		if (values==null)
			return null;
		return new CallStringSeq(values);
	}

	public CallDrawing createDrawingFromTemplate(String name, String template, CallModelDescriptor drawingModel, CallDrawingCreateOptions options) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Session,createDrawingFromTemplate", 0, NitroConstants.DEBUG_JLINK_KEY);
		Drawing drw = session.CreateDrawingFromTemplate(
				name, 
				template, 
				drawingModel!=null ? drawingModel.getDesc() : null, 
				options.getOptions());
		if (drw==null)
			return null;
		return new CallDrawing(drw);
	}
	
	public Session getSession() {
		return session;
	}
}
