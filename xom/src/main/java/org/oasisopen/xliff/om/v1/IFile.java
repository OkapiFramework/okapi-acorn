package org.oasisopen.xliff.om.v1;

/**
 * Represents a file object.
 */
public interface IFile extends IWithExtFields, IWithExtObjects, IWithNotes {

	/**
	 * Gets the id for this file.
	 * @return the id for this file (never null).
	 */
	public String getId ();
	
	/**
	 * Sets the id for this file.
	 * @param id the id for this file (cannot be null).
	 */
	public void setId (String id);

	/**
	 * Gets the {@link IGroup} for a given id.
	 * The group can be at any level in this file.
	 * @param id the id to look for.
	 * @return the group for the given id, or null if none is found.
	 */
	public IGroup getGroup (String id);
	
	/**
	 * Adds a {@link IGroup} to this file.
	 * @param group the group to add.
	 * @return the added group (same as the parameter).
	 */
	public IGroup add (IGroup group);

	/**
	 * Gets the {@link IUnit} for a given id.
	 * The unit can be at any level in this file.
	 * @param id the id to look for.
	 * @return the unit for the given id, or null if none is found.
	 */
	public IUnit getUnit (String id);
	
	/**
	 * Adds a {@link IUnit} to this file.
	 * @param unit the unit to add.
	 * @return the added unit (same as the parameter).
	 */
	public IUnit add (IUnit unit);
	
}
