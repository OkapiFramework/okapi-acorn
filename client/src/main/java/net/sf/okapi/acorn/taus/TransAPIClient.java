package net.sf.okapi.acorn.taus;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.okapi.acorn.xom.json.JSONWriter;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.oasisopen.xliff.om.v1.IContent;

public class TransAPIClient {

	private final static JSONWriter jw = new JSONWriter();
	
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
	{
		StringBuilder jtmp = new StringBuilder("{\"translationRequest\"{");
		jtmp.append("\"id\":\""+id+"\",");
		jtmp.append("\"sourceLanguage\":\""+sourceLang+"\",");
		jtmp.append("\"targetLanguage\":\""+targetLang+"\",");
		jtmp.append("\"source\":"+quote(source.getPlainText())+",");
		// XLIFF content
		jtmp.append("\"xlfSource\":"+jw.fromContent(source).toJSONString());
		// End of payload
		jtmp.append("}}");
		WebTarget target = clientMP.target(baseURL).path("translation");
		MultiPart multiPartEntity = new MultiPart(MediaType.MULTIPART_FORM_DATA_TYPE).bodyPart(
			new BodyPart(jtmp.toString(), MediaType.APPLICATION_JSON_TYPE));
		response = target.request().post(Entity.entity(multiPartEntity, multiPartEntity.getMediaType()));
		return response.getStatus();
	}
	
	public int translation_id_put (String id,
		String sourceLang,
		String targetLang,
		IContent source,
		IContent target)
	{
		StringBuilder jtmp = new StringBuilder("{\"translationRequest\"{");
		jtmp.append("\"id\":\""+id+"\",");
		jtmp.append("\"sourceLanguage\":\""+sourceLang+"\",");
		jtmp.append("\"targetLanguage\":\""+targetLang+"\",");
		// XLIFF content
		if ( source != null ) {
			jtmp.append("\"source\":"+quote(source.getPlainText())+",");
			jtmp.append("\"xlfSource\":"+jw.fromContent(source).toJSONString());
		}
		if ( target != null ) {
			if ( source != null ) jtmp.append(",");
			jtmp.append("\"target\":"+quote(target.getPlainText())+",");
			jtmp.append("\"xlfTarget\":"+jw.fromContent(target).toJSONString());
		}
		// End of payload
		jtmp.append("}}");
		WebTarget wt = clientMP.target(baseURL).path("translation/"+id);
		MultiPart multiPartEntity = new MultiPart(MediaType.MULTIPART_FORM_DATA_TYPE).bodyPart(
			new BodyPart(jtmp.toString(), MediaType.APPLICATION_JSON_TYPE));
		response = wt.request().put(Entity.entity(multiPartEntity, multiPartEntity.getMediaType())); 
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

}
