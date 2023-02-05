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

import com.simplifiedlogic.nitro.jshell.json.request.JLCreoRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLFamilyTableRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLFeatureRequestParams;
import com.simplifiedlogic.nitro.jshell.json.request.JLFileRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLCreoResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "creo" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonCreoHelp extends JLJsonCommandHelp implements JLCreoRequestParams, JLCreoResponseParams {

	String[] validColors;
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getCommand()
	 */
	public String getCommand() {
		return COMMAND;
	}

	/**
	 * 
	 */
	public JLJsonCreoHelp() {
		validColors = new String[] {
			STD_COLOR_LETTER,
			STD_COLOR_HIGHLIGHT,
			STD_COLOR_DRAWING,
			STD_COLOR_BACKGROUND,
			STD_COLOR_HALF_TONE,
			STD_COLOR_EDGE_HIGHLIGHT,
			STD_COLOR_DIMMED,
			STD_COLOR_ERROR,
			STD_COLOR_WARNING,
			STD_COLOR_SHEETMETAL,
			STD_COLOR_CURVE,
			STD_COLOR_PRESEL_HIGHLIGHT,
			STD_COLOR_SELECTED,
			STD_COLOR_SECONDARY_SELECTED,
			STD_COLOR_PREVIEW,
			STD_COLOR_SECONDARY_PREVIEW,
			STD_COLOR_DATUM,
			STD_COLOR_QUILT
		};
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpCd());
		list.add(helpDeleteFiles());
		list.add(helpGetConfig());
		list.add(helpGetStandardColor());
		list.add(helpListDirs());
		list.add(helpListFiles());
		list.add(helpMkdir());
		list.add(helpPwd());
		list.add(helpRmdir());
		list.add(helpSetConfig());
		list.add(helpSetCreoVersion());
		list.add(helpSetStandardColor());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		return null;
	}
	
	private FunctionTemplate helpCd() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CD);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Change Creo's working directory");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New directory name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Name of new working directory");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "c:\\myfiles\\testing");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/testing");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpMkdir() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_MKDIR);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Create a new directory");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New directory name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Full name of new working directory");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "C:\\testfiles\\sub");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/testfiles/sub");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "subdir");
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/testing/subdir");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpPwd() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_PWD);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Return Creo's current working directory");
    	FunctionReturn ret;
    	
    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Full name of working directory");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_DIRNAME, "C:/myfiles/testing");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpListDirs() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_DIRS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List subdirectories of Creo's current working directory");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory name filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All subdirectories are listed");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_DIRLIST, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of subdirectories");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_DIRLIST, new String[] {"project","testing","working"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "*ing");
    	ex.addOutput(OUTPUT_DIRLIST, new String[] {"testing","working"});
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpListFiles() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_FILES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List files in Creo's current working directory");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name filter");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All files are listed");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FILELIST, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of files");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_FILELIST, new String[] {"notes.txt","box.prt","bracket.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "*prt");
    	ex.addOutput(OUTPUT_DIRLIST, new String[] {"box.prt","bracket.prt"});
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpDeleteFiles() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_FILES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete files from a directory working directory");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory name");
    	arg.setDefaultValue("Creo's current working directory");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name filter; only used if " + PARAM_FILENAMES + " is not given");
    	arg.setWildcards(true);
