package net.sf.okapi.acorn.calais;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.okapi.lib.xliff2.core.Fragment;
import net.sf.okapi.lib.xliff2.core.MTag;

public class Annotator {

	public interface IInfoSpan {
		public int getStart ();
		public int getEnd ();
		public MTag getInfo ();
	}

	private static Comparator<IInfoSpan> positionComparator = new Comparator<IInfoSpan>() {
		public int compare(IInfoSpan span1, IInfoSpan span2) {
			return Integer.compare(span2.getStart(), span1.getStart());
		}
	};

	/**
	 * Annotates a given fragment with a list of annotations.
	 * @param fragment the fragment to annotate.
	 * @param spans the annotations to apply. The list must be modifiable.
	 * The start and end positions in each span must be the plain text positions
	 * of the span (i.e. in the text return by {@link Fragment#getPlainText()}.
	 * The IDs of the tags in the spans will not be used.
	 */
	public void annotates (Fragment fragment,
		List<IInfoSpan> spans)
	{
		// First make sure the spans are sorted, first in the list is the last one
		Collections.sort(spans, positionComparator);

		// Get the base text
		String oriCt = fragment.getCodedText();
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
			System.out.println("s="+start+" e="+end+" t="+type+" v="+value);
			
			int addStart = 0;
			int addEnd = 0;
			if ( prevStart == start ) {
				addStart = (++countStart)*2;
				addEnd = (++countEnd)*2;
				if ( end > prevEnd ) {
					addEnd = (++countEnd)*2;
				}
			}
			else { // start > prevStart
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
			
			fragment.annotate(start+addStart, end+addEnd, type, value, null);
		}
	}

	
}
