package net.sf.okapi.acorn.xom.json;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.okapi.acorn.xom.Const;

import org.json.simple.JSONArray;
import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.TagType;

public class JSONWriter {
	
	public Map<String, Object> fromPart (IPart part) {
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("isseg", part.isSegment());
		map.put("id", part.getId());
		map.put("pws", part.getPreserveWS());
		map.put("src", fromContent(part.getSource()));
		if ( part.hasTarget() ) {
			map.put("trg", fromContent(part.getTarget()));
		}
		if ( part.isSegment() ) {
			ISegment seg = (ISegment)part;
			map.put("state", seg.getState().toString());
			if ( seg.getSubState() != null ){
				map.put("substate", seg.getSubState());
			}
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray fromContent (IContent content) {
		JSONArray array = new JSONArray();
		for ( Object obj : content ) {
			if ( obj instanceof String ) {
				array.add((String)obj);
			}
			else if ( obj instanceof ITag ) {
				array.add(fromTag((ITag)obj, content.getTags()));
			}
			else {
				throw new RuntimeException("Unexpected part in content: "+obj.getClass().getName());
			}
		}
		return array;
	}
	
	private Map<String, Object> fromTag (ITag tag,
		ITags tags)
	{
		String kind = null;
		ICTag start = null;
		ICTag end = null;
		switch ( tag.getTagType() ) {
		case CLOSING:
			kind = "" + (tag.isCode() ? Const.CODE_CLOSING : Const.MARKER_CLOSING);
			if ( tag.isCode() ) {
				end = (ICTag)tag;
				start = (ICTag)tags.getOpeningTag(end.getId());
			}
			break;
		case OPENING:
			kind = "" + (tag.isCode() ? Const.CODE_OPENING : Const.MARKER_OPENING);
			if ( tag.isCode() ) {
				start = (ICTag)tag;
				end = (ICTag)tags.getClosingTag(start.getId());
			}
			break;
		case STANDALONE:
			kind = "" + Const.CODE_STANDALONE;
			start = (ICTag)tag;
			break;
		}
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("kind", kind);
		map.put("id", tag.getId());
		map.put("type", tag.getType());
		if ( tag.isCode() ) { // Original code
			ICTag code = (ICTag)tag;
			if ( code.getTagType() == TagType.CLOSING ) {
				if ( start == null ) {
					// If there is no start code: use the end code to output the common info
					if ( code.getSubType() != null ) map.put("subt", code.getSubType());
					if ( !code.getCanCopy() ) map.put("canc", code.getCanCopy());
					if ( !code.getCanDelete() ) map.put("cand", code.getCanDelete());
					if ( code.getCanOverlap() ) map.put("cano", code.getCanOverlap());
					if ( code.getCanReorder() != CanReorder.YES ) map.put("canr", code.getCanReorder().toString());
					if ( code.getCopyOf() != null ) map.put("copy", code.getCopyOf());
					if ( code.getDir() != Direction.INHERITED ) map.put("dir", code.getDir());
				}
			}
			else { // opening or placeholder code
				if ( code.getSubType() != null ) map.put("subt", code.getSubType());
				if ( !code.getCanCopy() ) map.put("canc", code.getCanCopy());
				if ( !code.getCanDelete() ) map.put("cand", code.getCanDelete());
				if ( code.getCanOverlap() ) map.put("cano", code.getCanOverlap());
				if ( code.getCanReorder() != CanReorder.YES ) map.put("canr", code.getCanReorder().toString());
				if ( code.getCopyOf() != null ) map.put("copy", code.getCopyOf());
				if ( code.getTagType() == TagType.OPENING ) {
					// Not for standalone codes
					if ( code.getDir() != Direction.INHERITED ) map.put("dir", code.getDir());
				}
			}
			// Fields with values specific to all types of code
			if ( code.getDisp() != null ) map.put("disp", code.getDisp());
			if ( !code.getEquiv().isEmpty() ) map.put("equi", code.getEquiv());
			if ( code.getSubFlows() != null ) map.put("subf", code.getSubFlows());
			if ( code.getData() != null ) map.put("data", code.getData());
			if ( code.getDataDir() != Direction.AUTO ) map.put("datd", code.getDataDir().toString());
		}
		else { // Annotation
			if ( tag.getTagType() == TagType.OPENING ) {
				IMTag am = (IMTag)tag;
				if ( am.getRef() != null ) map.put("ref", am.getRef());
				if ( am.getValue() != null ) map.put("val", am.getValue());
				map.put("tran", am.getTranslate());
			}
			// Nothing for closing annotation
		}

		return map;
	}
	
}
