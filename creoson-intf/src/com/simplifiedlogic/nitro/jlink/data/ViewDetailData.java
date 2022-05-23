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

import java.io.Serializable;

/**
 * Data about a drawing view
 * 
 * @author Adam Andrews
 *
 */
public class ViewDetailData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private int sheetNo;
	private JLPoint location;
	private double textHeight;
	private String model;
	private String simpRep;
	
	/**
	 * @return The view name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The view name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return The sheet number that contains the view
	 */
	public int getSheetNo() {
		return sheetNo;
	}
	/**
	 * @param sheetNo The sheet number that contains the view
	 */
	public void setSheetNo(int sheetNo) {
		this.sheetNo = sheetNo;
	}
	/**
	 * @return The text height for the view
	 */
	public double getTextHeight() {
		return textHeight;
	}
	/**
	 * @param textHeight The text height for the view
	 */
	public void setTextHeight(double textHeight) {
		this.textHeight = textHeight;
	}
	/**
	 * @return The coordinates of the center of the view's bounding box
	 */
	public JLPoint getLocation() {
		return location;
	}
	/**
	 * @param location The coordinates of the center of the view's bounding box
	 */
	public void setLocation(JLPoint location) {
		this.location = location;
	}
	/**
	 * @return The view model name
	 */
	public String getModel() {
		return model;
	}
	/**
	 * @param model The view model name
	 */
	public void setModel(String model) {
		this.model = model;
	}
	/**
	 * @return The view's simplified rep, if any
	 */
	public String getSimpRep() {
		return simpRep;
	}
	/**
	 * @param simpRep The view's simplified rep
	 */
	public void setSimpRep(String simpRep) {
		this.simpRep = simpRep;
	}
}
