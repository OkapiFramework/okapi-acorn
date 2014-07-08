package org.oasisopen.xliff.om.v1;

/**
 * Represents a tag, an inline object that represent the opening, the closing or the stand-alone {@link ICode}
 * or the opening or closing {@link IAnnotation}.
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
	 * corresponds to an {@link ICode} or an {@link IAnnotation}.
	 */
	public void setType (String type);

	/**
	 * Indicates if this tag corresponds to a code ({@link ICode}), as opposed to an annotation ({@link IAnnotation}).
	 * @return true if this tag corresponds to a code, false if it corresponds to an annotation
	 */
	public boolean isCode ();

}
