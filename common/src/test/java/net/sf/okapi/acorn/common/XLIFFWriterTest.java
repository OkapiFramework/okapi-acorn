package net.sf.okapi.acorn.common;

import static org.junit.Assert.assertEquals;
import net.sf.okapi.acorn.xom.Content;
import net.sf.okapi.acorn.xom.Unit;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IContent;

public class XLIFFWriterTest {

	@Test
	public void testOwnTagsStatus ()
		throws Exception
	{
		IContent cont = new Content(new Unit("id").getStore(), true);
		cont.openCodeSpan("1", "<1>"); // 2
		cont.closeCodeSpan("1", "</1>"); // 2
		cont.appendCode("2", "<2/>"); // -
		cont.openCodeSpan("3", "<3>"); // 1
		cont.openCodeSpan("4", "<4>"); // 1
		cont.closeCodeSpan("3", "</3>"); // 1
		cont.openCodeSpan("5", "<5>"); // 2
		cont.openCodeSpan("6", "<6>"); // will be deleted
		cont.closeCodeSpan("6", "</6>"); // will be 0
		cont.closeCodeSpan("5", "</5>"); // 2
		cont.closeCodeSpan("4", "</4>"); // 1
		cont.openCodeSpan("7", "<7>"); // 0
		cont.delete(14, 16); // Delete opening of id=6
		try ( XLIFFWriter writer = new XLIFFWriter() ) {
			assertEquals("<pc id=\"1\"></pc><ph id=\"2\"/><sc id=\"3\" canOverlap=\"no\"/><pc id=\"4\"><ec startRef=\"3\" canOverlap=\"no\"/>"
				+ "<pc id=\"5\"><ec id=\"6\" canOverlap=\"no\" isolated=\"yes\"/></pc></pc><sc id=\"7\" canOverlap=\"no\" isolated=\"yes\"/>",
				writer.toXLIFF(cont));
		}
		
	}

}
