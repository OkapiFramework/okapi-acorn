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
 * Indicates that an action was using the second special character of a tag reference
 * as a normal character. For example, the start position of an insert command was pointing to
 * the second character of the tag reference.
 */
public class InvalidPositionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new exception with a given text.
	 * @param text Text to go with the new exception.
	 */
	public InvalidPositionException (String text) {
		super(text);
	}
	
	/**
	 * Creates a new exception with a given parent exception.
	 * @param e The parent exception.
	 */
	public InvalidPositionException (Throwable e) {
		super(e);
	}

}
