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
