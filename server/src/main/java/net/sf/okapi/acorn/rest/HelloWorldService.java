package net.sf.okapi.acorn.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;

@Path("/hello")
public class HelloWorldService {
 
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg, @HeaderParam("my-header") String myHeader) {
 		
		String output = "Jersey say : " + msg;
 
		System.out.println("my-header is : " + myHeader);		
		
		return Response.status(200).entity(output).build();
 
	}
	
	@POST
	@Consumes("multipart/mixed")
	public Response postForm(MultiPart multiPart) {
		
		for (BodyPart bp : multiPart.getBodyParts()) {
			String bpStr = bp.getEntityAs(String.class);
			System.out.println(bpStr);			
		}
		
		return Response.status(200).entity("Processed multipart").build();
	}
}