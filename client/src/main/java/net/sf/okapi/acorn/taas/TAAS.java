package net.sf.okapi.acorn.taas;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.sf.okapi.acorn.client.XLIFFDocumentTask;
import net.sf.okapi.acorn.common.NSContext;
import net.sf.okapi.acorn.common.Util;
import net.sf.okapi.acorn.xom.Factory;

import org.apache.commons.httpclient.HttpClient;
import org.oasisopen.xliff.om.v1.ExtObjectItemType;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjectData;
import org.oasisopen.xliff.om.v1.IExtObjectItem;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class TAAS extends XLIFFDocumentTask {

	private static final String BASEURL = "https://api.taas-project.eu";
	private static final String GLS_URI = Util.NS_XLIFF20_GLOSSARY;

	private final DocumentBuilder docBuilder;
	//private final SSLSocketFactory sockFactory;
	private final String srcLang = "en";
	private final String trgLang = "fi"; // For demo
	private final String transacUser = "ysavourel";
	
	private HttpClient client;
	private Credentials credentials;
	private List<Entry> terms;

	private class Entry {
		
		String source;
		List<String> targets;

		public Entry () {
			targets = new ArrayList<>();
		}
	}

	/**
	 * Simple trust manager that does nothing.
	 * To by-pass invalid SSL certificate on web service end-point.
	 */
//	private class SimpleX509TrustManager implements X509TrustManager {
//		@Override
//		public void checkClientTrusted (X509Certificate[] cert, String s)
//			throws CertificateException {
//		}
//
//		@Override
//		public void checkServerTrusted (X509Certificate[] cert, String s)
//			throws CertificateException {
//		}
//
//		@Override
//		public X509Certificate[] getAcceptedIssuers () {
//			return null;
//		}
//	}	

	public TAAS ()
		throws ParserConfigurationException, NoSuchAlgorithmException, KeyManagementException
	{
		client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Okapi-Acorn");
        credentials = new Credentials();

        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		docBuilder = domFactory.newDocumentBuilder();

		// Using our own trust manager is needed temporarily to by-pass 
		// the invalid SSL certificate of the Web service end-point.
		// This should be removed as soon as the issue is fixed at the end-point
//	    SSLContext ssl = SSLContext.getInstance("TLSv1");
//		ssl.init(null, new TrustManager[]{new SimpleX509TrustManager()}, null);
//		sockFactory = ssl.getSocketFactory();
	}

	@Override
	public void process (IUnit unit) {
		terms = new ArrayList<>();
		try {
			for ( ISegment seg : unit.getSegments() ) {
				process(seg);
			}
			addGlossaryEntries(unit);
		}
		catch ( Throwable e ) {
			throw new RuntimeException("Error processing unit id="+unit.getId(), e);
		}
	}

	@Override
	protected void process (ISegment segment) {
    	super.process(segment);
		try {
			String codedText = segment.getSource().getCodedText();
			if ( codedText.isEmpty() ) return;
	        StringBuilder r = new StringBuilder();
	        r.append(BASEURL).append("/extraction/");
	        r.append("?sourceLang=").append(srcLang);
	        r.append("&targetLang=").append(trgLang); // Hard-coded for the demo
	        r.append("&method=").append("4"); //4
	        HttpsURLConnection conn;
	        conn = (HttpsURLConnection) new URL(r.toString()).openConnection();
	        //--- start fix: by-pass SSL bad certificate error
	        // conn.setSSLSocketFactory(sockFactory);
	        //--- end fix
	        conn.setRequestProperty("Authorization", credentials.getBasic());
	        String tuk = credentials.getUserKey();
	        if ( tuk != null ) {
	            conn.setRequestProperty("TaaS-User-Key", tuk);
	        }
	        conn.setRequestProperty("Accept", "text/xml");
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "text/plain");
	        conn.setDoOutput(true);
	        OutputStream out = conn.getOutputStream();
	        try {
	            out.write(codedText.getBytes(StandardCharsets.UTF_8));
	        }
	        finally {
	            out.close();
	        }
	
	        String res = null;
	        InputStream in = conn.getInputStream();
	        try {
	            ByteArrayOutputStream o = new ByteArrayOutputStream();
	            byte[] b = new byte[1024];
	            int readBytes;
	            while ((readBytes = in.read(b)) > 0) o.write(b, 0, readBytes);
	            res = new String(o.toByteArray(), StandardCharsets.UTF_8);
	        }
	        finally {
	            in.close();
	        }	        
	        parseTerms(res);
		}
		catch ( Throwable e ) {
			throw new RuntimeException(e);
		}
	}

	private void parseTerms (String res)
		throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
	{
		// Get the TBX chunk
		InputSource is = new InputSource(new StringReader(res));
		Document doc = docBuilder.parse(is);
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new NSContext());
		XPathExpression expr = xpath.compile("/extractionResult/terms");
		Node node = (Node)expr.evaluate(doc, XPathConstants.NODE);
		String data = node.getTextContent();
		
		is = new InputSource(new StringReader(data));
		doc = docBuilder.parse(is);

		// Get the term entries
		expr = xpath.compile("/martif/text/body/termEntry");
		NodeList termEntryList = (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
		
		for ( int i=0; i<termEntryList.getLength(); i++ ) {
			// Get the source
			expr = xpath.compile("langSet[@lang='"+srcLang+"' or @xml:lang='"+srcLang+"']/ntig/termGrp/term");
			String source = (String)expr.evaluate(termEntryList.item(i), XPathConstants.STRING);
			
			// Check if it exists already
			boolean exists = false;
			for ( Entry ent : terms ) {
				if ( ent.source.equals(source) ) {
					exists = true;
					break;
				}
			} // If it does exists do not store it twice.
			if ( exists ) continue;
			
			// Add the source
			Entry entry = new Entry();
			entry.source = source;
			
			// Get the target
			expr = xpath.compile("langSet[@lang='"+trgLang+"' or @xml:lang='"+trgLang+"']/"
				+"ntig[transacGrp/transacNote[@type='responsibility']='"+transacUser+"']/termGrp/term");
			NodeList list = (NodeList)expr.evaluate(termEntryList.item(i), XPathConstants.NODESET);
			for ( int j=0; j<list.getLength(); j++ ) {
				entry.targets.add(list.item(j).getTextContent());
//				System.out.println("trg: [["+entry.targets.get(j)+"]]");
			}
			
			if ( !entry.targets.isEmpty() ) {
				terms.add(entry);
			}
		}
	}

	private void addGlossaryEntries (IUnit unit) {
		// Do we have translations?
		if ( terms.isEmpty() ) return;

		// Get or create the glossary element
		IExtObjects eos = unit.getExtObjects();
		IExtObject eo = eos.getOrCreate(GLS_URI, "glossary");
		// Add entries to the glossary
		for ( Entry ent : terms ) {
			// Try to find if there is an entry already for this source term
			IExtObject entry = null;
			for ( IExtObjectItem item : eo.getItems() ) {
				if ( item.getType() == ExtObjectItemType.OBJECT ) {
					IExtObject obj = (IExtObject)item;
					if ( obj.getNSUri().equals(GLS_URI) ) {
						IExtObject t = (IExtObject)obj.getItems().get(0);
						IExtObjectData data = (IExtObjectData)t.getItems().get(0);
						if (( data != null ) && data.getContent().equalsIgnoreCase(ent.source) ) {
							entry = obj;
							break;
						}
					}
				}
			}
			// Create the glossEntry element if none was found
			if ( entry == null ) {
				entry = Factory.XOM.createExtObject(GLS_URI, "glossEntry");
				eo.getItems().add(entry);
				IExtObject term = Factory.XOM.createExtObject(GLS_URI, "term");
				entry.getItems().add(term);
				term.add(ent.source, false);
			}
			// Then, in all cases: add the translation elements
			for ( String tra : ent.targets ) {
				IExtObject trans = Factory.XOM.createExtObject(GLS_URI, "translation");
				entry.getItems().add(trans);
				trans.add(tra, false);
				trans.getExtFields().set("source", "TaaS Glossary");
			}
		}
	}
	
    @Override
	public String getInfo () {
		return "<html><header><style>"
			+ "body{font-size: large;} code{font-size: large;}"
			+ "</style></header><body>"
			+ "<p>The <b>TAAS Web Service</b> (Terminology as a Service) powered by Tilde, provides a way "
			+ "to discover in the source text the terms stored in common or private collections.</p>"
			+ "<p>The list of the found terms and their translation is added to each unit of the document "
			+ "using the <b>Glossary module</b>.</p>"
			+ "</body></html>";
	}

    @Override
	public String getInfoLink () {
		return "https://term.tilde.com/projects/4222";
	};
    
}
