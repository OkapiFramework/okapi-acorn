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
 * Represents an extension field.
 */
public interface IExtField {

	/**
	 * Gets the field namespace URI.
	 * @return the field namespace URI.
	 */
	public String getNSUri ();

	/**
	 * Gets the name of the field.
	 * @return the name of the field.
	 */
	public String getName ();

	/**
	 * Gets the value of the field.
	 * @return the value of the field (can be null).
	 */
	public String getValue ();

	/**
	 * Sets the value of the field.
	 * @param value the value of the field (cane be null).
	 */
	public void setValue (String value);

	/**
	 * Indicates if this extension field is part of an un-supported module (rather than a true extension).
	 * @return true if this field belongs to an un-supported module, false if not.
	 */
	public boolean isModule ();
	
}
