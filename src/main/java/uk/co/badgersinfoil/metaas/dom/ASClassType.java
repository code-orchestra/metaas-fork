/*
 * ASClassType.java
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

// TODO: add a createConstructor() ?

/**
 * A handle on the definition of an ActionScript class.
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newClass(String)
 */
public interface ASClassType extends ASType {

	List<Statement> getStaticInitStatementList();

    /**
	 * Returns the name of this ActionScript class' superclass, or null of
	 * the superclass is not specified.
	 */
	public String getSuperclass();

	/**
	 * Defines the name of the superclass for this ActionScript class.
	 */
	public void setSuperclass(String className);

	/**
	 * Returns the list of names of the interfaces that this ActionScript
	 * class implements.
	 */
	public List getImplementedInterfaces();

	/**
	 * Adds an interface name to the list of interfaces which this
	 * ActionScript class implements.
	 */
	public void addImplementedInterface(String string);

	/**
	 * Removes an interface name from the list of interfaces which this
	 * ActionScript class implements.
	 */
	public void removeImplementedInterface(String string);

	/**
	 * Adds a new ActionScript field definition to this ActionScript class.
	 */
	public ASField newField(String name, Visibility visibility, String type);

	/**
	 * Returns a reference to the ActionScript field with the given name,
	 * or null, if no such field exists.
	 */
	public ASField getField(String name);

	/**
	 * Returns a list of {@link ASField} objects representing the
	 * fields this ActionScript class defines.
	 */
    @Deprecated
	public List getFields();

    public List<ASField> getAllFields();

	/**
	 * Returns true if the class is defined with the <code>dynamic</code>
	 * modifier, and false otherwise.
	 */
	public boolean isDynamic();
	
	/**
	 * Returns true if the class is defined with the <code>final</code>
	 * modifier, and false otherwise.
	 */
	public boolean isFinal();

	/**
	 * Removes the named field from the list of fields which this
	 * ActionScript class defines.
	 */
	public void removeField(String name);
	
	/**
	 * Adds or removes the <code>dynamic</code> modifer.
	 */
	public void setDynamic(boolean value);
	
	/**
	 * Adds or removes the <code>final</code> modifer.
	 */
	public void setFinal(boolean value);
}