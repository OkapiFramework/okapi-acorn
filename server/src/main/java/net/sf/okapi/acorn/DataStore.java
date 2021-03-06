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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import com.mycorp.tmlib.SimpleTM;

public class DataStore implements Iterable<TransRequest> {

	private static TimeZone timeZone = TimeZone.getTimeZone("UTC");
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
	private static long errCounter = 0;
	
	private SimpleTM tm;
	private Map<String, TransRequest> requests;
	
	private DataStore () {
		dateFormat.setTimeZone(timeZone);
		requests = new HashMap<>();
		tm = new SimpleTM();
		// Load the default data
		URL url = getClass().getResource("data.tmx");
		try {
			//File inputFile = new File("C:\\Dev\\OkapiAcorn\\dev\\trunk\\server\\src\\main\\resources\\net\\sf\\okapi\\acorn\\data.tmx"); //url.toURI());
			File inputFile = new File(url.toURI());
			int count = tm.importSegments(inputFile);
			for ( int i=0; i<50; i++ ) System.out.println("");
			System.out.println("Okapi-Acorn Server for TAUS Translation API");
			System.out.println("Entries imported = "+count+"\n");
		}
		catch ( URISyntaxException e ) {
			e.printStackTrace();
		}
	}
	
	private static class Holder {
		private static final DataStore INSTANCE = new DataStore();
	}
	
	public static DataStore getInstance () {
		return Holder.INSTANCE;
	}

	public static String formatDate (Date date) {
		return dateFormat.format(date);
	}
	
	public static String getNextErrorId () {
		return ""+(++errCounter);
	}

	/**
	 * Escapes a string to JSON.
	 * A null parameter returns a null.
	 * @param text the text to escape.
	 * @return the escaped text.
	 */
	public static String esc (String text) {
		if ( text == null ) return null;
		return text.replaceAll("(\\\"|\\\\|/)", "\\$1");
	}
	
	/**
	 * Gets the JSON quoted string value or null of a string.
	 * @param text the text to quote
	 * @return the quoted and escaped text or null.
	 */
	public static String quote (String text) {
		if ( text == null ) return null;
		return "\""+text.replaceAll("(\\\"|\\\\|/)", "\\$1")+"\"";
	}

	public TransRequest get (String id) {
		return requests.get(id);
	}

	public TransRequest remove (String id) {
		return requests.remove(id);
	}

	public void add (TransRequest treq) {
		if ( !requests.containsKey(treq.getId()) ) {
			requests.put(treq.getId(), treq);
		}
	}
	
	@Override
	public Iterator<TransRequest> iterator () {
		return requests.values().iterator();
	}

	public SimpleTM getTM () {
		return tm;
	}

}
