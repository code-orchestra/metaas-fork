package uk.co.badgersinfoil.metaas;

import java.io.StringWriter;
import uk.co.badgersinfoil.metaas.dom.ASBinaryExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTPrinter;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class TokenBoundryTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASBinaryExpression expr;

	public void setUp() {
		expr = null;
	}

	public void tearDown() {
		if (expr != null) {
			StringWriter buff = new StringWriter();
			LinkedListTree ast = ((ASTExpression)expr).getAST();
			new ASTPrinter(buff).print(ast);
			LinkedListTree parsed = AS3FragmentParser.parseExpr(buff.toString());
			CodeMirror.assertASTMatch(ast, parsed);
		}
	}

	// TODO: expression list

//
//TODO:	public void testAssignment() {
//		expr = (ASBinaryExpression)fact.newExpression("a = b = c");
//		ASExpression left = fact.newExpression("foo");
//		expr.setLeftSubexpression(left);
//	}

	// TODO: conditional expression

	public void testLogicalOr() {
		expr = (ASBinaryExpression)fact.newExpression("a || b || c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testLogicalAnd() {
		expr = (ASBinaryExpression)fact.newExpression("a && b && c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testBitOr() {
		expr = (ASBinaryExpression)fact.newExpression("a | b | c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testXor() {
		expr = (ASBinaryExpression)fact.newExpression("a ^ b ^ c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testBitAnd() {
		expr = (ASBinaryExpression)fact.newExpression("a & b & c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testEquality() {
		expr = (ASBinaryExpression)fact.newExpression("a == b == c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testRelational() {
		// not strictly possible (I wonder if the grammar should forbid?)
		expr = (ASBinaryExpression)fact.newExpression("a < b < c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testShift() {
		expr = (ASBinaryExpression)fact.newExpression("a << b << c");
		Expression left = fact.newExpression("foo");
		expr.setLeftSubexpression(left);
	}

	public void testAdditive() {
		expr = (ASBinaryExpression)fact.newExpression("1 + 2 + 2");
		Expression left = fact.newIntegerLiteral(1);
		expr.setLeftSubexpression(left);
	}

	public void testMultiplicative() {
		expr = (ASBinaryExpression)fact.newExpression("1 * 2 * 2");
		Expression left = fact.newIntegerLiteral(1);
		expr.setLeftSubexpression(left);
	}
}