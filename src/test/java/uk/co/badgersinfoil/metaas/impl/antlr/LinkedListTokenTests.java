package uk.co.badgersinfoil.metaas.impl.antlr;

import junit.framework.TestCase;

public class LinkedListTokenTests extends TestCase {
	public void testSetNext() {
		LinkedListToken tok = new LinkedListToken(1, "test");
		tok.setNext(null);  // should ne ok
		try {
			tok.setNext(tok);
			fail("Setting [token].next to [token] should fail, otherwise we get an infinite loop");
		} catch (Exception e) {
			// expected
		}
	}
	public void testSetPrev() {
		LinkedListToken tok = new LinkedListToken(1, "test");
		tok.setPrev(null);  // should ne ok
		try {
			tok.setPrev(tok);
			fail("Setting [token].prev to [token] should fail, otherwise we get an infinite loop");
		} catch (Exception e) {
			// expected
		}
	}
}
