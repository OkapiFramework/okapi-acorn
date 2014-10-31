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

package org.oasisopen.xliff.om.v1;

import java.util.List;
import java.util.Map;

/**
 * Represents the inline content of an extracted original document.
 * For example, the content of XLIFF <code>&lt;source></code> or <code>&lt;target></code>.
 * The content is composed of text and {@link ITag} objects which represent the inline
 * data that are not text.
 */
public interface IContent extends Iterable<Object> {

	/**
	 * Indicates if this content has any text or tag reference.
	 * @return true if there is at least one character or one tag reference, false otherwise.
	 */
	public boolean isEmpty ();
	
	/**
	 * Indicates if this is a target content.
	 * @return true if it is a target content, false if it is a source content.
	 */
	public boolean isTarget ();
	
	/**
	 * Indicates if this content has at least one tag reference.
	 * @return true if this content has one tag reference or more.
	 */
	public boolean hasTag ();
	
	/**
	 * Indicates if this content has some text (content that is not tags).
	 * @param seeWhitespaceAsText true to see whitespace as part of the text,
	 * false to not consider whitespace as text. 
	 * @return true if there is text, false otherwise.
	 */
	public boolean hasText (boolean seeWhitespaceAsText);
	
	/**
	 * Gets a plain text version (all tag references stripped out) of the content of this fragment.
	 * @return the plain text version of this fragment.
	 */
	public String getPlainText ();

	/**
	 * Gets the coded text for this content.
	 * @return the coded text for this content.
	 * @see #setCodedText(String)
	 * @see #getTags()
	 * @see #getOwnTags()
	 */
	public String getCodedText ();
	
	/**
	 * Sets the coded text for this content.
	 * See {@link #getCodedText()} for more information on coded text.
	 * @param codedText the new coded text. It must have all the necessary tag references.
	 * @see #getCodedText()
	 */
	public void setCodedText (String codedText);
	
	/**
	 * Gets the tag collection associated with this content.
	 * Note that tags are shared across all segments, so it may contain tags not used in this content.
	 * To get a list of only the tags used in this content use {@link #getOwnTags()}.
	 * @return the tag collection associated with this content.
	 * @see #getCodedText()
	 * @see #getOwnTags()
	 */
	public ITags getTags ();
	
	/**
	 * Creates a list of the tags that have a reference in this content.
	 * The tag are listed in the order they occur in the coded text. The tags are the live objects,
	 * but the list is not live (not updated if tags are added/removed).
	 * To get the collection of all tags associated with this content and the other contents of
	 * the unit use {@link #getTags()}.
	 * @return a new list of the tags that have a reference in this content (can be empty but never null).
	 * @see #getTags()
	 */
	public List<ITag> getOwnTags ();

	/**
	 * Clears this content of all text and its tags.
	 */
	public void clear ();
	
	/**
	 * Deletes a span of content and any tag that has its reference within that span.
	 * @param start the first character to delete in the coded text.
	 * @param end the position after the last character to delete in the coded text.
	 * You can use -1 to indicate the end of the content.
	 */
	public void delete (int start,
		int end);
	
	/**
	 * Appends a plain text string at the end of this content.
	 * @param plainText the text to append.
	 * @return this content itself.
	 */
	public IContent append (CharSequence plainText);
	
	/**
	 * Appends a character at the end of this content.
	 * @param ch the character to add.
	 * @return this content itself.
	 */
	public IContent append (char ch);

	/**
	 * Appends an {@link ICTag} object at the end of this content.
	 * @param code the code to append.
	 * @return the appended code (same code as the parameter).
	 */
	public ICTag append (ICTag code);
	
	/**
	 * Appends a content to this content.
	 * @param content the content to append (cannot be itself or null).
	 */
	public void append (IContent content);
	
	/**
	 * Appends to this content the opening tag for a new code.
	 * @param id the id of the code.
	 * @param data the original data for the code, e.g. <code>&lt;B></code> (can be null).
	 * @return the new {@link ICTag} created.
	 * @see #closeCodeSpan(String, String)
	 * @see #appendCode(String, String)
	 */
	public ICTag openCodeSpan (String id, 
		String data);
	
