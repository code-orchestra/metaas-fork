package uk.co.badgersinfoil.metaas;

import java.io.StringWriter;
import uk.co.badgersinfoil.metaas.dom.ASBinaryExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTPrinter;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class OperatorPrecedenceTests extends TestCase {
	private Expression left;
	private Expression right;
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASBinaryExpression expr;

	public void setUp() {
		right = fact.newIntegerLiteral(2);
		expr = null;
	}

	public void tearDown() {
		StringWriter buff = new StringWriter();
		LinkedListTree ast = ((ASTExpression)expr).getAST();
		new ASTPrinter(buff).print(ast);
		LinkedListTree parsed = AS3FragmentParser.parseExpr(buff.toString());
		CodeMirror.assertASTMatch(ast, parsed);
	}
	
	public void testBasic() {
		left = fact.newAddExpression(fact.newIntegerLiteral(1), fact.newIntegerLiteral(1));
		// the resulting structure will not be correct unless the
		// left-hand expression is parenthesised.  metaas should add
		// parenthesis automatically, as required
		// i.e. (1 + 1) * 2
		expr = fact.newMultiplyExpression(left, right);
	}
}
