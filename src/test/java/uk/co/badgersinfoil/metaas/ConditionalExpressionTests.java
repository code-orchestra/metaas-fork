package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASConditionalExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASFieldAccessExpression;
import uk.co.badgersinfoil.metaas.dom.ASIntegerLiteral;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;

public class ConditionalExpressionTests extends TestCase {
	private ASConditionalExpression conditionalExpr = null;
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASCompilationUnit reflect;

	protected void setUp() {
		unit = fact.newClass("Test");
		reflect = null;
		conditionalExpr = null;
	}

	protected void tearDown() throws IOException {
		if (reflect == null) {
			ASClassType clazz = (ASClassType)unit.getType();
			ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, null);
			meth.newExprStmt(conditionalExpr);
			reflect = assertReflection();
		}
	}

	private ASCompilationUnit assertReflection() throws IOException {
		return CodeMirror.assertReflection(fact, unit);
	}

	public void testBasic() {
		Expression conditionExpr = fact.newExpression("a.b");
		Expression thenExpr = fact.newExpression("1");
		Expression elseExpr = fact.newExpression("2");
		conditionalExpr = fact.newConditionalExpression(conditionExpr, thenExpr, elseExpr);
		ExtraAssertions.assertInstanceof(conditionalExpr.getConditionExpression(), ASFieldAccessExpression.class);
		ExtraAssertions.assertInstanceof(conditionalExpr.getThenExpression(), ASIntegerLiteral.class);
		ExtraAssertions.assertInstanceof(conditionalExpr.getElseExpression(), ASIntegerLiteral.class);
	}

	public void testParse() {
		conditionalExpr = (ASConditionalExpression)fact.newExpression("a.b ? 1 : 2");
		ExtraAssertions.assertInstanceof(conditionalExpr.getConditionExpression(), ASFieldAccessExpression.class);
		ExtraAssertions.assertInstanceof(conditionalExpr.getThenExpression(), ASIntegerLiteral.class);
		ExtraAssertions.assertInstanceof(conditionalExpr.getElseExpression(), ASIntegerLiteral.class);
	}

	public void testSetters() {
		Expression conditionExpr = fact.newExpression("a.b");
		Expression thenExpr = fact.newExpression("1");
		Expression elseExpr = fact.newExpression("2");
		conditionalExpr = fact.newConditionalExpression(conditionExpr, thenExpr, elseExpr);
		conditionalExpr.setConditionExpression(fact.newBooleanLiteral(true));
		conditionalExpr.setThenExpression(fact.newIntegerLiteral(3));
		conditionalExpr.setElseExpression(fact.newIntegerLiteral(4));
	}
}
