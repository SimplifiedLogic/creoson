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

/**
 * Information about a layer
 * @author Adam Andrews
 *
 */
public class LayerData {

	private static final long serialVersionUID = 2L;
	
	String name;
	String status;
	int layerId;
	
	/**
	 * @return Layer Name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name Layer Name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Layer Status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status Layer Status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Layer ID
	 */
	public int getLayerId() {
		return layerId;
	}
	/**
	 * @param layerId Layer ID
	 */
	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}
}
