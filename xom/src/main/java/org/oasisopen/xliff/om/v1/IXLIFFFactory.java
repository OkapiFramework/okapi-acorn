package org.oasisopen.xliff.om.v1;

/**
 * Provides methods to create new or copies of various objects for the interface.
 */
public interface IXLIFFFactory {

	/**
	 * Indicates if this implementation of the object model supports a given module.
	 * @param moduleUri the namespace URI of the module for which the query is made.
	 * @return true if the module is supported, false if it is not.
	 */
	public boolean supports (String moduleUri);

	/**
	 * Creates a new empty instance of {@link IDocument}.
	 * @return the new document.
	 */
	public IDocument createDocument ();

	/**
	 * Creates a new empty instance of {@link IFile}.
	 * @param id the id of this file (must not be null or empty).
	 * @return the new file.
	 */
	public IFile createFile (String id);
	
	/**
	 * Creates a new empty instance of {@link IGroup}.
	 * @param parent the parent group or null to create a top-level group.
	 * @param id the id of the group (must not be null or empty).
	 * @return the new group.
	 */
	public IGroup createGroup (IGroup parent,
		String id);
	
	/**
	 * Creates a new instance of {@link IUnit}.
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
	public IContent copyContent (IStore store,
		boolean isTarget,
		IContent original);
	
	/**
	 * Creates a copy of a {@link ISegment} object, with all its core metadata but with an empty content.
	 * The method will create a new ID for the new segment.
	 * @param store the store that should be associated with the new segment.
	 * @param original the original object to copy.
	 * @return the new segment.
	 */
	public ISegment copyEmptySegment (IStore store,
		ISegment original);
	
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
	 * @return the new segment.
	 */
	public ISegment createSegment (IStore store);
	
	/**
	 * Creates a new unattached {@link ISegment} object with an empty source.
	 * This segment will have its own store and cannot be used with other or within a unit.
	 * Lone segments cannot use any methods related to their parent. 
	 * @return the new segment.
	 */
	public ISegment createLoneSegment ();
	
	/**
	 * Creates a deep copy of a given {@link ITag} object.
	 * @param original the original tag to duplicate.
	 * @param destinationTags the list of destination tags. This is used to find and connect
	 * opening tags when creating closing tags.
	 * @return the new tag.
	 */
	public ITag copyTag (ITag original,
		ITags destinationTags);

	/**
	 * Creates a new {@link IExtObject} object.
	 * @param nsUri the namespace URI of this object.
	 * @param name the name of this object.
	 * @return the new extension object.
	 */
	public IExtObject createExtObject (String nsUri,
		String name);
	
	/**
	 * Creates a new opening tag for a marker.
	 * @param id the id of the marker.
	 * @param type the type of the marker.
	 * @return the new tag.
	 */
	public IMTag createOpeningMTag (String id,
		String type);

}
