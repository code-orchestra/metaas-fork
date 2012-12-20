/*
 * ASReturnStatement.java
 * 
 * Copyright (c) 2007-2008 David Holroyd
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
 * A return statement, such as <code>return;</code> or <code>return res;</code>.
 * 
 * <p>Instances can be created using {@link StatementContainer#newReturn(Expression)}:</p>
 * <pre class="eg">method.newReturn();</pre>
 * <p>Will result in ActionScript code like,</p>
 * <pre class="eg">return;</pre>
 * <p>or, with an expression,this Java code,</p>
 * <pre class="eg">method.newReturn("doIt()");</pre>
 * <p>will result in ActionScript code like,</p>
 * <pre class="eg">return doIt();</pre>
 * 
 * @see StatementContainer#newReturn()
 * @see StatementContainer#newReturn(Expression)
 */
public interface ASReturnStatement extends Statement {

	/**
	 * Returns a string representation of the expression who's value
	 * this statement would return when executed, or null if there is
	 * no such expression.
	 */
	public String getExpressionString();

	public Expression getExpression();

    public List<Expression> getExpressions();

	/**
	 * Changes the expression that this statement would return when
	 * executed.  If null is given, any expression will be removed.
	 * 
	 * @throws SyntaxException if the given string is not null and is not
	 *         a valid ActionScript expression.
	 */
	public void setExpression(String expr);

	public void setExpression(Expression expr);
}