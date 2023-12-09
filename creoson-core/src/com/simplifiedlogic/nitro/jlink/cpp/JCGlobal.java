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

import java.io.File;

import com.simplifiedlogic.nitro.jlink.intf.JShellProvider;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Class for general functions related to the native interface.
 *
 */
public class JCGlobal {
    
	/**
	 * Flag indicating whether native library has been loaded
	 */
	public static boolean libraryLoaded = false;
	
	/**
	 * Path/File name of the native library. 
	 */
	private static File libraryFile = null;

    /**
     * Detect whether the Nitro OTK native library is loaded; if not, then it
     * loads the native library.
     * @throws JLIException
     */
    public static void loadLibrary() throws JLIException {
    	String bits = System.getProperty("sun.arch.data.model");
    	if (bits!=null && !"64".equals(bits))
    		throw new JLIException("Can't load jshell native library, must be running with 64-bit java.");
    	// need to DO this if we're not in an OSGi environment
        if (!libraryLoaded) {
        	//System.err.println("loadLibrary-creoson-core");
            libraryLoaded = true;
            try {
//            	System.out.println("libpath: "+System.getProperty("java.library.path"));
//            	System.loadLibrary(LIBNAME);
            	
            	//File file = getLibraryFile();
            	System.out.println("Load check: "+libraryFile);
            	if (libraryFile!=null)
            		System.load(libraryFile.getAbsolutePath());
            	else
            		System.load(JShellProvider.NATIVE_LIBNAME);
            }
            catch (Exception e) {
            	e.printStackTrace();
            	throw new JLIException(e);
            }
        }
    }
    
    /**
     * Sets the native library path/filename.
     * @param path Path to the native DLL library file
     */
    public static void setLibraryFile(File path) {
    	libraryFile = path;
    }
}
