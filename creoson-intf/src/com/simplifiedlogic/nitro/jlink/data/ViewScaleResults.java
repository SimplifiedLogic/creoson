/*
 * MIT LICENSE
 * Copyright 2000-2017 Simplified Logic, Inc
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
import java.util.List;

/**
 * Output from the drawing-scale_view function
 * 
 * @author Adam Andrews
 *
 */
public class ViewScaleResults implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> failedViews;
	private List<String> successViews;
	
	/**
	 * @return List of views that failed to scale
	 */
	public List<String> getFailedViews() {
		return failedViews;
	}
	/**
	 * @param failedViews List of views that failed to scale
	 */
	public void setFailedViews(List<String> failedViews) {
		this.failedViews = failedViews;
	}
	/**
	 * @return List of views that succeeded in scaling
	 */
	public List<String> getSuccessViews() {
		return successViews;
	}
	/**
	 * @param successViews List of views that succeeded in scaling
	 */
	public void setSuccessViews(List<String> successViews) {
		this.successViews = successViews;
	}
	
}
