package uk.co.badgersinfoil.metaas.impl;

import java.io.Reader;
import java.io.StringReader;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import junit.framework.TestCase;


public class ActionScriptWalkerTest extends TestCase {
	public void testNull() {
		ASWalker walker = new ASWalker(new ASVisitor.Null());
		walker.walk(createTestProject());
	}

	private ActionScriptProject createTestProject() {
		ActionScriptFactory fact = new ActionScriptFactory();
		ActionScriptProject proj = fact.newEmptyASProject(".");

		String source =
			"package foo.bar {" +
			"  class Blat {" +
			"   private var field1;" +
			"  }" +
			" }";
		Reader in = new StringReader(source);
		ASCompilationUnit cu = fact.newParser().parse(in);
		proj.addCompilationUnit(cu);

		source =
			"package {" +
			"  interface Bar {" +
			"   public function meth1();" +
			"  }" +
			" }";
		in = new StringReader(source);
		cu = fact.newParser().parse(in);
		proj.addCompilationUnit(cu);

		return proj;
	}
}
