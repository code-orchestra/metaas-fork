package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.util.List;

import uk.co.badgersinfoil.metaas.dom.*;
import junit.framework.TestCase;


public class StatementTests extends TestCase {

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

	private void assertFirstReflectedStatementIsA(Class expectedClass) throws IOException {
		reflect = assertReflection();
		ASClassType clazz = (ASClassType)reflect.getType();
		ASMethod meth = clazz.getMethod("test");
		List statements = meth.getStatementList();
		Object stmt = statements.get(0);
		assertTrue("Expected instance of "+expectedClass.getName()+", got "+stmt.getClass().getName(),
		           expectedClass.isInstance(stmt));
	}

	public void testStatements() throws IOException {
		meth.setDocComment("\n doc\n comment!\n");
	}

	public void testGenericStatement() {
		meth.addStmt("a=1");
	}

	public void testExpressionStatement() throws IOException {
		ASExpressionStatement exprStmt = meth.newExprStmt("go()");
		assertEquals("go()", exprStmt.getExpressionString());
		ExtraAssertions.assertInstanceof(exprStmt.getExpression(),
		                                 ASInvocationExpression.class);
		exprStmt.setExpression("stop()");
		assertEquals("stop()", exprStmt.getExpressionString());
		assertFirstReflectedStatementIsA(ASExpressionStatement.class);
	}

	public void testParseIf() {
		ASIfStatement ifStmt = (ASIfStatement)meth.addStmt("if (foo) blah();");
		assertNotNull(ifStmt.getThenStatement());
		ExtraAssertions.assertInstanceof(ifStmt.getThenStatement(), ASExpressionStatement.class);
		try {
			// no block in the 'if', so SyntaxException is expected,
			ifStmt.getStatementList();
			fail();
		} catch (SyntaxException e) {
			// expected
		}
	}

	public void testParseIfElseIf() {
		ASIfStatement ifStmt = (ASIfStatement)meth.addStmt("if (foo) {blah();} else if (bar) {other();}");
		assertNotNull(ifStmt.getElseStatement());
		ExtraAssertions.assertInstanceof(ifStmt.getElseStatement(), ASIfStatement.class);
		try {
			ifStmt.elseBlock();
			fail("should not allow access to block from else-clause when else-cluase contains other stmt type");
		} catch (SyntaxException e) {
			// expected
		}
	}

	public void testFor() {
		ASForStatement forStmt = meth.newFor((String)null, null, null);
		forStmt.addStmt("trace(\"forever\")");
		assertNull(forStmt.getInitString());
		assertNull(forStmt.getConditionString());
		assertNull(forStmt.getUpdateString());
		forStmt.setInit("var j");
		forStmt.setCondition("j<10");
		forStmt.setUpdate("j++");
		assertEquals("var j", forStmt.getInitString());
		assertEquals("j<10", forStmt.getConditionString());
		assertEquals("j++", forStmt.getUpdateString());
		ExtraAssertions.assertInstanceof(forStmt.getInit(), ASDeclarationStatement.class);
		ExtraAssertions.assertInstanceof(forStmt.getCondition(), ASBinaryExpression.class);
		ExtraAssertions.assertInstanceof(forStmt.getUpdate(), ASPostfixExpression.class);

		forStmt = meth.newFor("const i", "i<10", "i++");
		forStmt.addStmt("trace(i)");
		assertEquals("const i", forStmt.getInitString());
		assertEquals("i<10", forStmt.getConditionString());
		assertEquals("i++", forStmt.getUpdateString());
		forStmt.setInit(null);
		forStmt.setCondition((String)null);
		forStmt.setUpdate((String)null);
		assertNull(forStmt.getInitString());
		assertNull(forStmt.getConditionString());
		assertNull(forStmt.getUpdateString());

		forStmt.setInit("k=0");
		assertEquals("k=0", forStmt.getInitString());
		ExtraAssertions.assertInstanceof(forStmt.getInit(), ExpressionList.class);
		
		forStmt.setCondition(fact.newBooleanLiteral(true));
		forStmt.setUpdate(fact.newExpression("++i"));
	}

	public void testForIn() {
		ASForInStatement forInStmt = meth.newForIn("var i", "obj.arry");
		forInStmt.addStmt("trace(i)");

		assertEquals("var i", forInStmt.getVarString());
		assertEquals("obj.arry", forInStmt.getIteratedString());
		ExtraAssertions.assertInstanceof(forInStmt.getIterated(), ASFieldAccessExpression.class);

		forInStmt.setVar("j");
		forInStmt.setIterated("obj");
		assertEquals("j", forInStmt.getVarString());
		assertEquals("obj", forInStmt.getIteratedString());
		forInStmt.setIterated(fact.newExpression("obj.blah"));
	}

