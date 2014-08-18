package org.oasisopen.xliff.om.v1;

/**
 * Represents a tag, an inline object that represent the opening, the closing or the standalone {@link ICTag}
 * or the opening or closing {@link IMTag}.
 */
public interface ITag {

	public TagType getTagType ();
	
	/**
	 * Gets the id of this tag.
	 * @return the id of this tag.
	 */
	public String getId ();
	
	/**
	 * Sets the id of this tag (and of its corresponding opening/closing tag if needed).
	 * @param id the new id value.
	 */
	public void setId (String id);
	
	/**
	 * Gets the type for this tag.
	 * @return the type for this tag (can be null).
	 */
	public String getType ();
	
	/**
	 * Sets the type for this tag (and of its corresponding opening/closing tag if needed).
	 * @param type the new type to set. The allowed values depends on whether the tag
	 * corresponds to an {@link ICTag} or an {@link IMTag}.
	 */
	public void setType (String type);

	/**
	 * Indicates if this tag corresponds to a code ({@link ICTag}), as opposed to an annotation ({@link IMTag}).
	 * @return true if this tag corresponds to a code, false if it corresponds to an annotation
	 */
	public boolean isCode ();

}
