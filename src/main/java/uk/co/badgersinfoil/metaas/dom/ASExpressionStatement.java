/*
 * ASExpressionStatement.java
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
 * A simple statement which evaluates an expression.
 * 
 * <p>Instances can be created using {@link StatementContainer#newExprStmt(String)}:</p>
 * <pre class="eg">ASExpressionStatement stmt = method.newExprStmt("trace(\"hello world\")");</pre>
 * <p>Will result in the ActionScript code,</p>
 * <pre class="eg">trace("hello world");</pre>
 * 
 * @see StatementContainer#newExprStmt(Expression)
 */
public interface ASExpressionStatement extends Statement {

	/**
	 * Returns the expression this statement would evaluate when run.
	 */
    @Deprecated
	public Expression getExpression();

    List<Expression> getSubExpressions();
	
	/**
	 * Returns a string representation of the expression this statement
	 * would evaluate when run.
	 */
	public String getExpressionString();
	
	/**
	 * Changes the expression that this statement would evaluate when run.
	 * 
	 * @throws SyntaxException if the given string is not a valid
	 *         ActionScript expression.
	 */
	public void setExpression(String expr);
}
