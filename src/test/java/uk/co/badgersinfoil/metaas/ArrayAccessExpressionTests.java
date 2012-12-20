package uk.co.badgersinfoil.metaas;

import java.io.StringWriter;
import uk.co.badgersinfoil.metaas.dom.ASArrayAccessExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTPrinter;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class ArrayAccessExpressionTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASArrayAccessExpression expr;
	protected Expression subscript;
	protected Expression target;

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

	public void testBasic() {
		target = fact.newExpression("foo");
		subscript = fact.newIntegerLiteral(1);
		expr = fact.newArrayAccessExpression(target, subscript);
		assertEquals(target, expr.getTargetExpression());
		assertEquals(subscript,expr.getSubscriptExpression());
	}
	
	public void testParse() {
		expr = (ASArrayAccessExpression)fact.newExpression("foo[1]");
		target = fact.newExpression("foo");
		subscript = fact.newExpression("1");
	}

	public void testTokenBoundries() {
		expr = (ASArrayAccessExpression)fact.newExpression("a[b][c]");
		Expression target = fact.newExpression("foo");
		expr.setTargetExpression(target);
	}

	public void testSubscript() {
		expr = (ASArrayAccessExpression)fact.newExpression("foo[1]");
		target = fact.newExpression("foo");
		subscript = fact.newStringLiteral("bar");
		expr.setSubscriptExpression(subscript);
	}
}