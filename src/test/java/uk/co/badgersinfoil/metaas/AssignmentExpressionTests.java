package uk.co.badgersinfoil.metaas;

import uk.co.badgersinfoil.metaas.dom.ASAssignmentExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.ASTExpression;
import uk.co.badgersinfoil.metaas.impl.ASTUtils;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;
import junit.framework.TestCase;

public class AssignmentExpressionTests extends TestCase {
	private Expression left;
	private Expression right;
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASAssignmentExpression expr;

	public void setUp() {
		left = fact.newExpression("a");
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

	public void testAssign() {
		expr = fact.newAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.ASSIGN);
	}

	public void testAdd() {
		expr = fact.newAddAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.ADD_ASSIGN);
	}

	public void testBitAnd() {
		expr = fact.newBitAndAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.BITAND_ASSIGN);
	}

	public void testBitOr() {
		expr = fact.newBitOrAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.BITOR_ASSIGN);
	}

	public void testBitXor() {
		expr = fact.newBitXorAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.BITXOR_ASSIGN);
	}

	public void testDivision() {
		expr = fact.newDivideAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.DIV_ASSIGN);
	}

	public void testModulo() {
		expr = fact.newModuloAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.MOD_ASSIGN);
	}

	public void testMultiply() {
		expr = fact.newMultiplyAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.MUL_ASSIGN);
	}

	public void testShiftLeft() {
		expr = fact.newShiftLeftAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.SL_ASSIGN);
	}

	public void testShiftRight() {
		expr = fact.newShiftRightAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.SR_ASSIGN);
	}

	public void testShiftRightUnsigned() {
		expr = fact.newShiftRightUnsignedAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.SRU_ASSIGN);
	}

	public void testSubtract() {
		expr = fact.newSubtractAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.SUB_ASSIGN);
	}

	private void assertOp(ASAssignmentExpression.Op expected) {
		LinkedListTree ast = ((ASTExpression)expr).getAST();
		Expression expr = fact.newExpression(ASTUtils.stringifyNode(ast));
		assertEquals(expected, ((ASAssignmentExpression)expr).getOperator());
	}

	public void testOpString() throws IllegalArgumentException, IllegalAccessException {
		EnumAssert.assertValidEnumConstants(ASAssignmentExpression.Op.class);
	}

	public void testSetOp() {
		expr = fact.newAddAssignExpression(left, right);
		assertOp(ASAssignmentExpression.Op.ADD_ASSIGN);
		expr.setOperator(ASAssignmentExpression.Op.SUB_ASSIGN);
		assertOp(ASAssignmentExpression.Op.SUB_ASSIGN);
	}

	public void testSetLeft() {
		expr = fact.newAddAssignExpression(left, right);
		left = fact.newIntegerLiteral(24);
		expr.setLeftSubexpression(left);
	}

	public void testSetRight() {
		expr = fact.newAddAssignExpression(left, right);
		right = fact.newIntegerLiteral(24);
		expr.setRightSubexpression(right);
	}
}
