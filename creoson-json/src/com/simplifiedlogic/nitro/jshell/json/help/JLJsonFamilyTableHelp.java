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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.intf.IJLParameter;
import com.simplifiedlogic.nitro.jshell.json.request.JLCreoRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLFamilyTableRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFamilyTableResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "familytable" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonFamilyTableHelp extends JLJsonCommandHelp implements JLFamilyTableRequestParams, JLFamilyTableResponseParams {

	public static final String OBJ_FAM_TABLE_INSTANCE = "FamTableInstance";
	public static final String OBJ_FAM_TABLE_ROW_COLUMN = "FamTableRowColumn";
	
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
		list.add(helpAddInst());
		list.add(helpCreateInst());
		list.add(helpDelete());
		list.add(helpDeleteInst());
		list.add(helpExists());
		list.add(helpGetCell());
		list.add(helpGetHeader());
		list.add(helpGetParents());
		list.add(helpGetRow());
		list.add(helpList());
		list.add(helpListTree());
		list.add(helpReplace());
		list.add(helpSetCell());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpFamTableInstance());
		list.add(helpFamTableRowColumn());
		return list;
	}
	
	private FunctionTemplate helpExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether an instance exists in a family table");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Instance name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the instance exists in the model's family table; returns false if there is no family table in the model");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "thick_bracket");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpDelete() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete an entire family table");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc*.asm");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpDeleteInst() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_INST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete an instance from a family table");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Instance name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "thick_bracket");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetRow() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_ROW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get one row of a family table");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Family Table instance name");
    	arg.setRequired(true);
    	spec.addArgument(arg);
    	
    	ret = new FunctionReturn(OUTPUT_INSTANCE, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Family Table instance name");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_COLUMNS, FunctionSpec.TYPE_OBJARRAY, OBJ_FAM_TABLE_ROW_COLUMN);
    	ret.setDescription("List of data about each column in the row");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "thick_bracket");
    	Map<String, Object> rec;
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	list.add(rec);
    	rec.put(OUTPUT_COLID, "d0");
		rec.put(OUTPUT_VALUE, 45.0);
		rec.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(OUTPUT_COLTYPE, "dimension");
    	rec = new OrderedMap<String, Object>();
    	list.add(rec);
    	rec.put(OUTPUT_COLID, "BRACKET_WIDTH");
		rec.put(OUTPUT_VALUE, 100.0);
		rec.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(OUTPUT_COLTYPE, "parameter");
    	rec = new OrderedMap<String, Object>();
    	list.add(rec);
    	rec.put(OUTPUT_COLID, "HOLE_DIST");
		rec.put(OUTPUT_VALUE, 50.0);
		rec.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(OUTPUT_COLTYPE, "parameter");
		ex.addOutput(OUTPUT_COLUMNS, list);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetCell() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_CELL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get one cell of a family table");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Family Table instance name");
    	arg.setRequired(true);
    	spec.addArgument(arg);
    	
    	arg = new FunctionArgument(PARAM_COLID, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Column ID");
    	arg.setRequired(true);
    	spec.addArgument(arg);
    	
    	ret = new FunctionReturn(OUTPUT_INSTANCE, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Family Table instance name");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_COLID, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Column ID");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_VALUE, FunctionSpec.TYPE_DEPEND);
    	ret.setDescription("Cell value");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_DATATYPE, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Data type");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_COLTYPE, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Column Type; a string corresponding to the Creo column type");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "thick_bracket");
    	ex.addInput(PARAM_COLID, "BRACKET_WIDTH");
    	ex.addOutput(OUTPUT_COLID, "BRACKET_WIDTH");
    	ex.addOutput(OUTPUT_VALUE, 100.0);
    	ex.addOutput(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE);
    	ex.addOutput(OUTPUT_COLTYPE, "parameter");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpSetCell() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_CELL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the value of one cell of a family table");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Family Table instance name");
    	arg.setRequired(true);
    	spec.addArgument(arg);
    	
    	arg = new FunctionArgument(PARAM_COLID, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Column ID");
    	arg.setRequired(true);
    	spec.addArgument(arg);
    	
    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Cell value");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "thick_bracket");
    	ex.addInput(PARAM_COLID, "BRACKET_WIDTH");
    	ex.addInput(PARAM_VALUE, 120.0);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetHeader() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_HEADER);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the header of a family table");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_COLUMNS, FunctionSpec.TYPE_OBJARRAY, OBJ_FAM_TABLE_ROW_COLUMN);
    	ret.setDescription("List of data about each column in the header");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	Map<String, Object> rec;
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    	rec = new OrderedMap<String, Object>();
    	list.add(rec);
    	rec.put(OUTPUT_COLID, "d0");
		rec.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(OUTPUT_COLTYPE, "dimension");
    	rec = new OrderedMap<String, Object>();
    	list.add(rec);
    	rec.put(PARAM_COLID, "BRACKET_WIDTH");
		rec.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(OUTPUT_COLTYPE, "parameter");
    	rec = new OrderedMap<String, Object>();
    	list.add(rec);
    	rec.put(PARAM_COLID, "HOLE_DIST");
		rec.put(OUTPUT_DATATYPE, IJLParameter.TYPE_DOUBLE);
		rec.put(OUTPUT_COLTYPE, "parameter");
		ex.addOutput(OUTPUT_COLUMNS, list);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpAddInst() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_ADD_INST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Add a new instance to a family table; creates a family table if one does not exist");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New instance name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "light_bracket");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpReplace() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_REPLACE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Replace a model in an assembly with another instance in the same family table");
    	spec.addFootnote("If you are running Creo 7 or higher, you must call "+JLCreoRequestParams.COMMAND+":"+JLCreoRequestParams.FUNC_SET_CREO_VERSION+" to set the Creo version");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name (usually an assembly)");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CURMODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Generic model containing the instances");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CURINST, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Instance name to replace");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PATH, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Path to component to replace");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NEWINST, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New instance name");
    	spec.addArgument(arg);
    	
    	spec.addFootnote("You must specify either " + PARAM_CURINST + " or " + PARAM_PATH + ".");

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc123.asm");
    	ex.addInput(PARAM_CURMODEL, "bracket.prt");
    	ex.addInput(PARAM_CURINST, "wide_bracket");
    	ex.addInput(PARAM_NEWINST, "thick_bracket");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc123.asm");
    	ex.addInput(PARAM_CURMODEL, "bracket.prt");
    	ex.addInput(PARAM_PATH, new int[] {0, 5, 12});
    	ex.addInput(PARAM_NEWINST, "thick_bracket");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List the instance names in a family table");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Instance name filter");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_INSTANCES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of matching instance names");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addOutput(OUTPUT_INSTANCES, new String[] {"WIDE_BRACKET", "WIDE_THICK_BRACKET","THICK_BRACKET"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "WIDE*");
    	ex.addOutput(OUTPUT_INSTANCES, new String[] {"WIDE_BRACKET", "WIDE_THICK_BRACKET"});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListTree() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_TREE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a hierarchical structure of a nested family table");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ERASE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Erase model and non-displayed models afterwards");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_TOTAL, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Count of all child instances including their decendants");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_CHILDREN, FunctionSpec.TYPE_OBJARRAY, OBJ_FAM_TABLE_INSTANCE);
    	ret.setDescription("List of child instances");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addOutput(OUTPUT_TOTAL, 7);
    	List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
    	ex.addOutput(OUTPUT_CHILDREN, list1);
    	Map<String, Object> rec;
    	Map<String, Object> rec2;
    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "thick_bracket");
    	rec.put(OUTPUT_TOTAL, 3);
    	List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
    	rec.put(OUTPUT_CHILDREN, list2);
    	list1.add(rec);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(OUTPUT_NAME, "thick_bracket-1");
    	rec2.put(OUTPUT_TOTAL, 0);
    	list2.add(rec2);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(OUTPUT_NAME, "thick_bracket-2");
    	rec2.put(OUTPUT_TOTAL, 0);
    	list2.add(rec2);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(OUTPUT_NAME, "thick_bracket-3");
    	rec2.put(OUTPUT_TOTAL, 0);
    	list2.add(rec2);

    	rec = new OrderedMap<String, Object>();
    	rec.put(OUTPUT_NAME, "wide_bracket");
    	rec.put(OUTPUT_TOTAL, 2);
    	list2 = new ArrayList<Map<String, Object>>();
    	rec.put(OUTPUT_CHILDREN, list2);
    	list1.add(rec);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(OUTPUT_NAME, "wide_bracket-1");
    	rec2.put(OUTPUT_TOTAL, 0);
    	list2.add(rec2);
    	rec2 = new OrderedMap<String, Object>();
    	rec2.put(OUTPUT_NAME, "wide_bracket-2");
    	rec2.put(OUTPUT_TOTAL, 0);
    	list2.add(rec2);
    	
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpCreateInst() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CREATE_INST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Create a new model from a family table row");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INSTANCE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Instance name");
    	arg.setRequired(true);
    	spec.addArgument(arg);
    	
    	ret = new FunctionReturn(OUTPUT_NAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("File name of the new model");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_INSTANCE, "wide_bracket");
    	ex.addOutput(OUTPUT_NAME, "wide_bracket<bracket>.prt");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetParents() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_PARENTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the parent instances of a model in a nested family table");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_PARENTS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of parent instance names, starting with the immediate parent and working back");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "wide_bracket-1b<bracket>.prt");
    	ex.addOutput(OUTPUT_PARENTS, new String[] {"WIDE_BRACKET-1", "WIDE_BRACKET", "BRACKET"});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionObject helpFamTableInstance() {
    	FunctionObject obj = new FunctionObject(OBJ_FAM_TABLE_INSTANCE);
    	obj.setDescription("Information about an instance in a nested family table");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Name of the family table instance");
    	obj.add(arg);
        
    	arg = new FunctionArgument(OUTPUT_TOTAL, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Count of all child instances including their decendants");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_CHILDREN, FunctionSpec.TYPE_OBJARRAY, OBJ_FAM_TABLE_INSTANCE);
    	arg.setDescription("List of child instances");
    	obj.add(arg);
        
        return obj;
    }
    
	private FunctionObject helpFamTableRowColumn() {
    	FunctionObject obj = new FunctionObject(OBJ_FAM_TABLE_ROW_COLUMN);
    	obj.setDescription("Information about one cell in a family table");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_COLID, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Column ID");
    	obj.add(arg);
        
    	arg = new FunctionArgument(OUTPUT_VALUE, FunctionSpec.TYPE_DEPEND);
    	arg.setDescription("Cell value");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_DATATYPE, FunctionSpec.TYPE_STRING);
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

    	arg = new FunctionArgument(OUTPUT_COLTYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Column Type; a string corresponding to the Creo column type");
    	obj.add(arg);
        
        return obj;
    }
    
}
