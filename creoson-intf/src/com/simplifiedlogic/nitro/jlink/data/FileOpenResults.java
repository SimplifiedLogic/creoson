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
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;

/**
 * Results from the file.open function
 * @author Adam Andrews
 *
 */
public class FileOpenResults implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dirname;
	private String[] filenames;
	int fileRevision;
	
	/**
	 * Simple method to check whether the results contain a file name
	 * @return Whether the results contain a file name
	 */
	public boolean hasFile() {
		if (filenames!=null &&
			filenames.length>0 && 
			filenames[0]!=null &&
			filenames[0].length()>0)
			return true;
		return false;
	}
	
	/**
	 * @return Directory name of opened file(s)
	 */
	public String getDirname() {
		return dirname;
	}
	/**
	 * @param dirname Directory name of opened file(s)
	 */
	public void setDirname(String dirname) {
		this.dirname = dirname;
	}
	/**
	 * @return File names that were opened
	 */
	public String[] getFilenames() {
		return filenames;
	}
	/**
	 * @param filenames File names that were opened
	 */
	public void setFilenames(String[] filenames) {
		this.filenames = filenames;
	}

	/**
	 * @return Revision of file that was opened; if more than one file was opened, this field is not returned
	 */
	public int getFileRevision() {
		return fileRevision;
	}

	/**
	 * @param fileRevision Revision of file that was opened; if more than one file was opened, this field is not returned
	 */
	public void setFileRevision(int fileRevision) {
		this.fileRevision = fileRevision;
	}
}
