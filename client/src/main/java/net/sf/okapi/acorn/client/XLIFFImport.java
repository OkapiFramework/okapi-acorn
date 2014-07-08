package net.sf.okapi.acorn.client;

import java.io.File;
import java.util.List;

import net.sf.okapi.acorn.xom.ExtObject;
import net.sf.okapi.acorn.xom.Group;
import net.sf.okapi.lib.xliff2.core.AMarker;
import net.sf.okapi.lib.xliff2.core.BaseMarker;
import net.sf.okapi.lib.xliff2.core.CMarker;
import net.sf.okapi.lib.xliff2.core.Directionality;
import net.sf.okapi.lib.xliff2.core.ExtAttribute;
import net.sf.okapi.lib.xliff2.core.ExtAttributes;
import net.sf.okapi.lib.xliff2.core.ExtContent;
import net.sf.okapi.lib.xliff2.core.ExtElement;
import net.sf.okapi.lib.xliff2.core.ExtElements;
import net.sf.okapi.lib.xliff2.core.Fragment;
import net.sf.okapi.lib.xliff2.core.IExtChild;
import net.sf.okapi.lib.xliff2.core.IWithExtElements;
import net.sf.okapi.lib.xliff2.core.Part;
import net.sf.okapi.lib.xliff2.core.StartFileData;
import net.sf.okapi.lib.xliff2.core.StartGroupData;
import net.sf.okapi.lib.xliff2.core.StartXliffData;
import net.sf.okapi.lib.xliff2.core.Unit;
import net.sf.okapi.lib.xliff2.reader.Event;
import net.sf.okapi.lib.xliff2.reader.XLIFFReader;

import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.IAnnotation;
import org.oasisopen.xliff.om.v1.ICode;
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
			if ( Fragment.isMarker(ct.charAt(i)) ) {
				int key = Fragment.toKey(ct.charAt(i), ct.charAt(++i));
				BaseMarker bm = frag.getMarker(key);
				CMarker cm = (bm.isCode() ? (CMarker)bm : null);
				ICode code = null;
				IAnnotation anno = null;
				switch ( bm.getTagType() ) {
				case CLOSING:
					if ( bm.isCode() ) {
						code = cont.closeCode(bm.getId(), cm.getOriginalData());
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
					else {
						anno = cont.closeAnnotation(bm.getId());
					}
					break;
				case OPENING:
					if ( bm.isCode() ) {
						code = cont.appendOpeningCode(bm.getId(), cm.getOriginalData());
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
					else {
						AMarker am = (AMarker)bm;
						anno = cont.appendOpeningAnnotation(bm.getId(), am.getType());
						anno.setRef(am.getRef());
						anno.setValue(am.getValue());
						anno.setTranslate(am.getTranslate().equals("yes"));
						copyExtAttributes(anno, am.getExtAttributes());
					}
					break;
				case STANDALONE: // Always a code
					code = cont.appendStandaloneCode(cm.getId(), cm.getOriginalData());
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
					break;
				}
			}
			else {
				cont.append(ct.charAt(i));
			}
		}
	}
	
	private org.oasisopen.xliff.om.v1.CanReorder convCanReorder (int canReorder) {
		switch ( canReorder ) {
		case CMarker.CANREORDER_FIRSTNO:
			return CanReorder.FIRSTNO;
		case CMarker.CANREORDER_NO:
			return CanReorder.NO;
		case CMarker.CANREORDER_YES:
		default:
			return CanReorder.YES;
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
