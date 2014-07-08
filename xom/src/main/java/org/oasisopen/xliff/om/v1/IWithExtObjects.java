package org.oasisopen.xliff.om.v1;

/**
 * Represents an object with a collection of extension objects ({@link IExtObject} objects).
 */
public interface IWithExtObjects {

	/**
	 * Indicates if this object is associated with one extension object or more,
	 * or if one namespace or more is declared in the {@link IExtObjects} object.
	 * @return true if this object has at least one extension object or extension object namespace.
	 */
	public boolean hasExtObject ();

	/**
	 * Gets (and if necessary create) the {@link IExtObjects} object associated with this object.
	 * @return the new or existing {@link IExtObjects} associated with this object.
	 */
	public IExtObjects getExtObjects ();
	
	/**
	 * Gets the namespace URI for this object.
	 * @return the namespace URI for this object (must not be null or empty).
	 */
	public String getNSUri ();

}
