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
 * Represents a segment in a {@link IUnit}.
 * This object is derived from the {@link IPart} object.
 * A segment has always a source content and may have a target content, both are
 * represented by {@link IContent} objects.
 */
public interface ISegment extends IPart {

	/**
	 * Indicates if this segment can be re-segmented.
	 * @return true if this segment can be re-segmented, false otherwise.
	 */
	public boolean getCanResegment ();
	
	/**
	 * Sets the flag indicating if this segment can be re-segmented.
	 * @param canResegment true to indicate that this segment can be re-segmented, false otherwise.
	 */
	public void setCanResegment (boolean canResegment);
	
	/**
	 * Gets the state of this segment.
	 * @return the state of this segment.
	 */
	public TargetState getState ();
	
	/**
	 * Sets the state of this segment.
	 * @param state the new state of this segment.
	 */
	public void setState (TargetState state);
	
	/**
	 * Sets the sub-state of this segment.
	 * @return the sub-state of this segment (can be null).
	 */
	public String getSubState ();
	
	/**
	 * Sets the sub-state of this segment.
	 * @param subState the new sub-state of this segment (can be null).
	 */
	public void setSubState (String subState);

}
