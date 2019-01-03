/*
 * MIT LICENSE
 * Copyright 2000-2019 Simplified Logic, Inc
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
package com.simplifiedlogic.nitro.jlink.impl;


/**
 * High-level constants for JShell
 * 
 * @author Adam Andrews
 */
public interface NitroConstants {

    /**
     * Name of the JLink core library to load before running functions
     */
    public static final String LIBNAME = "pfcasyncmt";
    
    /**
     * Whether to track time consumed by each JShell function.  Note that 
     * logging of this time will still not take place unless debugging for
     * the DEBUG_KEY is enabled.
     * @see com.simplifiedlogic.nitro.jlink.intf.DebugLogging
     */
    public static final boolean TIME_TASKS = true;
    
    /**
     * Debug key to turn on logging for JShell calls
     * @see com.simplifiedlogic.nitro.jlink.intf.DebugLogging
     */
    public static final String DEBUG_KEY = "jshell";
    /**
     * Debug key to turn on logging for regenerates
     * @see com.simplifiedlogic.nitro.jlink.intf.DebugLogging
     */
    public static final String DEBUG_REGEN_KEY = "regen";
    /**
     * Debug key to turn on logging for the setting of parameters
     * @see com.simplifiedlogic.nitro.jlink.intf.DebugLogging
     */
    public static final String DEBUG_SET_PARAM_KEY = "parameter.set";
    /**
     * Debug key to turn on logging for connections to JShell
     * @see com.simplifiedlogic.nitro.jlink.intf.DebugLogging
     */
    public static final String DEBUG_CONNECT_KEY = "connect";
    
    /**
     * Compile-time constant to turn on logging for JLink function calls.
     * Set at compile time so that the code will be more efficient when
     * it is false.
     */
    public static final boolean DEBUG_JLINK = false;
    /**
     * Debug key to turn on logging for JLink function calls
     * @see com.simplifiedlogic.nitro.jlink.intf.DebugLogging
     */
    public static final String DEBUG_JLINK_KEY = "jlink";
}
