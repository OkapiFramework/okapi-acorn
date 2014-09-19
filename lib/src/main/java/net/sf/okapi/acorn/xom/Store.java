package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.IWithStore;

public class Store implements IStore {

	private final IWithStore parent;

	private Tags srcTags;
	private Tags trgTags;
	private int lastSuggested;
	private int lastSegSuggested;

	public Store (IWithStore parent) {
		//TODO: check for not null
		this.parent = parent;
	}
	
	@Override
	public IWithStore getParent () {
		return parent;
	}

	@Override
	public boolean hasSourceTag () {
		return (( srcTags != null ) && ( srcTags.size() > 0 ));
	}
	
	@Override
	public boolean hasTargetTag () {
		return (( trgTags != null ) && ( trgTags.size() > 0 ));
	}

	@Override
	public ITags getSourceTags () {
		if ( srcTags == null ) srcTags = new Tags(this);
		return srcTags;
	}

	@Override
	public ITags getTargetTags () {
		if ( trgTags == null ) trgTags = new Tags(this);
		return trgTags;
	}

	@Override
	public ITag getTag (String id) {
		if ( srcTags != null ) {
			for ( ITag tag : srcTags ) {
				if ( id.equals(tag.getId()) ) return tag;
			}
		}
		if ( trgTags != null ) {
			for ( ITag marker : trgTags ) {
				if ( id.equals(marker.getId()) ) return marker;
			}
		}
		return null;
	}

	@Override
	public String suggestId (boolean forSegment) {
		String id;
		while ( true ) {
			if ( forSegment ) {
				id = "s"+String.valueOf(++lastSegSuggested);
			}
			else {
				id = String.valueOf(++lastSuggested);
			}
			if ( !parent.isIdUsed(id) ) return id;
			// Else: try another one
		}
	}

}
