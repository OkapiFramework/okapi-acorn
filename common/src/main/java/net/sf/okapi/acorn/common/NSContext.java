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

package net.sf.okapi.acorn.common;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * Represents the context of the namespaces at a specific point during reading or writing of an XLIFF document.
 * The namespaces http://www.w3.org/XML/1998/namespace and http://www.w3.org/2000/xmlns/ are pre-defined.
 * This class implements the {@link NamespaceContext} interface. 
 */
public class NSContext implements NamespaceContext, Cloneable {
	
	private Hashtable<String, String> table;

	/**
	 * Creates a new object.
	 */
	public NSContext () {
		table = new Hashtable<String, String>();
	}
	
	/**
	 * Creates a new context object and add one namespace to it.
	 * @param prefix the prefix of the namespace to add.
	 * @param uri the namespace URI.
	 */
	public NSContext (String prefix,
		String uri)
	{
		this();
		put(prefix, uri);
	}
	
	@Override
	public String toString () {
		return table.toString();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public NSContext clone () {
		NSContext copy = new NSContext();
		copy.table = (Hashtable<String, String>)table.clone();
		return copy;
	}

	@Override
	public String getNamespaceURI (String prefix) {
		if ( table.containsKey(prefix) )
			return table.get(prefix);
		if ( prefix.equals(XMLConstants.XML_NS_PREFIX) )
			return XMLConstants.XML_NS_URI;
		if ( prefix.equals(XMLConstants.XMLNS_ATTRIBUTE) )
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		else
			return XMLConstants.NULL_NS_URI;
	}

	@Override
	public String getPrefix (String uri) {
		Enumeration<String> E = table.keys();
		String key;
		while ( E.hasMoreElements() ) {
			key = E.nextElement();
			if ( table.get(key).equals(uri) )
				return key;
		}
		if ( uri.equals(XMLConstants.XML_NS_URI))
			return XMLConstants.XML_NS_PREFIX;
		if ( uri.equals(XMLConstants.XMLNS_ATTRIBUTE_NS_URI) )
			return XMLConstants.XMLNS_ATTRIBUTE;
		else
			return null;
	}

	@Override
	public Iterator<String> getPrefixes (String uri) {
		// Not implemented
		return null;
	} 

	/**
	 * Sets a prefix/uri pair to this context. No checking is done for existing
	 * prefix: If the same is already defined, it will be overwritten.
	 * @param prefix the prefix of the namespace.
	 * @param uri the URI of the namespace.
	 */
	public void put (String prefix,
		String uri)
	{
		table.put(prefix, uri);
	}

}
