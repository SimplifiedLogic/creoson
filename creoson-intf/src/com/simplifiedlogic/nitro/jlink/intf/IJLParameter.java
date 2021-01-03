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
import com.simplifiedlogic.nitro.jlink.data.ParameterCollData;
import com.simplifiedlogic.nitro.jlink.data.ParameterData;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLParameter {

    public static final String TYPE_STRING  = "STRING";
    public static final String TYPE_DOUBLE  = "DOUBLE";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_BOOL    = "BOOL";
    public static final String TYPE_NOTE    = "NOTE";
    
    public static final int DESIGNATE_ON		= 1;
    public static final int DESIGNATE_OFF		= 0;
    public static final int DESIGNATE_UNKNOWN	= -1;
    
    public static final boolean ENCODED_YES		= true;
    public static final boolean ENCODED_NO		= false;
    public static final boolean NOCREATE_YES	= true;
    public static final boolean NOCREATE_NO		= false;

	public void set(String filename, String paramName,
			Object value, String type, int designate,
			boolean encoded, boolean noCreate, String sessionId) throws JLIException;
	public void set(String filename, String paramName,
			Object value, String type, int designate,
			boolean encoded, boolean noCreate, AbstractJLISession sess) throws JLIException;

	public void setBatch(ParameterCollData batch, 
			boolean encoded, boolean noCreate, String sessionId) throws JLIException;
	public void setBatch(ParameterCollData batch, 
			boolean encoded, boolean noCreate, AbstractJLISession sess) throws JLIException;

	public void setDesignated(String filename, String paramName,
			boolean designate, String sessionId) throws JLIException;
	public void setDesignated(String filename, String paramName,
			boolean designate, AbstractJLISession sess) throws JLIException;
	
    public void delete(String filename, String paramName,
			String sessionId) throws JLIException;
	public void delete(String filename, String paramName,
			AbstractJLISession sess) throws JLIException;

	public void copy(String filename, String paramName,
			String toModel, String toName, int designate,
			String sessionId) throws JLIException;
	public void copy(String filename, String paramName,
			String toModel, String toName, int designate,
			AbstractJLISession sess) throws JLIException;

    public List<ParameterData> list(String filename, String paramName,
			List<String> paramNames, String valuePattern, 
			boolean encoded, String sessionId)
			throws JLIException;
	public List<ParameterData> list(String filename, String paramName,
			List<String> paramNames, String valuePattern, 
			boolean encoded, AbstractJLISession sess)
			throws JLIException;

    public boolean exists(String filename, String paramName,
			List<String> paramNames, String sessionId)
			throws JLIException;
    public boolean exists(String filename, String paramName,
			List<String> paramNames, AbstractJLISession sess)
			throws JLIException;

}