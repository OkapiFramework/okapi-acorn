package net.sf.okapi.acorn.xom.json;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.okapi.acorn.xom.Const;
import net.sf.okapi.acorn.xom.Util;

import org.json.simple.JSONArray;
import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.IAnnotation;
import org.oasisopen.xliff.om.v1.ICode;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.ITags;
import org.oasisopen.xliff.om.v1.TagType;

public class JSONWriter {
	
	public JSONArray fromContent (IContent content) {
		JSONArray array = new JSONArray();
		String ct = content.getCodedText();
		int start = 0;
		for ( int i=0; i<ct.length(); i++ ) {
			char ch = ct.charAt(i);
			if ( Util.isChar1(ch) ) {
				// Output the preceding text if there is one
				if ( i-start > 0 ) {
					array.add(ct.substring(start, i));
				}
				// Get the tag
				ITag tag = content.getTags().get(Util.toKey(ch, ct.charAt(++i)));
				array.add(fromTag(tag, content.getTags()));
				// Set the start for the next text
				start = i+1;
			}
		}
		// Remaining text (if any)
		if ( ct.length()-start > 0 ) {
			array.add(ct.substring(start));
		}
		return array;
	}
	
	private Map<String, Object> fromTag (ITag tag,
		ITags tags)
	{
		JSONArray a1 = new JSONArray();
		String kind = null;
		ICode start = null;
		ICode end = null;
		switch ( tag.getTagType() ) {
		case CLOSING:
			kind = "" + (tag.isCode() ? Const.CLOSING_CODE : Const.CLOSING_ANNOTATION);
			end = (ICode)tag;
			start = (ICode)tags.getOpeningTag(end.getId());
			break;
		case OPENING:
			kind = "" + (tag.isCode() ? Const.OPENING_CODE : Const.OPENING_ANNOTATION);
			start = (ICode)tag;
			end = (ICode)tags.getClosingTag(start.getId());
			break;
		case STANDALONE:
			kind = "" + Const.STANDALONE_CODE;
			start = (ICode)tag;
			break;
		}
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("kind", kind);
		map.put("id", tag.getId());
		if ( tag.isCode() ) { // Original code
			ICode code = (ICode)tag;
			if ( code.getTagType() == TagType.CLOSING ) {
				if ( start == null ) {
					// If there is no start code: use the end code to output the common info
					if ( code.getType() != null ) map.put("type", code.getType());
					if ( code.getSubType() != null ) map.put("subt", code.getSubType());
					if ( !code.getCanCopy() ) map.put("canc", code.getCanCopy());
					if ( !code.getCanDelete() ) map.put("cand", code.getCanDelete());
					if ( code.getCanOverlap() ) map.put("cano", code.getCanOverlap());
					if ( code.getCanReorder() != CanReorder.YES ) map.put("canr", code.getCanReorder());
					if ( code.getCopyOf() != null ) map.put("copy", code.getCopyOf());
					if ( code.getDir() != Direction.INHERITED ) map.put("dir", code.getDir());
				}
			}
			else { // opening or placeholder code
				if ( code.getType() != null ) map.put("type", code.getType());
				if ( code.getSubType() != null ) map.put("subt", code.getSubType());
				if ( !code.getCanCopy() ) map.put("canc", code.getCanCopy());
				if ( !code.getCanDelete() ) map.put("cand", code.getCanDelete());
				if ( code.getCanOverlap() ) map.put("cano", code.getCanOverlap());
				if ( code.getCanReorder() != CanReorder.YES ) map.put("canr", code.getCanReorder());
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
				IAnnotation am = (IAnnotation)tag;
				if ( am.getRef() != null ) map.put("ref", am.getRef());
				if ( am.getValue() != null ) map.put("val", am.getValue());
				map.put("tran", am.getTranslate());
			}
			// Nothing for closing annotation
		}

		return map;
	}
	
}
