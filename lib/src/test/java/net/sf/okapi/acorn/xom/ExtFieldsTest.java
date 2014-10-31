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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.okapi.acorn.xom.Const;
import net.sf.okapi.acorn.xom.Unit;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IExtField;
import org.oasisopen.xliff.om.v1.IExtFields;
import org.oasisopen.xliff.om.v1.IUnit;
import org.oasisopen.xliff.om.v1.InvalidParameterException;

public class ExtFieldsTest {

	@Test
	public void testIWithExtFields () {
		IUnit unit = new Unit("id");
		assertFalse(unit.hasExtField());
		assertFalse(null==unit.getExtFields());
		assertFalse(null==unit.getNSUri());
		assertFalse(unit.getNSUri().isEmpty());
	}
	
	@Test
	public void testSimpleExtension () {
		IUnit unit = new Unit("id");
		IExtFields xfields = unit.getExtFields();
		IExtField xf = xfields.set("nsuri", "name", "value");
		assertEquals("name", xf.getName());
		assertEquals("value", xf.getValue());
		assertEquals("nsuri", xf.getNSUri());
		assertFalse(xf.isModule());
	}
	
	@Test
	public void testUnsupportedModule () {
		IUnit unit = new Unit("id");
		IExtFields xfields = unit.getExtFields();
		IExtField xf = xfields.set(Const.NS_XLIFF_FS20, "fs", "b");
		assertEquals("fs", xf.getName());
		assertEquals("b", xf.getValue());
		assertEquals(Const.NS_XLIFF_FS20, xf.getNSUri());
		assertTrue(xf.isModule());
	}
	
	@Test (expected=InvalidParameterException.class)
	public void testInvalidNSExplicit () {
		new Unit("id").getExtFields().set(Const.NS_XLIFF_CORE20, "name", "value");
	}
	
	@Test (expected=InvalidParameterException.class)
	public void testInvalidNSImplicit () {
		new Unit("id").getExtFields().set("name", "value");
	}
	
}
