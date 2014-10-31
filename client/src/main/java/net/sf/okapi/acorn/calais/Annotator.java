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

package net.sf.okapi.acorn.calais;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.okapi.lib.xliff2.core.Fragment;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.IMTag;

public class Annotator {

	public interface IInfoSpan {
		public int getStart ();
		public int getEnd ();
		public IMTag getInfo ();
	}

	private static Comparator<IInfoSpan> positionComparator = new Comparator<IInfoSpan>() {
		public int compare(IInfoSpan span1, IInfoSpan span2) {
			int s = Integer.compare(span2.getStart(), span1.getStart());
			if ( s == 0 ) {
				s = Integer.compare((span2.getEnd()-span2.getStart()), (span1.getEnd()-span1.getStart()));
			}
			return s;
			//Integer.compare(span2.getStart(), span1.getStart());
		}
	};

	/**
	 * Annotates a given fragment with a list of annotations.
	 * @param fragment the fragment to annotate.
	 * @param spans the annotations to apply. The list must be modifiable.
	 * The start and end positions in each span must be the coded text positions
	 * of the span (i.e. in the text return by {@link Fragment#getPlainText()}.
	 * The IDs of the tags in the spans will not be used.
	 */
	public void annotates (IContent content,
		List<IInfoSpan> spans)
	{
		// First make sure the spans are sorted, first in the list is the last one
		Collections.sort(spans, positionComparator);

		// Get the base text
		int start, end;
		String type, value;
		int prevStart = Integer.MAX_VALUE;
		int prevEnd = Integer.MAX_VALUE;
		int countStart = 0;
		int countEnd = 0;
		
		for ( IInfoSpan span : spans ) {
			start = span.getStart();
			end = span.getEnd();
			type = span.getInfo().getType();
			value = span.getInfo().getValue();
//System.out.println("s="+start+" e="+end+" len="+(end-start)+" t="+type+" v="+value);
			
			int addStart = 0;
			int addEnd = 0;
			if ( start == prevStart ) {
				addStart = (++countStart)*2;
				addEnd = (++countEnd)*2;
				if ( end > prevEnd ) {
					addEnd = (++countEnd)*2;
				}
			}
			else { // start < prevStart (because of sorting)
				if ( end > prevStart ) {
					addEnd = (++countEnd)*2;
					if ( end >= prevEnd ) {
						addEnd = (++countEnd)*2;
					}
				}
				else {
					countEnd = 0;
				}
				countStart = 0;
			}
			prevStart = start;
			prevEnd = end;
			
			content.annotate(start+addStart, end+addEnd, type, value, null);
		}
	}

	
}
