package org.oasisopen.xliff.om.v1;

/**
 * Represents a file object. Such object is made of {@link IGroupOrUnit} objects, 
 * each of which is either a {@link IGroup} or a {@link IUnit} object.
 */
public interface IFile extends IWithGroupOrUnit, IWithExtFields, IWithExtObjects, IWithNotes {

	//TODO: file-specific metadata

}
