package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.GetTarget;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.IStore;

public class Part implements IPart {

	private IContent source;
	private IContent target;
	private String id;
	private int targetOrder = 0;
	private boolean preserveWS;
	private IStore store;
	
	/**
	 * Creates a new part with an empty source.
	 * @param store the store associated with the new part (cannot be null).
	 */
	public Part (IStore store) {
		this.store = store;
		source = new Content(store, false);
	}
	
	/**
	 * Creates a new part with a given plain text source.
	 * @param store the store associated with the new part (cannot be null).
	 * @param sourcePlainText the plain text source content of the new part.
	 */
	public Part (IStore store,
		String sourcePlainText)
	{
		this(store);
		source.append(sourcePlainText);
	}

	@Override
	public boolean isSegment () {
		return false;
	}

	@Override
	public String getId () {
		return id;
	}

	@Override
	public void setId (String id) {
		this.id = id;
	}

	@Override
	public IStore getStore () {
		return store;
	}

	@Override
	public IContent getSource () {
		return source;
	}
	
//	public IContent setSource (IContent content) {
//		if ( content.isTarget() ) {
//			throw new InvalidParameterException("The content parameter must not be defined as for target.");
//		}
//		if ( content.getTags().getStore() != getStore() ) {
//			throw new InvalidParameterException("The content parameter must use the same store as the part where is is set.");
//		}
//		//TODO: clean up tags of old source
//		source = content;
//		return source;
//	}

	@Override
	public boolean hasTarget () {
		return (target != null);
	}
	
	@Override
	public IContent createTarget () {
		target = new Content(store, true);
		return target;
	}

	@Override
	public IContent getTarget () {
		return target;
	}

	@Override
	public IContent getTarget (GetTarget option) {
		if ( target == null ) {
			switch ( option ) {
			case CLONE_SOURCE:
				throw new RuntimeException("CLONE_SOURCE case not implemented yet");
			case CREATE_EMPTY:
				target = new Content(store, true);
				break;
			case DONT_CREATE:
				// Nothing to do
				break;
			}
		}
		return target;
	}

//	public IContent setTarget (IContent content) {
//		if ( !content.isTarget() ) {
//			throw new InvalidParameterException("The content parameter must be defined as for target.");
//		}
//		if ( content.getTags().getStore() != getStore() ) {
//			throw new InvalidParameterException("The content parameter must use the same store as the part where is is set.");
//		}
//		//TODO: clean up the tags of the old target?
//		target = content;
//		return target;
//	}

	@Override
	public int getTargetOrder () {
		return targetOrder;
	}

	@Override
	public void setTargetOrder (int targetOrder) {
		if ( targetOrder < 1 ) this.targetOrder = 0; // Default
		else this.targetOrder = targetOrder;
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
	public IContent setSource (String plainText) {
		source.clear();
		source.append(plainText);
		return source;
	}

	@Override
	public IContent setTarget (String plainText) {
		if ( target == null ) target = new Content(store, false);
		else target.clear();
		target.append(plainText);
		return target;
	}

}
