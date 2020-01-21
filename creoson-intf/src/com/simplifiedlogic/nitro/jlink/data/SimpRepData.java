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

import java.util.ArrayList;
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

	private List<Integer> path;
	private String name;
	private boolean defaultExclude;
	private List<List<Integer>> items;
	private List<SimpRepData> subItems;
	
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
	 * Add a new sub-rep to the representation
	 * @param path The data for sub-representation
	 */
	public void addSubItem(List<Integer> path, SimpRepData data) {
		if (subItems==null)
			subItems = new Vector<SimpRepData>();
		data.setPath(path);
		subItems.add(data);
	}

	/**
	 * Check whether a component is excluded from the representation.
	 * @param path The component path to check
	 * @return Whether the component is excluded from the representation
	 */
	public boolean excludes(List<Integer> path) {
		if (containsItem(path)) 
			return !defaultExclude;
		else {
//			ret = defaultExclude;
			if (subItems!=null) {
				for (SimpRepData rep : subItems) {
					List<Integer> subpath = rep.getPath();
					if (isSubpath(path, subpath) && path.size()>subpath.size()) {
						List<Integer> newpath = new ArrayList<Integer>();
						for (int i=subpath.size(); i<path.size(); i++) {
							newpath.add(path.get(i));
						}
						if (rep.excludes(newpath))
							return true;
					}
				}
			}
			return defaultExclude;
		}
	}
	
	/**
	 * Check whether any descendant of a component is excluded from 
	 * the representation.<p>
	 * If the input path is empty, then it will check whether the
	 * representation excludes <b>anything</b>.
	 * @param path The component path to check
	 * @return Whether the component is excluded from the representation
	 */
	public boolean excludesDescendant(List<Integer> path) {
		boolean ret;
		if (path==null || path.size()==0) {
			if (numPaths()>0)
				return !defaultExclude;
			else
				return defaultExclude;
		}
		if (containsDescendant(path)) 
			ret = !defaultExclude;
		else {
//			ret = defaultExclude;
			if (subItems!=null) {
				for (SimpRepData rep : subItems) {
					List<Integer> subpath = rep.getPath();
					if (isSubpath(path, subpath)) {
						List<Integer> newpath = new ArrayList<Integer>();
						for (int i=subpath.size(); i<path.size(); i++) {
							newpath.add(path.get(i));
						}
						if (rep.excludesDescendant(newpath))
							return true;
					}
				}
			}
			return defaultExclude;
		}
		return ret;
	}

	private boolean isSubpath(List<Integer> fullpath, List<Integer> subpath) {
		if (subpath.size()>fullpath.size())
			return false;
		int len = subpath.size();
		for (int i=0; i<len; i++) {
			if (!subpath.get(i).equals(fullpath.get(i)))
				return false;
		}
		return true;
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
	 * Check whether the representation contains a reference to a descendant
	 * of the specified component.  A descendant may not be an immediate child.
	 * Note that this does NOT indicate that the component is excluded or
	 * excluded, you will need to check the defaultExclude flag or call
	 * excludes() for that.
	 * @param path The component path for the component
	 * @return Whether the rep contains any descendant the component
	 */
	public boolean containsDescendant(List<Integer> path) {
		if (path==null || items==null || items.size()==0)
			return false;
		else {
			for (List<Integer> item : items) {
				// ignore any items with shorter paths (higher level) or same length paths (same level)
				if (item.size()<=path.size()) 
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
	 * Returns the number of items (component paths) in the simplified rep.
	 * @return The number of items (component paths) in the simplified rep
	 */
	public int numPaths() {
		int total=0;
		if (items!=null)
			total += items.size();
		if (subItems!=null) {
			for (SimpRepData rep : subItems)
				total += rep.numPaths();
		}
		return total;
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

	public List<Integer> getPath() {
		return path;
	}

	public void setPath(List<Integer> path) {
		this.path = path;
	}

	public List<SimpRepData> getSubItems() {
		return subItems;
	}

	public void setSubItems(List<SimpRepData> subItems) {
		this.subItems = subItems;
	}
}
