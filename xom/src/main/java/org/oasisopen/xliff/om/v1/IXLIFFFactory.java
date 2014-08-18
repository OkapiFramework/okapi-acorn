package org.oasisopen.xliff.om.v1;

public interface IXLIFFFactory {

	/**
	 * Creates a new {@link IUnit} instance.
	 * @param id the id of the new unit.
	 * @return the new unit.
	 */
	public IUnit createUnit (String id);
	
	/**
	 * Creates a new {@link IContent} object.
	 * @param store the store to associate with the new object.
	 * @param isTarget true if the new content is for the target language, false for the source.
	 * @return the new content.
	 */
	public IContent createContent (IStore store,
		boolean isTarget);
	
	/**
	 * Creates a deep copy of a {@link IContent} object.
	 * @param store the store that should be associated with the new content.
	 * @param isTarget true if the destination is a target content.
	 * @param original the original object to duplicate.
	 * @return the new content.
	 */
	public IContent createContent (IStore store,
		boolean isTarget,
		IContent original);
	
	/**
	 * Creates a new {@link IPart} object with an empty source.
	 * @param store the store associated with the new part (cannot be null).
	 * @return the new part.
	 */
	public IPart createPart (IStore store);
	
	/**
	 * Creates a new {@link IPart} with a given plain text source.
	 * @param store the store associated with the new part (cannot be null).
	 * @param sourcePlainText the plain text source content of the new part.
	 * @return the new part.
	 */
	public IPart createPart (IStore store,
		String sourcePlainText);

	/**
	 * Creates a new {@link ISegment} object with an empty source.
	 * @param store the store associated with the new segment (cannot be null).
	 * @return the new part.
	 */
	public ISegment createSegment (IStore store);
	
	/**
	 * Creates a deep copy of a given {@link ITag} object.
	 * @param original the original tag to duplicate.
	 * @param destinationTags the list of destination tags. This is used to find and connect
	 * opening tags when creating closing tags.
	 * @return the new tag.
	 */
	public ITag createTag (ITag original,
		ITags destinationTags);

	/**
	 * Creates a new {@link IExtObject} object.
	 * @param nsUri the namespace URI of this object.
	 * @param name the name of this object.
	 * @return the new extension object.
	 */
	public IExtObject createExtObject (String nsUri,
		String name);
	
}
