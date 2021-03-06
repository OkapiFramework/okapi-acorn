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

import org.oasisopen.xliff.om.v1.GetTarget;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

public class Part implements IPart {

	private IContent source;
	private IContent target;
	private String id;
	private int targetOrder = 0;
	private boolean preserveWS;
	private IStore store;
	
	/**
	 * Creates a new part with an empty source.
	 * @param store the store associated with the new part (cannot be null).
	 */
	public Part (IStore store) {
		this.store = store;
		source = new Content(store, false);
	}
	
	/**
	 * Creates a new part with a given plain text source.
	 * @param store the store associated with the new part (cannot be null).
	 * @param sourcePlainText the plain text source content of the new part.
	 */
	public Part (IStore store,
		String sourcePlainText)
	{
		this(store);
		source.append(sourcePlainText);
	}

	@Override
	public boolean isSegment () {
		return false;
	}

	@Override
	public String getId () {
		return id;
	}

	@Override
	public void setId (String id) {
		this.id = id;
	}

	@Override
	public IStore getStore () {
		return store;
	}

	@Override
	public IContent getSource () {
		return source;
	}
	
//	public IContent setSource (IContent content) {
//		if ( content.isTarget() ) {
//			throw new InvalidParameterException("The content parameter must not be defined as for target.");
//		}
//		if ( content.getTags().getStore() != getStore() ) {
//			throw new InvalidParameterException("The content parameter must use the same store as the part where is is set.");
//		}
//		//TODO: clean up tags of old source
//		source = content;
//		return source;
//	}

	@Override
	public boolean hasTarget () {
		return (target != null);
	}
	
	@Override
	public IContent createTarget () {
		target = new Content(store, true);
		return target;
	}

	@Override
	public IContent getTarget () {
		return target;
	}

	@Override
	public IContent getTarget (GetTarget option) {
		if ( target == null ) {
			switch ( option ) {
			case CLONE_SOURCE:
				target = Factory.XOM.copyContent(store, true, source);
			case CREATE_EMPTY:
				target = new Content(store, true);
				break;
			case DONT_CREATE:
				// Nothing to do
				break;
			}
		}
		return target;
	}

//	public IContent setTarget (IContent content) {
//		if ( !content.isTarget() ) {
//			throw new InvalidParameterException("The content parameter must be defined as for target.");
//		}
//		if ( content.getTags().getStore() != getStore() ) {
//			throw new InvalidParameterException("The content parameter must use the same store as the part where is is set.");
//		}
//		//TODO: clean up the tags of the old target?
//		target = content;
//		return target;
//	}

	@Override
	public int getTargetOrder () {
		return targetOrder;
	}

	@Override
	public void setTargetOrder (int targetOrder) {
		if ( targetOrder < 1 ) this.targetOrder = 0; // Default
		else this.targetOrder = targetOrder;
	}

	@Override
	public boolean getPreserveWS () {
		return preserveWS;
	}

	@Override
	public void setPreserveWS (boolean preserveWS) {
		this.preserveWS = preserveWS;
	}

	@Override
	public IContent setSource (String plainText) {
		source.clear();
		if ( plainText != null ) source.append(plainText);
		return source;
	}

	@Override
	public IContent setSource (IContent content) {
		if ( content.getTags().getStore() != this.getStore() ) {
			throw new InvalidParameterException("The new content must be using the same store.");
		}
		if ( content.isTarget() ) {
			throw new InvalidParameterException("The new content must be a source.");
		}
		source.clear();
		source = content;
		return source;
	}

	@Override
	public IContent setTarget (String plainText) {
		if ( target == null ) target = new Content(store, false);
		else target.clear();
		if ( plainText != null ) target.append(plainText);
		return target;
	}

	@Override
	public IContent setTarget (IContent content) {
		if ( target != null ) target.clear();
		if ( content.getTags().getStore() != this.getStore() ) {
			throw new InvalidParameterException("The new content must be using the same store.");
		}
		if ( !content.isTarget() ) {
			throw new InvalidParameterException("The new content must be a target.");
		}
		target = content;
		return target;
	}

	@Override
	public IContent copyTarget (IContent original) {
		return setTarget(Factory.XOM.copyContent(store, true, original));
	}

}
