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
 * Represent a part of a unit (it can be a segment or an ignorable part).
 */
public interface IPart {

	/**
	 * Gets the id of this part.
	 * @return the id of this part (can be null if its an ignorable).
	 */
	public String getId ();
	
	/**
	 * Sets the id of this part.
	 * @param id the new id value.
	 */
	public void setId (String id);

	/**
	 * Gets the store associated with this part.
	 * @return the store associated with this part (never null).
	 */
	public IStore getStore ();
	
	/**
	 * Gets the source content for this part.
	 * @return the source content of this part (never null).
	 */
	public IContent getSource ();
	
	/**
	 * Indicates if this part has a target.
	 * @return true if this part has a target (which can be empty).
	 */
	public boolean hasTarget ();
	
	/**
	 * Creates a new empty target content for this part.
	 * @return the new target content.
	 * @see #getTarget(GetTarget)
	 */
	public IContent createTarget ();

	/**
	 * Gets the target content of this part.
	 * @return the target content of this part or null if there is no target.
	 * @see #createTarget()
	 * @see #getTarget(GetTarget)
	 */
	public IContent getTarget ();
	
	/**
	 * Gets the target content for this part, and possibly create it if it does not exists yet.
	 * @param option action to take if no target exists yet for this part.
	 * @return the target content or null if there is no target.
	 * @see #createTarget()
	 * @see #getTarget()
	 */
	public IContent getTarget (GetTarget option);

	/**
	 * Indicates if this part is a segment.
	 * @return true if it is a segment, false if it is an ignorable part.
	 */
	public boolean isSegment ();
	
	/**
	 * Gets the target order for this part. Zero means the default source order.
	 * @return the target order for this part.
	 */
	public int getTargetOrder ();
	
	/**
	 * Sets the target order for this part.
	 * @param targetOrder the new target order value (Use 0 or a negative value for the default order).
	 */
	public void setTargetOrder (int targetOrder);
	
	/**
	 * Indicates if the content of this part (source and target) need to preserve the whitespace.
	 * @return true if the whitespace in this part's contents must be preserved, false otherwise. 
	 */
	public boolean getPreserveWS ();
	
	/**
	 * Sets the flag indicating if the content of this part (source and target) need to preserve the whitespace.
	 * @param preserveWS true if the whitespace in this part's contents must be preserved, false otherwise. 
	 */
	public void setPreserveWS (boolean preserveWS);

	/**
	 * Sets the source for this part as a new content made of a plain text string.
	 * @param plainText the plain text source content to set.
	 * @return the new source content.
	 */
	public IContent setSource (String plainText);
	
	/**
	 * Set the source for this part to be a given content.
	 * The tags of the previous content (if any) are removed from the store.
	 * @param content the new content (it must be set to use the proper store).
	 * @return the new source.
	 */
	public IContent setSource (IContent content);
	
	/**
	 * Sets the target for this part as a new content made of a plain text string.
	 * @param plainText the plain text target content to set.
	 * @return the new target content.
	 */
	public IContent setTarget (String plainText);

	/**
	 * Set the target for this part to be a given content.
	 * The tags of the previous content (if any) are removed from the store.
	 * @param content the new content (it must be set to use the proper store).
	 * @return the new target.
	 */
	public IContent setTarget (IContent content);

	/**
	 * Creates a copy of a given content and set it as the target for this part.
	 * @param content the existing content to copy.
	 * @return the new content, copy of the parameter.
	 */
	public IContent copyTarget (IContent content);

}
