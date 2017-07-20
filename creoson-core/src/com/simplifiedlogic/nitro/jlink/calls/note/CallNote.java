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
package com.simplifiedlogic.nitro.jlink.calls.note;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.ptc.pfc.pfcDisplay.GraphicsMode;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcNote.Note;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.calls.modelitem.CallModelItem;
import com.simplifiedlogic.nitro.jlink.calls.seq.CallStringSeq;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcNote.Note
 * 
 * @author Adam Andrews
 *
 */
public class CallNote extends CallModelItem {

	public CallNote(Note note) {
		super((ModelItem)note);
	}
	
	public String getURL() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Note,GetURL", 0, NitroConstants.DEBUG_JLINK_KEY);
		return getNote().GetURL();
	}
	
	public void setURL(String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Note,SetURL", 0, NitroConstants.DEBUG_JLINK_KEY);
		getNote().SetURL(value);
	}
	
	public CallStringSeq getText(boolean giveParametersAsName) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Note,GetText", 0, NitroConstants.DEBUG_JLINK_KEY);
		stringseq text = getNote().GetText(giveParametersAsName);
		if (text==null)
			return null;
		return new CallStringSeq(text);
	}
	
	public CallStringSeq getLines() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Note,GetLines", 0, NitroConstants.DEBUG_JLINK_KEY);
		stringseq text = getNote().GetLines();
		if (text==null)
			return null;
		return new CallStringSeq(text);
	}
	
	public void setLines(CallStringSeq text) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Note,SetLines", 0, NitroConstants.DEBUG_JLINK_KEY);
		getNote().SetLines(text!=null ? text.getSeq() : null);
	}
	
	public void delete() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Note,Delete", 0, NitroConstants.DEBUG_JLINK_KEY);
		getNote().Delete();
	}
	
	public void display(GraphicsMode mode) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("Note,Display", 0, NitroConstants.DEBUG_JLINK_KEY);
		getNote().Display(mode);
	}
	
	public Note getNote() {
		return (Note)item;
	}
}
