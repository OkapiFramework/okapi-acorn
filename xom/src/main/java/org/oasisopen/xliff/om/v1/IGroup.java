package org.oasisopen.xliff.om.v1;


/**
 * Represents a group.
 */
public interface IGroup extends IGroupOrUnit, Iterable<IGroupOrUnit> {

	/**
	 * Gets the id for this group.
	 * @return the id for this group (never null).
	 */
	public String getId ();
	
	/**
	 * Sets the id for this group.
	 * @param id the id for this group (cannot be null).
	 */
	public void setId (String id);
	
	/**
	 * Gets the name for this group (can be null).
	 * @return the name for this group.
	 */
	public String getName ();
	
	/**
	 * Sets the name for this group.
	 * @param name the name for this group (cane be null).
	 */
	public void setName (String name);

	/**
	 * Gets the parent for this group.
	 * @return the parent for this group, or null if it is a top-level group.
	 */
	public IGroup getParent ();
	
	/**
	 * Gets the {@link IGroup} for a given id.
	 * The group can be at any level in this group.
	 * @param id the id to look for.
	 * @return the group for the given id, or null if none is found.
	 */
	public IGroup getGroup (String id);
	
	/**
	 * Adds a group to this group.
	 * @param group the group to add.
	 * @return the added group (same as the parameter).
	 */
	public IGroup add (IGroup group);
	
	/**
	 * Gets the {@link IUnit} for a given id.
	 * The unit can be at any level in this group.
	 * @param id the id to look for.
	 * @return the unit for the given id, or null if none is found.
	 */
	public IUnit getUnit (String id);
	
	/**
	 * Adds a unit to this group.
	 * @param unit the unit to add.
	 * @return the added unit (same as the parameter).
	 */
	public IUnit add (IUnit unit);
	
}
