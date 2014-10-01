package net.sf.okapi.acorn.client;

import net.sf.okapi.acorn.common.Util;

import org.oasisopen.xliff.om.v1.GetTarget;
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
		
		// Treat code-only segments cases (WS is not seen as text here)
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
