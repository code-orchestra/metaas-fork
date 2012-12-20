/*
 * ASTASPostfixExpression.java
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
import uk.co.badgersinfoil.metaas.dom.ASPostfixExpression;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTASPostfixExpression extends ASTExpression implements
		ASPostfixExpression {

	public ASTASPostfixExpression(LinkedListTree ast) {
		super(ast);
	}

	public Op getOperator() {
		return PostfixOperatorMap.opFromType(ast.getType());
	}

	public Expression getSubexpression() {
		return ExpressionBuilder.build(ast.getFirstChild());
	}

	public void setOperator(Op operator) {
		PostfixOperatorMap.initialiseFromOp(operator, ast.getToken());
	}

	public void setSubexpression(Expression subexpression) {
		ASTExpression sub = (ASTExpression)subexpression;
		// TODO: handle operator precidence issues
		ast.setChildWithTokens(0, sub.getAST());
	}
}