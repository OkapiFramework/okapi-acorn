package net.sf.okapi.acorn.taus;

import net.sf.okapi.acorn.client.XLIFFDocumentTask;
import net.sf.okapi.acorn.common.Util;

import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ISegment;

public class TAUSTransRetriever extends XLIFFDocumentTask {

	private TransAPIClient ttapi;

	public TAUSTransRetriever (TransAPIClient ttapi) {
		this.ttapi = ttapi;
	}
	
	@Override
	protected void process (ISegment segment) {
		super.process(segment);
		if ( segment.getSource().isEmpty() ) return;
		if ( segment.hasTarget() && !segment.getTarget().isEmpty() ) return;
		// Retrieve the request
		if ( ttapi.translation_id_get(segment.getId()) >= 400 ) {
			throw new RuntimeException("Error "+ttapi.getResponseString());
		}
		// Else: parse the result back to the target content
		IContent target = ttapi.getTargetContent(ttapi.getResponseString());
		if ( target != null ) {
			Util.leverage(segment, target);
		}
		// Remove the translation request from the server
		ttapi.translation_id_delete(segment.getId());
	}

    @Override
	public String getInfo () {
		return "<html><header><style>"
			+ "body{font-size: large;} code{font-size: large;}"
			+ "</style></header><body>"
			+ "<p>This function calls a translation server that implements the <b>TAUS Translation API v2</b> "
			+ "and retrieves, for each un-translated segment in the document, the translation request that was "
			+ "posted previously. Note that the server may have no translation available for some requests.</p>"
			+ "<p>Once retrieved the request is removed from the server.</p>"
			+ "</body></html>";
	}

    @Override
	public String getInfoLink () {
		return "https://labs.taus.net/interoperability/taus-translation-api";
	};
    
}
