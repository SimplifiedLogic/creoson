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
package com.simplifiedlogic.nitro.jlink.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.regex.Pattern;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcModelItem.ModelItemType;
import com.simplifiedlogic.nitro.jlink.DataUtils;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallAttachment;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailLeaders;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailNoteInstructions;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailNoteItem;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailText;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailTextLine;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailTextLines;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailTexts;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallFreeAttachment;
import com.simplifiedlogic.nitro.jlink.calls.drawing.CallDrawing;
import com.simplifiedlogic.nitro.jlink.calls.model.CallModel;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.note.CallNote;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelectionBuffer;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelections;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.calls.session.CallSession;
import com.simplifiedlogic.nitro.jlink.calls.solid.CallSolid;
import com.simplifiedlogic.nitro.jlink.data.AbstractJLISession;
import com.simplifiedlogic.nitro.jlink.data.JLPoint;
import com.simplifiedlogic.nitro.jlink.data.NoteData;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.intf.IJLNote;
import com.simplifiedlogic.nitro.rpc.JLIException;
import com.simplifiedlogic.nitro.rpc.JLISession;
import com.simplifiedlogic.nitro.util.JLConnectionUtil;
import com.simplifiedlogic.nitro.util.JLPointMaker;
import com.simplifiedlogic.nitro.util.ModelItemLooper;
import com.simplifiedlogic.nitro.util.ModelLooper;

/**
 * @author Adam Andrews
 *
 */
public class JLNote implements IJLNote {

