/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.simplifiedlogic.nitro.jlink.data.JLConstraintInput;
import com.simplifiedlogic.nitro.jshell.json.request.JLFileRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLFileResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "file" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonFileHelp extends JLJsonCommandHelp implements JLFileRequestParams, JLFileResponseParams {

	public static final String OBJ_CONSTRAINT = "JLConstraint";
	public static final String OBJ_POINT = "JLPoint";
	public static final String OBJ_TRANSFORM = "JLTransform";
	public static final String OBJ_INERTIA = "JLInertia";
	public static final String OBJ_FILEMATERIAL = "FileMaterial";
	
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
		list.add(helpAssemble());
		list.add(helpBackup());
		list.add(helpCloseWindow());
		list.add(helpDeleteMaterial());
		list.add(helpDisplay());
		list.add(helpErase());
		list.add(helpEraseNotDisplayed());
		list.add(helpExists());
		list.add(helpGetActive());
		list.add(helpGetCurrentMaterial());
		list.add(helpGetCurrentMaterialWildcard());
		list.add(helpGetFileinfo());
		list.add(helpGetLengthUnits());
		list.add(helpGetMassUnits());
		list.add(helpGetTransform());
		list.add(helpHasInstances());
		list.add(helpIsActive());
		list.add(helpList());
		list.add(helpListInstances());
		list.add(helpListMaterials());
		list.add(helpListMaterialsWildcard());
		list.add(helpListSimpReps());
		list.add(helpLoadMaterialFile());
		list.add(helpMassprops());
		list.add(helpOpen());
		list.add(helpOpenErrors());
		list.add(helpPostRegenRelationsGet());
		list.add(helpPostRegenRelationsSet());
		list.add(helpRefresh());
		list.add(helpRegenerate());
		list.add(helpRelationsGet());
		list.add(helpRelationsSet());
		list.add(helpRename());
		list.add(helpRepaint());
		list.add(helpSave());
		list.add(helpSetCurrentMaterial());
		list.add(helpSetLengthUnits());
		list.add(helpSetMassUnits());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpFileMaterial());
		list.add(helpJLConstraint());
		list.add(helpJLInertia());
		list.add(helpJLPoint());
		list.add(helpJLTransform());
		return list;
	}
	
	private FunctionTemplate helpOpen() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_OPEN);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Open one or more files in memory or from the drive");
    	spec.addFootnote("The "+PARAM_DISPLAY+", "+PARAM_ACTIVATE+", "+PARAM_NEWWIN+" and "+PARAM_FORCE+" parameters are *ignored* if "+PARAM_MODEL+" contains wildcards or if "+PARAM_MODELS+" contains a list of names");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory name");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name; only used if " + PARAM_MODELS + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_MODELS + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of file names");
    	arg.setDefaultValue("The " + PARAM_MODEL + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_GENERIC, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Generic model name (if file name represents an instance)");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DISPLAY, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Display the model after opening");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ACTIVATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Activate the model after opening");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NEWWIN, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Open model in a new window");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FORCE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Force regeneration after opening");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Directory name of opened file(s)");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FILES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("File names that were opened");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_REVISION, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Revision of file that was opened; if more than one file was opened, this field is not returned");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "thin_bracket.prt");
    	ex.addInput(PARAM_GENERIC, "bracket");
    	ex.addInput(PARAM_DISPLAY, true);
    	ex.addInput(PARAM_ACTIVATE, true);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/somefiles/parts");
    	ex.addOutput(OUTPUT_FILES, new String[] {"thin_bracket<bracket>.prt"});
    	ex.addOutput(OUTPUT_REVISION, 23);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "abc*.asm");
    	ex.addInput(PARAM_DIRNAME, "c:/mydir/parts");
    	ex.addInput(PARAM_DISPLAY, true);
    	ex.addInput(PARAM_ACTIVATE, true);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/mydir/parts");
    	ex.addOutput(OUTPUT_FILES, new String[] {"abc123.asm","abc93939.asm"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	List<String> files = new ArrayList<String>();
    	files.add("able.prt");
    	files.add("baker.prt");
    	files.add("charlie.prt");
    	ex.addInput(PARAM_MODELS, files);
    	ex.addInput(PARAM_DIRNAME, "c:/mydir/parts");
    	ex.addInput(PARAM_DISPLAY, true);
    	ex.addInput(PARAM_ACTIVATE, true);
    	ex.addOutput(OUTPUT_DIRNAME, "C:/mydir/parts");
    	ex.addOutput(OUTPUT_FILES, new String[] {"able.prt","baker.prt","charlie.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "nofile.prt");
    	ex.addInput(PARAM_DISPLAY, true);
    	ex.addInput(PARAM_ACTIVATE, true);
    	ex.createError("Could not open file 'nofile.prt'.");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpOpenErrors() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_OPEN_ERRORS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether Creo errors have occurred opening a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Current active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_ERRORS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether errors exist in Creo");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_ERRORS, false);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpRename() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_RENAME);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Rename a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Old file name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NEWNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New file name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ONLYSESSION, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Modify only in memory, not on disk");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);
    	
    	ret = new FunctionReturn(OUTPUT_MODEL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("The new model name");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NEWNAME, "box_flat.prt");
    	ex.addInput(PARAM_ONLYSESSION, true);
    	ex.addOutput(OUTPUT_MODEL, "box_flat.prt");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSave() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SAVE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Save one or more models");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name; only used if " + PARAM_MODELS + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_MODELS + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of file names");
    	arg.setDefaultValue("The " + PARAM_MODEL + " parameter is used");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	List<String> files = new ArrayList<String>();
    	files.add("able.prt");
    	files.add("baker.prt");
    	files.add("charlie.prt");
    	ex.addInput(PARAM_MODELS, files);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpBackup() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_BACKUP);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Back up a model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setRequired(true);
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TARGETDIR, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Target directory name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_TARGETDIR, "c:/somefiles/parts");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpErase() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_ERASE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Erase one or more models from memory");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name; only used if " + PARAM_MODELS + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_MODELS + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of file names");
    	arg.setDefaultValue("The " + PARAM_MODEL + " parameter is used; if both are empty, then all models in memory are erased");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ERASE_CHILDREN, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Erase children of the models too");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_ERASE_CHILDREN, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	List<String> files = new ArrayList<String>();
    	files.add("able.prt");
    	files.add("baker.prt");
    	files.add("charlie.prt");
    	ex.addInput(PARAM_MODELS, files);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpEraseNotDisplayed() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_ERASE_NOT_DISP);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Erase all non-displayed models from memory");
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpRegenerate() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_REGENERATE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Regenerate one or more models");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name; only used if " + PARAM_MODELS + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_MODELS + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of file names");
    	arg.setDefaultValue("The " + PARAM_MODEL + " parameter is used");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DISPLAY, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Display the model before regenerating");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DISPLAY, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	List<String> files = new ArrayList<String>();
    	files.add("able.prt");
    	files.add("baker.prt");
    	files.add("charlie.prt");
    	ex.addInput(PARAM_MODELS, files);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetActive() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_ACTIVE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the active model from Creo");
    	FunctionReturn ret;
    	
    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Directory name of current model");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_MODEL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("File name of current model");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_MODEL, "box.prt");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a list of files in the current Creo session that match patterns");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name; only used if " + PARAM_MODELS + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_MODELS + " parameter is used; if both are empty, then all files are listed");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of file names");
    	arg.setDefaultValue("The " + PARAM_MODEL + " parameter is used; if both are empty, then all files are listed");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FILES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of file names");
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box*.prt");
    	ex.addOutput(OUTPUT_FILES, new String[] {"box.prt","box_flat.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	List<String> files = new ArrayList<String>();
    	files.add("able.prt");
    	files.add("baker.prt");
    	files.add("charlie.prt");
    	ex.addInput(PARAM_MODELS, files);
    	ex.addOutput(OUTPUT_FILES, new String[] {"baker.prt"});
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether a model exists in memory");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the file is open in Creo");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetFileinfo() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_FILEINFO);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Open one or more files in memory or from the drive");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Directory name of the file");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_MODEL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("File name");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_REVISION, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Revision number of file");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_REVISION, 23);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpHasInstances() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_HAS_INSTANCES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether a model has a family table");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the file has a family table");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_EXISTS, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListInstances() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_INSTANCES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List instances in a model's family table");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Directory name of the file");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_GENERIC, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Generic name");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_FILES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of model names in the table");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/parts");
    	ex.addOutput(OUTPUT_GENERIC, "bracket");
    	ex.addOutput(OUTPUT_FILES, new String[] {"wide_bracket.prt", "thick_bracket.prt", "wide_thick_bracket.prt"});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpMassprops() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_MASSPROPS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get mass property information about a model");
    	spec.addFootnote("PTC's description of "+OUTPUT_COORD_SYS_INERTIA+": \"The inertia matrix with respect to coordinate frame:(element ij is the integral of x_i x_j over the object)\"");
    	spec.addFootnote("PTC's description of "+OUTPUT_COORD_SYS_INERTIA_TENSOR+": \"The inertia tensor with respect to coordinate frame:CoordSysInertiaTensor = trace(CoordSysInertia) * identity - CoordSysInertia\"");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_VOLUME, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Model volume");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_MASS, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Model mass");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_DENSITY, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Model density");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_SURFACE_AREA, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Model surface area");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_CTR_GRAV_INERTIA_TENSOR, FunctionSpec.TYPE_OBJECT, OBJ_INERTIA);
    	ret.setDescription("Model's Inertia Tensor translated to center of gravity.");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_COORD_SYS_INERTIA, FunctionSpec.TYPE_OBJECT, OBJ_INERTIA);
    	ret.setDescription("Model's Inertia Matrix with respect to the coordinate frame.");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_COORD_SYS_INERTIA_TENSOR, FunctionSpec.TYPE_OBJECT, OBJ_INERTIA);
    	ret.setDescription("Model's Inertia Tensor with respect to the coordinate frame.");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addOutput(OUTPUT_VOLUME, 40762.91034040907);
    	ex.addOutput(OUTPUT_MASS, 40762.91034040907);
    	ex.addOutput(OUTPUT_DENSITY, 1.0);
    	ex.addOutput(OUTPUT_SURFACE_AREA, 11820.348301046597);
    	ex.addOutput(OUTPUT_CTR_GRAV_INERTIA_TENSOR, 
    			makeInertia(
    					makePoint(0.6779467214807345, 0, 0), 
    					makePoint(0, 0.6780732249931747, 0), 
    					makePoint(0, 0, 0.003650664136195683) 
    					));
    	ex.addOutput(OUTPUT_COORD_SYS_INERTIA, 
    			makeInertia(
    					makePoint(0.001888583824317897, 0, 0), 
    					makePoint(0, 0.0017620803118777856, 0), 
    					makePoint(0, 0, 2.652848778057265) 
    					));
    	ex.addOutput(OUTPUT_COORD_SYS_INERTIA_TENSOR, 
    			makeInertia(
    					makePoint(2.654610858369143, 0, 0), 
    					makePoint(0, 2.654737361881583, 0), 
    					makePoint(0, 0, 0.003650664136195683) 
    					));
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetMassUnits() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_MASS_UNITS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the current mass units for a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_UNITS, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Mass units");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addOutput(OUTPUT_UNITS, "kg");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpSetMassUnits() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_MASS_UNITS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the current mass units for a model");
    	spec.addFootnote("This will search the model's available Unit Systems for the first one which contains the given mass unit");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_UNITS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New mass units");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CONVERT, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to convert the model's mass values to the new units (true) or leave them the same value (false)");
    	arg.setDefaultValue("true");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_UNITS, "mm");
    	ex.addInput(PARAM_CONVERT, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_UNITS, "in");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetLengthUnits() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_LENGTH_UNITS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the current length units for a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_UNITS, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Length units");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addOutput(OUTPUT_UNITS, "cm");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpSetLengthUnits() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_LENGTH_UNITS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the current length units for a model");
    	spec.addFootnote("This will search the model's available Unit Systems for the first one which contains the given length unit");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_UNITS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New length units");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CONVERT, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to convert the model's length values to the new units (true) or leave them the same value (false)");
    	arg.setDefaultValue("true");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "bracket.prt");
    	ex.addInput(PARAM_UNITS, "g");
    	ex.addInput(PARAM_CONVERT, false);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_UNITS, "lbs");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpRelationsGet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_RELATIONS_GET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get relations for a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_RELATIONS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Exported relations text, one entry per line");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	List<String> rels = new ArrayList<String>();
    	rels.add("d2=bracket_width");
    	rels.add("d3=hole_dist");
    	ex.addOutput(OUTPUT_RELATIONS, rels);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpRelationsSet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_RELATIONS_SET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set relations for a model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_RELATIONS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Relations text to import, one line per entry");
    	arg.setDefaultValue("Clear the relations if missing");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	List<String> rels = new ArrayList<String>();
    	rels.add("d2=bracket_width");
    	rels.add("d3=hole_dist");
    	ex.addInput(PARAM_RELATIONS, rels);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpPostRegenRelationsGet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_POST_REGEN_RELATIONS_GET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get post-regeneration relations for a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_RELATIONS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Exported relations text, one entry per line");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	List<String> rels = new ArrayList<String>();
    	rels.add("d2=bracket_width");
    	rels.add("d3=hole_dist");
    	ex.addOutput(OUTPUT_RELATIONS, rels);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpPostRegenRelationsSet() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_POST_REGEN_RELATIONS_SET);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set post-regeneration relations for a model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_RELATIONS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Relations text to import, one line per entry");
    	arg.setDefaultValue("Clear the relations if missing");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	List<String> rels = new ArrayList<String>();
    	rels.add("d2=bracket_width");
    	rels.add("d3=hole_dist");
    	ex.addInput(PARAM_RELATIONS, rels);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpAssemble() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_ASSEMBLE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Assemble a component into an assembly");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory name");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name of component");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_GENERIC, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Generic model name (if file name represents an instance)");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INTOASM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Target assembly");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PATH, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Path to a component that the new part will be constrained to");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_REF_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Reference model that the new part will be constrained to; only used if " + PARAM_PATH + " is not given.  " +
    			"If there are multiple of this model in the assembly, the component will be assembled multiple times, once to each occurrence");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_TRANSFORM, FunctionSpec.TYPE_OBJECT, OBJ_TRANSFORM);
    	arg.setDescription("Transform structure for the initial position and orientation of the new component; only used if there are no constraints, or for certain constraint types");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CONSTRAINTS, FunctionSpec.TYPE_OBJARRAY, OBJ_CONSTRAINT);
    	arg.setDescription("Assembly constraints");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PACKAGE_ASSEMBLY, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to package the component to the assembly; only used if there are no constraints specified");
    	arg.setDefaultValue("If there are no constraints, then the user will be prompted to constrain the component through the Creo user interface");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_WALK_CHILDREN, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to walk into subassemblies to find reference models to constrain to");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ASSEMBLE_TO_ROOT, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to always assemble to the root assembly, or assemble to the subassembly containing the reference path/model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_SUPPRESS, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to suppress the components immediately after assembling them");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Directory name of component");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FILES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("File name of component");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_REVISION, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Revision of file that was opened; if more than one file was opened, this field is not returned");
    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(OUTPUT_FEATUREID, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Last Feature ID of component after assembly");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "coupling.prt");
    	ex.addInput(PARAM_INTOASM, "stub.asm");
        Map<String, Object> c1 = new HashMap<String, Object>();
        c1.put(PARAM_TYPE, JLConstraintInput.CONSTRAINT_CSYS);
        c1.put(PARAM_ASMREF, "right_asm_ref");
        c1.put(PARAM_COMPREF, "asy_ref");
    	ex.addInput(PARAM_CONSTRAINTS, new Object[] {c1});
    	ex.addInput(PARAM_PATH, new int[] {39,78});
    	ex.addOutput(OUTPUT_DIRNAME, "c:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILES, new String[] {"coupling.prt"});
    	ex.addOutput(OUTPUT_REVISION, 3);
    	ex.addOutput(OUTPUT_FEATUREID, 150);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "coupling.prt");
    	ex.addInput(PARAM_INTOASM, "stub.asm");
        c1 = new HashMap<String, Object>();
        c1.put(PARAM_TYPE, JLConstraintInput.CONSTRAINT_CSYS);
        c1.put(PARAM_ASMREF, "left_asm_ref");
        c1.put(PARAM_COMPREF, "asy_ref");
    	ex.addInput(PARAM_CONSTRAINTS, new Object[] {c1});
    	ex.addInput(PARAM_REF_MODEL, "hose.prt");
    	ex.addOutput(OUTPUT_DIRNAME, "c:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILES, new String[] {"coupling.prt"});
    	ex.addOutput(OUTPUT_REVISION, 3);
    	ex.addOutput(OUTPUT_FEATUREID, 150);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "coupling.prt");
    	ex.addInput(PARAM_INTOASM, "stub.asm");
    	ex.addInput(PARAM_PACKAGE_ASSEMBLY, true);
        c1 = new HashMap<String, Object>();
        c1.put(PARAM_TYPE, JLConstraintInput.CONSTRAINT_FIX);
    	ex.addInput(PARAM_CONSTRAINTS, new Object[] {c1});
        Map<String, Object> trans = new Hashtable<String, Object>();
        trans.put(JLFileRequestParams.PARAM_ORIGIN, writePoint(100.0, 50.0, 0.0));
        trans.put(JLFileRequestParams.PARAM_X_ROT, 180.0);
    	ex.addInput(PARAM_TRANSFORM, trans);
    	ex.addOutput(OUTPUT_DIRNAME, "c:/myfiles/parts");
    	ex.addOutput(OUTPUT_FILES, new String[] {"coupling.prt"});
    	ex.addOutput(OUTPUT_REVISION, 3);
    	ex.addOutput(OUTPUT_FEATUREID, 150);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetTransform() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_TRANSFORM);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the 3D transform for a component in an assembly");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_ASM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Assembly name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PATH, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Path to a component in the assembly");
    	arg.setDefaultValue("The transform is calculated for the assembly itself");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CSYS, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Coordinate system on the component to calculate the transform for");
    	arg.setDefaultValue("The component's default coordinate system");
    	spec.addArgument(arg);

