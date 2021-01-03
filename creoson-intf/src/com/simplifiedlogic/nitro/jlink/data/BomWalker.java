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

/**
 * Interface class used to define a component handler when walking a BOM 
 * hierarchy created by bom-get_paths. 
 * 
 * @author Adam Andrews
 * @see BomChild
 */
public interface BomWalker {
	/**
	 * Process one component in the hierarchy.
	 * @param node The BOM entry representing the current component
	 * @param level The level of this component in the hierarchy
	 * @param data User-specified data object to be passed along in the walk
	 */
	public void handleNode(BomChild node, int level, Object data);
	
	/**
	 * Check whether the walk process should descend into the children of the 
	 * current component.
	 * 
	 * @param root The top-level root node of the hierarchy
	 * @param node The BOM entry representing the current component
	 * @param level The level of this component in the hierarchy
	 * @param data User-specified data object to be passed along in the walk
	 * @return True if the walk process should descend into the children of this component
	 */
	public boolean isDescend(BomChild root, BomChild node, int level, Object data);
}