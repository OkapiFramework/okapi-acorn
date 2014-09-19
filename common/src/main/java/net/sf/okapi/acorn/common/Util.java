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

import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.TagType;

public class Util {

	public static String fmt (IContent content) {
		if ( content == null ) return "null";
		StringBuilder tmp = new StringBuilder();
		for ( Object obj : content ) {
			if ( obj instanceof String ) {
				tmp.append((String)obj);
			}
			else if ( obj instanceof ICTag ) {
				ICTag ctag = (ICTag)obj;
				tmp.append("<");
				if ( ctag.getTagType() == TagType.CLOSING ) tmp.append("/");
				tmp.append("C:"+ctag.getId());
				if ( ctag.getTagType() == TagType.STANDALONE ) tmp.append("/");
				tmp.append(">");
			}
			else if ( obj instanceof IMTag ) {
				IMTag mtag = (IMTag)obj;
				tmp.append("<");
				if ( mtag.getTagType() == TagType.CLOSING ) tmp.append("/");
				tmp.append("M:"+mtag.getId());
				if ( mtag.getTagType() == TagType.STANDALONE ) tmp.append("/");
				tmp.append(">");
			}
			else {
				tmp.append("[ERR!]");
			}
		}
		return tmp.toString();
	}
}
