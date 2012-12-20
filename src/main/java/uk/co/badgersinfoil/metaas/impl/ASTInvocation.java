/*
 * ASTInvocation.java
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

import java.util.List;
import uk.co.badgersinfoil.metaas.dom.Expression;
import uk.co.badgersinfoil.metaas.dom.Invocation;
import uk.co.badgersinfoil.metaas.impl.antlr.LinkedListTree;

public class ASTInvocation extends ASTExpression implements Invocation {
	
	public ASTInvocation(LinkedListTree ast) {
		super(ast);
	}

	public List getArguments() {
		LinkedListTree args = ArgumentUtils.findArgs(ast);
		return ArgumentUtils.astToExpressionList(args);
	}

	public Expression getTargetExpression() {
		return ExpressionBuilder.build(ast.getFirstChild());
	}

	public void setArguments(List args) {
		LinkedListTree argumentNode = ArgumentUtils.findArgs(ast);
		ArgumentUtils.overwriteArgsWithExpressionList(argumentNode, args);
	}

	public void setTargetExpression(Expression expr) {
		LinkedListTree exprAST = ((ASTExpression)expr).getAST();
		ast.setChildWithTokens(0, exprAST);
	}
}
