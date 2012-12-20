package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.CodeMirror;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class ASTBuilderTest extends TestCase {

	public void testDup() {
		LinkedListTree src = AS3FragmentParser.parseExpr("1 + 1");
		LinkedListTree dst = ASTBuilder.dup(src);
		assertEquals(AS3Parser.DECIMAL_LITERAL, dst.getStartToken().getType());
		assertEquals(AS3Parser.WS, dst.getStartToken().getNext().getType());
		CodeMirror.assertASTMatch(src, dst);
	}
}
