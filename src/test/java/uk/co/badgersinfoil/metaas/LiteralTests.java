package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import uk.co.badgersinfoil.metaas.dom.ASArrayLiteral;
import uk.co.badgersinfoil.metaas.dom.ASAssignmentExpression;
import uk.co.badgersinfoil.metaas.dom.ASBooleanLiteral;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.Literal;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.ASNullLiteral;
import uk.co.badgersinfoil.metaas.dom.ASObjectLiteral;
import uk.co.badgersinfoil.metaas.dom.ASRegexpLiteral;
import uk.co.badgersinfoil.metaas.dom.ASStringLiteral;
import uk.co.badgersinfoil.metaas.dom.ASIntegerLiteral;
import uk.co.badgersinfoil.metaas.dom.ASUndefinedLiteral;
import uk.co.badgersinfoil.metaas.dom.ASXMLLiteral;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;

public class LiteralTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASCompilationUnit reflect;
	private Literal literal = null;

	protected void setUp() {
		unit = fact.newClass("Test");
		reflect = null;
		literal = null;
	}

	protected void tearDown() throws IOException {
		if (literal != null && reflect == null) {
			ASClassType clazz = (ASClassType)unit.getType();
			ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, null);
			meth.newExprStmt(fact.newAssignExpression(fact.newExpression(getName()), literal));
			reflect = assertReflection();
		}
	}

	private ASCompilationUnit assertReflection() throws IOException {
		return CodeMirror.assertReflection(fact, unit);
	}

	public void testStringLiteral() {
		ASStringLiteral lit = fact.newStringLiteral("");
		assertEquals("", lit.getValue());
		lit.setValue("\"");
		assertEquals("\"", lit.getValue());
		literal = lit;
	}

	public void testNumberLiteral() {
		ASIntegerLiteral lit = fact.newIntegerLiteral(123);
		assertEquals(123, lit.getValue());
		lit.setValue(0);
		assertEquals(0, lit.getValue());
		literal = lit;
	}

	public void testNullLiteral() {
		ASNullLiteral lit = fact.newNullLiteral();
		assertNotNull(lit);
		literal = lit;
	}

	public void testParseNullLiteral() {
		ASNullLiteral lit = (ASNullLiteral)fact.newExpression("null");
		assertNotNull(lit);
		literal = lit;
	}

	public void testBoolLiteral() {
		ASBooleanLiteral lit = fact.newBooleanLiteral(true);
		assertTrue(lit.getValue());
		lit.setValue(false);
		assertFalse(lit.getValue());

		lit = fact.newBooleanLiteral(false);
		assertFalse(lit.getValue());
		lit.setValue(true);
		assertTrue(lit.getValue());
		
		ExtraAssertions.assertInstanceof(fact.newExpression("true"),
		                                 ASBooleanLiteral.class);
		literal = lit;
	}

	public void testUndefinedLiteral() {
		ASUndefinedLiteral lit = fact.newUndefinedLiteral();
		assertNotNull(lit);
		literal = lit;
	}

	public void testParseUndefinedLiteral() {
		ASUndefinedLiteral lit = (ASUndefinedLiteral)fact.newExpression("undefined");
		assertNotNull(lit);
		literal = lit;
	}

	public void testEmptyArrayLiteral() {
		ASArrayLiteral lit = fact.newArrayLiteral();
		assertNotNull(lit);
		ExtraAssertions.assertSize(0, lit.getEntries());
		literal = lit;
	}

	public void testArrayLiteral() {
		ASArrayLiteral lit = fact.newArrayLiteral();
		lit.add(fact.newIntegerLiteral(1));
		ExtraAssertions.assertSize(1, lit.getEntries());
		ExtraAssertions.assertInstanceof(lit.getEntries().get(0), ASIntegerLiteral.class);
		lit.add(fact.newStringLiteral("foo"));
		ExtraAssertions.assertSize(2, lit.getEntries());
		ExtraAssertions.assertInstanceof(lit.getEntries().get(1), ASStringLiteral.class);
		literal = lit;
	}

	public void testArrayLiteralRemoveFirst() {
		ASArrayLiteral lit = fact.newArrayLiteral();
		lit.add(fact.newIntegerLiteral(1));
		lit.add(fact.newStringLiteral("foo"));
		lit.remove(0);
		ExtraAssertions.assertSize(1, lit.getEntries());
		literal = lit;
	}

	public void testArrayLiteralRemoveLast() {
		ASArrayLiteral lit = fact.newArrayLiteral();
		lit.add(fact.newIntegerLiteral(1));
		lit.add(fact.newStringLiteral("foo"));
		lit.remove(1);
		ExtraAssertions.assertSize(1, lit.getEntries());
		literal = lit;
	}

	public void testArrayLiteralRemoveMiddle() {
		ASArrayLiteral lit = fact.newArrayLiteral();
		lit.add(fact.newIntegerLiteral(1));
		lit.add(fact.newStringLiteral("foo"));
		lit.add(fact.newBooleanLiteral(false));
		lit.remove(1);
		ExtraAssertions.assertSize(2, lit.getEntries());
		literal = lit;
	}

	public void testParseArrayLiteral() {
		literal = (ASArrayLiteral)fact.newExpression("['foo', [1]]");
		assertNotNull(literal);
	}

	public void testEmptyObjectLiteral() {
		ASObjectLiteral lit = fact.newObjectLiteral();
		assertNotNull(lit);
		ExtraAssertions.assertSize(0, lit.getFields());
		literal = lit;
	}

	public void testObjectLiteral() {
		ASObjectLiteral lit = fact.newObjectLiteral();
		ASObjectLiteral.Field field = lit.newField("foo", fact.newIntegerLiteral(33));
		assertEquals("foo", field.getName());
		ExtraAssertions.assertInstanceof(field.getValue(), ASIntegerLiteral.class);
		field = lit.newField("bar", fact.newStringLiteral("hello"));
		literal = lit;
	}
	
	public void testParseObjectLiteral() {
		ASAssignmentExpression expr = (ASAssignmentExpression)fact.newExpression("test = {foo:2}");
		literal = (Literal)expr.getRightSubexpression();
	}

	public void testXMLLiteral() {
		ASXMLLiteral lit = fact.newXMLLiteral("<hello>world</hello>");
		assertNotNull(lit);
		literal = lit;
	}

	public void testParseXMLLiteral() {
		ASXMLLiteral lit = (ASXMLLiteral)fact.newExpression("<hello>world</hello>");
		assertNotNull(lit);
		literal = lit;
	}

	public void testRegexpLiteral() {
		ASRegexpLiteral lit = fact.newRegexpLiteral("\\d+", ASRegexpLiteral.FLAG_NONE);
		assertNotNull(lit);
		literal = lit;
	}

	public void testParseRegexpLiteral() {
		ASRegexpLiteral lit = (ASRegexpLiteral)fact.newExpression("/[a-z]+-\\d+/i");
		assertNotNull(lit);
		literal = lit;
	}
}
