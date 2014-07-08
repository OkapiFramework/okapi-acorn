package org.oasisopen.xliff.om.v1;

/**
 * Handle common to a group and a unit.
 */
public interface IGroupOrUnit extends IWithExtFields, IWithExtObjects, IWithNotes {

	/**
	 * Indicates if this object is a unit.
	 * @return true if this object is a {@link IUnit}, false if it is a {@link IGroup}.
	 */
	public boolean isUnit ();

}
