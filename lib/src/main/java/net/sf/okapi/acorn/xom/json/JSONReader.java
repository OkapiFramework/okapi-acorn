/*===========================================================================
  Copyright (C) 2014 by the Okapi Framework contributors
-----------------------------------------------------------------------------
  This library is free software; you can redistribute it and/or modify it 
  under the terms of the GNU Lesser General Public License as published by 
  the Free Software Foundation; either version 2.1 of the License, or (at 
  your option) any later version.

  This library is distributed in the hope that it will be useful, but 
  WITHOUT ANY WARRANTY; without even the implied warranty of 
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser 
  General Public License for more details.

  You should have received a copy of the GNU Lesser General Public License 
  along with this library; if not, write to the Free Software Foundation, 
  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

  See also the full LGPL text here: http://www.gnu.org/copyleft/lesser.html
===========================================================================*/

package net.sf.okapi.acorn.xom.json;

import java.util.Map;

import net.sf.okapi.acorn.xom.Const;
import net.sf.okapi.acorn.xom.Content;
import net.sf.okapi.acorn.xom.Part;
import net.sf.okapi.acorn.xom.Segment;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.Direction;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.IPart;
import org.oasisopen.xliff.om.v1.IStore;

public class JSONReader {

	JSONParser parser;
	
	public JSONReader () {
		parser = new JSONParser();
	}
	
	public IPart readPart (IStore store,
		String text)
	{
		IPart part = null;
		try {
			Object res = parser.parse(text);
			if ( !(res instanceof JSONObject) ) {
				throw new RuntimeException("Unexpected object found while reading.");
			}
			JSONObject obj  = (JSONObject)res;
			boolean isSeg = (boolean)obj.get("isseg");
			if ( isSeg ) part = new Segment(store);
			else part = new Part(store);
			part.setId((String)obj.get("id"));
			part.setPreserveWS((boolean)obj.get("pws"));
			convertToContent((JSONArray)obj.get("src"), part.getSource());
			if ( obj.containsKey("trg") ) {
				part.createTarget();
				convertToContent((JSONArray)obj.get("trg"), part.getTarget());
			}
		}
		catch ( Throwable e ) {
			throw new RuntimeException("JSON parsing error.\n"+e.getLocalizedMessage());
		}
		return part;
	}
	
	public IContent readContent (IStore store,
		boolean isTarget,
		String text)
	{
		IContent cont = new Content(store, isTarget);
		try {
			Object res = parser.parse(text);
			if ( !(res instanceof JSONArray) ) {
				throw new RuntimeException("Unexpected JSON object found while reading.");
			}
			convertToContent((JSONArray)res, cont);
		}
		catch ( Throwable e ) {
			throw new RuntimeException("JSON parsing error.\n"+e.getLocalizedMessage());
		}
		return cont;
	}
	
	public IContent readContent (IStore store,
		boolean isTarget,
		JSONArray array)
	{
		IContent cont = new Content(store, isTarget);
		convertToContent(array, cont);
		return cont;
	}
	
	
	private void convertToContent (JSONArray array,
		IContent dest)
	{
		for ( Object obj : array ) {
			if ( obj instanceof String ) {
				dest.append((String)obj);
			}
			else if ( obj instanceof Map ) {
				JSONObject o = (JSONObject)obj;
				String id = (String)o.get("id");
				String data = (String)o.get("data");
				String type = (String)o.get("type");
				ICTag ctag = null;
				IMTag mtag = null;
				// Now get the tag-type and tag-class to instantiate the tag
				String tmp = (String)o.get("kind");
				switch ( tmp.charAt(0) ) {
				case Const.CODE_OPENING:
					ctag = dest.openCodeSpan(id, data);
					break;
				case Const.CODE_CLOSING:
					ctag = dest.closeCodeSpan(id, data);
					break;
				case Const.CODE_STANDALONE:
					ctag = dest.appendCode(id, data);
					break;
				case Const.MARKER_OPENING:
					mtag = dest.openMarkerSpan(id, type);
					break;
				case Const.MARKER_CLOSING:
					mtag = dest.closeMarkerSpan(id);
					break;
				default:
					throw new RuntimeException("Unexpected 'kind' value: "+tmp);
				}
				
				if ( ctag != null ) {
					ctag.setType(type);
					ctag.setSubType((String)o.get("subt"));
					if ( o.containsKey("canc") ) ctag.setCanCopy((boolean)o.get("canc"));
					if ( o.containsKey("cand") ) ctag.setCanDelete((boolean)o.get("cand"));
					if ( o.containsKey("cano") ) ctag.setCanOverlap((boolean)o.get("cano"));
					tmp = (String)o.get("canr");
					if ( tmp != null ) ctag.setCanReorder(CanReorder.valueOf(tmp));
					ctag.setCopyOf((String)o.get("copy"));
					tmp = (String)o.get("dir");
					if ( tmp != null ) ctag.setDir(Direction.valueOf(tmp));
					tmp = (String)o.get("disp");
					if ( tmp != null ) ctag.setDisp(tmp);
					tmp = (String)o.get("equi");
					if ( tmp != null ) ctag.setEquiv(tmp);
				}
				else if ( mtag != null ) {
					mtag.setType(type);
					tmp = (String)o.get("val");
					if ( tmp != null ) mtag.setValue(tmp);
					tmp = (String)o.get("ref");
					if ( tmp != null ) mtag.setRef(tmp);
					if ( o.containsKey("tran") ) mtag.setTranslate((boolean)o.get("tran"));
				}
			}
		}
	}

}
