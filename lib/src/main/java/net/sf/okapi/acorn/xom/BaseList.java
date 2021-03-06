/*===========================================================================
  Copyright (C) 2014 by the Okapi Framework contributors
-----------------------------------------------------------------------------
  This library is free software; you can redistribute it and/or modify it 
  under the terms of the GNU Lesser General Public License as published by 
  the Free Software Foundation; either version 2.1 of the License, or (at 
  your option) any later version.

  This library is distributed in the hope that it will be useful, but 
  WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
  General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License 
  along with this library; if not, write to the Free Software Foundation, 
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  See also the full LGPL text here: http://www.gnu.org/copyleft/lesser.html
===========================================================================*/

package net.sf.okapi.acorn.xom;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Provides the common methods for accessing and manipulating list-type objects
 * such as the notes, the extension attributes, glossary, etc.
 * Classes used as the type for the {@link BaseList} must be supported in the {@link CloneFactory}. 
 * @param <T> the type of item for this list (e.g. Note, Match, etc.).
 */
abstract class BaseList<T> implements Iterable<T> {

	private ArrayList<T> list = new ArrayList<>(2);

	/**
	 * Creates an empty {@link BaseList} object.
	 */
	protected BaseList () {
		// Do nothing
	}
	
//	/**
//	 * Copy constructor.
//	 * @param original the original object to duplicate (can be null).
//	 */
//	@SuppressWarnings("unchecked")
//	protected BaseList (BaseList<T> original) {
//		if ( original == null ) return;
//		for ( T object : original ) {
//			add((T)CloneFactory.create(object));
//		}
//	}
	
	public int size () {
		return list.size();
	}
	
	public boolean isEmpty () {
		return (list.size()==0);
	};
	
	@Override
	public Iterator<T> iterator () {
		return list.iterator();
	}

	/**
	 * Removes all objects in this list.
	 */
	public void clear () {
		list.clear();
	}
	
	/**
	 * Adds an object to this list.
	 * @param object the object to add.
	 * @return the object that was added.
	 */
	public T add (T object) {
		list.add(object);
		return object;
	}
	
	/**
	 * Removes from this list the object at the given index position. 
	 * @param index the index position.
	 * @throws IndexOutOfBoundsException if the index is invalid.
	 */
	public void remove (int index) {
		list.remove(index);
	};
	
	/**
	 * Removes a given object from this list.
	 * If the object is not in the list nothing changes.
	 * @param object the object to remove.
	 */
	public void remove (T object) {
		list.remove(object);
	};
	
	/**
	 * Gets the object at a given index position.
	 * @param index the index position.
	 * @return the object at the given index.
	 * @throws IndexOutOfBoundsException if the index is invalid.
	 */
	public T get (int index) {
		return list.get(index);
	};
	
	public T set (int index,
		T object)
	{
		list.set(index, object);
		return list.get(index);
	};

}
