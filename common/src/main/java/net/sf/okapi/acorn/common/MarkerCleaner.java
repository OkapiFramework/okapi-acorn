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

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.IUnit;

public class MarkerCleaner extends BaseXLIFFProcessor {

	private String type = null;
	
	@Override
	public void process (IUnit unit) {
		for ( IPart part : unit ) {
			removeMarkers(part.getSource(), type);
			if ( part.hasTarget() ) {
				removeMarkers(part.getTarget(), type);
			}
		}
	}
	
	public void removeMarkers (IContent content,
		String type)
	{
		ITags tags = content.getTags();
		// Loop through the part and remove the markers
		StringBuilder tmp = new StringBuilder(content.getCodedText());
		for ( int i=0; i<tmp.length(); i++ ) {
			char ch1 = tmp.charAt(i);
			if ( net.sf.okapi.acorn.xom.XUtil.isChar1(ch1) ) {
				ITag tag = tags.get(tmp, i);
				if ( tag instanceof IMTag ) {
					if ( type != null ) { // Skip annotation markers not of the specified type
						if ( !type.equals(tag.getType()) ) continue;
					}
					tags.remove(net.sf.okapi.acorn.xom.XUtil.toKey(ch1, tmp.charAt(i+1)));
					tmp.delete(i, i+2);
				}
				else {
					i++;
				}
			}
		}
		content.setCodedText(tmp.toString());
	}
}
