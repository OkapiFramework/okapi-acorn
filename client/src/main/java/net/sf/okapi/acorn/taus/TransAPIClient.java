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

package net.sf.okapi.acorn.taus;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.acorn.xom.json.JSONReader;
import net.sf.okapi.acorn.xom.json.JSONWriter;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ISegment;

public class TransAPIClient {

	private final JSONParser parser = new JSONParser();
	private final static JSONWriter JW = new JSONWriter();
	private final static JSONReader JR = new JSONReader();
	
	private String baseURL;
	private Client clientSP;
	private Client clientMP;
	private Response response;

	/**
	 * Gets the JSON quoted string value or null of a string.
	 * @param text the text to quote
	 * @return the quoted and escaped text or null.
	 */
	public static String quote (String text) {
		if ( text == null ) return null;
		return "\""+text.replaceAll("(\\\"|\\\\|/)", "\\$1")+"\"";
	}

	public TransAPIClient (String baseURL) {
		this.baseURL = baseURL;
		clientSP = ClientBuilder.newClient();
		clientMP = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
	}
	
	public void setBaseURL (String baseURL) {
		this.baseURL = baseURL;
	}

	public Response getResponse () {
		return response;
	}
	
	public String getResponseString () {
		return response.readEntity(String.class);
	}
	
	public int status_id_get (String id) {
		WebTarget target = clientSP.target(baseURL).path("status/"+id);
		response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		return response.getStatus();
	}

	public int translation_get () {
		WebTarget target = clientSP.target(baseURL).path("translation");
		response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		return response.getStatus();
	}
	
	public int translation_post (String id,
		String sourceLang,
		String targetLang,
		IContent source)
	{ //contentType 
		StringBuilder tmp = new StringBuilder("{\"translationRequest\"{");
		tmp.append("\"id\":\""+id+"\",");
		tmp.append("\"sourceLanguage\":\""+sourceLang+"\",");
		tmp.append("\"targetLanguage\":\""+targetLang+"\",");
		// Content
		tmp.append("\"source\":"+quote(source.getPlainText())+",");
		tmp.append("\"xlfSource\":"+JW.fromContent(source).toJSONString());
		// End of payload
		tmp.append("}}");
		
		WebTarget target = clientMP.target(baseURL).path("translation");
		MultiPart multiPartEntity = null;
		try {
			multiPartEntity = new MultiPart(MediaType.MULTIPART_FORM_DATA_TYPE).bodyPart(
				new BodyPart(tmp.toString(), MediaType.APPLICATION_JSON_TYPE));
			response = target.request().post(Entity.entity(multiPartEntity, multiPartEntity.getMediaType()));
		}
		finally {
			if ( multiPartEntity != null ) {
				try {
					multiPartEntity.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return response.getStatus();
	}
	
	public int translation_id_put (String id,
		String sourceLang,
		String targetLang,
		IContent source,
		IContent target)
	{
		StringBuilder tmp = new StringBuilder("{\"translationRequest\"{");
		tmp.append("\"id\":\""+id+"\",");
		tmp.append("\"sourceLanguage\":\""+sourceLang+"\",");
		tmp.append("\"targetLanguage\":\""+targetLang+"\",");
		// Content
		if ( source != null ) {
			tmp.append("\"source\":"+quote(source.getPlainText())+",");
			tmp.append("\"xlfSource\":"+JW.fromContent(source).toJSONString());
		}
		if ( target != null ) {
			if ( source != null ) tmp.append(",");
			tmp.append("\"target\":"+quote(target.getPlainText())+",");
			tmp.append("\"xlfTarget\":"+JW.fromContent(target).toJSONString());
		}
		// End of payload
		tmp.append("}}");
		WebTarget wt = clientMP.target(baseURL).path("translation/"+id);
		MultiPart multiPartEntity = null;
		try {
			multiPartEntity = new MultiPart(MediaType.MULTIPART_FORM_DATA_TYPE).bodyPart(
				new BodyPart(tmp.toString(), MediaType.APPLICATION_JSON_TYPE));
			response = wt.request().put(Entity.entity(multiPartEntity, multiPartEntity.getMediaType()));
		}
		finally {
			if ( multiPartEntity != null ) {
				try {
					multiPartEntity.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return response.getStatus();
	}
	
	public int translation_id_get (String id) {
		WebTarget target = clientSP.target(baseURL).path("translation/"+id);
		response = target.request(MediaType.APPLICATION_JSON_TYPE).get();
		return response.getStatus();
	}

	public int translation_id_delete (String id) {
		WebTarget target = clientSP.target(baseURL).path("translation/"+id);
		response = target.request(MediaType.APPLICATION_JSON_TYPE).delete();
		return response.getStatus();
	}

	public int confirm_id_put (String id) {
		WebTarget target = clientSP.target(baseURL).path("confirm/"+id);
		response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.text(""));
		return response.getStatus();
	}

	public int accept_id_put (String id) {
		WebTarget target = clientSP.target(baseURL).path("accept/"+id);
		response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.text(""));
		return response.getStatus();
	}

	public int cancel_id_put (String id) {
		WebTarget target = clientSP.target(baseURL).path("cancel/"+id);
		response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.text(""));
		return response.getStatus();
	}

	public int reject_id_put (String id) {
		WebTarget target = clientSP.target(baseURL).path("reject/"+id);
		response = target.request(MediaType.APPLICATION_JSON_TYPE).put(Entity.text(""));
		return response.getStatus();
	}

	public IContent getTargetContent (String jsonText) {
		ISegment seg = Factory.XOM.createLoneSegment();
		try {
			JSONObject o1 = (JSONObject)parser.parse(jsonText);
			JSONObject o2 = (JSONObject)o1.get("translationRequest");
			if ( o2.containsKey("xlfTarget") ) {
				seg.setTarget(JR.readContent(seg.getStore(), true, (JSONArray)o2.get("xlfTarget")));
			}
			else {
				String t = (String)o2.get("target");
				if (( t != null ) && !t.isEmpty() ) seg.setTarget(t);
			}
		}
		catch ( ParseException e ) {
			e.printStackTrace();
		}
		return seg.getTarget();
	}

}
