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

import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the logging interface expected by com.simplifiedlogic.nitro.jlink.intf.DebugLogging
 * 
 * @author Adam Andrews
 *
 */
public class DebugHandler {

	/**
	 * List of currently enabled debug keys
	 */
	private List<String> keys = null;
	
	/**
	 * The singleton instance of this class
	 */
	private static DebugHandler thisInstance = null;
	
	/**
	 * The writer for the current log file.
	 */
	private FileWriter log = null;

	/**
	 * Get the singleton instance of the class
	 * 
	 * @return The singleton instance.
	 */
	public static DebugHandler getInstance() {
		if (thisInstance==null) {
			thisInstance = new DebugHandler();
		}
		return thisInstance;
	}
	
	/**
	 * Default constructor.
	 * 
	 * <p>This checks the system properties for a comma-separated list of debug keys 
	 * to be turned on, then stores that list of keys in memory.
	 */
	private DebugHandler() {
		String inkeys = System.getProperty("sli.debug");
		if (inkeys!=null && inkeys.length()>0) {
			String[] split = inkeys.split(",");
			this.keys = new ArrayList<String>();
			for (String k : split)
				this.keys.add(k);
		}
	}
	
	/**
	 * Send a standard message to the log.
	 * 
	 * @param message A text message to put in the log
	 * @param key The debug key
	 */
	private void sendMessage(String message, String key) {
		logit("[" + key + "]\t" + message);
	}

	/**
	 * Log a timer message, which is intended to show how long a given action has taken
	 * 
	 * <p>Debugging is controlled by keys which are specified as system properties at 
	 * application startup.  If the key is not set, then any log reuests with that key are ignored. 
	 * 
	 * @param text A text message to put in the log
	 * @param start The time the action was started, in milliseconds
	 * @param key The debug key
	 */
	public static void sendTimerMessage(String text, long start, String key) {
		long diff = start>0 ? System.currentTimeMillis() - start : 0;
		if (!getInstance().isDebug(key))
			return;
		
		getInstance().sendMessage(text + " Duration: " + diff, key);
	}
	
	/**
	 * Log a standard message
	 * 
	 * <p>Debugging is controlled by keys which are specified as system properties at 
	 * application startup.  If the key is not set, then any log reuests with that key are ignored. 
	 * 
	 * @param text A text message to put in the log
	 * @param key The debug key
	 */
	public static void sendDebugMessage(String text, String key) {
		if (!getInstance().isDebug(key))
			return;
		
		getInstance().sendMessage(text, key);
	}
	
	/**
	 * Check whether debug logging is enabled for a given key.
	 * 
	 * @param key The debug key
	 * @return true if the key is enabled.
	 */
	public boolean isDebug(String key) {
		if (keys==null)
			return false;
		return keys.contains(key);
	}

	/**
	 * Check whether debug logging is enabled for ANY key.
	 * 
	 * @return true if any key is enabled.
	 */
	public boolean isDebug() {
		return keys!=null;
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
				log = new FileWriter("logs/debug.log");
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
