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
	
	public static final String NS_XLIFF_CORE20 = "urn:oasis:names:tc:xliff:document:2.0";
	public static final String NS_XLIFF20_GLOSSARY = "urn:oasis:names:tc:xliff:glossary:2.0";
	
	public static String toXML (String text,
		boolean attribute)
	{
		if ( text == null ) return "null";
		StringBuilder tmp = new StringBuilder(text.length());
		for ( int i=0; i<text.length(); i++ ) {
			char ch = text.charAt(i);
			if ( ch == '&' ) tmp.append("&amp;");
			else if ( ch == '<' ) tmp.append("&lt;");
			else if ( attribute && ( ch == '"' )) tmp.append("&quot;");
			else tmp.append(ch);
		}
		return tmp.toString();
	}

	public static String toSafeXML (String text) {
		if ( text == null ) return "null";
		// In XML 1.0 the valid characters are:
		// #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
		StringBuilder tmp = new StringBuilder(text.length());
		for ( int i=0; i<text.length(); i++ ) {
			int cp = text.codePointAt(i);
			switch ( cp ) {
			case '&':
				tmp.append("&amp;");
				break;
			case '<':
				tmp.append("&lt;");
				break;
			case 0x0009:
			case 0x000A:
			case 0x000D:
				tmp.append((char)cp);
				continue;
			default:
				if (( cp > 0x001F ) && ( cp < 0xD800 )) {
					tmp.append((char)cp);
					continue;
				}
				if ( cp > 0xD7FF ) {
					if (( cp < 0xE000 ) || ( cp == 0xFFFE ) || ( cp == 0xFFFF )) {
						tmp.append(String.format("<cp hex=\"%04X\"/>", cp));
					}
					else {
						tmp.append(Character.toChars(cp));
						i++; // Skip second char of the pair
					}
					continue;
				}
				// Else: control characters
				tmp.append(String.format("<cp hex=\"%04X\"/>", cp));
				continue;
			}
		}
		return tmp.toString();
	}

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

	public static boolean isNoE (String text) {
		return (( text == null ) || text.isEmpty() );
	}

}
