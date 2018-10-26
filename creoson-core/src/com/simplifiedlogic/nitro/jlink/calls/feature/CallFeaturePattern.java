package com.simplifiedlogic.nitro.jlink.calls.feature;

import com.ptc.cipjava.jxthrowable;
import com.ptc.pfc.pfcFeature.FeaturePattern;
import com.ptc.pfc.pfcFeature.Features;
import com.simplifiedlogic.nitro.jlink.impl.NitroConstants;
import com.simplifiedlogic.nitro.jlink.intf.DebugLogging;

public class CallFeaturePattern {

	private FeaturePattern pattern;
	
	public CallFeaturePattern(FeaturePattern pattern) {
		this.pattern = pattern;
	}
	
	public CallFeatures listMembers() throws jxthrowable {
        if (NitroConstants.DEBUG_JLINK) DebugLogging.sendTimerMessage("FeaturePattern,ListMembers", 0, NitroConstants.DEBUG_JLINK_KEY);
		Features feats = getPattern().ListMembers();
		if (feats==null)
			return null;
		return new CallFeatures(feats);
	}

	public FeaturePattern getPattern() {
		return pattern;
	}
}
