/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jshell.json.handler;

import java.util.Hashtable;
import java.util.List;

import com.simplifiedlogic.nitro.jlink.data.JLColor;
import com.simplifiedlogic.nitro.jlink.intf.IJLProe;
import com.simplifiedlogic.nitro.jshell.json.request.JLCreoRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLCreoResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "creo" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonCreoHandler extends JLJsonCommandHandler implements JLCreoRequestParams, JLCreoResponseParams {

	private IJLProe proeHandler = null;

	/**
	 * @param proeHandler
	 */
	public JLJsonCreoHandler(IJLProe proeHandler) {
		this.proeHandler = proeHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_CD))
			return actionCd(sessionId, input);
		else if (function.equals(FUNC_MKDIR))
			return actionMkdir(sessionId, input);
		else if (function.equals(FUNC_PWD))
			return actionPwd(sessionId, input);
		else if (function.equals(FUNC_LIST_DIRS))
			return actionListDirs(sessionId, input);
		else if (function.equals(FUNC_LIST_FILES))
			return actionListFiles(sessionId, input);
		else if (function.equals(FUNC_RMDIR))
			return actionRmdir(sessionId, input);
		else if (function.equals(FUNC_DELETE_FILES))
			return actionDeleteFiles(sessionId, input);
		else if (function.equals(FUNC_SET_CONFIG))
			return actionSetConfig(sessionId, input);
		else if (function.equals(FUNC_GET_CONFIG))
			return actionGetConfig(sessionId, input);
		else if (function.equals(FUNC_SET_STD_COLOR))
			return actionSetStandardColor(sessionId, input);
		else if (function.equals(FUNC_GET_STD_COLOR))
			return actionGetStandardColor(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}
	
	private Hashtable<String, Object> actionCd(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String dirname = checkStringParameter(input, PARAM_DIRNAME, true);
        
		String outdir = proeHandler.cd(dirname, sessionId);
		
		if (outdir!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, outdir);
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionMkdir(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String dirname = checkStringParameter(input, PARAM_DIRNAME, true);
        
		String outdir = proeHandler.mkdir(dirname, sessionId);
		
		if (outdir!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, outdir);
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionPwd(String sessionId, Hashtable<String, Object> input) throws JLIException {
        
		String outdir = proeHandler.pwd(sessionId);
		
		if (outdir!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRNAME, outdir);
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionListDirs(String sessionId, Hashtable<String, Object> input) throws JLIException {

		String filename= checkStringParameter(input, PARAM_DIRNAME, true);
        
		List<String> dirs = proeHandler.list_dirs(filename, sessionId);
		
		if (dirs!=null && dirs.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_DIRLIST, dirs);
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionListFiles(String sessionId, Hashtable<String, Object> input) throws JLIException {

		String filename= checkStringParameter(input, PARAM_FILENAME, true);
        
		List<String> files = proeHandler.list_files(filename, sessionId);
		
		if (files!=null && files.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_FILELIST, files);
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionDeleteFiles(String sessionId, Hashtable<String, Object> input) throws JLIException {

		String dirname = checkStringParameter(input, PARAM_DIRNAME, false);
		String filename = checkStringParameter(input, PARAM_FILENAME, false);
        List<String> filenames = null;
        Object namesObj = checkParameter(input, PARAM_FILENAMES, false);
        if (namesObj!=null) {
        	filenames = getStringListValue(namesObj);
        }
        
		List<String> files = proeHandler.deleteFiles(dirname, filename, filenames, sessionId);
		
		if (files!=null && files.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_FILELIST, files);
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionRmdir(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String dirname = checkStringParameter(input, PARAM_DIRNAME, true);
        
		proeHandler.rmdir(dirname, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionSetConfig(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String name = checkStringParameter(input, PARAM_NAME, true);
        String value = checkStringParameter(input, PARAM_VALUE, false);
        boolean ignoreErrors = checkFlagParameter(input, PARAM_IGNORE_ERRORS, false, false);
       
		proeHandler.setConfig(name, value, ignoreErrors, sessionId);
		
		return null;
	}

	private Hashtable<String, Object> actionGetConfig(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String name = checkStringParameter(input, PARAM_NAME, true);
        
		List<String> values = proeHandler.getConfig(name, sessionId);
		
		if (values!=null && values.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_VALUES, values);
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionGetStandardColor(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String colorName = checkStringParameter(input, PARAM_COLOR_TYPE, true);
        int type = getColorType(colorName);
        
		JLColor color = proeHandler.getStandardColor(type, sessionId);
		
		if (color!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			out.put(OUTPUT_RED, color.getRed());
			out.put(OUTPUT_GREEN, color.getGreen());
			out.put(OUTPUT_BLUE, color.getBlue());
			return out;
		}
		return null;
	}

	private Hashtable<String, Object> actionSetStandardColor(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String colorName = checkStringParameter(input, PARAM_COLOR_TYPE, true);
        int type = getColorType(colorName);
        
        int red = checkIntParameter(input, PARAM_RED, true, 0);
        int green = checkIntParameter(input, PARAM_GREEN, true, 0);
        int blue = checkIntParameter(input, PARAM_BLUE, true, 0);

        JLColor color = new JLColor(red, green, blue);
        
        proeHandler.setStandardColor(type, color, sessionId);
        
        return null;
	}
	
	private int getColorType(String colorName) {
		int type=0;
        if (STD_COLOR_LETTER.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_LETTER;
        else if (STD_COLOR_HIGHLIGHT.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_HIGHLIGHT;
        else if (STD_COLOR_DRAWING.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_DRAWING;
        else if (STD_COLOR_BACKGROUND.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_BACKGROUND;
        else if (STD_COLOR_HALF_TONE.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_HALF_TONE;
        else if (STD_COLOR_EDGE_HIGHLIGHT.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_EDGE_HIGHLIGHT;
        else if (STD_COLOR_DIMMED.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_DIMMED;
        else if (STD_COLOR_ERROR.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_ERROR;
        else if (STD_COLOR_WARNING.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_WARNING;
        else if (STD_COLOR_SHEETMETAL.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_SHEETMETAL;
        else if (STD_COLOR_CURVE.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_CURVE;
        else if (STD_COLOR_PRESEL_HIGHLIGHT.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_PRESEL_HIGHLIGHT;
        else if (STD_COLOR_SELECTED.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_SELECTED;
        else if (STD_COLOR_SECONDARY_SELECTED.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_SECONDARY_SELECTED;
        else if (STD_COLOR_PREVIEW.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_PREVIEW;
        else if (STD_COLOR_SECONDARY_PREVIEW.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_SECONDARY_PREVIEW;
        else if (STD_COLOR_DATUM.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_DATUM;
        else if (STD_COLOR_QUILT.equalsIgnoreCase(colorName))
        	type = IJLProe.STD_COLOR_QUILT;
        
		return type;
	}
}
