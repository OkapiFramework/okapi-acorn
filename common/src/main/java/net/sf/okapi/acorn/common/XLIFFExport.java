package net.sf.okapi.acorn.common;

import java.io.File;

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
import net.sf.okapi.lib.xliff2.core.StartGroupData;
import net.sf.okapi.lib.xliff2.core.Tag;
import net.sf.okapi.lib.xliff2.core.Unit;
import net.sf.okapi.lib.xliff2.writer.XLIFFWriter;

import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IGroupOrUnit;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.IWithExtFields;
import org.oasisopen.xliff.om.v1.IWithExtObjects;

public class XLIFFExport {

	private XLIFFWriter writer;
	
	public void exportDocument (IDocument doc,
		File outputFile)
	{
		try {
			writer = new XLIFFWriter();
			writer.create(outputFile, doc.getSourceLanguage(), doc.getTargetLanguage());
			for ( IFile file : doc ) {
				for ( IGroupOrUnit gou : file ) {
					if ( gou.isUnit() ) writeUnit((IUnit)gou);
					else writeGroup((IGroup)gou);
				}
			}
		}
		catch ( Throwable e ) {
			if ( writer != null ) writer.close();
		}
	}
	
	private void writeUnit (IUnit unit) {
		Unit xUnit = new Unit(unit.getId());
		//xUnit.setName(unit.getName());
		//xUnit.setSourceDir(unit.getSourceDir());
		//xUnit.setType(unit.getType());
		
	}
	
	private void writeGroup (IGroup group) {
		StartGroupData sgd = new StartGroupData(group.getId());
		//sgd.setType(group.);
		sgd.setName(group.getName());
		//sgd.setType(group.getType());
		writer.writeStartGroup(sgd);
		for ( IGroupOrUnit gou : group ) {
			if ( gou.isUnit() ) writeUnit((IUnit)gou);
			else writeGroup((IGroup)gou);
		}
		writer.writeEndGroup();
	}
	
	private IUnit fromXLIFFUnit (Unit unit) {
		IUnit mUnit = Factory.XOM.createUnit(unit.getId());
		mUnit.setCanResegment(unit.getCanResegment());
		mUnit.setTranslate(unit.getTranslate());
		for ( Part part : unit ) {
			IPart mPart;
			if ( part.isSegment() ) mPart = Factory.XOM.createPart(mUnit.getStore());
			else mPart = Factory.XOM.createSegment(mUnit.getStore());
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
		for ( IExtChild child : xElem.getChildren() ) {
			switch ( child.getType() ) {
			case CDATA:
				xObj.add(((ExtContent)child).getText(), true);
				break;
			case ELEMENT:
			default:
				ExtElement cElem = (ExtElement)child;
				IExtObject xChildObj = Factory.XOM.createExtObject(cElem.getQName().getNamespaceURI(),
					cElem.getQName().getLocalPart());
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
				Tag tag = frag.getTag(key);
				CTag ctag = (tag.isCode() ? (CTag)tag : null);
				ICTag code = null;
				IMTag anno = null;
				switch ( tag.getTagType() ) {
				case CLOSING:
					if ( tag.isCode() ) {
						code = cont.closeCodeSpan(tag.getId(), ctag.getData());
						code.setCanCopy(ctag.getCanCopy());
						code.setCanDelete(ctag.getCanDelete());
						code.setCanOverlap(ctag.getCanOverlap());
						code.setCanReorder(convCanReorder(ctag.getCanReorder()));
						code.setCopyOf(ctag.getCopyOf());
						code.setDataDir(convDir(ctag.getDataDir()));
						code.setDir(convDir(ctag.getDir()));
						code.setDisp(ctag.getDisp());
						code.setEquiv(ctag.getEquiv());
						code.setSubFlows(ctag.getSubFlows());
						code.setSubType(ctag.getSubType());
						code.setType(ctag.getType());
					}
					else {
						anno = cont.closeMarkerSpan(tag.getId());
					}
					break;
				case OPENING:
					if ( tag.isCode() ) {
						code = cont.openCodeSpan(tag.getId(), ctag.getData());
						code.setCanCopy(ctag.getCanCopy());
						code.setCanDelete(ctag.getCanDelete());
						code.setCanOverlap(ctag.getCanOverlap());
						code.setCanReorder(convCanReorder(ctag.getCanReorder()));
						code.setCopyOf(ctag.getCopyOf());
						code.setDataDir(convDir(ctag.getDataDir()));
						code.setDir(convDir(ctag.getDir()));
						code.setDisp(ctag.getDisp());
						code.setEquiv(ctag.getEquiv());
						code.setSubFlows(ctag.getSubFlows());
						code.setSubType(ctag.getSubType());
						code.setType(ctag.getType());
					}
					else {
						MTag am = (MTag)tag;
						anno = cont.openMarkerSpan(tag.getId(), am.getType());
						anno.setRef(am.getRef());
						anno.setValue(am.getValue());
						anno.setTranslate(am.getTranslate().equals("yes"));
						copyExtAttributes(anno, am.getExtAttributes());
					}
					break;
				case STANDALONE: // Always a code
					code = cont.appendCode(ctag.getId(), ctag.getData());
					code.setCanCopy(ctag.getCanCopy());
					code.setCanDelete(ctag.getCanDelete());
					code.setCanOverlap(ctag.getCanOverlap());
					code.setCanReorder(convCanReorder(ctag.getCanReorder()));
					code.setCopyOf(ctag.getCopyOf());
					code.setDataDir(convDir(ctag.getDataDir()));
					code.setDir(convDir(ctag.getDir()));
					code.setDisp(ctag.getDisp());
					code.setEquiv(ctag.getEquiv());
					code.setSubFlows(ctag.getSubFlows());
					code.setSubType(ctag.getSubType());
					code.setType(ctag.getType());
					break;
				}
			}
			else {
				cont.append(ct.charAt(i));
			}
		}
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
