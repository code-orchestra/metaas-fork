/*
 * ASIfStatement.java
 * 
 * Copyright (c) 2006-2008 David Holroyd
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

import uk.co.badgersinfoil.metaas.SyntaxException;

import java.util.List;

/**
 * An if-statement, such as <code>if (a) { doSomething(); }</code>.
 * 
 * <p>ASIfStatement is a {@link StatementContainer} to simplify the common
 * case where the attached statement (as returned by getThenStatement()) is
 * a block (very handy if you are <em>generating</em> code using metaas).  If
 * the attached statement is not a block, attempting to call StatementContainer
 * methods directly on this if-statement will fail with an exception, so it is
 * generally required to call getThenStatement() rather than attempting to
 * use StatementContainer methods on the ASIfStatement, or code which was
 * parsed.</p>
 * 
 * <p>e.g. To simply add statements to the 'then' branch (executed when
 * the condition holds true),</p>
 * <pre class="eg">
 * ASIfStatement ifStmt = method.newIf("test()");
 * isStmt.addStmt("trace('test succeeded')");</pre>
 * <p>will result in ActionScript code like,</p>
 * <pre class="eg">
 * if (test()) {
 * 	trace('test succeeded');
 * }</pre>
 * 
 * <p>To add code to both 'then' and 'else' branches,</p>
 * <pre class="eg">
 * ASIfStatement ifStmt = method.newIf("test()");
 * ifStmt.addStmt("trace('test succeeded')");
 * ifStmt.elseBlock().addStmt("trace('test failed')");</pre>
 * <p>will result in ActionScript code like,</p>
 * <pre class="eg">
 * if (test()) {
 * 	trace('test succeeded');
 * } else {
 * 	trace('test failed');
 * }</pre>
 * 
 * <p>Note that the first call to elseBlock() will cause the else-clause to be
 * created with a block attached to it (even if no statements are subsequently
 * added).  Subsequent calls to elseBlock() will return references to the same
 * block, rather than creating further code.</p>
 * 
 * @see StatementContainer#newIf(Expression)
 */
public interface ASIfStatement extends Statement, StatementContainer, LabeledStatement {

	/**
	 * @deprecated use {@link #elseBlock()}.
	 */
	public ASBlock getElse();

	/**
	 * Returns a reference to an object which can populate the else-clause
	 * of this ActionScript if-statement with new code.  If no else-clause
	 * is attached to this if-statement, one will be automatically added as
	 * a result of calling this method.
	 * 
	 * @throws SyntaxException if this if-statement already has an
	 *         else-clause attached and the statement in the else-clause is
	 *         something other than a block-statement.
	 */
	public ASBlock elseBlock();

	/**
	 * Returns the statement attached to the else-clause of this
	 * if-statement, or null if no else-clause is present.
	 */
	public Statement getElseStatement();

	public Statement getThenStatement();

	public void setThenStatement(Statement then);

	/** @deprecated Use {@link #setThenStatement(Statement)} */
	public void setThen(ASBlock thenBlock);

	/**
	 * Returns a string representation of the condition-expression for
	 * this if-statement.  e.g. for the expression
	 * <code>if (test()) { }</code>, this method will return the string
	 * <code>"test()"</code>.
	 */
    @Deprecated
	String getConditionString();

    List<Expression> getConditions();

    @Deprecated
	public Expression getCondition();

	/**
	 * Changes the condition-expression for this if-statement.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setCondition(String expr);

	public void setCondition(Expression expr);
}