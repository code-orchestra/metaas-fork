package uk.co.badgersinfoil.metaas.impl.antlr;

import org.asdt.core.internal.antlr.AS3Parser;
import junit.framework.TestCase;

public class LinkedListTreeTests extends TestCase {
	LinkedListTreeAdaptor adaptor = new LinkedListTreeAdaptor();

	public void testIt() {
		LinkedListToken a = new LinkedListToken(AS3Parser.IDENT, "foo");
		LinkedListTree identA = (LinkedListTree)adaptor.create(a);

		LinkedListToken b = new LinkedListToken(AS3Parser.IDENT, "bar");
		LinkedListTree identB = (LinkedListTree)adaptor.create(b);
		identA.appendToken(new LinkedListToken(AS3Parser.DOT, "."));
		identA.addChildWithTokens(identB);
		
		assertSame(b, a.getNext().getNext());
	}
}
