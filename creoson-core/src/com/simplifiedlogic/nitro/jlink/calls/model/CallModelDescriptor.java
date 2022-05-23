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
package com.simplifiedlogic.nitro.jlink.calls.model;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModel.ModelDescriptor;
import com.ptc.pfc.pfcModel.pfcModel;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModel.ModelDescriptor
 * 
 * @author Adam Andrews
 *
 */
public class CallModelDescriptor {

	private ModelDescriptor desc;
	
	public CallModelDescriptor(ModelDescriptor desc) {
		this.desc = desc;
	}

	public static CallModelDescriptor createFromFileName(String fileName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcModel,ModelDescriptor_CreateFromFileName", 0, NitroConstants.DEBUG_JLINK_KEY);
		ModelDescriptor descr = pfcModel.ModelDescriptor_CreateFromFileName(fileName);
		if (descr==null)
			return null;
		return new CallModelDescriptor(descr);
	}
	
	public int getType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetType", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetType().getValue();
	}
	
	public String getFileName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetFileName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetFileName();
	}
	
	public String getFullName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetFullName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetFullName();
	}

	public String getInstanceName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetInstanceName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetInstanceName();
	}
	
	public Integer getFileVersion() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetFileVersion", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetFileVersion();
	}

	public String getExtension() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetExtension", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetExtension();
	}
	
	public String getPath() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetPath", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetPath();
	}
	
	public void setPath(String path) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,SetPath", 0, NitroConstants.DEBUG_JLINK_KEY);
        desc.SetPath(path);
	}
	
	public void setGenericName(String path) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,SetGenericName", 0, NitroConstants.DEBUG_JLINK_KEY);
        desc.SetGenericName(path);
	}
	
	public String getDevice() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelDescriptor,GetDevice", 0, NitroConstants.DEBUG_JLINK_KEY);
		return desc.GetDevice();
	}
	
	public ModelDescriptor getDesc() {
		return desc;
	}
	
}
