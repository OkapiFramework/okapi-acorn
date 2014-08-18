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

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

@Path("translation")
public class Translation {
	
	private @Context ServletContext context;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response translation_POST (FormDataMultiPart form
//    	@FormDataParam("translationRequest") List<FormDataBodyPart> tr
//    	@FormDataParam("id") String id,
//    	@FormDataParam("sourceLanguage") String srcLang,
//    	@FormDataParam("targetLanguage") String trgLang,
//    	@FormDataParam("source") String source
		)
	{
		int i=0;
		for ( BodyPart bp : form.getBodyParts() ) {
			String bpStr = bp.getEntityAs(String.class);
			System.out.println(bpStr);
			if ( i == 0 ) {
				
			}
			i++;
		}
//		FormDataBodyPart tr = form.getField("translationRequest");
//		System.out.println(tr.getValue());
//		if (( id == null ) || id.trim().isEmpty() ) {
//			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID must not be null or empty.");
//		}
//		if ( DataStore.getInstance().get(id) != null ) {
//			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "ID exists already.");
//		}
//		if (( srcLang == null ) || srcLang.isEmpty() ) {
//			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "Invalid source language.");
//		}
//		if (( trgLang == null ) || trgLang.isEmpty() ) {
//			return ErrorResponse.create(Response.Status.BAD_REQUEST, id, "Invalid target language.");
//		}

		String id="123";
		String source = "text";
		
		TransRequest treq = new TransRequest(id);
		treq.setSource(source);
		DataStore.getInstance().add(treq);
		
		ResponseBuilder rb = Response.ok(treq.toJSON(), MediaType.APPLICATION_JSON);
		return rb.status(Response.Status.CREATED).build();
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
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id,
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
			return ErrorResponse.create(Response.Status.BAD_REQUEST, id,
				String.format("ID '%s' does not exists.", id));
		}

		// Delete
		DataStore.getInstance().remove(id);
		return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response translation_id_PUT (
    	@PathParam("id") String id,
    	@QueryParam("sourceLanguage") String srcLang,
    	@QueryParam("targetLanguage") String trgLang,
    	@QueryParam("source") String source
    )
    {
    	return ErrorResponse.create(Response.Status.NOT_IMPLEMENTED, id, "This method is not implemented.");
//		TransRequest treq = DataStore.getInstance().get(id);
//		if ( treq == null ) {
//			return ErrorResponse.create(Response.Status.BAD_REQUEST, id,
//				String.format("ID '%s' does not exists.", id));
//		}
//
//		return Response.ok("Found", MediaType.TEXT_PLAIN).build();
    }

}
