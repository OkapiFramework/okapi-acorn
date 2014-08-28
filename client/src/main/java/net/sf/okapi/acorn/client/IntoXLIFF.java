package net.sf.okapi.acorn.client;

import java.io.File;
import java.util.Stack;

import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.common.Event;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.resource.Code;
import net.sf.okapi.common.resource.ITextUnit;
import net.sf.okapi.common.resource.RawDocument;
import net.sf.okapi.common.resource.StartDocument;
import net.sf.okapi.common.resource.StartGroup;
import net.sf.okapi.common.resource.TextContainer;
import net.sf.okapi.common.resource.TextFragment;
import net.sf.okapi.common.resource.TextPart;
import net.sf.okapi.filters.openxml.OpenXMLFilter;

import org.oasisopen.xliff.om.v1.GetTarget;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithGroupOrUnit;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class IntoXLIFF {

	private static final IXLIFFFactory xf = Factory.XOM;
	
	private LocaleId srcLoc = LocaleId.ENGLISH;
	private LocaleId trgLoc = LocaleId.fromString("iu");
	
	public IDocument importDocument (File inputFile,
		String filterConfigId)
	{
		RawDocument rd = new RawDocument(inputFile.toURI(), "UTF-8", srcLoc, trgLoc, filterConfigId);
		IFilter filter = null;
		IDocument doc = null;
		
		try {
			filter = new OpenXMLFilter();
			filter.open(rd);
			IFile file;
			Stack<IWithGroupOrUnit> parents = new Stack<>();
			
			while ( filter.hasNext() ) {
				Event event = filter.next();
				switch ( event.getEventType() ) {
				case START_DOCUMENT:
					StartDocument sd = event.getStartDocument();
					doc = xf.createDocument();
					doc.setSourceLanguage(srcLoc.toString());
					doc.setTargetLanguage(trgLoc.toString());
					break;
				case START_SUBDOCUMENT:
					file = xf.createFile(event.getStartSubDocument().getId());
					doc.add(file);
					parents.push(file);
					break;
				case START_GROUP:
					StartGroup sg = event.getStartGroup();
					IGroup parent = null; // Default
					if ( parents.size() > 1 ) parent = (IGroup)parents.peek();
					IGroup group = xf.createGroup(parent, sg.getId());
					parents.peek().add(group);
					break;
				case TEXT_UNIT:
					ITextUnit oriUnit = event.getTextUnit();
					IUnit dstUnit = xf.createUnit(oriUnit.getId());
					parents.peek().add(dstUnit);
					convert(oriUnit, dstUnit);
					break;
				case END_GROUP:
					parents.pop();
					break;
				default:
					break;
				}
			}
		}
		finally {
			if ( filter != null ) filter.close();
		}
		
		return doc;
	}
	
	private void convert (ITextUnit oriUnit,
		IUnit dstUnit)
	{
		dstUnit.setName(oriUnit.getName());
		dstUnit.setTranslate(oriUnit.isTranslatable());
		
		TextContainer trgCont = oriUnit.getTarget(trgLoc);
		int i = 0;
		for ( TextPart srcPart : oriUnit.getSource().getParts() ) {
			IPart dstPart;
			if ( srcPart.isSegment() ) dstPart = dstUnit.appendSegment();
			else dstPart = dstUnit.appendIgnorable();
			// Source
			fillContent(dstPart, srcPart, false);
			// Target
			if ( trgCont != null ) {
				TextPart trgPart = trgCont.get(i);
				fillContent(dstPart, trgPart, true);
			}			
			i++;
		}
	}

	private void fillContent (IPart dstPart,
		TextPart oriPart,
		boolean isTarget)
	{
		TextFragment oriFrag = oriPart.getContent();
		String ct = oriFrag.getCodedText();
		IContent dstCont;
		if ( isTarget ) dstCont = dstPart.getTarget(GetTarget.CREATE_EMPTY);
		else dstCont = dstPart.getSource();
		// Process the content
		for ( int i=0; i<ct.length(); i++ ) {
			char ch = ct.charAt(i);
			switch ( ch ) {
			case TextFragment.MARKER_OPENING:
			case TextFragment.MARKER_CLOSING:
			case TextFragment.MARKER_ISOLATED:
				Code oriCode = oriFrag.getCode(ct.charAt(++i));
				switch ( oriCode.getTagType() ) {
				case CLOSING:
					ICTag dstCTag = null;
					dstCTag = dstCont.closeCodeSpan(""+oriCode.getId(), oriCode.getData());
					copyCode(oriCode, dstCTag);
					break;
				case OPENING:
					dstCTag = dstCont.openCodeSpan(""+oriCode.getId(), oriCode.getData());
					copyCode(oriCode, dstCTag);
					break;
				case PLACEHOLDER:
					dstCTag = dstCont.appendCode(""+oriCode.getId(), oriCode.getData());
					copyCode(oriCode, dstCTag);
					break;
				}
				break;
			default:
				dstCont.append(ch);
				break;
			}
		}
	}
	
	private void copyCode (Code oriCode,
		ICTag dstCTag)
	{
		dstCTag.setCanCopy(oriCode.isCloneable());
		dstCTag.setCanDelete(oriCode.isDeleteable());
		//dstCTag.setCanOverlap(oriCTag.getCanOverlap());
		//dstCTag.setCanReorder(convCanReorder(oriCTag.getCanReorder()));
		//dstCTag.setCopyOf(oriCTag.getCopyOf());
		//dstCTag.setDataDir(convDir(oriCTag.getDataDir()));
		//dstCTag.setDir(convDir(oriCTag.getDir()));
		dstCTag.setDisp(oriCode.getDisplayText());
		//dstCTag.setEquiv(oriCTag.getEquiv());
		//dstCTag.setSubFlows(oriCTag.getSubFlows());
		//dstCTag.setSubType(oriCTag.getSubType());
		//??? dstCTag.setType(oriCode.getType());
	}
	

}
