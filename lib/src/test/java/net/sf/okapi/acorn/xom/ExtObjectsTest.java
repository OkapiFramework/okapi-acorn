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

import java.util.List;

import net.sf.okapi.acorn.xom.Unit;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.IExtObject;
import org.oasisopen.xliff.om.v1.IExtObjects;
import org.oasisopen.xliff.om.v1.IUnit;

public class ExtObjectsTest {

	@Test
	public void testIWithExtObjects () {
		IUnit unit = new Unit("id");
		assertFalse(unit.hasExtObject());
		assertFalse(null==unit.getExtObjects());
		assertFalse(null==unit.getNSUri());
		assertFalse(unit.getNSUri().isEmpty());
	}
	
	@Test
	public void testSimpleExtension () {
		IUnit unit = new Unit("id");
		IExtObjects xobjs = unit.getExtObjects();
		IExtObject xo1 = xobjs.add("nsuri", "name");
		assertEquals("name", xo1.getName());
		assertFalse(xobjs.isEmpty());
		assertTrue(xo1.isEmpty());
		assertEquals("nsuri", xo1.getNSUri());
		assertFalse(xo1.isModule());
		// Get the object
		List<IExtObject> res = xobjs.find("nsuri", "name");
		assertEquals(1, res.size());
		assertTrue(xo1==res.get(0));
	}

}
