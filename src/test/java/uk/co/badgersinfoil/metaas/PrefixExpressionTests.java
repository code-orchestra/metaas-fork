package uk.co.badgersinfoil.metaas;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASPrefixExpression;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class PrefixExpressionTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private Expression sub;
	private ASPrefixExpression expr;

	protected void setUp() {
		sub = fact.newExpression("i");
	}
	
	protected void tearDown() {
		if (expr != null) {
			assertNotNull(expr.getSubexpression());
		}
	}

	public void testSetOp() {
		expr = fact.newPreDecExpression(sub);
		assertOp(ASPrefixExpression.Op.PREDEC);
		expr.setOperator(ASPrefixExpression.Op.PREINC);
		assertOp(ASPrefixExpression.Op.PREINC);
	}

	public void testPreDec() {
		expr = fact.newPreDecExpression(sub);
		assertOp(ASPrefixExpression.Op.PREDEC);
	}

	public void testPreInc() {
		expr = fact.newPreIncExpression(sub);
		assertOp(ASPrefixExpression.Op.PREINC);
	}

	public void testPositive() {
		expr = fact.newPositiveExpression(sub);
		assertOp(ASPrefixExpression.Op.POS);
	}

	public void testNegative() {
		expr = fact.newNegativeExpression(sub);
		assertOp(ASPrefixExpression.Op.NEG);
	}

	public void testSetSubexpression() {
		expr = fact.newPreIncExpression(sub);
		sub = fact.newExpression("j");
		expr.setSubexpression(sub);
	}

	private void assertOp(ASPrefixExpression.Op expected) {
		LinkedListTree ast = ((ASTExpression)expr).getAST();
		Expression expr = fact.newExpression(ASTUtils.stringifyNode(ast));
		assertEquals(expected, ((ASPrefixExpression)expr).getOperator());
	}

	public void testOpString() throws IllegalArgumentException, IllegalAccessException {
		EnumAssert.assertValidEnumConstants(ASPrefixExpression.Op.class);
	}
}