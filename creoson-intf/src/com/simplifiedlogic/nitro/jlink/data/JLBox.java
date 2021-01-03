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

/**
 * Bounding Box data for a model
 * @author Adam Andrews
 *
 */
public class JLBox implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;
	private double zmin;
	private double zmax;
	
	/**
	 * @return Minimum X-coordinate of model
	 */
	public double getXmin() {
		return xmin;
	}
	/**
	 * @param xmin Minimum X-coordinate of model
	 */
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}
	/**
	 * @return Maximum X-coordinate of model
	 */
	public double getXmax() {
		return xmax;
	}
	/**
	 * @param xmax Maximum X-coordinate of model
	 */
	public void setXmax(double xmax) {
		this.xmax = xmax;
	}
	/**
	 * @return Minimum Y-coordinate of model
	 */
	public double getYmin() {
		return ymin;
	}
	/**
	 * @param ymin Minimum Y-coordinate of model
	 */
	public void setYmin(double ymin) {
		this.ymin = ymin;
	}
	/**
	 * @return Maximum Y-coordinate of model
	 */
	public double getYmax() {
		return ymax;
	}
	/**
	 * @param ymax Maximum Y-coordinate of model
	 */
	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	/**
	 * @return Minimum Z-coordinate of model
	 */
	public double getZmin() {
		return zmin;
	}
	/**
	 * @param zmin Minimum Z-coordinate of model
	 */
	public void setZmin(double zmin) {
		this.zmin = zmin;
	}
	/**
	 * @return Maximum Z-coordinate of model
	 */
	public double getZmax() {
		return zmax;
	}
	/**
	 * @param zmax Maximum Z-coordinate of model
	 */
	public void setZmax(double zmax) {
		this.zmax = zmax;
	}
	
}
