package net.sf.okapi.acorn.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

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
}