	public void testForEachIn() {
		ASForEachInStatement forEachInStmt = meth.newForEachIn("var i", "obj.arry");
		forEachInStmt.addStmt("trace(i)");

		assertEquals("var i", forEachInStmt.getVarString());
		assertEquals("obj.arry", forEachInStmt.getIteratedString());
		ExtraAssertions.assertInstanceof(forEachInStmt.getIterated(), ASFieldAccessExpression.class);

		forEachInStmt.setVar("j");
		forEachInStmt.setIterated("obj");
		assertEquals("j", forEachInStmt.getVarString());
		assertEquals("obj", forEachInStmt.getIteratedString());
		forEachInStmt.setIterated(fact.newExpression("obj.blah"));
	}

    /*
	public void testWhile() {
		ASWhileStatement whileStmt = meth.newWhile("test()");
		whileStmt.addStmt("trace(result())");
		assertEquals("test()", whileStmt.getConditionString());
		whileStmt.setCondition("a == b");
		assertEquals("a == b", whileStmt.getConditionString());
		ExtraAssertions.assertInstanceof(whileStmt.getCondition(), ASBinaryExpression.class);
		ExtraAssertions.assertInstanceof(whileStmt.getBody(), ASBlock.class);
	}
	*/

    /*
	public void testWhileCondition() {
		Expression left = fact.newExpression("a");
		Expression right = fact.newExpression("b");
		ASWhileStatement whileStmt = meth.newWhile(fact.newAndExpression(left, right));
		whileStmt.addStmt("trace(result())");
		assertEquals("a && b", whileStmt.getConditionString());
		whileStmt.setCondition(fact.newBooleanLiteral(true));
		ExtraAssertions.assertInstanceof(whileStmt.getCondition(), ASBooleanLiteral.class);
	}
	*/

    /*
	public void testDoWhile() {
		ASDoWhileStatement doWhileStmt = meth.newDoWhile("test()");
		doWhileStmt.addStmt("trace(result())");
		assertEquals("test()", doWhileStmt.getConditionString());
		doWhileStmt.setCondition("a == b");
		assertEquals("a == b", doWhileStmt.getConditionString());
		ExtraAssertions.assertInstanceof(doWhileStmt.getCondition(), ASBinaryExpression.class);
		doWhileStmt.setCondition(fact.newBooleanLiteral(true));
	}
	*/

	public void testWith() {
		ASWithStatement with = meth.newWith("foo");
		with.addStmt("trace(test)");
		assertEquals("foo", with.getScopeString());
		with.setScope("foo.bar");
		assertEquals("foo.bar", with.getScopeString());
		ExtraAssertions.assertInstanceof(with.getScope(), ASFieldAccessExpression.class);
		with.setScope(fact.newExpression("bar.foo"));
		ExtraAssertions.assertInstanceof(with.getScope(), ASFieldAccessExpression.class);
		ExtraAssertions.assertInstanceof(with.getBody(), ASBlock.class);
	}

	public void testDeclaration() {
		ASDeclarationStatement decl = meth.newDeclaration("a=1");
		assertFalse(decl.isConstant());
		decl.setConstant(false); // should not change the result
		assertFalse(decl.isConstant());

		assertEquals("a", decl.getFirstVarName());
		assertNull(decl.getFirstVarType().getName());
		ExtraAssertions.assertInstanceof(decl.getFirstVarInitializer(), ASIntegerLiteral.class);

		decl.setConstant(true);
		assertTrue(decl.isConstant());

		List vars = decl.getVars();
		ExtraAssertions.assertSize(1, vars);
		ASVarDeclarationFragment var = (ASVarDeclarationFragment)vars.get(0);
		assertEquals("a", var.getName());
		assertNull(var.getTypeName());
		ExtraAssertions.assertInstanceof(var.getInitializer(), ASIntegerLiteral.class);
	}

	public void testParseDeclaration() {
		ASDeclarationStatement decl = (ASDeclarationStatement)meth.addStmt("var a=1, b:String='2'");
		List vars = decl.getVars();
		ExtraAssertions.assertSize(2, vars);

		ASVarDeclarationFragment a = (ASVarDeclarationFragment)vars.get(0);
		assertEquals("a", a.getName());
		assertNull(a.getTypeName());
		ExtraAssertions.assertInstanceof(a.getInitializer(), ASIntegerLiteral.class);

		ASVarDeclarationFragment b = (ASVarDeclarationFragment)vars.get(1);
		assertEquals("b", b.getName());
		assertEquals("String", b.getTypeName());
		ExtraAssertions.assertInstanceof(b.getInitializer(), ASStringLiteral.class);
	}
	
