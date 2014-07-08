package org.oasisopen.xliff.om.v1;

/**
 * Represents an extension field.
 */
public interface IExtField {

	/**
	 * Gets the field namespace URI.
	 * @return the field namespace URI.
	 */
	public String getNSUri ();

	/**
	 * Gets the name of the field.
	 * @return the name of the field.
	 */
	public String getName ();

	/**
	 * Gets the value of the field.
	 * @return the value of the field (can be null).
	 */
	public String getValue ();

	/**
	 * Sets the value of the field.
	 * @param value the value of the field (cane be null).
	 */
	public void setValue (String value);

	/**
	 * Indicates if this extension field is part of an un-supported module (rather than a true extension).
	 * @return true if this field belongs to an un-supported module, false if not.
	 */
	public boolean isModule ();
	
}
