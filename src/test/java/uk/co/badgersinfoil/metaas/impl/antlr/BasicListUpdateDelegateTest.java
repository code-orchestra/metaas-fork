package uk.co.badgersinfoil.metaas.impl.antlr;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.TokenBuilder;
import junit.framework.TestCase;

public class BasicListUpdateDelegateTest extends TestCase {

	public void testReplacedChild() {
		BasicListUpdateDelegate delegate = new BasicListUpdateDelegate();
		LinkedListTree parent = ASTUtils.newImaginaryAST(AS3Parser.TYPE_SPEC);
		int index = 0;
		LinkedListTree oldChild = ASTUtils.newAST(AS3Parser.IDENT, "foo");
		parent.addChildWithTokens(oldChild);
		LinkedListToken space = TokenBuilder.newSpace();
		parent.addToken(0, space);
		LinkedListToken semi = TokenBuilder.newSemi();
		parent.appendToken(semi);
		// check preconditions,
		assertEquals(space, parent.getStartToken());
		assertEquals(semi, parent.getStopToken());

		LinkedListTree child = AS3FragmentParser.parseTypeSpec("void").getFirstChild();

		// make the change,
		delegate.replacedChild(parent, index, child, oldChild);

		// check postconditions,
		assertEquals(space, parent.getStartToken());
		assertEquals(semi, parent.getStopToken());
		assertNotNull(space.getNext());
		assertEquals("void", space.getNext().getText());
		assertSame(semi, space.getNext().getNext());
	}

}
