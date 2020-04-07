/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
import java.util.Map;
import java.util.Vector;

import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.data.NoteData;
import com.simplifiedlogic.nitro.jlink.intf.IJLNote;
import com.simplifiedlogic.nitro.jshell.json.request.JLNoteRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLNoteResponseParams;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Handle JSON requests for "note" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonNoteHandler extends JLJsonCommandHandler implements JLNoteRequestParams, JLNoteResponseParams {

	private IJLNote noteHandler = null;

	/**
	 * @param noteHandler
	 */
	public JLJsonNoteHandler(IJLNote noteHandler) {
		this.noteHandler = noteHandler;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler#handleFunction(java.lang.String, java.lang.String, java.util.Hashtable)
	 */
	public Hashtable<String, Object> handleFunction(String sessionId, String function, Hashtable<String, Object> input) throws JLIException {
		if (function==null)
			return null;
		
		if (function.equals(FUNC_SET))
			return actionSet(sessionId, input);
		else if (function.equals(FUNC_GET))
			return actionGet(sessionId, input);
		else if (function.equals(FUNC_DELETE))
			return actionDelete(sessionId, input);
		else if (function.equals(FUNC_LIST))
			return actionList(sessionId, input);
		else if (function.equals(FUNC_COPY))
			return actionCopy(sessionId, input);
		else if (function.equals(FUNC_EXISTS))
			return actionExists(sessionId, input);
		else {
			throw new JLIException("Unknown function name: " + function);
		}
	}

	private Hashtable<String, Object> actionSet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String notename = checkStringParameter(input, PARAM_NAME, true);
        Object valueObj = checkParameter(input, PARAM_VALUE, false);
        boolean encoded = checkFlagParameter(input, PARAM_ENCODED, false, false);
		Map<String, Object> pointObj = checkMapParameter(input, PARAM_LOCATION, false);
		JLPoint pt = readPoint(pointObj);

        noteHandler.set(modelname, notename, valueObj, encoded, pt, sessionId);
        
        return null;
	}

	private Hashtable<String, Object> actionGet(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String notename = checkStringParameter(input, PARAM_NAME, true);
        
        NoteData note = noteHandler.get(modelname, notename, sessionId);
        
        if (note!=null) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
        	if (note.getName()!=null)
        		out.put(OUTPUT_NAME, note.getName());
        	if (note.getValue()!=null)
        		out.put(OUTPUT_VALUE, note.getValue());
        	if (note.getValueExpanded()!=null)
        		out.put(OUTPUT_VALUE_EXPANDED, note.getValueExpanded());
        	out.put(OUTPUT_ENCODED, note.isEncoded());
        	if (note.getUrl()!=null)
        		out.put(OUTPUT_URL, note.getUrl());
        	if (note.getLocation()!=null)
				out.put(OUTPUT_LOCATION, writePoint(note.getLocation()));
        	return out;
        }
        return null;
	}

	private Hashtable<String, Object> actionDelete(String sessionId, Hashtable<String, Object> input) throws JLIException {
		
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String notename = checkStringParameter(input, PARAM_NAME, true);
        
        noteHandler.delete(modelname, notename, sessionId);
        
        return null;
	}

	private Hashtable<String, Object> actionList(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String namePattern = checkStringParameter(input, PARAM_NAME, false);
        List<String> noteNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	noteNames = getStringListValue(namesObj);
        }
        String valuePattern = checkStringParameter(input, PARAM_VALUE, false);
        boolean getExpanded = checkFlagParameter(input, PARAM_GET_EXPANDED, false, false);
        boolean select = checkFlagParameter(input, PARAM_SELECT, false, false);

        List<NoteData> params = noteHandler.list(filename, namePattern, noteNames, valuePattern, getExpanded, select, sessionId);
        
        if (params!=null && params.size()>0) {
			Hashtable<String, Object> out = new Hashtable<String, Object>();
			Vector<Map<String, Object>> outNotes = new Vector<Map<String, Object>>();
			out.put(OUTPUT_ITEMLIST, outNotes);
			Map<String, Object> outNote = null;
			for (NoteData note : params) {
				outNote = new Hashtable<String, Object>();
	        	if (note.getName()!=null)
	        		outNote.put(OUTPUT_NAME, note.getName());
	        	if (note.getValue()!=null)
	        		outNote.put(OUTPUT_VALUE, note.getValue());
	        	if (note.getValueExpanded()!=null)
	        		outNote.put(OUTPUT_VALUE_EXPANDED, note.getValueExpanded());
	        	outNote.put(OUTPUT_ENCODED, note.isEncoded());
	        	if (note.getUrl()!=null)
	        		outNote.put(OUTPUT_URL, note.getUrl());
	        	if (note.getLocation()!=null)
					outNote.put(OUTPUT_LOCATION, writePoint(note.getLocation()));
				
				outNotes.add(outNote);
			}
			
			return out;
        }
		return null;
	}

	private Hashtable<String, Object> actionCopy(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String modelname = checkStringParameter(input, PARAM_MODEL, false);
        String noteName = checkStringParameter(input, PARAM_NAME, false);
        String toName = checkStringParameter(input, PARAM_TONAME, false);
        String toModel = checkStringParameter(input, PARAM_TOMODEL, false);

        noteHandler.copy(modelname, noteName, toModel, toName, sessionId);
        
		return null;
	}
	
	private Hashtable<String, Object> actionExists(String sessionId, Hashtable<String, Object> input) throws JLIException {
        String filename = checkStringParameter(input, PARAM_MODEL, false);
        String namePattern = checkStringParameter(input, PARAM_NAME, false);
        List<String> noteNames = null;
        Object namesObj = checkParameter(input, PARAM_NAMES, false);
        if (namesObj!=null) {
        	noteNames = getStringListValue(namesObj);
        }

        boolean exists = noteHandler.exists(filename, namePattern, noteNames, sessionId);
        
		Hashtable<String, Object> out = new Hashtable<String, Object>();
        out.put(OUTPUT_EXISTS, exists);
       	return out;
	}
}
