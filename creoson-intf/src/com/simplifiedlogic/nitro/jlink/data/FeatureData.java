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
package com.simplifiedlogic.nitro.jlink.data;

import java.io.Serializable;

/**
 * Information about a feature
 * 
 * @author Adam Andrews
 *
 */
public class FeatureData implements Serializable {

	private static final long serialVersionUID = 2L;
	
	String name;
	String status;
	int featureId;
	String featureType;
	int featureNumber;
	
	/**
	 * @return Feature name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name Feature name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Feature status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status Feature status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return Feature ID
	 */
	public int getFeatureId() {
		return featureId;
	}
	/**
	 * @param featureId Feature ID
	 */
	public void setFeatureId(int featureId) {
		this.featureId = featureId;
	}
	/**
	 * @return Feature type
	 */
	public String getFeatureType() {
		return featureType;
	}
	/**
	 * @param featureType Feature type
	 */
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	/**
	 * @return Feature Number
	 */
	public int getFeatureNumber() {
		return featureNumber;
	}
	/**
	 * @param featureNumber Feature Number
	 */
	public void setFeatureNumber(int featureNumber) {
		this.featureNumber = featureNumber;
	}
}
