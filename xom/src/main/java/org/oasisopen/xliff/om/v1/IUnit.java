package org.oasisopen.xliff.om.v1;

import java.util.List;

/**
 * Represents a unit. The unit is the representation of an extracted 'paragraph', for
 * example: the content of an HTML &lt;p> element, the content of a single entry in
 * a Java properties file, etc.
 * <p>A unit is made of one or more {@link ISegment} objects and zero or more "ignorable" elements 
 * which are represented by {@link IPart} objects. Note that a {@link ISegment} is derived
 * from {@link IPart}.
 */
public interface IUnit extends IGroupOrUnit, IWithStore, Iterable<IPart> {

	/**
	 * Sets the id for this unit.
	 * @param id the new id for this unit (cannot be null or empty).
	 * @throws InvalidParameterException if the id is null or empty.
	 */
	public void setId (String id);

	/**
	 * Gets the name of this unit.
	 * @return the name of this unit (can be null).
	 */
	public String getName ();
	
	/**
	 * Sets the name of this unit.
	 * @param name the name of this unit (can be null).
	 */
	public void setName (String name);
	
	public String getType ();
	
	public void setType (String type);
	
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
	 * Indicates if this unit has at least one target part not in the same order as the source.
	 * @return true if there is a target-specific order for this unit, false otherwise
	 */
	public boolean hasTargetOrder ();

	/**
	 * Creates a list of all the parts (segments and ignorable parts) in this unit in the sequence set by the target order.
	 * @return a new list of the parts (segments and ignorable parts) in the target order. 
	 */
	public List<IPart> getTargetOrderedParts ();
	
	/**
	 * Gets the object associated with a given id in this unit.
	 * <p>The objects checked are: all the parts (segments and ignorable part)
	 * and all the inline codes and annotations (the scope of the "#u=id" in an XLIFF fragment identifier).
	 * <p>Note that for inline codes and annotations with both opening and closing tags either one
	 * can be returned.
	 * @param id the id to look for.
	 * @return the object found, or null if not found.
	 */
	public Object getObject (String id);

	/**
	 * Creates a new {@link ISegment} at the end of the parts of this unit.
	 * @return the new {@link ISegment} object.
	 */
	public ISegment appendSegment ();

	/**
	 * Creates a new ignorable part at the end of the parts of this unit.
	 * @return the new {@link IPart} object.
	 */
	public IPart appendIgnorable ();

	/**
	 * Splits a segment.
	 * @param partIndex the part index of the segment to split.
	 * @param srcStart the start position of the middle new segment for the source (inclusive, in coded text).
	 * @param srcEnd the end position of the middle new segment for the source (exclusive, in coded text),
	 * use -1 for the end of the current segment.
	 * @param trgStart the start position of the middle new segment for the target (inclusive, in coded text).
	 * @param trgEnd the end position of the middle new segment for the target (exclusive, in coded text).
	 * @param changeState true to change the state and possibly the subState attributes for the modified or added
	 * segments if the initial segment as a target and its state is other than "initial" and "translated".
	 * Use false to keep the same state and subState. 
	 */
	public void split (int partIndex,
		int srcStart,
		int srcEnd,
		int trgStart,
		int trgEnd,
		boolean changeState);

	/**
	 * Joins two or more parts together into the first one.
	 * @param startPartIndex the index of the first part to join (in the target order)
	 * @param endPartIndex the index of the last part to join (in the target order)
	 * @param restrictedJoin true to throw an exception if one of the segment cannot be merged,
	 * false to allow to merge regardless of the canResegment values (e.g for merger mode)
	 * @param sourceIfNoTargetSegment true to copy the source if the target segment is absent 
	 * or empty, false to leave the target empty.
	 * @param sourceIfNoTargetIgnorable true to copy the source if the target ignorable is
	 * absent or empty, false or leave the target empty. 
	 */
	public void join (int startPartIndex,
		int endPartIndex,
		boolean restrictedJoin,
		boolean sourceIfNoTargetSegment,
		boolean sourceIfNoTargetIgnorable);

	/**
	 * Joins all the parts of this unit into a single segment.
	 * @param sourceIfNoTargetSegment true to copy the source if the target segment is absent 
	 * or empty, false to leave the target empty.
	 * @param sourceIfNoTargetIgnorable true to copy the source if the target ignorable is
	 * absent or empty, false or leave the target empty. 
	 */
	public void joinAll (boolean sourceIfNoTargetSegment,
		boolean sourceIfNoTargetIgnorable);
	
}
