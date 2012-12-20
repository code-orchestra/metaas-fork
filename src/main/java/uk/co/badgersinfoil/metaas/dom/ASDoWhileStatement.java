/*
 * ASDoWhileStatement.java
 * 
 * Copyright (c) 2007 David Holroyd
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
 * A do-while loop, such as <code>do {  } while (condition);</code>.
 * 
 * <p>e.g. The following Java code,</p>
 * <pre class="eg">ASDoWhileStatement doWhile = method.newDoWhile("test()");
 *doWhile.addStmt("trace('still testing')");</pre>
 * <p>Will result in AS which looks like this,</p>
 * <pre class="eg">do {
 *	trace('still testing');
 *} while (test());</pre>
 *
 * @see StatementContainer#newDoWhile(String)
 */
public interface ASDoWhileStatement extends LabeledStatement, StatementContainer {

	/**
	 * Returns a string representation of the loop termination condition
	 * expression.
	 * 
	 * <p>e.g. given the loop,</p>
	 * <pre class="eg">do { nothing(); } while (test());</pre>
	 * <p>This method would return <code>"test()"</code>.</p>
	 */
	public String getConditionString();

    public Statement getBody();

    public String getLabel();

	public Expression getCondition();

	/**
	 * Sets the loop termination condition for this do-while loop.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setCondition(String expr);

	public void setCondition(Expression expr);

    List<Expression> getConditions();
}