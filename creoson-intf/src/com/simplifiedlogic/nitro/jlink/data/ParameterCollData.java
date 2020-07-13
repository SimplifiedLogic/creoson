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

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a list of parameters for a list of models
 * @author Adam Andrews
 *
 */
public class ParameterCollData {

	private List<ParameterCollFileData> files;
	
	public void addFile(ParameterCollFileData file) {
		if (files==null)
			files = new ArrayList<ParameterCollFileData>();
		files.add(file);
	}

	public int size() {
		if (files==null)
			return 0;
		return files.size();
	}

	public List<String> getFileNames() {
		if (files==null || files.size()==0)
			return null;
		List<String> out = new ArrayList<String>();
		for (ParameterCollFileData file : files) {
			if (file.getFileName()!=null)
				out.add(file.getFileName());
		}
		return out;
	}

	public void clear() {
		if (files!=null) {
			for (ParameterCollFileData file : files)
				file.clear();
		}
		files = null;
	}

	public List<ParameterCollFileData> getFiles() {
		return files;
	}

	public void setFiles(List<ParameterCollFileData> files) {
		this.files = files;
	}

}
