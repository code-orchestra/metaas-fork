/*
 * ASTryStatement.java
 * 
 * Copyright (c) 2008 David Holroyd
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
 * A try-statement, such as <code>try {  } catch (e) {  }</code>.
 * 
 * <p>A statement container may have multiple catch-clauses and may have a
 * single finally-clause.  At either a catch-cluase or a finally-clause must
 * be present.</p>
 * 
 * <p>The body of the try-statement (the braces right after the
 * <code>try</code> keyword) can be accessed by calling the methods of
 * {@link StatementContainer} on the ASTryStatement instance.</p>
 * 
 * @see StatementContainer#newTryCatch(String, String)
 * @see StatementContainer#newTryFinally()
 */
public interface ASTryStatement extends StatementContainer, Statement, LabeledStatement {

	public ASCatchClause newCatchClause(String var, String type);

	/**
	 * Returns a list of {@link ASCatchClause}.
	 */
	public List getCatchClauses();

	public ASFinallyClause getFinallyClause();

	public ASFinallyClause newFinallyClause();

}
