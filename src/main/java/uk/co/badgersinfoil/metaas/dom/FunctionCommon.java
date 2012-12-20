/*
 * FunctionCommon.java
 * 
 * Copyright (c) 2008 David Holroyd
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
 * Common interface for {@link ASMethod} and {@link ASFunctionExpression}.
 */
public interface FunctionCommon {

	/**
	 * Returns a list of the formal arguments accepted by this ActionScript
	 * method.  Note that the resulting list is not modifiable.
	 * 
	 * @see #addParam(String, String)
	 */
	public List getArgs();

	/**
	 * Returns the name of the return type of this ActionScript
	 * method, or null if it is untyped.
	 */
	public TypeDescriptor getType();

	/**
	 * Defines the name of the type of object returned by this ActionScript
	 * method.  May be set to null, denoting that the return value is
	 * untyped.
	 */
	public void setType(String string);

	/**
	 * Adds a formal parameter to the list of parameters supported by this
	 * ActionScript method.
	 * 
	 * @param name The name for the parameter
	 * @param type The type for the parameter, or null if the parameter is
	 *        to be untyped
	 * @return the newly created parameter
	 * 
	 * @see #addRestParam(String)
	 */
	public ASArg addParam(String name, String type);

	/**
	 * Adds a 'rest' parameter to the list of parameters supported by this
	 * ActionScript method.
	 * 
	 * <p>e.g. The java code,</p>
	 * <pre class="eg">ASMethod meth = class.newMethod("test", Visibility.PUBLIC, "void");
	 *meth.addRestParam("foo");</pre>
	 * <p>will result in ActionScript code like,</p>
	 * <pre class="eg">public function test(...foo):void {
	 *}</pre>
	 * 
	 * <p>To create an anonymous 'rest' parameter, pass the name "..."
	 * (this name can also be passed to removeParam() to remove an
	 * anonymous rest parameter).</p>
	 * 
	 * @return the newly created parameter
	 * 
	 * @see #addParam(String,String)
	 */
	public ASArg addRestParam(String name);

	/**
	 * Removes the named parameter from this ActionScript methods formal
	 * parameter list.  In the case of a 'rest' parameter, give the name
	 * of the parameter without the ellipsis prefix (i.e. if the parameter
	 * is declared as "...foo", pass the name "foo" to this method).
	 * To remove an anonymous 'rest' parameter, pass the string "..." as
	 * the argument to this method.
	 * 
	 * @return the removed parameter, if found, or null otherwise.
	 */
	public ASArg removeParam(String string);
}
