package uk.co.badgersinfoil.metaas;

import java.io.IOException;

import uk.co.badgersinfoil.metaas.dom.*;
import junit.framework.TestCase;


public class E4XTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASMethod meth;

	protected void setUp() {
		unit = fact.newClass("Test");
		ASClassType clazz = (ASClassType)unit.getType();
		meth = clazz.newMethod("test", Visibility.PUBLIC, null);
	}

	protected void tearDown() throws IOException {
		CodeMirror.assertReflection(fact, unit);
	}

	public void testLiteralElement() {
		ASDeclarationStatement decl = (ASDeclarationStatement)meth.addStmt("var x:XML = <element/>");
		ASXMLLiteral init = (ASXMLLiteral)decl.getFirstVarInitializer();
		assertEquals("<element/>", init.getValueString());
	}

	public void testDefaultXMLNamespace() {
		ASDefaultXMLNamespaceStatement stmt = meth.newDefaultXMLNamespace("http://example.com\\");
		assertEquals("http://example.com\\", ((ASStringLiteral) stmt.getNamespace()).getValue());
	}

	public void testParseDefaultXMLNamespace() {
		ASDefaultXMLNamespaceStatement stmt
			= (ASDefaultXMLNamespaceStatement)meth.addStmt("default xml namespace = \"http://example.com\\\\\"");
		assertEquals("http://example.com\\", ((ASStringLiteral) stmt.getNamespace()).getValue());
	}

    /*
	public void testDescendant() {
		Expression target = fact.newExpression("a");
		Expression selector = fact.newExpression("b");
		ASDescendantExpression expr = fact.newDescendantExpression(target, selector);
		assertNotNull(expr);
		meth.newExprStmt(expr);
	}
	*/

	public void testParseDescendant() {
		ASFieldAccessExpression expr = (ASFieldAccessExpression) fact.newExpression("a..b");
		Expression target = expr.getTargetExpression();
		ExtraAssertions.assertInstanceof(target, ASSimpleNameExpression.class);
		assertEquals("a", ((ASSimpleNameExpression)target).getName());
		String rightName = expr.getName();
		assertEquals("b", rightName);
	}
	
	public void testFilterPredicate() {
		Expression target = fact.newExpression("a");
		Expression selector = fact.newExpression("b");
		ASFilterExpression expr = fact.newFilterExpression(target, selector);
		assertNotNull(expr);
		meth.newExprStmt(expr);
	}

	public void testParseFilterPredicate() {
		ASFilterExpression expr = (ASFilterExpression)fact.newExpression("a.(b)");
		Expression target = expr.getTarget();
		ExtraAssertions.assertInstanceof(target, ASSimpleNameExpression.class);
		assertEquals("a", ((ASSimpleNameExpression)target).getName());
		Expression query = expr.getQuery();
		ExtraAssertions.assertInstanceof(query, ASSimpleNameExpression.class);
		assertEquals("b", ((ASSimpleNameExpression)query).getName());
	}

	public void testStarAttribute() {
		ASStarAttribute expr = fact.newStarAttribute();
		assertNotNull(expr);
		meth.newExprStmt(expr);
	}

	public void testParseStarAttribute() {
		ASStarAttribute expr = (ASStarAttribute)fact.newExpression("@*");
		assertNotNull(expr);
		meth.newExprStmt(expr);
	}

	public void testPropertyAttribute() {
		ASPropertyAttribute expr = fact.newPropertyAttribute("test");
		assertNotNull(expr);
		assertEquals("test", expr.getNameString());
		meth.newExprStmt(expr);
	}

	public void testParsePropertyAttribute() {
		ASPropertyAttribute expr = (ASPropertyAttribute)fact.newExpression("@ns::test");
		assertNotNull(expr);
		assertEquals("ns::test", expr.getNameString());
		meth.newExprStmt(expr);
	}

	public void testExpressionAttribute() {
		Expression expr = fact.newExpression("foo");
		ASExpressionAttribute attrexpr = fact.newExpressionAttribute(expr);
		assertNotNull(attrexpr);
		ExtraAssertions.assertInstanceof(attrexpr.getExpression(), ASSimpleNameExpression.class);
		meth.newExprStmt(attrexpr);
	}

	public void testParseExpressionAttribute() {
		ASExpressionAttribute expr = (ASExpressionAttribute)fact.newExpression("@[foo]");
		assertNotNull(expr);
		ExtraAssertions.assertInstanceof(expr.getExpression(), ASSimpleNameExpression.class);
		meth.newExprStmt(expr);
	}

	public void testParseDescendantExpressionAttribute() {
		ASDescendantExpression expr = (ASDescendantExpression) fact.newExpression("a..@[b=1]");
		assertEquals("@[b=1]", expr.getName());

        Expression expression = fact.newExpression(expr.getName());
        System.out.println(expression.getClass());

		meth.newExprStmt(expr);
	}

	public void testParseAttributeProp() {
		ASFieldAccessExpression expr = (ASFieldAccessExpression)fact.newExpression("a.@b");
//		assertEquals(@????????!?!?, expr.getName());
		meth.newExprStmt(expr);
	}

	public void testParseStarProp() {
		ASFieldAccessExpression expr = (ASFieldAccessExpression)fact.newExpression("a.*");
//		assertEquals(????????!?!?, expr.getName());
		meth.newExprStmt(expr);
	}
}