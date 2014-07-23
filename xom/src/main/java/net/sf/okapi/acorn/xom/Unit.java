package net.sf.okapi.acorn.xom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

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
	public ISegment appendNewSegment () {
		ISegment seg = new Segment(store);
		parts.add(seg);
		return seg;
	}

	@Override
	public IPart appendNewIgnorable () {
		IPart part = new Part(store);
		parts.add(part);
		return part;
	}

	@Override
	public boolean isUnit () {
		return true;
	}

}
