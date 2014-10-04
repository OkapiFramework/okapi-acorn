package net.sf.okapi.acorn.calais;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import net.sf.okapi.acorn.client.XLIFFDocumentTask;
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

public class OpenCalais extends XLIFFDocumentTask {

	private class Occurrence implements Annotator.IInfoSpan, Comparable<Occurrence> {
		
		private int start;
		private int end;
		private IMTag info;
		private String name;

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
	
	@Override
	protected void process (ISegment segment) {
    	super.process(segment);
		IContent content = segment.getSource();
		if ( content.isEmpty() ) return;
		String body = content.getCodedText(); //.getPlainText();
		if ( body.isEmpty() ) return;
		
		PostMethod method = new PostMethod(BASEURL);
		method.setRequestHeader("x-calais-licenseID", "qp4vk8cbb4xjmjde6hzpuxdq");
        method.setRequestHeader("Content-Type", "text/raw; charset=UTF-8");
        method.setRequestHeader("Accept", "application/json");
        method.setRequestHeader("enableMetadataType", "SocialTags,GenericRelations");
        
        // Do the call
        try {
			method.setRequestEntity(new StringRequestEntity(body, "text/raw", "UTF-8"));
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
        String res = doRequest(method);
        
        // Process the results
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

    @Override
	public String getInfo () {
		return "<html><header><style>"
			+ "body{font-size: large;} code{font-size: large;}"
			+ "</style></header><body>"
			+ "<p>The <b>Open-Calais Web Service</b> from Thomson Reuters is used to find entities and mark them "
			+ "using the ITS Text Analysis annotation.</p>"
			+ "</body></html>";
	}

    @Override
	public String getInfoLink () {
		return "http://www.opencalais.com/documentation/calais-web-service-api";
	};
    
}
