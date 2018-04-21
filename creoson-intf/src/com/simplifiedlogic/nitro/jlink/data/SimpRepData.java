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

import java.util.List;
import java.util.Vector;

/**
 * Simplified Representation data for a model
 * 
 * <p>A simplified rep is a list of component paths.  The tricky part is whether 
 * those components are included or excluded from the rep.
 * <p>If defaultExclude=true, then any components not listed are excluded from
 * the rep, and the listed items are included.
 * <p>If defaultExclude=false (the default), then any components not listed are included in 
 * the rep, and the listed items are excluded. 
 * @author Adam Andrews
 *
 */
public class SimpRepData {

	private static final long serialVersionUID = 1L;

	private String name;
	private boolean defaultExclude;
	private List<List<Integer>> items;
	
	/**
	 * Add a new component to the representation
	 * @param path The component path for the component
	 */
	public void addItem(List<Integer> path) {
		if (items==null)
			items = new Vector<List<Integer>>();
		items.add(path);
	}
	
	/**
	 * Check whether a component is excluded from the representation
	 * @param path The component path to check
	 * @return Whether the component is excluded from the representation
	 */
	public boolean excludes(List<Integer> path) {
		boolean ret;
		if (containsItem(path)) ret = !defaultExclude;
		else ret = defaultExclude;
		return ret;
	}

	/**
	 * Check whether the representation contains a reference to a component.
	 * Note that this does NOT indicate that the component is excluded or
	 * excluded, you will need to check the defaultExclude flag or call
	 * excludes() for that.
	 * @param path The component path for the component
	 * @return Whether the rep contains the component
	 */
	public boolean containsItem(List<Integer> path) {
		if (path==null || items==null || items.size()==0)
			return false;
		else {
			for (List<Integer> item : items) {
				if (item.size()!=path.size())
					continue;
				int len = path.size();
				boolean valid=true;
				for (int i=0; i<len; i++) 
					if (!item.get(i).equals(path.get(i))) {
						valid=false;
						break;
					}
				if (valid)
					return true;
			}
			return false;
		}
	}

	/**
	 * @return Name of the simplified rep
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name Name of the simplified rep
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return List of component paths in the rep
	 */
	public List<List<Integer>> getItems() {
		return items;
	}
	/**
	 * @param items List of component paths in the rep
	 */
	public void setItems(List<List<Integer>> items) {
		this.items = items;
	}

	/**
	 * @return Whether non-listed componets are excluded; if false, then non-listed components are included
	 */
	public boolean isDefaultExclude() {
		return defaultExclude;
	}

	/**
	 * @param defaultExclude Whether non-listed componets are excluded; if false, then non-listed components are included
	 */
	public void setDefaultExclude(boolean defaultExclude) {
		this.defaultExclude = defaultExclude;
	}
}
