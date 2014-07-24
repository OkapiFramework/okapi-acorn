package net.sf.okapi.acorn.rest;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/hello")
public class HelloWorldService {
 
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg, @HeaderParam("my-header") String myHeader) {
 		
		String output = "Jersey say : " + msg;
 
		System.out.println("my-header is : " + myHeader);		
		
		return Response.status(200).entity(output).build();
 
	}
}