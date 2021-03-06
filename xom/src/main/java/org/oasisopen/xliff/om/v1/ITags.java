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
 * Represents the collection of tags for a source or a target content.
 */
public interface ITags extends Iterable<ITag> {

	/**
	 * Gets the {@link IStore} object that holds this set of tags.
	 * @return the store associated with this collection of tags.
	 */
	public IStore getStore ();

	/**
	 * Gets the number of tags in this collection.
	 * @return the number of tags in this collection.
	 */
	public int size ();
	
	/**
	 * Gets the tag for a given key.
	 * @param key the key of the tag to retrieve.
	 * @return the tag for the given key, or throws an exception.
	 * @throws InvalidParameterException if there is not tag for the given key.
	 */
	public ITag get (int key);
	
	/**
	 * Gets the {@link IMTag} or {@link ICTag} for a given reference in a coded text.
	 * @param ctext the coded text (e.g. String or StringBuilder object).
	 * @param pos the position of the first character of the reference.
	 * @return the tag for the given tag reference, or null if there is no corresponding tag.
	 */
	public ITag get (CharSequence ctext,
		int pos);

	/**
	 * Gets the key for a given tag.
	 * @param tag the tag to lookup.
	 * @return the key value for the given tag, or -1 if the tag is not found.
	 */
	public int getKey (ITag tag);

	/**
	 * Removes the tag for a given key.
	 * If no tag corresponds to the key no change is made.
	 * @param key the key of the tag to remove.
	 */
	public void remove (int key);

	/**
	 * Gets the opening tag for a given tag id.
	 * @param id the id of the opening tag to retrieve.
	 * @return the opening tag corresponding to the given id (or null if none is found).
	 */
	public ITag getOpeningTag (String id);

	/**
	 * Gets the opening {@link ICTag} for a given id.
	 * @param id the ID of the opening tag to search for.
	 * @return the opening {@link ICTag} or null if no tag with the given ID is found.
	 * @throws InvalidParameterException if a tag is found but it is not a {@link ICTag} object.
	 */
	public ICTag getOpeningCTag (String id);

	/**
	 * Gets the opening {@link IMTag} for a given id.
	 * @param id the ID of the opening tag to search for.
	 * @return the opening {@link IMTag} or null if no tag with the given ID is found.
	 * @throws InvalidParameterException if a tag is found but it is not a {@link IMTag} object.
	 */
	public IMTag getOpeningMTag (String id);

	/**
	 * Gets the closing tag for a given tag id.
	 * @param id the id of the closing tag to retrieve.
	 * @return the closing tag corresponding to the given id (or null if none is found).
	 */
	public ITag getClosingTag (String id);

	/**
	 * Added a given tag to this collection.
	 * @param tag the new tag to add.
	 * @return the key of the added tag.
	 */
	public int add (ITag tag);

}
