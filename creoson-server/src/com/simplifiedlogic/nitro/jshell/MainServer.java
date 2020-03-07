package com.simplifiedlogic.nitro.jshell;
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

import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

/**
 * The main class for the server.  Run this class at the command line to 
 * start the server.
 * 
 * <p>This class will not exit until the server process is killed.  You can also
 * close the server by going to /status?kill on the local server
 * 
 * @author Adam Andrews
 *
 */
public class MainServer {

	/**
	 * Property which specifies which port the server will listen on
	 */
	private static final String PORT_PROP = "sli.socket.port";
	
	/**
	 * Endpoint for CREOSON JSON requests
	 */
	public static final String ENDPOINT_CREOSON	= "/creoson";
	/**
	 * Endpoint for Server HTTP status requests 
	 */
	public static final String ENDPOINT_STATUS	= "/status";
	/**
	 * Endpoint for Server JSON requests
	 */
	public static final String ENDPOINT_SERVER	= "/server";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			int port = 8000;
			String portStr = System.getProperty(PORT_PROP);
			if (portStr!=null) {
	    		try {
	    			port = Integer.parseInt(portStr);
	    		}
	    		catch (Exception e) {
					System.err.println("Invalid port: " + portStr);
					return;
	    		}
			}
			if (port==0) {
				System.err.println("Invalid port: " + port);
				return;
			}
/*
			if (args.length>0) {
				if (args[0].equals(JLConnectRequestParams.FUNC_START_PROE)) {
					JShellJsonHandler hdlr = new JShellJsonHandler();
					BaseRequest req = new BaseRequest();
					req.setCommand(JLConnectRequestParams.COMMAND);
					req.setFunction(JLConnectRequestParams.FUNC_START_PROE);
					Hashtable<String, Object> data = new Hashtable<String, Object>();
					req.setData(data);
//					data.put(JLConnectRequestParams.PARAM_START_DIR, "d:/ptc/workspace35-nitro/JshellClient");
//					data.put(JLConnectRequestParams.PARAM_START_COMMAND, "nitro_proe_remote.bat");
					
					for (int i=1; i<args.length; i++) {
						String[] arr = args[i].split("=");
						if (arr.length!=2) // bad argument
							continue;
						if (arr[0].equalsIgnoreCase("port")) {
							port = Integer.valueOf(arr[1]).intValue();
						}
						data.put(arr[0], arr[1]);
					}
					
					hdlr.handleRequest(req);
					return;
				}
			}
*/
			HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext(ENDPOINT_CREOSON, new JshellHttpHandler());
			server.createContext(ENDPOINT_STATUS, new StatusHttpHandler());
			server.createContext(ENDPOINT_SERVER, new ServerHttpHandler());
			server.createContext("/", new FileHttpHandler());
			server.setExecutor(null);
			System.out.println("Starting server, listening on port " + port + ".");
			server.start();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
