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
package com.simplifiedlogic.nitro.jshell.funcs;

import java.util.ArrayList;
import java.util.List;

import com.simplifiedlogic.nitro.jshell.MainServer;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionReturn;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for server-family functions
 * 
 * @author Adam Andrews
 *
 */
public class ServerJsonHelp implements ServerRequestParams, ServerResponseParams {

	/**
	 * Generates a list of help doc objects for all the functions in the family
	 * @return A list of FunctionTemplate objects
	 */
	public List<FunctionTemplate> getHelp() {
		List<FunctionTemplate> list = new ArrayList<FunctionTemplate>();
		list.add(helpPwd());
		return list;
	}
	
	/**
	 * Generates a list of help doc objects for all the shared data objects in the family 
	 * @return A list of FunctionObject objects
	 */
	public List<FunctionObject> getHelpObjects() {
		return null;
	}
	
	/**
	 * Generate help doc for the "pwd" function.
	 * 
	 * @return A help doc object
	 */
	private FunctionTemplate helpPwd() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_PWD);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Return the server's execution directory");
    	spec.addFootnote("*** WARNING *** WARNING *** WARNING ***");
    	spec.addFootnote("-");
    	spec.addFootnote("The endpoint for this function must be " + MainServer.ENDPOINT_SERVER + ", not " + MainServer.ENDPOINT_CREOSON);
    	spec.addFootnote("-");
    	spec.addFootnote("*** WARNING *** WARNING *** WARNING ***");
    	FunctionReturn ret;
    	
    	ret = new FunctionReturn(OUTPUT_DIRNAME, FunctionSpec.TYPE_STRING);
    	ret.setDescription("Full name of working directory");
    	spec.addReturn(ret);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_DIRNAME, "c:/CreosonServer");
    	template.addExample(ex);
    	
        return template;
    }
    
}
