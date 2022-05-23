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
package com.simplifiedlogic.nitro.jshell;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.sql.Timestamp;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Implementation of HttpHandler which handles requests for the internal website on this server
 * 
 * @author Adam Andrews
 */
public class FileHttpHandler implements HttpHandler {
	/**
	 * The root folder for the website.
	 */
	private static final String root = System.getProperty("user.dir").toLowerCase() + "\\web";
	
	/**
	 * The writer for the current log file.
	 */
	private FileWriter log = null;

	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
		File file = new File(root + uri.getPath()).getCanonicalFile();
//		System.out.println();
		if (!file.getPath().toLowerCase().startsWith(root)) {
			// Suspected path traversal attack: reject with 403 error.
			logit("Request: " + file.getAbsolutePath() + " Response: 403");
			String response = "403 (Forbidden)\n";
			t.sendResponseHeaders(403, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes(Charset.forName("UTF-8")));
			os.close();
			return;
		}
		if (file.exists() && file.isDirectory()) {
			// automatically append index.html to any URL that points to a folder
			file = new File(file, "/index.html");
		}
		if (!file.isFile()) {
			// Object does not exist or is not a file: reject with 404 error.
			logit("Request: " + file.getAbsolutePath() + " Response: 404");
			String response = "404 (Not Found)\n";
			t.sendResponseHeaders(404, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes(Charset.forName("UTF-8")));
			os.close();
			return;
		}
		// Object exists and is a file: accept with response code 200.
		logit("Request: " + file.getAbsolutePath() + " Response: 200");
		Headers headers = t.getResponseHeaders();
		// Workaround for Microsoft's blocking of CSS files that don't have a content type
		if (file.getName().toLowerCase().endsWith(".css")) {
			headers.add("Content-Type", "text/css");
		}
		t.sendResponseHeaders(200, 0);
		OutputStream os = t.getResponseBody();
		FileInputStream fs = new FileInputStream(file);
		final byte[] buffer = new byte[0x10000];
		int count = 0;
		while ((count = fs.read(buffer)) >= 0) {
			os.write(buffer, 0, count);
		}
		fs.close();
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
				log = new FileWriter("logs/web.log");
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
