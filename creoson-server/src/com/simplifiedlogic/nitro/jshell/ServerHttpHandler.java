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
package com.simplifiedlogic.nitro.jshell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Hashtable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplifiedlogic.nitro.jshell.funcs.ServerJsonHandler;
import com.simplifiedlogic.nitro.jshell.json.JShellJsonHandler;
import com.simplifiedlogic.nitro.jshell.json.request.BaseRequest;
import com.simplifiedlogic.nitro.jshell.json.response.BaseResponse;
import com.simplifiedlogic.nitro.jshell.json.response.ServiceStatus;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Implementation of HttpHandler which handles JSON requests for special 
 * server-only functions
 * 
 * @author Adam Andrews
 */
public class ServerHttpHandler implements HttpHandler {
	
	/**
	 * Handler for Server JSON calls 
	 */
	private ServerJsonHandler handler = new ServerJsonHandler();

	/**
	 * Object for converting JSON strings to java objects and back again
	 */
	private ObjectMapper mapper = new ObjectMapper();
	
	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
		
		// retrieve the request json data
		InputStream is = t.getRequestBody();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len;
		while ((len = is.read(buffer))>0) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		String data = new String(bos.toByteArray());
		System.out.println("Request: \n    " + data);

		// pass the data to the handler and receive a response
		String response = handleRequest(data);
		System.out.println("    " + response);
		
		// format and return the response to the user
		t.sendResponseHeaders(200, response.getBytes(Charset.forName("UTF-8")).length);
		t.getResponseHeaders().set("Content-Type", "application/json, charset=UTF-8");
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes(Charset.forName("UTF-8")));
		os.close();
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

		try {
			// pass the request to an external handler
			Hashtable<String, Object> output = handler.handleFunction(req.getSessionId(), req.getFunction(), req.getData());

			// insert the results into the data portion of the response object 
			resp.setData(output);

			if (JShellJsonHandler.alwaysIncludeStatus) {
				// force a status object to always be returned, to make things easier on the user
				// when checking for errors
				ServiceStatus status = new ServiceStatus();
				status.setError(false);
				resp.setStatus(status);
			}
		}
		catch (ServerException e) {
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
	
}
