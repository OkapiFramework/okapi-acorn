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

import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.TargetState;

public class Segment extends Part implements ISegment {

	public final TargetState DEFSTATE_DEFAULT = TargetState.INITIAL;
	
	private boolean canResegment = true;
	private TargetState state = DEFSTATE_DEFAULT;
	private String subState;
	
	/**
	 * Creates a new {@link Segment} object.
	 * @param store the shared {@link Store} for this object.
	 */
	public Segment (IStore store) {
		super(store);
	}

	@Override
	public boolean isSegment () {
		return true;
	}

	@Override
	public boolean getCanResegment () {
		return canResegment;
	}

	@Override
	public void setCanResegment (boolean canResegment) {
		this.canResegment = canResegment;
	}
	
	@Override
	public TargetState getState () {
		return state;
	}
	
	@Override
	public void setState (TargetState state) {
		this.state = state;
	}
	
	@Override
	public String getSubState () {
		return subState;
	}
	
	@Override
	public void setSubState (String subState) {
		this.subState = subState;
	}
	
}
