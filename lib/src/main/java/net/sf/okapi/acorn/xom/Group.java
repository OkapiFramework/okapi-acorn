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

import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

public class Group extends BaseData4 implements IGroup {

	private String name;
	private IGroup parent;
	
	/**
	 * Creates a new {@link IGroup} object with a given id and parent. 
	 * @param parent the parent of this new group node (use null for top-level groups).
	 * @param id the id for this group.
	 * @throws InvalidParameterException if the id is invalid.
	 */
	public Group (IGroup parent,
		String id)
	{
		setId(id);
		this.parent = parent;
	}

	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setName (String name) {
		this.name = name;
	}

	@Override
	public boolean isUnit () {
		return false;
	}

	@Override
	public IGroup getParent () {
		return parent;
	}

}
