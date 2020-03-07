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
package com.simplifiedlogic.nitro.jshell.json.request;

/**
 * Constants defining the JSON request parameters for the connection command
 * 
 * @author Adam Andrews
 */
public interface JLConnectRequestParams {

	// command name
	public static final String COMMAND			= "connection";

	// function names
	public static final String FUNC_CONNECT		= "connect";
    public static final String FUNC_DISCONNECT	= "disconnect";
    public static final String FUNC_START_PROE	= "start_creo";
    public static final String FUNC_STOP_PROE	= "stop_creo";
    public static final String FUNC_IS_RUNNING	= "is_creo_running";
    public static final String FUNC_KILL_PROE	= "kill_creo";
    
    // request fields
    public static final String PARAM_PRODUCT	= "product";
    public static final String PARAM_SYSTEM		= "system";
    public static final String PARAM_COMMANDLOG	= "commandlog";
    public static final String PARAM_START_DIR	= "start_dir";
    public static final String PARAM_START_COMMAND	= "start_command";
    public static final String PARAM_RETRIES	= "retries";
    public static final String PARAM_USE_DESKTOP	= "use_desktop";
//    public static final String PARAM_START_TEMP_SERVICE	= "start_temp_service";
//    public static final String PARAM_CREOSON_COMMAND_DIR	= "creoson_command_dir";
//    public static final String PARAM_CREOSON_PORT	= "creoson_port";

    // special data value for connect function
	public static final String APP_PROE = "pro/e";

    public static final String ALLOWED_PROE_CMD = "nitro_proe_remote.bat";

}
