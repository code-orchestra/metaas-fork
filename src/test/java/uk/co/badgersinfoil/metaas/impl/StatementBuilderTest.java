package uk.co.badgersinfoil.metaas.impl;

import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.SyntaxException;
import junit.framework.TestCase;

public class StatementBuilderTest extends TestCase {
	public void testInvalidStatement() {
		try {
			StatementBuilder.build(ASTUtils.newAST(AS3Parser.AS, "not-a-statement"));
			fail("should reject invalid AST node type");
		} catch (SyntaxException e) {
			// expected
		}
	}
}
