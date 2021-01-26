/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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

import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.jshell.json.request.JLParameterRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLParameterResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "parameter" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonParameterHelp extends JLJsonCommandHelp implements JLParameterRequestParams, JLParameterResponseParams {

	public static final String OBJ_PARAMETER_DATA = "ParameterData";

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getCommand()
	 */
	public String getCommand() {
		return COMMAND;
	}

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpCopy());
		list.add(helpDelete());
		list.add(helpExists());
		list.add(helpList());
		list.add(helpSet());
		list.add(helpSetDesignated());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpParameterData());
		return list;
	}
	
	private FunctionTemplate helpSet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the value of a parameter");
    	spec.addFootnote("One reason to encode values is if the value contains special characters, such as Creo symbols");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name");
    	arg.setWildcards(true);
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Data type");
    	arg.setValidValues(new String[] {
	    	IJLParameter.TYPE_STRING,
	    	IJLParameter.TYPE_DOUBLE,
	    	IJLParameter.TYPE_INTEGER,
	    	IJLParameter.TYPE_BOOL,
	    	IJLParameter.TYPE_NOTE
    	});
    	arg.setDefaultValue(IJLParameter.TYPE_STRING);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Parameter value");
    	arg.setDefaultValue("Clears the parameter value if missing");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DESCRIPTION, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter description");
    	arg.setDefaultValue("If missing, leaves the current description in place");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether the value is Base64-encoded");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

