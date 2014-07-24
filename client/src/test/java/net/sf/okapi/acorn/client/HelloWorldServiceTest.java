package net.sf.okapi.acorn.client;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.Test;

public class HelloWorldServiceTest {

	public static String restURL = "http://localhost:8080/taustapi/v2";
	
	@Test
	public void testHelloWorldService(){

		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(restURL).path("hello/nisse");
		Invocation.Builder builder = target.request();
		builder.header("my-header", "MyHeaderValue");
		Response response = builder.get();
		
		
		if (response.getStatus() != 200) {
		   throw new RuntimeException("Failed : HTTP error code : "
			+ response.getStatus());
		}
 
		String output = response.readEntity(String.class);
 
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		
		response.close();
	}
	
	@Test
	public void testMultiPart() throws IOException{
		
	    MultiPart multiPart = new MultiPart().
	  	      bodyPart(new BodyPart("Body Part 1", MediaType.TEXT_PLAIN_TYPE)).
	  	      bodyPart(new BodyPart("Body Part 2", MediaType.TEXT_PLAIN_TYPE));
		
		Client client = ClientBuilder.newClient().register(MultiPartFeature.class);
		WebTarget target = client.target(restURL).path("hello");
		
		System.out.println(multiPart.getMediaType());
		
		Response response = target.request().post(Entity.entity(multiPart, multiPart.getMediaType()));
		
		if (response.getStatus() != 200) {
			   throw new RuntimeException("Failed : HTTP error code : "
				+ response.getStatus());
		}
		
		String output = response.readEntity(String.class);
		 
		System.out.println("Output from Server .... \n");
		System.out.println(output);
		
		multiPart.close();
		response.close();
	}
}
