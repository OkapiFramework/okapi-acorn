package org.oasisopen.xliff.om.v1;


/**
 * Represents an object with a collection of notes ({@link INote} objects).
 */
public interface IWithNotes {

	/**
	 * Indicates if this object is associated with one note or more.
	 * @return true if this object has at least one note.
	 */
	public boolean hasNote ();

	/**
	 * Gets (and if necessary create) the {@link INotes} object associated with this object.
	 * @return the new or existing {@link INotes} associated with this object.
	 */
	public INotes getNotes ();
	
}
