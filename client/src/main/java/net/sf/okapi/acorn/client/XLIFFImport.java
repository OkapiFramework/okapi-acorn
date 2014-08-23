package net.sf.okapi.acorn.client;

import java.io.File;
import java.util.List;

import net.sf.okapi.acorn.xom.ExtObject;
import net.sf.okapi.acorn.xom.Group;
import net.sf.okapi.lib.xliff2.core.Tag;
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
import net.sf.okapi.lib.xliff2.core.Unit;
import net.sf.okapi.lib.xliff2.reader.Event;
import net.sf.okapi.lib.xliff2.reader.XLIFFReader;

import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjectItem;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithExtFields;
import org.oasisopen.xliff.om.v1.IWithExtObjects;

public class XLIFFImport {

	public IDocument importDocument (File inputFile) {
		IDocument doc = null;
		IFile file = null;
		IGroup group = null;
		try ( XLIFFReader reader = new XLIFFReader() ) {
			reader.open(inputFile);
			while ( reader.hasNext() ) {
				Event event = reader.next();
				switch ( event.getType() ) {
				case START_DOCUMENT:
					doc = new net.sf.okapi.acorn.xom.Document();
					break;
				case START_XLIFF:
					StartXliffData sxd = event.getStartXliffData();
					doc.setSourceLanguage(sxd.getSourceLanguage());
					doc.setTargetLanguage(sxd.getTargetLanguage());
					//todo: ext attributes
					break;
				case START_FILE:
					StartFileData sfd = event.getStartFileData();
					file = doc.add(new net.sf.okapi.acorn.xom.File(sfd.getId()));
					//todo: ext attributes
					//copyExtAttributes(file, sfd);
					break;
				case START_GROUP:
					StartGroupData sgd = event.getStartGroupData();
					IGroup newGroup = new Group(group, sgd.getId());
					if ( group == null ) file.add(newGroup);
					else group.add(newGroup);
					group = newGroup;
					group.setName(sgd.getName());
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
	
	private IUnit fromXLIFFUnit (Unit unit) {
		IUnit mUnit = new net.sf.okapi.acorn.xom.Unit(unit.getId());
		mUnit.setCanResegment(unit.getCanResegment());
		mUnit.setTranslate(unit.getTranslate());
		mUnit.setName(unit.getName());
		mUnit.setType(unit.getType());
		mUnit.setSourceDir(convDir(unit.getSourceDir()));
		mUnit.setTargetDir(convDir(unit.getTargetDir()));
		//mUnit.setPreserveWS(unit.getPreserveWS());
		for ( Part part : unit ) {
			IPart mPart;
			if ( part.isSegment() ) mPart = new net.sf.okapi.acorn.xom.Part(mUnit.getStore());
			else mPart = new net.sf.okapi.acorn.xom.Segment(mUnit.getStore());
			// Source
			fillContent(mPart, part.getSource(), false);
			if ( part.hasTarget() ) fillContent(mPart, part.getTarget(), true);
		}
		copyExtAttributes(mUnit, unit.getExtAttributes());
		copyExtElements(mUnit, unit);
		return mUnit;
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
		List<IExtObjectItem> list = xObj.getItems();
		for ( IExtChild child : xElem.getChildren() ) {
			switch ( child.getType() ) {
			case CDATA:
				xObj.add(((ExtContent)child).getText(), true);
				break;
			case ELEMENT:
			default:
				ExtElement cElem = (ExtElement)child;
				IExtObject xChildObj = new ExtObject(cElem.getQName().getNamespaceURI(), cElem.getQName().getLocalPart());
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
	
	private void fillContent (IPart mPart,
		Fragment frag,
		boolean isTarget)
	{
		String ct = frag.getCodedText();
		IContent cont;
		if ( isTarget ) cont = mPart.getTarget();
		else cont = mPart.getSource();
		// Process the content
		for ( int i=0; i<ct.length(); i++ ) {
			if ( Fragment.isChar1(ct.charAt(i)) ) {
				int key = Fragment.toKey(ct.charAt(i), ct.charAt(++i));
				Tag bm = frag.getTag(key);
				CTag cm = (bm.isCode() ? (CTag)bm : null);
				ICTag code = null;
				IMTag anno = null;
				switch ( bm.getTagType() ) {
				case CLOSING:
					if ( bm.isCode() ) {
						code = cont.closeCodeSpan(bm.getId(), cm.getData());
						copyCMarker(cm, code);
					}
					else {
						anno = cont.closeMarkerSpan(bm.getId());
					}
					break;
				case OPENING:
					if ( bm.isCode() ) {
						code = cont.openCodeSpan(bm.getId(), cm.getData());
						copyCMarker(cm, code);
					}
					else {
						MTag am = (MTag)bm;
						anno = cont.openMarkerSpan(bm.getId(), am.getType());
						anno.setRef(am.getRef());
						anno.setValue(am.getValue());
						anno.setTranslate(am.getTranslate().equals("yes"));
						copyExtAttributes(anno, am.getExtAttributes());
					}
					break;
				case STANDALONE: // Always a code
					code = cont.appendCode(cm.getId(), cm.getData());
					copyCMarker(cm, code);
					break;
				}
			}
			else {
				cont.append(ct.charAt(i));
			}
		}
	}
	
	private void copyCMarker (CTag cm,
		ICTag code)
	{
		code.setCanCopy(cm.getCanCopy());
		code.setCanDelete(cm.getCanDelete());
		code.setCanOverlap(cm.getCanOverlap());
		code.setCanReorder(convCanReorder(cm.getCanReorder()));
		code.setCopyOf(cm.getCopyOf());
		code.setDataDir(convDir(cm.getDataDir()));
		code.setDir(convDir(cm.getDir()));
		code.setDisp(cm.getDisp());
		code.setEquiv(cm.getEquiv());
		code.setSubFlows(cm.getSubFlows());
		code.setSubType(cm.getSubType());
		code.setType(cm.getType());
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

//	private org.oasisopen.xliff.om.v1.ExtObjectType convExtElemType (ExtChildType type) {
//		switch ( type ) {
//		case CDATA:
//			return ExtObjectType.RAWTEXT;
//		case PI:
//			return ExtObjectType.INSTRUCTION;
//		case TEXT:
//			return ExtObjectType.TEXT;
//		case ELEMENT:
//		default:
//			return ExtObjectType.OBJECTS;
//		}
//	}
	
}
