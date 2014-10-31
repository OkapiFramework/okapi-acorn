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
 * Represents an object that can have groups or units.
 */
public interface IWithGroupOrUnit extends Iterable<IGroupOrUnit> {

	/**
	 * Gets the id for this object.
	 * @return the id for this object (never null).
	 */
	public String getId ();
	
	/**
	 * Sets the id for this object.
	 * @param id the id for this object (cannot be null).
	 */
	public void setId (String id);

	/**
	 * Gets the {@link IGroup} for a given id.
	 * The group can be at any level in this object.
	 * @param id the id to look for.
	 * @return the group for the given id, or null if none is found.
	 */
	public IGroup getGroup (String id);
	
	/**
	 * Adds a {@link IGroup} to this object.
	 * @param group the group to add.
	 * @return the added group (same as the parameter).
	 */
	public IGroup add (IGroup group);

	/**
	 * Gets the {@link IUnit} for a given id.
	 * The unit can be at any level in this object.
	 * @param id the id to look for.
	 * @return the unit for the given id, or null if none is found.
	 */
	public IUnit getUnit (String id);
	
	/**
	 * Adds a {@link IUnit} to this object.
	 * @param unit the unit to add.
	 * @return the added unit (same as the parameter).
	 */
	public IUnit add (IUnit unit);

}
