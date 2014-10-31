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
 * Represents a collection of notes ({@link INote} objects).
 */
public interface INotes extends Iterable<INote> {

	/**
	 * Gets the number of notes in this list.
	 * @return the number of notes in this list.
	 */
	public int size ();
	
	/**
	 * Indicates if this list is empty.
	 * @return true if there is no note in this list, false if there is one or more.
	 */
	public boolean isEmpty ();
	
	/**
	 * Removes all notes in this list.
	 */
	public void clear ();
	
	/**
	 * Adds a note to this list.
	 * @param note the note to add.
	 * @return the note that was added.
	 */
	public INote add (INote note);
	
	/**
	 * Removes from this list the note at the given index position. 
	 * @param index the index position.
	 * @throws InvalidParameterException if the index is invalid.
	 */
	public void remove (int index);
	
	/**
	 * Removes a given note from this list.
	 * If the note is not in the list nothing changes.
	 * @param note the note to remove.
	 */
	public void remove (INote note);
	
	/**
	 * Gets the note at a given index position.
	 * @param index the index position.
	 * @return the note at the given index.
	 * @throws InvalidParameterException if the index is invalid.
	 */
	public INote get (int index);
	
	/**
	 * Replaces an existing note by a new one at a given index position.
	 * @param index the index position.
	 * @param note the new note to set.
	 * @return the note that was set.
	 * @throws InvalidParameterException if the index is invalid.
	 */
	public INote set (int index,
		INote note);

}
