package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.IWithExtFields;

abstract class BaseData1 implements IWithExtFields {

	private IExtFields xFields;

	@Override
	public boolean hasExtField () {
		if ( xFields == null ) return false;
		return !xFields.isEmpty();
	}

	@Override
	public IExtFields getExtFields () {
		if ( xFields == null ) xFields = new ExtFields(this);
		return xFields;
	}

	@Override
	public String getNSUri () {
		return Const.NS_XLIFF_CORE20;
	}

}
