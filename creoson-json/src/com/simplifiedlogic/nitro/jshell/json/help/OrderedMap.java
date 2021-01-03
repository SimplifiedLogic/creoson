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
package com.simplifiedlogic.nitro.jshell.json.help;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * This is an implementation of java.util.Map.  Its primary function is that when 
 * you iterate through the keys or values in the Map, they are accessed in the same
 * order that the objects were originally added to the Map.  If an existing key value
 * is updated, then it is moved to the bottom of the ordering.
 * 
 * <p>This is accomplished by storing the key values separately from the internal Map,
 * in an ordered List object.
 * 
 * <p>Since you normally don't care what order that entries are stored in within a Map,
 * this is intended primarily for creating JSON help documentation, where keeping 
 * a consistent order of fields makes it easier for the reader to understand.
 * 
 * @author Adam Andrews
 *
 * @param <K> The data type for keys in the map
 * @param <V> The data type for entries in the map
 */
public class OrderedMap<K, V> implements Map<K, V> {

	/**
	 * The ordered list of Map key values
	 */
	private List<K> keys;
	/**
	 * The internal Map which holds the data
	 */
	private Map<K, V> data;
	private OrderedKeySet keySet=null;
	private OrderedEntrySet entrySet=null;
	
	/**
	 * Default constructor
	 */
	public OrderedMap() {
		keys = new Vector<K>();
		data = new Hashtable<K, V>();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	@Override
	public void clear() {
		keys.clear();
		data.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	@Override
	public boolean containsKey(Object key) {
		if (key instanceof String)
			return data.containsKey(key.toString());
		return false;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	@Override
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		if (entrySet==null)
			entrySet = new OrderedEntrySet();
		return entrySet;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	@Override
	public V get(Object key) {
		return data.get(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	@Override
	public Set<K> keySet() {
		if (keySet==null)
			keySet = new OrderedKeySet();
		return keySet;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	@Override
	public V put(K key, V value) {
		if (!keys.contains(key))
			keys.add(key);
		data.put(key, value);
		return value;
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (K key : m.keySet()) {
			put(key, m.get(key));
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	@Override
	public V remove(Object key) {
		keys.remove(key);
		return data.remove(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	@Override
	public int size() {
		return keys.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	@Override
	public Collection<V> values() {
		return data.values();
	}

	/**
	 * A representation of the Map's keySet which will return keys in the order
	 * that they were added.
	 * 
	 * <p>This doesn't have all the functionality it should have for completeness,
	 * but it has what it needs for this purpose.
	 * 
	 * @author Adam Andrews
	 *
	 */
	private class OrderedKeySet extends AbstractSet<K> {

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override
		public Iterator<K> iterator() {
			return keys.iterator();
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return keys.size();
		}
		
	}
	
	/**
	 * A representation of the Map's entrySet which will return entries in the order
	 * that they were added.
	 * 
	 * <p>This doesn't have all the functionality it should have for completeness,
	 * but it has what it needs for this purpose.
	 * 
	 * @author Adam Andrews
	 *
	 */
	private class OrderedEntrySet extends AbstractSet<java.util.Map.Entry<K, V>> {

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override
		public Iterator<java.util.Map.Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return keys.size();
		}

	}
	
	/**
	 * Create an Iterator over the entries which will loop through the keys in 
	 * the order they were added.
	 *  
	 * @author Adam Andrews
	 *
	 */
	private class EntryIterator implements Iterator<java.util.Map.Entry<K, V>> {

		private Iterator<K> keyIter = keys.iterator();
		@SuppressWarnings("unused")
		private Set<Entry<K, V>> entries = data.entrySet();
		
		/* (non-Javadoc)
		 * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext() {
			return keyIter.hasNext();
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#next()
		 */
		@Override
		public java.util.Map.Entry<K, V> next() {
			K key = keyIter.next();
			if (key==null)
				return null;
			return new OrderedEntry(key, data.get(key));
		}

		/* (non-Javadoc)
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Object used by EntryIterator to return an Entry object which
	 * represents the key-value pair in the Map.  This is needed
	 * only because the EntryIterator does not have direct access
	 * to the Entry objects provided by the internal Map.
	 *  
	 * @author Adam Andrews
	 *
	 */
	private class OrderedEntry implements Map.Entry<K,V> {
		private K key;
		private V value;

		/**
		 * @param key The value of the key
		 * @param value The value of the entry
		 */
		public OrderedEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/* (non-Javadoc)
		 * @see java.util.Map.Entry#getKey()
		 */
		@Override
		public K getKey() {
			return key;
		}

		/* (non-Javadoc)
		 * @see java.util.Map.Entry#getValue()
		 */
		@Override
		public V getValue() {
			return value;
		}

		/* (non-Javadoc)
		 * @see java.util.Map.Entry#setValue(java.lang.Object)
		 */
		@Override
		public V setValue(V value) {
			this.value = value;
			return value;
		}
		
	}
}
