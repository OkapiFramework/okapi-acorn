package net.sf.okapi.acorn.dbpedia;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sf.okapi.acorn.client.XLIFFDocumentTask;
import net.sf.okapi.acorn.common.Util;
import net.sf.okapi.acorn.xom.Factory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class DBpediaSpotlight extends XLIFFDocumentTask {

	private static final String ITS20_URI = "http://www.w3.org/2005/11/its";
	private static final String DBP_URI = "myDBpediaNS";
	private static final String GLS_URI = Util.NS_XLIFF20_GLOSSARY;
	
	private String dbpslBaseURL = "http://spotlight.sztaki.hu:2222/rest";
	private String wikidataBaseURL = "http://www.wikidata.org/entity/";
	
	private JSONParser parser;
	private String defaultTargetLang;
	private String trgLang;
	private HashMap<String, Resource> unitEntries;
	private int unitTransCount;

	private class TransRes {
		String trans;
		String origin;
		// Constructor with values
		TransRes (String trans,
			String original)
		{
			this.trans = trans;
			this.origin = original;
		}
	}
	
	private class Resource {
		String name;
		String uri;
		String types;
		String qvalue;
		List<TransRes> trans;
		Double score;
	}
	
	private class Occurrence implements Comparable<Occurrence> {
		String uriAndName;
		int start;
		// CompareTo implementation to sort by position
		@Override
		public int compareTo (Occurrence other) {
			return Integer.compare(start, other.start);
		}
	}
	
	/**
	 * Creates a new DBpediaSpotlight object.
	 * @param defaultTargetLang the code of the target language to use if none
	 * is already specified in the document.
	 */
	public DBpediaSpotlight (String defaultTargetLang) {
		this.parser = new JSONParser();
		this.defaultTargetLang = defaultTargetLang;
	}
	
	@Override
	public void process (IDocument document) {
		trgLang = document.getTargetLanguage();
		if ( trgLang == null ) {
			// Use the default target if none is set
			trgLang = defaultTargetLang;
			document.setTargetLanguage(trgLang);
		}
		// Keep only the language part
		int pos = trgLang.indexOf('-');
		if ( pos > -1 ) trgLang = trgLang.substring(0, pos);

		// Add namespace declaration on the file elements to avoid having them all over
		for ( IFile file : document ) {
			file.getExtFields().setNS(ITS20_URI, "its");
			file.getExtFields().setNS(DBP_URI, "dbp");
		}
		
		// Then process
		super.process(document);
	}

	@Override
	public void process (IUnit unit) {
		unitEntries = new HashMap<>();
		unitTransCount = 0;
		for ( ISegment segment : unit.getSegments() ) {
			process(segment);
		}
		// Add the glossary entry if needed
		if ( unitTransCount > 0 ) {
			addGlossaryEntries(unit);
		}
	}
	
	@Override
	protected void process (ISegment segment) {
    	super.process(segment);
		try {
			IContent content = segment.getSource();
			String text = content.getCodedText();
			if ( content.isEmpty() ) return;
			List<Occurrence> list = new ArrayList<>();
			HashMap<String, Resource> newEntries = new HashMap<>();
			// Get the candidates parse back the result
			JSONObject o1 = (JSONObject)parser.parse(getCandidates(text));
			JSONObject o2 = (JSONObject)o1.get("annotation");
			// Gather the results in a list so it can be sorted
			// surfaceForm can be return as a single object or an array
			Object sfObj = o2.get("surfaceForm");
			if ( sfObj != null ) {
				if ( sfObj instanceof JSONArray ) {
					for ( Object obj : (JSONArray)sfObj ) {
						processSurfaceForm(obj, list, newEntries);
					}
				}
				else {
					processSurfaceForm(sfObj, list, newEntries);
				}
			}

			// Try to get the Wikidata q-values for the new Resource objects
			int count = 0;
			for ( Resource res : newEntries.values() ) {
				// Try to get the Q-value
				String qvalue = getWikiDataQValue(res.uri);
				if ( qvalue != null ) {
					res.qvalue = qvalue;
					// Try to get a translation from Wikidata (from the q-value)
					count = getTranslationFromWikiData(res);
					unitTransCount += count;
				}
				unitTransCount += count;
			}

			// Add the new entries to the unit-level entries
			unitEntries.putAll(newEntries);
			
			// Sort the list by offset
			Collections.sort(list);

			// Annotate the fragment using the ITS Text Analysis data category
			int add = 0;
			for ( Occurrence occurrence : list ) {
				Resource res = unitEntries.get(occurrence.uriAndName);
				int before = content.getCodedText().length();
				IMTag am = content.getOrCreateMTag(occurrence.start+add,
					occurrence.start+add+res.name.length(), null, "its:any");
				add += (content.getCodedText().length()-before);
				am.getExtFields().set(ITS20_URI, "taIdentRef", "http://dbpedia.org/resource/"+res.uri);
				if ( !Util.isNoE(res.types) ) {
					am.getExtFields().set(DBP_URI, "types", res.types);
				}
//				am.getExtFields().set(DBP_URI, "score", res.score.toString());
			}

		}
		catch ( Throwable e ) {
			throw new RuntimeException("Error while querying service.", e);
		}
	}

	private void processSurfaceForm (Object surfaceFormItem,
		List<Occurrence> list,
		HashMap<String, Resource> newEntries)
	{
		Occurrence occurrence = new Occurrence();
		list.add(occurrence);
		JSONObject o3 = (JSONObject)surfaceFormItem;
		occurrence.start = Integer.parseInt(o3.get("@offset").toString());
		JSONObject o4 = (JSONObject)o3.get("resource");
		String uri = (String)o4.get("@uri");
		String name = (String)o3.get("@name");
		// Use URI+name to store same URI found based on different spans of content
		occurrence.uriAndName = uri+"_"+name;
//		System.out.println("New Occurence: name="+name+" start="+occurrence.start);
		// Do we have it yet?
		if ( unitEntries.containsKey(occurrence.uriAndName)
			|| newEntries.containsKey(occurrence.uriAndName) ) {
			// We already have the data, move to the next one
//			System.out.println(" Resource exists already");
			return;
		}
		// Else: create a new resource
		Resource res = new Resource();
		res.uri = uri;
		res.name = name;
		res.types = (String)o4.get("@types");
		res.score = Double.parseDouble(o4.get("@finalScore").toString());
		newEntries.put(occurrence.uriAndName, res);
//		System.out.println(" New Resource: uri="+res.uri);
	}
	
	private void addGlossaryEntries (IUnit unit) {
		// Do we have translations?
		boolean done = true;
		for ( String uri : unitEntries.keySet() ) {
			Resource res = unitEntries.get(uri);
			if (( res.trans != null ) && !res.trans.isEmpty() ) {
				done = false;
				break;
			}
		}
		if ( done ) return;
		
		// Get or create the glossary element
		IExtObjects eos = unit.getExtObjects();
		IExtObject eo = eos.getOrCreate(GLS_URI, "glossary");
		// Add entries to the glossary
		for ( String uri : unitEntries.keySet() ) {
			Resource res = unitEntries.get(uri);
			if (( res.trans != null ) && !res.trans.isEmpty() ) {
				// Add the glossEntry element
				IExtObject entry = Factory.XOM.createExtObject(GLS_URI, "glossEntry");
				eo.getItems().add(entry);
				IExtObject term = Factory.XOM.createExtObject(GLS_URI, "term");
				entry.getItems().add(term);
				term.add(res.name, false);
				// Add the translation elements
				for ( TransRes tra : res.trans ) {
					IExtObject trans = Factory.XOM.createExtObject(GLS_URI, "translation");
					entry.getItems().add(trans);
					trans.add(tra.trans, false);
					trans.getExtFields().set("source", tra.origin);
				}
			}
		}
	}
	
	private String getCandidates (String text)
    	throws IOException
    {
    	URL url = new URL(dbpslBaseURL+"/candidates");
    	HttpURLConnection handle = (HttpURLConnection)url.openConnection();
    	handle.setDoOutput(true);
    	handle.setRequestProperty("Accept", "application/json");

        StringBuilder data = new StringBuilder();
        data.append("text="+URLEncoder.encode(text, "UTF-8"));
        handle.addRequestProperty("Content-Length", Integer.toString(data.length()));

        DataOutputStream ostream = new DataOutputStream(handle.getOutputStream());
        ostream.write(data.toString().getBytes());
        ostream.close();
        return doRequest(handle);
    }
    
    private int getTranslationFromWikiData (Resource resource)
    	throws IOException, ParseException
    {
    	if ( resource.qvalue == null ) return 0;
    	URL url = new URL("http://www.wikidata.org/w/api.php?action=wbgetentities&ids="+resource.qvalue+"&format=json");
    	HttpURLConnection handle = (HttpURLConnection)url.openConnection();
    	handle.setDoOutput(true);
		handle.setRequestMethod("GET");
		String json = doRequest(handle);
		JSONObject o1 = (JSONObject)parser.parse(json);
		JSONObject o2 = (JSONObject)((JSONObject)((JSONObject)o1.get("entities")).get(resource.qvalue)).get("labels");
		JSONObject o3 = (JSONObject)o2.get(trgLang);
		if ( o3 == null ) return 0; // No translation for this language
		// One translation available: add it
		if ( resource.trans == null ) {
			resource.trans = new ArrayList<>();
		}
		resource.trans.add(new TransRes(
			o3.get("value").toString(),
			wikidataBaseURL+resource.qvalue));
		return 1;
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
	
	public void getTranslations (String str)
		throws UnsupportedEncodingException
	{
		String ustr = URLEncoder.encode(str, "UTF-8");
		String qt = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
			+ "SELECT ?o\n"
			+ "WHERE {\n"
			+ "   <http://dbpedia.org/resource/"+ustr+"> owl:sameAs ?o.\n"
			+ "  FILTER regex(?o,'http://"+trgLang+".dbpedia')\n"
			+ "}";
		Query query = QueryFactory.create(qt);
        QueryExecution qExe = QueryExecutionFactory.sparqlService( "http://dbpedia.org/sparql", query);
        ResultSet results = qExe.execSelect();
        while ( results.hasNext() ) {
        	QuerySolution qs = results.next();
        	String res = qs.get("?o").asResource().toString();
        	int pos = res.lastIndexOf('/');
        	res = res.substring(pos+1);
        }
	}

	/**
	 * Try to get the Wikidata Q-value for a given DBpedia URI
	 * @param uri the DBpedia URI (ending part) 
	 * @return the Q-value or null.
	 */
	public String getWikiDataQValue (String uri)
			throws UnsupportedEncodingException
	{
		String ustr = URLEncoder.encode(uri, "UTF-8");
		String qt = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
			+ "SELECT ?o\n"
			+ "WHERE {\n"
			+ "   <http://dbpedia.org/resource/"+ustr+"> owl:sameAs ?o.\n"
			+ "  FILTER regex(?o,'http://wikidata.org/entity')\n"
			+ "}";
		Query query = QueryFactory.create(qt);
        QueryExecution qExe = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", query);
        ResultSet results = qExe.execSelect();
        if ( results.hasNext() ) {
        	QuerySolution qs = results.next();
        	String res = qs.get("?o").asResource().toString();
        	int pos = res.lastIndexOf('/');
        	return res.substring(pos+1);
        }
        return null;
	}

    @Override
	public String getInfo () {
		return "<html><header><style>"
			+ "body{font-size: large;} code{font-size: large;}"
			+ "</style></header><body>"
			+ "<p>This function uses several Web service to find entities in the source text and try to get "
			+ "the corresponding translations. The process is the following:</p>"
			+ "<ul><li>The <b>DBpedia Splotlight Web service</b> is used to extract entities. Each entity comes with a URI.</li>"
			+ "<li>For each entity, we use a <b>SPARQL query</b> to cross-reference on <b>DBpedia</b> the URI with <b>Wikidata</b> information. This may give us a Q-Value (a unique identifier for Wikidata).</li>"
			+ "<li>If there is a Q-Value, we query it on Wikidata to try to get translations in the target language.</li></ul>"
			+ "<p>Found entities are annotated with their types, and any translation found is added in the unit using the Glossary module.</p>"
			+ "</body></html>";
	}

    @Override
	public String getInfoLink () {
		return "https://github.com/dbpedia-spotlight/dbpedia-spotlight/wiki";
	};
    
}
