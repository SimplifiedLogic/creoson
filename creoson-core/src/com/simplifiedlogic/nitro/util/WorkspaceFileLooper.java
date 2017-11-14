/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.util;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcSession.FileListOpt;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.impl.JlinkUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is an abstract class which loops over the files in a
 * Windchill workspace.
 * 
 * @author Adam Andrews
 */
public abstract class WorkspaceFileLooper {

	public static final FileListOpt LIST_OPT = FileListOpt.FILE_LIST_ALL;

	private String namePattern = null;
	private CallSession session = null;
    private String serverAlias = null;
    private String workspace = null;
    
    public void loop() throws JLIException,jxthrowable {

        if (namePattern==null)
        	namePattern="*";
        
		String workspaceUrl = JlinkUtils.makeWindchillUrl(serverAlias, workspace);
        CallStringSeq files = session.listFiles(namePattern, LIST_OPT, workspaceUrl);
        
        if (files==null)
        	return;
        int sz = files.getarraysize();
        String fileUrl;
        for (int i=0; i<sz; i++) {
            fileUrl = files.get(i);
            if (fileUrl==null) continue;
            String name = JlinkUtils.stripWindchillUrl(fileUrl);
            
            boolean abort = loopAction(name);
            if (abort)
                break;
        }
    }

    /**
     * Abstract function which is called for each file which matches the filters.
     * This must be implemented by a class extending this looper class.
     * @param fileName The file name which matched the filters
     * @return True to abort the loop, false to continue it
     * @throws JLIException
     * @throws jxthrowable
     */
    public abstract boolean loopAction(String fileName) throws JLIException,jxthrowable;

    /**
     * @return The Creo session
     */
    public CallSession getSession() {
        return session;
    }

    /**
     * @param session The Creo session
     */
    public void setSession(CallSession session) {
        this.session = session;
    }

	/**
	 * @return The Windchill server alias
	 */
	public String getServerAlias() {
		return serverAlias;
	}

	/**
	 * @param serverAlias The Windchill server alias
	 */
	public void setServerAlias(String serverAlias) {
		this.serverAlias = serverAlias;
	}

    /**
     * @return The Windchill workspace
     */
	public String getWorkspace() {
		return workspace;
	}

    /**
     * @param workspace The Windchill workspace
     */
	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) {
		this.namePattern = namePattern;
	}

}
