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
 * Represents a tag, an inline object that represent the opening, the closing or the standalone {@link ICTag}
 * or the opening or closing {@link IMTag}.
 */
public interface ITag {

	public TagType getTagType ();
	
	/**
	 * Gets the id of this tag.
	 * @return the id of this tag.
	 */
	public String getId ();
	
	/**
	 * Sets the id of this tag (and of its corresponding opening/closing tag if needed).
	 * @param id the new id value.
	 */
	public void setId (String id);
	
	/**
	 * Gets the type for this tag.
	 * @return the type for this tag (can be null).
	 */
	public String getType ();
	
	/**
	 * Sets the type for this tag (and of its corresponding opening/closing tag if needed).
	 * @param type the new type to set. The allowed values depends on whether the tag
	 * corresponds to an {@link ICTag} or an {@link IMTag}.
	 */
	public void setType (String type);

	/**
	 * Indicates if this tag corresponds to a code ({@link ICTag}), as opposed to an annotation ({@link IMTag}).
	 * @return true if this tag corresponds to a code, false if it corresponds to an annotation
	 */
	public boolean isCode ();

}
