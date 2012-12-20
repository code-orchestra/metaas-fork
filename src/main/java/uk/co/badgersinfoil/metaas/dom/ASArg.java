/*
 * ASArg.java
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

/**
 * A parameter in a method or function definition.
 * 
 * @see FunctionCommon#addParam(String, String)
 * @see FunctionCommon#getArgs()
 */
public interface ASArg extends ScriptElement {

    Expression getInitializer();

    /**
	 * Returns the name of this ActionScript method argument
	 */
	public String getName();
	/**
	 * Returns the name of this parameter's type, or null if the parameter
	 * is untyped.
	 */
	public TypeDescriptor getType();

	/**
	 * Defines the name of the type of object this parameter may
	 * reference.  May be set to null, denoting that the value is
	 * untyped.
	 */
	public void setType(String string);
	
	/**
	 * Specifies the compile-time-constant value that will be the default
	 * for the argument if no value is provided by the calling code.
	 */
	public void setDefault(String string);

	/**
	 * Returns a String representation of the default value for the
	 * argument (if no value is provided by the calling code), or null if
	 * there is no default.
	 * 
	 * @see #setDefault(String)
	 */
	public String getDefaultString();

	/**
	 * Returns true if this is a 'rest' parameter; an array that will hold
	 * remaning arguments passed after the other formal arguments.  A 'rest'
	 * argument should always be the last one declared in the parameter
	 * list of a function or method.
	 */
	public boolean isRest();

	/**
	 * Returns the description of this parameter from the documentation
	 * comment attached to the enclosing method, or null, if there is no
	 * such comment, or it lacks a @<span/>param paragraph with a name
	 * matching this parameter.
	 */
	public String getDescriptionString();
	
	/**
	 * Defines the descriptive text for for this parameter in the
	 * documentation comment attached to the enclosing method.  If null is
	 * given, any descriptive @<span/>param paragraph with a name
	 * matching this parameter will be removed from the method's
	 * documentation.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 *         text contains an end-of-comment marker, or appears to
	 *         include another tagged paragraph
	 * @see DocComment
	 */
	public void setDescription(String description);
}