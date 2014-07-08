package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.TargetState;

public class Segment extends Part implements ISegment {

	public final TargetState DEFSTATE_DEFAULT = TargetState.INITIAL;
	
	private boolean canResegment;
	private TargetState state;
	private String subState;
	
	/**
	 * Creates a new {@link Segment} object.
	 * @param store the shared {@link Store} for this object.
	 */
	public Segment (IStore store) {
		super(store);
	}

	@Override
	public boolean isSegment () {
		return true;
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
	public TargetState getState () {
		return state;
	}
	
	@Override
	public void setState (TargetState state) {
		this.state = state;
	}
	
	@Override
	public String getSubState () {
		return subState;
	}
	
	@Override
	public void setSubState (String subState) {
		this.subState = subState;
	}
	
}
