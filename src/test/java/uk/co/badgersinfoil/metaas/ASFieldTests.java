package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASIntegerLiteral;
import uk.co.badgersinfoil.metaas.dom.ASStringLiteral;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class ASFieldTests extends TestCase {

	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASField field;

	protected void setUp() {
		unit = fact.newClass("Test");
		ASClassType clazz = (ASClassType)unit.getType();
		field = clazz.newField("test", Visibility.PUBLIC, "Bar");
	}

	protected void tearDown() throws IOException {
		CodeMirror.assertReflection(fact, unit);
	}

	public void testName() {
		assertEquals("test", field.getName());
		field.setName("foobar");
		assertEquals("foobar", field.getName());
		try {
			field.setName("bad.name");
			fail("should not have accepted field name containing '.'");
		} catch (SyntaxException e) {
			// expected
		}
	}

	public void testStatic() {
		assertFalse("new fields should be non-static by default", field.isStatic());
		field.setStatic(false);
		assertFalse("seting non-static when already non-static should be ok", field.isStatic());
		field.setStatic(true);
		assertTrue("changing to static failed", field.isStatic());
		field.setStatic(true);
		assertTrue("static static when already static didn't work", field.isStatic());
		field.setStatic(false);
		assertFalse("removing static again didn't work", field.isStatic());
	}

	public void testInit() throws IOException {
		// should no none to start with,
		assertNull(field.getInitializer());
		
		field.setInitializer(fact.newStringLiteral("foo"));
		assertTrue(field.getInitializer() instanceof ASStringLiteral);

		field.setInitializer("1");
		assertTrue(field.getInitializer() instanceof ASIntegerLiteral);

		field.setInitializer((String)null);  // remove it again
		assertNull(field.getInitializer());

		field.setInitializer((Expression)null);  // when already absent
		

		// complicated initialiser value,
		field.setInitializer("function() { trace('test'); }");
		CodeMirror.assertReflection(fact, unit);
	}

	public void testRenameWithInit() {
		field.setInitializer(fact.newStringLiteral("foo"));
		assertTrue(field.getInitializer() instanceof ASStringLiteral);

		field.setName("foo");
		// renaming the field should cause the initialiser to vanish
		assertNotNull(field.getInitializer());
	}

	public void testDocComment() {
		// initial add,
		String comment = "foo\n bar";
		field.setDocComment(comment);
		assertEquals(comment, field.getDocComment());

		// modify,
		comment = "blat";
		field.setDocComment(comment);
		assertEquals(comment, field.getDocComment());

		// delete,
		field.setDocComment(null);
		assertNull(field.getDocComment());
	}
	
	public void testType() {
		assertEquals("Bar", field.getType().getName());
		// overwrite the old type,
		field.setType("Foo");
		assertEquals("Foo", field.getType().getName());

		field.setType(null);
		assertNull(field.getType());
		field.setType(null);  // when already absent

		field.setType("Foo");
		assertEquals("Foo", field.getType().getName());
	}
	
	public void testConst() {
		assertFalse(field.isConst());
		field.setConst(true);
		assertTrue(field.isConst());
		field.setConst(false);
		assertFalse(field.isConst());

		// set it to the value it already has,
		field.setConst(false);
		assertFalse(field.isConst());
	}

	public void testConstSerialisation() throws IOException {
		field.setConst(true);
		field.setName("renamed");
		unit = CodeMirror.assertReflection(fact, unit);
		field = ((ASClassType)unit.getType()).getField("renamed");
		assertNotNull(field);
		assertTrue(field.isConst());
	}
}