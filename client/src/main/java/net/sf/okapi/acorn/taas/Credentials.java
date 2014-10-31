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

package net.sf.okapi.acorn.taas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

public class Credentials {

	private String username;
	private String password;
	private String userkey;
	
	public Credentials ()
		throws IOException
	{
		Properties prop = new Properties();
		prop.load(getClass().getResourceAsStream("taas.properties"));
		username = prop.getProperty("username");
		password = prop.getProperty("password");
		userkey = prop.getProperty("userkey");
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
