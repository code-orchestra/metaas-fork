package uk.co.badgersinfoil.metaas.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.TestCase;

public class AS3FragmentParserTest extends TestCase {

	public void testParseStatement() throws Exception {
		assertParseMethodFailsFor("parseStatement", "+-");
	}

	public void testParseExprStatement() throws Exception {
		assertParseMethodFailsFor("parseExprStatement", "");
	}

	public void testParseCondition() throws Exception {
		assertParseMethodFailsFor("parseCondition", "");
	}

	public void testParseExpr() throws Exception {
		assertParseMethodFailsFor("parseExpr", "");
	}

	public void testParseExprList() throws Exception {
		assertParseMethodFailsFor("parseExprList", "");
	}

	public void testParseIdent() throws Exception {
		assertParseMethodFailsFor("parseIdent", "");
	}

	public void testParseParameterDefault() throws Exception {
		assertParseMethodFailsFor("parseParameterDefault", "");
	}

	public void testParseForInit() throws Exception {
		assertParseMethodFailsFor("parseForInit", "+-");
		assertParseMethodFailsFor("parseForInit", "class");
	}

	public void testParseForCond() throws Exception {
		assertParseMethodFailsFor("parseForCond", "+-");
		assertParseMethodFailsFor("parseForCond", "class");
	}

	public void testParseForIter() throws Exception {
		assertParseMethodFailsFor("parseForIter", "+-");
		assertParseMethodFailsFor("parseForIter", "class");
	}

	public void testParseImport() throws Exception {
		assertParseMethodFailsFor("parseImport", "");
	}

	public void testParseTypeSpec() throws Exception {
		assertParseMethodFailsFor("parseTypeSpec", "");
		assertParseMethodFailsFor("parseTypeSpec", "String*");
	}

	public void testParseForInVar() throws Exception {
		assertParseMethodFailsFor("parseForInVar", "");
	}

	public void testParseForInIterated() throws Exception {
		assertParseMethodFailsFor("parseForInIterated", "");
	}

	public void testParseVariableDeclarator() throws Exception {
		assertParseMethodFailsFor("parseVariableDeclarator", "");
	}

	private static void assertParseMethodFailsFor(String methodName, String input) throws Exception {
		Method meth = AS3FragmentParser.class.getMethod(methodName, new Class[] { String.class });
		try {
			meth.invoke(null, new Object[] { input });
			fail(methodName+"(\""+input+"\") should fail");
		} catch (InvocationTargetException e) {
			// expected
		}
	}
}
