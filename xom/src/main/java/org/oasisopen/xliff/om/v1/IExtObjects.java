package org.oasisopen.xliff.om.v1;

import java.util.List;

/**
 * Represents a collection of extension objects ({@link IExtObject} objects).
 */
public interface IExtObjects extends IWithExtFields, Iterable<IExtObject> {

	/**
	 * Gets the object to which the objects apply.
	 * @return the object to which the objects apply.
	 */
	public IWithExtObjects getParent ();

	
	public IExtObject add (String nsUri,
		String name);
	
	/**
	 * Gets the list of the objects for the given name and namespace URI.
	 * @param nsUri the object namespace URI.
	 * @param name the object name.
	 * @return the list of the objects for the given namespace URI and name (may be empty but never null).
	 */
	public List<IExtObject> find (String nsUri,
		String name);

	/**
	 * Get, and if needed, create before, a given extension object from this collection.
	 * If the object is created it is added automatically to the collection.
	 * @param nsUri the namespace of the extension object.
	 * @param name the name of the extension object.
	 * @return the extension object searched for: the first existing one, or one just created.
	 */
	public IExtObject getOrCreate (String nsUri,
		String name);
	
	/**
	 * Indicates if this collection is empty or not.
	 * @return true if there is no extension objects and no namespaces defined in this collection.
	 */
	public boolean isEmpty ();
	
	/**
	 * Removes all objects and namespaces from this collection.
	 */
	public void clear ();
	
	/**
	 * Removes the given object from this collection.
	 * If the object does not exists, nothing happens. 
	 * @param object the object to delete.
	 */
	public void delete (IExtObject object);

	/**
	 * Declares a namespace.
	 * @param nsUri the namespace URI.
	 * @param nsShorthand the namespace shorthand.
	 */
	public void setNS (String nsUri, String nsShorthand);
	
}
