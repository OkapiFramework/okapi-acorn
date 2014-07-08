package org.oasisopen.xliff.om.v1;

/**
 * Represents a collection of notes ({@link INote} objects).
 */
public interface INotes extends Iterable<INote> {

	/**
	 * Gets the number of notes in this list.
	 * @return the number of notes in this list.
	 */
	public int size ();
	
	/**
	 * Indicates if this list is empty.
	 * @return true if there is no note in this list, false if there is one or more.
	 */
	public boolean isEmpty ();
	
	/**
	 * Removes all notes in this list.
	 */
	public void clear ();
	
	/**
	 * Adds a note to this list.
	 * @param note the note to add.
	 * @return the note that was added.
	 */
	public INote add (INote note);
	
	/**
	 * Removes from this list the note at the given index position. 
	 * @param index the index position.
	 * @throws InvalidParameterException if the index is invalid.
	 */
	public void remove (int index);
	
	/**
	 * Removes a given note from this list.
	 * If the note is not in the list nothing changes.
	 * @param note the note to remove.
	 */
	public void remove (INote note);
	
	/**
	 * Gets the note at a given index position.
	 * @param index the index position.
	 * @return the note at the given index.
	 * @throws InvalidParameterException if the index is invalid.
	 */
	public INote get (int index);
	
	/**
	 * Replaces an existing note by a new one at a given index position.
	 * @param index the index position.
	 * @param note the new note to set.
	 * @return the note that was set.
	 * @throws InvalidParameterException if the index is invalid.
	 */
	public INote set (int index,
		INote note);

}