//    	arg = new FunctionArgument(PARAM_DESIGNATE, FunctionSpec.TYPE_INTEGER);
//    	arg.setDescription("Set parameter to be designated; " + IJLParameter.DESIGNATE_ON + "=designated," + IJLParameter.DESIGNATE_OFF + "=not designated,blank=do not set");
//    	vals = new ArrayList<String>();
//    	vals.add("blank");
//    	vals.add(String.valueOf(IJLParameter.DESIGNATE_ON));
//    	vals.add(String.valueOf(IJLParameter.DESIGNATE_OFF));
//    	arg.setValidValues(vals);
//    	arg.setDefaultValue("blank");
//    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DESIGNATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Set parameter to be designated/not designated, blank=do not set");
    	arg.setDefaultValue("blank");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NO_CREATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("If parameter does not already exist, do not create it");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "TEST");
    	ex.addInput(PARAM_TYPE, IJLParameter.TYPE_INTEGER);
    	ex.addInput(PARAM_VALUE, 32);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "TEST");
    	ex.addInput(PARAM_TYPE, IJLParameter.TYPE_INTEGER);
    	ex.addInput(PARAM_VALUE, 32);
    	ex.addInput(PARAM_DESCRIPTION, "Test Parameter");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "ALPHA");
    	ex.addInput(PARAM_TYPE, IJLParameter.TYPE_STRING);
    	ex.addInput(PARAM_VALUE, "ZnJpZW5kbHk=");
    	ex.addInput(PARAM_ENCODED, true);
    	ex.addInput(PARAM_DESIGNATE, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpSetDesignated() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_DESIGNATED);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the designated state of a parameter");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name");
    	arg.setWildcards(true);
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DESIGNATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Set parameter to be designated/not designated");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "TEST");
    	ex.addInput(PARAM_DESIGNATE, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpDelete() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a parameter");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name");
    	arg.setWildcards(true);
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "ALPHA");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "LM*");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc*.asm");
    	ex.addInput(PARAM_NAME, "MFG_ID");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpCopy() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_COPY);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Copy parameter to another in the same model or another model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Source model");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name to copy");
    	arg.setWildcards(true);
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TONAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination parameter");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TOMODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Destination model");
    	arg.setDefaultValue("The source model");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DESIGNATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Set copied parameter to be designated/not designated, blank=do not set");
    	arg.setDefaultValue("blank");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "ALPHA");
    	ex.addInput(PARAM_TONAME, "BETA");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box_flat.prt");
    	ex.addInput(PARAM_NAME, "LM_*");
    	ex.addInput(PARAM_TOMODEL, "box.prt");
    	ex.addInput(PARAM_DESIGNATE, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "MFG_ID");
    	ex.addInput(PARAM_TOMODEL, "box*.prt");
    	ex.addInput(PARAM_DESIGNATE, false);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a list of parameters from one or more models");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name; only used if " + PARAM_NAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_NAMES + " parameter is used; if both are empty, then all parameters are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of parameter names");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty, then all parameters are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return the values Base64-encoded");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter value filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("no filter");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_PARAMLIST, FunctionSpec.TYPE_OBJARRAY, OBJ_PARAMETER_DATA);
    	ret.setDescription("List of parameter information");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PTC*");
    	Map<String, Object> rec;
    	List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "PTC_BRAND");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_STRING);
		rec.put(PARAM_VALUE, "ALPHA");
		rec.put(PARAM_DESCRIPTION, "Brand");
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "box.prt");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "PTC_LIFETIME");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_INTEGER);
		rec.put(PARAM_VALUE, 95);
		rec.put(PARAM_DESCRIPTION, "Lifetime");
		rec.put(PARAM_DESIGNATE, true);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "box.prt");
		ex.addOutput(OUTPUT_PARAMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAMES, new String[] {"LM_LENGTH","LM_WIDTH","LM_HEIGHT"});
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "LM_LENGTH");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(PARAM_VALUE, 12.5);
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "abc123.asm");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "LM_HEIGHT");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(PARAM_VALUE, 5.5);
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "abc123.asm");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "LM_WIDTH");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(PARAM_VALUE, 15.5);
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "abc123.asm");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "LM_LENGTH");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(PARAM_VALUE, 30.0);
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "box.prt");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "LM_HEIGHT");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(PARAM_VALUE, 12.3);
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "box.prt");
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "LM_WIDTH");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(PARAM_VALUE, 20.37);
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, false);
		rec.put(OUTPUT_OWNER_NAME, "box.prt");
		ex.addOutput(OUTPUT_PARAMLIST, params);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "MFG_ID");
    	ex.addInput(PARAM_ENCODED, true);
    	params = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	params.add(rec);
    	rec.put(PARAM_NAME, "MFG_ID");
    	rec.put(PARAM_TYPE, IJLParameter.TYPE_STRING);
		rec.put(PARAM_VALUE, "ZnJpZW5kbHk=");
    	rec.put(PARAM_DESCRIPTION, "Manufacturing ID");
		rec.put(PARAM_DESIGNATE, false);
		rec.put(PARAM_ENCODED, true);
		rec.put(OUTPUT_OWNER_NAME, "box.prt");
		ex.addOutput(OUTPUT_PARAMLIST, params);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpParameterData() {
    	FunctionObject obj = new FunctionObject(OBJ_PARAMETER_DATA);
    	obj.setDescription("Information about a Creo parameter");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Parameter value");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Data type");
    	arg.setValidValues(new String[] {
	    	IJLParameter.TYPE_STRING,
	    	IJLParameter.TYPE_DOUBLE,
	    	IJLParameter.TYPE_INTEGER,
	    	IJLParameter.TYPE_BOOL,
	    	IJLParameter.TYPE_NOTE
    	});
    	arg.setDefaultValue(IJLParameter.TYPE_STRING);
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_DESIGNATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Value is designated");
    	arg.setDefaultValue("false");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_DESCRIPTION, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Description");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_ENCODED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Value is Base64-encoded");
    	arg.setDefaultValue("false");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_OWNER_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Owner Name (model or feature name)");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_OWNER_ID, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Owner ID (if owner is a feature)");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_OWNER_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Owner type (if owner is a feature)");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionTemplate helpExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether parameter(s) exists on a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Parameter name; only used if " + PARAM_NAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_NAMES + " parameter is used; if both are empty, then it checks for any parameter's existence");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of parameter names");
    	arg.setDefaultValue("The " + PARAM_NAME + " parameter is used; if both are empty, then it checks for any parameter's existence");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the parameter exists on the model");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "MFG_ID");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "PTC*");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAMES, new String[] {"LM_LENGTH","LM_WIDTH","LM_HEIGHT"});
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

        return template;
    }
    
}
