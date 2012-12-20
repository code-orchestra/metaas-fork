
package uk.co.badgersinfoil.metaas;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASSimpleNameExpression;
import junit.framework.TestCase;

public class NameTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();

	public void testSimpleName() {
		ASSimpleNameExpression expr = fact.newSimpleName("foo");
		assertNotNull(expr);
		assertEquals("foo", expr.getName());
	}

	public void testParseSimpleName() {
		Expression expr = fact.newExpression("foo");
		ExtraAssertions.assertInstanceof(expr, ASSimpleNameExpression.class);
	}
}
