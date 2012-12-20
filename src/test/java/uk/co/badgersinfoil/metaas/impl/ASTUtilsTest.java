package uk.co.badgersinfoil.metaas.impl;

import java.io.IOException;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;


public class ASTUtilsTest extends TestCase {
	public void testFindIndentIsolatedTree() throws IOException {
		LinkedListTree ast = ASTUtils.newAST(AS3Parser.PACKAGE, "package");
		assertEquals("", ASTUtils.findIndent(ast));
		ast.addToken(0, TokenBuilder.newWhiteSpace("\t"));
		assertEquals("\t", ASTUtils.findIndent(ast));
	}
	public void testFindIndent() throws IOException {
		LinkedListTree ast = ASTUtils.newAST(AS3Parser.PACKAGE, "package");
		assertEquals("", ASTUtils.findIndent(ast));
		ast.addToken(0, TokenBuilder.newWhiteSpace("\t\t"));
		ast.addToken(0, TokenBuilder.newNewline());
		assertEquals("\t\t", ASTUtils.findIndent(ast));
	}
}
