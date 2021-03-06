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

package net.sf.okapi.acorn;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import net.sf.okapi.acorn.xom.Factory;
import net.sf.okapi.acorn.xom.json.JSONReader;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.oasisopen.xliff.om.v1.ISegment;

import com.mycorp.tmlib.Entry;

@Path("translation")
public class Translation {
	
	private @Context ServletContext context;
	private final JSONParser parser = new JSONParser();
	private final JSONReader jr = new JSONReader();

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response translation_POST (FormDataMultiPart form) {
		String id = null;
		String source = null;
		String srcLang = null;
		String trgLang = null;
		ISegment seg = null;
		
		int i=0;
		for ( BodyPart bp : form.getBodyParts() ) {
			String bpStr = bp.getEntityAs(String.class);
			if ( i == 0 ) {
				try {
					JSONObject o1 = (JSONObject)parser.parse(bpStr);
					JSONObject o2 = (JSONObject)o1.get("translationRequest");
					id = (String)o2.get("id");
					source = (String)o2.get("source");
					srcLang = (String)o2.get("sourceLanguage");
					trgLang = (String)o2.get("targetLanguage");
					if ( o2.containsKey("xlfSource") ) {
						seg = Factory.XOM.createLoneSegment();
						JSONArray src = (JSONArray)o2.get("xlfSource");
						System.out.println("-- xlfSource for translation POST:");
						System.out.println(src.toJSONString());
						seg.setSource(jr.readContent(seg.getStore(), false, src));
					}
				}
				catch ( ParseException e ) {
					return ErrorResponse.create(Response.Status.BAD_REQUEST, "unknown", "JSON parsing error:\n"+e.getMessage());
				}
			}
			i++;
		}
		if (( id == null ) || id.trim().isEmpty() ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID must not be null or empty.");
		}
		if ( DataStore.getInstance().get(id) != null ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID exists already.");
		}
		if (( srcLang == null ) || srcLang.isEmpty() ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "Invalid source language.");
		}
		if (( trgLang == null ) || trgLang.isEmpty() ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "Invalid target language.");
		}

		TransRequest treq = new TransRequest(id);
		treq.setSourceLang(srcLang);
		treq.setTargetLang(trgLang);
		if ( seg != null ) {
			treq.setSegment(seg);
		}
		else if ( source != null ) {
			treq.setSource(source);
		}
		DataStore.getInstance().add(treq);
		
		ResponseBuilder rb = Response.ok(treq.toJSON(), MediaType.APPLICATION_JSON);
		Response response = rb.status(Response.Status.CREATED).build();

		// Trigger the translation
		triggerTranslation(treq);
		
		return response;
	}
	
    @PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/{id}")
    public Response translation_id_PUT (FormDataMultiPart form) {
		String id = null;
		String source = null;
		String target = null;
		String srcLang = null;
		String trgLang = null;
		boolean hasData = false;

		int i=0;
		for ( BodyPart bp : form.getBodyParts() ) {
			String bpStr = bp.getEntityAs(String.class);
			if ( i == 0 ) {
				try {
					JSONObject o1 = (JSONObject)parser.parse(bpStr);
					JSONObject o2 = (JSONObject)o1.get("translationRequest");
					id = (String)o2.get("id");
					if ( o2.containsKey("sourceLanguage") ) {
						srcLang = (String)o2.get("sourceLanguage");
						hasData = true;
					}
					if ( o2.containsKey("targetLanguage") ) {
						trgLang = (String)o2.get("targetLanguage");
						hasData = true;
					}
					if ( o2.containsKey("source") ) {
						source = (String)o2.get("source");
						hasData = true;
					}
					if ( o2.containsKey("target") ) {
						target = (String)o2.get("target");
						hasData = true;
					}
				}
				catch ( ParseException e ) {
					return ErrorResponse.create(Response.Status.BAD_REQUEST, "unknown", "JSON parsing error:\n"+e.getMessage());
				}
			}
			i++;
		}

		if (( id == null ) || id.trim().isEmpty() ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID must not be null or empty.");
		}
		TransRequest treq = DataStore.getInstance().get(id);
		if ( treq == null ) {
			return ErrorResponse.create(Response.Status.NOT_FOUND, id,
				String.format("ID '%s' does not exists.", id));
		}
		// Update the entry
		if ( srcLang != null ) {
			if ( !srcLang.equals(treq.getSourceLang()) ) {
				treq.setSourceLang(srcLang);
				treq.setStatus(TransRequest.STATUS_INITIAL); 
			}
		}
		if ( trgLang != null ) {
			if ( !trgLang.equals(treq.getTargetLang()) ) {
				treq.setTargetLang(trgLang);
				treq.setStatus(TransRequest.STATUS_INITIAL); 
			}
		}
		if ( source != null ) {
			if ( !source.equals(treq.getSource()) ) {
				treq.setSource(source);
				treq.setStatus(TransRequest.STATUS_INITIAL);
			}
		}
		if ( target != null ) {
			treq.setTarget(target);
		}
		if ( hasData ) treq.stamp();
		
		ResponseBuilder rb = Response.ok(treq.toJSON(), MediaType.APPLICATION_JSON);
		return rb.status(Response.Status.OK).build();
    }

	/**
	 * Gets the list of the existing translation requests. 
	 * @param srcLang the source language (null for all).
	 * @param trgLang the target language (null for all).
	 * @return the response.
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response translation_GET (
    	@QueryParam("sourceLanguage") String srcLang,
    	@QueryParam("targetLanguage") String trgLang
		)
	{
		StringBuilder tmp = new StringBuilder("[");
		for ( TransRequest treq : DataStore.getInstance() ) {
			// Filter out requests not matching the query
			if (( srcLang != null ) && !srcLang.equalsIgnoreCase(treq.getSourceLang()) ) continue; 
			if (( trgLang != null ) && !trgLang.equalsIgnoreCase(treq.getTargetLang()) ) continue; 
			// add to the list
			tmp.append((tmp.length()>1 ? "," : "") + "\""+treq.getId()+"\"");
		}
		tmp.append("]");
		return Response.ok(tmp.toString(), MediaType.APPLICATION_JSON).build();
	}
	
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response translation_id_GET (
    	@PathParam("id") String id
    )
    {
		if (( id == null ) || id.trim().isEmpty() ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID must not be null or empty.");
		}
		TransRequest treq = DataStore.getInstance().get(id);
		if ( treq == null ) {
			return ErrorResponse.create(Response.Status.NOT_FOUND, id,
				String.format("ID '%s' does not exists.", id));
		}

		return Response.ok(treq.toJSON(), MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response translation_id_DELETE (
    	@PathParam("id") String id
    )
    {
		if (( id == null ) || id.trim().isEmpty() ) {
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID must not be null or empty.");
		}
		TransRequest treq = DataStore.getInstance().get(id);
		if ( treq == null ) {
			return ErrorResponse.create(Response.Status.NOT_FOUND, id,
				String.format("ID '%s' does not exists.", id));
		}

		// Delete
		DataStore.getInstance().remove(id);
		return Response.status(Response.Status.NO_CONTENT).build();
    }

    private void triggerTranslation (TransRequest treq) {
    	ISegment seg = treq.getSegment();
    	Entry res = DataStore.getInstance().getTM().search(seg.getSource());
    	if ( res == null ) {
    		return;
    	}
    	// Else: set the translation and update the status
    	seg.copyTarget(res.getTarget());
    	treq.setStatus(TransRequest.STATUS_CONFIRMED);
    	treq.stamp();
    }
    
}
