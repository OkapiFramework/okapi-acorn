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

package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.ExtObjectItemType;
import org.oasisopen.xliff.om.v1.IExtObjectData;

/**
 * Implements the {@link IExtObjectData} interface.
 */
public class ExtObjectData implements IExtObjectData {

	private String content;
	private boolean raw;

	/**
	 * Creates a new {@link IExtObjectData} object with a given content.
	 * @param content the content of the object item.
	 */
	public ExtObjectData (String content) {
		this.content = content;
	}
	
	@Override
	public ExtObjectItemType getType () {
		return ExtObjectItemType.TEXT;
	}

	@Override
	public String getContent () {
		return content;
	}

	@Override
	public void setContent (String content) {
		this.content = content;
	}

	@Override
	public boolean getRaw () {
		return raw;
	}

	@Override
	public void setRaw (boolean raw) {
		this.raw = raw;
	}

}
