package net.sf.okapi.acorn.yahoo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.okapi.acorn.common.BaseXLIFFProcessor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

public class YahooAnalyzer extends BaseXLIFFProcessor {

	private JSONParser parser;

    public YahooAnalyzer () {
		parser = new JSONParser();
    }
    
    @Override
    public void process (IUnit unit) {
    	// Add terms in each source segment
    	for ( ISegment segment : unit.getSegments() ) {
    		getTerms(segment);
    	}
    }

	private void getTerms (ISegment segment) {
		try {
			// Get the source content
			IContent fragment = segment.getSource();
			// Get the coded text for the content
			String text = fragment.getCodedText();
			if ( text.isEmpty() ) return;
			// Query Yahoo with the coded text: this gives an array of terms
			JSONObject o1 = (JSONObject)parser.parse(post(text));
			JSONObject o2 = (JSONObject)o1.get("query");
			JSONObject o3 = (JSONObject)o2.get("results");
			if ( o3 == null ) return; // No result
			JSONObject o4 = (JSONObject)o3.get("entities");
			Object object = o4.get("entity");
			if ( !(object instanceof JSONArray) ) {
				return;
			}
			JSONArray array = (JSONArray)o4.get("entity");
			// Markup the terms
			for ( Object obj : array ) {
				JSONObject o5 = (JSONObject)obj;
				String term = ((JSONObject)o5.get("text")).get("content").toString();
				// Find the term in the coded text
				// Note: This works only on the first occurrence!
				int start = fragment.getCodedText().indexOf(term);
				if ( start == -1 ) continue;
				// Annotate the coded text with the term
				fragment.annotate(start, start+term.length(), "term", null, (String)o5.get("wiki_url"));
			}
		}
		catch ( Throwable e ) {
			throw new RuntimeException("Error while querying service.", e);
		}
	}

    private String post (String text)
    	throws IOException
    {
    	String baseUrl = "http://query.yahooapis.com/v1/public/yql";
    	URL url = new URL(baseUrl);
    	HttpURLConnection handle = (HttpURLConnection)url.openConnection();
    	handle.setDoOutput(true);

        StringBuilder data = new StringBuilder();
    	String query = "select * from  contentanalysis.analyze where text='" + escape(text) + "'";
        data.append("q="+URLEncoder.encode(query, "UTF-8"));
        data.append("&format=json");
        handle.addRequestProperty("Content-Length", Integer.toString(data.length()));

        DataOutputStream ostream = new DataOutputStream(handle.getOutputStream());
        ostream.write(data.toString().getBytes());
        ostream.close();
        return doRequest(handle);
    }
    
	private String escape(String text) {
    	return text.replace("'", "\\'");
    }

	private String doRequest (HttpURLConnection handle)
		throws IOException
    {
		if ( handle.getResponseCode() == 200 ) {
			try ( BufferedReader in = new BufferedReader(
				new InputStreamReader(handle.getInputStream(), "UTF-8")) )
			{
				StringBuilder response = new StringBuilder();
				String inputLine;
				while ( (inputLine = in.readLine()) != null ) {
					response.append(inputLine);
				}
				return response.toString();
			}
		}
		// Else: error
		throw new RuntimeException("Error processing the response, code="+handle.getResponseCode());
    }

}
