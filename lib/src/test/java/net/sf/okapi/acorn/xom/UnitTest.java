package net.sf.okapi.acorn.xom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.okapi.acorn.xom.Unit;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IContent;
import org.oasisopen.xliff.om.v1.ITag;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

public class UnitTest {

	@Test
	public void testDefaultUnit () {
		IUnit unit = new Unit("id");
		assertTrue(null!=unit.getStore());
		assertEquals("id", unit.getId());
		assertEquals(0, unit.getSegmentCount());
		assertEquals(0, unit.getPartCount());
	}

	@Test (expected=InvalidParameterException.class)
	public void testUnitConstructorWithNullId () {
		new Unit(null);
	}

	@Test (expected=InvalidParameterException.class)
	public void testUnitConstructorWithEmptyId () {
		new Unit("");
	}

	@Test (expected=InvalidParameterException.class)
	public void testSetIdWithNullString () {
		IUnit unit = new Unit("id");
		unit.setId(null);
	}

	@Test (expected=InvalidParameterException.class)
	public void testSetIdWithEmptyString () {
		IUnit unit = new Unit("id");
		unit.setId("");
	}

	@Test
	public void testSimpleUnit () {
		IUnit unit = new Unit("id");
		unit.appendNewSegment().setSource("src");
		unit.getSegment(0).setTarget("trg");
		unit.appendNewIgnorable().setSource(" S");
		unit.getPart(1).setTarget(" T");
		assertEquals(1, unit.getSegmentCount());
		assertEquals(2, unit.getPartCount());
		assertEquals("src", unit.getSegment(0).getSource().getCodedText());
		assertEquals("trg", unit.getPart(0).getTarget().getCodedText());
		assertEquals(" S", unit.getPart(1).getSource().getCodedText());
		assertEquals(" T", unit.getPart(1).getTarget().getCodedText());
	}

	@Test
	public void testTags () {
		IUnit unit = new Unit("id");
		IContent cont = unit.appendNewSegment().getSource();
		cont.startCodeSpan("1", "[a]");
		cont.closeCodeSpan("1", "[/a]");
		ITag tag = unit.getStore().getTag("1");
		assertTrue(null!=tag);
		assertEquals("1", tag.getId());
		assertTrue(null==unit.getStore().getTag("2"));
	}

}
