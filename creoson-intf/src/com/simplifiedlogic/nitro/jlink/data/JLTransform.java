/*
 * MIT LICENSE
 * Copyright 2000-2020 Simplified Logic, Inc
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
 * A representation of a 3D transform as a set of vectors and rotations
 * 
 * @author Adam Andrews
 *
 */
public class JLTransform implements Serializable {

	private static final long serialVersionUID = 2L;
	
	/**
	 * The transform's origin as given by Creo
	 */
	public JLPoint origin;
	/**
	 * The transform's X-axis as given by Creo
	 */
	public JLPoint xvector;
	/**
	 * The transform's Y-axis as given by Creo
	 */
	public JLPoint yvector;
	/**
	 * The transform's Z-axis as given by Creo
	 */
	public JLPoint zvector;
	/**
	 * The transform's X-rotation as calculated
	 */
	public Double xrot;
	/**
	 * The transform's Y-rotation as calculated
	 */
	public Double yrot;
	/**
	 * The transform's Z-rotation as calculated
	 */
	public Double zrot;
	
}
