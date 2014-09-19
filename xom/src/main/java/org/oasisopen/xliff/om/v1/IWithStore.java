package org.oasisopen.xliff.om.v1;

/**
 * Represents an object with a store.
 */
public interface IWithStore {

	/**
	 * Gets the id of this object.
	 * @return the id of the object.
	 */
	String getId ();
	
	/**
	 * Gets the {@link IStore} for this object.
	 * @return the store for this object. 
	 */
	IStore getStore ();

	/**
	 * Indicates if a given id value is already in use in the object.
	 * @param id the id value to lookup.
	 * @return true if the value is already used, false otherwise.
	 */
	public boolean isIdUsed (String id);

}
