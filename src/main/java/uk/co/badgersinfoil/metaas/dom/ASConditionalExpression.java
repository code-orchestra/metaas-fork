/*
 * ASConditionalExpression.java
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

import uk.co.badgersinfoil.metaas.ActionScriptFactory;

/**
 * A 'conditional' (or 'ternary') expression, such as <code>a ? b : c</code>.
 * 
 * @see ActionScriptFactory#newConditionalExpression(Expression, Expression, Expression)
 */
public interface ASConditionalExpression extends Expression {

	public Expression getConditionExpression();

	public Expression getThenExpression();

	public Expression getElseExpression();

	public void setConditionExpression(Expression expr);

	public void setThenExpression(Expression expr);

	public void setElseExpression(Expression expr);
}
