
package uk.co.badgersinfoil.metaas;

import uk.co.badgersinfoil.metaas.dom.ASArrayAccessExpression;
import uk.co.badgersinfoil.metaas.dom.ASFieldAccessExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASInvocationExpression;
import junit.framework.TestCase;

public class FieldAccessExpressionTests extends TestCase {

	private ActionScriptFactory fact = new ActionScriptFactory();

	public void testIt() {
		Expression target = fact.newExpression("f()");
		String name = "foo";
		ASFieldAccessExpression expr
			= fact.newFieldAccessExpression(target, name);
		assertNotNull(expr);
		ExtraAssertions.assertInstanceof(expr.getTargetExpression(),
		                                 ASInvocationExpression.class);
		assertEquals(name, expr.getName());

		// test setters,
		expr.setName("bar");
		expr.setTargetExpression(fact.newExpression("f[1]"));
		ExtraAssertions.assertInstanceof(expr.getTargetExpression(),
		                                 ASArrayAccessExpression.class);
		assertEquals("bar", expr.getName());
	}
}
