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

package com.mycorp.tmlib;

import static org.junit.Assert.assertNotNull;
import net.sf.okapi.acorn.xom.Unit;

import org.junit.Test;
import org.oasisopen.xliff.om.v1.GetTarget;
import org.oasisopen.xliff.om.v1.IUnit;

public class SimpleTMTest {

	@Test
	public void testSimple () {
		SimpleTM tm = createTM();
		assertNotNull(tm);
	}

	private SimpleTM createTM () {
		SimpleTM tm = new SimpleTM();
		IUnit unit = new Unit("id");
		unit.appendSegment().getSource().append("This is a test.");
		unit.getPart(0).getTarget(GetTarget.CREATE_EMPTY).append("C'est un test.");
		tm.addSegments(unit);
		return tm;
	}
	
}