//    	ret = new FunctionReturn(OUTPUT_TRANSFORM, FunctionSpec.TYPE_OBJECT, OBJ_TRANSFORM);
//    	ret.setDescription("The 3D transform from the assembly to the component's coordinate system");
//    	spec.addReturn(ret);
        
    	ret = new FunctionReturn(PARAM_ORIGIN, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	ret.setDescription("Matrix origin");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(PARAM_XAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	ret.setDescription("Matrix X Axis");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(PARAM_YAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	ret.setDescription("Matrix Y Axis");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(PARAM_ZAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	ret.setDescription("Matrix Z Axis");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(PARAM_X_ROT, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("X rotation in degrees");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(PARAM_Y_ROT, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Y rotation in degrees");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(PARAM_Z_ROT, FunctionSpec.TYPE_DOUBLE);
    	ret.setDescription("Z rotation in degrees");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_ASM, "plate_assy.asm");
    	ex.addInput(PARAM_PATH, new int[] {54,23,45});
    	ex.addInput(PARAM_CSYS, "CS0");
		makeTransform(
				ex, 
				makePoint(380.0000554810615, 120.0, 1.000000000017174E-20), 
				makePoint(0, 1, 0), 
				makePoint(1, 0, 0), 
				makePoint(0, 0, 1), 
				-0, -0, -0);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_PATH, new int[] {54,23,52});
		makeTransform(
				ex, 
				makePoint(305.0000554810615, 19.999999999999996, -4.592173826833917E-16), 
				makePoint(1.836909530733566E-16, -1.0, 0), 
				makePoint(1, 1.836909530733566E-16, 0), 
				makePoint(0, 0, 1), 
				0, 0, 90);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
		makeTransform(
				ex, 
				makePoint(50.0, 20.0, 0.0), 
				makePoint(1, 0, 0),
				makePoint(0, 1, 0), 
				makePoint(0, 0, 1), 
				-0, -0, -0);
    	template.addExample(ex);
    	
        return template;
    }
    
    /**
     * Convert a 3D coordinate into a generic JSON structure
     * @param x
     * @param y
     * @param z
     * @return The JSON data as a Hashtable
     */
    protected static OrderedMap<String, Object> writePoint(double x, double y, double z) {
    	OrderedMap<String, Object> out = new OrderedMap<String, Object>();
		out.put(JLFileResponseParams.OUTPUT_X, x);
		out.put(JLFileResponseParams.OUTPUT_Y, y);
		out.put(JLFileResponseParams.OUTPUT_Z, z);
		return out;
    }

	private FunctionTemplate helpRefresh() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_REFRESH);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Refresh the window containing a model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpRepaint() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_REPAINT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Repaint the window containing a model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpIsActive() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_IS_ACTIVE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether a model is the active model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_ACTIVE, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the file is the currently active model");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_ACTIVE, true);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpDisplay() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DISPLAY);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Display a model in a window");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_ACTIVATE, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Activate the model after displaying");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpCloseWindow() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CLOSE_WINDOW);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Close the window containing a model");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpListSimpReps() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_SIMP_REPS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List simplified reps in a model");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("Currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_REP, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Simplified rep name pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All simplified reps");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_REPS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of simplified rep names");
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_REPS, new String[] {"LASER_CUT","SHORT","HAND_CUT"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_REP, "SHORT");
    	ex.addOutput(OUTPUT_REPS, new String[] {"SHORT"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_REP, "*CUT*");
    	ex.addOutput(OUTPUT_REPS, new String[] {"LASER_CUT","HAND_CUT"});
    	template.addExample(ex);
    	
        return template;
    }

	private FunctionTemplate helpGetCurrentMaterial() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_CUR_MATL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the current material for a part");
    	spec.addFootnote("This is the same as '"+FUNC_GET_CUR_MATL_WILDCARD+"' but this function does not allow wildcards on the part name.  They are separate functions because the return structures are different.  This function is retained for backwards compatibility.");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	arg.setDefaultValue("Currently active model");
    	arg.setWildcards(false);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MATERIAL, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Current material for the part, may be null if there is no current material");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_MATERIAL, "brass");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "wingnut.prt");
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpGetCurrentMaterialWildcard() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_CUR_MATL_WILDCARD);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the current material for a part or parts");
    	spec.addFootnote("This is the same as '"+FUNC_GET_CUR_MATL+"' but this function allows wildcards on the part name.  They are separate functions because the return structures are different.");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	arg.setDefaultValue("Currently active model");
    	arg.setWildcards(true);
    	spec.addArgument(arg);
    	
    	arg = new FunctionArgument(PARAM_INCLUDE_NON_MATCHING, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to include parts that match the part name pattern but don't have a current material");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MATERIALS, FunctionSpec.TYPE_OBJARRAY, OBJ_FILEMATERIAL);
    	ret.setDescription("A list of part and current-material pairs.");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	List<Map<String, Object>> matls;
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "wingnut.prt");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.prt");
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	matls.add(makeFileMaterial("lid.prt", "bronze"));
    	matls.add(makeFileMaterial("handle.prt", "steel"));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpSetCurrentMaterial() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_CUR_MATL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the current material for a part or parts");
    	spec.addFootnote("If '"+PARAM_MATERIAL+"' has a file extension, it will be removed before the material is set.");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	arg.setDefaultValue("Currently active model");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MATERIAL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Material name");
