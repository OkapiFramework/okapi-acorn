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
		unit.appendNewSegment().getSource().append("This is a test.");
		unit.getPart(0).getTarget(GetTarget.CREATE_EMPTY).append("C'est un test.");
		tm.addSegments(unit);
		return tm;
	}
	
}
