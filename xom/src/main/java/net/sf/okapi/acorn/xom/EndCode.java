package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.TagType;

public class EndCode  extends BaseCode {

	public EndCode (CodeCommon cm,
		TagType tagType,
		String id,
		String data)
	{
		super(cm, tagType, id, data);
	}

	public EndCode (StartCode sc,
		String data)
	{
		super(sc.cm, TagType.CLOSING, sc.getId(), data);
	}
	
}