//    	arg.setDefaultValue("If missing, will unset the current material");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of models which had the material set");

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{"box.prt"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{
    			"box.prt",
    			"lid.prt",
    			"handle.prt"
    			});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpLoadMaterialFile() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LOAD_MATL_FILE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Load a new material file into a part or parts");
    	spec.addFootnote("If '"+PARAM_MATERIAL+"' has a file extension, it will be removed before the material is loaded.");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	arg.setDefaultValue("Currently active model");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory name containing the material file");
    	arg.setDefaultValue("Creo's 'pro_material_dir' config setting, or search path, or current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MATERIAL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Material name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of models which had the material loaded");

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{"box.prt"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_DIRNAME, "C:/mydir/materials");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{"box.prt"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{"box.prt"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{
    			"box.prt",
    			"lid.prt",
    			"handle.prt"
    			});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListMaterials() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_MATERIALS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List materials on a part");
    	spec.addFootnote("This is the same as '"+FUNC_LIST_MATERIALS_WILDCARD+"' but this function does not allow wildcards on the part name.  They are separate functions because the return structures are different.  This function is retained for backwards compatibility.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	arg.setDefaultValue("Currently active model");
    	arg.setWildcards(false);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MATERIAL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Material name pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All materials");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MATERIALS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of materials in the part");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_MATERIALS, new String[] {"brass", "bronze", "steel"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MATERIAL, "br*");
    	ex.addOutput(OUTPUT_MATERIALS, new String[] {"brass", "bronze"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MATERIALS, new String[] {"brass"});
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpListMaterialsWildcard() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_MATERIALS_WILDCARD);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List materials on a part or parts");
    	spec.addFootnote("This is the same as '"+FUNC_LIST_MATERIALS+"' but this function allows wildcards on the part name.  They are separate functions because the return structures are different.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	arg.setDefaultValue("Currently active model");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MATERIAL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Material name pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All materials");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_INCLUDE_NON_MATCHING, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to include parts that match the part name pattern but don't have any materials matching the material pattern");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MATERIALS, FunctionSpec.TYPE_OBJARRAY, OBJ_FILEMATERIAL);
    	ret.setDescription("A list of part and material pairs.  If a part has more than one material, it will have multiple entries in this array.");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	List<Map<String, Object>> matls;
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	matls.add(makeFileMaterial("box.prt", "bronze"));
    	matls.add(makeFileMaterial("box.prt", "steel"));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MATERIAL, "br*");
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	matls.add(makeFileMaterial("box.prt", "bronze"));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.prt");
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	matls.add(makeFileMaterial("box.prt", "bronze"));
    	matls.add(makeFileMaterial("box.prt", "steel"));
    	matls.add(makeFileMaterial("lid.prt", "bronze"));
    	matls.add(makeFileMaterial("handle.prt", "steel"));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.prt");
    	ex.addInput(PARAM_MATERIAL, "br*");
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	matls.add(makeFileMaterial("box.prt", "bronze"));
    	matls.add(makeFileMaterial("lid.prt", "bronze"));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.prt");
    	ex.addInput(PARAM_MATERIAL, "br*");
    	ex.addInput(PARAM_INCLUDE_NON_MATCHING, true);
    	matls = new ArrayList<Map<String, Object>>();
    	matls.add(makeFileMaterial("box.prt", "brass"));
    	matls.add(makeFileMaterial("box.prt", "bronze"));
    	matls.add(makeFileMaterial("lid.prt", "bronze"));
    	matls.add(makeFileMaterial("handle.prt", null));
    	ex.addOutput(OUTPUT_MATERIALS, matls);
    	template.addExample(ex);

        return template;
    }
    
	private FunctionTemplate helpDeleteMaterial() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_MATERIAL);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a material from a part or parts");
    	spec.addFootnote("If '"+PARAM_MATERIAL+"' has a file extension, it will be removed before the material is deleted.");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	arg.setDefaultValue("Current active model");
    	arg.setWildcards(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_MATERIAL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Material name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_MODELS, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of models which had the material deleted");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{"box.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "*.prt");
    	ex.addInput(PARAM_MATERIAL, "brass");
    	ex.addOutput(OUTPUT_MODELS, new String[]{
    			"box.prt",
    			"lid.prt",
    			"handle.prt"
    			});
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionObject helpJLConstraint() {
    	FunctionObject obj = new FunctionObject(OBJ_CONSTRAINT);
    	obj.setDescription("An assembly constraint");
    	obj.addFootnote("At this time, only constraint type \"" + JLConstraintInput.CONSTRAINT_CSYS + "\" is fully supported.");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Constraint type");
    	arg.setRequired(true);
    	arg.setValidValues(new String[] {
    	    	JLConstraintInput.CONSTRAINT_ALIGN,
    	    	JLConstraintInput.CONSTRAINT_AUTO,
    	    	JLConstraintInput.CONSTRAINT_CSYS,
    	    	JLConstraintInput.CONSTRAINT_DEFPLMT,
    	    	JLConstraintInput.CONSTRAINT_EDGEONSRF,
    	    	JLConstraintInput.CONSTRAINT_FIX,
    	    	JLConstraintInput.CONSTRAINT_INSERT,
    	    	JLConstraintInput.CONSTRAINT_MATE,
    	    	JLConstraintInput.CONSTRAINT_ORIENT,
    	    	JLConstraintInput.CONSTRAINT_PNTONLINE,
    	    	JLConstraintInput.CONSTRAINT_PNTONSRF,
    	    	JLConstraintInput.CONSTRAINT_SUBST,
    	    	JLConstraintInput.CONSTRAINT_TANGENT,
    	});
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_ASMREF, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Reference on the assembly");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_COMPREF, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Reference on the component");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_ASMDATUM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Datum side on the assembly (for appropriate constraint types)");
    	arg.setValidValues(new String[] {DATUM_SIDE_RED, DATUM_SIDE_YELLOW});
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_COMPDATUM, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Datum side on the component (for appropriate constraint types)");
    	arg.setValidValues(new String[] {DATUM_SIDE_RED, DATUM_SIDE_YELLOW});
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_OFFSET, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Offset from the assembly reference (for appropriate constraint types)");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionObject helpJLPoint() {
    	FunctionObject obj = new FunctionObject(OBJ_POINT);
    	obj.setDescription("A 3D coordinate");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_X, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("X-coordinate");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_Y, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Y-coordinate");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_Z, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Z-coordinate");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionObject helpJLTransform() {
    	FunctionObject obj = new FunctionObject(OBJ_TRANSFORM);
    	obj.setDescription("A 3D Transform matrix for a component");
    	obj.addFootnote("On input, you may set either the axes or the rotations but you may not set both");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_ORIGIN, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	arg.setDescription("Matrix origin");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_XAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	arg.setDescription("Matrix X Axis");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_YAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	arg.setDescription("Matrix Y Axis");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_ZAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	arg.setDescription("Matrix Z Axis");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_X_ROT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("X rotation in degrees");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_Y_ROT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Y rotation in degrees");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_Z_ROT, FunctionSpec.TYPE_DOUBLE);
    	arg.setDescription("Z rotation in degrees");
    	obj.add(arg);

        return obj;
    }
    
	private FunctionObject helpJLInertia() {
    	FunctionObject obj = new FunctionObject(OBJ_INERTIA);
    	obj.setDescription("A matrix representing an inertia or inertia tensor");

    	FunctionArgument arg;
    	arg = new FunctionArgument(PARAM_XAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	arg.setDescription("Matrix X Axis");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_YAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	arg.setDescription("Matrix Y Axis");
    	obj.add(arg);

    	arg = new FunctionArgument(PARAM_ZAXIS, FunctionSpec.TYPE_OBJECT, OBJ_POINT);
    	arg.setDescription("Matrix Z Axis");
    	obj.add(arg);

        return obj;
    }
    
	private Map<String, Object> makeFileMaterial(String filename, String material) {

		Map<String, Object> rec = new OrderedMap<String, Object>();
		if (filename!=null)
			rec.put(JLFileResponseParams.OUTPUT_MODEL, filename);
		if (material!=null)
			rec.put(JLFileResponseParams.OUTPUT_MATERIAL, material);
		
		return rec;
	}

	private FunctionObject helpFileMaterial() {
    	FunctionObject obj = new FunctionObject(OBJ_FILEMATERIAL);
    	obj.setDescription("An object representing a part and material pair");

    	FunctionArgument arg;
    	arg = new FunctionArgument(OUTPUT_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Part name");
    	obj.add(arg);

    	arg = new FunctionArgument(OUTPUT_MATERIAL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Material name");
    	obj.add(arg);

        return obj;
    }
    
}
