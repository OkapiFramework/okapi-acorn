package net.sf.okapi.acorn.common;

import java.io.File;
import java.util.UUID;

import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.lib.xliff2.core.CTag;
import net.sf.okapi.lib.xliff2.core.CanReorder;
import net.sf.okapi.lib.xliff2.core.Directionality;
import net.sf.okapi.lib.xliff2.core.ExtAttribute;
import net.sf.okapi.lib.xliff2.core.ExtAttributes;
import net.sf.okapi.lib.xliff2.core.ExtContent;
import net.sf.okapi.lib.xliff2.core.ExtElement;
import net.sf.okapi.lib.xliff2.core.ExtElements;
import net.sf.okapi.lib.xliff2.core.Fragment;
import net.sf.okapi.lib.xliff2.core.IExtChild;
import net.sf.okapi.lib.xliff2.core.IWithExtElements;
import net.sf.okapi.lib.xliff2.core.MTag;
import net.sf.okapi.lib.xliff2.core.Part;
import net.sf.okapi.lib.xliff2.core.StartFileData;
import net.sf.okapi.lib.xliff2.core.StartGroupData;
import net.sf.okapi.lib.xliff2.core.StartXliffData;
import net.sf.okapi.lib.xliff2.core.Tag;
import net.sf.okapi.lib.xliff2.core.Unit;
import net.sf.okapi.lib.xliff2.reader.Event;
import net.sf.okapi.lib.xliff2.reader.XLIFFReader;

import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.GetTarget;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithExtFields;
import org.oasisopen.xliff.om.v1.IWithExtObjects;
import org.oasisopen.xliff.om.v1.IXLIFFFactory;

public class XLIFF2Reader implements IDocumentReader {
	
	final static IXLIFFFactory xf = Factory.XOM;

	@Override
	public IDocument load (File inputFile) {
		IDocument doc = null;
		IFile file = null;
		IGroup group = null;
		try ( XLIFFReader reader = new XLIFFReader() ) {
			reader.open(inputFile);
			while ( reader.hasNext() ) {
				Event event = reader.next();
				switch ( event.getType() ) {
				case START_DOCUMENT:
					doc = xf.createDocument();
					break;
				case START_XLIFF:
					StartXliffData sxd = event.getStartXliffData();
					doc.setSourceLanguage(sxd.getSourceLanguage());
					doc.setTargetLanguage(sxd.getTargetLanguage());
					//todo: ext attributes
					break;
				case START_FILE:
					StartFileData sfd = event.getStartFileData();
					file = xf.createFile(sfd.getId());
					doc.add(file);
					//todo: ext attributes
					//copyExtAttributes(file, sfd);
					break;
				case START_GROUP:
					StartGroupData sgd = event.getStartGroupData();
					IGroup newGroup = xf.createGroup(group, sgd.getId());
					if ( group == null ) file.add(newGroup);
					else group.add(newGroup);
					group = newGroup;
					group.setName(sgd.getName());
					file.add(group);
					//group.setType(sgd.getType());
					//todo copy 
					copyExtAttributes(group, sgd.getExtAttributes());
					break;
				case TEXT_UNIT:
					IUnit mUnit = fromXLIFFUnit(event.getUnit());
					if ( group == null ) file.add(mUnit);
					else group.add(mUnit);
					break;
				case END_GROUP:
					group = group.getParent();
					break;
				case MID_FILE:
					break;
				case SKELETON:
					break;
				case END_DOCUMENT:
				case END_FILE:
				case END_XLIFF:
				case INSIGNIFICANT_PART:
					break;
				}
			}
		}
		return doc;
	}
	
	private IUnit fromXLIFFUnit (Unit oriUnit) {
		IUnit dstUnit = xf.createUnit(oriUnit.getId());
		dstUnit.setCanResegment(oriUnit.getCanResegment());
		dstUnit.setTranslate(oriUnit.getTranslate());
		dstUnit.setName(oriUnit.getName());
		dstUnit.setType(oriUnit.getType());
		dstUnit.setSourceDir(convDir(oriUnit.getSourceDir()));
		dstUnit.setTargetDir(convDir(oriUnit.getTargetDir()));
		//mUnit.setPreserveWS(unit.getPreserveWS());
		for ( Part oriPart : oriUnit ) {
			IPart dstPart;
			if ( oriPart.isSegment() ) dstPart = dstUnit.appendSegment();
			else dstPart = dstUnit.appendIgnorable();
			// Part-level data
			dstPart.setId(UUID.randomUUID().toString()); //oriPart.getId());
			dstPart.setPreserveWS(oriPart.getPreserveWS());
			// Source
			fillContent(dstPart, oriPart.getSource(), false);
			// Target
			if ( oriPart.hasTarget() ) fillContent(dstPart, oriPart.getTarget(), true);
		}
		copyExtAttributes(dstUnit, oriUnit.getExtAttributes());
		copyExtElements(dstUnit, oriUnit);
		return dstUnit;
	}
	
	private void copyExtElements (IWithExtObjects dest,
		IWithExtElements ori)
	{
		if ( !ori.hasExtElements() ) return;
		ExtElements xElems = ori.getExtElements();
		IExtObjects xObjs = dest.getExtObjects();
		for ( ExtElement xElem : xElems ) {
			IExtObject xObj = xObjs.add(xElem.getQName().getNamespaceURI(), xElem.getQName().getLocalPart());
			copyExtAttributes(xObj, xElem.getAttributes());
			switch ( xElem.getType() ) {
			case ELEMENT:
			default:
				copyChildren(xObj, xElem);
				break;
			case CDATA:
				xObj.add(xElem.getFirstContent().getText(), true);
				break;
			case PI:
				throw new RuntimeException("Support for PI is TODO");
			case TEXT:
				xObj.add(xElem.getFirstContent().getText(), false);
				break;
			}
		}
	}
	
