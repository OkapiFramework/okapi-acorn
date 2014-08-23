package com.mycorp.tmlib;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EntryTest {

	@Test
	public void testMakeSearchKey () {
		assertEquals("a b",
			Entry.makeSearchKey("a\b\t\n\f\rb"));
		assertEquals("this is a test",
			Entry.makeSearchKey("This \t is a TEST."));
		assertEquals("this is a test",
			Entry.makeSearchKey("  (This \t \"is\" a {'TEST'})!"));
		assertEquals("this is a test",
			Entry.makeSearchKey("this- is$ %a\t\t\tTest@#"));
	}

}
