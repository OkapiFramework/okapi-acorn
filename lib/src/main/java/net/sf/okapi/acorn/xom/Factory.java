package net.sf.okapi.acorn.xom;

import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;
import org.oasisopen.xliff.om.v1.InvalidParameterException;
import org.oasisopen.xliff.om.v1.TagType;

/**
 * Implements a singleton instance of the {@link IXLIFFFactory} interface.
 */
public enum Factory implements IXLIFFFactory {

	/**
	 * The unique instance of the factory.
	 */
	XOM;
	
	@Override
	public boolean supports (String moduleUri) {
		// No modules are supported directly for now.
		return false;
	}

	@Override
	public IDocument createDocument () {
		return new Document();
	}

	@Override
	public IFile createFile (String id) {
		return new File(id);
	}
	
	@Override
	public IGroup createGroup (IGroup parent,
		String id)
	{
		return new Group(parent, id);
	}
	
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
	public IContent copyContent (IStore store,
		boolean isTarget,
		IContent original)
	{
		IContent dest = new Content(store, isTarget);
		ITags oriTags = original.getTags();
		ITags destTags = dest.getTags();
		StringBuilder tmp = new StringBuilder(original.getCodedText());
		int key;
		for ( int i=0; i<tmp.length(); i++ ) {
			char ch = tmp.charAt(i);
			if ( Util.isChar1(ch) ) {
				ITag tag = oriTags.get(tmp, i);
				key = destTags.add(copyTag(tag, destTags));
				tmp.replace(i, i+2, Util.toRef(key)); i++;
			}
		}
		dest.setCodedText(tmp.toString());
		return dest;
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
	public ISegment createLoneSegment () {
		return new Segment(new Store(null)); 
	}

	@Override
	public ITag copyTag (ITag original,
		ITags destinationTags)
	{
		if ( original instanceof ICTag ) {
			ICTag oct = null;
			if ( original.getTagType() == TagType.CLOSING ) {
				oct = destinationTags.getOpeningCTag(original.getId());
			}
			return new CTag((ICTag)original, (CTag)oct);
		}
		if ( original instanceof IMTag ) {
			IMTag omt = null;
			if ( original.getTagType() == TagType.CLOSING ) {
				omt = destinationTags.getOpeningMTag(original.getId());
			}
			return new MTag((IMTag)original, (MTag)omt);
		}
		// Else: error
		throw new InvalidParameterException("The type of the original object is invalid.");
	}

	@Override
	public IExtObject createExtObject (String nsUri,
		String name)
	{
		return new ExtObject(nsUri, name);
	}

	
}
