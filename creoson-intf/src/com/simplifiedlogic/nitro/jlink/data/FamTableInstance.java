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

import com.simplifiedlogic.nitro.rpc.JLIException;

/**
 * Information about an instance in a nested family table.  Also contains child instances stored recursively.
 * 
 * @author Adam Andrews
 *
 */
public class FamTableInstance implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private String name;
	private List<FamTableInstance> children;
	private int total=0;

    /**
     * Standard Constructor
     * @param name The name of a family table instance
     */
    public FamTableInstance(String name) {
        this.name = name;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name;
    }
    
    /**
     * Add a new child instance.  This will create a basic FamilyTableInstance object and add it, to be filled in later 
     * @param newname The name of the child instance
     */
    public void addChild(String newname) {
    	FamTableInstance inst = new FamTableInstance(newname);
        addChild(inst);
    }
    
    /**
     * Add a new child instance.
     * @param inst The chuld instance to add
     */
    public void addChild(FamTableInstance inst) {
        if (children==null)
            children = new ArrayList<FamTableInstance>();
        children.add(inst);
    }

    /**
	 * Walk the hierarchy in prefix order.  This means that the current 
	 * instance is processed before its children. 
	 * 
     * @param walker The class to be called for each instance in the hierarchy
     * @throws JLIException
     */
    public void prefixWalk(FamTableWalker walker) throws JLIException {
        walker.handleNode(this);
        if (children!=null) {
            int len = children.size();
            for (int i=0; i<len; i++) {
                ((FamTableInstance)children.get(i)).postfixWalk(walker);
            }
        }
    }

    /**
	 * Walk the hierarchy in postfix order.  This means that the current 
	 * instance is processed after its children. 
	 * 
	 * @param walker The class to be called for each instance in the hierarchy
     * @throws JLIException
     */
    public void postfixWalk(FamTableWalker walker) throws JLIException {
        if (children!=null) {
            int len = children.size();
            for (int i=0; i<len; i++) {
                ((FamTableInstance)children.get(i)).postfixWalk(walker);
            }
        }
        walker.handleNode(this);
    }

	/**
	 * @return Name of the family table instance
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return List of child instances
	 */
	public List<FamTableInstance> getChildren() {
		return children;
	}

	/** 
	 * @return Count of all child instances including their decendants
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total Count of all child instances including their decendants
	 */
	public void setTotal(int total) {
		this.total = total;
	}
}
