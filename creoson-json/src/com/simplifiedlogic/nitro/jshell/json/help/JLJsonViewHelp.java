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

import com.simplifiedlogic.nitro.jshell.json.request.JLViewRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLViewResponseParams;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "view" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonViewHelp extends JLJsonCommandHelp implements JLViewRequestParams, JLViewResponseParams {

	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelp()
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpActivate());
		list.add(helpList());
		list.add(helpListExploded());
		list.add(helpSave());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		return null;
	}
	
	private FunctionTemplate helpList() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List views that match criteria");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All views are listed");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_VIEWLIST, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of view names");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_VIEWLIST, new String[] {"FRONT", "BACK","RIGHT","LEFT","TOP","BOTTOM"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "*O*");
    	ex.addOutput(OUTPUT_VIEWLIST, new String[] {"FRONT", "TOP","BOTTOM"});
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpListExploded() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_LIST_EXPLODED);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("List views that match criteria and are exploded");
    	FunctionArgument arg;
    	FunctionReturn ret;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setWildcards(true);
    	arg.setDefaultValue("All views are listed");
    	spec.addArgument(arg);

    	ret = new FunctionReturn(OUTPUT_VIEWLIST, FunctionSpec.TYPE_ARRAY, FunctionSpec.TYPE_STRING);
    	ret.setDescription("List of view names");
    	spec.addReturn(ret);
    	
    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addOutput(OUTPUT_VIEWLIST, new String[] {"Exp001", "BACK_EXP"});
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "*A*");
    	ex.addOutput(OUTPUT_VIEWLIST, new String[] {"BACK_EXP"});
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpActivate() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_ACTIVATE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Activate a model view");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "FRONT");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "RIGHT");
    	template.addExample(ex);

    	return template;
	}

	private FunctionTemplate helpSave() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_SAVE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Save a model's current orientation as a new view");
    	FunctionArgument arg;
    	
    	arg = new FunctionArgument(PARAM_MODEL, FunctionSpec.TYPE_STRING);
    	arg.setDescription("File name");
    	arg.setDefaultValue("The currently active model");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_NAME, FunctionSpec.TYPE_STRING);
    	arg.setDescription("View name");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	FunctionExample ex;
    	
    	ex = new FunctionExample();
    	ex.addInput(PARAM_MODEL, "box.prt");
    	ex.addInput(PARAM_NAME, "west_view");
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_NAME, "north");
    	template.addExample(ex);

    	return template;
	}

}
