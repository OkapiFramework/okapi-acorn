package net.sf.okapi.acorn.common;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.IUnit;

public class MarkerCleaner extends BaseXLIFFProcessor {

	private String type = null;
	
	@Override
	public void process (IUnit unit) {
		for ( IPart part : unit ) {
			removeMarkers(part.getSource(), type);
			if ( part.hasTarget() ) {
				removeMarkers(part.getTarget(), type);
			}
		}
	}
	
	public void removeMarkers (IContent content,
		String type)
	{
		ITags tags = content.getTags();
		// Loop through the part and remove the markers
		StringBuilder tmp = new StringBuilder(content.getCodedText());
		for ( int i=0; i<tmp.length(); i++ ) {
			char ch1 = tmp.charAt(i);
			if ( net.sf.okapi.acorn.xom.XUtil.isChar1(ch1) ) {
				ITag tag = tags.get(tmp, i);
				if ( tag instanceof IMTag ) {
					if ( type != null ) { // Skip annotation markers not of the specified type
						if ( !type.equals(tag.getType()) ) continue;
					}
					tags.remove(net.sf.okapi.acorn.xom.XUtil.toKey(ch1, tmp.charAt(i+1)));
					tmp.delete(i, i+2);
				}
				else {
					i++;
				}
			}
		}
		content.setCodedText(tmp.toString());
	}
}
