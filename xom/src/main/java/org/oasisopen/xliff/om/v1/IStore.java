package org.oasisopen.xliff.om.v1;

/**
 * Holds the tag objects associated with a {@link IWithStore} parent, for example
 * an {@link IUnit}. This 
 */
public interface IStore {

	/**
	 * Gets the parent object associated with this store.
	 * @return the parent of the store.
	 */
	public IWithStore getParent ();

	/**
	 * Indicates if there is at least one tag (for code or annotation) in the source content for this store.
	 * @return true if there is one, false otherwise.
	 */
	public boolean hasSourceTag ();
	
	/**
	 * Indicates if there is at least one tag (for code or annotation) in the target content for this store.
	 * @return true if there is one, false otherwise.
	 */
	public boolean hasTargetTag ();
	
	/**
	 * Gets the collection of tags associated with the source content of this store.
	 * @return the collection of source tags for this store. 
	 */
	public ITags getSourceTags ();
	
	/**
	 * Gets the collection of tags associated with the target content of this store.
	 * @return the collection of target tags for this store. 
	 */
	public ITags getTargetTags ();

	/**
	 * Gets the first tag with a given id.
	 * For constructs that have an opening and closing tag there is no prescription which one
	 * should be returned. The tag can also be from either the source and target content.
	 * @param id the id to search for.
	 * @return the first tag with the given id or null if none is found. 
	 */
	public ITag getTag (String id);

}
