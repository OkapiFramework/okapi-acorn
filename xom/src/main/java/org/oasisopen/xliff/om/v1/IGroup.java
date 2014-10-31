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
 * Represents a group. Such object is made of {@link IGroupOrUnit} objects, 
 * each of which is either a {@link IGroup} or a {@link IUnit} object.
 */
public interface IGroup extends IGroupOrUnit, IWithGroupOrUnit {

	/**
	 * Gets the name for this group (can be null).
	 * @return the name for this group.
	 */
	public String getName ();
	
	/**
	 * Sets the name for this group.
	 * @param name the name for this group (cane be null).
	 */
	public void setName (String name);

	/**
	 * Gets the parent for this group.
	 * @return the parent for this group, or null if it is a top-level group.
	 */
	public IGroup getParent ();
	
}
