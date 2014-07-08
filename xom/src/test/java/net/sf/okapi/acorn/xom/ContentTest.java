package net.sf.okapi.acorn.xom;

import static org.junit.Assert.assertEquals;
import net.sf.okapi.acorn.xom.Content;
import net.sf.okapi.acorn.xom.EndCode;
import net.sf.okapi.acorn.xom.StartCode;
import net.sf.okapi.acorn.xom.Store;
import net.sf.okapi.acorn.xom.Unit;
import net.sf.okapi.acorn.xom.Util;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.ICode;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.TagType;

public class ContentTest {

	@Test
	public void testEmptyFragment () {
		IContent cont = new Content(new Unit("id").getStore(), true);
		
		assertEquals("", cont.getCodedText());
		assertEquals(0, cont.getTags().size());
	}
	
	@Test
	public void testSimpleFragment () {
		IContent cont = new Content(new Unit("id").getStore(), true);
		cont.append("Text in");
		cont.appendOpeningCode("1", "<b>");
		cont.append("bold");
		cont.closeCode("1", "</b>");
		cont.append(" format.");
		cont.appendStandaloneCode("2", "<br>");
		
		String ct = cont.getCodedText();
		assertEquals("Text in\uE101\uE110bold\uE102\uE110 format.\uE103\uE110", ct);
		//            01234567     8     90123     4     567890123
		assertEquals(3, cont.getTags().size());
		ITag tag = cont.getTags().get(Util.toKey(ct.charAt(7), ct.charAt(8)));
		assertEquals("1", tag.getId());
		ICode code = (ICode)tag;
		assertEquals("<b>", code.getData());
	}

	@Test
	public void testSharedFields () {
		IContent cont = new Content(new Store(null), true);
		ICode sc = cont.appendOpeningCode("1", "<b>");
		sc.setCanCopy(false);
		sc.setCanDelete(false);
		sc.setCanReorder(CanReorder.FIRSTNO);
		sc.setCanOverlap(true);
		sc.setSubType("x:st");
		sc.setType("fmt");
		ICode ec = cont.closeCode("1", "<b>");
		assertEquals(false, ec.getCanCopy());
		assertEquals(false, ec.getCanDelete());
		assertEquals(CanReorder.FIRSTNO, ec.getCanReorder());
		assertEquals(true, ec.getCanOverlap());
		assertEquals("x:st", ec.getSubType());
		assertEquals("fmt", ec.getType());
	}

	@Test
	public void testDeletion () {
		IContent cont = new Content(new Store(null), true);
		cont.append("a ");
		ICode sc = cont.appendOpeningCode("1", "<b>");
		sc.setType("fmt");
		cont.append("b");
		cont.closeCode("1", "<b>");
		cont.append(" c");
		// a ##b##c
		// 01234567
		// Delete a span that includes the opening code
		cont.delete(1, 5);
		assertEquals(1, cont.getTags().size());
		assertEquals("fmt", cont.getTags().get(U.kCC(0)).getType());
		// a##c
		// 0123
		// Re-insert the opening code elsewhere
		//TODO: need a more abstract way to connect start/end on add/insert
		EndCode end = (EndCode)cont.getTags().get(U.kCC(0));
		ICode newSc = new StartCode(end.cm, TagType.OPENING, "1", "<B>");
		cont.insert(0, newSc);
		// Note the new OC is index 1 now (just implementation)
		assertEquals("fmt", cont.getTags().get(U.kOC(1)).getType());
		newSc.setSubType("x:test");
		assertEquals("x:test", ((ICode)cont.getTags().get(U.kOC(1))).getSubType());
		assertEquals("x:test", sc.getSubType());
	}

}
