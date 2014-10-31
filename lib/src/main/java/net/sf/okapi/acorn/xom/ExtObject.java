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

package net.sf.okapi.acorn.xom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.oasisopen.xliff.om.v1.ExtObjectItemType;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjectData;
import org.oasisopen.xliff.om.v1.IExtObjectItem;

/**
 * Implements the {@link IExtObject} interface.
 */
public class ExtObject extends BaseData1 implements IExtObject {

	private QName qname;
	private List<IExtObjectItem> items = new ArrayList<IExtObjectItem>(1);

	/**
	 * Creates a new {@link IExtObject} object.
	 * @param nsUri the namespace URI of this object.
	 * @param name the name of this object.
	 */
	public ExtObject (String nsUri,
		String name)
	{
		this.qname = new QName(nsUri, name);
	}
	
	@Override
	public String getNSUri () {
		return qname.getNamespaceURI();
	}

	@Override
	public String getName () {
		return qname.getLocalPart();
	}

	@Override
	public boolean isModule () {
		return qname.getNamespaceURI().startsWith(Const.NS_XLIFF_MODSTART);
	}

	@Override
	public ExtObjectItemType getType () {
		return ExtObjectItemType.OBJECT;
	}

	@Override
	public boolean isEmpty () {
		return items.isEmpty();
	}

	@Override
	public List<IExtObjectItem> getItems () {
		return items;
	}

	@Override
	public IExtObjectData add (String content,
		boolean raw)
	{
		IExtObjectData item = new ExtObjectData(content);
		item.setRaw(raw);
		items.add(item);
		return item;
	}

	@Override
	public Iterator<IExtObjectItem> iterator () {
		return items.iterator();
	}

}
