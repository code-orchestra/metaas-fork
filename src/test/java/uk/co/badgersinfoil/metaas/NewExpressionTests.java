package uk.co.badgersinfoil.metaas;

import java.util.ArrayList;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.ASInvocationExpression;
import uk.co.badgersinfoil.metaas.dom.ASNewExpression;
import uk.co.badgersinfoil.metaas.dom.Invocation;


public class NewExpressionTests extends InvocationExpressionTests {

	public void setUp() {
		args = new ArrayList();
		sub = fact.newExpression("com.example.MyClass");
	}
	
	protected Invocation newTestExpr(Expression sub, List args) {
		return fact.newNewExpression(sub, args);
	}
	
	private ASNewExpression getExpr() {
		return (ASNewExpression)expr;
	}

	public void testNoArgs() {
		expr = newTestExpr(sub, null);
		assertNull(getExpr().getArguments());
	}
	
	public void testRemoveArgs() {
		args.add(fact.newIntegerLiteral(1));
		expr = newTestExpr(sub, args);
		getExpr().setArguments(null);
		assertNull(getExpr().getArguments());
	}

	public void testAddArgs() {
		expr = newTestExpr(sub, null);
		getExpr().setArguments(args);
		assertNotNull(getExpr().getArguments());
	}
	
	public void testParse() {
		expr = (Invocation)fact.newExpression("new Foo()");
	}
}