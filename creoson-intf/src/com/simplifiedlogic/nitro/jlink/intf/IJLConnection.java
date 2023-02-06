/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.JLStatus;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLConnection {

	public static final String APP_PROE = "pro/e";

	public JLStatus connect(String system) throws JLIException;
	
	public void disconnect(AbstractJLISession sess) throws JLIException;
	public void disconnect(String sessionId) throws JLIException;
	
	public AbstractJLISession startProe(String startDir, String startCommand, int retries, boolean useDesktop, AbstractJLISession sess) throws JLIException;
	public AbstractJLISession startProe(String startDir, String startCommand, int retries, boolean useDesktop, String sessionId) throws JLIException;

	public void stopProe(AbstractJLISession sess) throws JLIException;
	public void stopProe(String sessionId) throws JLIException;
	
	public boolean isProeRunning() throws JLIException;
	public boolean isProeRunning(AbstractJLISession sess) throws JLIException;
	public boolean isProeRunning(String sessionId) throws JLIException;
	
	public void killProe() throws JLIException;
	
	public AbstractJLISession getSession(String sessionId);
}