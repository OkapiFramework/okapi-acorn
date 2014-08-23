package net.sf.okapi.acorn.client;

import java.io.File;

import net.sf.okapi.common.Event;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.resource.ITextUnit;
import net.sf.okapi.common.resource.RawDocument;
import net.sf.okapi.common.resource.Segment;
import net.sf.okapi.common.resource.StartDocument;
import net.sf.okapi.common.resource.StartSubDocument;
import net.sf.okapi.common.resource.TextPart;
import net.sf.okapi.filters.openxml.OpenXMLFilter;
import net.sf.okapi.lib.xliff2.core.Part;
import net.sf.okapi.lib.xliff2.core.StartFileData;
import net.sf.okapi.lib.xliff2.core.Unit;
import net.sf.okapi.lib.xliff2.document.FileNode;
import net.sf.okapi.lib.xliff2.document.XLIFFDocument;

public class IntoXLIFF {

	LocaleId srcLoc = LocaleId.ENGLISH;
	LocaleId trgLoc = LocaleId.fromString("iu");
	public void importDocument (File inputFile,
		String filterConfigId)
	{
		RawDocument rd = new RawDocument(inputFile.toURI(), "UTF-8", srcLoc, trgLoc, filterConfigId);
		IFilter filter = null;
		try {
			filter = new OpenXMLFilter();
			filter.open(rd);
			XLIFFDocument doc = new XLIFFDocument();
			FileNode fn;
			
			while ( filter.hasNext() ) {
				Event event = filter.next();
				switch ( event.getEventType() ) {
				case START_DOCUMENT:
					convert(doc, event.getStartDocument());
					break;
				case START_SUBDOCUMENT:
					break;
				case START_SUBFILTER:
					fn = convert(event.getStartSubDocument());
					break;
				case START_GROUP:
					break;
				case DOCUMENT_PART:
					break;
				case TEXT_UNIT:
					Unit unit = convert(event.getTextUnit());
					break;
				case END_GROUP:
					break;
				case END_SUBFILTER:
					break;
				case END_SUBDOCUMENT:
					break;
				case END_DOCUMENT:
					break;
				}
			}
		}
		finally {
			if ( filter != null ) filter.close();
		}
	}
	
	private void convert (XLIFFDocument doc,
		StartDocument sd)
	{
		doc.getStartXliffData().setSourceLanguage(srcLoc.toString());
		doc.getStartXliffData().setTargetLanguage(trgLoc.toString());
	}

	private FileNode convert (StartSubDocument ssd) {
		StartFileData sfd = new StartFileData(ssd.getId());
		FileNode fn = new FileNode(sfd);
		return fn;
	}

	private Unit convert (ITextUnit tu) {
		Unit unit = new Unit(tu.getId());
		unit.setName(tu.getName());
		for ( TextPart srcPart : tu.getSource().getParts() ) {
			if ( srcPart.isSegment() ) {
				Segment srcSeg = (Segment)srcPart;
			}
			else {
				Part xPart = unit.appendIgnorable();
				//convert(xPart, srcPart.getContent())
			}
		}
		return unit;
	}
}
