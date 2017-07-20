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
package com.simplifiedlogic.nitro.jlink.data;

import java.util.Map;

import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Abstract class representing a JShell session object.
 * 
 * @author Adam Andrews
 *
 */
public abstract class AbstractJLISession {

    /**
     * When a function is executed, this function will write the request to a command log file
     * @param cmd The generic request  structure
     * @return Whether the write occurred successfully
     */
    public abstract boolean logCommand(Map<Object, Object> cmd);

    /**
     * When a function is executed, this function will write the response data to a command log file
     * @param cmd The generic response structure
     * @return Whether the write occurred successfully
     */
    public abstract boolean logReturn(Map<Object, Object> cmd);
    
    /**
     * Get the session's ID
     * @return The session ID
     */
    public abstract String getSessionId();
    
	/**
	 * Get the Creo connection ID; create a connection to Creo if one does not exist.
	 * @return The Creo connection ID
	 * @throws JLIException
	 */
	public abstract String getConnectionId() throws JLIException;
	
    /**
     * Get the Creo connection ID
     * @param connId The Creo connection ID
     */
    public abstract void setConnectionId(String connId);
}
