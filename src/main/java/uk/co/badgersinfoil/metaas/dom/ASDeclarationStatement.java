/*
 * ASDeclarationStatement.java
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

// TODO: according to Adobe docs, a decl is a 'directive', not a statement

/**
 * A statement that declares variables, such as
 * <code>var a = 1;</code>.
 * 
 * <p>A declaration-statement may declare multiple variables, each optionally
 * specifying a type and an initialiser-value.</p>
 * 
 * @see StatementContainer#newDeclaration(Expression)
 */
public interface ASDeclarationStatement extends Statement, MetaTagable {


	/**
	 * Returns false if this is a declaration using the <code>var</code>
	 * keyword (the default), and true is this is a declaration using the
	 * <code>const</code> keyword.
	 */
	boolean isConstant();

	/**
	 * If given true, this declaration will use the <code>const</code>
	 * keyword, if given false, this declaration will use the
	 * <code>var</code> keyword.
	 */
	void setConstant(boolean constant);

	/**
	 * Returns a list of {@link ASVarDeclarationFragment} objects.
	 */
	List<ASVarDeclarationFragment> getVars();

	String getFirstVarName();

	TypeDescriptor getFirstVarType();

	Expression getFirstVarInitializer();
}