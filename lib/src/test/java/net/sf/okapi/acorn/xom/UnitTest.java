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
		unit.appendSegment().setSource("src");
		unit.getSegment(0).setTarget("trg");
		unit.appendIgnorable().setSource(" S");
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
		IContent cont = unit.appendSegment().getSource();
		cont.openCodeSpan("1", "[a]");
		cont.closeCodeSpan("1", "[/a]");
		ITag tag = unit.getStore().getTag("1");
		assertTrue(null!=tag);
		assertEquals("1", tag.getId());
		assertTrue(null==unit.getStore().getTag("2"));
	}

}
