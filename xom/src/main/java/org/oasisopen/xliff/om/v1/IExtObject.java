package org.oasisopen.xliff.om.v1;

import java.util.List;

/**
 * Represents an extension object.
 * <p>An extension object can hold a collection of extension objects or a terminal {@link IExtObjectData}
 * with some content.
 */
public interface IExtObject extends IExtObjectItem, IWithExtFields, Iterable<IExtObjectItem> {

	/**
	 * Gets the object namespace URI.
	 * @return the object namespace URI.
	 */
	public String getNSUri ();

	/**
	 * Gets the name of the object.
	 * @return the name of the object.
	 */
	public String getName ();

	/**
	 * Indicates if this extension object is part of an un-supported module (rather than a true extension).
	 * @return true if this object belongs to an un-supported module, false if not.
	 */
	public boolean isModule ();

	/**
	 * Indicates if this extension object contains one or more items.
	 * @return true if this object contains at least one item.
	 */
	public boolean isEmpty ();
	
	/**
	 * Gets (and if necessary creates) the list of the items in this extension objects.
	 * @return the list of the items in this extension objects (may be empty, but never null).
	 */
	public List<IExtObjectItem> getItems ();
	
	/**
	 * Adds a {@link IExtObjectData} item to this object
	 * @param content the content of the data.
	 * @param raw true if the serialization of this item should use a raw form, false for a normal content.
	 * @return the added {@link IExtObjectData} object.
	 */
	public IExtObjectData add (String content,
		boolean raw);
	
}
