package uk.co.badgersinfoil.metaas;

import java.io.IOException;

import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import uk.co.badgersinfoil.metaas.dom.ASPackage;
import junit.framework.TestCase;


public class ImportsTests extends TestCase {
	private ActionScriptFactory fact = new ActionScriptFactory();

	public void testImports() throws IOException {
		ASCompilationUnit unit = fact.newClass("Test");

		ASPackage pkg = unit.getPackage();
		assertEquals(0, pkg.findImports().size());

		pkg.addImport("java.util.List");
		assertEquals(1, pkg.findImports().size());
		assertEquals("java.util.List", pkg.findImports().get(0));

		pkg.addImport("junit.framework.*");
		assertEquals(2, pkg.findImports().size());
		assertEquals("java.util.List", pkg.findImports().get(0));
		assertEquals("junit.framework.*", pkg.findImports().get(1));
		
		assertTrue(pkg.removeImport("java.util.List"));
		assertEquals(1, pkg.findImports().size());
		assertEquals("junit.framework.*", pkg.findImports().get(0));
		
		assertFalse(pkg.removeImport("missing"));

		assertTrue(pkg.removeImport("junit.framework.*"));
		assertEquals(0, pkg.findImports().size());
	}
	
	public void testInvalidImport() {
		ASCompilationUnit unit = fact.newClass("Test");

		ASPackage pkg = unit.getPackage();
		
		try {
			pkg.addImport("--invalid code--");
			fail("should have failed on invalid import");
		} catch (SyntaxException e) {
			// expected
		}
	}
}
