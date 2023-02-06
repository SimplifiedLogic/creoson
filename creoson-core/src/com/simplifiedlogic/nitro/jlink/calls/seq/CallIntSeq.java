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
package com.simplifiedlogic.nitro.jlink.calls.seq;

import com.ptc.cipjava.intseq;
import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;

/**
 * Wrapper for JLink's com.ptc.cipjava.intseq
 * 
 * @author Adam Andrews
 *
 */
public class CallIntSeq {

	private intseq seq;
	
	public CallIntSeq(intseq seq) {
		this.seq = seq;
	}

	public static CallIntSeq create() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("intseq,create", 0, NitroConstants.DEBUG_JLINK_KEY);
        intseq args = intseq.create();
		if (args==null)
			return null;
		return new CallIntSeq(args);
	}
	
	public void append(int id) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("intseq,append", 0, NitroConstants.DEBUG_JLINK_KEY);
		seq.append(id);
	}
	
	public int getarraysize() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("intseq,getarraysize", 0, NitroConstants.DEBUG_JLINK_KEY);
		return seq.getarraysize();
	}
	
	public int get(int idx) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("intseq,get(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        return seq.get(idx);
	}

	public void set(int idx, int id) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("intseq,set(" + idx + ")", 0, NitroConstants.DEBUG_JLINK_KEY);
        seq.set(idx, id);
	}

	public intseq getSeq() {
		return seq;
	}

}
