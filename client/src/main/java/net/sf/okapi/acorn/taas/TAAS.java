package net.sf.okapi.acorn.taas;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import org.apache.commons.httpclient.params.HttpClientParams;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TAAS extends DefaultEventHandler {

	private static final String BASEURL = "https://api.taas-project.eu";

	private JSONParser parser;
	private HttpClient client;
	private Credentials credentials;
	
	public TAAS () {
		client = new HttpClient();
        client.getParams().setParameter("http.useragent", "Okapi-Acorn");
        parser = new JSONParser();
        credentials = new Credentials();
	}
	
	@Override
	public Event handleUnit (Event event) {
//		for ( Segment segment : event.getUnit().getSegments() ) {
//			process(segment);
//		}
		
		post("This is an example of bookmark.");
		return event;
	}
	
	private void post (String codedText) {
		try {
	        StringBuilder r = new StringBuilder();
	        r.append(BASEURL).append("/extraction/");
	        r.append("?sourceLang=").append("en");
	        r.append("&targetLang=").append("fi");
	        r.append("&method=").append("2"); //4
	//        if (!StringUtil.isEmpty(domain)) {
	//            r.append("&domain=").append(URLEncoder.encode(domain, "UTF-8"));
	//        }
	        HttpURLConnection conn;
	        conn = (HttpURLConnection) new URL(r.toString()).openConnection();
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
	        } finally {
	            in.close();
	        }	        
	        System.out.println(res);
		}
		catch ( Throwable e ) {
			e.printStackTrace();
		}
	}

}
