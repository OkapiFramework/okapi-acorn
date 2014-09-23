package net.sf.okapi.acorn.xom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.InvalidParameterException;
import org.oasisopen.xliff.om.v1.TargetState;

public class Unit extends BaseData3 implements IUnit {

	private String id;
	private String name;
	private String type;
	private IStore store;
	private ArrayList<IPart> parts;
	private boolean translate = true;
	private boolean canResegment = true;
	private Direction srcDir = Direction.INHERITED;
	private Direction trgDir = Direction.INHERITED;
	private boolean preserveWS;
	
	/**
	 * Creates a new {@link Unit} object.
	 * @param id the id of the unit.
	 */
	public Unit (String id) {
		setId(id);
		store = new Store(this);
		parts = new ArrayList<IPart>(1);
	}

	@Override
	public String getId () {
		return id;
	}

	@Override
	public void setId (String id) {
		if (( id == null ) || id.isEmpty() ) {
			throw new InvalidParameterException("Id cannot be null or empty.");
		}
		this.id = id;
	}
	
	@Override
	public String getName () {
		return name;
	}

	@Override
	public void setName (String name) {
		this.name = name;
	}

	@Override
	public String getType () {
		return type;
	}

	@Override
	public void setType (String type) {
		this.type = type;
	}

	@Override
	public boolean getTranslate () {
		return translate;
	}
	
	@Override
	public void setTranslate (boolean translate) {
		this.translate = translate;
	}
	
	@Override
	public boolean getCanResegment () {
		return canResegment;
	}
	
	@Override
	public void setCanResegment (boolean canResegment) {
		this.canResegment = canResegment;
	}
	
	@Override
	public Direction getSourceDir () {
		return srcDir;
	}
	
	@Override
	public void setSourceDir (Direction dir) {
		this.srcDir = dir;
	}
	
	@Override
	public Direction getTargetDir () {
		return trgDir;
	}
	
	@Override
	public void setTargetDir (Direction dir) {
		this.trgDir = dir;
	}

	@Override
	public boolean getPreserveWS () {
		return preserveWS;
	}

	@Override
	public void setPreserveWS (boolean preserveWS) {
		this.preserveWS = preserveWS;
	}

	@Override
	public IStore getStore () {
		return store;
	}

	@Override
	public boolean hasTarget () {
		for ( IPart part : parts ) {
			if ( part.hasTarget() ) return true;
		}
		return false;
	}

	@Override
	public int getSegmentCount () {
		int count = 0;
		for ( IPart part : parts ) {
			if ( part.isSegment() ) count++;
		}
		return count;
	}

	@Override
	public Iterable<ISegment> getSegments () {
		return new Iterable<ISegment>() {
			@Override
			public Iterator<ISegment> iterator () {
				return new Iterator<ISegment>() {
					int current = 0;

					@Override
					public void remove () {
						throw new UnsupportedOperationException("The method remove() not supported.");
					}

					@Override
					public ISegment next () {
						while ( current < parts.size() ) {
							IPart part = parts.get((++current)-1);
							if ( part.isSegment() ) return (ISegment)part;
						}
						return null;
					}

					@Override
					public boolean hasNext () {
						int tmp = current;
						while ( tmp < parts.size() ) {
							if ( parts.get(tmp).isSegment() ) return true;
							tmp++;
						}
						return false;
					}
				};
			}
		};
	}

	@Override
	public ISegment getSegment (int segIndex) {
		int si = 0;
		for ( int i=0; i<parts.size(); i++ ) {
			if ( parts.get(i).isSegment() ) {
				if ( si == segIndex ) {
					return (ISegment)parts.get(i);
				}
				si++;
			}
		}
		throw new InvalidParameterException(
			String.format("The index %d is out-of-bound for segments.", segIndex));
	}

	@Override
	public ISegment getSegment (String id) {
		for ( IPart part : parts ) {
			if ( part.isSegment() ) {
				String sid = part.getId();
				if (( sid != null ) && sid.equals(id) ) return (ISegment)part;
			}
		}
		return null;
	}

	@Override
	public Iterator<IPart> iterator () {
		return parts.iterator();
	}

	@Override
	public int getPartCount () {
		return parts.size();
	}

	@Override
	public Iterable<IPart> getParts () {
		return parts;
	}

