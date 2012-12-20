package uk.co.badgersinfoil.metaas.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.CodeMirror;
import uk.co.badgersinfoil.metaas.dom.ASArg;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASInterfaceType;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import junit.framework.TestCase;


public class AutoImporterTest extends TestCase {
	public void testIt() throws IOException {
		AutoImporter autoimp = new AutoImporter();
		ActionScriptFactory fact = new ActionScriptFactory();
		ActionScriptProject proj = createProject(fact);
		autoimp.performAutoImport((ASTActionScriptProject)proj);
		checkProject(fact, proj);
	}

	private ActionScriptProject createProject(ActionScriptFactory fact) {
		ActionScriptProject proj = fact.newEmptyASProject(".");

		String source =
			"package pkg1 {" +
			" class Foo implements pkg2.Bar {" +
			"  public function meth1():pkg2.Bar {" +
			"  }" +
			" }" +
			" }";
		StringReader in = new StringReader(source);
		proj.addCompilationUnit(fact.newParser().parse(in));


		source =
			"package pkg2 {" +
			" interface Bar extends pkg1.Foo {" +
			"  public function meth2(a:pkg1.Foo):void;" +
			" }" +
			" }";
		in = new StringReader(source);
		proj.addCompilationUnit(fact.newParser().parse(in));

		return proj;
	}

	private void checkProject(ActionScriptFactory fact,
	                          ActionScriptProject proj)
		throws IOException
	{
		List units = proj.getCompilationUnits();
		for (Iterator i=units.iterator(); i.hasNext(); ) {
			ASCompilationUnit unit = (ASCompilationUnit)i.next();
			if (unit.getPackageName().equals("pkg1")) {
				checkPkg1(CodeMirror.assertReflection(fact, unit));
			} else if (unit.getPackageName().equals("pkg2")) {
				checkPkg2(CodeMirror.assertReflection(fact, unit));
			}
		}
	}

	private void checkPkg1(ASCompilationUnit unit) {
		ASPackage pkg1 = unit.getPackage();
		List imports = pkg1.findImports();
		assertTrue(imports.contains("pkg2.Bar"));
		assertEquals("Bar", ((ASClassType)pkg1.getType()).getImplementedInterfaces().get(0));
		ASMethod meth1 = pkg1.getType().getMethod("meth1");
		assertEquals("Bar", meth1.getType().getName());
	}

	private void checkPkg2(ASCompilationUnit unit) {
		ASPackage pkg2 = unit.getPackage();
		List imports = pkg2.findImports();
		assertTrue(imports.contains("pkg1.Foo"));
		assertEquals("Foo", ((ASInterfaceType)pkg2.getType()).getSuperInterfaces().get(0));
		ASMethod meth2 = pkg2.getType().getMethod("meth2");
		ASArg a = (ASArg)meth2.getArgs().get(0);
		assertEquals("Foo", a.getType().getName());
	}
}
