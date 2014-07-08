package org.oasisopen.xliff.om.v1;

/**
 * Represents a note object.
 */
public interface INote extends IWithExtFields {

	/**
	 * Gets the text content of this note.
	 * @return the text content of this note.
	 */
	public String getText ();
	
	/**
	 * Sets the content of this note.
	 * @param content the new content to set.
	 */
	public void setText (String content);
	
	/**
	 * Indicates if this note is empty, that is if the content is null or empty.
	 * @return true if the content is null or empty.
	 */
	public boolean isEmpty ();

	/**
	 * Gets the id of this note.
	 * @return the id of this note (never null or empty).
	 */
	public String getId ();
	
	/**
	 * Sets the id of this note.
	 * @param id the id of this note (must not be empty or null).
	 */
	public void setId (String id);
	
	/**
	 * Gets the category for this note.
	 * @return the category for this note (can be null).
	 */
	public String getCategory ();
	
	/**
	 * Sets the category for this note.
	 * @param category the category to set (can be null).
	 */
	public void setCategory (String category);
	
	/**
	 * Gets the priority for this note.
	 * @return the priority for this note (default is 1).
	 */
	public int getPriority ();
	
	/**
	 * Sets the priority for this note.
	 * @param priority the priority for this note.
	 * The value must be between 1 (the highest priority) and 10 (the lowest priority).
	 */
	public void setPriority (int priority);

	/**
	 * Indicates to what type of content this note applies.
	 * @return One of the {@link AppliesTo} values (default is {@link AppliesTo#UNDEFINED}).
	 */
	public AppliesTo getAppliesTo ();
	
	/**
	 * Sets the indicator for what type of content this note applies to.
	 * @param appliesTo One of the {@link AppliesTo} values.
	 */
	public void setAppliesTo (AppliesTo appliesTo);

}
