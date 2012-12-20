package uk.co.badgersinfoil.metaas.impl;

import org.antlr.runtime.RecognitionException;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class ParserTests extends TestCase {

	public void testUnaryExpressionNotPlusMinus() throws RecognitionException {
		AS3Parser parser = ASTUtils.parse("delete foo");
		LinkedListTree ast = AS3FragmentParser.tree(parser.unaryExpressionNotPlusMinus());
		assertEquals("delete", ast.getText());
		assertEquals(1, ast.getChildCount());
		LinkedListTree child = ast.getFirstChild();
		assertEquals(AS3Parser.IDENT, child.getType());
		assertEquals("foo", child.getText());
	}

	public void testUnnamedRestParameter() throws RecognitionException {
		AS3Parser parser = ASTUtils.parse("(foo, ...)");
		LinkedListTree ast = AS3FragmentParser.tree(parser.parameterDeclarationList());
	}

	public void testNamedRestParameter() throws RecognitionException {
		AS3Parser parser = ASTUtils.parse("(foo, ...bar)");
		LinkedListTree ast = AS3FragmentParser.tree(parser.parameterDeclarationList());
	}
	
	public void testBasicNewExpression() throws RecognitionException {
		AS3Parser parser = ASTUtils.parse("new String()");
		LinkedListTree ast = AS3FragmentParser.tree(parser.newExpression());
		assertEquals(AS3Parser.NEW, ast.getType());
		assertEquals(2, ast.getChildCount());
		assertEquals(AS3Parser.IDENT, ast.getFirstChild().getType());
		assertEquals(AS3Parser.ARGUMENTS, ast.getLastChild().getType());
	}
	
	public void testRecursiveNewExpression() throws RecognitionException {
		AS3Parser parser = ASTUtils.parse("new new Function('blah')()");
		LinkedListTree ast = AS3FragmentParser.tree(parser.newExpression());
		assertEquals(AS3Parser.NEW, ast.getType());
System.err.println(ast.toStringTree());
		assertEquals(2, ast.getChildCount());
		assertEquals(AS3Parser.NEW, ast.getFirstChild().getType());
		assertEquals(AS3Parser.ARGUMENTS, ast.getLastChild().getType());
	}
}