	@Override
	public IPart getPart (int partIndex) {
		try {
			return parts.get(partIndex);
		}
		catch ( Throwable e ) {
			throw new InvalidParameterException(
				String.format("The part index %d is out-of-bound.", partIndex));
		}
	}

	@Override
	public IPart getPart (String id) {
		for ( IPart part : parts ) {
			// The part's id can be null, but id.equals() should support that
			if ( id.equals(part.getId()) ) return (IPart)part;
		}
		return null;
	}

	@Override
	public boolean hasTargetOrder () {
		for ( int i=0; i<parts.size(); i++ ) {
			if ( parts.get(i).getTargetOrder() > 0 ) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<IPart> getTargetOrderedParts () {
		ArrayList<IPart> list = new ArrayList<>(parts);
		int index = 1; // Order values are 1-based, real index is 0-based
		for ( IPart part : parts ) {
			int order = part.getTargetOrder();
			if ( order == 0 ) order = index; // Default
			list.set(order-1, part);
			index++;
		}
		return list;
	}

	@Override
	public Object getObject (String id) {
		// Check the part
		for ( IPart part : parts ) {
			// The part's id can be null, but equals should support that
			if ( id.equals(part.getId()) ) return part;
		}
		// Check the markers
		return store.getTag(id);
	}

	@Override
	public ISegment appendSegment () {
		ISegment seg = new Segment(store);
		parts.add(seg);
		return seg;
	}

	@Override
	public IPart appendIgnorable () {
		IPart part = new Part(store);
		parts.add(part);
		return part;
	}

	@Override
	public boolean isUnit () {
		return true;
	}

	@Override
	public boolean isIdUsed (String id) {
		return (getObject(id) != null);
	}

	@Override
	public void split (int partIndex,
		int srcStart,
		int srcEnd,
		int trgStart,
		int trgEnd,
		boolean changeState)
	{
//		//--- Debug trace
//		System.out.println("before:");
//		List<Part> list = getTargetOrderedParts();
//		for ( Part tmp : list ) {
//			System.out.print("{"+tmp.getTargetOrSource().toXLIFF(null)+"}");
//		}
//		System.out.println("");
//		//--- End debug trace

		IPart part = getPart(partIndex);
		if ( !part.isSegment() ) {
			throw new InvalidParameterException("Cannot split a non-segment part.");
		}
		ISegment oriSeg = (ISegment)part;
		IContent src = part.getSource();

		String srcCt = src.getCodedText();
		if ( srcEnd == -1 ) srcEnd = srcCt.length()-1;

		// Do various checks
		if ( srcStart > srcEnd ) {
			throw new InvalidParameterException("Invalid source range.");
		}
		if (( srcStart < 0 ) || ( srcCt.length() < srcEnd )) {
			throw new InvalidParameterException("Source range out of bounds.");
		}
//		if ( Fragment.isMarker(ctext.codePointAt(srcStart)) || Fragment.isMarker(ctext.codePointAt(srcEnd)) ) {
//			throw new InvalidParameterException("You cannot split inside a inline marker.");
//		}

		String trgCt = null;
		boolean hasTarget = oriSeg.hasTarget();
		if ( hasTarget ) {
			trgCt = oriSeg.getTarget().getCodedText();
			if ( trgStart > trgEnd ) {
				throw new InvalidParameterException("Invalid target range.");
			}
			if (( trgStart < 0 ) || ( trgCt.length() < trgEnd )) {
				throw new InvalidParameterException("Target range out of bounds.");
			}
		}
		
		String srcMid = srcCt.substring(srcStart, srcEnd);
		boolean srcToDo = true;
		if ( srcMid.isEmpty() && (( srcStart == 0 ) || ( srcStart >= srcCt.length() ))) {
			// Middle new part is empty and at of of the ends: There is nothing to split
			srcToDo = false;
		}
		
		String trgMid = "";
		if ( hasTarget ) {
			trgMid = trgCt.substring(trgStart, trgEnd);
			if ( trgMid.isEmpty() & (( trgStart == 0 ) || ( trgStart >= trgCt.length() ))) {
				// Nothing to split for the target
				// If there is nothing to split for the source either we stop here
				if ( !srcToDo ) return;
			}
		}
		
		String srcLeft = srcCt.substring(0, srcStart);
		String srcRight = srcCt.substring(srcEnd); 
		
		String trgLeft = "";
		String trgRight = "";
		if ( hasTarget ) {
			trgLeft = trgCt.substring(0, trgStart);
			trgRight = trgCt.substring(trgEnd);
		}
		
		// Fill empty content by non-empty ones, shifting content to the left
		// (so we start at the right-most part)
		if ( srcMid.isEmpty() ) {
			srcMid = srcRight; srcRight = "";
		}
		if ( srcLeft.isEmpty() ) {
			srcLeft = srcMid; srcMid = "";
		}
		if ( trgMid.isEmpty() ) {
			trgMid = trgRight; trgRight = "";
		}
		if ( trgLeft.isEmpty() ) {
			trgLeft = trgMid; trgMid = "";
		}

		// Re-use the original segment first
		if ( !srcLeft.isEmpty() || !trgLeft.isEmpty() ) {
			part.getSource().setCodedText(srcLeft);
			if ( hasTarget ) part.getTarget().setCodedText(trgLeft);
		}
		int added = 0;
		// Add a first segment if needed (it'll be the new right or middle)
		if ( !srcMid.isEmpty() || !trgMid.isEmpty() ) {
			ISegment seg = Factory.XOM.copyEmptySegment(oriSeg.getStore(), oriSeg);
			seg.getSource().setCodedText(srcMid);
			if ( hasTarget ) seg.getTarget().setCodedText(trgMid);
			parts.add(partIndex+(++added), seg);
		}
		// Add a last segment if needed (it'll be the new right)
		if ( !srcRight.isEmpty() || !trgRight.isEmpty() ) {
			ISegment seg = Factory.XOM.copyEmptySegment(oriSeg.getStore(), oriSeg);
			seg.getSource().setCodedText(srcRight);
			if ( hasTarget ) seg.getTarget().setCodedText(trgRight);
			parts.add(partIndex+(++added), seg);
		}
		
		// If we have added part: we may need to adjust the target orders
		if ( added > 0 ) {
			if ( hasTargetOrder() ) {
				int oriOrder = parts.get(partIndex).getTargetOrder();
				int resolvedOriOrder = (oriOrder > 0 ) ? oriOrder : partIndex+1;
				for ( int i=0; i<parts.size(); i++ ) {
					if ( i == partIndex+1 ) {
						// The first added part is always the order of the original + 1
						parts.get(i).setTargetOrder(resolvedOriOrder+1);
					}
					else if (( added == 2 ) && ( i == partIndex+2 )) {
						// the second added part is always the order of the original + 2
						parts.get(i).setTargetOrder(resolvedOriOrder+2);
					}
					else {
						int order = parts.get(i).getTargetOrder();
						// Calculate the old order
						int oldResolvedOrder;
						if ( order > 0 ) oldResolvedOrder = order;
						else {
							// If it was using the default part position we need to adjust
							// when that part is after the added part(s)
							if ( i <= partIndex ) oldResolvedOrder = i+1;
							else oldResolvedOrder = (i-added)+1;
						}
						// Calculate the new order: the same as before, except when
						// it's after the position of the original part
						int newOrder = oldResolvedOrder;
						if ( oldResolvedOrder > resolvedOriOrder ) {
							newOrder = oldResolvedOrder+added;
						}
						// Now we set the new value or use the default
						if ( i+1 == newOrder ) parts.get(i).setTargetOrder(0); // Same as default
						else parts.get(i).setTargetOrder(newOrder);
					}
				}
			}
			
			if ( changeState && hasTarget ) {
				// Update the state and possibly the subState if needed
				for ( int i=0; i<=added; i++ ) {
					part = parts.get(partIndex+i);
					if ( part.isSegment() ) {
						Segment seg = (Segment)part;
						switch ( seg.getState() ) {
						case INITIAL:
						case TRANSLATED:
							// No change
							break;
						default: // Update the state and subState
							seg.setState(TargetState.TRANSLATED);
							seg.setSubState(null);
							break;
						}
					}
				}
				
			}
			
//			//--- Debug trace
//			System.out.println("after-fix:");
//			list = getTargetOrderedParts();
//			for ( Part tmp : list ) {
//				System.out.print("{"+tmp.getTargetOrSource().toXLIFF(null)+"}");
//			}
//			System.out.println("");
//			//--- End debug trace
		}
	}

}
