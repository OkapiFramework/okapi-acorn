package net.sf.okapi.acorn.calais;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.okapi.lib.xliff2.core.Fragment;
import net.sf.okapi.lib.xliff2.core.MTag;
import net.sf.okapi.lib.xliff2.core.Segment;
import net.sf.okapi.lib.xliff2.processor.DefaultEventHandler;
import net.sf.okapi.lib.xliff2.reader.Event;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OpenCalais extends DefaultEventHandler {

	private class Occurrence implements Annotator.IInfoSpan, Comparable<Occurrence> {
		
		int start;
		int end;
		MTag info;
		String type;
		String name;

		public Occurrence (int offset,
			int length,
			String type,
			String name)
		{
			this.start = offset;
			this.end = offset+length;
			this.type = type;
			this.name = name;
			info = new MTag("notused", type);
			info.setValue(name);
		}
		
		// CompareTo implementation to sort by position
		@Override
		public int compareTo (Occurrence other) {
			return Integer.compare(start, other.start);
		}

		@Override
		public int getStart () {
			return start;
		}

		@Override
		public int getEnd () {
			return end;
		}

		@Override
		public MTag getInfo () {
			return info;
		}

	}
	
	private static final String BASEURL = "http://api.opencalais.com/tag/rs/enrich";

	private JSONParser parser;
	private HttpClient client;
	
	public OpenCalais () {
		client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Okapi-Acorn");
        parser = new JSONParser();
	}
	
	@Override
	public Event handleUnit (Event event) {
		for ( Segment segment : event.getUnit().getSegments() ) {
			process(segment);
		}
		return event;
	}
	
	private void process (Segment segment) {
		PostMethod method = new PostMethod(BASEURL);
		method.setRequestHeader("x-calais-licenseID", "qp4vk8cbb4xjmjde6hzpuxdq");
        method.setRequestHeader("Content-Type", "text/raw; charset=UTF-8");
        method.setRequestHeader("Accept", "application/json");
        method.setRequestHeader("enableMetadataType", "SocialTags,GenericRelations");
        
        // Do the call
        Fragment frag = segment.getSource();
        String body = frag.getPlainText(); //.getCodedText();
        try {
			method.setRequestEntity(new StringRequestEntity(body, "text/raw", "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        String res = doRequest(method);
        
        // Process the results
        System.out.println(res);
        annotate(frag, res);
        System.out.println(frag.toXLIFF());
	}

	private String doRequest (PostMethod method) {
		try {
			int returnCode = client.executeMethod(method);
			if ( returnCode == HttpStatus.SC_OK ) {
				return method.getResponseBodyAsString();
			}
			else {
				System.err.println("Post failed:");
				System.err.println("Code: " + returnCode);
				System.err.println("response: " + method.getResponseBodyAsString());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			method.releaseConnection();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void annotate (Fragment fragment,
		String result)
	{
		try {
			Object o1 = parser.parse(result);
			ArrayList<Annotator.IInfoSpan> occs = new ArrayList<>();
			
			if ( o1 instanceof HashMap ) {
				HashMap<String, Object> map = (HashMap<String, Object>)o1;
				for ( String key : map.keySet() ) {
					if ( !key.equals("doc") ) {
						HashMap<String, Object> m1 = (HashMap<String, Object>)map.get(key);
						String typeGroup = (String)m1.get("_typeGroup");
						if ( !typeGroup.equals("entities")) continue;
						String name = (String)m1.get("name");
						String type = (String)m1.get("_type");
						JSONArray a1 = (JSONArray)m1.get("instances");
						for ( Object o3 : a1 ) {
							HashMap<String, Object> m2 = (HashMap<String, Object>)o3;
							int offset = ((int)(long)m2.get("offset"));
							int length = (int)(long)m2.get("length");
							occs.add(new Occurrence(offset, length, "oc:entity/"+type, name));
						}
					}
				}
			}

			Annotator ann = new Annotator();
			ann.annotates(fragment, occs);
			
//			if ( occs.isEmpty() ) return;
//
//			// Sort the occurrences
//			Collections.sort(occs);
//			Collections.reverse(occs);
//			String oriCt = fragment.getCodedText();
//
//			// Annotate
//			int count1 = 0;
//			int count2 = 0;
//			int prevStart = 0;
//			int prevEnd = oriCt.length();
//			for ( Occurrence occ : occs ) {
//				int start = Fragment.getCodedTextPosition(oriCt, occ.start, true);
//				int end = Fragment.getCodedTextPosition(oriCt, occ.end, false);
//				if ( occ.start == prevStart ) {
//					count1++;
//					fragment.annotate(start+(count1*2), end+(count1*2), occ.type, occ.name, null);
//				}
//				else if ( occ.end >= prevEnd ) {
//					count2 += 2; // start and end tags
//					fragment.annotate(start, end+(count2*2), occ.type, occ.name, null);
//				}
//				else {
//					count1 = count2 = 0;
//					fragment.annotate(start, end, occ.type, occ.name, null);
//				}
//				prevStart = occ.start;
//				prevEnd = occ.end;
//			}
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
