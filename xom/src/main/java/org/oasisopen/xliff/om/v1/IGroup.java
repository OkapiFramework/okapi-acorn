package org.oasisopen.xliff.om.v1;


/**
 * Represents a group.
 */
public interface IGroup extends IGroupOrUnit, IWithGroupOrUnit {

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
	
}
