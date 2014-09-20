package net.sf.okapi.acorn.calais;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.okapi.acorn.xom.Factory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

public class OpenCalais {

	private class Occurrence implements Annotator.IInfoSpan, Comparable<Occurrence> {
		
		int start;
		int end;
		IMTag info;
		String name;

		public Occurrence (int offset,
			int length,
			String type,
			String name)
		{
			this.start = offset;
			this.end = offset+length;
			this.name = name;
			info = Factory.XOM.createOpeningMTag("notused", type);
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
		public IMTag getInfo () {
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
	
	public void process (IUnit unit) {
		for ( ISegment segment : unit.getSegments() ) {
			process(segment);
		}
	}
	
	private void process (ISegment segment) {
		IContent content = segment.getSource();
		if ( content.isEmpty() ) return;
		
		PostMethod method = new PostMethod(BASEURL);
		method.setRequestHeader("x-calais-licenseID", "qp4vk8cbb4xjmjde6hzpuxdq");
        method.setRequestHeader("Content-Type", "text/raw; charset=UTF-8");
        method.setRequestHeader("Accept", "application/json");
        method.setRequestHeader("enableMetadataType", "SocialTags,GenericRelations");
        
        // Do the call
        String body = content.getPlainText();
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
        annotate(content, res);
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
	private void annotate (IContent content,
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
			ann.annotates(content, occs);
			
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
