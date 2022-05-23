/*
 * MIT LICENSE
 * Copyright 2000-2022 Simplified Logic, Inc
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
 * Information about a Creo dimension (more detailed than DimData)
 * 
 * @author Adam Andrews
 *
 */
public class DimDetailData extends DimData {
	
	private static final long serialVersionUID = 1L;

	// Dimension types
	public static final String TYPE_LINEAR = "linear";
	public static final String TYPE_RADIAL = "radial";
	public static final String TYPE_DIAMETER = "diameter";
	public static final String TYPE_ANGULAR = "angular";

	private DimToleranceData tolerance;
	private int sheetNo;
	private String viewName;
	private String dimType;
	private String[] text;
	private JLPoint location;

	/**
	 * @return The dimension tolerance object
	 */
	public DimToleranceData getTolerance() {
		return tolerance;
	}
	/**
	 * @param tolerance The dimension tolerance object
	 */
	public void setTolerance(DimToleranceData tolerance) {
		this.tolerance = tolerance;
	}
	/**
	 * @return The view that contains the dimension
	 */
	public String getViewName() {
		return viewName;
	}
	/**
	 * @param viewName The view that contains the dimension
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	/**
	 * @return The dimension type
	 */
	public String getDimType() {
		return dimType;
	}
	/**
	 * @param dimType The dimension type
	 */
	public void setDimType(String dimType) {
		this.dimType = dimType;
	}
	/**
	 * @return The text of the dimension (no parameter replacement)
	 */
	public String[] getText() {
		return text;
	}
	/**
	 * @param text The text of the dimension (no parameter replacement)
	 */
	public void setText(String[] text) {
		this.text = text;
	}
	/**
	 * @return The sheet number containing the dimension
	 */
	public int getSheetNo() {
		return sheetNo;
	}
	/**
	 * @param sheetNo The sheet number containing the dimension
	 */
	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}
	/**
	 * @return The dimension's coordinates in drawing units
	 */
	public JLPoint getLocation() {
		return location;
	}
	/**
	 * @param location The dimension's coordinates in drawing units
	 */
	public void setLocation(JLPoint location) {
		this.location = location;
	}

}