	/**
	 * Minimum valid length for a note name
	 */
	private static final int MIN_NOTE_NAME = 2;
    /**
     * Note names are not allowed to start with this prefix, because that is reserved by Creo
     */
    private static final String badPrefix = "Note_";
    /**
     * Length of the prefix (to save time)
     */
    private static final int badPrefixLen = badPrefix.length();
    /**
     * Maximum size of a line of text for a drawing note
     */
    private static final int DETAILTEXT_LIMIT = 80;
    
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#set(java.lang.String, java.lang.String, java.lang.Object, boolean, com.simplifiedlogic.nitro.jlink.data.JLPoint, java.lang.String)
	 */
	@Override
	public void set(
			String filename, 
			String noteName, 
			Object valueObj,
			boolean encoded, 
			JLPoint location, 
			String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

        set(filename, noteName, valueObj, encoded, location, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#set(java.lang.String, java.lang.String, java.lang.Object, boolean, com.simplifiedlogic.nitro.jlink.data.JLPoint, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void set(
			String filename, 
			String noteName, 
			Object valueObj,
			boolean encoded, 
			JLPoint location, 
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("note.set: " + noteName + "=" + valueObj, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (noteName==null || noteName.trim().length()==0)
    		throw new JLIException("No note name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            NitroUtils.validateNameChars(noteName);
            if (noteName.trim().length()<MIN_NOTE_NAME)
            	throw new JLIException("Note names must be at least " + MIN_NOTE_NAME + " characters");
	        
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        String[] text = null;
	        if (valueObj!=null) {
	        	try {
	        		String value = DataUtils.getStringValue(valueObj, encoded); 
		            text = value.split("\n");
	        	}
	        	catch (Exception e) {
	        		throw new JLIException("Unable to read data value for note: " + e.getMessage());
	        	}
	        }
	        
	        CallStringSeq textseq = CallStringSeq.create();
	        if (text!=null) {
	            for (int i=0; i<text.length; i++) {
	                textseq.set(i, text[i]);
	            }
	        }
	        else
	            textseq.set(0, " ");

	        SetLooper looper = new SetLooper();
            looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
	        looper.noteName = noteName;
	        looper.textseq = textseq;
	        looper.textArray = text;
	        looper.location = location;
	        looper.loop();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("note.set,"+noteName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#setLoc(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, java.lang.String)
	 */
	@Override
	public void setLoc(
			String filename, 
			String noteName, 
			JLPoint location, 
			String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);

        setLoc(filename, noteName, location, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#setLoc(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.JLPoint, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void setLoc(
			String filename, 
			String noteName, 
			JLPoint location, 
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("note.set_loc: " + noteName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (noteName==null || noteName.trim().length()==0)
    		throw new JLIException("No note name parameter given");
    	if (location==null)
    		throw new JLIException("No location parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            NitroUtils.validateNameChars(noteName);
            if (noteName.trim().length()<MIN_NOTE_NAME)
            	throw new JLIException("Note names must be at least " + MIN_NOTE_NAME + " characters");
	        
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        CallModel m = JlinkUtils.getFile(session, filename, false);
	        if (!(m instanceof CallDrawing)) {
	        	if (filename==null)
	        		throw new JLIException("Active model is not a drawing");
	        	else
	        		throw new JLIException("Model is not a drawing: " + filename);
	        }
        	CallDrawing dwg = (CallDrawing)m;
	        
			CallModelItem item = JlinkUtils.getNote(m, noteName, ModelItemType.ITEM_DTL_NOTE, false);

	        if (item==null)
	        	throw new JLIException("Note "+noteName+" not found on model");

        	// update an existing drawing note
        	CallDetailNoteItem note = (CallDetailNoteItem)item;
        	CallDetailNoteInstructions inst = note.getInstructions(true);
        	CallPoint3D loc = JLPointMaker.create3D(location);
        	int sheet = dwg.getCurrentSheetNumber();
	        loc = JLDrawing.drawingToScreenPoint(dwg, sheet, loc);
    		CallFreeAttachment position = CallFreeAttachment.create(loc);

    		CallDetailLeaders leaders = CallDetailLeaders.create();
    		leaders.setItemAttachment(position);

        	inst.setLeader(leaders);
        	
    		note.modify(inst);
            
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("note.set_loc,"+noteName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#get(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public NoteData get(
			String filename, 
			String noteName, 
			String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return get(filename, noteName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#get(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public NoteData get(
			String filename, 
			String noteName, 
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("note.get: " + noteName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (noteName==null || noteName.trim().length()==0)
    		throw new JLIException("No note name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            NitroUtils.validateNameChars(noteName);
            
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);

	        NoteData out = new NoteData();
	        out.setName(noteName);
	        boolean encoded = false;
	        CallModelItem item = JlinkUtils.getNote(m, noteName, false);
	        if (item!=null) {
	        	getNoteInfo(m, item, out, encoded, false);
	        }
	        else {
	            out.setValue("");
	        }
	        
	        return out;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("note.get,"+noteName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#delete(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void delete(
			String filename, 
			String noteName, 
			String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        delete(filename, noteName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#delete(java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public void delete(
			String filename, 
			String noteName, 
			AbstractJLISession sess) throws JLIException {

		DebugLogging.sendDebugMessage("note.delete: " + noteName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	if (noteName==null || noteName.trim().length()==0)
    		throw new JLIException("No note name parameter given");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;
	
	        DeleteLooper looper = new DeleteLooper();
        	looper.setNamePattern(filename);
	        looper.setDefaultToActive(true);
	        looper.setSession(session);
        	looper.noteName = noteName;
	        
	        looper.loop();
	        
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("note.delete,"+noteName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#list(java.lang.String, java.lang.String, java.util.List, java.lang.String, boolean, java.lang.String)
     */
    public List<NoteData> list(
	        String filename,
    		String noteName,
    		List<String> noteNames,
    		String valuePattern, 
    		boolean getExpandedValues, 
    		boolean select, 
	        String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return list(filename, noteName, noteNames, valuePattern, getExpandedValues, select, sess);
    }
    	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#list(java.lang.String, java.lang.String, java.util.List, java.lang.String, boolean, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	public List<NoteData> list(
	        String filename,
    		String noteName,
    		List<String> noteNames,
    		String valuePattern,
    		boolean getExpandedValues,
    		boolean select, 
	        AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("note.list: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return null;
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);
	
	        ListLooper looper = new ListLooper(m, session, select);
	        if (noteNames!=null)
	        	looper.setNameList(noteNames);
	        else if (noteName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(noteName);
	        looper.setValuePattern(valuePattern);
	        looper.getExpandedValues = getExpandedValues;
	        
	        looper.loop(m);
	        if (looper.output==null)
	            return new Vector<NoteData>();
	        else
	        	return looper.output;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("note.list,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
    }	

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#copy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void copy(
			String filename, 
			String noteName,
			String toModel, 
			String toName, 
			String sessionId) throws JLIException {
		
        JLISession sess = JLISession.getSession(sessionId);
        
        copy(filename, noteName, toModel, toName, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#copy(java.lang.String, java.lang.String, java.lang.String, java.lang.String, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	public void copy(
			String filename, 
			String noteName,
			String toModel, 
			String toName, 
			AbstractJLISession sess) throws JLIException {
		

		DebugLogging.sendDebugMessage("note.copy: " + noteName, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
            NitroUtils.validateNameChars(toName);
            if (toName!=null && toName.trim().length()<MIN_NOTE_NAME)
            	throw new JLIException("Note names must be at least " + MIN_NOTE_NAME + " characters");

	        JLGlobal.loadLibrary();
	    	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return;

	        CallModel m = JlinkUtils.getFile(session, filename, true);

	        BasicListLooper looper = new BasicListLooper(m);
	        if (noteName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(noteName);
	        looper.loop(m);

	        if (looper.modelNotes==null && looper.drawingNotes==null)
	            throw new JLIException("No matching notes: " + noteName);
	        
	        CopyLooper looper2 = new CopyLooper();
            looper2.setNamePattern(toModel);
	        looper2.setDefaultToActive(true);
	        looper2.setSession(session);
	        looper2.modelInputs = looper.modelNotes;
	        looper2.drawingInputs = looper.drawingNotes;
	        looper2.toName = toName;
	        looper2.exceptFileName = m.getFileName();
	        looper2.loop();
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("note.copy,"+noteName, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}

    /* (non-Javadoc)
     * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#exists(java.lang.String, java.lang.String, java.util.List, java.lang.String)
     */
    @Override
	public boolean exists(
			String filename, 
			String noteName,
			List<String> noteNames, 
			String sessionId) throws JLIException {

        JLISession sess = JLISession.getSession(sessionId);
        
        return exists(filename, noteName, noteNames, sess);
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jlink.intf.IJLNote#exists(java.lang.String, java.lang.String, java.util.List, com.simplifiedlogic.nitro.jlink.data.AbstractJLISession)
	 */
	@Override
	public boolean exists(
			String filename, 
			String noteName,
			List<String> noteNames, 
			AbstractJLISession sess) throws JLIException {
    	
		DebugLogging.sendDebugMessage("note.exists: " + filename, NitroConstants.DEBUG_KEY);
		if (sess==null)
			throw new JLIException("No session found");

    	long start = 0;
    	if (NitroConstants.TIME_TASKS)
    		start = System.currentTimeMillis();
    	try {
	        JLGlobal.loadLibrary();
	
	        CallSession session = JLConnectionUtil.getJLSession(sess.getConnectionId());
	        if (session == null)
	            return false;
	
	        CallModel m;
	        m = JlinkUtils.getFile(session, filename, true);
	
	        ExistsLooper looper = new ExistsLooper(m);
	        if (noteNames!=null)
	        	looper.setNameList(noteNames);
	        else if (noteName==null)
	        	looper.setNamePattern(null);
	        else
	        	looper.setNamePattern(noteName);
	        
	        looper.loop(m);
	        return looper.exists;
    	}
    	catch (jxthrowable e) {
    		throw JlinkUtils.createException(e);
    	}
    	finally {
        	if (NitroConstants.TIME_TASKS) {
        		DebugLogging.sendTimerMessage("note.exists,"+filename, start, NitroConstants.DEBUG_KEY);
        	}
    	}
	}
    
    /**
     * Checks whether a note name is valid.  Throws an error if the name is not valid
     * @param name The name to validate
     * @throws JLIException
     */
    public void validateNoteName(String name) throws JLIException {
        int len=name.length();
        if (len>badPrefixLen && name.substring(0, badPrefixLen).equalsIgnoreCase(badPrefix)) {
            // Note: turns out this much effort isn't necessary, because pro/e just checks 
            // whether the first character after the prefix is a digit 
//            int i;
//            for (i=badPrefixLen; i<len; i++) {
//                if (!Character.isDigit(name.charAt(i)))
//                    break;
//            }
//            if (i>=len)
//                throw new JLIException("Cannot create a note using the Note_# name pattern");
            if (Character.isDigit(name.charAt(badPrefixLen)))
                throw new JLIException("Cannot create a note using the Note_# name pattern");
        }

        NitroUtils.validateNameChars(name);

    }
    
    /**
     * Get the text of a model note.  If the note contains multiple lines, the returned text will have
     * a newline character at the end of each line.
     * @param note The note object
     * @param expanded Whether to expand parameter values
     * @return The text of the note
     * @throws jxthrowable
     */
    public static String getNoteText(CallNote note, boolean expanded) throws jxthrowable {
        CallStringSeq textseq = note.getText(!expanded);
        
        StringBuffer buf = new StringBuffer();
        if (textseq!=null) {
            int len = textseq.getarraysize();
            String line;
            for (int i=0; i<len; i++) {
                line = textseq.get(i);
                buf.append(line);
                buf.append('\n');
            }
        }
        return buf.toString();
    }

    /**
     * Get the text of a drawing note.  If the note contains multiple lines, the returned text will have
     * a newline character at the end of each line.
     * @param note The note object
     * @param expanded Whether to expand parameter values
     * @return The text of the note
     * @throws jxthrowable
     */
    public static String getNoteText(CallDetailNoteItem note, boolean expanded) throws jxthrowable {
        StringBuffer buf = new StringBuffer();

    	CallDetailNoteInstructions inst = note.getInstructions(!expanded);
    	if (inst==null)
    		return null;
    	
		CallDetailTextLines lines = inst.getTextLines();
		if (lines!=null) {
			int len = lines.getarraysize();
			CallDetailTextLine line;
			String txt;
			for (int i=0; i<len; i++) {
				line = lines.get(i);
				
				if (i>0)
					buf.append("\n");
				
				CallDetailTexts texts = line.getTexts();
				if (texts!=null) {
					int len2 = texts.getarraysize();
					CallDetailText textData;
					for (int k=0; k<len2; k++) {
						textData = texts.get(k);
						txt = textData.getText();
						
						buf.append(txt);
					}
				}
			}
		}
		
        return buf.length()>0 ? buf.toString() : null;
    }
    
    /**
     * Get the first block of text of a drawing note.  This is used to determine the initial 
     * formatting style for the note so that it can be copied or preserved, because drawing
     * note formatting is stored by block.  
     * 
     * <p>If the note changes formatting after the first block, that information is lost.
     * 
     * @param inst The instructions for a drawing note
     * @return The text object for the first block of text.
     * @throws jxthrowable
     */
    private CallDetailText getFirstBlock(CallDetailNoteInstructions inst) throws jxthrowable {
		CallDetailTextLines lines = inst.getTextLines();
		return getFirstBlock(lines);
    }

    /**
     * Get the first block of text of a drawing note.  This is used to determine the initial 
     * formatting style for the note so that it can be copied or preserved, because drawing
     * note formatting is stored by block.  
     * 
     * <p>If the note changes formatting after the first block, that information is lost.
     * 
     * @param lines The text lines for the drawing note
     * @return The text object for the first block of text
     * @throws jxthrowable
     */
    private CallDetailText getFirstBlock(CallDetailTextLines lines) throws jxthrowable {
		CallDetailText ret = null;
		if (lines!=null) {
			int len = lines.getarraysize();
			CallDetailTextLine line;
			for (int i=0; i<len; i++) {
				line = lines.get(i);
				
				CallDetailTexts texts = line.getTexts();
				if (texts!=null) {
					int len2 = texts.getarraysize();
					if (len2>0) {
						ret = texts.get(0);
						break;
					}
				}
				if (ret!=null)
					break;
			}
		}
		return ret;
    }

    /**
     * Copy a model note to a note in another model/drawing or to another note in the same model
     * @param note The model note to copy
     * @param m The model/drawing containing the target note
     * @param toName The name of the target note
     * @throws JLIException
     * @throws jxthrowable
     */
    private void copyOneNote(CallNote note, CallModel m, String toName) throws JLIException, jxthrowable {
		String noteName = note.getName();
		if (toName==null)
			toName = noteName;
		ModelItemType type = JlinkUtils.getNoteType(m);
		CallModelItem item = JlinkUtils.getNote(m, toName, type, false);
		if (type.getValue()==ModelItemType._ITEM_NOTE) {
			// copy a model note to a model note
	        if (item!=null) {
	        	// update an existing model note from a model note
	        	CallNote noteNew = (CallNote)item;
	            noteNew.setLines(note.getText(true));
	            noteNew.setURL(note.getURL());
	        }
	        else {
	        	// create a new model note from a model note
	            if (!(m instanceof CallSolid))
	                throw new JLIException("File '" + m.getFileName() + "' must be a solid.");
	
	            validateNoteName(toName);
	            
	            CallSolid solid = (CallSolid)m;
	            item = solid.createNote(note.getText(true), null);
	            CallNote noteNew = (CallNote)item;
	            // note: the following command crashes if the name is of the pattern "Note_#"
	            noteNew.setName(toName);
	            noteNew.setURL(note.getURL());
	        }
		}
		else if (type.getValue()==ModelItemType._ITEM_DTL_NOTE) {
			// copy a model note to a drawing note
	        if (item!=null) {
	        	// update an existing drawing note from a model note.  Copy the formatting from the first block of the drawing note
	        	CallDetailNoteItem noteNew = (CallDetailNoteItem)item;
	        	CallDetailNoteInstructions inst = noteNew.getInstructions(true);
	        	CallDetailTextLines lines = inst.getTextLines();
	        	CallDetailText oldFirstBlock = getFirstBlock(lines);
	    		lines.clear();
	        	createLines(lines, note.getText(true).toArray(), oldFirstBlock);

	    		noteNew.modify(inst);
	        }
	        else {
	        	// create a new drawing note from a model note.  Use the default formatting
	            if (!(m instanceof CallDrawing))
	                throw new JLIException("File '" + m.getFileName() + "' must be a drawing.");

	            validateNoteName(noteName);
	            
            	CallDrawing dwg = (CallDrawing)m;
            	CallDetailTextLines lines = CallDetailTextLines.create();
	        	createLines(lines, note.getText(true).toArray(), null);
            	CallDetailNoteInstructions inst = CallDetailNoteInstructions.create(lines);
            	item = dwg.createDetailItem(inst);

	            // note: the following command crashes if the name is of the pattern "Note_#"
	            item.setName(toName);
	        }
		}
    }

    /**
     * Copy a drawing note to a note in another model/drawing or to another note in the same drawing
     * @param note The drawing note to copy
     * @param m The model/drawing containing the target note
     * @param toName The name of the target note
     * @throws JLIException
     * @throws jxthrowable
     */
    public void copyOneNote(CallDetailNoteItem note, CallModel m, String toName) throws JLIException, jxthrowable {
		String noteName = note.getName();
		if (toName==null)
			toName = noteName;
		ModelItemType type = JlinkUtils.getNoteType(m);
		CallModelItem item = JlinkUtils.getNote(m, toName, type, false);
		if (type.getValue()==ModelItemType._ITEM_NOTE) {
			// copy a drawing note to a model note
			String text = getNoteText(note, false);
			String[] newlines = null;
			if (text!=null)
				newlines = text.split("\n");

			if (item!=null) {
	        	// update an existing model note from a drawing note
	        	CallNote noteNew = (CallNote)item;
	            noteNew.setLines(CallStringSeq.fromArray(newlines));
	        }
	        else {
	        	// create a new model note from a drawing note
	            if (!(m instanceof CallSolid))
	                throw new JLIException("File '" + m.getFileName() + "' must be a solid.");
	
	            validateNoteName(toName);
	            
	            CallSolid solid = (CallSolid)m;
	            item = solid.createNote(CallStringSeq.fromArray(newlines), null);
	            CallNote noteNew = (CallNote)item;
	            // note: the following command crashes if the name is of the pattern "Note_#"
	            noteNew.setName(toName);
	        }
		}
		else if (type.getValue()==ModelItemType._ITEM_DTL_NOTE) {
			// copy a drawing note to a drawing note
			String text = getNoteText(note, false);
			String[] newlines = null;
			if (text!=null)
				newlines = text.split("\n");
			
	        if (item!=null) {
	        	// update an existing drawing note from a drawing note
	        	CallDetailNoteItem targetNote = (CallDetailNoteItem)item;
	        	CallDetailNoteInstructions targetInst = targetNote.getInstructions(false);
	        	CallDetailTextLines targetLines = targetInst.getTextLines();
	        	CallDetailText oldFirstBlock = getFirstBlock(targetLines);
	    		targetLines.clear();

	    		CallDetailTextLine line;
	        	CallDetailTexts texts;
	        	CallDetailText targetText;
	        	CallDetailNoteInstructions sourceInst = note.getInstructions(false);
	        	CallDetailTextLines sourcelines = sourceInst.getTextLines();
	        	if (sourcelines!=null) {
		        	int len = sourcelines.getarraysize();
		        	for (int i=0; i<len; i++) {
		        		CallDetailTexts sourceText = sourcelines.get(i).getTexts();
		        		if (sourceText!=null) {
			        		texts = CallDetailTexts.create();
			        		int len2 = sourceText.getarraysize();
			        		for (int k=0; k<len2; k++) {
				        		targetText = CallDetailText.create(sourceText.get(k).getText());
					    		if (oldFirstBlock!=null)
					    			oldFirstBlock.copySettings(targetText);
				        		texts.append(targetText);
			        		}
			        		line = CallDetailTextLine.create(texts);
			        		targetLines.insert(i, line);
		        		}
		        	}
	        	}
	    		
	    		targetNote.modify(targetInst);
	        }
	        else {
	        	// create a new drawing note from a drawing note
	            if (!(m instanceof CallDrawing))
	                throw new JLIException("File '" + m.getFileName() + "' must be a drawing.");

	            validateNoteName(toName);
	            
            	CallDrawing dwg = (CallDrawing)m;
            	CallDetailTextLines lines = CallDetailTextLines.create();
	        	createLines(lines, newlines, null);
            	CallDetailNoteInstructions inst = CallDetailNoteInstructions.create(lines);
            	item = dwg.createDetailItem(inst);

	            // note: the following command crashes if the name is of the pattern "Note_#"
	            item.setName(toName);
	        }
		}
    }

    /**
     * Get data about a model or drawing note
     * @param item The model or drawing note object
     * @param outvals The output object
     * @param encoded Whether to return the text as a byte array
     * @param getExpandedValues Whether to expand parameter values
     * @return Whether the method was successful
     * @throws jxthrowable
     * @throws JLIException 
     */
    public static boolean getNoteInfo(CallModel model, CallModelItem item, NoteData outvals, boolean encoded, boolean getExpandedValues) throws jxthrowable, JLIException {
    	return getNoteInfo(model, item, outvals, encoded, null, getExpandedValues);
    }

    /**
     * Get data about a model or drawing note
     * @param model The model or drawing containing the note (used for drawing notes only)
     * @param item The model or drawing note object
     * @param outvals The output object
     * @param encoded Whether to return the text as a byte array
     * @param valuePtn Value pattern filter - if the value does not match the pattern, then the method will return false
     * @param getExpandedValues Whether to expand parameter values
     * @return Whether the function was successful
     * @throws jxthrowable
     * @throws JLIException 
     */
    public static boolean getNoteInfo(CallModel model, CallModelItem item, NoteData outvals, boolean encoded, Pattern valuePtn, boolean getExpandedValues) throws jxthrowable, JLIException {
        if (item instanceof CallNote) {
        	// handle a model note
        	CallNote note = (CallNote)item;
            String text = getNoteText(note, false);
            String textExpanded = null;
            if (getExpandedValues)
            	textExpanded = getNoteText(note, true);
            if (!JlinkUtils.checkValueFilter(text, valuePtn) && !JlinkUtils.checkValueFilter(textExpanded, valuePtn))
            	return false;

            if (!encoded) {
            	if (checkForFailedExpand(text, textExpanded))
            		textExpanded = text;
                encoded = NitroUtils.hasBinary(text);
            }
            if (encoded) {
                outvals.setValue(text.getBytes(Charset.forName("UTF-8")));
                if (textExpanded!=null)
                	outvals.setValueExpanded(textExpanded.getBytes(Charset.forName("UTF-8")));
            }
            else {
            	outvals.setValue(text);
            	outvals.setValueExpanded(textExpanded);
            }
            
            String url = note.getURL();
            if (url!=null && url.length()>0)
            	outvals.setUrl(url);
            
            outvals.setEncoded(encoded);
            return true;
        }
        else if (item instanceof CallDetailNoteItem) {
        	// handle a drawing note
        	CallDetailNoteItem note = (CallDetailNoteItem)item;
            String text = getNoteText(note, false);
            String textExpanded = null;
            if (getExpandedValues)
            	textExpanded = getNoteText(note, true);
            if (!JlinkUtils.checkValueFilter(text, valuePtn) && !JlinkUtils.checkValueFilter(textExpanded, valuePtn))
            	return false;
        	
            if (!encoded) {
            	if (checkForFailedExpand(text, textExpanded))
            		textExpanded = text;
                encoded = NitroUtils.hasBinary(text);
            }
            if (encoded)
                outvals.setValue(text.getBytes(Charset.forName("UTF-8")));
            else {
            	outvals.setValue(text);
            	outvals.setValueExpanded(textExpanded);
            }

            if (model!=null) {
	        	CallDetailNoteInstructions inst = note.getInstructions(true);
	        	CallDetailLeaders leaders = inst.getLeader();
	        	if (leaders!=null) {
		        	CallAttachment attach = leaders.getItemAttachment();
		        	if (attach instanceof CallFreeAttachment) {
		        		CallPoint3D loc = ((CallFreeAttachment)attach).getAttachmentPoint();
		            	CallDrawing dwg = (CallDrawing)model;
		            	int sheet = dwg.getCurrentSheetNumber();
		        		loc = JLDrawing.screenToDrawingPoint(dwg, sheet, loc);
		        		JLPoint location = JLPointMaker.create(loc);
		        		outvals.setLocation(location);
		        	}
	        	}
	        }

            outvals.setEncoded(encoded);
            return true;
        }
        return false;
    }

    /**
     * Create the lines of a drawing note
     * @param lines The text-lines object to receive the text
     * @param textArray The array of text to assign to the note, one line per entry
     * @param oldFirstBlock The first block of another note to use for formatting
     * @throws jxthrowable
     */
    private void createLines(CallDetailTextLines lines, String[] textArray, CallDetailText oldFirstBlock) throws jxthrowable {
    	CallDetailTextLine line;
    	CallDetailTexts texts;
    	CallDetailText text;
    	for (int i=0; i<textArray.length; i++) {
    		texts = CallDetailTexts.create();
    		List<String> splits = splitTextOnWhitespace(textArray[i]);
    		int len = splits.size();
    		for (int k=0; k<len; k++) {
	    		text = CallDetailText.create(splits.get(k));
	    		if (oldFirstBlock!=null)
	    			oldFirstBlock.copySettings(text);
	    		texts.append(text);
    		}
    		line = CallDetailTextLine.create(texts);
    		lines.insert(i, line);
    	}
    }
    
    /**
     * Split the text of a drawing note so that no line is greater than the maximum length
     * @param text A block of text to split
     * @return The text split into short lines
     */
    private List<String> splitTextOnWhitespace(String text) {
    	List<String> out = new ArrayList<String>();
    	while (true) {
	    	if (text.length()<DETAILTEXT_LIMIT) {
	    		out.add(text);
	    		return out;
	    	}
	    	int pos;
	    	for (pos=DETAILTEXT_LIMIT-1; text.charAt(pos)!=' ' && text.charAt(pos)!='\t'; pos--);
	    	pos++;
	    	out.add(text.substring(0, pos));
	    	text = text.substring(pos);
    	}
    }
    
    /**
     * Check whether parameter values in a note failed to expand
     * @param text The original note text before expansion
     * @param textExpanded The text after expansion
     * @return Whether any parameters failed to expand
     */
    private static boolean checkForFailedExpand(String text, String textExpanded) {
    	if (text==null || textExpanded==null)
    		return false;
    	if (text.indexOf('&')<0)
    		return false;
    	StringBuffer buf = new StringBuffer();
    	int len = text.length();
    	char c;
    	for (int i=0; i<len; i++) {
    		c = text.charAt(i);
    		if (c!='&')
    			buf.append(c);
    	}
    	if (buf.toString().equalsIgnoreCase(textExpanded))
    		return true;
    	return false;
    }

    /**
     * An implementation of ModelLooper which deletes notes in a list of models.  This calls an instance of DeleteLooper2 to delete notes in one model.
     * @author Adam Andrews
     *
     */
    private class DeleteLooper extends ModelLooper {
    	/**
    	 * The note name or pattern to delete
    	 */
    	String noteName;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			DeleteLooper2 looper = new DeleteLooper2(m);
	        looper.setNamePattern(noteName);
	        looper.loop(m);
			return false;
		}
    }

	/**
	 * An implementation of ModelItemLooper which deletes notes in a model.
	 * @author Adam Andrews
	 *
	 */
	private static class DeleteLooper2 extends ModelItemLooper {
        /**
         * Constructor which determines the note type based on the model type
         * @param model The model containing the notes
         */
        public DeleteLooper2(CallModel model) {
            setSearchType(JlinkUtils.getNoteType(model));
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallNote) {
        		CallNote note = (CallNote)item;
	            note.delete();
        	}
        	else if (item instanceof CallDetailNoteItem) {
        		CallDetailNoteItem note = (CallDetailNoteItem)item;
	            note.delete();
        	}
        	return false;
        }
	}
    
	/**
	 * An implementation of ModelItemLooper which collects information about notes in a model
	 * @author Adam Andrews
	 *
	 */
	private static class ListLooper extends ModelItemLooper {
		/**
		 * The output list of note data
		 */
		protected List<NoteData> output;
		/**
		 * Note text filter
		 */
		protected String valuePattern;
		private Pattern valuePtn;
		/**
		 * Whether to expand parameter values in the notes
		 */
		protected boolean getExpandedValues;
        /**
         * Whether to select the items that are found.
         */
        private boolean select = false;
		/**
		 * The model containing the notes
		 */
		private CallModel m;
		
        private CallSelectionBuffer buf = null;

        /**
         * Constructor which determines the note type based on the model type
         * @param model The model containing the notes
         */
        public ListLooper(CallModel m, CallSession session, boolean select) throws jxthrowable {
        	ModelItemType type = JlinkUtils.getNoteType(m);
            setSearchType(type);
            this.m=m;
        	this.select=select;
        	if (select) {
				buf = session.getCurrentSelectionBuffer();
				CallSelections sels = buf.getContents();
				if (sels!=null) {
					int len = sels.getarraysize();
					buf.clear();
				}
        	}
        }
        
        /**
         * @param valuePattern The note text filter
         */
        public void setValuePattern(String valuePattern) {
        	if (valuePattern!=null) {
                this.valuePattern = NitroUtils.transformPattern(valuePattern.toLowerCase());
                this.valuePtn = Pattern.compile(this.valuePattern);
        	}
        }

        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
            if (currentName==null)
                currentName = item.getName();
            
            if (output==null)
                output = new Vector<NoteData>();

            boolean encoded = false;
            NoteData outvals = new NoteData();
            outvals.setName(currentName==null?badPrefix+item.getId():currentName);
            if (!getNoteInfo(m, item, outvals, encoded, valuePtn, getExpandedValues))
            	return false;

            if (select) {
            	CallSelection sel = CallSelection.createModelItemSelection(item, null);
            	buf.addSelection(sel);
            }
            output.add(outvals);
        	return false;
        }
	}
    
	/**
	 * An implementation of ModelItemLooper which checks whether a note exists in a model
	 * @author Adam Andrews
	 *
	 */
	private static class ExistsLooper extends ModelItemLooper {
		/**
		 * Output flag indicating whether the note(s) exists
		 */
		boolean exists = false;
		
        /**
         * Constructor which determines the note type based on the model type
         * @param model The model containing the notes
         */
        public ExistsLooper(CallModel m) {
            setSearchType(JlinkUtils.getNoteType(m));
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallNote || item instanceof CallDetailNoteItem) {
        		exists=true;
        		return true;
        	}
        	return false;
        }
	}

    /**
     * An implementation of ModelLooper which creates or updates a note on a list of models
     * @author Adam Andrews
     *
     */
    public class SetLooper extends ModelLooper {
    	/**
    	 * The name of the note to set
    	 */
    	String noteName;
    	/**
    	 * The lines of text for the note as a JLink string sequence
    	 */
    	CallStringSeq textseq;
    	/**
    	 * The lines of text for the note as a Java string array
    	 */
    	String[] textArray;
    	/**
    	 * The location for the note (drawing notes only)
    	 */
    	JLPoint location;
    	
		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			ModelItemType type = JlinkUtils.getNoteType(m);
			if (type.getValue()==ModelItemType._ITEM_NOTE) {
				// set a model note
				CallModelItem item = JlinkUtils.getNote(m, noteName, type, false);

	        	if (item!=null) {
	        		// update an existing model note
		        	CallNote note = (CallNote)item;
		            note.setLines(textseq);
		        }
		        else {
		        	// create a new model note
		            if (!(m instanceof CallSolid))
		                throw new JLIException("File '" + m.getFileName() + "' must be a solid.");

		            validateNoteName(noteName);
		            
		            CallSolid solid = (CallSolid)m;
		            item = solid.createNote(textseq, null);
		            // note: the following command crashes if the name is of the pattern "Note_#"
		            item.setName(noteName);
		        }
			}
			else if (type.getValue()==ModelItemType._ITEM_DTL_NOTE) {
				// handle a drawing note
				CallModelItem item = JlinkUtils.getNote(m, noteName, type, false);

		        if (item!=null) {
		        	// update an existing drawing note
		        	CallDetailNoteItem note = (CallDetailNoteItem)item;
		        	CallDetailNoteInstructions inst = note.getInstructions(true);
		        	CallDetailTextLines lines = inst.getTextLines();
		        	CallDetailText oldFirstBlock = getFirstBlock(lines);
		    		lines.clear();
		        	createLines(lines, textArray, oldFirstBlock);
	            	if (location!=null) {
		            	CallDrawing dwg = (CallDrawing)m;
	                	CallPoint3D loc = JLPointMaker.create3D(location);
	                	int sheet = dwg.getCurrentSheetNumber();
	        	        loc = JLDrawing.drawingToScreenPoint(dwg, sheet, loc);
	            		CallFreeAttachment position = CallFreeAttachment.create(loc);

	            		CallDetailLeaders leaders = CallDetailLeaders.create();
	            		leaders.setItemAttachment(position);

	                	inst.setLeader(leaders);
	            	}
		        	
		    		note.modify(inst);
		        }
		        else {
		        	// create a new drawing note
		            if (!(m instanceof CallDrawing))
		                throw new JLIException("File '" + m.getFileName() + "' must be a drawing.");

		            validateNoteName(noteName);
		            
	            	CallDrawing dwg = (CallDrawing)m;
	            	CallDetailTextLines lines = CallDetailTextLines.create();
	            	createLines(lines, textArray, null);
	            	CallDetailNoteInstructions inst = CallDetailNoteInstructions.create(lines);
	            	if (location!=null) {
	                	CallPoint3D loc = JLPointMaker.create3D(location);
	                	int sheet = dwg.getCurrentSheetNumber();
	        	        loc = JLDrawing.drawingToScreenPoint(dwg, sheet, loc);
	            		CallFreeAttachment position = CallFreeAttachment.create(loc);

	            		CallDetailLeaders leaders = CallDetailLeaders.create();
	            		leaders.setItemAttachment(position);

	                	inst.setLeader(leaders);
	            	}
	            	inst.setIsDisplayed(Boolean.TRUE);
	            	item = dwg.createDetailItem(inst);

		            // note: the following command crashes if the name is of the pattern "Note_#"
		            item.setName(noteName);
		            
		            if (item instanceof CallDetailNoteItem) {
		            	// if you try to Show a note on a drawing that isn't being displayed,
		            	// you get a General Error from Creo.
		            	if (getSession().getModelWindow(m)!=null) {
		            		((CallDetailNoteItem)item).show();
		            	}
		            }
		        }
			}
			return false;
		}

    }

	/**
	 * An implementation of ModelItemLooper which just collects note objects for a model/drawing
	 * @author Adam Andrews
	 *
	 */
	private static class BasicListLooper extends ModelItemLooper {
    	/**
    	 * An output list of model notes
    	 */
    	List<CallNote> modelNotes = null;
    	/**
    	 * An output list of drawing notes
    	 */
    	List<CallDetailNoteItem> drawingNotes = null;
		
        /**
         * Constructor which determines the note type based on the model type
         * @param model The model containing the notes
         */
        public BasicListLooper(CallModel m) {
            setSearchType(JlinkUtils.getNoteType(m));
        }
        
        /* (non-Javadoc)
         * @see com.simplifiedlogic.nitro.util.ModelItemLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem)
         */
        public boolean loopAction(CallModelItem item) throws JLIException, jxthrowable {
        	if (item instanceof CallNote) {
                if (modelNotes==null)
                	modelNotes = new ArrayList<CallNote>();
                modelNotes.add((CallNote)item);
        	}
        	else if (item instanceof CallDetailNoteItem) {
                if (drawingNotes==null)
                	drawingNotes = new ArrayList<CallDetailNoteItem>();
                drawingNotes.add((CallDetailNoteItem)item);
        	}
        	return false;
        }
	}

    /**
     * An implementation of ModelLooper which copies notes to a list of models
     * @author Adam Andrews
     *
     */
    public class CopyLooper extends ModelLooper {
		/**
		 * A list of model notes to copy
		 */
		List<CallNote> modelInputs;
		/**
		 * A list of drawing notes to copy
		 */
		List<CallDetailNoteItem> drawingInputs;
		/**
		 * New name for the note.  If more than one note is being copied, this parameter is ignored.
		 */
		String toName;
		/**
		 * Name of a model to skip over when looping.  Intended to use to skip over the model that currently contains the note.
		 */
		String exceptFileName;

		/* (non-Javadoc)
		 * @see com.simplifiedlogic.nitro.util.ModelLooper#loopAction(com.simplifiedlogic.nitro.jlink.calls.model.CallModel)
		 */
		@Override
		public boolean loopAction(CallModel m) throws JLIException, jxthrowable {
			int cnt=0;
			if (modelInputs!=null)
				cnt += modelInputs.size();
			if (drawingInputs!=null)
				cnt += drawingInputs.size();
			if (cnt>1) {
				toName=null;
			}
			if (exceptFileName!=null && m.getFileName().equals(exceptFileName)) {
				if (namePattern!=null && !namePattern.equalsIgnoreCase(m.getFileName()))
				return false;
			}

			if (modelInputs!=null) {
				// copy the model notes
				for (CallNote note : modelInputs) {
					copyOneNote(note, m, toName);
				}
			}
			if (drawingInputs!=null) {
				// copy the drawing notes
				for (CallDetailNoteItem note : drawingInputs) {
					copyOneNote(note, m, toName);
				}
			}
			return false;
		}
    }
}
