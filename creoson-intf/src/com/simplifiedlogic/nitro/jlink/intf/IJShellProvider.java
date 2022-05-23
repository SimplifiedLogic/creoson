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
package com.simplifiedlogic.nitro.jlink.intf;

/**
 * Interface for a class which provides handlers for JShell command groups.
 * 
 * @author Adam Andrews
 *
 */
public interface IJShellProvider {

	/**
	 * Get the "bom" JShell handler
	 * @return The "bom" JShell handler
	 */
	public IJLBom getJLBom();
	/**
	 * Get the "connection" JShell handler
	 * @return The "connection" JShell handler
	 */
	public IJLConnection getJLConnection();
	/**
	 * Get the "dimension" JShell handler
	 * @return The "dimension" JShell handler
	 */
	public IJLDimension getJLDimension();
	/**
	 * Get the "familytable" JShell handler
	 * @return The "familytable" JShell handler
	 */
	public IJLFamilyTable getJLFamilyTable();
	/**
	 * Get the "file" JShell handler
	 * @return The "file" JShell handler
	 */
	public IJLFile getJLFile();
	/**
	 * Get the "parameter" JShell handler
	 * @return The "parameter" JShell handler
	 */
	public IJLParameter getJLParameter();
	/**
	 * Get the "creo" JShell handler
	 * @return The "creo" JShell handler
	 */
	public IJLProe getJLProe();
	/**
	 * Get the "interface" JShell handler
	 * @return The "interface" JShell handler
	 */
	public IJLTransfer getJLTransfer();
	/**
	 * Get the "feature" JShell handler
	 * @return The "feature" JShell handler
	 */
	public IJLFeature getJLFeature();
	/**
	 * Get the "drawing" JShell handler
	 * @return The "drawing" JShell handler
	 */
	public IJLDrawing getJLDrawing();
	/**
	 * Get the "note" JShell handler
	 * @return The "note" JShell handler
	 */
	public IJLNote getJLNote();
	/**
	 * Get the "layer" JShell handler
	 * @return The "layer" JShell handler
	 */
	public IJLLayer getJLLayer();
	/**
	 * Get the "windchill" JShell handler
	 * @return The "windchill" JShell handler
	 */
	public IJLWindchill getJLWindchill();
	/**
	 * Get the "geometry" JShell handler
	 * @return The "geometry" JShell handler
	 */
	public IJLGeometry getJLGeometry();
	/**
	 * Get the "view" JShell handler
	 * @return The "view" JShell handler
	 */
	public IJLView getJLView();

}