package uk.co.badgersinfoil.metaas;

import uk.co.badgersinfoil.metaas.dom.ASBinaryExpression;
import junit.framework.TestCase;

public class ExpressionTransplantTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	public void testBinaryNew() {
		ASBinaryExpression source = (ASBinaryExpression)fact.newExpression("3*7");
		try {
			fact.newAddExpression(source.getLeftSubexpression(),
			                      fact.newIntegerLiteral(1));
			fail("adding node to a second subtree should fail");
		} catch (SyntaxException e) {
			// expected
		}
	}
	public void testBinarySet() {
		ASBinaryExpression target = (ASBinaryExpression)fact.newExpression("foo + bar");
		ASBinaryExpression source = (ASBinaryExpression)fact.newExpression("3*7");
		try {
			target.setLeftSubexpression(source.getLeftSubexpression());
			fail("adding node to a second subtree should fail");
		} catch (SyntaxException e) {
			// expected
		}
	}
}
