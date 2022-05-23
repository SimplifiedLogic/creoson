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
package com.simplifiedlogic.nitro.jlink.intf;

import java.lang.reflect.Method;

/**
 * Wrapper for JShell debug logging.
 * 
 * This class will pass calls to a handler class whose implementation depends on the other
 * libraries in the class path.  Currently there is one implementation for running as a console
 * app, and another implementation for running in an Eclipse OSGi application.
 * 
 * <p>The handler is determined in the static initializer portion of the class.
 * 
 * <p>Debugging is controlled by keys which are specified as system properties at 
 * application startup.  If the key is not set, then any log reuests with that key are ignored. 
 * 
 * @author Adam Andrews
 *
 */
public class DebugLogging {

	/**
	 * The implementation of the handler class for debug logging.
	 */
	private static Class<?> handler = null;
	
	/**
	 * A pointer to the "sendTimerMessage()" method in the handler class 
	 */
	private static Method timerMethod = null;
	
	/**
	 * A pointer to the "sendDebugMessage()" method in the handler class 
	 */
	private static Method debugMethod = null;
	
	static {
		if (handler==null) {
			try {
//				handler = Class.forName("com.simplifiedlogic.nitro.common.DebugHandler");
				handler = DebugLogging.class.getClassLoader().loadClass("com.simplifiedlogic.nitro.common.DebugHandler");
			}
			catch (Exception e) {
				// fall through
			}
		}
		if (handler==null) {
			try {
//				handler = Class.forName("com.simplifiedlogic.nitro.jshell.DebugHandler");
				handler = DebugLogging.class.getClassLoader().loadClass("com.simplifiedlogic.nitro.jshell.DebugHandler");
			}
			catch (Exception e) {
				// fall through
			}
		}
		
		if (handler!=null) {
			try {
				timerMethod = handler.getMethod("sendTimerMessage", new Class[] {String.class, long.class, String.class});
			}
			catch (Exception e) {
				// fall through
			}
			try {
				debugMethod = handler.getMethod("sendDebugMessage", new Class[] {String.class, String.class});
			}
			catch (Exception e) {
				// fall through
			}
		}
		
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
		if (timerMethod!=null) {
			try {
				timerMethod.invoke(null, new Object[] {text, start, key});
			}
			catch (Exception e) {
				// fall through
			}
		}
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
		if (debugMethod!=null) {
			try {
				debugMethod.invoke(null, new Object[] {text, key});
			}
			catch (Exception e) {
				// fall through
			}
		}
	}
}
