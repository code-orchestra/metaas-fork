/*
 * ActionScriptProject.java
 * 
 * Copyright (c) 2006-2008 David Holroyd
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

package uk.co.badgersinfoil.metaas;

import java.io.IOException;
import java.util.List;
import uk.co.badgersinfoil.metaas.dom.ASCompilationUnit;


/**
 * <p>A container for a set of ActionScript files.</p>
 * 
 * <p>An ActionScriptProject helps deal with groups of source files.  You can
 * use the project object to create a bunch of compilation units by calling
 * newClass() and newInterface() from disparate points in your code, and then
 * call writeAll() to save everything to your output location in one go when
 * you're done.</p>
 * 
 * <h3>Note</h3>
 * <p>{@link #writeAll()} performs postprocessing steps on the generated code.
 * At the moment this is not configurable.</p>
 */
public interface ActionScriptProject {
	public void setOutputLocation(String outputLocation);
	public String getOutputLocation();

	public void addClasspathEntry(String classpathEntry);
	public void removeClasspathEntry(String classpathEntry);
	public List getClasspathEntries();

	public void addCompilationUnit(ASCompilationUnit cu);
	public void removeCompilationUnit(ASCompilationUnit cu);
	public List getCompilationUnits();

	/**
	 * Shortcut for {@link ActionScriptFactory#newClass(String)} that also
	 * adds the resulting CompilationUnit to the list managed by this
	 * project.
	 */
	public ASCompilationUnit newClass(String qualifiedClassName);

	/**
	 * Shortcut for {@link ActionScriptFactory#newInterface(String)} that also
	 * adds the resulting CompilationUnit to the list managed by this
	 * project.
	 */
	public ASCompilationUnit newInterface(String qualifiedClassName);

	/**
	 * Writes all CompilationUnits that have been added to this project
	 * to the output location.
	 */
	public void writeAll() throws IOException;

	/**
	 * <p>Process code to automatically add import statements.</p>
	 * 
	 * <p>The code in each compilation unit is analysed to find
	 * fully-qualified references to other types defined within this
	 * project, or the classpath.  Where the unqualified names of these
	 * types are unambiguous, the code is altered to use an unqualified
	 * name; all suspected type references are also added to the list of
	 * package-level imports in the compilation unit, if not already
	 * there.</p>
	 * 
	 * <p>Note that in some future release, this method will be replaced
	 * with a more general way of performing post-processing on
	 * ActionScript code.</p>
	 */
	public void performAutoImport();
}