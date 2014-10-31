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

import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.IMTag;
import org.oasisopen.xliff.om.v1.TagType;

public class MTag implements IMTag {

	protected MTagCommon cm;
	
	private final TagType tagType;

	/**
	 * Creates a new {@link MTag} object with a given id and type.
	 * Use this to create opening tags, the safe method to create closing tags is {@link #MTag(MTag)}.
	 * @param opening true to create a new opening tag, false to create a closing tag.
	 * @param id the ID of the new marker.
	 * @param type the type of the new marker (can be null for the default type).
	 */
	MTag (boolean opening,
		String id,
		String type)
	{
		tagType = (opening ? TagType.OPENING : TagType.CLOSING);
		cm = new MTagCommon(id, type);
	}

	/**
	 * Creates a new opening or closing tag for a marker.
	 * @param opposite the counterpart tag to connect to this new opening or closing tag.
	 */
	MTag (MTag opposite) {
		this.cm = opposite.cm;
		if ( opposite.tagType == TagType.OPENING ) tagType = TagType.CLOSING;
		else tagType = TagType.OPENING;
	}

	/**
	 * Copy constructor.
	 * @param original the original object to copy.
	 * @param opposite the opening/closing tag to connect with this new tag.
	 * this parameter must be created already. 
	 */
	MTag (IMTag original,
		MTag opposite)
	{
		this.tagType = original.getTagType();
		if ( opposite == null ) this.cm = new MTagCommon(original);
		else this.cm = opposite.cm;
	}

	@Override
	public boolean isCode () {
		return false;
	}

	@Override
	public TagType getTagType () {
		return tagType;
	}

	@Override
	public String getId () {
		return cm.getId();
	}

	@Override
	public void setId (String id) {
		cm.setId(id);
	}

	@Override
	public String getType () {
		return cm.getType();
	}

	@Override
	public void setType (String type) {
		cm.setType(type);
	}

	@Override
	public boolean getTranslate () {
		return cm.getTranslate();
	}

	@Override
	public void setTranslate (boolean translate) {
		cm.setTranslate(translate);
	}

	@Override
	public String getValue () {
		return cm.getValue();
	}

	@Override
	public void setValue (String value) {
		cm.setValue(value);
	}

	@Override
	public String getRef () {
		return cm.getRef();
	}

	@Override
	public void setRef (String ref) {
		cm.setRef(ref);
	}

	@Override
	public boolean hasExtField () {
		return cm.hasExtField();
	}

	@Override
	public IExtFields getExtFields () {
		return cm.getExtFields();
	}

	@Override
	public String getNSUri () {
		return cm.getNSUri();
	}

}
