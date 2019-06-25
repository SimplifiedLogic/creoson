package com.simplifiedlogic.nitro.jlink.calls.feature;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.FeatureGroup;
import com.ptc.pfc.pfcFeature.Features;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

public class CallFeatureGroup {

	protected FeatureGroup group;
	
	public CallFeatureGroup(FeatureGroup group) {
		this.group = group;
	}

	public String getGroupName() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeatureGroup,GetGroupName", 0, NitroConstants.DEBUG_JLINK_KEY);
		return group.GetGroupName();
	}
	
	public CallFeatures listMembers() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeatureGroup,ListMembers", 0, NitroConstants.DEBUG_JLINK_KEY);
		Features feats = getGroup().ListMembers();
		if (feats==null)
			return null;
		return new CallFeatures(feats);
	}
	
	public FeatureGroup getGroup() {
		return group;
	}
}
