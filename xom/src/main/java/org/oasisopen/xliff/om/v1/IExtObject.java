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

package org.oasisopen.xliff.om.v1;

import java.util.List;

/**
 * Represents an extension object.
 * <p>An extension object can hold a collection of extension objects or a terminal {@link IExtObjectData}
 * with some content.
 */
public interface IExtObject extends IExtObjectItem, IWithExtFields, Iterable<IExtObjectItem> {

	/**
	 * Gets the object namespace URI.
	 * @return the object namespace URI.
	 */
	public String getNSUri ();

	/**
	 * Gets the name of the object.
	 * @return the name of the object.
	 */
	public String getName ();

	/**
	 * Indicates if this extension object is part of an un-supported module (rather than a true extension).
	 * @return true if this object belongs to an un-supported module, false if not.
	 */
	public boolean isModule ();

	/**
	 * Indicates if this extension object contains one or more items.
	 * @return true if this object contains at least one item.
	 */
	public boolean isEmpty ();
	
	/**
	 * Gets (and if necessary creates) the list of the items in this extension objects.
	 * @return the list of the items in this extension objects (may be empty, but never null).
	 */
	public List<IExtObjectItem> getItems ();
	
	/**
	 * Adds a {@link IExtObjectData} item to this object
	 * @param content the content of the data.
	 * @param raw true if the serialization of this item should use a raw form, false for a normal content.
	 * @return the added {@link IExtObjectData} object.
	 */
	public IExtObjectData add (String content,
		boolean raw);
	
}