	private void copyChildren (IExtObject xObj,
		ExtElement xElem)
	{
		if ( !xElem.hasChild() ) return;
		for ( IExtChild child : xElem.getChildren() ) {
			switch ( child.getType() ) {
			case CDATA:
				xObj.add(((ExtContent)child).getText(), true);
				break;
			case ELEMENT:
			default:
				ExtElement cElem = (ExtElement)child;
				IExtObject xChildObj = xf.createExtObject(cElem.getQName().getNamespaceURI(), cElem.getQName().getLocalPart());
				xObj.getItems().add(xChildObj);
				copyChildren(xChildObj, cElem);
				break;
			case PI:
				throw new RuntimeException("Support for PI is TODO");
			case TEXT:
				xObj.add(((ExtContent)child).getText(), false);
				break;
			}
		}
	}
	
	private void copyExtAttributes (IWithExtFields dest,
		ExtAttributes xAtts)
	{
		if ( xAtts == null ) return;
		for ( ExtAttribute xAtt : xAtts ) {
			dest.getExtFields().set(xAtt.getNamespaceURI(), xAtt.getLocalPart(), xAtt.getValue());
		}
		for ( String nsUri : xAtts.getNamespaces() ) {
			dest.getExtFields().setNS(nsUri, xAtts.getNamespacePrefix(nsUri));
		}
	}
	
	private void fillContent (IPart dstPart,
		Fragment oriFrag,
		boolean isTarget)
	{
		String ct = oriFrag.getCodedText();
		IContent dstCont;
		if ( isTarget ) dstCont = dstPart.getTarget(GetTarget.CREATE_EMPTY);
		else dstCont = dstPart.getSource();
		// Process the content
		for ( int i=0; i<ct.length(); i++ ) {
			if ( Fragment.isChar1(ct.charAt(i)) ) {
				int key = Fragment.toKey(ct.charAt(i), ct.charAt(++i));
				Tag oriTag = oriFrag.getTag(key);
				CTag oriCTag = (oriTag.isCode() ? (CTag)oriTag : null);
				ICTag dstCTag = null;
				IMTag dstCMrk = null;
				switch ( oriTag.getTagType() ) {
				case CLOSING:
					if ( oriTag.isCode() ) {
						dstCTag = dstCont.closeCodeSpan(oriTag.getId(), oriCTag.getData());
						copyCTag(oriCTag, dstCTag);
					}
					else {
						dstCMrk = dstCont.closeMarkerSpan(oriTag.getId());
					}
					break;
				case OPENING:
					if ( oriTag.isCode() ) {
						dstCTag = dstCont.openCodeSpan(oriTag.getId(), oriCTag.getData());
						copyCTag(oriCTag, dstCTag);
					}
					else {
						MTag am = (MTag)oriTag;
						dstCMrk = dstCont.openMarkerSpan(oriTag.getId(), am.getType());
						dstCMrk.setRef(am.getRef());
						dstCMrk.setValue(am.getValue());
						if ( am.getTranslate() != null ) {
							dstCMrk.setTranslate(am.getTranslate());
						}
						copyExtAttributes(dstCMrk, am.getExtAttributes());
					}
					break;
				case STANDALONE: // Always a code
					dstCTag = dstCont.appendCode(oriCTag.getId(), oriCTag.getData());
					copyCTag(oriCTag, dstCTag);
					break;
				}
			}
			else {
				dstCont.append(ct.charAt(i));
			}
		}
	}
	
	private void copyCTag (CTag oriCTag,
		ICTag dstCTag)
	{
		dstCTag.setCanCopy(oriCTag.getCanCopy());
		dstCTag.setCanDelete(oriCTag.getCanDelete());
		dstCTag.setCanOverlap(oriCTag.getCanOverlap());
		dstCTag.setCanReorder(convCanReorder(oriCTag.getCanReorder()));
		dstCTag.setCopyOf(oriCTag.getCopyOf());
		dstCTag.setDataDir(convDir(oriCTag.getDataDir()));
		dstCTag.setDir(convDir(oriCTag.getDir()));
		dstCTag.setDisp(oriCTag.getDisp());
		dstCTag.setEquiv(oriCTag.getEquiv());
		dstCTag.setSubFlows(oriCTag.getSubFlows());
		dstCTag.setSubType(oriCTag.getSubType());
		dstCTag.setType(oriCTag.getType());
	}
	
	private org.oasisopen.xliff.om.v1.CanReorder convCanReorder (CanReorder canReorder) {
		switch ( canReorder ) {
		case FIRSTNO:
			return org.oasisopen.xliff.om.v1.CanReorder.FIRSTNO;
		case NO:
			return org.oasisopen.xliff.om.v1.CanReorder.NO;
		case YES:
		default:
			return org.oasisopen.xliff.om.v1.CanReorder.YES;
		}
	}
	
	private Direction convDir (Directionality dir) {
		switch ( dir ) {
		case AUTO:
			return Direction.AUTO;
		case INHERITED:
			return Direction.INHERITED;
		case RTL:
			return Direction.RTL;
		case LTR:
		default:
			return Direction.LTR;
		}
	}

}
