/*
 * ASTASProject.java
 * 
 * Copyright (c) 2006-2007 David Holroyd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.badgersinfoil.metaas.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import uk.co.badgersinfoil.metaas.ActionScriptProject;
import uk.co.badgersinfoil.metaas.ActionScriptFactory;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;

// TODO: some checks that types are not created that clash with existing types,
// or at least filenames wont collide in the outputDir, would be good.

public class ASTActionScriptProject implements ActionScriptProject {
	private List classpath = new ArrayList();
	private Map resourceRoots = new HashMap();
	private List compilationUnits = new ArrayList();
	private String outputLocation;
	private ActionScriptFactory fact;

	public ASTActionScriptProject(ActionScriptFactory fact) {
		this.fact = fact;
	}

	// TODO: interface for cp entries; not just strings.  Add support for
	//       .swc classpath entries
	public void addClasspathEntry(String classpathEntry) {
		ResourceRoot root = resourceRootFor(classpathEntry);
		resourceRoots.put(classpathEntry, root);
		classpath.add(classpathEntry);
	}

	private ResourceRoot resourceRootFor(String classpathEntry) {
		File path = new File(classpathEntry);
		if (path.isDirectory()) {
			return new SourceFolderResourceRoot(path);
		}
		if (classpathEntry.endsWith(".swc")) {
			try {
				return new SWCResourceRoot(classpathEntry);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		throw new IllegalArgumentException("Unknown resorce type: "+classpathEntry);
	}
	
	/**
	 * Returns a list of ResourceRoot implementations pertaining to the
	 * classpath entries that have been added to this project.
	 */
	public Collection getResourceRoots() {
		return resourceRoots.values();
	}

	public void removeClasspathEntry(String classpathEntry) {
		classpath.remove(classpathEntry);
		resourceRoots.remove(classpathEntry);
	}

	public List getClasspathEntries() {
		return Collections.unmodifiableList(classpath);
	}

	public void addCompilationUnit(ASCompilationUnit cu) {
		compilationUnits.add(cu);
	}

	public void removeCompilationUnit(ASCompilationUnit cu) {
		compilationUnits.remove(cu);
	}
	public ASCompilationUnit newClass(String qualifiedClassName) {
		ASCompilationUnit cu = fact.newClass(qualifiedClassName);
		addCompilationUnit(cu);
		return cu;
	}
	public ASCompilationUnit newInterface(String qualifiedInterfaceName) {
		ASCompilationUnit cu = fact.newInterface(qualifiedInterfaceName);
		addCompilationUnit(cu);
		return cu;
	}

	public List getCompilationUnits() {
		return Collections.unmodifiableList(compilationUnits);
	}

	public String getOutputLocation() {
		return outputLocation;
	}

	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
	}

	public void writeAll() throws IOException {
		for (Iterator i=compilationUnits.iterator(); i.hasNext(); ) {
			ASCompilationUnit cu = (ASCompilationUnit)i.next();
			write(outputLocation, cu);
		}
	}

	public void performAutoImport() {
		AutoImporter autoImporter = new AutoImporter();
		autoImporter.performAutoImport(this);
	}

	/**
	 * Writes the ActionScript code in the given CompilationUnit to the
	 * given directory, creating any subfolders for package hierarchy as
	 * appropriate, and deriving the filename from the name of the type
	 * defined by the compilation unit.
	 */
	private void write(String destinationDir, ASCompilationUnit cu) throws IOException {
		String filename = filenameFor(cu);
		File destFile = new File(destinationDir, filename);
		destFile.getParentFile().mkdirs();
		FileOutputStream os = new FileOutputStream(destFile);
		OutputStreamWriter out = new OutputStreamWriter(os);
		fact.newWriter().write(out, cu);
		out.close();
	}

	private static String filenameFor(ASCompilationUnit unit) {
		String name;
		String pkg = unit.getPackageName();
		if (pkg == null || pkg.equals("")) {
			name = unit.getType().getName();
		} else {
			name = unit.getPackageName() + "." + unit.getType().getName();
		}
		return name.replace('.', File.separatorChar) + ".as";
	}
}