/*
 * CompilationUnit.java
 * 
 * Copyright (c) 2006 David Holroyd
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

package uk.co.badgersinfoil.metaas.dom;

import java.util.List;

/**
 * A 'compilation unit' represents an entire file of ActionScript code.
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptProject#newClass(String)
 * @see uk.co.badgersinfoil.metaas.ActionScriptProject#newInterface(String)
 */
public interface ASCompilationUnit extends ScriptElement {



    /**
	 * Returns the name of the package, or null if the contents of the file
	 * are in the 'default' (top-level) package.
	 */
	String getPackageName();

    List<ScriptElement> getAllMembers();
    
    ASMethod getGlobalFunction();

    ASField getGlobalField();

	/**
	 * Returns the type ({@link ASClassType} or {@link ASInterfaceType})
	 * which this file defines.
	 */
	ASType getType();

    ASNamespaceDeclaration getGlobalNamespaceDeclaration();

    List<ASField> getOutOfPackageFields();

    List<ASMethod> getOutOfPackageFunctions();

    List<ASType> getOutOfPackageTypes();

    List<String> getOutOfPackageImports();

    List<Statement> getOutOfPackageStatements();

    List<ASNamespaceDeclaration> getOutOfPackageNamespaces();

	/**
	 * Modifies the name of the package containing the code of this
	 * compilation-unit.  Passing null causes the resulting code to be in
	 * the 'default' (top-level) package.
	 */
	void setPackageName(String name);

	/**
	 * Returns the first ActionScript package-block declaired in this
	 * compilation-unit.
	 */
	ASPackage getPackage();
}