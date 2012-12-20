/*
 * ASForEachInStatement.java
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
 * A for-each-in statement, such as <code>for each(v in a) { }</code>.
 * 
 * <p>e.g. The Java code</p>
 * <pre class="eg">ASForEachInStatement forEachIn = method.newForEachIn("var v", "arr");
 *forEachIn.addStmt("trace(v)");</pre>
 *
 * <p>Will create ActionScript code like,</p>
 * <pre class="eg">for each(var v in arr) {
 *	trace(v);
 *}</pre>
 *
 * @see StatementContainer#newForEachIn(Expression, Expression)
 *
 * @author Alexander Eliseyev - added methods for iteration var access
 */ 
public interface ASForEachInStatement extends LabeledStatement, StatementContainer {

    boolean hasVarDeclaration();

	/**
	 * Returns a string representation of the loop-variable declaration
	 * for this loop.
	 * 
	 * e.g. given the loop <code>for each(v in a) { }</code>, this method
	 * will return the string <code>"v"</code>.
	 */
	public String getVarString();

    public String getLabel();

    @Deprecated
    public String getVarName();

    public Expression getIterExpression();

    ASDeclarationStatement getDeclarationStatement();

	/**
	 * Returns a string representation of the expression whose value will
	 * iterated over.
	 * 
	 * e.g. given the loop <code>for each(v in a) { }</code>, this method
	 * will return the string <code>"a"</code>.
	 */
	public String getIteratedString();

	public Expression getIterated();

	/**
	 * Specifies the loop-variable declaration for this loop.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setVar(String expr);

	/**
	 * Changes the expression whose value will be iterated over by this
	 * loop.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setIterated(String expr);

	public void setIterated(Expression expr);

    public Statement getBody();
}