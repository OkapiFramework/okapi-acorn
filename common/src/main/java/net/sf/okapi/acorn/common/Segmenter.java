package net.sf.okapi.acorn.common;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.IUnit;

public class Segmenter extends BaseXLIFFProcessor {

	@Override
	public void process (IUnit unit) {
		Pattern pattern = Pattern.compile("\\.[ \\t\\n]");
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
