package org.oasisopen.xliff.om.v1;

/**
 * Represents an object that can have groups or units.
 */
public interface IWithGroupOrUnit extends Iterable<IGroupOrUnit> {

	/**
	 * Gets the id for this object.
	 * @return the id for this object (never null).
	 */
	public String getId ();
	
	/**
	 * Sets the id for this object.
	 * @param id the id for this object (cannot be null).
	 */
	public void setId (String id);

	/**
	 * Gets the {@link IGroup} for a given id.
	 * The group can be at any level in this object.
	 * @param id the id to look for.
	 * @return the group for the given id, or null if none is found.
	 */
	public IGroup getGroup (String id);
	
	/**
	 * Adds a {@link IGroup} to this object.
	 * @param group the group to add.
	 * @return the added group (same as the parameter).
	 */
	public IGroup add (IGroup group);

	/**
	 * Gets the {@link IUnit} for a given id.
	 * The unit can be at any level in this object.
	 * @param id the id to look for.
	 * @return the unit for the given id, or null if none is found.
	 */
	public IUnit getUnit (String id);
	
	/**
	 * Adds a {@link IUnit} to this object.
	 * @param unit the unit to add.
	 * @return the added unit (same as the parameter).
	 */
	public IUnit add (IUnit unit);

}
