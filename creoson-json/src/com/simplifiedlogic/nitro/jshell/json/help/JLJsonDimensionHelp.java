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
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.DataUtils;
import com.simplifiedlogic.nitro.jlink.data.DimDetailData;
import com.simplifiedlogic.nitro.jlink.data.DimToleranceData;
import com.simplifiedlogic.nitro.jshell.json.request.JLDimensionRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLDimensionResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "dimension" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonDimensionHelp extends JLJsonCommandHelp implements JLDimensionRequestParams, JLDimensionResponseParams {

	public static final String OBJ_DIM_DATA = "DimData";
	public static final String OBJ_DIM_DETAIL_DATA = "DimDetailData";
	public static final String OBJ_DIM_SELECT_DATA = "DimSelectData";
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpCopy());
		list.add(helpList());
		list.add(helpListDetail());
		list.add(helpSet());
		list.add(helpSetText());
		list.add(helpShow());
		list.add(helpUserSelect());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpDimData());
		list.add(helpDimDetailData());
		list.add(helpDimSelectData());
		return list;
	}
	
	private FunctionTemplate helpSet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set a dimension value");
    	spec.addFootnote("One reason to encode values is if the value contains special characters, such as Creo symbols.");
    	spec.addFootnote("You may be able to avoid Base64-encoding symbols by using Unicode for the binary characters, for example including \\u0001#\\u0002 in the "+PARAM_VALUE+" to insert a plus/minus symbol."); 
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name");
    	arg.setRequired(true);
       	spec.addArgument(arg);
       	
    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Dimension value");
    	arg.setDefaultValue("Clears the dimension value if missing");
       	spec.addArgument(arg);
       	
    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether the value is Base64-encoded");
    	arg.setDefaultValue("false");
       	spec.addArgument(arg);
       	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d3");
    	ex.addInput(PARAM_VALUE, 32.0);
    	ex.addInput(PARAM_ENCODED, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "RADIUS");
    	ex.addInput(PARAM_VALUE, 2.5);
    	ex.addInput(PARAM_ENCODED, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "ANGLE");
    	ex.addInput(PARAM_VALUE, "MzAgASQCCg==");
    	ex.addInput(PARAM_ENCODED, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpSetText() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_TEXT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set dimension text");
    	spec.addFootnote("If the text contains Creo Symbols or other non-ASCII text, you must Base64-encode the "+PARAM_TEXT+" and set "+PARAM_ENCODED+" to true.");
    	spec.addFootnote("You may be able to avoid Base64-encoding symbols by using Unicode for the binary characters, for example including \\u0001#\\u0002 in the "+PARAM_TEXT+" to insert a plus/minus symbol."); 
    	spec.addFootnote("Embed newlines in the "+PARAM_TEXT+" for line breaks");
    	spec.addFootnote("Since J-Link does not support setting the Prefix or Suffix, you will need to include those in the "+PARAM_TEXT+" value if you need them.");
    	
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name");
    	arg.setRequired(true);
       	spec.addArgument(arg);
       	
    	arg = new FunctionArgument(PARAM_TEXT, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Dimension text");
    	arg.setDefaultValue("Sets the dimension's text to @D if missing");
       	spec.addArgument(arg);
       	
    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether the text value is Base64-encoded");
    	arg.setDefaultValue("false");
       	spec.addArgument(arg);
       	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d3");
    	ex.addInput(PARAM_TEXT, "@D rad");
    	ex.addInput(PARAM_ENCODED, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "RADIUS");
    	ex.addInput(PARAM_TEXT, "(@D)\nAS SHOWN");
    	ex.addInput(PARAM_ENCODED, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "ANGLE");
    	byte[] enc = DataUtils.encodeBase64("@D \001$\002");
    	ex.addInput(PARAM_TEXT, new String(enc));
    	ex.addInput(PARAM_ENCODED, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "ANGLE");
    	ex.addInput(PARAM_TEXT, "@D \u0001$\u0002");
    	ex.addInput(PARAM_ENCODED, false);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpCopy() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_COPY);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Copy dimension to another in the same model or another model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Source model");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name to copy");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TONAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination dimension; the dimension must already exist");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TOMODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination model");
    	arg.setDefaultValue("The source model");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d2");
    	ex.addInput(PARAM_TONAME, "d3");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box_flat.prt");
    	ex.addInput(PARAM_NAME, "RADIUS");
    	ex.addInput(PARAM_TOMODEL, "box.prt");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "RADIUS");
    	ex.addInput(PARAM_TOMODEL, "box.prt");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a list of dimensions from a model");
    	spec.addFootnote("If "+PARAM_SELECT+" is true, then the current selection in Creo will be cleared even if no items are found.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name; only used if " + PARAM_NAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_NAMES + " parameter is used; if both are empty, then all dimensions are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of dimension names");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty, then all dimensions are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DIM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension type filter");
    	arg.setValidValues(new String[] {
        		DimDetailData.TYPE_LINEAR,
        		DimDetailData.TYPE_RADIAL,
        		DimDetailData.TYPE_DIAMETER,
        		DimDetailData.TYPE_ANGULAR
    	});
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return the values Base64-encoded");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SELECT, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("If true, the dimensions that are found will be selected in Creo");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIMLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_DIM_DATA);
    	ret.setDescription("List of dimension information");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d*1");
    	Map<String, Object> rec;
    	List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d11");
		rec.put(OUTPUT_VALUE, 5.0);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAMES, new String[] {"d1","RADIUS","ANGLE"});
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "ANGLE");
    	rec.put(OUTPUT_VALUE, "MzAgASQCCg==");
    	rec.put(OUTPUT_ENCODED, true);
		rec.put(OUTPUT_DWG_DIM, false);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "RADIUS");
    	rec.put(OUTPUT_VALUE, 2.5);
    	rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d1");
    	ex.addInput(PARAM_ENCODED, true);
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, "MzIuNQ==");
		rec.put(OUTPUT_ENCODED, true);
		rec.put(OUTPUT_DWG_DIM, false);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DIM_TYPE, DimDetailData.TYPE_LINEAR);
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.drw");
    	ex.addInput(PARAM_NAME, "d*1");
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d11");
		rec.put(OUTPUT_VALUE, 5.0);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "ad11");
		rec.put(OUTPUT_VALUE, 32.0);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, true);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListDetail() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_DETAIL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a list of dimension details from a model");
    	spec.addFootnote("If "+PARAM_SELECT+" is true, then the current selection in Creo will be cleared even if no items are found.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name; only used if " + PARAM_NAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_NAMES + " parameter is used; if both are empty, then all dimensions are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of dimension names");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty, then all dimensions are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DIM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension type filter");
    	arg.setValidValues(new String[] {
        		DimDetailData.TYPE_LINEAR,
        		DimDetailData.TYPE_RADIAL,
        		DimDetailData.TYPE_DIAMETER,
        		DimDetailData.TYPE_ANGULAR
    	});
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return the values Base64-encoded");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SELECT, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("If true, the dimensions that are found will be selected in Creo");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIMLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_DIM_DETAIL_DATA);
    	ret.setDescription("List of dimension information");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d*1");
    	Map<String, Object> rec;
    	List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
    	// record 1
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		rec.put(OUTPUT_SHEET, 1);
		rec.put(OUTPUT_VIEW_NAME, "main_view");
		rec.put(OUTPUT_DIM_TYPE, DimDetailData.TYPE_LINEAR);
		rec.put(OUTPUT_TEXT, new String[] {"{0:@D}\n"});
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(100.0, 50.0, 0.0));
        rec.put(OUTPUT_TOLERANCE_TYPE, DimToleranceData.TYPE_PLUS_MINUS);
        rec.put(OUTPUT_TOL_PLUS, 0.75);
        rec.put(OUTPUT_TOL_MINUS, 1.0);
    	// record 2
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d11");
		rec.put(OUTPUT_VALUE, 5.0);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		rec.put(OUTPUT_SHEET, 1);
		rec.put(OUTPUT_VIEW_NAME, "main_view");
		rec.put(OUTPUT_DIM_TYPE, DimDetailData.TYPE_LINEAR);
		rec.put(OUTPUT_TEXT, new String[] {"{0:@D}\n"});
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(5.0, 7.5, 0.0));
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAMES, new String[] {"d1","RADIUS","ANGLE"});
    	params = new ArrayList<Map<String, Object>>();
    	// record 1
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "ANGLE");
    	rec.put(OUTPUT_VALUE, "MzAgASQCCg==");
    	rec.put(OUTPUT_ENCODED, true);
		rec.put(OUTPUT_DWG_DIM, false);
		rec.put(OUTPUT_SHEET, 2);
		rec.put(OUTPUT_VIEW_NAME, "hole_view");
		rec.put(OUTPUT_DIM_TYPE, DimDetailData.TYPE_ANGULAR);
		rec.put(OUTPUT_TEXT, new String[] {"{0:@D}\n"});
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(16.0, 3.0, 0.0));
    	// record 2
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		rec.put(OUTPUT_SHEET, 1);
		rec.put(OUTPUT_VIEW_NAME, "main_view");
		rec.put(OUTPUT_DIM_TYPE, DimDetailData.TYPE_LINEAR);
		rec.put(OUTPUT_TEXT, new String[] {"{0:@D}\n"});
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(100.0, 50.0, 0.0));
        rec.put(OUTPUT_TOLERANCE_TYPE, DimToleranceData.TYPE_PLUS_MINUS);
        rec.put(OUTPUT_TOL_PLUS, 0.75);
        rec.put(OUTPUT_TOL_MINUS, 1.0);
    	// record 3
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "RADIUS");
    	rec.put(OUTPUT_VALUE, 2.5);
    	rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		rec.put(OUTPUT_SHEET, 2);
		rec.put(OUTPUT_VIEW_NAME, "hole_view");
		rec.put(OUTPUT_DIM_TYPE, DimDetailData.TYPE_RADIAL);
		rec.put(OUTPUT_TEXT, new String[] {"{0:@D}\n"});
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(12.32, 4.25, 0.0));
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d1");
    	ex.addInput(PARAM_ENCODED, true);
    	params = new ArrayList<Map<String, Object>>();
    	// record 1
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, "MzIuNQ==");
		rec.put(OUTPUT_ENCODED, true);
		rec.put(OUTPUT_DWG_DIM, false);
		rec.put(OUTPUT_SHEET, 1);
		rec.put(OUTPUT_VIEW_NAME, "main_view");
		rec.put(OUTPUT_DIM_TYPE, DimDetailData.TYPE_LINEAR);
		rec.put(OUTPUT_TEXT, new String[] {"{0:@D}\n"});
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(100.0, 50.0, 0.0));
        rec.put(OUTPUT_TOLERANCE_TYPE, DimToleranceData.TYPE_PLUS_MINUS);
        rec.put(OUTPUT_TOL_PLUS, 0.75);
        rec.put(OUTPUT_TOL_MINUS, 1.0);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DIM_TYPE, DimDetailData.TYPE_LINEAR);
    	params = new ArrayList<Map<String, Object>>();
    	// record 1
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(OUTPUT_DWG_DIM, false);
		rec.put(OUTPUT_SHEET, 1);
		rec.put(OUTPUT_VIEW_NAME, "main_view");
		rec.put(OUTPUT_DIM_TYPE, DimDetailData.TYPE_LINEAR);
		rec.put(OUTPUT_TEXT, new String[] {"{0:@D}\n"});
        rec.put(OUTPUT_LOCATION, JLJsonFileHelp.writePoint(100.0, 50.0, 0.0));
        rec.put(OUTPUT_TOLERANCE_TYPE, DimToleranceData.TYPE_PLUS_MINUS);
        rec.put(OUTPUT_TOL_PLUS, 0.75);
        rec.put(OUTPUT_TOL_MINUS, 1.0);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpDimData() {
    	FunctionObject obj = new FunctionObject(OBJ_DIM_DATA);
    	obj.setDescription("Information about a Creo dimension");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Dimension value");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Value is Base64-encoded");
    	arg.setDefaultValue("false");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_DWG_DIM, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether dimension is a drawing dimension rather than a model dimension");
    	arg.setDefaultValue("false");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionObject helpDimSelectData() {
		FunctionObject obj = helpDimData();
		obj.setObjectName(OBJ_DIM_SELECT_DATA);
    	obj.setDescription("Information about a dimension that the user has selected");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name that owns the dimension");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_RELATION_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Relation ID for the dimension's model");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionObject helpDimDetailData() {
		FunctionObject obj = helpDimData();
		obj.setObjectName(OBJ_DIM_DETAIL_DATA);
    	obj.setDescription("More detailed information about a Creo dimension");
    	obj.addFootnote("If dimension is not a drawing dimension, then only "+OUTPUT_NAME+", "+OUTPUT_VALUE+", "+OUTPUT_ENCODED+", "+OUTPUT_DIM_TYPE+", and "+OUTPUT_TEXT+" will be returned.");
    	obj.addFootnote(OUTPUT_TOL_LOWER_LIMIT + " and " + OUTPUT_TOL_UPPER_LIMIT + " are only set when " + OUTPUT_TOLERANCE_TYPE + "=" + DimToleranceData.TYPE_LIMITS);
    	obj.addFootnote(OUTPUT_TOL_PLUS + " and " + OUTPUT_TOL_MINUS + " are only set when " + OUTPUT_TOLERANCE_TYPE + "=" + DimToleranceData.TYPE_PLUS_MINUS);
    	obj.addFootnote(OUTPUT_TOL_SYMMETRIC_VALUE + " is only set when " + OUTPUT_TOLERANCE_TYPE + "=" + DimToleranceData.TYPE_SYMMETRIC + " or " + DimToleranceData.TYPE_SYM_SUPERSCRIPT);
    	obj.addFootnote(OUTPUT_TOL_TABLE_NAME + ", " + OUTPUT_TOL_TABLE_COLUMN + " and " + OUTPUT_TOL_TABLE_TYPE + " are only set when " + OUTPUT_TOLERANCE_TYPE + "=" + DimToleranceData.TYPE_ISODIN);

    	FunctionArgument arg;

    	arg = new FunctionArgument(OUTPUT_DIM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension type");
    	arg.setValidValues(new String[] {
        		DimDetailData.TYPE_LINEAR,
        		DimDetailData.TYPE_RADIAL,
        		DimDetailData.TYPE_DIAMETER,
        		DimDetailData.TYPE_ANGULAR
    	});
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TEXT, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension raw text");
    	obj.add(arg);
    	
    	arg = new FunctionArgument(OUTPUT_SHEET, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Sheet number");
    	obj.add(arg);
        
    	arg = new FunctionArgument(OUTPUT_VIEW_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_LOCATION, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_POINT);
    	arg.setDescription("Coordinates of the dimension in drawing units");
    	obj.add(arg);
    	
    	arg = new FunctionArgument(OUTPUT_TOLERANCE_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Tolerance type, if any");
    	arg.setValidValues(new String[] {
    		DimToleranceData.TYPE_LIMITS,	
    		DimToleranceData.TYPE_PLUS_MINUS,
    		DimToleranceData.TYPE_SYMMETRIC,	
    		DimToleranceData.TYPE_SYM_SUPERSCRIPT,
    		DimToleranceData.TYPE_ISODIN	
    	});
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_LOWER_LIMIT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Tolerance Lower Limit");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_UPPER_LIMIT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Tolerance Upper Limit");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_PLUS, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Tolerance Plus Value");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_MINUS, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Tolerance Minus Value");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_SYMMETRIC_VALUE, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Tolerance Symmetric Value");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_TABLE_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Tolerance Table Name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_TABLE_COLUMN, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Tolerance Table Column");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TOL_TABLE_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Tolerance Table Type");
    	arg.setValidValues(new String[] {
			DimToleranceData.TABLE_GENERAL,
			DimToleranceData.TABLE_BROKEN_EDGE,
			DimToleranceData.TABLE_SHAFTS,
			DimToleranceData.TABLE_HOLES
    	});
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpShow() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SHOW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Display or hide a dimension in Creo");
    	spec.addFootnote("You can show a dimension on a specific occurrence of a part in an assembly, but if you hide a dimension it will be hidden on all occurrences of the component.");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name containing the dimension");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ASSEMBLY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Assembly name; only used if " + PARAM_PATH + " is given");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name");
    	arg.setRequired(true);
       	spec.addArgument(arg);
       	
    	arg = new FunctionArgument(PARAM_PATH, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Path to occurrence of the model within the assembly; the dimension will only be shown for that occurrence");
    	arg.setDefaultValue("All occurrences of the component are affected");
       	spec.addArgument(arg);
       	
    	arg = new FunctionArgument(PARAM_SHOW, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to show (or hide) the dimension");
    	arg.setDefaultValue("true (show)");
       	spec.addArgument(arg);
       	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d3");
    	ex.addInput(PARAM_SHOW, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bolt.prt");
    	ex.addInput(PARAM_ASSEMBLY, "engine.asm");
    	ex.addInput(PARAM_NAME, "d5");
    	ex.addInput(PARAM_PATH, new int[] {51, 12});
    	ex.addInput(PARAM_SHOW, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "d1");
    	ex.addInput(PARAM_SHOW, false);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpUserSelect() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_USER_SELECT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Prompt the user to select one or more dimensions, and return their selections");
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

    	ret = new FunctionReturn(OUTPUT_DIMLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_DIM_SELECT_DATA);
    	ret.setDescription("List of selected dimension information");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	Map<String, Object> rec;
    	List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(PARAM_MODEL, "box.prt");
		rec.put(PARAM_RELATION_ID, 23);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MAX, 2);
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "ANGLE");
    	rec.put(OUTPUT_VALUE, "MzAgASQCCg==");
    	rec.put(OUTPUT_ENCODED, true);
		rec.put(PARAM_MODEL, "box.prt");
		rec.put(PARAM_RELATION_ID, 145);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(OUTPUT_NAME, "d1");
		rec.put(OUTPUT_VALUE, 32.5);
		rec.put(OUTPUT_ENCODED, false);
		rec.put(PARAM_MODEL, "box.prt");
		rec.put(PARAM_RELATION_ID, 23);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

        return template;
    }
    
}
