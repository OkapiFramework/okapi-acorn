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

/**
 * Holds the tag objects associated with a {@link IWithStore} parent, for example
 * an {@link IUnit}. This 
 */
public interface IStore {

	/**
	 * Gets the parent object associated with this store.
	 * @return the parent of the store.
	 */
	public IWithStore getParent ();

	/**
	 * Indicates if there is at least one tag (for code or annotation) in the source content for this store.
	 * @return true if there is one, false otherwise.
	 */
	public boolean hasSourceTag ();
	
	/**
	 * Indicates if there is at least one tag (for code or annotation) in the target content for this store.
	 * @return true if there is one, false otherwise.
	 */
	public boolean hasTargetTag ();
	
	/**
	 * Gets the collection of tags associated with the source content of this store.
	 * @return the collection of source tags for this store. 
	 */
	public ITags getSourceTags ();
	
	/**
	 * Gets the collection of tags associated with the target content of this store.
	 * @return the collection of target tags for this store. 
	 */
	public ITags getTargetTags ();

	/**
	 * Gets the first tag with a given id.
	 * For constructs that have an opening and closing tag there is no prescription which one
	 * should be returned. The tag can also be from either the source and target content.
	 * @param id the id to search for.
	 * @return the first tag with the given id or null if none is found. 
	 */
	public ITag getTag (String id);

	/**
	 * Gets a suggested id for code, annotation, ignorable or segment.
	 * @param forSegment true for a segment id, false for other elements.
	 * @return a suggested id which does not exist in this unit.
	 */
	public String suggestId (boolean forSegment);

}
