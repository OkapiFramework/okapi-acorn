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

package net.sf.okapi.acorn.common;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

public abstract class BaseXLIFFProcessor implements IXLIFFProcessor {

	@Override
	public void process (IDocument document) {
		if ( document == null ) return;
		for ( IFile file : document ) {
			for ( IGroupOrUnit gou : file ) {
				if ( gou.isUnit() ) process((IUnit)gou);
				else process((IGroup)gou);
			}
		}
	}
	
	protected void process (IGroup group) {
		for ( IGroupOrUnit gou : group ) {
			if ( gou.isUnit() ) process((IUnit)gou);
			else process((IGroup)gou);
		}
	}

	protected void process (IUnit unit) {
		for ( ISegment seg : unit.getSegments() ) {
			process(seg);
		}
	}
	
	protected void process (ISegment segment) {
		// Do nothing by default
	}
	
}
