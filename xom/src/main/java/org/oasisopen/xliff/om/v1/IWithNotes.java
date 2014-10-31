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
 * Represents an object with a collection of notes ({@link INote} objects).
 */
public interface IWithNotes {

	/**
	 * Indicates if this object is associated with one note or more.
	 * @return true if this object has at least one note.
	 */
	public boolean hasNote ();

	/**
	 * Gets (and if necessary create) the {@link INotes} object associated with this object.
	 * @return the new or existing {@link INotes} associated with this object.
	 */
	public INotes getNotes ();
	
}
