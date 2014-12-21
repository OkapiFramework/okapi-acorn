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

package net.sf.okapi.acorn.jsonaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class JSONAccessTest {

	@Test
	public void testAccessWithoutRead () {
		JSONAccess ja = new JSONAccess();
		assertFalse(ja.hasNext());
	}

	@Test
	public void testSetInputNoRules () {
		JSONAccess ja = new JSONAccess();
		assertNull(ja.setInput("{"+createData1()+"}").getRules());
	}

	@Test
	public void testSetInputWithRules () {
		JSONAccess ja = new JSONAccess();
		assertEquals(1, ja.setInput(createInput1()).getRules().size());
	}

	@Test
	public void testSimpleRulesBefore () {
		JSONAccess ja = new JSONAccess();
		ja.read(createInput1());
		assertTrue(testData1(ja));
	}

	@Test
	public void testSimpleRulesAfter () {
		JSONAccess ja = new JSONAccess();
		ja.read(createInput2());
		assertTrue(testData1(ja));
	}

	@Test
	public void testSimpleExternalRules () {
		JSONAccess ja = new JSONAccess();
		ja.read("{"+createData1()+"}",
			"{\"locRules\": [\"$.messages[*].text\"]}");
		assertTrue(testData1(ja));
	}

	@Test
	public void testModifications () {
		JSONAccess ja = new JSONAccess();
		ja.read("{"+createData1()+"}",
			"{\"locRules\": [\"$.messages[*].text\"]}");
		assertTrue(ja.hasNext());
		assertEquals("Text 1", ja.next());
		ja.setNewValue("new text");
		// Re-process the output
		ja.read(ja.getOutput());
		assertTrue(ja.hasNext());
		assertEquals("new text", ja.next());
	}

	@Test
	public void testReApply () {
		JSONAccess ja = new JSONAccess();
		// Apply once
		ja.read(createInput1());
		assertTrue(testData1(ja));
		// Re-apply
		ja.applyRules();
		assertTrue(testData1(ja));
	}

	@Test
	public void testAlternateExternalRules () {
		JSONAccess ja = new JSONAccess();
		ja.read("{"+createData1()+"}",
			"{\"locRules\": [\"$..text\"]}");
		assertTrue(testData1(ja));
		ja.read("{"+createData1()+"}",
			"{\"locRules\": [\"$['messages'][*]['text']\"]}");
		assertTrue(testData1(ja));
	}

	private String createInput1 () {
		return "{"
			+ "\"locRules\": [\"$.messages[*].text\"],"
			+ createData1()
			+ "}";
	}

	private String createInput2 () {
		return "{"
			+ createData1()
			+ ", \"locRules\": [\"$.messages[*].text\"]"
			+ "}";
	}

	private boolean testData1 (JSONAccess ja) {
		assertTrue(ja.hasNext());
		assertEquals("Text 1", ja.next());
		assertTrue(ja.hasNext());
		assertEquals("Text 2", ja.next());
		assertFalse(ja.hasNext());
		return true;
	}
	
	private String createData1 () {
		return "\"messages\": ["
			+ "{\"text\": \"Text 1\", \"other\": \"data1\"},"
			+ "{\"text\": \"Text 2\", \"other\": \"data2\"},"
			+ "],"
			+ "\"extra\": \"extra-data\"";
	}

}
