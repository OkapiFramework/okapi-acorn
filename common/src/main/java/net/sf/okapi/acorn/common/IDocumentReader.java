package net.sf.okapi.acorn.common;

import java.io.File;

import org.oasisopen.xliff.om.v1.IDocument;

public interface IDocumentReader {

	public IDocument load (File inputFile);

}
