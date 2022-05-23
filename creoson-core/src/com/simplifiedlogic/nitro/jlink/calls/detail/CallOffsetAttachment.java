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
import com.ptc.pfc.pfcBase.Point3D;
import com.ptc.pfc.pfcDetail.Attachment;
import com.ptc.pfc.pfcDetail.AttachmentType;
import com.ptc.pfc.pfcDetail.OffsetAttachment;
import com.ptc.pfc.pfcDetail.pfcDetail;
import com.ptc.pfc.pfcSelect.Selection;
import com.simplifiedlogic.nitro.jlink.calls.base.CallPoint3D;
import com.simplifiedlogic.nitro.jlink.calls.select.CallSelection;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcDetail.OffsetAttachment
 * 
 * @author Adam Andrews
 *
 */
public class CallOffsetAttachment extends CallAttachment {

	public CallOffsetAttachment(OffsetAttachment attachment) {
		super((Attachment)attachment);
	}

	public static CallOffsetAttachment create(CallSelection inAttachedGeometry, CallPoint3D inAttachmentPoint) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("pfcDetail,OffsetAttachment_Create", 0, NitroConstants.DEBUG_JLINK_KEY);
        OffsetAttachment attachment= pfcDetail.OffsetAttachment_Create(inAttachedGeometry.getSel(), inAttachmentPoint.getPoint());
		if (attachment==null)
			return null;
		return new CallOffsetAttachment(attachment);
	}

	public static CallOffsetAttachment createAttachment(OffsetAttachment attachment) {
		if (attachment==null)
			return null;
		return new CallOffsetAttachment(attachment);
	}

	public CallPoint3D getAttachmentPoint() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("OffsetAttachment,GetAttachmentPoint", 0, NitroConstants.DEBUG_JLINK_KEY);
		Point3D pt = getOffsetAttachment().GetAttachmentPoint();
		if (pt==null)
			return null;
		return new CallPoint3D(pt);
	}

	public CallSelection getAttachedGeometry() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("OffsetAttachment,GetAttachedGeometry", 0, NitroConstants.DEBUG_JLINK_KEY);
		Selection sel = getOffsetAttachment().GetAttachedGeometry();
		if (sel==null)
			return null;
		return new CallSelection(sel);
	}
	
	public AttachmentType getType() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("OffsetAttachment,GetType", 0, NitroConstants.DEBUG_JLINK_KEY);
		if (attachment==null)
			return null;
		return attachment.GetType();
	}

	public OffsetAttachment getOffsetAttachment() {
		return (OffsetAttachment)getAttachment();
	}
}
