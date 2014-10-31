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
 * Represents an object with a store.
 */
public interface IWithStore {

	/**
	 * Gets the id of this object.
	 * @return the id of the object.
	 */
	String getId ();
	
	/**
	 * Gets the {@link IStore} for this object.
	 * @return the store for this object. 
	 */
	IStore getStore ();

	/**
	 * Indicates if a given id value is already in use in the object.
	 * @param id the id value to lookup.
	 * @return true if the value is already used, false otherwise.
	 */
	public boolean isIdUsed (String id);

}
