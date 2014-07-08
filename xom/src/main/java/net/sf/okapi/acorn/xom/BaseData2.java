package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IWithExtObjects;

abstract class BaseData2 extends BaseData1 implements IWithExtObjects {

	private IExtObjects xObjs;

	@Override
	public boolean hasExtObject () {
		if ( xObjs == null ) return false;
		return !xObjs.isEmpty();
	}

	@Override
	public IExtObjects getExtObjects () {
		if ( xObjs == null ) xObjs = new ExtObjects(this);
		return xObjs;
	}

	@Override
	public String getNSUri () {
		return Const.NS_XLIFF_CORE20;
	}

}
