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
package com.simplifiedlogic.nitro.jlink.cpp;

/**
 * Class containing native interface methods meant to be called from JLConnection, but currently unused.
 */
public class JCConnection {

	/**
	 * Turn on all debugging for native interface
	 * @param debug Whether to turn on or off debugging
	 */
	public static void setDebug(boolean debug) {
		setConnectionDebug(debug);
		setSessionDebug(debug);
	}

    /**
     * Native function to connect to Creo.
     * @return The connection ID
     */
    public static native String connect();
    /**
     * Native function to connect to Creo with a specific connection ID.
     * @param connId The connection ID
     * @return The connection ID (may be different from input)
     */
    public static native String connectById(String connId);
    /**
     * @param debug Whether to turn connection debugging on
     */
    public static native void setConnectionDebug(boolean debug);
    /**
     * @param debug Whether to turn session debugging on
     */
    public static native void setSessionDebug(boolean debug);
}
