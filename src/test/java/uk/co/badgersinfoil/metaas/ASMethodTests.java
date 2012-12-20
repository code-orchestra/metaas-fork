package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASArg;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class ASMethodTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASClassType clazz;
	private ASMethod meth;

	protected void setUp() {
		unit = fact.newClass("Test");
		clazz = (ASClassType)unit.getType();
		meth = clazz.newMethod("test", Visibility.PUBLIC, null);
	}

	protected void tearDown() throws IOException {
		CodeMirror.assertReflection(fact, unit);
	}

	public void testName() {
		assertEquals("test", meth.getName());
		meth.setName("foobar");
		assertEquals("foobar", meth.getName());
		try {
			meth.setName("bad.name");
			fail("should not have accepted method name containing '.'");
		} catch (SyntaxException e) {
			// expected
		}
		try {
			meth.setName("bad:name");
			fail("should not have accepted method name containing ':'");
		} catch (SyntaxException e) {
			// expected
		}
	}

	public void testStatic() {
		assertFalse("new methods should be non-static by default", meth.isStatic());
		meth.setStatic(false);
		assertFalse(meth.isStatic());
		meth.setStatic(true);
		assertTrue(meth.isStatic());
		meth.setStatic(true);
		assertTrue(meth.isStatic());
	}

	public void testAccessorRole() {
		assertEquals(ASMethod.AccessorRole.NORMAL_METHOD, meth.getAccessorRole());
		// now add a role,
		meth.setAccessorRole(ASMethod.AccessorRole.GETTER);
		assertEquals(ASMethod.AccessorRole.GETTER, meth.getAccessorRole());
		// change existing role to set
		meth.setAccessorRole(ASMethod.AccessorRole.SETTER);
		assertEquals(ASMethod.AccessorRole.SETTER, meth.getAccessorRole());
		// change existing role to get
		meth.setAccessorRole(ASMethod.AccessorRole.GETTER);
		assertEquals(ASMethod.AccessorRole.GETTER, meth.getAccessorRole());

		// check the AST internals didn't get mangled along the way,
		assertEquals("test", meth.getName());

		meth.setAccessorRole(ASMethod.AccessorRole.NORMAL_METHOD);
		assertEquals(ASMethod.AccessorRole.NORMAL_METHOD, meth.getAccessorRole());
		
		// now add a role back in, but this time as a setter
		meth.setAccessorRole(ASMethod.AccessorRole.SETTER);
		assertEquals(ASMethod.AccessorRole.SETTER, meth.getAccessorRole());

		// squeeze out the last bit of code coverage,
		assertEquals("NORMAL_METHOD", ASMethod.AccessorRole.NORMAL_METHOD.toString());
		assertEquals("GETTER", ASMethod.AccessorRole.GETTER.toString());
		assertEquals("SETTER", ASMethod.AccessorRole.SETTER.toString());
	}

	public void testReturnType() throws IOException {
		assertNull(meth.getType().getName());
		meth.setType("pkg.Clazz");
		assertEquals("pkg.Clazz", meth.getType().getName());
		// "void" is a keyword, rather than an IDENT
		meth.setType("void");
		assertEquals("void", meth.getType().getName());
		ASCompilationUnit reflectedUnit = CodeMirror.assertReflection(fact, unit);
		ASMethod reflectedMethod = reflectedUnit.getType().getMethod(meth.getName());
		assertEquals("void", reflectedMethod.getType().getName());
		meth.setType(null);
		assertNull(meth.getType().getName());
	}

	public void testProtection() {
		assertVisibility(Visibility.PUBLIC, meth);
		meth.setVisibility(Visibility.PRIVATE);
		assertVisibility(Visibility.PRIVATE, meth);
		meth.setVisibility(Visibility.PROTECTED);
		assertVisibility(Visibility.PROTECTED, meth);
		meth.setVisibility(Visibility.INTERNAL);
		assertVisibility(Visibility.INTERNAL, meth);
		meth.setVisibility(Visibility.PUBLIC);
		assertVisibility(Visibility.PUBLIC, meth);

		checkSetVisibilityAfterDefault(meth, Visibility.PRIVATE);
		checkSetVisibilityAfterDefault(meth, Visibility.PUBLIC);
		checkSetVisibilityAfterDefault(meth, Visibility.PROTECTED);
		checkSetVisibilityAfterDefault(meth, Visibility.INTERNAL);
		
		// see what happens when there is no visibility modifier, but
		// there *is* some other kind of modifier keyword,
		meth.setVisibility(Visibility.DEFAULT);
		meth.setStatic(true);
		assertVisibility(Visibility.DEFAULT, meth);

		meth.setVisibility(Visibility.PUBLIC);
		assertVisibility(Visibility.PUBLIC, meth);
		try {
			meth.setVisibility(null);
			fail("should reject 'null' visibility");
		} catch (Exception e) {
			// expected
		}

		// squeeze out the last bit of code coverage,
		assertEquals("public", Visibility.PUBLIC.toString());
		assertEquals("private", Visibility.PRIVATE.toString());
		assertEquals("protected", Visibility.PROTECTED.toString());
		assertEquals("internal", Visibility.INTERNAL.toString());
		assertEquals("[default]", Visibility.DEFAULT.toString());
	}
	
	private static void assertVisibility(Visibility expectedVisibility,
	                                     ASMethod method)
	{
		assertSame(expectedVisibility, method.getVisibility());
	}

	private static void checkSetVisibilityAfterDefault(ASMethod method,
	                                            Visibility visibility)
	{
		method.setVisibility(Visibility.DEFAULT);
		assertVisibility(Visibility.DEFAULT, method);
		method.setVisibility(visibility);
		assertVisibility(visibility, method);
	}
	
	public void testProtectionOnCreate() {
		meth = clazz.newMethod("testDefault", Visibility.DEFAULT, null);
		assertVisibility(Visibility.DEFAULT, meth);
		meth = clazz.newMethod("testPublic", Visibility.PUBLIC, null);
		assertVisibility(Visibility.PUBLIC, meth);
		meth = clazz.newMethod("testPrivate", Visibility.PRIVATE, null);
		assertVisibility(Visibility.PRIVATE, meth);
		meth = clazz.newMethod("testProtected", Visibility.PROTECTED, null);
		assertVisibility(Visibility.PROTECTED, meth);
		meth = clazz.newMethod("testInternal", Visibility.INTERNAL, null);
		assertVisibility(Visibility.INTERNAL, meth);
	}

	public void testArgs() throws IOException {
		ASArg foo = meth.addParam("foo", "Number");
		assertEquals("foo", foo.getName());
		assertEquals("Number", foo.getType().getName());
		foo.setDefault("null");
		foo.setDefault("1"); // reset existing value 
		assertEquals("1", foo.getDefaultString());
		foo.setDefault(null); // remove value
		try {
			foo.setDefault("]");
			fail("should have rejected invalid initialiser value");
		} catch (SyntaxException e) {
			// expected
		}
		assertNull(foo.getDefaultString());
		meth.addParam("bar", null);
		meth.addParam("blat", null);
		List args = meth.getArgs();
		assertEquals(3, args.size());
		ASArg arg0 = (ASArg)args.get(0);
		assertEquals("foo", arg0.getName());
		assertEquals("Number", arg0.getType().getName());
		assertEquals("foo:Number", arg0.toString());
		arg0.setType("String");
		assertEquals("String", arg0.getType().getName());
		arg0.setType(null);
		assertNull(arg0.getType().getName());
		assertEquals("foo", arg0.toString());
		ASArg arg1 = (ASArg)args.get(1);
		assertEquals("bar", arg1.getName());
		assertEquals("bar", arg1.toString());
		assertNull(arg1.getType().getName());
		ASArg arg2 = (ASArg)args.get(2);
		arg2.setType("*");
		assertEquals("*", arg2.getType().getName());
		assertEquals("bar", meth.removeParam("bar").getName());
		assertEquals(2, meth.getArgs().size());
		assertNull(meth.removeParam("missing"));
		assertEquals(2, meth.getArgs().size());

		ASCompilationUnit unit2 = CodeMirror.assertReflection(fact, unit);
		ASClassType type2 = (ASClassType)unit2.getType();
		ASMethod meth2 = type2.getMethod("test");
		List args2 = meth2.getArgs();
		ASArg bar2 = (ASArg)args2.get(1);
		assertEquals("blat", bar2.getName());
		assertEquals("*", bar2.getType().getName());
	}

	public void testRemoveLastParameter() {
		meth.addParam("foo", "Number");
		meth.addRestParam("bar");
		meth.removeParam("bar");
		assertEquals(1, meth.getArgs().size());
	}

	public void testRestParameter() {
		ASArg foo = meth.addParam("foo", "Number");
		assertFalse(foo.isRest());
		ASArg bar = meth.addRestParam("bar");
		assertTrue(bar.isRest());
		meth.removeParam("bar");
		assertEquals(1, meth.getArgs().size());
		ASArg rest = meth.addRestParam("..."); // no name
		assertEquals("...", rest.getName());
		meth.removeParam("...");
		assertEquals(1, meth.getArgs().size());

		rest = meth.addRestParam("...");
		try {
			rest.setDefault("12");
			fail("should not have been able to set a default value for a 'rest' parameter");
		} catch (SyntaxException e) {
			// expected
		}
		assertEquals(2, meth.getArgs().size());
	}

	public void testDocComment() {
		String comment = "foo\n bar";
		meth.setDocComment(comment);
		assertEquals(comment, meth.getDocComment());
	}
}