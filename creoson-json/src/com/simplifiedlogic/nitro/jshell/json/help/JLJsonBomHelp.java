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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jshell.json.request.JLBomRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLFileRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLBomResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "bom" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonBomHelp extends JLJsonCommandHelp implements JLBomRequestParams, JLBomResponseParams {

	public static final String OBJ_BOM_CHILD = "BomChild";

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
		list.add(helpGetPaths());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpBomChild());
		return list;
	}
	
	private FunctionTemplate helpGetPaths() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_PATHS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a hierarchy of components within an assembly");
    	spec.addFootnote("Even if you do not set " + PARAM_EXCLUDE_INACTIVE + " to true, the function will still exclude any components with a status of INACTIVE or UNREGENERATED.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Current active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PATHS, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return component paths for each component");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SKELETON, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to include skeleton components");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TOPLEVEL, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return only the top-level components in the assembly");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TRANSFORMS, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return the 3D transform matrix for each component");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_EXCLUDE_INACTIVE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to exclude components which do not have an ACTIVE status");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SIMPREP, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to return the Simplified Rep data for each component");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Assembly file name");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_GENERIC, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Generic name for the assembly");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_CHILDREN, FunctionSpec.TYPE_OBJECT, OBJ_BOM_CHILD);
    	ret.setDescription("The hierarchy of component data, starting with the top-level assembly");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_HAS_SIMPREP, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the assembly has a Simplified Rep");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	// example with no params
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "plate_assy.asm");
    	ex.addOutput(PARAM_MODEL, "plate_assy.asm");
    	ex.addOutput(OUTPUT_GENERIC, "base_assy");
    	Map<String, Object> rec, rec2, rec3;
    	List<Map<String, Object>> list, list2;
    	rec = new OrderedMap<String, Object>();
    	ex.addOutput(OUTPUT_CHILDREN, rec);
    	rec.put(PARAM_MODEL, "plate_assy.asm");
    	rec.put(OUTPUT_SEQ_PATH, "root");
    	rec.put(OUTPUT_HAS_SIMPREP, Boolean.TRUE);

    	list = new ArrayList<Map<String, Object>>();
    	rec.put(OUTPUT_CHILDREN, list);
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "bracket.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.1");
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "bracket-2.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.2");
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "plate_sub.asm");
    	rec2.put(OUTPUT_SEQ_PATH, "root.3");
	    	list2 = new ArrayList<Map<String, Object>>();
	    	rec2.put(OUTPUT_CHILDREN, list2);
	    	rec3 = new OrderedMap<String, Object>();
	    	list2.add(rec3);
	    	rec3.put(PARAM_MODEL, "bracket.prt");
	    	rec3.put(OUTPUT_SEQ_PATH, "root.3.1");
	    	rec3 = new OrderedMap<String, Object>();
	    	list2.add(rec3);
	    	rec3.put(PARAM_MODEL, "nut.prt");
	    	rec3.put(OUTPUT_SEQ_PATH, "root.3.2");
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "no_solid.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.4");
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "short<screw>.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.5");

    	template.addExample(ex);

    	// example with paths on
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "plate_assy.asm");
    	ex.addInput(PARAM_PATHS, true);
    	ex.addOutput(PARAM_MODEL, "plate_assy.asm");
    	ex.addOutput(OUTPUT_GENERIC, "base_assy");
    	rec = new OrderedMap<String, Object>();
    	ex.addOutput(OUTPUT_CHILDREN, rec);
    	rec.put(PARAM_MODEL, "plate_assy.asm");
    	rec.put(OUTPUT_SEQ_PATH, "root");
    	list = new ArrayList<Map<String, Object>>();
    	rec.put(OUTPUT_CHILDREN, list);
    	
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "bracket.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.1");
    	rec2.put(OUTPUT_PATH, new int[] {39});
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "bracket-2.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.2");
    	rec2.put(OUTPUT_PATH, new int[] {40});
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "plate_sub.asm");
    	rec2.put(OUTPUT_SEQ_PATH, "root.3");
    	rec2.put(OUTPUT_PATH, new int[] {41});
	    	list2 = new ArrayList<Map<String, Object>>();
	    	rec2.put(OUTPUT_CHILDREN, list2);
	    	rec3 = new OrderedMap<String, Object>();
	    	list2.add(rec3);
	    	rec3.put(PARAM_MODEL, "bracket.prt");
	    	rec3.put(OUTPUT_SEQ_PATH, "root.3.1");
	    	rec3.put(OUTPUT_PATH, new int[] {41,43});
	    	rec3 = new OrderedMap<String, Object>();
	    	list2.add(rec3);
	    	rec3.put(PARAM_MODEL, "nut.prt");
	    	rec3.put(OUTPUT_SEQ_PATH, "root.3.2");
	    	rec3.put(OUTPUT_PATH, new int[] {41,44});
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "no_solid.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.3");
    	rec2.put(OUTPUT_PATH, new int[] {41});
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "short<screw>.prt");
    	rec2.put(OUTPUT_SEQ_PATH, "root.4");
    	rec2.put(OUTPUT_PATH, new int[] {54});

    	template.addExample(ex);

    	// example with transforms on
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "01-51300.asm");
    	ex.addInput(PARAM_PATHS, true);
    	ex.addInput(PARAM_TRANSFORMS, true);
    	ex.addOutput(PARAM_MODEL, "01-51300.asm");
    	rec = new OrderedMap<String, Object>();
    	ex.addOutput(OUTPUT_CHILDREN, rec);
    	rec.put(PARAM_MODEL, "plate_assy.asm");
    	rec.put(OUTPUT_SEQ_PATH, "root");
    	list = new ArrayList<Map<String, Object>>();
    	rec.put(OUTPUT_CHILDREN, list);
    	
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "01-51243C.PRT");
    	rec2.put(OUTPUT_SEQ_PATH, "root.1");
    	rec2.put(OUTPUT_PATH, new int[] {27});
    	rec2.put(OUTPUT_TRANSFORM, 
    			makeTransform(
    					makePoint(0, 0, 0), 
    					makePoint(1, 0, 0), 
    					makePoint(0, 1, 0), 
    					makePoint(0, 0, 1), 
    					-0, -0, -0));
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "01-51243A.PRT");
    	rec2.put(OUTPUT_SEQ_PATH, "root.2");
    	rec2.put(OUTPUT_PATH, new int[] {29});
    	rec2.put(OUTPUT_TRANSFORM, 
    			makeTransform(
    					makePoint(0, 0, 0), 
    					makePoint(1, 0, 0), 
    					makePoint(0, 1, 0), 
    					makePoint(0, 0, 1), 
    					-0, -0, -0));
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "01-51243A.PRT");
    	rec2.put(OUTPUT_SEQ_PATH, "root.3");
    	rec2.put(OUTPUT_PATH, new int[] {66});
    	rec2.put(OUTPUT_TRANSFORM, 
    			makeTransform(
    					makePoint(0, 0, 0), 
    					makePoint(1, 0, 0), 
    					makePoint(0, 0.8660254037844386, -0.5), 
    					makePoint(0, 0.5, 0.8660254037844386), 
    					-30.000000000000004, -0, -0));
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "01-51243A.PRT");
    	rec2.put(OUTPUT_SEQ_PATH, "root.4");
    	rec2.put(OUTPUT_PATH, new int[] {67});
    	rec2.put(OUTPUT_TRANSFORM, 
    			makeTransform(
    					makePoint(0, 0, 0), 
    					makePoint(1, 0, 0), 
    					makePoint(0, 0.5, -0.8660254037844387), 
    					makePoint(0, 0.8660254037844387, 0.5), 
    					-60.00000000000001, -0, -0));
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "01-51243A.PRT");
    	rec2.put(OUTPUT_SEQ_PATH, "root.5");
    	rec2.put(OUTPUT_PATH, new int[] {68});
    	rec2.put(OUTPUT_TRANSFORM, 
    			makeTransform(
    					makePoint(0, 0, 0), 
    					makePoint(1, 0, 0), 
    					makePoint(0, 0, -1), 
    					makePoint(0, 1, 0), 
    					-90, -0, -0));
    	rec2 = new OrderedMap<String, Object>();
    	list.add(rec2);
    	rec2.put(PARAM_MODEL, "01-51243D.PRT");
    	rec2.put(OUTPUT_SEQ_PATH, "root.6");
    	rec2.put(OUTPUT_PATH, new int[] {30});
    	rec2.put(OUTPUT_TRANSFORM, 
    			makeTransform(
    					makePoint(0, 0, 0), 
    					makePoint(1, 0, 0), 
    					makePoint(0, 1, 0), 
    					makePoint(0, 0, 1), 
    					-0, -0, -0));

    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpBomChild() {
    	FunctionObject obj = new FunctionObject(OBJ_BOM_CHILD);
    	obj.setDescription("Data for a component in a BOM hierarchy");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Model name of the component");
    	arg.setRequired(true);
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_SEQ_PATH, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Dot-separated list of sequence numbers showing the order of component within the assembly");
    	arg.setRequired(true);
    	obj.add(arg);
    	
    	arg = new FunctionArgument(JLFileRequestParams.PARAM_PATH, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Creo's component path for the component");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_TRANSFORM, FunctionSpec.TYPE_OBJECT, JLJsonFileHelp.OBJ_TRANSFORM);
    	arg.setDescription("3D Transform for the component");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_CHILDREN, FunctionSpec.TYPE_OBJARRAY, OBJ_BOM_CHILD);
    	arg.setDescription("Data for the child components of the component");
    	obj.add(arg);

        return obj;
    }

}
