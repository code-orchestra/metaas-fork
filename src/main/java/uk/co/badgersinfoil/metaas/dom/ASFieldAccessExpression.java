/*
 * ASFieldAccessExpression.java
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

/**
 * An expression that accesses a field of an object, such as <code>person().name</code>.
 * 
 * <p><strong>NB</strong> when parsing ActionScript code, it is not possible to
 * determine if some expressions are field-accesses or package-quilified-names
 * (without <em>semantic</em> analysis, which metaas doesn't do).  Attempting to
 * parse an expression like <code>a.B</code> is therefore likely to produce
 * an instance of ASFieldAccessExpression, even if it should actually be a
 * package-qualified reference to a definition elsewhere.</p>
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newFieldAccessExpression(Expression, String)
 */
public interface ASFieldAccessExpression extends Expression {
	String getNamespace();
    public String getName();
	public void setName(String name);
	public Expression getTargetExpression();
	public void setTargetExpression(Expression expr);
    public boolean isLineBreak();
}
