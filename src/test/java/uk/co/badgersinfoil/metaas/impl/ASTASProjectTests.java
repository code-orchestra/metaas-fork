package uk.co.badgersinfoil.metaas.impl;

import java.util.Collections;
import java.util.List;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;
import junit.framework.TestCase;

public class ASTASProjectTests extends TestCase {
	ActionScriptProject project;

	public void setUp() {
		project = new ActionScriptFactory().newEmptyASProject(".");
		assertEquals(".", project.getOutputLocation());
	}

	public void testCompilationUnits() {
		assertEmpty(project.getCompilationUnits());
		ASCompilationUnit cu = project.newClass("Test");
		assertEquals(1, project.getCompilationUnits().size());
		project.removeCompilationUnit(cu);
		assertEmpty(project.getCompilationUnits());
		cu = project.newInterface("Test");
		assertEquals(1, project.getCompilationUnits().size());
	}

	private static void assertEmpty(List list) {
		assertEquals(Collections.EMPTY_LIST, list);
	}

	public void testClasspath() {
		assertEmpty(project.getClasspathEntries());
		project.addClasspathEntry(".");
		assertEquals(1, project.getClasspathEntries().size());
		project.removeClasspathEntry(".");
		assertEmpty(project.getClasspathEntries());
	}
}