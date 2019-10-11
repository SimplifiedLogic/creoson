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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.intf.IJLFeature;
import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.jshell.json.request.JLFeatureRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLParameterRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFeatureResponseParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLParameterResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "feature" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonFeatureHelp extends JLJsonCommandHelp implements JLFeatureRequestParams, JLFeatureResponseParams {

	public static final String OBJ_FEATURE_DATA = "FeatureData";
	
	private String[] validStatuses;
	private Map<String, Map<String, Object>> sampleFeatures;
	
	/**
	 * 
	 */
	public JLJsonFeatureHelp() {
		validStatuses = new String[] {
			IJLFeature.STATUS_ACTIVE,
			IJLFeature.STATUS_INACTIVE,
			IJLFeature.STATUS_FAMILY_TABLE_SUPPRESSED,
			IJLFeature.STATUS_SIMP_REP_SUPPRESSED,
			IJLFeature.STATUS_PROGRAM_SUPPRESSED,
			IJLFeature.STATUS_SUPPRESSED,
			IJLFeature.STATUS_UNREGENERATED
		};
		
		sampleFeatures = new HashMap<String, Map<String, Object>>();

    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_NAME, "CS3");
    	rec.put(PARAM_TYPE, "COORDINATE SYSTEM");
		rec.put(PARAM_STATUS, "ACTIVE");
    	sampleFeatures.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_NAME, "CS0");
    	rec.put(PARAM_TYPE, "COORDINATE SYSTEM");
		rec.put(PARAM_STATUS, "ACTIVE");
    	sampleFeatures.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_NAME, "PLN_34");
    	rec.put(PARAM_TYPE, "DATUM_PLANE");
		rec.put(PARAM_STATUS, "SUPPRESSED");
    	sampleFeatures.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_NAME, "FLAT_3");
    	rec.put(PARAM_TYPE, "FLAT");
		rec.put(PARAM_STATUS, "ACTIVE");
    	sampleFeatures.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_NAME, "CURVE_7");
    	rec.put(PARAM_TYPE, "CURVE");
		rec.put(PARAM_STATUS, "SUPPRESSED");
    	sampleFeatures.put((String)rec.get(PARAM_NAME), rec);

    	rec = new OrderedMap<String, Object>();
    	rec.put(PARAM_NAME, "wheel.prt");
    	rec.put(PARAM_TYPE, "COMPONENT");
		rec.put(PARAM_STATUS, "ACTIVE");
    	sampleFeatures.put((String)rec.get(PARAM_NAME), rec);

	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpDelete());
		list.add(helpDeleteParam());
		list.add(helpList());
		list.add(helpListParams());
		list.add(helpListGroupFeatures());
		list.add(helpListPatternFeatures());
		list.add(helpParamExists());
		list.add(helpRename());
		list.add(helpResume());
		list.add(helpSetParam());
		list.add(helpSuppress());
		list.add(helpUserSelectCsys());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpFeatureData());
		return list;
	}
	
	private FunctionTemplate helpDelete() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete one or more features that match criteria");
    	spec.addFootnote("Will only delete visible features.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name; only used if " + PARAM_NAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_NAMES + " parameter is used; if both are empty, then all features may be deleted");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of feature names");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty, then all features may be deleted");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_STATUS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature status pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature statuses");
    	arg.setValidValues(validStatuses);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature types");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CLIP, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to clip-delete ANY features from this feature through the end of the structure");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "CS3");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	ex.addInput(PARAM_CLIP, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAMES, new String[] {"PLN_34", "CS3", "FLAT_3"});
    	ex.addInput(PARAM_CLIP, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_STATUS, "*SUPPRESSED*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	ex.addInput(PARAM_STATUS, "*SUPPRESSED*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_TYPE, "DATUM_*");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List features that match criteria");
    	spec.addFootnote("Will only list visible features.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All features are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_STATUS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature status pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature statuses");
    	arg.setValidValues(validStatuses);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature types");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PATHS, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether feature ID and feature number are returned with the data");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NO_DATUM, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to exclude datum-type features from the list; these are COORD_SYS, CURVE, DATUM_AXIS, DATUM_PLANE, DATUM_POINT, DATUM_QUILT, and DATUM_SURFACE features.");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INC_UNNAMED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to include unnamed features in the list");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NO_COMP, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to include component-type features in the list");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FEATLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_FEATURE_DATA);
    	ret.setDescription("List of feature information");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "CS3");
    	List<Map<String, Object>> feats = new ArrayList<Map<String, Object>>();
    	feats.add(sampleFeatures.get("CS3"));
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	ex.addInput(PARAM_PATHS, true);
    	feats = new ArrayList<Map<String, Object>>();
    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.putAll(sampleFeatures.get("PLN_34"));
    	rec.put(OUTPUT_ID, 735);
    	rec.put(OUTPUT_FEATNO, 5);
    	feats.add(rec);
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAMES, new String[] {"PLN_34", "CS3", "FLAT_3"});
    	feats = new ArrayList<Map<String, Object>>();
    	feats.add(sampleFeatures.get("PLN_34"));
    	feats.add(sampleFeatures.get("CS3"));
    	feats.add(sampleFeatures.get("FLAT_3"));
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_STATUS, "*SUPPRESSED*");
    	feats = new ArrayList<Map<String, Object>>();
    	feats.add(sampleFeatures.get("PLN_34"));
    	feats.add(sampleFeatures.get("CURVE_7"));
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	ex.addInput(PARAM_STATUS, "*SUPPRESSED*");
    	feats = new ArrayList<Map<String, Object>>();
    	feats.add(sampleFeatures.get("PLN_34"));
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_TYPE, "DATUM_*");
    	feats = new ArrayList<Map<String, Object>>();
    	feats.add(sampleFeatures.get("PLN_34"));
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_TYPE, "CURVE");
    	ex.addInput(PARAM_PATHS, true);
    	ex.addInput(PARAM_INC_UNNAMED, true);
    	feats = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	rec.putAll(sampleFeatures.get("CURVE_7"));
    	rec.put(OUTPUT_ID, 601);
    	rec.put(OUTPUT_FEATNO, 23);
    	feats.add(rec);
    	rec = new OrderedMap<String, Object>();
    	rec.putAll(sampleFeatures.get("CURVE_7"));
    	rec.remove(PARAM_NAME);
    	rec.put(PARAM_STATUS, "ACTIVE");
    	rec.put(OUTPUT_ID, 620);
    	rec.put(OUTPUT_FEATNO, 25);
    	feats.add(rec);
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_STATUS, "ACTIVE");
    	feats = new ArrayList<Map<String, Object>>();
    	feats.add(sampleFeatures.get("CS3"));
    	feats.add(sampleFeatures.get("CS0"));
    	feats.add(sampleFeatures.get("FLAT_3"));
    	feats.add(sampleFeatures.get("wheel.prt"));
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_STATUS, "ACTIVE");
    	ex.addInput(PARAM_NO_COMP, true);
    	feats = new ArrayList<Map<String, Object>>();
    	feats.add(sampleFeatures.get("CS3"));
    	feats.add(sampleFeatures.get("CS0"));
    	feats.add(sampleFeatures.get("FLAT_3"));
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpFeatureData() {
    	FunctionObject obj = new FunctionObject(OBJ_FEATURE_DATA);
    	obj.setDescription("Information about a feature");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_STATUS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature status");
    	arg.setValidValues(validStatuses);
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Feature ID");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_FEATNO, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Feature Number");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpListPatternFeatures() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_PATTERN_FEATURES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List features in a Creo Pattern");
    	spec.addFootnote("Will only list visible features.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PATTERN_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Pattern name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature types");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FEATLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_FEATURE_DATA);
    	ret.setDescription("List of feature information");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_PATTERN_NAME, "PIN_PAT");
    	List<Map<String, Object>> feats = new ArrayList<Map<String, Object>>();
    	Map<String, Object> rec;
    	for (int i=0; i<5; i++) {
        	rec = new OrderedMap<String, Object>();
        	rec.put(PARAM_NAME, "PIN_CSYS");
        	rec.put(PARAM_TYPE, "COORDINATE SYSTEM");
    		rec.put(PARAM_STATUS, "ACTIVE");
    		rec.put(OUTPUT_ID, 57+i);
    		rec.put(OUTPUT_FEATNO, 5+i);
    		feats.add(rec);
    	}
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListGroupFeatures() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_GROUP_FEATURES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List features in a Creo Group");
    	spec.addFootnote("Will only list visible features.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_GROUP_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Group name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature types");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FEATLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_FEATURE_DATA);
    	ret.setDescription("List of feature information");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_GROUP_NAME, "HOLE_GRP");
    	List<Map<String, Object>> feats = new ArrayList<Map<String, Object>>();
    	Map<String, Object> rec;
    	for (int i=0; i<5; i++) {
        	rec = new OrderedMap<String, Object>();
        	rec.put(PARAM_NAME, "HOLE_CSYS");
        	rec.put(PARAM_TYPE, "COORDINATE SYSTEM");
    		rec.put(PARAM_STATUS, "ACTIVE");
    		rec.put(OUTPUT_ID, 21+i);
    		rec.put(OUTPUT_FEATNO, 12+i);
    		feats.add(rec);
    	}
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpRename() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_RENAME);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Rename a feature");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Feature ID");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name; only used if " + PARAM_ID + " is not given");
//    	arg.setDefaultValue("The " + PARAM_ID + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NEWNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New name for the feature");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_ID, 620);
    	ex.addInput(PARAM_NEWNAME, "CURVE_7b");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "CS3");
    	ex.addInput(PARAM_NEWNAME, "CS3b");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpResume() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_RESUME);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Resume one or more features that match criteria");
    	spec.addFootnote("Will only resume visible features.");
    	spec.addFootnote("There are 3 ways of specifying features; "+PARAM_ID+", "+PARAM_NAMES+", and "+PARAM_NAME+" in that order.  If none of these are given, then all features may be resumed.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Feature ID");
    	arg.setDefaultValue("The " + PARAM_NAME + " or " + PARAM_NAMES + " parameters are used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of feature names; only used if "+PARAM_ID+" is not given");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty and "+PARAM_ID+" is not given, then all features may be resumed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name; only used if " + PARAM_NAMES + " and "+PARAM_ID+" are not given");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_STATUS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature status pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature statuses");
    	arg.setValidValues(validStatuses);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature types");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_WCHILDREN, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to resume any child features of the resumed feature");
    	arg.setDefaultValue("true");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "CS3");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_STATUS, "*SUPPRESSED*");
    	ex.addInput(PARAM_WCHILDREN, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	ex.addInput(PARAM_STATUS, "*SUPPRESSED*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_TYPE, "DATUM_*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc123.asm");
    	ex.addInput(PARAM_NAME, "bolt.prt");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc123.asm");
    	ex.addInput(PARAM_NAMES, new String[] {"bolt.prt", "nut.prt"});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpSuppress() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SUPPRESS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Suppress one or more features that match criteria");
    	spec.addFootnote("Will only suppress visible features.");
    	spec.addFootnote("There are 3 ways of specifying features; "+PARAM_ID+", "+PARAM_NAMES+", and "+PARAM_NAME+" in that order.  If none of these are given, then all features may be suppressed.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Feature ID");
    	arg.setDefaultValue("The " + PARAM_NAME + " or " + PARAM_NAMES + " parameters are used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of feature names; only used if "+PARAM_ID+" is not given");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty and "+PARAM_ID+" is not given, then all features may be suppressed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name; only used if " + PARAM_NAMES + " and "+PARAM_ID+" are not given");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_STATUS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature status pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature statuses");
    	arg.setValidValues(validStatuses);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature types");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CLIP, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to clip-suppress ANY features from this feature through the end of the structure");
    	arg.setDefaultValue("true");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_WCHILDREN, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to suppress any child features of the suppressed feature");
    	arg.setDefaultValue("true");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "CS3");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	ex.addInput(PARAM_CLIP, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_STATUS, "*ACTIVE*");
    	ex.addInput(PARAM_WCHILDREN, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_*");
    	ex.addInput(PARAM_STATUS, "ACTIVE");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_TYPE, "DATUM_*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc123.asm");
    	ex.addInput(PARAM_NAME, "bolt.prt");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc123.asm");
    	ex.addInput(PARAM_NAMES, new String[] {"bolt.prt", "nut.prt"});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpDeleteParam() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_PARAM);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a feature parameter");
    	spec.addFootnote("Will only delete parameters from visible features.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature names");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PARAM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All parameter names");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "FLAT_3");
    	ex.addInput(PARAM_PARAM, "ALPHA");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "CS*");
    	ex.addInput(PARAM_PARAM, "LM*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc*.asm");
    	ex.addInput(PARAM_PARAM, "MFG_ID");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpParamExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_PARAM_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether parameter(s) exists on a feature");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PARAM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name; only used if " + PARAM_PARAMS + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_PARAMS + " parameter is used; if both are empty, then it checks for any parameter's existence");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PARAMS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of parameter names");
    	arg.setDefaultValue("The " + PARAM_PARAM + " parameter is used; if both are empty, then it checks for any parameter's existence");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the parameter exists on the model");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_34");
    	ex.addInput(PARAM_PARAM, "MFG_ID");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLN_34");
    	ex.addInput(PARAM_PARAM, "PTC*");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "CS3");
    	ex.addInput(PARAM_PARAMS, new String[] {"LM_LENGTH","LM_WIDTH","LM_HEIGHT"});
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListParams() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_PARAMS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List feature parameters that match criteria");
    	spec.addFootnote("Will only list parameters on visible features.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Feature ID");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name; only used if " + PARAM_ID + " is not given");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All features are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature type pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All feature types");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NO_DATUM, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to exclude datum-type features from the list; these are COORD_SYS, CURVE, DATUM_AXIS, DATUM_PLANE, DATUM_POINT, DATUM_QUILT, and DATUM_SURFACE features.");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INC_UNNAMED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to include unnamed features in the list");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NO_COMP, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to include component-type features in the list");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PARAM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name; only used if " + PARAM_PARAMS + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_PARAMS + " parameter is used; if both are empty, then all parameters are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PARAMS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of parameter names");
    	arg.setDefaultValue("The " + PARAM_PARAM + " parameter is used; if both are empty, then all parameters are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(JLParameterRequestParams.PARAM_VALUE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter value filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(JLParameterRequestParams.PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return the values Base64-encoded");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_PARAMLIST, FunctionSpec.TYPE_OBJARRAY, JLJsonParameterHelp.OBJ_PARAMETER_DATA);
    	ret.setDescription("List of parameter information");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "TEST*");
    	ex.addInput(PARAM_PARAM, "PTC*");
    	Map<String, Object> rec;
    	List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "PTC_BRAND");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_STRING);
		rec.put(JLParameterRequestParams.PARAM_VALUE, "ALPHA");
		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "TEST_1");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "CURVE");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 121);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "PTC_LIFETIME");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_INTEGER);
		rec.put(JLParameterRequestParams.PARAM_VALUE, 95);
		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, true);
		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "TEST_1");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "CURVE");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 121);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "PTC_BRAND");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_STRING);
		rec.put(JLParameterRequestParams.PARAM_VALUE, "BETA");
		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "TEST_2");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "CURVE");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 125);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "PTC_LIFETIME");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_INTEGER);
		rec.put(JLParameterRequestParams.PARAM_VALUE, 70);
		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, true);
		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "TEST_2");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "CURVE");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 125);
		ex.addOutput(OUTPUT_PARAMLIST, params);
    	template.addExample(ex);

