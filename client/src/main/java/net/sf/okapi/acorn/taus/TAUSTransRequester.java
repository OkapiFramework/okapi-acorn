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

package net.sf.okapi.acorn.taus;

import net.sf.okapi.acorn.client.XLIFFDocumentTask;

import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.ISegment;

public class TAUSTransRequester extends XLIFFDocumentTask {

	private TransAPIClient ttapi;
	private String defaultTrgLang;
	private String srcLang;
	private String trgLang;
	
	public TAUSTransRequester (String defaultTrgLang,
		TransAPIClient ttapi)
	{
		this.defaultTrgLang = defaultTrgLang;
		this.ttapi = ttapi;
	}
	
	@Override
	public void process (IDocument document) {
		srcLang = document.getSourceLanguage();
		trgLang = document.getTargetLanguage();
		if ( trgLang == null ) {
			trgLang = defaultTrgLang;
			document.setTargetLanguage(trgLang);
		}
		super.process(document);
	}
	
	@Override
	protected void process (ISegment segment) {
		super.process(segment);
		if ( segment.getSource().isEmpty() ) return;
		if ( segment.hasTarget() && !segment.getTarget().isEmpty() ) return;
		// This assume the segment ID is set properly
		// Post the translation request
		if ( ttapi.translation_post(segment.getId(), srcLang, trgLang,
			segment.getSource()) >= 400 )
		{
			throw new RuntimeException("Error "+ttapi.getResponseString());
		}
	}

    @Override
	public String getInfo () {
		return "<html><header><style>"
			+ "body{font-size: large;} code{font-size: large;}"
			+ "</style></header><body>"
			+ "<p>This function calls a translation server that implements the <b>TAUS Translation API v2</b> "
			+ "and posts a translation request for each un-translated segment in the document.</p>"
			+ "<p>The server triggers the translation, which can be retrieve later.</p>"
			+ "</body></html>";
	}

    @Override
	public String getInfoLink () {
		return "https://labs.taus.net/interoperability/taus-translation-api";
	};
    
}
