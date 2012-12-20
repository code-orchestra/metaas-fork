/*
 * ASTASConditionalExpression.java
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

import uk.co.badgersinfoil.metaas.dom.ASConditionalExpression;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASConditionalExpression extends ASTExpression implements ASConditionalExpression {

	private static final int INDEX_COND = 0;
	private static final int INDEX_THEN = 1;
	private static final int INDEX_ELSE = 2;

	public ASTASConditionalExpression(LinkedListTree ast) {
		super(ast);
	}

	public Expression getConditionExpression() {
		return ExpressionBuilder.build(ast.getFirstChild());
	}

	public Expression getThenExpression() {
		return ExpressionBuilder.build((LinkedListTree)ast.getChild(INDEX_THEN));
	}

	public Expression getElseExpression() {
		return ExpressionBuilder.build(ast.getLastChild());
	}

	public void setConditionExpression(Expression expr) {
		ast.setChildWithTokens(INDEX_COND, ast(expr));
	}

	public void setThenExpression(Expression expr) {
		ast.setChildWithTokens(INDEX_THEN, ast(expr));
	}

	public void setElseExpression(Expression expr) {
		ast.setChildWithTokens(INDEX_ELSE, ast(expr));
	}
}
