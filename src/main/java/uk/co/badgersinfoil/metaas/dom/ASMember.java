/*
 * ASMember.java
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
 * A member of a type; an {@link ASMethod} or {@link ASField}.
 */
public interface ASMember extends ScriptElement, MetaTagable, Documentable {

    /**
     * @return namespace
     */
    String getNamespace();

    /**
	 * Returns the name of this field.
	 */
	public String getName();

	/**
	 * Changes the name of this ActionScript field to the given value.
	 */
	public void setName(String string);

	/**
	 * Returns the return type descriptor of value this ActionScript
	 * field may contain, or null if it is untyped.
	 */
	public TypeDescriptor getType();

	/**
	 * Defines the name of the type of object this ActionScript field may
	 * contain.  May be set to null, denoting that the value is
	 * untyped.
	 */
	public void setType(String string);

	/**
	 * Returns a value representing any protection-against-access defined
	 * for this ActionScript field.
	 */
	public Visibility getVisibility();

	/**
	 * Defines the level of protection-against-external-access for this
	 * ActionScript field.
	 */
	public void setVisibility(Visibility visibility);


	/**
	 * Returns true if this ActionScript field is static (i.e. the field
	 * definition uses the <code>static</code> keyword).
	 */
	public boolean isStatic();

	/**
	 * Defines this method to be static, or not.  If set to true, this
	 * member definition will be qualified with the <code>static</code>
	 * keyword, if false, the keyword will be absent.
	 */
	public void setStatic(boolean s);
}