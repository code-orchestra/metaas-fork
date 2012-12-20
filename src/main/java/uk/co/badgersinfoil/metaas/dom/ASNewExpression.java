/*
 * ASNewExpression.java
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
 * A constructor invocation, such as <code>new MyThing()</code>.
 * 
 * <p>It is legal for a constructor  invocation to appear without arguments
 * (the parenthesis can be dropped e.g <code>new Ctor</code>, rather than
 * <code>new Ctor()</code>), and in this case, getArguments() will return null.
 * If the argument list is present, but empty, an empty List will be returned
 * instead.</p>
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newNewExpression(Expression, java.util.List)
 */
public interface ASNewExpression extends Expression, Invocation {
}
