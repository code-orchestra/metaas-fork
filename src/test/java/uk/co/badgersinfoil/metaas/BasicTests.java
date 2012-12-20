package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASArg;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASField;
import uk.co.badgersinfoil.metaas.dom.ASForStatement;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class BasicTests extends TestCase {
	public void testBasic() throws IOException {
		String source =
			"/*fpkg*/package foo.bar {" +
				" import pkg.Stuff;" +
				" /**\n" +
				"  * java doc\n" +
				"  */" +
				" public class /*f*/Blat extends Bing implements Febraz, Goo {" +
					" [Annotate]" +
					" [Basic()]" +
					" [String(\"foo\")]" +
					" [Num(1)]" +
					" [Bool(false)]" +
					" [Arg(foo=\"bar\")]" +
					" [List(foo=\"bar\", that=2)]" +
					" [Event(\"alpha\")]" +
					" [Event(\"beta\")]" +
					" private var x:String = 1;" +
					" /** javadoc? */" +
					" public static function func(arg:Number, foo=null, ...):Boolean {" +
						" for (var b=1;b<=10;b++) { bar(); }" +
						" for (var g in blah) { r(); }" +
						" for each (var g in blah) { r(); }" +
						" if (h==undefined) { poo(); } else { /* bar */ }" +
						" while (false) v();" +
						" do { continue; } while (m);" +
						" switch (blah) {" +
							" case 0x3: x(); y(); break;" +
							" case 4: next;" +
							" default: x(); y();" +
						" }" +
						" with (scope) foo();" +
						" var a=b?(x+y):new Foo();" +
						" const X=true;" +
						" ff = function(y, z) { };" +
						" l=[1,'2'];" +
						" xx = doc.ns::name;" +
						" m={a:null};" +
						" d = <foo bar=\"{blat}\"/>;" +
						" r = /regexp/;" +
						" /* unary expressions */" +
						" --a; ++a; a--; a++; a = -a;" +
						" return a.call(i.j*k, l[m]);" +
					" }" +
				" }" +
			" } // trailing comment";
		StringReader reader = new StringReader(source);

		ActionScriptFactory fact = new ActionScriptFactory();
		ASCompilationUnit unit = fact.newParser().parse(reader);

		// check that everything looks as it should,
		assertEquals("foo.bar", unit.getPackageName());

		ASPackage pkg = unit.getPackage();
		assertEquals("foo.bar", pkg.getName());
		List imports = pkg.findImports();
		assertEquals(1, imports.size());
		assertEquals("pkg.Stuff", imports.get(0));
		
		ASClassType clazz = (ASClassType)unit.getType();
		assertEquals("Blat", clazz.getName());
		assertEquals("Bing", clazz.getSuperclass());
		assertEquals(Arrays.asList(new String[] {"Febraz", "Goo"}),
		             clazz.getImplementedInterfaces());
		assertEquals("\n java doc\n", clazz.getDocComment());
		assertEquals(1, clazz.getMethods().size());

		ASField x = clazz.getField("x");
		assertEquals("x", x.getName());
		assertEquals("String", x.getType().getName());
		assertEquals(Visibility.PRIVATE, x.getVisibility());
		
		assertNotNull(x.getFirstMetatag("Annotate"));

		ASMethod func = clazz.getMethod("func");
		assertEquals("func", func.getName());
		assertEquals("Boolean", func.getType().getName());
		assertTrue(func.isStatic());
		assertEquals(Visibility.PUBLIC, func.getVisibility());
		assertEquals(ASMethod.AccessorRole.NORMAL_METHOD,
		             func.getAccessorRole());
		List args = func.getArgs();
		assertEquals(3, args.size());
		ASArg arg0 = (ASArg)args.get(0);
		assertEquals("arg", arg0.getName());
		assertEquals("Number", arg0.getType().getName());
		ASArg arg1 = (ASArg)args.get(1);
		assertEquals("foo", arg1.getName());
		assertNull(arg1.getType().getName());
		assertEquals(" javadoc? ", func.getDocComment());
		ASArg arg2 = (ASArg)args.get(2);
		assertTrue(arg2.isRest());
		assertEquals("...", arg2.getName());
		assertNull(arg2.getType().getName());

		// compare the re-serialised code with the origional input,
		StringWriter writer = new StringWriter();
		fact.newWriter().write(writer, unit);
		assertEquals(source.trim(), writer.toString());
		
		List stats = func.getStatementList();
		assertTrue(stats.get(0) instanceof ASForStatement);
	}
	
	public void testInvalidInput() {
		StringReader reader = new StringReader("(*H!D)(&H!)D&*GH");

		ActionScriptFactory fact = new ActionScriptFactory();
		try {
			fact.newParser().parse(reader);
			fail("Should raise exception on invalid input");
		} catch (SyntaxException e) {
			// expected
		}
	}
}