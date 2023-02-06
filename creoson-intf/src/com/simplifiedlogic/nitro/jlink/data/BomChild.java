/*
 * MIT LICENSE
 * Copyright 2000-2023 Simplified Logic, Inc
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
 * Data for a component in a BOM hierarchy
 * 
 * @author Adam Andrews
 *
 */
public class BomChild implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String sequencePath;
	String filename;
	List<Integer> componentPath;
	JLMatrix transform;
	JLTransform transformTable;
	String simpRep;

	BomChild parent;
	List<BomChild> children;
	
	/**
	 * Add a new child entry to the current component in the hierarchy
	 * @param child The new child to add
	 */
	public void add(BomChild child) {
		if (children==null)
			children = new ArrayList<BomChild>();
		children.add(child);
		child.setParent(this);
	}

	/**
	 * Find the position of a child entry within the current component
	 * @param child The child entry to find
	 * @return The index of the child entry, or -1 if the entry is not a child of this component
	 */
	public int childIndex(BomChild child) {
		if (children==null)
			return -1;
		return children.indexOf(child);
	}

	/**
	 * Remove a child entry from the current component in the hierarchy
	 * @param child The child to remove
	 */
	public void remove(BomChild child) {
		if (children!=null) {
			int index = childIndex(child);
			if (index>=0) {
				children.remove(index);
				child.setParent(null);
			}
		}
	}

	/**
	 * @return The number of children of this component in the structure
	 */
	public int numChildren() {
		if (children!=null)
			return children.size();
		else
			return 0;
	}

	/**
	 * @return Dot-separated list of sequence numbers showing the order of component within the assembly
	 */
	public String getSequencePath() {
		return sequencePath;
	}
	/**
	 * @param sequencePath Dot-separated list of sequence numbers showing the order of component within the assembly
	 */
	public void setSequencePath(String sequencePath) {
		this.sequencePath = sequencePath;
	}
	/**
	 * @return Model name of the component
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename Model name of the component
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return Creo's component path for the component
	 */
	public List<Integer> getComponentPath() {
		return componentPath;
	}
	/**
	 * @param componentPath Creo's component path for the component
	 */
	public void setComponentPath(List<Integer> componentPath) {
		this.componentPath = componentPath;
	}
	/**
	 * @return 3D Transform for the component.  This is only set if the 
	 * hierarchy was created with transformAsTable=false
	 */
	public JLMatrix getTransform() {
		return transform;
	}
	/**
	 * @param transform 3D Transform for the component.  This is only set if the 
	 * hierarchy was created with transformAsTable=false
	 */
	public void setTransform(JLMatrix transform) {
		this.transform = transform;
	}

	/**
	 * @return The parent component in the hierarchy
	 */
	public BomChild getParent() {
		return parent;
	}

	/**
	 * @param parent The parent component in the hierarchy
	 */
	public void setParent(BomChild parent) {
		this.parent = parent;
	}

	/**
	 * @return Data for the child components of the component
	 */
	public List<BomChild> getChildren() {
		return children;
	}
	
	/**
	 * Remove all children from the component
	 */
	public void clearChildren() {
		if (children!=null)
			children = null;
	}

	/**
	 * Walk the hierarchy in prefix order.  This means that the current 
	 * component is processed before its children. 
	 * 
	 * @param walker The class to be called for each component in the hierarchy
	 */
	public void prefixWalk(BomWalker walker) {
		prefixWalk(walker, this, 0, null);
	}
	
	/**
	 * Walk the hierarchy in prefix order.  This means that the current 
	 * component is processed before its children. 
	 * 
	 * @param walker The class to be called for each component in the hierarchy
	 * @param data User-specified data object to be passed along in the walk
	 */
	public void prefixWalk(BomWalker walker, Object data) {
		prefixWalk(walker, this, 0, data);
	}
	
	/**
	 * Walk the hierarchy in prefix order.  This means that the current 
	 * component is processed before its children. 
	 * 
	 * @param walker The class to be called for each component in the hierarchy
	 * @param root The top-level root node of the hierarchy
	 * @param level The level of this component in the hierarchy
	 * @param data User-specified data object to be passed along in the walk
	 */
	public void prefixWalk(BomWalker walker, BomChild root, int level, Object data) {
		walker.handleNode(this, level, data);
		if (walker.isDescend(root, this, level, data) && children!=null) {
			for (BomChild child : children) {
				child.prefixWalk(walker, root, level+1, data);
			}
		}
	}

	/**
	 * Walk the hierarchy in postfix order.  This means that the current 
	 * component is processed after its children. 
	 * 
	 * @param walker The class to be called for each component in the hierarchy
	 */
	public void postfixWalk(BomWalker walker) {
		postfixWalk(walker, this, 0, null);
	}
	
	/**
	 * Walk the hierarchy in postfix order.  This means that the current 
	 * component is processed after its children. 
	 * 
	 * @param walker The class to be called for each component in the hierarchy
	 * @param data User-specified data object to be passed along in the walk
	 */
	public void postfixWalk(BomWalker walker, Object data) {
		postfixWalk(walker, this, 0, data);
	}
	
	/**
	 * Walk the hierarchy in postfix order.  This means that the current 
	 * component is processed after its children. 
	 * 
	 * @param walker The class to be called for each component in the hierarchy
	 * @param root The top-level root node of the hierarchy
	 * @param level The level of this component in the hierarchy
	 * @param data User-specified data object to be passed along in the walk
	 */
	public void postfixWalk(BomWalker walker, BomChild root, int level, Object data) {
		if (walker.isDescend(root, this, level, data) && children!=null) {
			for (BomChild child : children) {
				child.postfixWalk(walker, root, level+1, data);
			}
		}
		walker.handleNode(this, level, data);
	}

	/**
	 * @return 3D Transform for the component.  This is only set if the 
	 * hierarchy was created with transformAsTable=true
	 */
	public JLTransform getTransformTable() {
		return transformTable;
	}

	/**
	 * @param transformTable 3D Transform for the component.  This is only set if the 
	 * hierarchy was created with transformAsTable=false
	 */
	public void setTransformTable(JLTransform transformTable) {
		this.transformTable = transformTable;
	}

	/**
	 * @return The name of the simplified rep active for this component (assemblies only)
	 */
	public String getSimpRep() {
		return simpRep;
	}

	/**
	 * @param simpRep The name of the simplified rep active for this component (assemblies only)
	 */
	public void setSimpRep(String simpRep) {
		this.simpRep = simpRep;
	}
}
