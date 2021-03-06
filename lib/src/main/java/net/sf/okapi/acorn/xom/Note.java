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

import org.oasisopen.xliff.om.v1.AppliesTo;
import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.INote;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

/**
 * Implements the {@link INote} interface.
 */
public class Note extends BaseData1 implements INote {

	private String id;
	private String content;
	private AppliesTo appliesTo = AppliesTo.UNDEFINED;
	private int priority = 1;
	private String category;

	/**
	 * Creates a new {@link Note} object with a content with a scope set to {@link AppliesTo#UNDEFINED}.
	 * @param content the content of the note.
	 */
	public Note (String content) {
		this(content, AppliesTo.UNDEFINED);
	}
	
	/**
	 * Creates a new {@link Note} object with a content and a scope.
	 * @param content the content of this note.
	 * @param appliesTo the scope of this note.
	 */
	public Note (String content,
		AppliesTo appliesTo)
	{
		this.content = content;
		this.appliesTo = appliesTo;
	}

	/**
	 * Gets a representation for this note: its content.
	 * @return the text content of this note.
	 */
	@Override
	public String toString () {
		return content;
	}

	@Override
	public String getText () {
		return content;
	}
	
	@Override
	public void setText (String content) {
		this.content = content;
	}
	
	@Override
	public boolean isEmpty () {
		return (( content != null ) && !content.isEmpty());
	}

	@Override
	public AppliesTo getAppliesTo () {
		return appliesTo;
	}

	@Override
	public void setAppliesTo (AppliesTo appliesTo) {
		this.appliesTo = appliesTo;
	}

	@Override
	public String getId () {
		return id;
	}

	@Override
	public void setId (String id) {
		this.id = id;
	}

	@Override
	public String getCategory () {
		return category;
	}

	@Override
	public void setCategory (String category) {
		this.category = category;
	}
	
	@Override
	public int getPriority () {
		return priority;
	}
	
	@Override
	public void setPriority (int priority) {
		if (( priority < 1 ) || ( priority > 10 )) {
			throw new InvalidParameterException("Invalid priority value. It must be between 1 and 10.");
		}
		this.priority = priority;
	}

	@Override
	public boolean hasExtField () {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IExtFields getExtFields () {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNSUri () {
		// TODO Auto-generated method stub
		return null;
	}

}
