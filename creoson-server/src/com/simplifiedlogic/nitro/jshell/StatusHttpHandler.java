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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Implementation of HttpHandler which handles simple HTTP requests 
 * for dealing with server state
 * 
 * @author Adam Andrews
 */
public class StatusHttpHandler implements HttpHandler {

	/* (non-Javadoc)
	 * @see com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange)
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
		URI uri = t.getRequestURI();
		String query = uri.getQuery();
		if ("kill".equals(query)) {
			// a kill request will stop the server
			System.exit(0);
		}
		else if (query==null || query.length()==0 || "test".equals(query)) {
			// an empty request or a test request will return an empty page;
			// this is used to check whether the server is running
			t.sendResponseHeaders(200, 0);
			OutputStream os = t.getResponseBody();
			os.write("Connected!".getBytes(Charset.forName("UTF-8")));
			os.close();
		}
	}
}
