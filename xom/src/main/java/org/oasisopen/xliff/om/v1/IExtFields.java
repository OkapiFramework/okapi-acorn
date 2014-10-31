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

package org.oasisopen.xliff.om.v1;

/**
 * Represents a collection of extension fields ({@link IExtField} objects).
 */
public interface IExtFields extends Iterable<IExtField> {

	/**
	 * Gets the object to which the fields apply.
	 * @return the object to which the fields apply.
	 */
	public IWithExtFields getParent ();
	
	/**
	 * Gets the field for the given name and namespace URI.
	 * @param nsUri the field namespace URI.
	 * @param name the field name.
	 * @return the field for the given namespace URI and name, or null if it is not found.
	 */
	public IExtField get (String nsUri,
		String name);
	
	/**
	 * Gets the value of the field for the given name and namespace URI.
	 * @param nsUri the field namespace URI.
	 * @param name the field name.
	 * @return the value of the field for the given namespace URI and name, or null if it is not found.
	 */
	public String getValue (String nsUri,
		String name);
	
	/**
	 * Sets the value of a field for a given name and namespace URI.
	 * If such field exists already, its value (not the field object) is overwritten.
	 * If such field does not exist one is created.
	 * @param nsUri the field namespace URI (must not be the Core namespace URI).
	 * @param name the field name.
	 * @param value the value (can be null).
	 * @return the modified or created field.
	 * @throws InvalidParameterException if the namespace URI is the Core namespace URI.
	 */
	public IExtField set (String nsUri,
		String name,
		String value);

	/**
	 * Sets the value of a field for a given name in the namespace of the parent object.
	 * If such field exists already, its value (not the field object) is overwritten.
	 * If such field does not exist one is created.
	 * @param name the field name.
	 * @param value the value (can be null).
	 * @return the modified or created field.
	 * @throws InvalidParameterException if the namespace URI is the Core namespace URI.
	 */
	public IExtField set (String name,
		String value);
	
	/**
	 * Sets a given field object in this collection.
	 * If such field exists already (that is: there is already an entry in the collection with
	 * the same qualified name as the one of the field passed as parameter),
	 * it is replaced by the parameter.
	 * @param field the field object to set.
	 * @return the field that was set (same as the parameter).
	 * @throws InvalidParameterException if the namespace URI is the Core namespace URI.
	 */
	public IExtField set (IExtField field);

	/**
	 * Removes the field for a given name and namespace URI.
	 * @param nsUri the field namespace URI.
	 * @param name the field name
	 */
	public void delete (String nsUri,
		String name);
	
	/**
	 * Indicates if this collection is empty or not.
	 * @return true if there is no extension fields and no namespaces defined in this collection.
	 */
	public boolean isEmpty ();
	
	/**
	 * Removes all fields and namespaces from this collection.
	 */
	public void clear ();

	/**
	 * Sets a namespace.
	 * @param nsUri the namespace URI.
	 * @param nsShorthand the namespace shorthand.
	 */
	public void setNS (String nsUri,
		String nsShorthand);
	
	/**
	 * Gets the namespace shorthand for a given namespace URI.
	 * @param nsUri the namespace URI.
	 * @return the namespace shorthand or null if the namespace URI was not found.
	 */
	public String getNSShorthand (String nsUri);
	
	/**
	 * Gets the namespace URI for a given namespace shorthand.
	 * @param nsShorthand the namespace shorthand.
	 * @return the namespace URI or null if the namespace shorthand was not found.
	 */
	public String getNSUri (String nsShorthand);
	
	/**
	 * Gets an iterable interface for the namespaces defined in this collection 
	 * of extension fields.
	 * @return an iterable interface for the namespaces in this object.
	 */
	public Iterable<String> getNSUris ();

}
