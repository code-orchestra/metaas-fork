/*
 * ASTExpression.java
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

package uk.co.badgersinfoil.metaas.impl;

import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTExpression extends ASTScriptElement implements Expression {

	public ASTExpression(LinkedListTree ast) {
		super(ast);
	}

	// equals()/hashCode() useful for unit tests

	public boolean equals(Object o) {
		if (o.getClass() != getClass()) {
			return false;
		}
		ASTExpression expr = (ASTExpression)o;
		return ast == expr.ast;
	}

	public int hashCode() {
		return ast.hashCode();
	}
}
