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

import org.oasisopen.xliff.om.v1.IExtField;

/**
 * Implements the {@link IExtField} interface.
 */
public class ExtField implements IExtField {

	private String nsUri;
	private String name;
	private String value;

	public ExtField (String namespaceUri,
		String name,
		String value)
	{
		this.nsUri = namespaceUri;
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getNSUri () {
		return nsUri;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public String getValue () {
		return value;
	}

	@Override
	public void setValue (String value) {
		this.value = value;
	}

	@Override
	public boolean isModule () {
		return nsUri.startsWith(Const.NS_XLIFF_MODSTART);
	}

}
