package net.sf.okapi.acorn.taas;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

public class Credentials {

	private String username;
	private String password;
	private String userkey;
	
	public Credentials () {
		username = "OkapiFramework";
		password = "F45$DeFJ@3dSD";
		userkey = "0753d625-b774-480d-a257-4a25d91f25d5";
	}
	
	public String getBasic () {
		String base64 = null;
		try {
			base64 = DatatypeConverter.printBase64Binary((username+":"+password).getBytes("ISO-8859-1")); //"UTF-8"));
		}
		catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
		return "Basic "+base64;
	}
	
	public String getUserKey () {
		return userkey;
	}

}
