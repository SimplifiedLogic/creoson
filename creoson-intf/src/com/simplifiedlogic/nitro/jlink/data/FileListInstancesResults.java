/*
 * MIT LICENSE
 * Copyright 2000-2021 Simplified Logic, Inc
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
import java.util.ArrayList;
import java.util.List;

/**
 * Results from the file.list_instances function.
 * @author Adam Andrews
 *
 */
public class FileListInstancesResults implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String dirname;
	private String generic;
	private List<String> instances;
	
	/**
	 * Add an instance name to the result
	 * @param inst Instance name
	 */
	public void addInstance(String inst) {
		if (instances==null)
			instances = new ArrayList<String>();
		instances.add(inst);
	}

	/**
	 * @return Directory name for the generic model
	 */
	public String getDirname() {
		return dirname;
	}
	/**
	 * @param dirname Directory name for the generic model
	 */
	public void setDirname(String dirname) {
		this.dirname = dirname;
	}
	/**
	 * @return Generic name
	 */
	public String getGeneric() {
		return generic;
	}
	/**
	 * @param generic Generic name
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}
	/**
	 * @return List of family table instance names
	 */
	public List<String> getInstances() {
		return instances;
	}
	/**
	 * @param instances List of family table instance names
	 */
	public void setInstances(List<String> instances) {
		this.instances = instances;
	}
}
