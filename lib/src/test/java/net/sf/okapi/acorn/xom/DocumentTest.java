package net.sf.okapi.acorn.xom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IDocument;
import org.oasisopen.xliff.om.v1.IFile;
import org.oasisopen.xliff.om.v1.IGroup;
import org.oasisopen.xliff.om.v1.IUnit;

public class DocumentTest {

	final static Factory xf = Factory.XOM;
	
	@Test
	public void testEmptyDocument () {
		IDocument doc = xf.createDocument();
		assertEquals(0, doc.getFileCount());
		assertEquals("2.0", doc.getVersion());
		assertEquals("en", doc.getSourceLanguage());
	}
	
	@Test
	public void testSimpleDocument () {
		IDocument doc = xf.createDocument();
		IFile f1 = doc.add(xf.createFile("f1"));
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
		IDocument doc = xf.createDocument();
		IFile f1 = doc.add(xf.createFile("f1"));
		IGroup g1 = f1.add(xf.createGroup(null, "g1"));
		IUnit u11 = g1.add(xf.createUnit("u11"));
		IUnit u1 = f1.add(xf.createUnit("u1"));
		assertTrue(f1==doc.getFile("f1"));
		assertTrue(g1==f1.getGroup("g1"));
		assertTrue(u11==g1.getUnit("u11"));
		assertTrue(u1==f1.getUnit("u1"));
	}

}
