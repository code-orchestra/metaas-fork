/*
 * ASSwitchStatement.java
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

import java.util.List;

/**
 * A switch-statement, such as <code>switch (c) {  }</code>.
 * 
 * <p>Allows <code>case</code> and <code>default</code> statements to be added
 * to to the switch-statement body.
 * 
 * e.g.
 * <pre class="eg">
 * ASSwitchStatement switchStmt = method.newSwitch("c");
 * switchStmt.newCase("'a'").addStmt("aay()");
 * switchStmt.newCase("'b'").addStmt("bee()");
 * switchStmt.newDefault().addStmt("cee()");
 * </pre>
 * 
 * Will result in ActionScript something like,
 * <pre class="eg">
 * switch(c) {
 * 	case 'a':
 * 		aay();
 * 	case 'b':
 * 		bee();
 * 	default:
 * 		cee();
 * }
 * </pre>
 * 
 * <p>To add <code>break</code> statements to either <code>case</code> or
 * <code>default</code> labels, use <code>switchLabel.newBreak()</code>.</p>
 * 
 * @see StatementContainer#newSwitch(String)
 * @see ASSwitchCase
 * @see ASSwitchDefault
 */
public interface ASSwitchStatement extends LabeledStatement {


	/**
	 * Creates a <code>case</code>-label in this switch-statement with the given value,
	 * to which other statements can be added.
	 */
	ASSwitchCase newCase(String string);

    public String getLabel();

	// TODO: newCase(ASExpression / ASLiteral)?

	/**
	 * Creates a <code>default</code>-label in this switch-statement, to
	 * which other statements can be added.
	 */
	ASSwitchDefault newDefault();

    List<Expression> getConditions();

    @Deprecated
	Expression getCondition();
	@Deprecated
    public void setCondition(Expression expr);

	/**
	 * Returns a list of {@link SwitchLabel} elements (i.e. either
	 * {@link ASSwitchCase} or {@link ASSwitchDefault}).
	 */
	List getLabels();
}