//    	arg.setDefaultValue("The " + PARAM_FILENAMES + " parameter is used; if both are blank, all files will be deleted");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of file names");
    	arg.setDefaultValue("The " + PARAM_FILENAME + " parameter is used; if both are blank, all files will be deleted");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FILELIST, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of deleted files");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_FILELIST, new String[] {"notes.txt","box.prt","bracket.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "C:/myfiles/parts");
    	ex.addInput(PARAM_FILENAME, "*prt");
    	ex.addOutput(OUTPUT_DIRLIST, new String[] {"box.prt","bracket.prt"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_FILENAMES, new String[] {"box.prt","notes.txt","nofile.txt"});
    	ex.addOutput(OUTPUT_DIRLIST, new String[] {"box.prt","notes.txt"});
    	template.addExample(ex);
    	
    	ex.addInput(PARAM_FILENAME, "nofile.txt");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpRmdir() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_RMDIR);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a new directory");
    	spec.addFootnote("Directory must be empty before deleting");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_DIRNAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory name to delete");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "C:\\testfiles\\sub");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_DIRNAME, "subdir");
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSetConfig() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_CONFIG);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set a Creo config option");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Option name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_VALUE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("New option value");
    	arg.setDefaultValue("Clear the option");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_IGNORE_ERRORS, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to ignore errors that might occur when setting the config option");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "allow_save_as_instance");
    	ex.addInput(PARAM_VALUE, "no");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "allow_save_as_instance");
    	ex.addInput(PARAM_VALUE, "no");
    	ex.addInput(PARAM_IGNORE_ERRORS, true);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetConfig() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_CONFIG);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get the value of a Creo config option");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Option name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_VALUES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of option values (some options can have multiple values)");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "allow_save_as_instance");
    	ex.addOutput(OUTPUT_VALUES, new String[] {"yes"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "search_path");
    	ex.addOutput(OUTPUT_VALUES, new String[] {"C:/myfiles/parts","C:/somefiles/parts"});
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSetStandardColor() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_STD_COLOR);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set one of Creo's standard colors");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_COLOR_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Color type");
    	arg.setRequired(true);
    	arg.setValidValues(validColors);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_RED, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Red value (0-255)");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_GREEN, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Green value (0-255)");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_BLUE, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Blue value (0-255)");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_COLOR_TYPE, STD_COLOR_HIGHLIGHT);
    	ex.addInput(PARAM_RED, 255);
    	ex.addInput(PARAM_GREEN, 127);
    	ex.addInput(PARAM_BLUE, 127);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpGetStandardColor() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_STD_COLOR);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get one of Creo's standard colors");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_COLOR_TYPE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Color type");
    	arg.setRequired(true);
    	arg.setValidValues(validColors);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_RED, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Red value (0-255)");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_GREEN, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Green value (0-255)");
    	spec.addReturn(ret);

    	ret = new FunctionReturn(OUTPUT_BLUE, FunctionSpec.TYPE_INTEGER);
    	ret.setDescription("Blue value (0-255)");
    	spec.addReturn(ret);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_COLOR_TYPE, STD_COLOR_HIGHLIGHT);
    	ex.addOutput(OUTPUT_RED, 255);
    	ex.addOutput(OUTPUT_GREEN, 127);
    	ex.addOutput(OUTPUT_BLUE, 127);
    	template.addExample(ex);
    	
        return template;
    }
    
	private FunctionTemplate helpSetCreoVersion() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_CREO_VERSION);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set the version of Creo you are running");
    	spec.addFootnote("This function only needs to be called once per creoson session.");
    	spec.addFootnote("This function must be called if you are doing certain functions in Creo 7 or later due to deprecated config options.");
    	spec.addFootnote("At this time this function only supports 7, 8 and 9.");
    	spec.addFootnote("This is needed for functions: "+
    			JLFamilyTableRequestParams.COMMAND+":"+JLFamilyTableRequestParams.FUNC_REPLACE+", "+
    			JLFileRequestParams.COMMAND+":"+JLFileRequestParams.FUNC_ASSEMBLE+", "+
    			JLFileRequestParams.COMMAND+":"+JLFileRequestParams.FUNC_REGENERATE+", "+
    			JLFeatureRequestParams.COMMAND+":"+JLFeatureRequestParams.FUNC_DELETE+", "+
    			JLFeatureRequestParams.COMMAND+":"+JLFeatureRequestParams.FUNC_RESUME+", "+
    			JLFeatureRequestParams.COMMAND+":"+JLFeatureRequestParams.FUNC_SUPPRESS+"");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_VERSION, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Creo version");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_VERSION, "7");
    	template.addExample(ex);
    	
        return template;
    }
    
}
