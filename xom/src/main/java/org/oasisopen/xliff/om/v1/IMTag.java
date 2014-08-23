package org.oasisopen.xliff.om.v1;

/**
 * Represents the tag of an inline marker.
 * <p><b>IMPORTANT</b>: A number of fields are shared by the opening and closing tags, implementations must ensure
 * that both objects have always the same values for those fields (i.e: when you change a value in one, it is
 * automatically changed in the other as well).
 */
public interface IMTag extends ITag, IWithExtFields {

	/**
	 * Indicates if the content within the annotation is translatable.
	 * @return if if the content is translatable, false if it is not.
	 */
	public boolean getTranslate ();
	
	/**
	 * Sets the flag indicating if the content within the annotation is translatable.
	 * @param translate true to mark the content as translatable, false to mark it as not translatable.
	 */
	public void setTranslate (boolean translate);
	
	/**
	 * Gets the value for this annotation.
	 * @return the value for this annotation (can be null).
	 */
	public String getValue ();
	
	/**
	 * Sets the value for this annotation.
	 * @param value the new value (can be null).
	 */
	public void setValue (String value);
	
	/**
	 * Gets the reference for this annotation.
	 * @return the reference for this annotation (can be null).
	 */
	public String getRef ();
	
	/**
	 * Sets the reference for this annotation.
	 * @param ref the new reference (can be null).
	 */
	public void setRef (String ref);

}
