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

package net.sf.okapi.acorn.jsonaccess;

public class Rule {

	private String selector;
	private boolean translate;
	
	public Rule (String selector,
		boolean translate)
	{
		this.selector = selector;
		this.translate = translate;
	}
	
	public String getSelector () {
		return selector;
	}
	
	public void setSelector (String selector) {
		this.selector = selector;
	}
	
	public boolean getTranslate () {
		return translate;
	}
	
	public void setTranslate (boolean translate) {
		this.translate = translate;
	}
	
}
