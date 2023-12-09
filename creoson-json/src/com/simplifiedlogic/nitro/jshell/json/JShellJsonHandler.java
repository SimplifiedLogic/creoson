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
package com.simplifiedlogic.nitro.jshell.json;

import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplifiedlogic.nitro.jlink.intf.JShellProvider;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonBomHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCommandHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonConnectionHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonCreoHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonDimensionHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonDrawingHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonFamilyTableHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonFeatureHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonFileHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonGeometryHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonInterfaceHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonLayerHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonNoteHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonParameterHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonViewHandler;
import com.simplifiedlogic.nitro.jshell.json.handler.JLJsonWindchillHandler;
import com.simplifiedlogic.nitro.jshell.json.request.BaseRequest;
import com.simplifiedlogic.nitro.jshell.json.response.BaseResponse;
import com.simplifiedlogic.nitro.jshell.json.response.JLConnectResponseParams;
import com.simplifiedlogic.nitro.jshell.json.response.ServiceStatus;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * This is the main entry point for the JSON wrapper for the JShell library.
 * 
 * <p>JSON requests are received here and forwarded to the appropriate handler
 * for the given function family.
 * 
 * @author Adam Andrews
 *
 */
public class JShellJsonHandler {

	/**
	 * Whether to always include the ServerStatus object on all responses, even
	 * if there is no special status to return.
	 */
	public static final boolean alwaysIncludeStatus = true;

	/**
	 * The collection of function handlers for the various function families.  Keyed by command name.
	 */
	private Map<String, JLJsonCommandHandler> commands = new HashMap<String, JLJsonCommandHandler>();
	
	/**
	 * Object for converting JSON strings to java objects and back again
	 */
	private ObjectMapper mapper = new ObjectMapper();
	
	public JShellJsonHandler() {
    	File file = getLibraryFile();
    	// do this instead of loading it ourselves, because it needs to be loaded 
    	// in the jshell.core package classloader in order to be used by jshell.core classes.
		if (file.exists())
			JShellProvider.setNativeLibraryFile(file);
		else
			System.err.println("Native library not found: "+file.getAbsolutePath());

		JShellProvider jp = JShellProvider.getInstance();
		jp.initializeStandalone();
		if (jp!=null) {
			// assemble the collection of function handlers
			commands.put(JLJsonConnectionHandler.COMMAND, new JLJsonConnectionHandler(jp.getJLConnection()));
			commands.put(JLJsonFileHandler.COMMAND, new JLJsonFileHandler(jp.getJLFile()));
			commands.put(JLJsonParameterHandler.COMMAND, new JLJsonParameterHandler(jp.getJLParameter()));
			commands.put(JLJsonCreoHandler.COMMAND, new JLJsonCreoHandler(jp.getJLProe()));
			commands.put(JLJsonFamilyTableHandler.COMMAND, new JLJsonFamilyTableHandler(jp.getJLFamilyTable()));
			commands.put(JLJsonFeatureHandler.COMMAND, new JLJsonFeatureHandler(jp.getJLFeature()));
			commands.put(JLJsonGeometryHandler.COMMAND, new JLJsonGeometryHandler(jp.getJLGeometry()));
			commands.put(JLJsonNoteHandler.COMMAND, new JLJsonNoteHandler(jp.getJLNote()));
			commands.put(JLJsonInterfaceHandler.COMMAND, new JLJsonInterfaceHandler(jp.getJLTransfer()));
			commands.put(JLJsonViewHandler.COMMAND, new JLJsonViewHandler(jp.getJLView()));
			commands.put(JLJsonLayerHandler.COMMAND, new JLJsonLayerHandler(jp.getJLLayer()));
			commands.put(JLJsonDrawingHandler.COMMAND, new JLJsonDrawingHandler(jp.getJLDrawing()));
			commands.put(JLJsonWindchillHandler.COMMAND, new JLJsonWindchillHandler(jp.getJLWindchill()));
			commands.put(JLJsonDimensionHandler.COMMAND, new JLJsonDimensionHandler(jp.getJLDimension()));
			commands.put(JLJsonBomHandler.COMMAND, new JLJsonBomHandler(jp.getJLBom()));
		}

	}
	
