package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.TagType;

public class PlaceholderCode  extends BaseCode {

	public PlaceholderCode (CodeCommon cm,
		TagType tagType,
		String id,
		String data)
	{
		super(cm, tagType, id, data);
	}

	@Override
	public Direction getDir () {
		// Special case for standalone codes
		return null;
	}

}
