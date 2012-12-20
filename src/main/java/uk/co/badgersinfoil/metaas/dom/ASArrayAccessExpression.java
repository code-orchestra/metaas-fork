/*
 * ASArrayAccessExpression.java
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
 * An array access, such as <code>a[1]</code>.
 * 
 * @see uk.co.badgersinfoil.metaas.ActionScriptFactory#newArrayAccessExpression(Expression, Expression)
 */
public interface ASArrayAccessExpression extends Expression {
	public Expression getTargetExpression();
	public void setTargetExpression(Expression expr);
	public Expression getSubscriptExpression();
	public void setSubscriptExpression(Expression expr);
}
