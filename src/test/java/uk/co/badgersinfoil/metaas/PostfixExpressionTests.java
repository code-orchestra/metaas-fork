package uk.co.badgersinfoil.metaas;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASPostfixExpression;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class PostfixExpressionTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private Expression sub;
	private ASPostfixExpression expr;

	protected void setUp() {
		sub = fact.newExpression("i");
	}

	protected void tearDown() {
		if (expr != null) {
			assertEquals(sub, expr.getSubexpression());
		}
	}

	public void testSetOp() {
		expr = fact.newPostDecExpression(sub);
		assertOp(ASPostfixExpression.Op.POSTDEC);
		expr.setOperator(ASPostfixExpression.Op.POSTINC);
		assertOp(ASPostfixExpression.Op.POSTINC);
	}

	public void testPostDec() {
		expr = fact.newPostDecExpression(sub);
		assertOp(ASPostfixExpression.Op.POSTDEC);
	}

	public void testPostInc() {
		expr = fact.newPostIncExpression(sub);
		assertOp(ASPostfixExpression.Op.POSTINC);
	}
	
	public void testSetSubexpression() {
		expr = fact.newPostIncExpression(sub);
		sub = fact.newExpression("j");
		expr.setSubexpression(sub);
	}

	private void assertOp(ASPostfixExpression.Op expected) {
		LinkedListTree ast = ((ASTExpression)expr).getAST();
		Expression expr = fact.newExpression(ASTUtils.stringifyNode(ast));
		assertEquals(expected, ((ASPostfixExpression)expr).getOperator());
	}

	public void testOpString() throws IllegalArgumentException, IllegalAccessException {
		EnumAssert.assertValidEnumConstants(ASPostfixExpression.Op.class);
	}
}