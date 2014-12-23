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

import com.jayway.jsonpath.InvalidJsonException;

public class JSONAccessTest {

	@Test
	public void testAccessWithoutRead () {
		JSONAccess ja = new JSONAccess();
		assertFalse(ja.hasNext());
	}

	@Test (expected = InvalidJsonException.class)
	public void testBadInput () {
		JSONAccess ja = new JSONAccess();
		ja.read("{\"abc :\"test\"}"); // Bad JSON syntax
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
			"{\"locRules\": [{\"selector\":\"$.messages[*].text\",\"translate\":true}]}");
		assertTrue(testData1(ja));
	}

	@Test
	public void testSimpleExternalRulesUsingDefault () {
		JSONAccess ja = new JSONAccess();
		ja.read("{"+createData1()+"}",
			"{\"locRules\": [{\"selector\":\"$.messages[*].text\"}]}");
		assertTrue(testData1(ja));
	}

	@Test
	public void testModifications () {
		JSONAccess ja = new JSONAccess();
		ja.read("{"+createData1()+"}",
			"{\"locRules\": [{\"selector\":\"$.messages[*].text\",\"translate\":true}]}");
		assertTrue(ja.hasNext());
		assertEquals("Text 1", ja.next());
		ja.setNewValue("new text");
		// Re-apply the rules on the modified data
		ja.applyRules();
		assertTrue(ja.hasNext());
		assertEquals("new text", ja.next());
		// Try also to re-read the modified data
		ja.read(ja.getOutput());
		assertTrue(ja.hasNext());
		assertEquals("new text", ja.next());
	}

	@Test
	public void testSimpleReApply () {
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
			"{\"locRules\": [{\"selector\":\"$..text\",\"translate\":true}]}");
		assertTrue(testData1(ja));
		ja.read("{"+createData1()+"}",
			"{\"locRules\": [{\"selector\":\"$['messages'][*]['text']\",\"translate\":true}]}");
		assertTrue(testData1(ja));
	}

	@Test
	public void testOveriddenRules () {
		JSONAccess ja = new JSONAccess();
		ja.read("{"+createData1()+"}",
			"{\"locRules\": ["
			+ "{\"selector\":\"$..text\",\"translate\":true},"
			+ "{\"selector\":\"['messages'][0]['text']\",\"translate\":false}"
			+ "]}");
		// Second rule overrides the translate=true for the first message
		assertTrue(ja.hasNext());
		assertEquals("Text 2", ja.next());
		assertFalse(ja.hasNext());
	}

	private String createInput1 () {
		return "{"
			+ "\"locRules\": [{\"selector\":\"$.messages[*].text\",\"translate\":true}],"
			+ createData1()
			+ "}";
	}

	private String createInput2 () {
		return "{"
			+ createData1()
			+ ", \"locRules\": [{\"selector\":\"$.messages[*].text\",\"translate\":true}]"
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