	/**
	 * Appends to this content the closing tag for an existing opened code.
	 * @param id the id of the code to close.
	 * @param data the original data for the code, e.g. <code>&lt;/B></code> (can be null).
	 * @return the new {@link ICTag} created.
	 * @see #openCodeSpan(String, String) 
	 */
	public ICTag closeCodeSpan (String id,
		String data);
	
	/**
	 * Appends a standalone inline code to this content.
	 * @param id the id of the code.
	 * @param data the original data for the code, e.g. <code>&lt;BR></code> (can be null). 
	 * @return the new {@link ICTag} created.
	 * @see #openCodeSpan(String, String)
	 */
	public ICTag appendCode (String id,
		String data);
	
	/**
	 * Appends to this content the opening tag of a new inline marker. 
	 * @param id the id of the marker.
	 * @param type the type of the marker.
	 * @return the new {@link IMTag} created.
	 */
	public IMTag openMarkerSpan (String id,
		String type);
	
	/**
	 * Appends to this content the closing tag for an existing opened marker.
	 * @param id the id of the marker to close.
	 * @return the new {@link IMTag} created.
	 */
	public IMTag closeMarkerSpan (String id);

	/**
	 * Inserts a plain text string at a given position in the coded text.
	 * @param pos the position where to insert.
	 * @param plainText the string to insert.
	 * @return this content itself.
	 * @throws InvalidPositionException if the insertion point is inside a tag reference.
	 */
	public IContent insert (int pos,
		String plainText);

	/**
	 * Inserts an inline code's tag at a given position in the coded text.
	 * @param pos the position where to insert.
	 * @param code the inline code's tag to insert.
	 * @return the {@link ICTag} that was inserted (same as the parameter).
	 * @throws InvalidPositionException if the insertion point is inside a tag reference.
	 */
	public ICTag insert (int pos,
		ICTag code);

	/**
	 * Annotates a span of content in this fragment.
	 * @param start the start position (in the coded text)
	 * @param end the position just after the last character of the span (in the coded text).
	 * You can use -1 to indicate the end of the fragment.
	 * @param type the type of the annotation.
	 * @param value the value of the <code>value</code> attribute (can be null).
	 * @param ref the value of the <code>ref</code> attribute (can be null).
	 * @return the number of characters added to the coded text.
	 */
	public int annotate (int start,
		int end,
		String type,
		String value,
		String ref);
	
	/**
	 * Annotates a span of content in this fragment.
	 * @param start the start position (in the coded text)
	 * @param end the position just after the last character of the span (in the coded text).
	 * You can use -1 to indicate the end of the fragment.
	 * @param opening the start tag of the marker. The end tag will be generated from this tag.
	 * @return the number of characters added to the coded text. 
	 */
	public int annotate (int start,
		int end,
		IMTag opening);

	/**
	 * Creates a map of the opening and closing tags in this fragment and their corresponding
	 * status as tag: (0=isolated, 1=not-well-formed or 2=well-formed).
	 * <p>isolated means to counterpart tag within the parent unit.
	 * <p>(not-)well-formed means (not-)well-formed within this fragment.  
	 * @return a map of the opening and closing tags and their status, the map
	 * may be empty but never null.
	 */
	public Map<ITag, Integer> getOwnTagsStatus ();
	
	/**
	 * Get or create an annotation marker tag for a given span of content.
	 * @param start the start position (in the coded text)
	 * @param end the position just after the last character of the span (in the coded text).
	 * You can use -1 to indicate the end of the fragment.
	 * @param matchingType the type of marker that can be reused. Use null to reuse any marker.
	 * @param typeForNew the type of the marker to create of none reusable is found (must not be null).
	 * @return the opening tag of the marker found or created.
	 */
	public IMTag getOrCreateMTag (int start,
		int end,
		String matchingType,
		String typeForNew);

}
