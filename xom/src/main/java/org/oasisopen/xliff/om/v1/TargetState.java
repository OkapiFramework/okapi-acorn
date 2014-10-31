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
* Possible values for the <code>state</code> attribute of a target content:
* {@link #INITIAL}, {@link #TRANSLATED}, {@link #REVIEWED} and {@link #FINAL}.
*/
public enum TargetState {

	/**
	 * Target state: initial.
	 */
	INITIAL("initial"),
	
	/**
	 * Target state: translated.
	 */
	TRANSLATED("translated"),
	
	/**
	 * Target state: reviewed.
	 */
	REVIEWED("reviewed"),
	
	/**
	 * Target state: final.
	 */
	FINAL("final");
	
	private String name;

	/**
	 * Creates a new targetState object with a given name.
	 * @param name the name of the item to create.
	 */
	private TargetState (String name) {
		this.name = name;
	}

	@Override
	public String toString () {
		return name;
	}
	
	/**
	 * Converts a given string representing the name of a target state into a {@link TargetState} object.
	 * @param name the name of the target state.
	 * @return the object for the given name.
	 * @throws InvalidParameterException if the name is invalid.
	 */
	public static TargetState fromString (String name) {
		if ( name == null ) {
			throw new InvalidParameterException("State cannot be null.");
		}
		switch ( name ) {
		case "initial":
			return INITIAL;
		case "translated":
			return TRANSLATED;
		case "reviewed":
			return REVIEWED;
		case "final":
			return FINAL;
		default:
			throw new InvalidParameterException(String.format("Invalid state value: '%s'.", name));
		}
	}

}
