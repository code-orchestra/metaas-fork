/*
 * ASObjectLiteral.java
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
 * An object-literal-expression, such as <code>{a: "b", c: 2}</code>.
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newObjectLiteral()
 */
public interface ASObjectLiteral extends Literal {

	/**
	 * Returns a list of {@link ASObjectLiteral.Field} objects.
	 */
	public List getFields();

	public Field newField(String name, Expression value);

	/**
	 * A field within an object literal
	 */
	public interface Field extends ScriptElement {

		// TODO: numbers are allowed for field-keys too?

		public String getName();

		public Expression getValue();
	}
}
