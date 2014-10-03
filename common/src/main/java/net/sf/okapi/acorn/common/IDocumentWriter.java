package net.sf.okapi.acorn.common;

import java.io.File;

import org.oasisopen.xliff.om.v1.IDocument;

public interface IDocumentWriter {

	public void merge (IDocument document,
		File originalFile);

}
