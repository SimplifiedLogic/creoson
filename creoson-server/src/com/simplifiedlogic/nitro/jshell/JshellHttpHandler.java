/*
 * MIT LICENSE
 * Copyright 2000-2018 Simplified Logic, Inc
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.sql.Timestamp;

import com.simplifiedlogic.nitro.jshell.json.JShellJsonHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Implementation of HttpHandler which handles JSON requests for CREOSON functions
 * 
 * @author Adam Andrews
 */
public class JshellHttpHandler implements HttpHandler {
	
	/**
	 * Handler for CREOSON JSON calls 
	 */
	private JShellJsonHandler handler = new JShellJsonHandler();

	/**
	 * The writer for the current log file.
	 */
	private FileWriter log = null;

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
		String data = new String(bos.toByteArray(), Charset.forName("UTF-8"));
		logit("Request: \n    " + data);

		// pass the data to the handler and receive a response
		String response = handler.handleRequest(data);
		logit("    " + response);
		
		// format and return the response to the user
		t.sendResponseHeaders(200, response.getBytes(Charset.forName("UTF-8")).length);
//		t.getResponseHeaders().set("Content-Type", "application/json");
		t.getResponseHeaders().set("Content-Type", "application/json, charset=UTF-8");
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes(Charset.forName("UTF-8")));
		os.close();
	}

	/**
	 * Write a log message to the debug log.
	 * 
	 * <p>Properly speaking, this needs to be converted to log4j.
	 * 
	 * @param msg The message to write to the log.
	 */
	private void logit(String msg) {
		try {
			if (log==null) {
				File f = new File("logs");
				if (!f.exists())
					f.mkdir();
				log = new FileWriter("logs/json.log");
			}
			Timestamp ts = new Timestamp(System.currentTimeMillis());
			log.write(ts.toString());
			log.write(": ");
			log.write(msg);
			log.write("\r\n");
			log.flush();
		}
		catch (Exception e) {
			System.err.println("Error writing to log: " + e.getLocalizedMessage());
		}
		
	}
}
