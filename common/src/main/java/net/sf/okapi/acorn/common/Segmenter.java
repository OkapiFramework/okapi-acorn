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

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

public class Segmenter extends BaseXLIFFProcessor {

	@Override
	public void process (IUnit unit) {
		Pattern pattern = Pattern.compile("[\\.!][ \\t\\n]");
		for ( int partIndex=0; partIndex<unit.getPartCount(); partIndex++ ) {
			if ( !unit.getPart(partIndex).isSegment() ) continue;
			ISegment seg = (ISegment)unit.getPart(partIndex);
			// Skip segment that we cannot re-segment
			if ( !seg.getCanResegment() ) continue;

			// Find break opportunities
			String srcCt = seg.getSource().getCodedText();
			Matcher srcMatcher = pattern.matcher(srcCt);
			
			Matcher trgMatcher = null;
			if ( seg.hasTarget() ) {
				String trgCt = seg.getTarget().getCodedText();
				trgMatcher = pattern.matcher(trgCt);
			}
			int srcEnd = 0;
			int trgEnd = 0;
			if ( srcMatcher.find() ) {
				srcEnd = srcMatcher.end();
			}
			if (( trgMatcher != null ) && trgMatcher.find() ) {
				trgEnd = trgMatcher.end();
			}
			if (( srcEnd != 0 ) || ( trgEnd != 0 )) {
				unit.split(partIndex, srcEnd, srcEnd, trgEnd, trgEnd, true);
			}
			// Go to next segment, it will be the new segment if one was created
		}
		// Set the segment IDs for TAUS
		for ( ISegment segment : unit.getSegments() ) {
			if ( segment.getId().length() < 5 ) {
				segment.setId(UUID.randomUUID().toString());
			}
		}
	}

}
