package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASInterfaceType;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class ASInterfaceTypeTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();
	private ASCompilationUnit unit;
	private ASInterfaceType iface;

	protected void setUp() {
		ActionScriptFactory fact = new ActionScriptFactory();
		unit = fact.newInterface("Test");
		iface = (ASInterfaceType)unit.getType();
	}

	protected void tearDown() throws IOException {
		CodeMirror.assertReflection(fact, unit);
	}
	
	public void testParse() throws IOException {
		String source =
			"package foo.bar {" +
				" public interface Blat extends Bing,Bong {" +
					" public static function func(arg:Number, foo):Boolean;" +
				" }" +
			" }";
		StringReader reader = new StringReader(source);

		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = fact.newParser().parse(reader);

		// check that everything looks as it should,
		assertEquals("foo.bar", unit.getPackageName());
		ASInterfaceType iface = (ASInterfaceType)unit.getType();
		assertEquals("Blat", iface.getName());
		
		assertEquals(Arrays.asList(new String[] {"Bing", "Bong"}),
		             iface.getSuperInterfaces());

		ASMethod meth = iface.getMethod("func");
		assertNotNull(meth);
		assertEquals("func", meth.getName());

		// compare the re-serialised code with the original input,
		StringWriter writer = new StringWriter();
		fact.newWriter().write(writer, unit);
		assertEquals(source, writer.toString());
	}

	public void testName() {
		assertEquals("Test", iface.getName());
		iface.setName("Another");
		assertEquals("Another", iface.getName());
	}

	public void testMethods() {
		ASMethod foo = iface.newMethod("foo", Visibility.PUBLIC, null);
		iface.newMethod("removeme", Visibility.PUBLIC, null);
		iface.newMethod("bar", Visibility.PUBLIC, null);

		iface.removeMethod("removeme");

		List methods = iface.getMethods();
		assertEquals(2, methods.size());
		ASMethod meth0 = (ASMethod)methods.get(0);
		assertEquals("foo", meth0.getName());
		ASMethod meth1 = (ASMethod)methods.get(1);
		assertEquals("bar", meth1.getName());

		assertEquals(foo.getName(), iface.getMethod("foo").getName());

        /*
		try {
			meth1.addStmt("helloWorld()");
			fail("adding code to interface method should fail");
		} catch (SyntaxException e) {
			// expected
		}
		*/
	}

	public void testSuper() {
		assertEquals(0, iface.getSuperInterfaces().size());
		iface.addSuperInterface("InterfaceTestA");
		iface.addSuperInterface("InterfaceTestB");
		iface.addSuperInterface("pkg.InterfaceTestC");

		assertEquals(3, iface.getSuperInterfaces().size());
		iface.removeSuperInterface("InterfaceTestB");

		List interfaces = iface.getSuperInterfaces();
		assertEquals(2, interfaces.size());
		String interface0 = (String)interfaces.get(0);
		assertEquals("InterfaceTestA", interface0);
		String interface1 = (String)interfaces.get(1);
		assertEquals("pkg.InterfaceTestC", interface1);
	}
	
	public void testReplaceSuper() {
		String source =
			"package foo.bar {" +
				" public interface Blat extends com.example.Bing {" +
				" }" +
			" }";
		StringReader reader = new StringReader(source);

		unit = fact.newParser().parse(reader);
		ASInterfaceType iface = (ASInterfaceType)unit.getType();
		assertEquals("com.example.Bing", iface.getSuperInterfaces().get(0));
		iface.removeSuperInterface("com.example.Bing");
		iface.addSuperInterface("Bing");
	}

	public void testPackage() {
		unit.setPackageName("testpkg");
	}
}