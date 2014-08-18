package net.sf.okapi.acorn.xom.json;

import java.util.Map;

import net.sf.okapi.acorn.xom.Const;
import net.sf.okapi.acorn.xom.Content;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IStore;
import org.oasisopen.xliff.om.v1.ITag;

public class JSONReader {

	JSONParser parser;
	
	public JSONReader () {
		parser = new JSONParser();
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
			JSONArray array = (JSONArray)res;
			for ( Object obj : array ) {
				if ( obj instanceof String ) {
					cont.append((String)obj);
				}
				else if ( obj instanceof Map ) {
					JSONObject o = (JSONObject)obj;
					String id = (String)o.get("id");
					String data = (String)o.get("data");
					String type = (String)o.get("type");
					ITag tag;
					// Now get the tag-type and tag-class to instantiate the tag
					String tmp = (String)o.get("kind");
					switch ( tmp.charAt(0) ) {
					case Const.CODE_OPENING:
						tag = cont.startCodeSpan(id, data);
						break;
					case Const.CODE_CLOSING:
						tag = cont.closeCodeSpan(id, data);
						break;
					case Const.CODE_STANDALONE:
						tag = cont.appendCode(id, data);
						break;
					case Const.MARKER_OPENING:
						tag = cont.startMarkerSpan(id, type);
						break;
					case Const.MARKER_CLOSING:
						tag = cont.closeMarkerSpan(id);
						break;
					default:
						throw new RuntimeException("Unexpected 'kind' value: "+tmp);
					}
				}
			}
		}
		catch ( Throwable e ) {
			throw new RuntimeException("JSON parsing error.\n"+e.getLocalizedMessage());
		}
		return cont;
	}

}