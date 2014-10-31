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
 * Represents an object with a collection of extension fields ({@link IExtField} objects).
 */
public interface IWithExtFields {

	/**
	 * Indicates if this object is associated with one extension field or more,
	 * or if one namespace or more is declared in the {@link IExtFields} object.
	 * @return true if this object has at least one extension field or extension field namespace.
	 */
	public boolean hasExtField ();

	/**
	 * Gets (and if necessary create) the {@link IExtFields} object associated with this object.
	 * @return the new or existing {@link IExtFields} associated with this object.
	 */
	public IExtFields getExtFields ();
	
	/**
	 * Gets the namespace URI of the object holding these extension fields.
	 * @return the namespace URI of the object holding these extension fields (must not be null or empty).
	 */
	public String getNSUri ();

}
