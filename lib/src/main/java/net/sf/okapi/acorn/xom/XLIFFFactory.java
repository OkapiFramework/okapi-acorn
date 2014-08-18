package net.sf.okapi.acorn.xom;

import net.sf.okapi.acorn.xom.json.JSONReader;
import net.sf.okapi.acorn.xom.json.JSONWriter;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class XLIFFFactory implements IXLIFFFactory {

	private JSONWriter jw = new JSONWriter();
	private JSONReader jr = new JSONReader();

	@Override
	public IUnit createUnit (String id) {
		return new Unit(id);
	}

	@Override
	public IContent createContent (IStore store,
		boolean isTarget)
	{
		return new Content(store, isTarget);
	}

	@Override
	public IContent createContent (IStore store,
		boolean isTarget,
		IContent original)
	{
		String str = jw.fromContent(original).toJSONString();
		return jr.readContent(store, isTarget, str);
	}

	@Override
	public IPart createPart (IStore store) {
		return new Part(store);
	}

	@Override
	public IPart createPart (IStore store,
		String sourcePlainText)
	{
		IPart part = createPart(store);
		part.getSource().append(sourcePlainText);
		return part;
	}

	@Override
	public ISegment createSegment (IStore store) {
		return new Segment(store); 
	}

	@Override
	public ITag createTag (ITag original,
		ITags destinationTags)
	{
		return null;
//		if ( original instanceof ICTag ) {
//			ICTag oct = null;
//			if ( original.getTagType() == TagType.CLOSING ) {
//				oct = destinationTags.getOpeningCTag(original.getId());
//				return new EndCode(oct, data)
//			}
//			new StartCode(cm, tagType, id, data)
//			return new CTag((CTag)original, oct);
//		}
//		if ( original instanceof MTag ) {
//			MTag mct = null;
//			if ( original.getTagType() == TagType.CLOSING ) {
//				mct = destinationTags.getOpeningMTag(original.getId());
//			}
//			return new MTag((MTag)original, mct);
//		}
//		throw new InvalidParameterException("The type of the original object is invalid.");
	}
	@Override
	public IExtObject createExtObject (String nsUri,
		String name)
	{
		return new ExtObject(nsUri, name);
	}
}
