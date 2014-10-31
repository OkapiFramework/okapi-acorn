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

package com.mycorp.tmlib;

import org.oasisopen.xliff.om.v1.IContent;

public class Entry {

	private String searchKey;
	private IContent source;
	private IContent target;
	
	static public String makeSearchKey (IContent source) {
		return makeSearchKey(source.getPlainText());
	}
	
	static public String makeSearchKey (String plainText) {
		StringBuilder tmp = new StringBuilder();
		boolean wasSpace = false;
		for ( int i=0; i<plainText.length(); i++ ) {
			char ch = plainText.charAt(i);
			switch ( Character.getType(ch) ) {
			case Character.START_PUNCTUATION:
			case Character.END_PUNCTUATION:
			case Character.CONNECTOR_PUNCTUATION:
			case Character.DASH_PUNCTUATION:
			case Character.INITIAL_QUOTE_PUNCTUATION:
			case Character.FINAL_QUOTE_PUNCTUATION:
			case Character.OTHER_PUNCTUATION:
			case Character.OTHER_SYMBOL:
			case Character.CURRENCY_SYMBOL:
				continue; // Skip
			case Character.CONTROL:
				if ( "\t\n\r\f\b".indexOf(ch) == -1 ) continue;
				// Else: treat it like a space
				ch = ' ';
				// Fall thru
			case Character.SPACE_SEPARATOR:
				if ( wasSpace ) continue; // Skip
				tmp.append(' ');
				wasSpace = true;
				break;
			default:
				tmp.append(ch);
				wasSpace = false;
				break;
			}
		}
		return tmp.toString().toLowerCase().trim();
	}
	
	public Entry (IContent src,
		IContent trg)
	{
		this.source = src;
		this.target = trg;
		searchKey = makeSearchKey(this.source);
	}
	
	public IContent getSource () {
		return source;
	}
	
	public IContent getTarget () {
		return target;
	}
	
	public String getSearchKey () {
		return searchKey;
	}
}
