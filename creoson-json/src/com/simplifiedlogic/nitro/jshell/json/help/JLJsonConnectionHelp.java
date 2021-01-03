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

import com.simplifiedlogic.nitro.jshell.json.request.JLConnectRequestParams;
import com.simplifiedlogic.nitro.jshell.json.response.JLConnectResponseParams;
import com.simplifiedlogic.nitro.jshell.json.response.ServiceStatus;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionArgument;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionExample;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionObject;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionSpec;
import com.simplifiedlogic.nitro.jshell.json.template.FunctionTemplate;

/**
 * Generate help doc for "connection" functions
 * 
 * @author Adam Andrews
 *
 */
public class JLJsonConnectionHelp extends JLJsonCommandHelp implements JLConnectRequestParams, JLConnectResponseParams {

	public static final String OBJ_SERVICE_STATUS = "ServiceStatus";
	
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
		list.add(helpConnect());
		list.add(helpDisconnect());
		list.add(helpIsCreoRunning());
		list.add(helpKillCreo());
		list.add(helpStartCreo());
		list.add(helpStopCreo());
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.simplifiedlogic.nitro.jshell.json.help.JLJsonCommandHelp#getHelpObjects()
	 */
	public List<FunctionObject> getHelpObjects() {
		List<FunctionObject> list = new ArrayList<FunctionObject>();
		list.add(helpServiceStatus());
		return list;
	}
	
    private FunctionObject helpServiceStatus() {
    	FunctionObject obj = new FunctionObject(OBJ_SERVICE_STATUS);
    	obj.setDescription("Status of service call, returned in each function response");

    	FunctionArgument arg;
    	arg = new FunctionArgument(ServiceStatus.PARAM_ERROR, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether an error occurred on the call");
    	obj.add(arg);

    	arg = new FunctionArgument(ServiceStatus.PARAM_MESSAGE, FunctionSpec.TYPE_STRING);
    	arg.setDescription("An error message, if an error occurred");
    	obj.add(arg);

    	arg = new FunctionArgument(ServiceStatus.PARAM_EXPIRED, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether the user's session has expired");
    	obj.add(arg);

        return obj;
    }
    
    private FunctionTemplate helpConnect() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_CONNECT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Connect to CREOSON");
    	spec.setHasSessionInput(false);
    	spec.setHasSessionOutput(true);
        
    	FunctionExample ex;

    	ex = new FunctionExample();
    	template.addExample(ex);
    	
        return template;
    }
    
    private FunctionTemplate helpDisconnect() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_DISCONNECT);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Disconnect from CREOSON");
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	template.addExample(ex);
    	
        return template;
    }
    
    private FunctionTemplate helpIsCreoRunning() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_IS_RUNNING);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Check whether Creo is running.");
    	spec.setHasSessionInput(false);
    	spec.addFootnote("This function tests whether the current connection is still active; if there is no active connection, then it tries to make a new connection to Creo and returns whether the connection succeeds.");
    	spec.addFootnote("The sessionId is optional, and ignored.");
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addOutput(OUTPUT_RUNNING, true);
    	template.addExample(ex);
    	
        return template;
    }
    
    private FunctionTemplate helpStartCreo() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_START_PROE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Execute an external .bat file to start Creo, then attempts to connect to Creo.");
    	spec.setHasSessionInput(false);
    	spec.addFootnote("The .bat file is restricted to a specific name to make the function more secure.");
    	spec.addFootnote("Set " + PARAM_RETRIES + " to 0 to NOT attempt to connect to Creo.");
    	spec.addFootnote("The server will pause for 3 seconds before attempting a connection, and will pause for 10 seconds between connection retries");
    	spec.addFootnote("If Creo pops up a message after startup, this function may cause Creo to crash unless retries is set to 0.");
    	spec.addFootnote("If "+PARAM_USE_DESKTOP+" is set, make sure that your "+"nitro_proe_remote.bat"+" file contains a cd command to change to the directory where you want Creo to start!");
    	FunctionArgument arg;

    	arg = new FunctionArgument(PARAM_START_DIR, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Directory containing the .bat file");
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_START_COMMAND, FunctionSpec.TYPE_STRING);
    	arg.setDescription("Name of the .bat file");
    	arg.setValidValues(new String[] {ALLOWED_PROE_CMD});
    	arg.setRequired(true);
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_RETRIES, FunctionSpec.TYPE_INTEGER);
    	arg.setDescription("Number of retries to make when connecting");
    	arg.setDefaultValue("20");
    	spec.addArgument(arg);

    	arg = new FunctionArgument(PARAM_USE_DESKTOP, FunctionSpec.TYPE_BOOL);
    	arg.setDescription("Whether to use the desktop to start creo rather than the java runtime.  Should only be used if the runtime method doesn't work.");
    	arg.setDefaultValue("false");
    	spec.addArgument(arg);

    	FunctionExample ex;

    	ex = new FunctionExample();
    	ex.addInput(PARAM_START_DIR, "C:/myfiles");
    	ex.addInput(PARAM_START_COMMAND, ALLOWED_PROE_CMD);
    	ex.addInput(PARAM_RETRIES, 5);
    	template.addExample(ex);

    	ex = new FunctionExample();
    	ex.addInput(PARAM_START_DIR, "C:/myfiles");
    	ex.addInput(PARAM_START_COMMAND, ALLOWED_PROE_CMD);
    	ex.addInput(PARAM_RETRIES, 0);
    	template.addExample(ex);

        return template;
    }
    
    private FunctionTemplate helpStopCreo() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_STOP_PROE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Disconnect current session from Creo and cause Creo to exit.");
    	spec.addFootnote("NOTE that this will cause Creo to exit cleanly.");
    	spec.addFootnote("If there is no current connection to Creo, this function will do nothing.");
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	template.addExample(ex);
    	
        return template;
    }
    
    private FunctionTemplate helpKillCreo() {
    	FunctionTemplate template = new FunctionTemplate(COMMAND, FUNC_KILL_PROE);
    	FunctionSpec spec = template.getSpec();
    	spec.setFunctionDescription("Kill primary Creo processes.");
    	spec.setHasSessionInput(false);
    	spec.addFootnote("This will kill the 'xtop.exe' and 'nmsd.exe' processes by name.");
    	spec.addFootnote("The sessionId is optional, and ignored.");
    	
    	FunctionExample ex;

    	ex = new FunctionExample();
    	template.addExample(ex);
    	
        return template;
    }
    
}
