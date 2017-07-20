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

/**
 * Information about an edge
 * @author Adam Andrews
 *
 */
public class EdgeData implements Serializable {

	private static final long serialVersionUID = 1L;

	private int edgeId;
	private double length=0.0;
	private JLPoint start=null;
	private JLPoint end=null;
	
	/**
	 * @return Edge ID
	 */
	public int getEdgeId() {
		return edgeId;
	}
	/**
	 * @param edgeId Edge ID
	 */
	public void setEdgeId(int edgeId) {
		this.edgeId = edgeId;
	}
	/**
	 * @return Edge length
	 */
	public double getLength() {
		return length;
	}
	/**
	 * @param length Edge length
	 */
	public void setLength(double length) {
		this.length = length;
	}
	/**
	 * @return Edge start point
	 */
	public JLPoint getStart() {
		return start;
	}
	/**
	 * @param start Edge start point
	 */
	public void setStart(JLPoint start) {
		this.start = start;
	}
	/**
	 * @return Edge end point
	 */
	public JLPoint getEnd() {
		return end;
	}
	/**
	 * @param end Edge end point
	 */
	public void setEnd(JLPoint end) {
		this.end = end;
	}
	
}