//    	ex = new FunctionExample();
//    	ex.addInput(PARAM_PARAMS, new String[] {"LM_LENGTH","LM_WIDTH","LM_HEIGHT"});
//    	params = new ArrayList<Map<String, Object>>();
//    	rec = new OrderedMap<String, Object>();
//    	params.add(rec);
//    	rec.put(PARAM_NAME, "LM_LENGTH");
//    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
//		rec.put(JLParameterRequestParams.PARAM_VALUE, 12.5);
//		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
//		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "FLAT_4");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "FLAT");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 93);
//    	rec = new OrderedMap<String, Object>();
//    	params.add(rec);
//    	rec.put(PARAM_NAME, "LM_HEIGHT");
//    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
//		rec.put(JLParameterRequestParams.PARAM_VALUE, 5.5);
//		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
//		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "FLAT_4");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "FLAT");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 93);
//    	rec = new OrderedMap<String, Object>();
//    	params.add(rec);
//    	rec.put(PARAM_NAME, "LM_WIDTH");
//    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
//		rec.put(JLParameterRequestParams.PARAM_VALUE, 15.5);
//		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
//		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "FLAT_4");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "FLAT");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 93);
//    	rec = new OrderedMap<String, Object>();
//    	params.add(rec);
//    	rec.put(PARAM_NAME, "LM_LENGTH");
//    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
//		rec.put(JLParameterRequestParams.PARAM_VALUE, 30.0);
//		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
//		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "BEVEL_1");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "BEVEL");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 70);
//    	rec = new OrderedMap<String, Object>();
//    	params.add(rec);
//    	rec.put(PARAM_NAME, "LM_HEIGHT");
//    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
//		rec.put(JLParameterRequestParams.PARAM_VALUE, 12.3);
//		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
//		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "BEVEL_1");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "BEVEL");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 70);
//    	rec = new OrderedMap<String, Object>();
//    	params.add(rec);
//    	rec.put(PARAM_NAME, "LM_WIDTH");
//    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
//		rec.put(JLParameterRequestParams.PARAM_VALUE, 20.37);
//		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
//		rec.put(JLParameterRequestParams.PARAM_ENCODED, false);
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "BEVEL_1");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "BEVEL");
//		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 70);
//		ex.addOutput(OUTPUT_PARAMLIST, params);
//    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "TEST_10");
    	ex.addInput(PARAM_PARAM, "MFG_ID");
    	ex.addInput(JLParameterRequestParams.PARAM_ENCODED, true);
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "MFG_ID");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_STRING);
		rec.put(JLParameterRequestParams.PARAM_VALUE, "ZnJpZW5kbHk=");
		rec.put(JLParameterRequestParams.PARAM_DESIGNATE, false);
		rec.put(JLParameterRequestParams.PARAM_ENCODED, true);
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_NAME, "TEST_10");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_TYPE, "COORDINATE SYSTEM");
		rec.put(JLParameterResponseParams.OUTPUT_OWNER_ID, 17);
		ex.addOutput(OUTPUT_PARAMLIST, params);
    	template.addExample(ex);

    	
    	return template;
	}
	
	private FunctionTemplate helpSetParam() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_PARAM);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the value of a feature parameter");
    	spec.addFootnote("Will only set parameters on visible features.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Feature name");
    	arg.setDefaultValue("All features are updated");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PARAM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter data type");
    	arg.setValidValues(new String[] {
	    	IJLParameter.TYPE_STRING,
	    	IJLParameter.TYPE_DOUBLE,
	    	IJLParameter.TYPE_INTEGER,
	    	IJLParameter.TYPE_BOOL,
	    	IJLParameter.TYPE_NOTE
    	});
    	arg.setDefaultValue(IJLParameter.TYPE_STRING);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(JLParameterRequestParams.PARAM_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Parameter value");
    	arg.setDefaultValue("Clears the parameter value if missing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(JLParameterRequestParams.PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Value is Base64-encoded");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(JLParameterRequestParams.PARAM_DESIGNATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Set parameter to be designated/not designated, blank=do not set");
    	arg.setDefaultValue("blank");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(JLParameterRequestParams.PARAM_NO_CREATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("If parameter does not already exist, do not create it");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PLANE_34");
    	ex.addInput(PARAM_PARAM, "TEST");
    	ex.addInput(PARAM_TYPE, IJLParameter.TYPE_INTEGER);
    	ex.addInput(JLParameterRequestParams.PARAM_VALUE, 32);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "FLAT_1");
    	ex.addInput(PARAM_PARAM, "ALPHA");
    	ex.addInput(PARAM_TYPE, IJLParameter.TYPE_STRING);
    	ex.addInput(JLParameterRequestParams.PARAM_VALUE, "ZnJpZW5kbHk=");
    	ex.addInput(JLParameterRequestParams.PARAM_ENCODED, true);
    	ex.addInput(JLParameterRequestParams.PARAM_DESIGNATE, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpUserSelectCsys() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_USER_SELECT_CSYS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Prompt the user to select one or more coordinate systems, and return their selections");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MAX, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("The maximum number of dimensions that the user can select");
    	arg.setDefaultValue("1");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FEATLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_FEATURE_DATA);
    	ret.setDescription("List of feature information");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	List<Map<String, Object>> feats = new ArrayList<Map<String, Object>>();
    	Map<String, Object> rec;
    	rec = new OrderedMap<String, Object>();
    	rec.putAll(sampleFeatures.get("CS3"));
    	rec.put(OUTPUT_ID, 44);
    	rec.put(OUTPUT_FEATNO, 3);
    	feats.add(rec);
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MAX, 2);
    	feats = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	rec.putAll(sampleFeatures.get("CS3"));
    	rec.put(OUTPUT_ID, 44);
    	rec.put(OUTPUT_FEATNO, 3);
    	feats.add(rec);
    	rec = new OrderedMap<String, Object>();
    	rec.putAll(sampleFeatures.get("CS0"));
    	rec.put(OUTPUT_ID, 40);
    	rec.put(OUTPUT_FEATNO, 1);
    	feats.add(rec);
		ex.addOutput(OUTPUT_FEATLIST, feats);
    	template.addExample(ex);

        return template;
    }
    
}
