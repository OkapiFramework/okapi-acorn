package org.oasisopen.xliff.om.v1;

/**
 * Represents a segment in a unit.
 */
public interface ISegment extends IPart {

	/**
	 * Indicates if this segment can be re-segmented.
	 * @return true if this segment can be re-segmented, false otherwise.
	 */
	public boolean getCanResegment ();
	
	/**
	 * Sets the flag indicating if this segment can be re-segmented.
	 * @param canResegment true to indicate that this segment can be re-segmented, false otherwise.
	 */
	public void setCanResegment (boolean canResegment);
	
	/**
	 * Gets the state of this segment.
	 * @return the state of this segment.
	 */
	public TargetState getState ();
	
	/**
	 * Sets the state of this segment.
	 * @param state the new state of this segment.
	 */
	public void setState (TargetState state);
	
	/**
	 * Sets the sub-state of this segment.
	 * @return the sub-state of this segment (can be null).
	 */
	public String getSubState ();
	
	/**
	 * Sets the sub-state of this segment.
	 * @param subState the new sub-state of this segment (can be null).
	 */
	public void setSubState (String subState);

}
