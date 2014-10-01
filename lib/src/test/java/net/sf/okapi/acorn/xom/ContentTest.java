package net.sf.okapi.acorn.xom;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.CanReorder;
import org.oasisopen.xliff.om.v1.ICTag;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.TagType;

public class ContentTest {

	@Test
	public void testEmptyFragment () {
		IContent cont = new Content(Factory.XOM.createUnit("id").getStore(), true);
		assertEquals("", cont.getCodedText());
		assertEquals(0, cont.getTags().size());
	}
	
	@Test
	public void testIterator () {
		IContent cont = new Content(new Unit("id").getStore(), true);
		cont.append("Text in");
		cont.openCodeSpan("1", "<b>");
		cont.append("bold");
		cont.closeCodeSpan("1", "</b>");
		cont.append(" format.");
		cont.appendCode("2", "<br>");
		for ( Object obj : cont ) {
			if ( obj instanceof String ) {
				System.out.println((String)obj);
			}
			else if ( obj instanceof ICTag ) {
				ICTag ctag = (ICTag)obj;
				System.out.println("ICTag id="+ctag.getId()+" ["+ctag.getData()+"]");
			}
		}
	}

	@Test
	public void testSimpleFragment () {
		IContent cont = new Content(new Unit("id").getStore(), true);
		cont.append("Text in");
		cont.openCodeSpan("1", "<b>");
		cont.append("bold");
		cont.closeCodeSpan("1", "</b>");
		cont.append(" format.");
		cont.appendCode("2", "<br>");
		
		String ct = cont.getCodedText();
		assertEquals("Text in\uE101\uE110bold\uE102\uE110 format.\uE103\uE110", ct);
		//            01234567     8     90123     4     567890123
		assertEquals(3, cont.getTags().size());
		ITag tag = cont.getTags().get(XUtil.toKey(ct.charAt(7), ct.charAt(8)));
		assertEquals("1", tag.getId());
		ICTag code = (ICTag)tag;
		assertEquals("<b>", code.getData());
	}

	@Test
	public void testSharedFields () {
		IContent cont = new Content(new Store(null), true);
		ICTag sc = cont.openCodeSpan("1", "<b>");
		sc.setCanCopy(false);
		sc.setCanDelete(false);
		sc.setCanReorder(CanReorder.FIRSTNO);
		sc.setCanOverlap(true);
		sc.setSubType("x:st");
		sc.setType("fmt");
		ICTag ec = cont.closeCodeSpan("1", "<b>");
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
		ICTag sc = cont.openCodeSpan("1", "<b>");
		sc.setType("fmt");
		cont.append("b");
		cont.closeCodeSpan("1", "<b>");
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
		CTag end = (CTag)cont.getTags().get(U.kCC(0));
		ICTag newSc = new CTag(end.cc, TagType.OPENING, "1", "<B>");
		cont.insert(0, newSc);
		// Note the new OC is index 1 now (just implementation)
		assertEquals("fmt", cont.getTags().get(U.kOC(1)).getType());
		newSc.setSubType("x:test");
		assertEquals("x:test", ((ICTag)cont.getTags().get(U.kOC(1))).getSubType());
		assertEquals("x:test", sc.getSubType());
	}

	@Test
	public void testOwnTagsStatus () {
		IContent cont = new Content(new Unit("id").getStore(), true);
		ITag t1o = cont.openCodeSpan("1", "<1>"); // 2
		ITag t1c = cont.closeCodeSpan("1", "</1>"); // 2
		cont.appendCode("2", "<2/>"); // -
		ITag t3o = cont.openCodeSpan("3", "<3>"); // 1
		ITag t4o = cont.openCodeSpan("4", "<4>"); // 1
		ITag t3c = cont.closeCodeSpan("3", "</3>"); // 1
		ITag t5o = cont.openCodeSpan("5", "<5>"); // 2
		cont.openCodeSpan("6", "<6>"); // will be deleted
		ITag t6c = cont.closeCodeSpan("6", "</6>"); // will be 0
		ITag t5c = cont.closeCodeSpan("5", "</5>"); // 2
		ITag t4c = cont.closeCodeSpan("4", "</4>"); // 1
		ITag t7o = cont.openCodeSpan("7", "<7>"); // 0
		cont.delete(14, 16); // Delete t6o
		Map<ITag, Integer> map = cont.getOwnTagsStatus();
		assertEquals(2, (int)map.get(t1o));
		assertEquals(2, (int)map.get(t1c));
		assertEquals(1, (int)map.get(t3o));
		assertEquals(2, (int)map.get(t4o));
		assertEquals(1, (int)map.get(t3c));
		assertEquals(2, (int)map.get(t5o));
		assertEquals(0, (int)map.get(t6c));
		assertEquals(2, (int)map.get(t5c));
		assertEquals(2, (int)map.get(t4c));
		assertEquals(0, (int)map.get(t7o));
	}

}
