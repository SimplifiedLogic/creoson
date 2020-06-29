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

	public static final int DEFAULT_UNKNOWN = -1;
	public static final int DEFAULT_EXCLUDE = 0;
	public static final int DEFAULT_INCLUDE = 1;

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
			if (subItems!=null) {
				for (SimpRepData rep : subItems) {
					List<Integer> subpath = rep.getPath();
					if (isSubpath(path, subpath) && path.size()>subpath.size()) {
						List<Integer> newpath = trimSubpath(path, rep);
						if (rep.excludes(newpath))
							return true;
						else
							return false; // ??
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
	 * 
	 * The PROBLEM with this is that it only works right when the rep 
	 * is defaultExclude=false.  When defaultExclude=true BUT all
	 * the components in the rep are items (included) then it will return
	 * true when it should return false.
	 *  
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
						List<Integer> newpath = trimSubpath(path, rep);
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

//	public boolean hasOwnSubRep(List<Integer> path) {
//		if (containsItem(path))
//			return true;
//		if (subItems==null || subItems.size()==0)
//			return false;
//		for (SimpRepData rep : subItems) {
//			List<Integer> subpath = rep.getPath();
//			if (isSubpath(path, subpath) && path.size()>=subpath.size()) {
//				List<Integer> newpath = trimSubpath(path, rep);
//				if (rep.hasOwnSubRep(newpath))
//					return true;
//			}
//		}
//		return false;
//	}
	
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
	 * Check whether the representation contains a nested rep for a 
	 * specified assembly component path.
	 * @param path The component path for the subassembly
	 * @return The nested rep for the subassembly
	 */
	public SimpRepData containsNestedRep(List<Integer> path) {
		if (path==null || subItems==null || subItems.size()==0)
			return null;
		else {
			for (SimpRepData rep : subItems) {
				if (rep.path.size()!=path.size())
					continue;
				int len = path.size();
				boolean valid=true;
				for (int i=0; i<len; i++) 
					if (!rep.path.get(i).equals(path.get(i))) {
						valid=false;
						break;
					}
				if (valid)
					return rep;
			}
			return null;
		}
	}
	
//	public boolean findLowestDefaultExclude(List<Integer> path) {
//		List<Integer> newpath = new ArrayList<Integer>();
//		for (int i=0; i<path.size(); i++) {
//			newpath.add(path.get(i));
//		}
//
//		boolean tmp = false;
//		do {
//			boolean contains = containsItem(newpath);
//			if (contains) {
//				// something
//				tmp = defaultExclude;
//				break;
//			}
//			newpath.remove(newpath.size()-1);
//		} while (newpath.size()>0);
//
//		if (subItems==null || subItems.size()==0)
//			return tmp;
//		
//	}

//	public SimpRepData findLowestSimpRep(List<Integer> path) {
//		SimpRepData rep = findClosestSubItem(path);
//		
//		if (rep!=null) {
//			// something
//			List<Integer> subpath = rep.getPath();
//			if (isSubpath(path, subpath) && path.size()>subpath.size()) {
//				List<Integer> newpath = trimSubpath(path, rep);
//				SimpRepData newrep = findLowestSimpRep(newpath);
//				if (newrep!=null)
//					return newrep;
//				else
//					return rep;
//			}
//		}
//
//		return rep;
//	}

	/**
	 * Finds the subItem (nested rep) with the longest path that
	 * is a sub-path of the given one.
	 */
	private SimpRepData findClosestSubItem(List<Integer> path) {
		if (subItems==null || subItems.size()==0)
			return null;
		SimpRepData rep = containsNestedRep(path);
		if (rep!=null)
			return rep;
		if (path.size()==1)
			return null;

		// no exact match, so look for the next shortest one
		List<Integer> newpath = new ArrayList<Integer>();
		for (int i=0; i<path.size()-1; i++) {
			newpath.add(path.get(i));
		}
		do {
			rep = containsNestedRep(newpath);
			if (rep!=null) {
				return rep;
			}
			newpath.remove(newpath.size()-1);
		} while (newpath.size()>0);

		return rep;
	}
	
	/**
	 * Returns the default status (include or exclude) for the givem 
	 * subassembly path
	 * @param path The component path for the subassembly
	 * @return One of DEFAULT_INCLUDE, DEFAULT_EXCLUDE, or DEFAULT_UNKNOWN
	 */
	public int getLevelDefault(List<Integer> path) {
		if (containsItem(path)) {
			// because if an item exists it is the opposite of the default
			return defaultExclude ? DEFAULT_INCLUDE : DEFAULT_EXCLUDE;
		}
		SimpRepData rep = findClosestSubItem(path);
		if (rep==null)
			return DEFAULT_UNKNOWN;

		List<Integer> newpath = trimSubpath(path, rep);
		if (newpath.size()==0)
			// return the default for this report
			return rep.defaultExclude ? DEFAULT_EXCLUDE : DEFAULT_INCLUDE;

		int newdef = rep.getLevelDefault(newpath);
		if (newdef!=DEFAULT_UNKNOWN)
			return newdef;
		else
			// return the default for this report
			return rep.defaultExclude ? DEFAULT_EXCLUDE : DEFAULT_INCLUDE;
	}

	/**
	 * Checks whether the specified component exists as an item
	 * anywhere in the simplified rep tree
	 * @param path The component path for the component
	 * @return Whether the item was found
	 */
	public boolean hasItem(List<Integer> path) {
		if (containsItem(path))
			return true;
		SimpRepData rep = findClosestSubItem(path);
		if (rep==null)
			return false;

		List<Integer> newpath = trimSubpath(path, rep);
		return rep.hasItem(newpath);
	}
	
	private List<Integer> trimSubpath(List<Integer> path, SimpRepData rep) {
		List<Integer> subpath = rep.getPath();
		List<Integer> newpath = new ArrayList<Integer>();
		for (int i=subpath.size(); i<path.size(); i++) {
			newpath.add(path.get(i));
		}
		return newpath;
	}

	/**
	 * Finds the simplified rep specific to the given subassembly.
	 * @param path The component path for the subassembly
	 * @return The simplified rep for that subassembly, or null if there is none
	 */
	public SimpRepData getSimpRep(List<Integer> path) {
		SimpRepData rep = findClosestSubItem(path);
		if (rep==null)
			return null;

		List<Integer> newpath = trimSubpath(path, rep);
		if (newpath.size()==0)
			// return the default for this report
			return rep;

		return rep.getSimpRep(newpath);
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
