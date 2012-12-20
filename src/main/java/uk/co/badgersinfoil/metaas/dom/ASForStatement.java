/*
 * ASForStatement.java
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
 * A <code>for</code> statement, such as <code>for (; ; ) { }</code>.
 * 
 * <p>e.g. The Java code</p>
 * <pre class="eg">ASForStatement forStmt = method.newFor("var i=0", "i<10", "i++");
 *forStmt.addStmt("trace(i)");</pre>
 *
 * <p>Will create ActionScript code like,</p>
 * <pre class="eg">for (var i=0; i<10; i++) {
 *	trace(i);
 *}</pre>
 *
 * @see StatementContainer#newFor(Expression, Expression, Expression)
 */
public interface ASForStatement extends LabeledStatement, StatementContainer {

	/**
	 * Returns a string representation of the loop initialisation
	 * expression.
	 * 
	 * e.g. given the loop <code>for (var i=0; i<10; i++)</code>, this
	 * method will return the string <code>"var i=0"</code>.
	 */
	public String getInitString();

    public String getLabel();

	/**
	 * Returns a script element representing the initialisation part of
	 * this for-statement.
	 * 
	 * <p>The return value depends on the kind of initialiser present:</p>
	 * 
	 * <dl>
	 * <dt>No initialiser: <code>for (; ; )</code></dt>
	 * <dd>Returns <code>null</code></dd>
	 * 
	 * <dt>Expression initialiser: <code>for (v=1; ; )</code></dt>
	 * <dd>Returns an {@link Expression}</dd>
	 * 
	 * <dt>Declaration initialiser: <code>for (var v=1; ; )</code></dt>
	 * <dd>Returns an {@link ASDeclarationStatement}</dd>
	 * </dl>
	 */
	public ScriptElement getInit();

	/**
	 * Returns a string representation of the loop termination condition
	 * expression.
	 * 
	 * e.g. given the loop <code>for (var i=0; i<10; i++)</code>, this
	 * method will return the string <code>"i<10"</code>.
	 */
	@Deprecated
    public String getConditionString();

	List<Expression> getConditions();

    @Deprecated
    public Expression getCondition();

	/**
	 * Returns a string representation of the loop update expression.
	 * 
	 * e.g. given the loop <code>for (var i=0; i<10; i++)</code>, this
	 * method will return the string <code>"i++"</code>.
	 */
    @Deprecated
	public String getUpdateString();

    @Deprecated
	public Expression getUpdate();

    List<Expression> getUpdates();

	/**
	 * Changes the initialisation expression for this loop.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setInit(String expr);

	/**
	 * Changes the termination condition expression for this loop.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setCondition(String expr);

	public void setCondition(Expression expr);

	/**
	 * Changes the update expression for this loop.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setUpdate(String expr);

	public void setUpdate(Expression expr);

    public Statement getBody();
}