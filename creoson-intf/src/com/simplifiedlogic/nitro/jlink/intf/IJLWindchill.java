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
package com.simplifiedlogic.nitro.jlink.intf;

import java.util.List;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLWindchill {

	public void authorize(String user, String password, String sessionId) throws JLIException;
	public void authorize(String user, String password, AbstractJLISession sess) throws JLIException;

	public void setServer(String serverUrl, String sessionId) throws JLIException;
	public void setServer(String serverUrl, AbstractJLISession sess) throws JLIException;

	public boolean serverExists(String serverUrl, String sessionId) throws JLIException;
	public boolean serverExists(String serverUrl, AbstractJLISession sess) throws JLIException;

	public List<String> listWorkspaces(String sessionId) throws JLIException;
	public List<String> listWorkspaces(AbstractJLISession sess) throws JLIException;

	public void setWorkspace(String workspace, String sessionId) throws JLIException;
	public void setWorkspace(String workspace, AbstractJLISession sess) throws JLIException;

	public String getWorkspace(String sessionId) throws JLIException;
	public String getWorkspace(AbstractJLISession sess) throws JLIException;

	public boolean workspaceExists(String workspace, String sessionId) throws JLIException;
	public boolean workspaceExists(String workspace, AbstractJLISession sess) throws JLIException;

	public void createWorkspace(String workspace, String context, String sessionId) throws JLIException;
	public void createWorkspace(String workspace, String context, AbstractJLISession sess) throws JLIException;

	public void clearWorkspace(String workspace, List<String> filenames, String sessionId) throws JLIException;
	public void clearWorkspace(String workspace, List<String> filenames, AbstractJLISession sess) throws JLIException;

	public void deleteWorkspace(String workspace, String sessionId) throws JLIException;
	public void deleteWorkspace(String workspace, AbstractJLISession sess) throws JLIException;

	public boolean fileCheckedOut(String workspace, String fileName, String sessionId) throws JLIException;
	public boolean fileCheckedOut(String workspace, String fileName, AbstractJLISession sess) throws JLIException;

	public List<String> listWorkspaceFiles(String filename, String workspace, String sessionId) throws JLIException;
	public List<String> listWorkspaceFiles(String filename, String workspace, AbstractJLISession sess) throws JLIException;

}
