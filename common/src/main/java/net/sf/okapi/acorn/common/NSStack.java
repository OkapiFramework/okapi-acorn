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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;

public class NSStack {

	private HashMap<String, String> map;
	private int level;
	private HashMap<String, Integer> levels;

	public NSStack () {
		map = new HashMap<>();
		levels = new HashMap<>();
	}

	/**
	 * Push the context by incrementing the current level.
	 */
	public void pushLevel () {
		level++;
	}
	
	/**
	 * Pops the context. This removes all namespaces that have been
	 * added at the current level and then decrement the level.
	 * Nothing happens if the current level is zero.
	 */
	public void popLevel () {
		if ( level == 0 ) return; // Too many pops
		Iterator<String> iter = map.keySet().iterator();
		while ( iter.hasNext() ) {
			String prefix = iter.next();
			Integer lev = levels.get(prefix);
			if ( lev == level ) {
				levels.remove(prefix);
				iter.remove();
			}
		}
		level--;
	}
	
	/**
	 * Adds a prefix/namespace-URI mapping. If the namespace is already declared the method
	 * returns the prefix it uses and does not add a new mapping. If the namespace does not
	 * exists yet, it is added and if the given prefix is already used, a new automated one is used.
	 * @param prefix the prefix of the namespace. If the given prefix is null it is treated as an empty string.
	 * Only one namespace can be mapped to the empty prefix.
	 * @param uri the URI of the namespace.
	 * @return the prefix used for the given namespace (it may be different from the given prefix).
	 */
	public String add (String prefix,
		String uri)
	{
		// If the namespace exists: returns the prefix used in the context
		// And do not add the mapping
		if ( map.containsValue(uri) ) {
			for ( Map.Entry<String, String> kv : map.entrySet() ) {
				if ( kv.getValue().equals(uri) ) return kv.getKey();
			}
		}
		// Else: add the namespace
		// First check if the prefix is used
		if ( prefix == null ) prefix = "";
		if ( map.containsKey(prefix) ) {
			// If the prefix is used: Get a new automated prefix
			int i = 0;
			while ( map.containsKey(prefix) ) {
				prefix = "x"+i;
			}
		}
		// Now add the information
		map.put(prefix, uri);
		levels.put(prefix, level);
		// And return the prefix used
		return prefix;
	}
	
	public boolean exists (String uri) {
		return map.containsValue(uri);
	}
	
	public String getPrefix (String uri) {
		for ( Map.Entry<String, String> kv : map.entrySet() ) {
			if ( kv.getValue().equals(uri) ) return kv.getKey();
		}
		return null;
	}
	
	public String getNamespaceURI (String prefix) {
		if ( map.containsKey(prefix) )
			return map.get(prefix);
		if ( prefix.equals(XMLConstants.XML_NS_PREFIX) )
			return XMLConstants.XML_NS_URI;
		if ( prefix.equals(XMLConstants.XMLNS_ATTRIBUTE) )
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		else
			return XMLConstants.NULL_NS_URI;
	}

}
