package org.oasisopen.xliff.om.v1;


/**
 * Represents an object with a collection of extension fields ({@link IExtField} objects).
 */
public interface IWithExtFields {

	/**
	 * Indicates if this object is associated with one extension field or more,
	 * or if one namespace or more is declared in the {@link IExtFields} object.
	 * @return true if this object has at least one extension field or extension field namespace.
	 */
	public boolean hasExtField ();

	/**
	 * Gets (and if necessary create) the {@link IExtFields} object associated with this object.
	 * @return the new or existing {@link IExtFields} associated with this object.
	 */
	public IExtFields getExtFields ();
	
	/**
	 * Gets the namespace URI of the object holding these extension fields.
	 * @return the namespace URI of the object holding these extension fields (must not be null or empty).
	 */
	public String getNSUri ();

}
