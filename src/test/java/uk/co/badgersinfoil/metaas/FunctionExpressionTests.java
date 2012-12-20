package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import uk.co.badgersinfoil.metaas.dom.ASAssignmentExpression;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASExpressionStatement;
import uk.co.badgersinfoil.metaas.dom.ASFunctionExpression;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;

public class FunctionExpressionTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASMethod meth;
	private ASCompilationUnit reflect;

	protected void setUp() {
		unit = fact.newClass("Test");
		ASClassType clazz = (ASClassType)unit.getType();
		meth = clazz.newMethod("test", Visibility.PUBLIC, null);
		reflect = null;
	}

	protected void tearDown() throws IOException {
		if (reflect == null) {
			reflect = assertReflection();
		}
	}

	private ASCompilationUnit assertReflection() throws IOException {
		return CodeMirror.assertReflection(fact, unit);
	}

	public void testIt() {
		ASFunctionExpression func = fact.newFunctionExpression();
		assertNotNull(func);
		func.addParam("foo", "String");
		meth.newExprStmt(fact.newAssignExpression(fact.newExpression("myFunc"), func));
		func.addStmt("trace('hello world')");
	}

	public void testParse() {
		ASExpressionStatement exprStmt = (ASExpressionStatement)meth.addStmt("theFunc = function () { trace('foo!'); }");
		ASAssignmentExpression assign = (ASAssignmentExpression)exprStmt.getExpression();
		ExtraAssertions.assertInstanceof(assign.getRightSubexpression(), ASFunctionExpression.class);
	}

	public void testParseNamed() {
		ASExpressionStatement exprStmt = (ASExpressionStatement)meth.addStmt("theFunc = function fn() { trace('foo!'); }");
		ASAssignmentExpression assign = (ASAssignmentExpression)exprStmt.getExpression();
		ExtraAssertions.assertInstanceof(assign.getRightSubexpression(), ASFunctionExpression.class);
	}
}
