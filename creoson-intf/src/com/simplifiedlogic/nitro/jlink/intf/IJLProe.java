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
package com.simplifiedlogic.nitro.jlink.intf;

import java.util.List;

import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.JLColor;
import com.simplifiedlogic.nitro.rpc.JLIException;

public interface IJLProe {
	
	public static final int STD_COLOR_LETTER 				= 1;
	public static final int STD_COLOR_HIGHLIGHT 			= 2;
	public static final int STD_COLOR_DRAWING 				= 3;
	public static final int STD_COLOR_BACKGROUND 			= 4;
	public static final int STD_COLOR_HALF_TONE 			= 5;
	public static final int STD_COLOR_EDGE_HIGHLIGHT 		= 6;
	public static final int STD_COLOR_DIMMED 				= 7;
	public static final int STD_COLOR_ERROR 				= 8;
	public static final int STD_COLOR_WARNING 				= 9;
	public static final int STD_COLOR_SHEETMETAL 			= 10;
	public static final int STD_COLOR_CURVE 				= 11;
	public static final int STD_COLOR_PRESEL_HIGHLIGHT 		= 12;
	public static final int STD_COLOR_SELECTED 				= 13;
	public static final int STD_COLOR_SECONDARY_SELECTED 	= 14;
	public static final int STD_COLOR_PREVIEW 				= 15;
	public static final int STD_COLOR_SECONDARY_PREVIEW 	= 16;
	public static final int STD_COLOR_DATUM 				= 17;
	public static final int STD_COLOR_QUILT 				= 18;

	public abstract String cd(String dirname, String sessionId) throws JLIException;
	public abstract String cd(String dirname, AbstractJLISession sess) throws JLIException;

	public abstract String mkdir(String dirname, String sessionId) throws JLIException;
	public abstract String mkdir(String dirname, AbstractJLISession sess) throws JLIException;
	
	public abstract String pwd(String sessionId) throws JLIException;
	public abstract String pwd(AbstractJLISession sess) throws JLIException;
	
	public abstract List<String> list_dirs(String filename, String sessionId) throws JLIException;
	public abstract List<String> list_dirs(String filename, AbstractJLISession sess) throws JLIException;

	public abstract List<String> list_files(String filename, String sessionId) throws JLIException;
	public abstract List<String> list_files(String filename, AbstractJLISession sess) throws JLIException;

	public List<String> deleteFiles(String dirname, String filename, List<String> filenames, String sessionId) throws JLIException;
	public List<String> deleteFiles(String dirname, String filename, List<String> filenames, AbstractJLISession sess) throws JLIException;
	
	public abstract void rmdir(String dirname, String sessionId) throws JLIException;
	public abstract void rmdir(String dirname, AbstractJLISession sess) throws JLIException;

	public void setConfig(String name, String value, boolean ignoreErrors, String sessionId) throws JLIException;
	public void setConfig(String name, String value, boolean ignoreErrors, AbstractJLISession sess) throws JLIException;

	public List<String> getConfig(String name, String sessionId) throws JLIException;
	public List<String> getConfig(String name, AbstractJLISession sess) throws JLIException;
	
	public void setStandardColor(int type, JLColor color, String sessionId) throws JLIException;
	public void setStandardColor(int type, JLColor color, AbstractJLISession sess) throws JLIException;
	
	public JLColor getStandardColor(int type, String sessionId) throws JLIException;
	public JLColor getStandardColor(int type, AbstractJLISession sess) throws JLIException;
}
