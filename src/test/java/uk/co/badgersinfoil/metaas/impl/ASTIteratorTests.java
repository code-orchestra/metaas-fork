package uk.co.badgersinfoil.metaas.impl;

import java.util.NoSuchElementException;
import org.asdt.core.internal.antlr.AS3Parser;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class ASTIteratorTests extends TestCase {
	public void testCtor() {
		try {
			new ASTIterator(null);
			fail("should have rejected null constructor argument");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	public void testNextNoElement() {
		ASTIterator i = iterateTreeWithSingleChild();
		i.next();
		try {
			i.next();
			fail("expected exception when there is no next element");
		} catch (NoSuchElementException e) {
			// expected
		}
	}

	public void testTypedNextNoElement() {
		ASTIterator i = iterateTreeWithSingleChild();
		i.next();
		try {
			i.next(AS3Parser.DECIMAL_LITERAL);
			fail("expected exception when there is no next element");
		} catch (IllegalStateException e) {
			// expected
		}
	}

	public void testTypedNextNotFound() {
		ASTIterator i = iterateTreeWithSingleChild();
		try {
			i.next(AS3Parser.STRING_LITERAL);
			fail("expected exception when the next element is of the wrong type");
		} catch (IllegalStateException e) {
			// expected
		}
	}

	public void testTypedNexFound() {
		ASTIterator i = iterateTreeWithSingleChild();
		LinkedListTree ast = i.next(AS3Parser.DECIMAL_LITERAL);
		assertEquals("200", ast.getText());
	}

	public void testFindNotFound() {
		ASTIterator i = iterateTreeWithSingleChild();
		try {
			i.find(AS3Parser.STRING_LITERAL);
			fail("expected exception when no child has the given type");
		} catch (IllegalStateException e) {
			// expected
		}
	}

	private ASTIterator iterateTreeWithSingleChild() {
		LinkedListTree tree = ASTUtils.newAST(AS3Parser.DECIMAL_LITERAL, "100");
		tree.addChildWithTokens(ASTUtils.newAST(AS3Parser.DECIMAL_LITERAL, "200"));
		return new ASTIterator(tree);
	}
}