/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.calls.detail;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.Attachment;
import com.ptc.pfc.pfcDetail.AttachmentType;
import com.ptc.pfc.pfcDetail.FreeAttachment;
import com.ptc.pfc.pfcDetail.OffsetAttachment;
import com.ptc.pfc.pfcDetail.ParametricAttachment;
import com.ptc.pfc.pfcDetail.UnsupportedAttachment;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.Attachment
 * 
 * @author Adam Andrews
 *
 */
public class CallAttachment {

	protected Attachment attachment;

	protected CallAttachment(Attachment attachment) {
		this.attachment = attachment;
	}
	
	public AttachmentType getType() throws jxthrowable {
		return null;
	}

	public static CallAttachment create(Attachment attachment) {
		if (attachment==null)
			return null;
		if (attachment instanceof FreeAttachment) 
			return CallFreeAttachment.createAttachment((FreeAttachment)attachment);
		else if (attachment instanceof OffsetAttachment) 
			return CallOffsetAttachment.createAttachment((OffsetAttachment)attachment);
		else if (attachment instanceof ParametricAttachment) 
			return CallParametricAttachment.createAttachment((ParametricAttachment)attachment);
		else if (attachment instanceof UnsupportedAttachment) 
			return CallUnsupportedAttachment.createAttachment((UnsupportedAttachment)attachment);
		else
			return new CallAttachment(attachment);
	}

	public Attachment getAttachment() {
		return attachment;
	}
}
