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
 * A 3D coordinate
 * 
 * @author Adam Andrews
 *
 */
public class JLPoint implements Serializable {

	private static final long serialVersionUID = 1L;

	private double x,y,z;

	/**
	 * 
	 */
	public JLPoint() {
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public JLPoint(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return x+" "+y+" "+z;
	}

	/**
	 * @return X-coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x X-coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return Y-coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y Y-coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return Z-coordinate
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @param z Z-coordinate
	 */
	public void setZ(double z) {
		this.z = z;
	}
}
