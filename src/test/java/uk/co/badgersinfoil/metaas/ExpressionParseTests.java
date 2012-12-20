package uk.co.badgersinfoil.metaas;

import java.io.StringWriter;
import uk.co.badgersinfoil.metaas.dom.ASBinaryExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASPrefixExpression;
import uk.co.badgersinfoil.metaas.impl.AS3FragmentParser;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTPrinter;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class ExpressionParseTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();

	public void testAdd() {
		Expression expr = fact.newExpression("1+1");
		assertTrue(expr instanceof ASBinaryExpression);
		ASBinaryExpression add = (ASBinaryExpression)expr;
		assertEquals(ASBinaryExpression.Op.ADD, add.getOperator());
	}

	public void testMultiplyAddPresidence() {
		// addition has lower presidence, so appears higher in AST
		Expression expr = fact.newExpression("1+2*2");
		assertTrue(expr instanceof ASBinaryExpression);
		ASBinaryExpression add = (ASBinaryExpression)expr;
		assertEquals(ASBinaryExpression.Op.ADD, add.getOperator());
		ASBinaryExpression right = (ASBinaryExpression)add.getRightSubexpression();
		assertEquals(ASBinaryExpression.Op.MUL, right.getOperator());

		// addition still has lower presidence, so appears higher in AST
		expr = fact.newExpression("2*2+1");
		assertTrue(expr instanceof ASBinaryExpression);
		add = (ASBinaryExpression)expr;
		assertEquals(ASBinaryExpression.Op.ADD, add.getOperator());
		ASBinaryExpression left = (ASBinaryExpression)add.getLeftSubexpression();
		assertEquals(ASBinaryExpression.Op.MUL, left.getOperator());
	}

	public void testPreIncrement() {
		Expression expr = fact.newExpression("++i");
		assertTrue(expr instanceof ASPrefixExpression);
		ASPrefixExpression inc = (ASPrefixExpression)expr;
		assertEquals(ASPrefixExpression.Op.PREINC, inc.getOperator());
	}

	public void testInvokeInvocation() {
		Expression expr = fact.newExpression("a().b()");
		StringWriter buff = new StringWriter();
		LinkedListTree ast = ((ASTExpression)expr).getAST();
		CodeMirror.assertTokenStreamNotDisjoint(ast);
		new ASTPrinter(buff).print(ast);
		LinkedListTree parsed = AS3FragmentParser.parseExpr(buff.toString());
		CodeMirror.assertASTMatch(ast, parsed);
	}
}