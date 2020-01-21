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
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;

/**
 * Results from the file.get_fileinfo function
 * @author Adam Andrews
 *
 */
public class FileInfoResults implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dirname;
	private String filename;
	int fileRevision;
	
	/**
	 * @return Directory name of the file
	 */
	public String getDirname() {
		return dirname;
	}
	/**
	 * @param dirname Directory name of the file
	 */
	public void setDirname(String dirname) {
		this.dirname = dirname;
	}
	/**
	 * @return File name
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename File name
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return Revision number of file
	 */
	public int getFileRevision() {
		return fileRevision;
	}

	/**
	 * @param fileRevision Revision number of file
	 */
	public void setFileRevision(int fileRevision) {
		this.fileRevision = fileRevision;
	}
}
