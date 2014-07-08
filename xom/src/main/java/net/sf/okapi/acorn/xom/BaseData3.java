package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.INotes;
import org.oasisopen.xliff.om.v1.IWithNotes;

abstract class BaseData3 extends BaseData2 implements IWithNotes {

	private INotes notes;

	@Override
	public boolean hasNote () {
		return (( notes != null ) && !notes.isEmpty() );
	}

	@Override
	public INotes getNotes () {
		if ( notes == null ) notes = new Notes();
		return notes;
	}

}
