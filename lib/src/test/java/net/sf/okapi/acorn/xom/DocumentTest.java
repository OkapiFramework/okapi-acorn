package net.sf.okapi.acorn.xom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;

public class DocumentTest {

	@Test
	public void testEmptyDocument () {
		IDocument doc = new Document();
		assertEquals(0, doc.getFileCount());
		assertEquals("2.0", doc.getVersion());
		assertEquals("en", doc.getSourceLanguage());
	}
	
	@Test
	public void testSimpleDocument () {
		IDocument doc = new Document();
		IFile f1 = doc.add(new File("f1"));
		assertEquals(1, doc.getFileCount());
		assertTrue(f1==doc.getFile("f1"));
		for ( IFile f2 : doc ) {
			assertTrue(f1==f2);
		}
		doc.remove("f1");
		assertEquals(0, doc.getFileCount());
	}

	@Test
	public void testGroupAndUnits () {
		IDocument doc = new Document();
		IFile f1 = doc.add(new File("f1"));
		
	}
}
