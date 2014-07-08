package org.oasisopen.xliff.om.v1;

import java.util.List;

/**
 * Represents a unit.
 */
public interface IUnit extends IGroupOrUnit, IWithStore, Iterable<IPart> {

	/**
	 * Sets the id for this unit.
	 * @param id the new id for this unit (cannot be null or empty).
	 * @throws InvalidParameterException if the id is null or empty.
	 */
	public void setId (String id);
	
	public boolean getTranslate ();
	
	public void setTranslate (boolean translate);
	
	public boolean getCanResegment ();
	
	public void setCanResegment (boolean canResegment);
	
	public Direction getSourceDir ();
	
	public void setSourceDir (Direction dir);
	
	public Direction getTargetDir ();
	
	public void setTargetDir (Direction dir);
	
	public boolean getPreserveWS ();
	
	public void setPreserveWS (boolean preserveWS);
	
	/**
	 * Indicates if this unit has at least one segment or inter-segment with target content (can be empty content).
	 * @return true if 
	 */
	public boolean hasTarget ();
	
	/**
	 * Gets the number of segments in this unit.
	 * @return the number of segments in this unit.
	 */
	public int getSegmentCount ();

	/**
	 * Gets an iterable interface for the segments in this unit.
	 * The iterable interface may not allow to remove segment.
	 * @return an iterable interface for the segments in this unit.
	 */
	public Iterable<ISegment> getSegments ();
	
	/**
	 * Gets the segment at a given segment index.
	 * @param segIndex the index of the segment to retrieve (between 0 and {@link #getSegmentCount()}-1).
	 * Note that the segment index may not be the same as the part index.
	 * @return the {@link ISegment} object at the given index position.
	 * @throws InvalidPositionException if the index is invalid.
	 */
	public ISegment getSegment (int segIndex);
	
	/**
	 * Gets the segment for a given id.
	 * @param id the id to search for.
	 * @return the segment for the given id, or null if no segment with such id has been found.
	 */
	public ISegment getSegment (String id);
	
	/**
	 * Gets the number of parts (segment and ignorable parts) in this unit.
	 * @return the number of parts (segment and ignorable parts) in this unit.
	 */
	public int getPartCount ();
	
	/**
	 * Gets an iterable interface for the parts (segments and ignorable parts) in this unit.
	 * The iterable interface may not allow to remove segment.
	 * @return an iterable interface for the parts (segments and ignorable parts) in this unit.
	 */
	public Iterable<IPart> getParts ();

	/**
	 * Gets the part (segment or ignorable part) at a given part index.
	 * @param partIndex the index of the part to retrieve (between 0 and {@link #getPartCount()}-1).
	 * Note that the part index may not be the same as the segment index.
	 * @return the {@link IPart} object at the given index position.
	 * @throws InvalidPositionException if the index is invalid.
	 */
	public IPart getPart (int partIndex);
	
	/**
	 * Gets the part (segment or ignorable part) for a given id.
	 * @param id the id to search for.
	 * @return the part (segment or ignorable part) for the given id, or null if no part with such id has been found.
	 */
	public IPart getPart (String id);

	/**
	 * Creates a list of all the parts (segments and ignorable parts) in this unit in the sequence set by the target order.
	 * @return a new list of the parts (segments and ignorable parts) in the target order. 
	 */
	public List<IPart> getTargetOrderedParts ();
	
	/**
	 * Gets the object associated with a given id in this unit.
	 * <p>The objects checked are: all the parts (segments and ignorable part)
	 * and all the inline codes and annotations.
	 * Note that for inline codes and annotations with an opening and closing tag either one
	 * can be returned.
	 * @param id the id to look for.
	 * @return the object found, or null if not found.
	 */
	public Object getObject (String id);

	/**
	 * Creates a new {@link ISegment} at the end of the parts of this unit.
	 * @return the new {@link ISegment} object.
	 */
	public ISegment appendNewSegment ();

	/**
	 * Creates a new ignorable part at the end of the parts of this unit.
	 * @return the new {@link IPart} object.
	 */
	public IPart appendNewIgnorable ();

}
