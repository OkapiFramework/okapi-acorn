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

import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.IWithStore;

public class Store implements IStore {

	private final IWithStore parent;

	private Tags srcTags;
	private Tags trgTags;
	private int lastSuggested;
	private int lastSegSuggested;

	public Store (IWithStore parent) {
		//TODO: check for not null
		this.parent = parent;
	}
	
	@Override
	public IWithStore getParent () {
		return parent;
	}

	@Override
	public boolean hasSourceTag () {
		return (( srcTags != null ) && ( srcTags.size() > 0 ));
	}
	
	@Override
	public boolean hasTargetTag () {
		return (( trgTags != null ) && ( trgTags.size() > 0 ));
	}

	@Override
	public ITags getSourceTags () {
		if ( srcTags == null ) srcTags = new Tags(this);
		return srcTags;
	}

	@Override
	public ITags getTargetTags () {
		if ( trgTags == null ) trgTags = new Tags(this);
		return trgTags;
	}

	@Override
	public ITag getTag (String id) {
		if ( srcTags != null ) {
			for ( ITag tag : srcTags ) {
				if ( id.equals(tag.getId()) ) return tag;
			}
		}
		if ( trgTags != null ) {
			for ( ITag marker : trgTags ) {
				if ( id.equals(marker.getId()) ) return marker;
			}
		}
		return null;
	}

	@Override
	public String suggestId (boolean forSegment) {
		String id;
		while ( true ) {
			if ( forSegment ) {
				id = "s"+String.valueOf(++lastSegSuggested);
			}
			else {
				id = String.valueOf(++lastSuggested);
			}
			if ( !parent.isIdUsed(id) ) return id;
			// Else: try another one
		}
	}

}
