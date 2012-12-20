/*
 * ASType.java
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

package uk.co.badgersinfoil.metaas.dom;

import java.util.List;


/**
 * Superinterface for {@link ASClassType} and {@link ASInterfaceType}.
 */
public interface ASType extends ScriptElement, MetaTagable, Documentable, CommentableBefore {

    List<ASIncludeDirective> getIncludeDirectives();

    /**
      * @return type's namespace declaration or null if not any present
     */
    List<ASNamespaceDeclaration> getNamespaceDeclarations();

    List<ASUseNamespaceStatement> getUseNamespaceDirectives();

    List findImports();

    /**
	 * Returns the name of this class or interface, excluding any package
	 * prefix.
	 * 
	 * TODO: maybe the prefix should be included, and the getPackage() in
	 *       CompilationUnit should be dropped, to mask the diff between
	 *       AS2 and AS3.
	 */
	public String getName();

	/**
	 * Sets the name by which this type is identified.
	 */
	public void setName(String name);

	/**
	 * Returns a list of {@link ASMethod} objects.  Don't attempt to change
	 * the type by modifying the list.
	 */
	public List getMethods();

	/**
	 * Returns a reference to the ActionScript method with the given name,
	 * or null, if no such method exists.
	 */
	ASMethod getMethod(String name);

	/**
	 * Creates a new ActionScript method definition, adds it to the list of
	 * methods supported by this tyoe, and returns a reference to the
	 * method.
	 * 
	 * <p>Note that the methods of an {@link ASInterfaceType} cannot have
	 * statements added to them, though this API doesn't make the
	 * restriction apparent.</p>
	 * 
	 * @param name The name of the method to be created
	 * @param visibility an object representing the alloed access to the
	 *        new method by other ActionScript code
	 * @param returnType The name of this method's return type, or null, if
	 *        this method's return value is untyped.
	 */
	public ASMethod newMethod(String name, Visibility visibility, String returnType);
	
	/**
	 * Removes the named ActionScript method from the list of methods
	 * supported by this ActionScript class.
	 */
	public void removeMethod(String name);

	/**
	 * Returns an object representing the visibility of this ActionScript
	 * type definition within the enclosing ActionScript3 package.
	 */
	public Visibility getVisibility();

	/**
	 * Defines the visibility of this ActionScript type definition within
	 * the enclosing ActionScript3 package.
	 */
	public void setVisibility(Visibility visibility);
}