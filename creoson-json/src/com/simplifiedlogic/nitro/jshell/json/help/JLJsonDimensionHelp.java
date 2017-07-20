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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	public static final String OBJ_DIM_SELECT_DATA = "DimSelectData";
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpCopy());
		list.add(helpList());
		list.add(helpSet());
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
		list.add(helpDimSelectData());
		return list;
	}
	
	private FunctionTemplate helpSet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set a dimension value");
    	spec.addFootnote("One reason to encode values is if the value contains special characters, such as Creo symbols");
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

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return the values Base64-encoded");
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
    	rec.put(PARAM_NAME, "d1");
		rec.put(PARAM_VALUE, 32.5);
		rec.put(PARAM_ENCODED, false);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "d11");
		rec.put(PARAM_VALUE, 5.0);
		rec.put(PARAM_ENCODED, false);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAMES, new String[] {"d1","RADIUS","ANGLE"});
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "ANGLE");
    	rec.put(PARAM_VALUE, "MzAgASQCCg==");
    	rec.put(PARAM_ENCODED, true);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "d1");
		rec.put(PARAM_VALUE, 32.5);
		rec.put(PARAM_ENCODED, false);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "RADIUS");
    	rec.put(PARAM_VALUE, 2.5);
    	rec.put(PARAM_ENCODED, false);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "d1");
    	ex.addInput(PARAM_ENCODED, true);
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "d1");
		rec.put(PARAM_VALUE, "MzIuNQ==");
		rec.put(PARAM_ENCODED, true);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpDimData() {
    	FunctionObject obj = new FunctionObject(OBJ_DIM_DATA);
    	obj.setDescription("Information about a Creo dimension");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dimension name");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Dimension value");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Value is Base64-encoded");
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
    	rec.put(PARAM_NAME, "d1");
		rec.put(PARAM_VALUE, 32.5);
		rec.put(PARAM_ENCODED, false);
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
    	rec.put(PARAM_NAME, "ANGLE");
    	rec.put(PARAM_VALUE, "MzAgASQCCg==");
    	rec.put(PARAM_ENCODED, true);
		rec.put(PARAM_MODEL, "box.prt");
		rec.put(PARAM_RELATION_ID, 145);
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "d1");
		rec.put(PARAM_VALUE, 32.5);
		rec.put(PARAM_ENCODED, false);
		rec.put(PARAM_MODEL, "box.prt");
		rec.put(PARAM_RELATION_ID, 23);
		ex.addOutput(OUTPUT_DIMLIST, params);
    	template.addExample(ex);

        return template;
    }
    
}
