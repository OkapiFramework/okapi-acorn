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

import java.util.ArrayList;
import java.util.List;

import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.acorn.xom.XUtil;

import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.TagType;
import org.oasisopen.xliff.om.v1.TargetState;

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

	private static String toStr (ITag tag) {
		String type = tag.getType();
		switch ( tag.getTagType() ) {
		case OPENING:
			return "o"+type;
		case CLOSING:
			return "c"+type;
		case STANDALONE:
			return "s"+type;
		}
		return null; // Should never occur
	}
	
	/**
	 * Leverages a given content into the target of a given segment.
	 * The text of the content is set in the target of the segment, then this method
	 * tries to copy the tags in the source of the segment to replace their corresponding
	 * tags into the target content. Any source tag without a match is then copied at the end
	 * of the target, and any target tag not replaced by a source one is removed.
	 * @param dest the segment where to apply the leveraging.
	 * @param translation the content to leverage into the segment.
	 */
	public static void leverage (ISegment dest,
		IContent translation)
	{
		// Get the list of the tags in the source
		List<ITag> srcTags = dest.getSource().getOwnTags();
		// Detect if there is a tag as the start of the segment
		ITag first = null;
		if ( XUtil.isChar1(dest.getSource().getCodedText().charAt(0)) ) {
			first = srcTags.get(0);
		}
		// Duplicate the translation into the target of the destination segment
		IContent cont = Factory.XOM.copyContent(dest.getStore(), true, translation);
		List<ITag> trgTags = cont.getOwnTags();
		
		if ( !srcTags.isEmpty() || !trgTags.isEmpty() ) {

			List<String> sigs = new ArrayList<>();
			for ( ITag tag : srcTags ) {
				sigs.add(toStr(tag));
			}
			ITags tags = cont.getTags();
			StringBuilder ct = new StringBuilder(cont.getCodedText());
			
			if ( !trgTags.isEmpty() ) {
				int ti = -1;
				for ( int i=0; i<ct.length(); i++ ) {
					char ch = ct.charAt(i);
					if ( XUtil.isChar1(ch) ) {
						// Get the key of the tag and its signature
						int key = XUtil.toKey(ch, ct.charAt(i+1));
						ITag tag = tags.get(key);
						String tsig = toStr(tag);
						ti++; // Increment the tag index
						for ( int j=0; j<sigs.size(); j++ ) {
							// search for non-null target signatures that match the source tag
							if ( tsig.equals(sigs.get(j)) ) {
								// Make a copy of the source tag
								ITag stag = Factory.XOM.copyTag(srcTags.get(j), tags);
								// And replace the target one
								ct.replace(i, i+2, XUtil.toRef(tags.add(stag)));
								// Remove the replaced tag from the list of target tags
								tags.remove(key);
								// Sets its signature to null (so it won't be used twice
								sigs.set(j, null);
								trgTags.set(ti, null);
								break;
							}
						}
						i++;
					}
				}
			}
			
			// Now, the non-nulls in sigs are the source tags not in the target
			// We need to add those to the new target content
			for ( int i=0; i<sigs.size(); i++ ) {
				if ( sigs.get(i) == null ) continue;
				// Try to add the code that starts or ends the segment at those positions
				if ( srcTags.get(i) == first ) {
					ct.insert(0, XUtil.toRef(tags.add(Factory.XOM.copyTag(srcTags.get(i), tags))));
				}
				else { // last or any
					ct.append(XUtil.toRef(tags.add(Factory.XOM.copyTag(srcTags.get(i), tags))));
				}
			}
			
			// And the non-nulls in trgTags are the extra tags in the target
			// We need to remove those from the new target content
			// (although some may be required
			for ( int i=0; i<trgTags.size(); i++ ) {
				if ( trgTags.get(i) == null ) continue;
				// Remove the tag from new target
				int key = cont.getTags().getKey(trgTags.get(i));
				String ref = XUtil.toRef(key);
				int p = ct.indexOf(ref);
				if ( p == -1 ) {
					//TODO: handle the issue, we should have a value
					continue;
				}
				// Delete the reference and the actual tag
				ct.delete(p, p+2);
				tags.remove(key);
			}

			// Set the new coded text
			cont.setCodedText(ct.toString());
		
		} // End of fixing up tags
		
		// Set that new content in the segment's target
		dest.setTarget(cont);
		// Update the target state
		dest.setState(TargetState.TRANSLATED);
		dest.setSubState(null); // Make sure any sub-state is reset
	}

}
