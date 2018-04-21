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
 * Output data from the bom-get_paths function
 * 
 * @author Adam Andrews
 *
 */
public class GetPathsOutput implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String modelname;
	String generic;
	BomChild root;
	
	/**
	 * @return The tope-level model name
	 */
	public String getModelname() {
		return modelname;
	}
	/**
	 * @param modelname The top-level model name
	 */
	public void setModelname(String modelname) {
		this.modelname = modelname;
	}
	/**
	 * @return The top-level model's generic name
	 */
	public String getGeneric() {
		return generic;
	}
	/**
	 * @param generic The top-level model's generic name
	 */
	public void setGeneric(String generic) {
		this.generic = generic;
	}
	/**
	 * @return The root node of the BOM hierarchy
	 */
	public BomChild getRoot() {
		return root;
	}
	/**
	 * @param root The root node of the BOM hierarchy
	 */
	public void setRoot(BomChild root) {
		this.root = root;
	}
}
