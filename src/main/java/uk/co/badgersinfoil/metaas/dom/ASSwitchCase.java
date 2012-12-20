/*
 * ASSwitchCase.java
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
 * A switch-statement <code>case</code>-label, and the list of statements
 * immediately following it.
 * 
 * <p>For example, given the following code...</p>
 * 
 * <pre class="eg">switch (c) {
 *    <strong>case 1:
 *	trace("one");
 *	break;</strong>
 *    default:
 *	trace("many");
 *}</pre>
 * 
 * <p>...an ASSwitchCase would be available with label-value of <code>1</code>
 * which will provide the <code>trace("one");</code> and <code>break;</code>
 * statements when its {@link #getStatementList()} method is called.</p>
 * 
 * @see ASSwitchStatement#newCase(String)
 * @see ASSwitchStatement
 */
public interface ASSwitchCase extends SwitchLabel {

    List<Expression> getConditions();

	/**
	 * Returns a the value expression for this label.
	 */
    @Deprecated
	public Expression getLabelValue();
	    
	/**
	 * Returns a string representation of the value expression for this
	 * label.  e.g. given the label <code>case 42:</code>, this method
	 * would return the string <code>"42"</code>.
	 */
    @Deprecated
	public String getLabelValueString();

	/**
	 * Changes the value expression for this case label.
	 * 
	 * @throws uk.co.badgersinfoil.metaas.SyntaxException if the given
	 * string is not a valid ActionScript expression.
	 */
	public void setLabelValue(String constant);
}