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
package com.simplifiedlogic.nitro.util;

import java.io.File;
import java.io.FileFilter;

import com.ptc.cipjava.jxthrowable;
import com.simplifiedlogic.nitro.jlink.impl.NitroUtils;
import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Implementation of java.io.FileFilter which is passed to the 
 * java.io.File.listFiles() method to find files in a disk directory
 * and filter them by patterns.
 *   
 * @author Adam Andrews
 *
 */
public class FileListFilter extends LooperBase implements FileFilter {
    private boolean listDirs = false;
    private boolean listFiles = true;
    private boolean stripNumExt = true;

    /* (non-Javadoc)
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
    	if (!isListDirs() && f.isDirectory())
    		return false;
    	if (!isListFiles() && !f.isDirectory())
    		return false;

        if (nameList!=null) {
            return checkNameAgainstList(fixFileName(f, f.getName()));
        }
        else {
            return checkName(f);
        }
    }

    /**
     * Check whether a file name matches the name pattern. 
     * @param f The file to check
     * @return Whether the file name matches the name pattern
     */
    private boolean checkName(File f) {
        if (namePattern!=null) {
            String name = f.getName();
            if (name==null)
                return false;
            String use_name=fixFileName(f, name);

            return checkName(use_name);
        }
        return true;
    }
    
    /**
     * Fix the file name so that it can be evaluated.  Mainly consists of
     * removing Creo's numeric file revision for the file name.
     * @param f The java.io.File object for the same file
     * @param name The file name to fix
     * @return The fixed name 
     */
    private String fixFileName(File f, String name) {
        if (name==null)
            return null;
        if (f!=null && f.isDirectory())
            return name;
        else
            return NitroUtils.removeNumericExtension(name); 
    }
    
    protected void processObjectByName(String name) throws JLIException,jxthrowable {
        // unused
    }

	/**
	 * @return Whether to include directories in the listing
	 */
	public boolean isListDirs() {
		return listDirs;
	}
	/**
	 * @param listDirs Whether to include directories in the listing
	 */
	public void setListDirs(boolean listDirs) {
		this.listDirs = listDirs;
	}
	/**
	 * @return Whether to include files in the listing
	 */
	public boolean isListFiles() {
		return listFiles;
	}
	/**
	 * @param listFiles Whether to include files in the listing
	 */
	public void setListFiles(boolean listFiles) {
		this.listFiles = listFiles;
	}

	/**
	 * @return Whether to strip the numeric revision extension from file names
	 */
	public boolean isStripNumExt() {
		return stripNumExt;
	}

	/**
	 * @param stripNumExt Whether to strip the numeric revision extension from file names
	 */
	public void setStripNumExt(boolean stripNumExt) {
		this.stripNumExt = stripNumExt;
	}

}
