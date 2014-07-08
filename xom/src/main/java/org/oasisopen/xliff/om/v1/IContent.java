package org.oasisopen.xliff.om.v1;

import java.util.List;

/**
 * Represents the inline content of an extracted original document.
 * For example, the content of XLIFF <code>&lt;source></code> or <code>&lt;target></code>.
 */
public interface IContent {

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
	 * The tag are listed in the order they occur in the coded text.
	 * To get the collection of all tags associated with this content and the other content of
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
	 */
	public void append (String plainText);
	
	/**
	 * Appends a character at the end of this content.
	 * @param ch the character to add.
	 */
	public void append (char ch);

	/**
	 * Appends an inline code at the end of this content.
	 * @param code the code to add.
	 * @return the code that was added (same as the parameter).
	 */
	public ICode append (ICode code);
	
	/**
	 * Appends an opening code to this content.
	 * @param id the id of the code.
	 * @param data the original data for the code, e.g. <code>&lt;B></code>. (can be null).
	 * @return the new {@link ICode} created.
	 * @see #closeCode(String, String)
	 * @see #appendStandaloneCode(String, String)
	 */
	public ICode appendOpeningCode (String id, 
		String data);
	
	/**
	 * Appends a closing tag for an existing opened code.
	 * @param id the id of the code to close.
	 * @param data the original data for the code, e.g. <code>&lt;/B></code>. (can be null).
	 * @return the new {@link ICode} created.
	 * @see #appendOpeningCode(String, String) 
	 */
	public ICode closeCode (String id,
		String data);
	
	/**
	 * Appends a standalone inline code to this content.
	 * @param id the id of the code.
	 * @param data the original data for the code, e.g. <code>&lt;BR></code>. (can be null). 
	 * @return the new {@link ICode} created.
	 * @see #appendOpeningCode(String, String)
	 */
	public ICode appendStandaloneCode (String id,
		String data);
	
	/**
	 * Appends an opening inline annotation to this content. 
	 * @param id the id of the annotation.
	 * @param type the type of the annotation.
	 * @return the new {@link IAnnotation} created.
	 */
	public IAnnotation appendOpeningAnnotation (String id,
		String type);
	
	/**
	 * Appends a closing tag for an existing opened annotation.
	 * @param id the id of the annotation to close.
	 * @return the new {@link IAnnotation} created.
	 */
	public IAnnotation closeAnnotation (String id);

	/**
	 * Inserts a plain text string at a given position in the coded text.
	 * @param pos the position where to insert.
	 * @param plainText the string to insert.
	 * @throws InvalidPositionException if the insertion point is inside a tag reference.
	 */
	public void insert (int pos,
		String plainText);

	/**
	 * Inserts an inline code at a given position in the coded text.
	 * @param pos the position where to insert.
	 * @param code the inline code to insert.
	 * @return the code that was inserted (same as the parameter).
	 * @throws InvalidPositionException if the insertion point is inside a tag reference.
	 */
	public ICode insert (int pos,
		ICode code);

}
