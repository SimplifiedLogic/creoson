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
import java.util.ArrayList;
import java.util.List;

/**
 * Information about a contour, which is a collection of connected edges
 * 
 * @author Adam Andrews
 *
 */
public class ContourData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// traverse types; indicates whether the contour is on the "inside" or "outside" of a surface
	public static final int TRAVERSE_INTERNAL = 1;
	public static final int TRAVERSE_EXTERNAL = 2;

	private int surfaceId;
	private int traversalType;
	private List<EdgeData> edges;

	/**
	 * Add a new Edge to the contour
	 * @param edge The edge data object
	 */
	public void addEdge(EdgeData edge) {
		if (edges==null)
			edges = new ArrayList<EdgeData>();
		edges.add(edge);
	}
	
	/**
	 * @return Surface ID
	 */
	public int getSurfaceId() {
		return surfaceId;
	}
	/**
	 * @param surfaceId Surface ID
	 */
	public void setSurfaceId(int surfaceId) {
		this.surfaceId = surfaceId;
	}
	/**
	 * @return List of edges
	 */
	public List<EdgeData> getEdges() {
		return edges;
	}
	/**
	 * @param edges List of edges
	 */
	public void setEdges(List<EdgeData> edges) {
		this.edges = edges;
	}
	/**
	 * @return Traversal type
	 */
	public int getTraversalType() {
		return traversalType;
	}
	/**
	 * @param traversalType Traversal type
	 */
	public void setTraversalType(int traversalType) {
		this.traversalType = traversalType;
	}
	
}
