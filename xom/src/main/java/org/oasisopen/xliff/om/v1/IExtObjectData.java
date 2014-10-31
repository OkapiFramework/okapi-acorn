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
 * Represents an extension object data.
 */
public interface IExtObjectData extends IExtObjectItem {

	/**
	 * Gets the text content of this object item.
	 * @return the text content of this object item (can be null).
	 */
	public String getContent ();
	
	/**
	 * Sets the text content of this object item.
	 * @param content the text content of this object item (can be null).
	 */
	public void setContent (String content);

	/**
	 * Indicates if, when possible, this text content should be serialized with a mechanism that allows
	 * its representation without escaping (e.g: in a CDATA section in XML).
	 * @return true if a raw output is requested.
	 */
	public boolean getRaw ();
	
	/**
	 * Sets the flag indicating if a raw output is requested.
	 * @param raw true to request a raw output, false to request a normal output.
	 */
	public void setRaw (boolean raw);
	
}
