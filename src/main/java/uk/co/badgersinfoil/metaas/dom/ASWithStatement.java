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

/**
 * A with-statement, such as <code>with (expr) { }</code>.
 * 
 * <p>e.g.</p>
 * <pre class="eg">ASWithStatement stmt = method.newWith("myObject");
 *stmt.newExprStmt("trace(property)");</pre>
 * <p>will result in ActionScript code like,</p>
 * <pre class="eg">with (myObject)) {
 * 	trace(property);
 *}</pre>
 *
 * @see StatementContainer#newWith(Expression)
 */
public interface ASWithStatement extends Statement, StatementContainer {

	/**
	 * Returns a string representation of the expression who's value
	 * will be used as new scope for the execution of the statements in
	 * the body of this with-statement.
	 */
	public String getScopeString();

	public Expression getScope();

	/**
	 * Changes the expression who's value will be used as new scope for the
	 * execution of the statements in the body of this with-statement.
	 * 
	 * @throws SyntaxException if the given string is not a valid
	 *         ActionScript expression.
	 */
	public void setScope(String expr);

	public void setScope(Expression expr);

	public Statement getBody();
}