/*
 * ASWhileStatement.java
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
 * A while-loop, such as <code>while (test()) {  }</code>.
 * 
 * <p>e.g. The Java code...</p>
 * <pre class="eg">ASWhileStatement whileStmt = method.newWhile("test()");
 *whileStmt.addStmt("trace('hello')");</pre>
 *
 * <p>...will produce the ActionScript code...</p>
 * <pre class="eg">while (test()) {
 *	trace('hello');
 *}</pre>
 *
 * @see StatementContainer#newWhile(Expression)
 */
public interface ASWhileStatement extends LabeledStatement, StatementContainer {

    public String getLabel();
	public String getConditionString();

    @Deprecated
    public Expression getCondition();
    public void setCondition(String expr);
	public void setCondition(Expression expr);

    public Statement getBody();

    List<Expression> getConditions();
}
