package uk.co.badgersinfoil.metaas;

import uk.co.badgersinfoil.metaas.dom.ASBinaryExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class BinaryExpressionTests extends TestCase {
	private Expression left;
	private Expression right;
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASBinaryExpression expr;

	public void setUp() {
		left = fact.newIntegerLiteral(1);
		right = fact.newIntegerLiteral(2);
		expr = null;
	}

	public void tearDown() {
		if (expr != null) {
			assertEquals(left,
			             expr.getLeftSubexpression());
			assertEquals(right,
			             expr.getRightSubexpression());
		}
	}

	public void testAdd() {
		expr = fact.newAddExpression(left, right);
		assertOp(ASBinaryExpression.Op.ADD);
	}

	public void testAnd() {
		expr = fact.newAndExpression(left, right);
		assertOp(ASBinaryExpression.Op.AND);
	}

	public void testBitAnd() {
		expr = fact.newBitAndExpression(left, right);
		assertOp(ASBinaryExpression.Op.BITAND);
	}

	public void testBitOr() {
		expr = fact.newBitOrExpression(left, right);
		assertOp(ASBinaryExpression.Op.BITOR);
	}

	public void testBitXor() {
		expr = fact.newBitXorExpression(left, right);
		assertOp(ASBinaryExpression.Op.BITXOR);
	}

	public void testDivision() {
		expr = fact.newDivisionExpression(left, right);
		assertOp(ASBinaryExpression.Op.DIV);
	}

	public void testEquals() {
		expr = fact.newEqualsExpression(left, right);
		assertOp(ASBinaryExpression.Op.EQ);
	}

	public void testGreaterEquals() {
		expr = fact.newGreaterEqualsExpression(left, right);
		assertOp(ASBinaryExpression.Op.GE);
	}

	public void testGreaterThan() {
		expr = fact.newGreaterThanExpression(left, right);
		assertOp(ASBinaryExpression.Op.GT);
	}

	public void testLessEquals() {
		expr = fact.newLessEqualsExpression(left, right);
		assertOp(ASBinaryExpression.Op.LE);
	}

	public void testLessThan() {
		expr = fact.newLessThanExpression(left, right);
		assertOp(ASBinaryExpression.Op.LT);
	}

	public void testModulo() {
		expr = fact.newModuloExpression(left, right);
		assertOp(ASBinaryExpression.Op.MOD);
	}

	public void testMultiply() {
		expr = fact.newMultiplyExpression(left, right);
		assertOp(ASBinaryExpression.Op.MUL);
	}

	public void testNotEquals() {
		expr = fact.newNotEqualsExpression(left, right);
		assertOp(ASBinaryExpression.Op.NE);
	}

	public void testOr() {
		expr = fact.newOrExpression(left, right);
		assertOp(ASBinaryExpression.Op.OR);
	}

	public void testShiftLeft() {
		expr = fact.newShiftLeftExpression(left, right);
		assertOp(ASBinaryExpression.Op.SL);
	}

	public void testShiftRight() {
		expr = fact.newShiftRightExpression(left, right);
		assertOp(ASBinaryExpression.Op.SR);
	}

	public void testShiftRightUnsigned() {
		expr = fact.newShiftRightUnsignedExpression(left, right);
		assertOp(ASBinaryExpression.Op.SRU);
	}

	public void testSubtract() {
		expr = fact.newSubtractExpression(left, right);
		assertOp(ASBinaryExpression.Op.SUB);
	}

	private void assertOp(ASBinaryExpression.Op expected) {
		LinkedListTree ast = ((ASTExpression)expr).getAST();
		Expression expr = fact.newExpression(ASTUtils.stringifyNode(ast));
		assertEquals(expected, ((ASBinaryExpression)expr).getOperator());
	}

	public void testOpString() throws IllegalArgumentException, IllegalAccessException {
		EnumAssert.assertValidEnumConstants(ASBinaryExpression.Op.class);
	}

	public void testSetOp() {
		expr = fact.newAddExpression(left, right);
		assertOp(ASBinaryExpression.Op.ADD);
		expr.setOperator(ASBinaryExpression.Op.SUB);
		assertOp(ASBinaryExpression.Op.SUB);
	}

	public void testSetLeft() {
		expr = fact.newAddExpression(left, right);
		left = fact.newIntegerLiteral(24);
		expr.setLeftSubexpression(left);
	}

	public void testSetRight() {
		expr = fact.newAddExpression(left, right);
		right = fact.newIntegerLiteral(24);
		expr.setRightSubexpression(right);
	}
}