	public void testReturn() throws IOException {
		ASReturnStatement returnStmt = meth.newReturn();
		assertNull(returnStmt.getExpressionString());
		returnStmt.setExpression("false");
		assertEquals("false", returnStmt.getExpressionString());
		ExtraAssertions.assertInstanceof(returnStmt.getExpression(), ASBooleanLiteral.class);
		returnStmt.setExpression(fact.newIntegerLiteral(2));
		ExtraAssertions.assertInstanceof(returnStmt.getExpression(), ASIntegerLiteral.class);
		assertFirstReflectedStatementIsA(ASReturnStatement.class);
	}

	public void testReturnRemoveExpr() {
		ASReturnStatement returnStmt = meth.newReturn("true");
		assertEquals("true", returnStmt.getExpressionString());
		returnStmt.setExpression((String)null);
		assertNull(returnStmt.getExpressionString());
		assertNull(returnStmt.getExpression());
		
		// test removal when there's already nothing to remove,
		returnStmt.setExpression((String)null);
	}

	public void testBlockEmpty() {
		assertFalse(meth.containsCode());
		meth.addComment(" a comment");
		assertFalse(meth.containsCode());
		meth.addStmt("return foo");
		assertTrue(meth.containsCode());
	}

	public void testParseBlock() {
		ASBlock stmt = (ASBlock)meth.addStmt("{ }");
	}

	public void testBreak() {
		Statement brk = meth.addStmt("break");
		ExtraAssertions.assertInstanceof(brk, ASBreakStatement.class);
		meth.newBreak();
	}

	public void testTryCatch() {
		ASTryStatement tryStmt = meth.newTryCatch("e", "Exception");
		tryStmt.addStmt("trace('trying')");
		List catches = tryStmt.getCatchClauses();
		ExtraAssertions.assertSize(1, catches);
		assertEquals(1, catches.size());
		ASCatchClause eCatch = (ASCatchClause)catches.get(0);
		eCatch.addStmt("trace('catching e')");
		assertEquals("e", eCatch.getParamName());
		assertEquals("Exception", eCatch.getTypeName());
		assertNull(tryStmt.getFinallyClause());
		ASCatchClause fCatch = tryStmt.newCatchClause("f", "Error");
		fCatch.addStmt("trace('catching f')");
	}

	public void testTryFinally() {
		ASTryStatement tryStmt = meth.newTryFinally();
		ASFinallyClause finallyClause = tryStmt.getFinallyClause();
		assertNotNull(finallyClause);
		assertFalse(finallyClause.containsCode());
		finallyClause.addStmt("trace('hello')");
		assertTrue(finallyClause.containsCode());
		try {
			finallyClause = tryStmt.newFinallyClause();
			fail("shouldn't be able to add another finally-clause");
		} catch (SyntaxException e) {
			// expected
		}
	}
	
	public void testParseTrayCatchFinally() {
		ASTryStatement tryStmt = (ASTryStatement)meth.addStmt("try {" +
				"} catch (e) {" +
				"} catch (f:Exception) {" +
				"} finally {" +
				"}");
		List catches = tryStmt.getCatchClauses();
		ExtraAssertions.assertSize(2, catches);

		ASCatchClause catch0 = (ASCatchClause)catches.get(0);
		assertEquals("e", catch0.getParamName());
		assertNull(catch0.getTypeName());

		ASCatchClause catch1 = (ASCatchClause)catches.get(1);
		assertEquals("f", catch1.getParamName());
		assertEquals("Exception", catch1.getTypeName());
		
		assertNotNull(tryStmt.getFinallyClause());
	}
	
	public void testContinue() {
		ASContinueStatement continueStmt = meth.newContinue();
		assertNotNull(continueStmt);
		ExtraAssertions.assertInstanceof(meth.addStmt("continue"), ASContinueStatement.class);
	}

	public void testThrow() {
		Expression t = fact.newExpression("new Error()");
		ASThrowStatement throwStmt = meth.newThrow(t);
		ExtraAssertions.assertInstanceof(throwStmt.getExpression(), ASNewExpression.class);
	}

	public void testParseThrow() {
		ASThrowStatement throwStmt = (ASThrowStatement)meth.addStmt("throw \"oops\"");
		ExtraAssertions.assertInstanceof(throwStmt.getExpression(), ASStringLiteral.class);
	}
}
