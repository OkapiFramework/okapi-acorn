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

package net.sf.okapi.acorn.client;

import net.sf.okapi.acorn.common.Util;

import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.TargetState;

import com.mycorp.tmlib.Entry;
import com.mycorp.tmlib.SimpleTM;

public class SimpleTMLeveraging extends XLIFFDocumentTask {

	private SimpleTM tm;
	
	public SimpleTMLeveraging (SimpleTM tm) {
		this.tm = tm;
	}

	@Override
	protected void process (ISegment segment) {
		super.process(segment);
		// Skip segments with empty source, or with existing non-empty target
		if ( segment.hasTarget() && !segment.getTarget().isEmpty() ) return;
		if ( segment.getSource().isEmpty() ) return;
		// Treat code-only segments cases (whitespace is not considered text here)
		if ( !segment.getSource().hasText(false) ) {
			// Just clone the source
			segment.copyTarget(segment.getSource());
			segment.setState(TargetState.FINAL);
			segment.setSubState(null);
			return;
		}
		// If there is text: try to leverage from the TM
		Entry res = tm.search(segment.getSource());
		if ( res != null ) {
			Util.leverage(segment, res.getTarget());
		}
	}

    @Override
	public String getInfo () {
		return "<html><header><style>"
			+ "body{font-size: large;} code{font-size: large;}"
			+ "</style></header><body>"
			+ "<p>This function calls a simple <b>Translation Memory engine</b> "
			+ "and tries to leverage matches it finds into the un-translated segment of the document.</p>"
			+ "</body></html>";
	}

}
