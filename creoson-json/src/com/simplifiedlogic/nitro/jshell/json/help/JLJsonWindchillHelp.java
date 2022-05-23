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

import com.simplifiedlogic.nitro.jshell.json.request.JLWindchillRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLWindchillResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "windchill" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonWindchillHelp extends JLJsonCommandHelp implements JLWindchillRequestParams, JLWindchillResponseParams {

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
		list.add(helpAuthorize());
		list.add(helpClearWorkspace());
		list.add(helpCreateWorkspace());
		list.add(helpDeleteWorkspace());
		list.add(helpListWorkspaces());
		list.add(helpServerExists());
		list.add(helpSetServer());
		list.add(helpSetWorkspace());
		list.add(helpGetWorkspace());
		list.add(helpWorkspaceExists());
		list.add(helpFileCheckedOut());
		list.add(helpListWorkspaceFiles());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		return null;
	}
	
	private FunctionTemplate helpAuthorize() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_AUTHORIZE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Set user's Windchill login/password");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_USER, FunctionSpec.TYPE_STRING);
    	arg.setDescription("User's Windchill login");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_PASSWORD, FunctionSpec.TYPE_STRING);
    	arg.setDescription("User's Windchill password");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_USER, "fmercer");
    	ex.addInput(PARAM_PASSWORD, "abc123");
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpSetServer() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_SERVER);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Select a Windchill server");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_SERVER_URL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Server URL or Alias");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_SERVER_URL, "http://myserver.com/Windchill");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SERVER_URL, "Main_Server");
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpServerExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SERVER_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether a server exists");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_SERVER_URL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Server URL or Alias");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the server exists");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_SERVER_URL, "http://myserver.com/Windchill");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SERVER_URL, "http://myserver.com");
    	ex.addOutput(OUTPUT_EXISTS, false);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_SERVER_URL, "Main_Server");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpListWorkspaces() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_WORKSPACES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a list of workspaces the user can access on the active server");
    	FunctionReturn ret;

    	ret = new FunctionReturn(OUTPUT_WORKSPACES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of workspaces");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_WORKSPACES, new String[] {"freds_workspace", "engineering", "group_ws", "ws12345"});
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpSetWorkspace() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SET_WORKSPACE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Select a workspace on the active server");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_WORKSPACE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Workspace name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "freds_workspace");
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpGetWorkspace() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_GET_WORKSPACE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Retrieve the name of the active workspace on the active server");
    	FunctionReturn ret;

    	ret = new FunctionReturn(OUTPUT_WORKSPACE, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Workspace name");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(PARAM_WORKSPACE, "freds_workspace");
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpWorkspaceExists() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_WORKSPACE_EXISTS);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether a workspace exists on the active server");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_WORKSPACE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Workspace name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_EXISTS, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the workspace exists");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "freds_workspace");
    	ex.addOutput(OUTPUT_EXISTS, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "sams_workspace");
    	ex.addOutput(OUTPUT_EXISTS, false);
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpCreateWorkspace() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CREATE_WORKSPACE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Create a workspace on the active server");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_WORKSPACE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Workspace name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_CONTEXT, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Windchill context");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "freds_workspace");
    	ex.addInput(PARAM_CONTEXT, "ctx1");
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpClearWorkspace() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CLEAR_WORKSPACE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Clear a workspace on the active server");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_WORKSPACE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Workspace name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAMES, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	arg.setDescription("List of files to delete from the workspace");
    	arg.setDefaultValue("All files are deleted.");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "test_workspace");
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "test_workspace");
    	List<String> files = new ArrayList<String>();
    	files.add("able.prt");
    	files.add("baker.prt");
    	files.add("charlie.prt");
    	ex.addInput(PARAM_FILENAMES, files);
    	template.addExample(ex);

    	return template;
    }
    	
	private FunctionTemplate helpDeleteWorkspace() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DELETE_WORKSPACE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Delete a workspace on the active server");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_WORKSPACE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Workspace name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "test_workspace");
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpFileCheckedOut() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_FILE_CHECKED_OUT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether a file is checked out in a workspace on the active server");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_WORKSPACE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Workspace name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_CHECKED_OUT, FunctionSpec.TYPE_BOOL);
    	ret.setDescription("Whether the file is checked out in the workspace");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "freds_workspace");
    	ex.addInput(PARAM_FILENAME, "box.prt");
    	ex.addOutput(OUTPUT_CHECKED_OUT, true);
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_WORKSPACE, "freds_workspace");
    	ex.addInput(PARAM_FILENAME, "box-x.prt");
    	ex.addOutput(OUTPUT_CHECKED_OUT, false);
    	template.addExample(ex);
    	
        return template;
    }
    	
	private FunctionTemplate helpListWorkspaceFiles() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_WORKSPACE_FILES);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Get a list of files in a workspace on the active server");
    	FunctionArgument arg;
    	FunctionReturn ret;

    	arg = new FunctionArgument(PARAM_FILENAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name pattern");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All files");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_FILELIST, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of files in the workspace");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_FILELIST, new String[] {"able.prt", "baker.prt", "abc123.asm", "abc93939.asm"});
    	template.addExample(ex);
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_FILENAME, "abc*");
    	ex.addOutput(OUTPUT_FILELIST, new String[] {"abc123.asm", "abc93939.asm"});
    	template.addExample(ex);
    	
        return template;
    }
    	
}