	/**
	 * Handle a JSON request and return its results
	 * @param reqString The JSON request string
	 * @return The JSON response string
	 */
	public String handleRequest(String reqString) {
		BaseRequest req = null;
		BaseResponse output = null;
		
		// turn the JSON string into a standard request object
		try {
			// parse json
			req = (BaseRequest)mapper.readValue(reqString, BaseRequest.class);
		}
		catch (Exception e) {
			output = new BaseResponse();
			createError(output, "Invalid JSON input: " + reqString);
			try {
				return mapper.writeValueAsString(output);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		// pass the request to the handler and receive a response
		output = handleRequest(req);
		if (output==null)
			output = new BaseResponse();
		if (output.getStatus()==null)
			output.setStatus(new ServiceStatus());
		
		if (req.isEcho())
			output.setEchoString(reqString);

		// turn the response object into a JSON string and return
		try {
			// unparse json
			String resp = mapper.writeValueAsString(output);
			return resp;
		}
		catch (Exception e) {
			createError(output, "Invalid JSON output: " + reqString);
			try {
				return mapper.writeValueAsString(output);
			}
			catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
		
	}

	/**
	 * Handle a standard request by passing it on to a handler class, and return its results
	 * @param req The request in the form of a standard request object
	 * @return The response in the form of a standard response object
	 */
	public BaseResponse handleRequest(BaseRequest req) {
		BaseResponse resp = new BaseResponse();
		
		// check for empty or invalid request
		if (req==null) {
			createError(resp, "Empty request");
			return resp;
		}
		
		if (req.getCommand()==null) {
			createError(resp, "Request is missing the 'command' property");
			return resp;
		}
		
		JLJsonCommandHandler handler = commands.get(req.getCommand());
		// check for invalid command
		if (handler==null) {
			createError(resp, "Invalid command: " + req.getCommand());
			return resp;
		}
		
		if (req.getFunction()==null) {
			createError(resp, "Request is missing the 'function' property");
			return resp;
		}
		
		try {
			// pass the request to an external handler
			Hashtable<String, Object> output = handler.handleFunction(req.getSessionId(), req.getFunction(), req.getData());

			if (output!=null) {
				// special handling for when a new session ID is returned in the data; 
				// set it into the response and remove it from the data
				if (output.get(JLConnectResponseParams.OUTPUT_SESSIONID)!=null) {
					resp.setSessionId(output.get(JLConnectResponseParams.OUTPUT_SESSIONID).toString());
					output.remove(JLConnectResponseParams.OUTPUT_SESSIONID);
					if (output.size()==0)
						output = null;
				}
			}
			
			// insert the results into the data portion of the response object 
			resp.setData(output);

			if (alwaysIncludeStatus) {
				// force a status object to always be returned, to make things easier on the user
				// when checking for errors
				ServiceStatus status = new ServiceStatus();
				status.setError(false);
				resp.setStatus(status);
			}
		}
		catch (JLIException e) {
			createError(resp, e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			if (e instanceof NullPointerException)
				msg = "Unexpected null value";
			createError(resp, "Error handling request: " + msg);
		}
		
		return resp;
	}
	
	/**
	 * Generate an error status return
	 * @param resp The response object to receive the error status
	 * @param msg The error message
	 */
	private void createError(BaseResponse resp, String msg) {
		ServiceStatus status = new ServiceStatus();
		status.setMessage(msg);
		status.setError(true);
		resp.setStatus(status);
	}
	
	private File getLibraryFile() {
		String libname = JShellProvider.NATIVE_LIBNAME;
		String filename = libname+".dll";
		
		return new File(filename);
	}
}
