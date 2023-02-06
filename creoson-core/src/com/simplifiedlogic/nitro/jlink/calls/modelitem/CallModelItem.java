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
package com.simplifiedlogic.nitro.jlink.calls.modelitem;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcDetail.DetailNoteItem;
import com.ptc.pfc.pfcDetail.DetailSymbolDefItem;
import com.ptc.pfc.pfcDetail.DetailSymbolInstItem;
import com.ptc.pfc.pfcDimension.Dimension;
import com.ptc.pfc.pfcDimension2D.Dimension2D;
import com.ptc.pfc.pfcFeature.Feature;
import com.ptc.pfc.pfcGeometry.CoordSystem;
import com.ptc.pfc.pfcGeometry.Surface;
import com.ptc.pfc.pfcLayer.Layer;
import com.ptc.pfc.pfcModelItem.ModelItem;
import com.ptc.pfc.pfcNote.Note;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailNoteItem;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailSymbolDefItem;
import com.simplifiedlogic.nitro.jlink.calls.detail.CallDetailSymbolInstItem;
import com.simplifiedlogic.nitro.jlink.calls.dimension.CallDimension;
import com.simplifiedlogic.nitro.jlink.calls.dimension2d.CallDimension2D;
import com.simplifiedlogic.nitro.jlink.calls.feature.CallFeature;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallCoordSystem;
import com.simplifiedlogic.nitro.jlink.calls.geometry.CallSurface;
import com.simplifiedlogic.nitro.jlink.calls.layer.CallLayer;
import com.simplifiedlogic.nitro.jlink.calls.note.CallNote;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

/**
 * Wrapper for JLink's com.ptc.pfc.pfcModelItem.ModelItem
 * 
 * @author Adam Andrews
 *
 */
public class CallModelItem {

	protected ModelItem item;
	
	protected CallModelItem(ModelItem item) {
		this.item = item;
	}
	
	public int getId() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelItem,GetId", 0, NitroConstants.DEBUG_JLINK_KEY);
		return item.GetId();
	}
	
	public String getName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelItem,GetName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return item.GetName();
	}
	
	public void setName(String name) throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("ModelItem,SetName", 0, NitroConstants.DEBUG_JLINK_KEY);
		item.SetName(name);
	}
	
	public static CallModelItem create(ModelItem item) {
		if (item==null)
			return null;
		if (item instanceof Feature) 
			return CallFeature.createFeature((Feature)item);
		else if (item instanceof Dimension)
			return new CallDimension((Dimension)item);
		else if (item instanceof Dimension2D)
			return new CallDimension2D((Dimension2D)item);
		else if (item instanceof CoordSystem)
			return new CallCoordSystem((CoordSystem)item);
		else if (item instanceof Layer)
			return new CallLayer((Layer)item);
		else if (item instanceof Note)
			return new CallNote((Note)item);
		else if (item instanceof DetailNoteItem)
			return new CallDetailNoteItem((DetailNoteItem)item);
		else if (item instanceof DetailSymbolDefItem)
			return new CallDetailSymbolDefItem((DetailSymbolDefItem)item);
		else if (item instanceof DetailSymbolInstItem)
			return new CallDetailSymbolInstItem((DetailSymbolInstItem)item);
		else if (item instanceof Surface)
			return new CallSurface((Surface)item);
		else
			return new CallModelItem(item);
	}
	
	public ModelItem getItem() {
		return item;
	}
}
