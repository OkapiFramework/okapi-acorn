package net.sf.okapi.acorn.common;

import static org.junit.Assert.*;
import net.sf.okapi.acorn.xom.Factory;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ISegment;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.TargetState;

public class UtilTest {

	@Test
	public void testPlainText () {
		ISegment dest = Factory.XOM.createLoneSegment();
		assertEquals(TargetState.INITIAL, dest.getState());
		dest.setSubState("my:substate");
		dest.getSource().append("the source");
		ISegment orig = Factory.XOM.createLoneSegment();
		IContent trans = Factory.XOM.createContent(orig.getStore(), true);
		trans.setCodedText("the target");
		Util.leverage(dest, trans);
		// Check
		assertEquals("the target", dest.getTarget().getCodedText());
		assertEquals(TargetState.TRANSLATED, dest.getState());
		assertNull(dest.getSubState());
	}

	@Test
	public void testOneTagStandaloneNullType () {
		ISegment dest = Factory.XOM.createLoneSegment();
		dest.getSource().append("text");
		dest.getSource().appendCode("s1", "[aaa/]");
		ISegment orig = Factory.XOM.createLoneSegment();
		IContent trans = Factory.XOM.createContent(orig.getStore(), true);
		trans.appendCode("t1", "[AAA/]");
		trans.append("TEXT");
		Util.leverage(dest, trans);
		// Check
		assertEquals("<C:s1/>TEXT", Util.fmt(dest.getTarget()));
		assertEquals(TargetState.TRANSLATED, dest.getState());
		assertNull(dest.getSubState());
		ITag t1 = dest.getTarget().getOwnTags().get(0);
		ITag t2 = dest.getSource().getOwnTags().get(0);
		ITag t3 = trans.getOwnTags().get(0);
		assertFalse(t1==t2);
		assertFalse(t1==t3);
	}

	@Test
	public void testOneTagInSourceNoneInTarget () {
		ISegment dest = Factory.XOM.createLoneSegment();
		dest.getSource().append("text");
		dest.getSource().appendCode("s1", "[aaa/]");
		ISegment orig = Factory.XOM.createLoneSegment();
		IContent trans = Factory.XOM.createContent(orig.getStore(), true);
		trans.append("TEXT");
		Util.leverage(dest, trans);
		// Check
		assertEquals("TEXT<C:s1/>", Util.fmt(dest.getTarget()));
		assertEquals(TargetState.TRANSLATED, dest.getState());
		assertNull(dest.getSubState());
		ITag t1 = dest.getTarget().getOwnTags().get(0);
		ITag t2 = dest.getSource().getOwnTags().get(0);
		assertFalse(t1==t2);
	}

	@Test
	public void testNoTagInSourceOneInTarget () {
		ISegment dest = Factory.XOM.createLoneSegment();
		dest.getSource().append("text");
		ISegment orig = Factory.XOM.createLoneSegment();
		IContent trans = Factory.XOM.createContent(orig.getStore(), true);
		trans.appendCode("t1", "[AAA/]");
		trans.append("TEXT");
		Util.leverage(dest, trans);
		// Check
		assertEquals("TEXT", Util.fmt(dest.getTarget()));
		assertEquals(TargetState.TRANSLATED, dest.getState());
		assertNull(dest.getSubState());
	}

}
