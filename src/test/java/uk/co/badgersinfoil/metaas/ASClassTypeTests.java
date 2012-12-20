package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class ASClassTypeTests extends TestCase {

	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASClassType clazz;

	protected void setUp() {
		ActionScriptFactory fact = new ActionScriptFactory();
		unit = fact.newClass("Test");
		clazz = (ASClassType)unit.getType();
	}

	protected void tearDown() throws IOException {
		CodeMirror.assertReflection(fact, unit);
	}

	public void testName() {
		assertEquals("Test", clazz.getName());
		clazz.setName("Another");
		assertEquals("Another", clazz.getName());
	}

	public void testMethods() {
		ASMethod foo = clazz.newMethod("foo", Visibility.PUBLIC, null);
		clazz.newMethod("removeme", Visibility.PUBLIC, null);
		clazz.newMethod("bar", Visibility.PUBLIC, null);

		clazz.removeMethod("removeme");

		List methods = clazz.getMethods();
		assertEquals(2, methods.size());
		ASMethod meth0 = (ASMethod)methods.get(0);
		assertEquals("foo", meth0.getName());
		ASMethod meth1 = (ASMethod)methods.get(1);
		assertEquals("bar", meth1.getName());

		assertEquals(foo.getName(), clazz.getMethod("foo").getName());
		assertNull(clazz.getMethod("missing"));
		
		// no exception should be raised,
		clazz.removeMethod("missing");
	}

	public void testFields() {
		clazz.newField("foo", Visibility.PUBLIC, null);
		clazz.newField("removeme", Visibility.PUBLIC, null);
		clazz.newField("bar", Visibility.PRIVATE, "Boolean");

		assertEquals("foo", clazz.getField("foo").getName());

		assertEquals(3, clazz.getFields().size());
		clazz.removeField("removeme");

		List fields = clazz.getFields();
		assertEquals(2, fields.size());

		ASField field0 = (ASField)fields.get(0);
		assertEquals("foo", field0.getName());
		ASField field1 = (ASField)fields.get(1);
		assertEquals("bar", field1.getName());
		
		assertEquals(null, clazz.getField("missing"));
		
		// check no exception raised,
		clazz.removeField("missing");
	}

	public void testSuper() throws IOException {
		assertNull(clazz.getSuperclass());
		clazz.setSuperclass("com.example.SuperTest");
		assertEquals("com.example.SuperTest", clazz.getSuperclass());
		// replace the original type name,
		clazz.setSuperclass("SuperTest");
		CodeMirror.assertReflection(fact, unit);
		clazz.setSuperclass(null);
		assertNull(clazz.getSuperclass());
	}

	public void testImplements() {
		assertEquals(0, clazz.getImplementedInterfaces().size());
		clazz.addImplementedInterface("InterfaceTestA");
		clazz.addImplementedInterface("InterfaceTestB");
		clazz.addImplementedInterface("pkg.InterfaceTestC");

		assertEquals(3, clazz.getImplementedInterfaces().size());
		clazz.removeImplementedInterface("InterfaceTestB");

		List interfaces = clazz.getImplementedInterfaces();
		assertEquals(2, interfaces.size());
		String interface0 = (String)interfaces.get(0);
		assertEquals("InterfaceTestA", interface0);
		String interface1 = (String)interfaces.get(1);
		assertEquals("pkg.InterfaceTestC", interface1);
	}
	
	public void testRemoveImplements() {
		clazz.addImplementedInterface("InterfaceTest");
		clazz.removeImplementedInterface("InterfaceTest");
		// tearDown() will assert that the code still parses, which
		// it shouldn't do if the inteface name is removed, but the
		// 'implements' keyword isn't.
	}

	public void testDocComment() {
		String comment = "\nfoo\n bar\n\n";
		clazz.setDocComment(comment);
		assertEquals(comment, clazz.getDocComment());
	}
	
	public void testPackage() {
		assertNull(unit.getPackageName());
		unit.setPackageName("test.foo");
		assertEquals("test.foo", unit.getPackageName());
		unit.setPackageName("test.bar");
		assertEquals("test.bar", unit.getPackageName());
		unit.setPackageName(null);
		assertNull(unit.getPackageName());
	}
	
	public void testVisibility() {
		assertEquals(Visibility.PUBLIC, clazz.getVisibility());
		clazz.setVisibility(Visibility.DEFAULT);
		assertEquals(Visibility.DEFAULT, clazz.getVisibility());
	}

	public void testDynamic() {
		assertFalse(clazz.isDynamic());
		clazz.setDynamic(false);
		// set false when already false,
		assertFalse(clazz.isDynamic());
		clazz.setDynamic(true);
		assertTrue(clazz.isDynamic());
		// set true when already true,
		clazz.setDynamic(true);
		assertTrue(clazz.isDynamic());
	}

	public void testFinal() {
		assertFalse(clazz.isFinal());
		clazz.setFinal(false);
		// set false when already false,
		assertFalse(clazz.isFinal());
		clazz.setFinal(true);
		assertTrue(clazz.isFinal());
		// set true when already true,
		clazz.setFinal(true);
		assertTrue(clazz.isFinal());
	}

	public void testRemoveModifiers() {
		// set the 'final' keyword
		clazz.setFinal(true);
		// remove the 'public' keyword added by default in setUp()
		clazz.setVisibility(Visibility.DEFAULT);
		// now, test removing the 'final' flag, which should also be
		// the only remaining modifier,
		clazz.setFinal(false);
	}
}