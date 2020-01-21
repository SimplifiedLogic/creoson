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
package com.simplifiedlogic.nitro.jlink.calls.seq;

import com.ptc.cipjava.jxthrowable;
import com.ptc.cipjava.stringseq;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.cipjava.stringseq
 * 
 * @author Adam Andrews
 *
 */
public class CallStringSeq {

	private stringseq seq;
	
	public CallStringSeq(stringseq seq) {
		this.seq = seq;
	}

	public static CallStringSeq create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("stringseq,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        stringseq args = stringseq.create();
		if (args==null)
			return null;
		return new CallStringSeq(args);
	}
	
	public void append(String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("stringseq,append", 0, NitroConstants.DEBUG_JLINK_KEY);
		seq.append(value);
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("stringseq,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return seq.getarraysize();
	}
	
	public String get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("stringseq,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        return seq.get(idx);
	}

	public void set(int idx, String value) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("stringseq,set(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        seq.set(idx, value);
	}

    public String[] toArray() throws jxthrowable {
		int len = seq.getarraysize();
		String[] out = new String[len];
		for (int i=0; i<len; i++) {
			out[i] = seq.get(i);
		}
		return out;
    }
    
    public static CallStringSeq fromArray(String[] array) throws jxthrowable {
    	if (array==null)
    		return null;
    	CallStringSeq seq = CallStringSeq.create();
    	for (int i=0; i<array.length; i++) {
    		seq.append(array[i]);
    	}
    	return seq;
    }

    public stringseq getSeq() {
		return seq;
	}

}
