package org.oasisopen.xliff.om.v1;

/**
 * Represents the collection of tags for a source or a target content.
 */
public interface ITags extends Iterable<ITag> {

	/**
	 * Gets the {@link IStore} object that holds this set of tags.
	 * @return the store associated with this collection of tags.
	 */
	public IStore getStore ();

	/**
	 * Gets the number of tags in this collection.
	 * @return the number of tags in this collection.
	 */
	public int size ();
	
	/**
	 * Gets the tag for a given key.
	 * @param key the key of the tag to retrieve.
	 * @return the tag for the given key, or throws an exception.
	 * @throws InvalidParameterException if there is not tag for the given key.
	 */
	public ITag get (int key);
	
	/**
	 * Gets the key for a given tag.
	 * @param tag the tag to lookup.
	 * @return the key value for the given tag, or -1 if the tag is not found.
	 */
	public int getKey (ITag tag);

	/**
	 * Removes the tag for a given key.
	 * If no tag corresponds to the key no change is made.
	 * @param key the key of the tag to remove.
	 */
	public void remove (int key);

	/**
	 * Gets the opening tag for a given tag id.
	 * @param id the id of the opening tag to retrieve.
	 * @return the opening tag corresponding to the given id (or null if none is found).
	 */
	public ITag getOpeningTag (String id);

	/**
	 * Gets the closing tag for a given tag id.
	 * @param id the id of the closing tag to retrieve.
	 * @return the closing tag corresponding to the given id (or null if none is found).
	 */
	public ITag getClosingTag (String id);

	/**
	 * Added a given tag to this collection.
	 * @param tag the new tag to add.
	 * @return the added tag (same as the parameter).
	 */
	public int add (ITag tag);
}
