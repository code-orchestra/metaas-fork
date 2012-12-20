package uk.co.badgersinfoil.metaas;

import java.io.File;
import java.io.IOException;
import uk.co.badgersinfoil.metaas.dom.ASClassType;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASMethod;
import uk.co.badgersinfoil.metaas.dom.Visibility;
import junit.framework.TestCase;


public class ActionScriptProjectTest extends TestCase {
	private File tmpDir;

	public void setUp() throws IOException {
		tmpDir = new File(System.getProperty("java.io.tmpdir"), "metaas-test");
		tmpDir.mkdir();
	}

	public void tearDown() {
		// delete files (assumes no subdirs created),
		File[] files = tmpDir.listFiles();
		for (int i=0; i<files.length; i++) {
			files[i].delete();
		}
		// delete the folder itself,
		tmpDir.delete();
	}

	public void testIt() throws IOException {
		ActionScriptFactory fact = new ActionScriptFactory();
		ActionScriptProject proj = fact.newEmptyASProject(tmpDir.getPath());
		ASCompilationUnit unit = proj.newClass("Test");
		ASClassType clazz = (ASClassType)unit.getType();
		ASMethod meth = clazz.newMethod("test", Visibility.PUBLIC, "void");
		meth.addStmt("trace('Hello world')");
		proj.writeAll();
		
		File expected = new File(tmpDir, "Test.as");
		assertTrue(expected.exists());
	}
}